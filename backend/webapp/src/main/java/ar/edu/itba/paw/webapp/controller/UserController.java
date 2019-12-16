package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.JwtTokenManager;
import ar.edu.itba.paw.webapp.auth.SecurityConstants;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.form.*;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("user")
@Component
public class UserController extends BaseController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ClassReservationService classReservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtTokenManager tokenManager;

    @Context
    private UriInfo uriInfo;


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response profile() {

        final User loggedUser = loggedUser();
        final Professor professor = professorService.findById(loggedUser.getId());

        if(professor == null) {
            return Response.ok(new UserDTO(loggedUser, uriInfo.getBaseUri(), false)).build();
        }
        return Response.ok(new ProfessorDTO(professor, uriInfo)).build();
    }

    @PUT
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA, })
    public Response modify(@Valid @BeanParam final EditProfessorProfileForm form) {

        final User loggedUser = loggedUser();
        final Professor professor;

        try {
            professor = professorService.modify(loggedUser.getId(), form.getDescription(),
                    form.getPictureBytes());
        } catch (DownloadFileException e) {
            final ValidationErrorDTO error = getErrors("fileUploadError");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } catch (NonexistentProfessorException | ProfessorWithoutUserException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(new ProfessorDTO(professor, uriInfo)).build();
    }

    @GET
    @Path("/courses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response courses(@DefaultValue("1") @QueryParam("page") final int page) {

        final User loggedUser = loggedUser();

        final PagedResults<Course> results = courseService.findCourseByProfessorId(loggedUser.getId(), page);

        if(results == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, results);

        final GenericEntity<List<CourseDTO>> entity = new GenericEntity<List<CourseDTO>>(
                results.getResults().stream()
                        .map(course -> new CourseDTO(course, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    //TODO: Might need to be revised
    @GET
    @Path("/schedule")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response schedule(){
        final User loggedUser = loggedUser();
        final Professor professor = professorService.findById(loggedUser.getId());

        if(professor == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Schedule schedule = scheduleService.getScheduleForProfessor(loggedUser.getId());
        return Response.ok(new ScheduleDTO(professor, schedule, uriInfo)).build();
    }

    @POST
    @Path("/schedule")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response createTimeslot(@Valid final ScheduleForm form) {

        final User currentUser = loggedUser();

        if(!form.validForm()) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "profile.add_schedule.timeError", "endHour");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        }

        final List<Timeslot> timeslots;
        try {
            timeslots = scheduleService.reserveTimeSlot(currentUser.getId(), form.getDay(),
                    form.getStartHour(), form.getEndHour());
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (TimeslotAllocatedException e) {
            final ValidationErrorDTO error = getErrors("TimeslotAllocatedError");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if(timeslots == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI uri = uriInfo.getBaseUriBuilder().path("/user/schedule").build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/schedule")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response RemoveTimeslot(@Valid final ScheduleForm form) {

        final User loggedUser = loggedUser();

        if(!form.validForm()) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "profile.add_schedule.timeError", "endHour");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        }

        final boolean removed;

        try {
            removed = scheduleService.removeTimeSlot(loggedUser.getId(),
                    form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(!removed) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/reservations")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservations(@DefaultValue("1") @QueryParam("page") final int page,
                                 @DefaultValue("false") @QueryParam("fullDetail") final boolean fullDetail) {
        final User loggedUser = loggedUser();

        final PagedResults<ClassReservation> classReservations =  userService.pagedReservations(loggedUser.getId(), page);

        if(classReservations == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, classReservations);

        final GenericEntity<?> entity;
        if (fullDetail) {
            entity = new GenericEntity<List<FullDetailClassReservationDTO>>(
                    classReservations.getResults().stream()
                            .map(reservation -> new FullDetailClassReservationDTO(reservation, uriInfo))
                            .collect(Collectors.toList())
            ) { };
        } else {
            entity = new GenericEntity<List<ClassReservationDTO>>(
                    classReservations.getResults().stream()
                            .map(reservation -> new ClassReservationDTO(reservation, uriInfo))
                            .collect(Collectors.toList())
            ) { };
        }

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/reservations/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservation(@PathParam("id") final long id,
                                @DefaultValue("false") @QueryParam("fullDetail") final boolean fullDetail) {
        final User loggedUser = loggedUser();

        final ClassReservation classReservation = classReservationService.findById(id);

        if(classReservation == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if(!classReservation.getStudent().getId().equals(loggedUser.getId())) {
            final ValidationErrorDTO error = getErrors("sameUserReservation");
            return Response.status(Response.Status.FORBIDDEN).entity(error).build();
        }

        if (fullDetail) {
            return Response.ok(new FullDetailClassReservationDTO(classReservation, uriInfo)).build();
        }

        return Response.ok(new ClassReservationDTO(classReservation, uriInfo)).build();
    }

    @GET
    @Path("/requests")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response requests(@DefaultValue("1") @QueryParam("page") final int page,
                             @DefaultValue("false") @QueryParam("fullDetail") final boolean fullDetail) {
        final User loggedUser = loggedUser();

        final PagedResults<ClassReservation> classRequests;
        try {
            classRequests = professorService.getPagedClassRequests(loggedUser.getId(), page);
        } catch (NonexistentProfessorException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classRequests == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, classRequests);

        final GenericEntity<?> entity;
        if (fullDetail) {
            entity = new GenericEntity<List<FullDetailClassReservationDTO>>(
                    classRequests.getResults().stream()
                            .map(request -> new FullDetailClassReservationDTO(request, uriInfo))
                            .collect(Collectors.toList())
            ) { };
        } else {
            entity = new GenericEntity<List<ClassReservationDTO>>(
                    classRequests.getResults().stream()
                            .map(request -> new ClassReservationDTO(request, uriInfo))
                            .collect(Collectors.toList())
            ) { };
        }

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/requests/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response request(@PathParam("id") final long id,
                            @DefaultValue("false") @QueryParam("fullDetail") final boolean fullDetail) {
        return reservation(id, fullDetail);
    }

    @PUT
    @Path("/requests/{id}")
    public Response approveClassRequest(@PathParam("id") final long id) {

        final User currentUser = loggedUser();

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.confirm(id, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classReservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/requests/{id}")
    public Response denyClassRequest(@PathParam("id") final long id) {

        final User currentUser = loggedUser();

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.deny(id, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(classReservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/forgot_password")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response forgotPassword(@Valid final ResetPasswordRequestForm form) {

        final boolean created;
        try {
            created = passwordResetService.createToken(form.getEmail());
        } catch (TokenCrationException e) {
            final ValidationErrorDTO error = getErrors("SendMessageError");
            return Response.status(Response.Status.BAD_GATEWAY).entity(error).build();
        }

        if(!created) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/forgot_password/{token}")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response forgotPassword(@Valid final ResetPasswordForm form,
                                   @PathParam("token") final String token) {

        final User changedUser;
        try {
            changedUser = passwordResetService.changePassword(token, form.getPassword());
        } catch (InvalidTokenException e) {
            final ValidationErrorDTO error = getErrors("invalidToken");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if(changedUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final String jwtToken = tokenManager.generateToken(changedUser.getUsername());

        return Response
                .ok()
                .header(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + jwtToken)
                .build();
    }

    @GET
    @Path("/forgot_password/{token}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response tokenValidity(@PathParam("token") final String token) {

        if(token.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if(passwordResetToken == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }
    
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response register(@Valid final RegisterForm form) {

        final User user;
        try {
            user = userService.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());
        } catch (EmailAlreadyInUseException e) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "RepeatedEmail", "email");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        } catch (UsernameAndEmailAlreadyInUseException e) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "RepeatedEmail", "email");
            addError(errors, "RepeatedUsername", "username");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        } catch (UsernameAlreadyInUseException e) {
            final ValidationErrorDTO errors = new ValidationErrorDTO();
            addError(errors, "RepeatedUsername", "username");
            return Response.status(Response.Status.CONFLICT).entity(errors).build();
        }

        if(user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final String token = tokenManager.generateToken(user.getUsername());

        final URI uri = uriInfo.getBaseUri().resolve("/user");
        return Response
                .created(uri)
                .header(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token)
                .build();
    }

    @POST
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/upgrade")
    public Response upgrade(@Valid @BeanParam final RegisterAsProfessorForm form) {

        final User loggedUser = loggedUser();

        final Professor professor;
        try {
            professor = professorService.create(loggedUser.getId(), form.getDescription(),
                    form.getPicture().getValueAs(byte[].class));
        } catch (DownloadFileException e) {
            final ValidationErrorDTO error = getErrors("fileUploadError");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        } catch (ProfessorWithoutUserException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(professor == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI uri = uriInfo.getBaseUri().resolve("/user");
        return Response.created(uri).build();
    }
}
