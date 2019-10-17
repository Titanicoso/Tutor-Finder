package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.SameUserException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.utils.PaginationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationServiceImpl.class);

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaginationResultBuilder pagedResultBuilder;

    @Transactional
    @Override
    public Conversation sendMessage(final Long userId, final Long professorId, final Long subjectId, final String body)
            throws SameUserException, UserNotInConversationException, NonexistentConversationException {

        final User user = userService.findUserById(userId);
        final Professor professor = professorService.findById(professorId);
        final Subject subject = subjectService.findSubjectById(subjectId);

        if(user == null || professor == null || subject == null) {
            LOGGER.error("Attempted to send message with invalid parameters");
            return null;
        }

        if(body == null || body.length() < 1 || body.length() > 1024) {
            LOGGER.error("Attempted to send message invalid body size");
            return null;
        }

        if(user.getId().equals(professor.getId())) {
            LOGGER.error("User attempted to send message to himself");
            throw new SameUserException();
        }

        LOGGER.debug("Searching for conversation belonging to user with id {} and professor with id {} about subject with id {}",
                userId, professorId, subjectId);
        final Conversation conversation = conversationDao.findByIds(user.getId(), professor.getId(), subject.getId());

        if(conversation == null) {
            LOGGER.debug("Conversation belonging to user with id {} and professor with id {} about subject with id {} does not exist, " +
                    "creating conversation", userId, professorId, subjectId);
            final Conversation conversationCreated = conversationDao.create(user, professor, subject);
            final boolean sent = sendMessage(user.getId(), conversationCreated.getId(), body);
            return sent ? conversationCreated : null;
        } else {
            final boolean sent = sendMessage(user.getId(), conversation.getId(), body);
            return sent ? conversation : null;
        }
    }
    
    @Override
    public boolean sendMessage(final Long userId, final Long conversationId, final String body)
            throws UserNotInConversationException, NonexistentConversationException {

        final User from = userService.findUserById(userId);
        if(from == null) {
            LOGGER.error("Attempted to send message from non existent user");
            return false;
        }
        final Conversation conversation = findById(conversationId, from.getId());

        if(conversation == null) {
            throw new NonexistentConversationException();
        }

        if(!conversation.belongs(from.getId())) {
            LOGGER.error("Attempted to send message in conversation with id {} from a user that is not part of it",
                    conversationId);
            throw new UserNotInConversationException();
        }

        if(body == null || body.length() < 1 || body.length() > 1024) {
            LOGGER.error("Attempted to send message invalid body size");
            return false;
        }

        LOGGER.debug("Sending message from user with id {} in conversation with id {}", userId, conversationId);
        final Message message = conversationDao.create(from, body, conversation);

        final User to = conversation.getProfessor().getId().equals(from.getId())
                ? conversation.getUser(): conversation.getProfessor();

        if(message != null) {
            LOGGER.debug("Sending contact email from user with id {} to user with id {} in conversation with id {}",
                    userId, to.getId(), conversationId);
            emailService.sendContactEmail(from, to, conversation, message);
        }

        return message != null;
    }


    @Override
    public PagedResults<Conversation> findByUserId(final Long userId, final int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for conversations belonging to user with id {}", userId);
        final List<Conversation> conversations = conversationDao.findByUserId(userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
        final long total = conversationDao.totalConversationsByUserId(userId);

        final PagedResults<Conversation> pagedResults =
                pagedResultBuilder.getPagedResults(conversations, total, page, PAGE_SIZE);

        if(pagedResults == null) {
            LOGGER.error("Page number exceeds total page count");
            return null;
        }

        return pagedResults;
    }

    @Transactional
    @Override
    public Conversation findById(final Long conversationId, final Long userId)
            throws UserNotInConversationException {
        LOGGER.debug("Searching for conversation with id {}", conversationId);
        final Conversation conversation = conversationDao.findById(conversationId);

        if(conversation == null) {
            LOGGER.error("Attempted to get a non existent conversation");
            return null;
        }

        if(!conversation.belongs(userId)) {
            LOGGER.error("User with id {} attempted to get a conversation that does not belong to him");
            throw new UserNotInConversationException();
        }

        return conversation;
    }

    @Transactional
    @Override
    public Conversation initializeMessages(final Conversation conversation) {
        final Conversation ret = conversationDao.merge(conversation);
        LOGGER.debug("Initializing messages for conversation with  id {}", ret.getId());
        ret.getMessages().size();
        return ret;
    }

}
