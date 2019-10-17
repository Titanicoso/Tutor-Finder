package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.SameUserException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.PagedResults;


public interface ConversationService {

    Conversation sendMessage(final Long userId, final Long professorId, final Long subjectId, final String body)
            throws SameUserException, UserNotInConversationException, NonexistentConversationException;

    boolean sendMessage(final Long userId, final Long conversationId, final String body) throws UserNotInConversationException, NonexistentConversationException;

    PagedResults<Conversation> findByUserId(final Long userId, final int page);

    Conversation findById(final Long conversation_id, final  Long userId) throws UserNotInConversationException;

    Conversation initializeMessages(Conversation conversation);
}
