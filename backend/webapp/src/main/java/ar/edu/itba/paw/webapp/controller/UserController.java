package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.dto.ClassReservationDTO;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.ProfessorDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.dto.form.EditProfessorProfileForm;
import ar.edu.itba.paw.webapp.dto.form.RegisterAsProfessorForm;
import ar.edu.itba.paw.webapp.dto.form.RegisterForm;
import ar.edu.itba.paw.webapp.dto.form.ResetPasswordRequestForm;
import ar.edu.itba.paw.webapp.form.ResetPasswordForm;
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
    private CourseService courseService;

    @Context
    private UriInfo uriInfo;


    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response profile() {

        final User loggedUser = loggedUser();
        final Professor professor = professorService.findById(loggedUser.getId());

        if(professor == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
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
                        .map(course -> new CourseDTO(course, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }


    @GET
    @Path("/schedule")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response schedule(){
        //TODO fill in when schedule model is revised.
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/reservations")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservations(@DefaultValue("1") @QueryParam("page") final int page) {
        final User loggedUser = loggedUser();

        final PagedResults<ClassReservation> classReservations =  userService.pagedReservations(loggedUser.getId(), page);

        if(classReservations == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, classReservations);

        final GenericEntity<List<ClassReservationDTO>> entity = new GenericEntity<List<ClassReservationDTO>>(
                classReservations.getResults().stream()
                        .map(reservation -> new ClassReservationDTO(reservation, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/reservations/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response reservation(@PathParam("id") final long id) {
        final User loggedUser = loggedUser();

        final ClassReservation classReservation = classReservationService.findById(id);

        if(classReservation == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if(!classReservation.getStudent().getId().equals(loggedUser.getId())) {
            final ValidationErrorDTO error = getErrors("sameUserReservation");
            return Response.status(Response.Status.FORBIDDEN).entity(error).build();
        }

        return Response.ok(new ClassReservationDTO(classReservation, uriInfo)).build();
    }

    @GET
    @Path("/requests")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response requests(@DefaultValue("1") @QueryParam("page") final int page) {
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

        final GenericEntity<List<ClassReservationDTO>> entity = new GenericEntity<List<ClassReservationDTO>>(
                classRequests.getResults().stream()
                        .map(request -> new ClassReservationDTO(request, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Path("/requests/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response request(@PathParam("id") final long id) {
        return reservation(id);
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

    //TODO: Maybe url encoded
    //TODO: Frontend has to check password repetition
    //TODO: Automatic authentication?
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

        return Response.ok().build();
    }
    
    //TODO: Maybe change to /users
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

        final URI uri = uriInfo.getBaseUri().resolve("/user");
        return Response.created(uri).build();
    }

    //TODO: Check if modify can be the same form
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
