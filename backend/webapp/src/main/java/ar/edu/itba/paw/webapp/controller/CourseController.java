package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.CommentDTO;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.dto.form.ClassReservationForm;
import ar.edu.itba.paw.webapp.dto.form.CommentForm;
import ar.edu.itba.paw.webapp.dto.form.CourseForm;
import ar.edu.itba.paw.webapp.dto.form.MessageForm;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


//TODO: Chequear badRequest en resultados paginados
@Path("courses")
@Component
public class CourseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassReservationService classReservationService;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("{professor}_{subject}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response course(@PathParam("professor") final long professorId,
                           @PathParam("subject") final long subjectId){

        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.debug("Creating view for Course with professor id {} and subject id {}", professorId, subjectId);

        return Response.ok(new CourseDTO(course, uriInfo.getBaseUri())).build();
    }

    @GET
    @Path("{professor}_{subject}/comments")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response comments(@PathParam("professor") final long professorId,
                             @PathParam("subject") final long subjectId,
                             @DefaultValue("1") @QueryParam("page") final int page) {

        final Course course = courseService.findCourseByIds(professorId, subjectId);

        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final PagedResults<Comment> comments = courseService.getComments(course, page);

        if(comments == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, comments);

        final GenericEntity<List<CommentDTO>> entity = new GenericEntity<List<CommentDTO>>(
                comments.getResults().stream()
                        .map(comment -> new CommentDTO(comment, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response courses(@DefaultValue("") @QueryParam("q") final String query,
                            @QueryParam("start") final Integer startHour,
                            @QueryParam("end") final Integer endHour,
                            @QueryParam("min") final Double minPrice,
                            @QueryParam("max") final Double maxPrice,
                            @QueryParam("days") final List<Integer> days,
                            @DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Course> courses = courseService.filterCourses(days, startHour, endHour,
                minPrice, maxPrice, query, page);

        if(courses == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, courses);

        final GenericEntity<List<CourseDTO>> entity = new GenericEntity<List<CourseDTO>>(
                courses.getResults().stream()
                        .map(course -> new CourseDTO(course, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @POST
    @Path("{professor}_{subject}/contact")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response contact(@PathParam("professor") final long professorId,
                            @PathParam("subject") final long subjectId,
                            @Valid final MessageForm message) {

        final User loggedUser = loggedUser();
        final Conversation conversation;

        try {
            conversation = conversationService.sendMessage(loggedUser.getId(), professorId, subjectId, message.getMessage());
        } catch (SameUserException e) {
            final ValidationErrorDTO error = getErrors("SameUserMessageError");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NonexistentConversationException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(conversation == null) {
            final ValidationErrorDTO error = getErrors("SendMessageError");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

        final URI uri = uriInfo.getBaseUriBuilder().path("/conversations/" + conversation.getId()).build();
        return Response.created(uri).build();
    }

    @POST
    @Path("{professor}_{subject}/comments")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response comment(@Valid final CommentForm comment,
                            @PathParam("professor") final long professorId,
                            @PathParam("subject") final long subjectId) {

        final User loggedUser = loggedUser();

        final boolean sent;
        try {
            sent = courseService.comment(loggedUser.getId(), professorId, subjectId,
                    comment.getCommentBody(), comment.getRating());
        } catch (SameUserException e) {
            final ValidationErrorDTO error = getErrors("sameUserComment");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } catch (NonAcceptedReservationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(!sent) {
            final ValidationErrorDTO error = getErrors("SendMessageError");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(uri).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response create(@Valid final CourseForm form) {

        final User user = loggedUser();

        final Course course;
        try {
            course = courseService.create(user.getId(), form.getSubject(), form.getDescription(), form.getPrice());
        } catch (CourseAlreadyExistsException e) {
            final ValidationErrorDTO error = getErrors("courseAlreadyExists");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NonexistentSubjectException e) {
            final ValidationErrorDTO error = getErrors("subjectDoesNotExist");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if(course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        LOGGER.debug("Posting request for course creation for professor with id {} in subject with id {}", user.getId(), form.getSubject());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId()) +
                "_" + String.valueOf(form.getSubject())).build();
        return Response.created(uri).build();
    }

    @POST
    @Path("{professor}_{subject}/reservations")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reserveClass(@PathParam("professor") final long professorId,
                                 @PathParam("subject") final long subjectId,
                                 @Valid final ClassReservationForm form) {

        final User currentUser = loggedUser();

        if(!form.validForm()) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "profile.add_schedule.timeError", "endHour");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        }

        final LocalDate day = new LocalDate(form.getDay());

        final LocalDateTime startTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getStartHour(), 0);

        final LocalDateTime endTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getEndHour(), 0);

        if(!startTime.isAfter(LocalDateTime.now())) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "futureDateError", "day");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        }

        final ClassReservation reservation;
        try {
            reservation = classReservationService.reserve(startTime, endTime,
                    professorId, subjectId, currentUser.getId());
        } catch (SameUserException e) {
            final ValidationErrorDTO error = getErrors("sameUserReservation");
            return Response.status(Response.Status.FORBIDDEN).entity(error).build();
        } catch (NonexistentCourseException e) {
            final ValidationErrorDTO error = getErrors("nonExistentCourse");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } catch (NonExistentUserException e) {
            final ValidationErrorDTO error = getErrors("nonExistentUser");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        } catch (ReservationTimeOutOfRange reservationTimeOutOfRange) {
            final ValidationErrorDTO error = getErrors("notAvailableTime");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if(reservation == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI uri = uriInfo.getBaseUriBuilder().path("/user/reservations/" + reservation.getClassRequestId()).build();

        return Response.created(uri).build();
    }


    @PUT
    @Path("{professor}_{subject}")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response modify(@Valid final CourseForm form,
                           @PathParam("professor") final long professorId,
                           @PathParam("subject") final long subjectId) {

        final User user = loggedUser();

        final Course course;
        try {
            course = courseService.modify(user.getId(), subjectId, form.getDescription(), form.getPrice());
        } catch (NonexistentCourseException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(course == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        LOGGER.debug("Posting request for course modification for professor with id {} in subject with id {}", user.getId(), subjectId);
        return Response.ok().build();
    }

    @DELETE
    @Path("{professor}_{subject}")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response delete(@PathParam("professor") final long professorId,
                           @PathParam("subject") final long subjectId) {

        final User user = loggedUser();

        final boolean deleted;
        deleted = courseService.deleteCourse(user.getId(), subjectId);

        if(!deleted) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }

}
