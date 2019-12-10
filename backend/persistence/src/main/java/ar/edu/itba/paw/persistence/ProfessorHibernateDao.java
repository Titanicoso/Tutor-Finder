package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.utils.InputSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorHibernateDao implements ProfessorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InputSanitizer inputSanitizer;

    @Override
    public Professor create(final User user, final String description, final byte[] picture) {

        final boolean exists = em.find(User.class, user.getId()) != null;
        LOGGER.trace("Adding user with id {} as professor", user.getId());

        final Professor professor = new Professor(user.getId(), user.getUsername(), user.getName(),
                user.getLastname(), user.getPassword(), user.getEmail(), description, picture);

        if(exists) {
            em.createNativeQuery("insert into professors(user_id, description, profile_picture)" +
                    " values (?, ?, ?)")
                    .setParameter(1, user.getId())
                    .setParameter(2, description)
                    .setParameter(3, picture)
                    .executeUpdate();
        } else {
            em.persist(professor);
        }
        return professor;
    }

    @Override
    public Professor modify(final Professor professor, final String description, final byte[] picture) {
        LOGGER.trace("Modifying professor with id {}", professor.getId());
        em.merge(professor);
        if(description != null){
            professor.setDescription(description);
        }
        if(picture != null){
            professor.setPicture(picture);
        }
        return professor;
    }

    @Override
    public Optional<Professor> findById(final Long professor_id) {
        LOGGER.trace("Querying for professor with id {}", professor_id);
        final Professor professor = em.find(Professor.class, professor_id);
        return Optional.ofNullable(professor);
    }

    @Override
    public Optional<Professor> findByUsername(final String username) {
        LOGGER.trace("Querying for professor with username {}", username);
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where p.username = :username",
                Professor.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Professor> filterByFullName(final String fullName, final int limit, final int offset) {
        final String search = "%" + inputSanitizer.sanitizeWildcards(fullName) + "%";
        LOGGER.trace("Querying for professor with full name containing {}", fullName);
        final TypedQuery<Professor> query = em.createQuery("from Professor as p where upper(concat(p.name, ' ', p.lastname)) like upper(:name)" +
                "order by p.id", Professor.class);
        query.setParameter("name", search);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long totalProfessorsByFullName(final String fullName) {
        final String search = "%" + inputSanitizer.sanitizeWildcards(fullName) + "%";
        LOGGER.trace("Counting professors with full name containing {}", fullName);
        final TypedQuery<Long> query = em.createQuery("select count(p.id) from Professor as p" +
                " where upper(concat(p.name, ' ', p.lastname)) like upper(:name)", Long.class);
        query.setParameter("name", search);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }

    @Override
    public Professor merge(final Professor professor) {
        return em.merge(professor);
    }

    public List<ClassReservation> getPagedClassRequests(final Long professorId, final int limit, final int offset) {
        LOGGER.trace("Getting class requests for professor with id {}", professorId);
        final TypedQuery<ClassReservation> query = em.createQuery("from ClassReservation as c where c.course.professor.id= :professor_id " +
                "order by c.status desc, c.startTime desc", ClassReservation.class);
        query.setParameter("professor_id", professorId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();

    }

    @Override
    public long totalClassRequests(final Long professorId) {
        LOGGER.trace("Counting  class requests for professor with id {}", professorId);
        final TypedQuery<Long> query = em.createQuery("select count(c.classRequestId) from ClassReservation as c where" +
                " c.course.professor.id= :professor_id", Long.class);
        query.setParameter("professor_id", professorId);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }
}
