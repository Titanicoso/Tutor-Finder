package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.ProfessorDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;


@Path("professors")
@Component
public class ProfessorController extends BaseController{


    private static final Logger LOGGER = LoggerFactory.getLogger(ar.edu.itba.paw.webapp.old_controller.UserController.class);

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HttpSessionRequestCache cache;

    @Autowired
    private ScheduleService ss;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response professors(@DefaultValue("") @QueryParam("q") final String query,
                               @DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Professor> professors = ps.filterByFullName(query, page);

        if(professors == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, professors);

        final GenericEntity<List<ProfessorDTO>> entity = new GenericEntity<List<ProfessorDTO>>(
                professors.getResults().stream()
                        .map(professor -> new ProfessorDTO(professor, uriInfo))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }


    @GET
    @Path("/{username}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response professor(@PathParam("username") final String username){
        final Professor professor = ps.findByUsername(username);
        //TODO: define behaviour for when professor is the person who is makeing the request.
        final User loggedUser = loggedUser();
        final boolean isProfessor = isProfessor();
        if(professor == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ProfessorDTO(professor, uriInfo)).build();

    }


    @GET
    @Path("/{username}/courses")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response professorCourses(@PathParam("username") final String username,
                                     @DefaultValue("1") @QueryParam("page") final int page){
        final Professor professor = ps.findByUsername(username);
        final PagedResults<Course> results = cs.findCourseByProfessorId(professor.getId(), page);

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
    @Path("/{username}/schedule")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response professorSchedule(@PathParam("username") final String username){
        //TODO fill in when schedule model is revised.
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{username}/image")
    @Produces({"image/png", "image/jpeg"})
    public Response getImage(@PathParam("username") final String username) {

        final Professor professor = ps.findByUsername(username);

        if(professor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final byte[] picture = professor.getPicture();

        return Response.ok(picture).build();
    }
}
