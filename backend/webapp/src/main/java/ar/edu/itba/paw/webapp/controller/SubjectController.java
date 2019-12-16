package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.SubjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("subjects")
@Component
public class SubjectController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/available")
    public Response availableSubjects() {
        final User loggedUser = loggedUser();
        final List<Subject> availableSubjects = subjectService.getAvailableSubjects(loggedUser.getId());

        final GenericEntity<List<SubjectDTO>> entity = new GenericEntity<List<SubjectDTO>>(
                availableSubjects.stream()
                        .map(subject -> new SubjectDTO(subject, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).build();
    }
}
