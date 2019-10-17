package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.webapp.dto.AreaDTO;
import ar.edu.itba.paw.webapp.dto.CourseDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Chequear minimo y maximo de paginacion error
@Path("areas")
@Component
public class AreaController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @Autowired
    private CourseService cs;

    @Context
    private UriInfo uriInfo;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response areas(@DefaultValue("") @QueryParam("q") final String query,
                          @DefaultValue("1") @QueryParam("page") final int page) {
        final PagedResults<Area> areas = as.filterAreasByName(query, page);

        if(areas == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, areas);

        final GenericEntity<List<AreaDTO>> entity = new GenericEntity<List<AreaDTO>>(
                areas.getResults().stream()
                        .map(area -> new AreaDTO(area, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response area(@PathParam("id") final long id) {
        final Area area = as.findAreaById(id);

        if(area == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(new AreaDTO(area, uriInfo.getBaseUri())).build();
    }

    @GET
    @Path("/{id}/image")
    @Produces({"image/png", "image/jpeg"})
    public Response getAreaPicture(@PathParam("id") final int id) {

        final Area area = as.findAreaById(id);

        if(area == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final byte[] picture = area.getImage();

        return Response.ok(picture).build();
    }

    @GET
    @Path("/{id}/courses")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response area(@PathParam("id") final long id,
                         @DefaultValue("1") @QueryParam("page") final int page) {

        final Area area = as.findAreaById(id);
        if(area == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.debug("Creating view for Area with id {}", id);

        final PagedResults<Course> results = cs.filterByAreaId(id, page);

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
}
