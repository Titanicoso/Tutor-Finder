package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.CourseFileDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.dto.form.UploadClassFileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("courses/{professor}_{subject}/files")
@Component
public class CourseFileController extends BaseController {

    @Autowired
    private CourseFileService cfs;

    @Autowired
    private CourseService cs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getCourseFiles(@PathParam("professor") final long professorId,
                                   @PathParam("subject") final long subjectId) {

        final User currentUser = loggedUser();
        final Course course = cs.findCourseByIds(professorId, subjectId);

        if(course == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final List<CourseFile> courseFiles;
        try {
            courseFiles = cfs.findForCourse(course, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final GenericEntity<List<CourseFileDTO>> entity = new GenericEntity<List<CourseFileDTO>>(
                courseFiles.stream()
                        .map(file -> new CourseFileDTO(file, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).build();
    }


    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_OCTET_STREAM, })
    public Response downloadFile(@PathParam("professor") final long professorId,
                                 @PathParam("subject") final long subjectId,
                                 @PathParam("id") final long id) {

        final User currentUser = loggedUser();
        final CourseFile courseFile;
        try {
            courseFile = cfs.findByIdForUser(id, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(courseFile == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Course course = courseFile.getCourse();

        if(course.getSubject().getId() != subjectId || course.getProfessor().getId() != professorId) {
            final ValidationErrorDTO error = getErrors("invalidCourse");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        //TODO: Verificar si el header es necesario
        return Response
                .ok(courseFile.getContent(), MediaType.valueOf(courseFile.getType()))
                .header("Content-Disposition","attachment; filename=\"" + courseFile.getName() +"\"")
                .build();
    }


    @PUT
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response uploadFile(@Valid @BeanParam final UploadClassFileForm form,
                               @PathParam("professor") final long professorId,
                               @PathParam("subject") final long subjectId) {

        final User currentUser = loggedUser();
        final CourseFile courseFile;

        try {
            courseFile = cfs.save(professorId, subjectId, currentUser, form.getFile().getName(),
                    form.getDescription(), form.getFile().getContentDisposition().getType(), form.getFile().getValueAs(byte[].class));
        }  catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(courseFile == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(courseFile.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam("professor") final long professorId,
                                   @PathParam("subject") final long subjectId,
                                   @PathParam("id") final long id) {

        final User currentUser = loggedUser();
        try {
            final CourseFile courseFile = cfs.findByIdForUser(id, currentUser);

            final Course course = courseFile.getCourse();

            if(course.getSubject().getId() != subjectId || course.getProfessor().getId() != professorId) {
                final ValidationErrorDTO error = getErrors("invalidCourse");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

            cfs.deleteById(id, currentUser);
        } catch (UserAuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.noContent().build();
    }
}
