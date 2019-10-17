package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.ConversationDTO;
import ar.edu.itba.paw.webapp.dto.MessageDTO;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import ar.edu.itba.paw.webapp.dto.form.MessageForm;
import ar.edu.itba.paw.webapp.utils.PaginationLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Chequear badRequest en resultados paginados
@Path("conversations")
@Component
public class ConversationController extends BaseController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private PaginationLinkBuilder linkBuilder;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response conversations(@DefaultValue("1") @QueryParam("page") final int page) {

        final User loggedUser = loggedUser();
        final PagedResults<Conversation> conversations = conversationService.findByUserId(loggedUser.getId(), page);

        if(conversations == null) {
            final ValidationErrorDTO error = getErrors("pageOutOfBounds");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        final Link[] links = linkBuilder.buildLinks(uriInfo, conversations);

        final GenericEntity<List<ConversationDTO>> entity = new GenericEntity<List<ConversationDTO>>(
                conversations.getResults().stream()
                        .map(conversation -> new ConversationDTO(conversation, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).links(links).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response conversation(@PathParam("id") final long id) {

        final User loggedUser = loggedUser();
        final Conversation conversation;
        try {
            conversation = conversationService.findById(id, loggedUser.getId());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(new ConversationDTO(conversation, uriInfo.getBaseUri())).build();
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}/messages")
    public Response messages(@PathParam("id") final long id) {

        final User loggedUser = loggedUser();
        final Conversation conversation;
        try {
            conversation = conversationService.findById(id, loggedUser.getId());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(conversation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Conversation ret = conversationService.initializeMessages(conversation);

        final GenericEntity<List<MessageDTO>> entity = new GenericEntity<List<MessageDTO>>(
                ret.getMessages().stream()
                        .map(message -> new MessageDTO(message, uriInfo.getBaseUri()))
                        .collect(Collectors.toList())
        ){};

        return Response.ok(entity).build();
    }

    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Path("/{id}")
    public Response sendMessage(@PathParam("id") final long id, @Valid final MessageForm message) {

        final User loggedUser = loggedUser();

        final boolean sent;
        try {
            sent = conversationService.sendMessage(loggedUser.getId(), id, message.getMessage());
        } catch (UserNotInConversationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (NonexistentConversationException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!sent) {
            final ValidationErrorDTO error = getErrors("SendMessageError");
            return Response.status(Response.Status.BAD_GATEWAY).entity(error).build();
        }

        final URI uri = uriInfo.getAbsolutePathBuilder().path("/messages").build();
        return Response.created(uri).build();
    }

}
