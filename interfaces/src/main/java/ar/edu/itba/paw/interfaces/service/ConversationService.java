package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.exceptions.SameUserConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.models.*;

import java.util.List;

public interface ConversationService {

    boolean sendMessage(final User user, final Professor professor, final Subject subject, final String body)
            throws SameUserConversationException, UserNotInConversationException;

    boolean sendMessage(final User from, final Conversation conversation, final String body) throws UserNotInConversationException;

    PagedResults<Conversation> findByUserId(final Long userId, final int page) throws PageOutOfBoundsException;

    Conversation findById(final Long conversation_id, final  Long userId) throws UserNotInConversationException, NonexistentConversationException;
}
