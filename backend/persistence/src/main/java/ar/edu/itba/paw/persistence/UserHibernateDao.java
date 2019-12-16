package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHibernateDao implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findById(final long id) {
        LOGGER.trace("Querying for user with id {}", id);
        User us =  em.find(User.class, id);
        return Optional.ofNullable(us);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        LOGGER.trace("Querying for user with email {}", email);
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public User create(final String username, final String password, final String email, final String name, final String lastName) throws UsernameAlreadyInUseException, EmailAlreadyInUseException, UsernameAndEmailAlreadyInUseException {
        final User user = new User(username, name, lastName, password, email);
        LOGGER.trace("Inserting user with username {}", username);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        LOGGER.trace("Querying for user with username {}", username);
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<ClassReservation> pagedReservations(final Long userId, final Integer limit, final Integer offset) {
        LOGGER.trace("Getting reservations for user with id {}", userId);
        final TypedQuery<ClassReservation> query = em.createQuery("from ClassReservation as c where c.student.id= :student_id " +
                "order by c.status, c.startTime desc", ClassReservation.class);
        query.setParameter("student_id", userId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long totalReservations(final Long userId) {
        LOGGER.trace("Counting reservations for user with id {}", userId);
        final TypedQuery<Long> query = em.createQuery("select count(c.classRequestId) from ClassReservation" +
                " as c where c.student.id= :student_id", Long.class);
        query.setParameter("student_id", userId);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }

    @Override
    public boolean changePasswordById(final Long userId, final String newPassword) {
        final User user = em.find(User.class, userId);

        if(user == null) {
            return false;
        }
        LOGGER.trace("Changing password for user with id {}", userId);
        user.setPassword(newPassword);
        return em.merge(user) != null;
    }
}
