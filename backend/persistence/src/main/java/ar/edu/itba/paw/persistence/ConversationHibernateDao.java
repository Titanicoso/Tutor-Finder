package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ConversationDao;
import ar.edu.itba.paw.models.*;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ConversationHibernateDao implements ConversationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Conversation create(final User user, final Professor professor, final Subject subject) {
        LOGGER.trace("Inserting conversation with user_id {}, professor_id {} and subject_id {}", user.getId(),
                professor.getId(), subject.getId());
        final Conversation conversation = new Conversation(user, professor, subject, null);
        em.persist(conversation);
        return conversation;
    }

    @Override
    public Conversation findById(final Long conversation_id) {
        LOGGER.trace("Querying for conversation with id {}", conversation_id);
        return em.find(Conversation.class, conversation_id);
    }

    @Override
    public List<Conversation> findByUserId(final Long user_id, final int limit, final int offset) {
        LOGGER.trace("Querying for conversations belonging to a user with id {}", user_id);
        final TypedQuery<Conversation> query = em.createQuery("from Conversation as c where" +
                " c.professor.id = :id or c.user.id = :id order by c.latestMessage desc", Conversation.class);
        query.setParameter("id", user_id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long totalConversationsByUserId(final Long userId) {
        LOGGER.trace("Counting conversations belonging to a user with id {}", userId);
        final TypedQuery<Long> query = em.createQuery("select count(c.id) from Conversation as c where" +
                " c.professor.id = :id or c.user.id = :id", Long.class);
        query.setParameter("id", userId);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }

    @Override
    public Conversation findByIds(final Long user_id, final Long professor_id, final Long subject_id) {
        LOGGER.trace("Querying for conversation belonging to the users with id {} and {} for subject with id {}",
                user_id, professor_id, subject_id);
        final TypedQuery<Conversation> query = em.createQuery("from Conversation as c " +
                "where c.user.id = :user_id and c.professor.id = :professor_id and " +
                "c.subject.id = :subject_id", Conversation.class);
        query.setParameter("user_id", user_id);
        query.setParameter("professor_id", professor_id);
        query.setParameter("subject_id", subject_id);
        final List<Conversation> conversations = query.getResultList();
        return conversations.isEmpty()? null : conversations.get(0);
    }

    @Override
    public Conversation merge(final Conversation conversation) {
        return em.merge(conversation);
    }

    @Override
    public Message create(final User sender, final String text, final Conversation conversation) {
        LOGGER.trace("Inserting message with sender_id {} into conversation with id {}", sender.getId(),
                conversation.getId());
        final LocalDateTime currentTime = LocalDateTime.now();
        final Message message = new Message(sender, text, currentTime);
        message.setConversation(conversation);
        em.persist(message);
        try {
            em.flush();
        } catch (PersistenceException e) {
            return null;
        }
        return message;
    }
}
