package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
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
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
public class CourseHibernateDao implements CourseDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Course> findByIds(final long professor_id, final long subject_id) {
        LOGGER.trace("Querying for course with professor_id {} and subject_id {}", professor_id, subject_id);
        final TypedQuery<Course> query = em.createQuery("from Course as c " +
                "where c.professor.id = :professor and c.subject.id = :subject", Course.class);
        query.setParameter("professor", professor_id);
        query.setParameter("subject", subject_id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Course> findByProfessorId(final long professor_id, final int limit, final int offset) {
        LOGGER.trace("Querying for courses belonging to a professor with id {}", professor_id);
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.professor.id = :id " +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", professor_id);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filterByAreaId(final long areaId, final int limit, final int offset) {
        LOGGER.trace("Querying for courses from area with id {}", areaId);
        final TypedQuery<Course> query = em.createQuery("from Course as c where c.subject.area.id = :id " +
                "order by c.professor.id, c.subject.id", Course.class);
        query.setParameter("id", areaId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Course> filter(final Filter filter, final int limit, final int offset) {
        TypedQuery<Course> query = em.createQuery(filter.getQuery(), Course.class);
        List<Object> params = filter.getQueryParams();
        IntStream.range(0, params.size())
                .forEach(i-> query.setParameter(i+1, params.get(i)));

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Course create(final Professor professor, final Subject subject, final String description, final Double price) throws CourseAlreadyExistsException {
        LOGGER.trace("Inserting course with user_id {} and subject_id {}", professor.getId(), subject.getId());
        final Course course = new Course(professor, subject, description, price);
        try {
            em.persist(course);
            em.flush();
            return course;
        }
        //TODO specify exception
        catch (PersistenceException e) {
            LOGGER.error("Course with user_id {} and subject_id {} already exists", professor.getId(), subject.getId());
            throw new CourseAlreadyExistsException();
        }
    }

    @Override
    public Comment create(final User creator, final String text, final Course course, final int rating) {
        final LocalDateTime currentTime = LocalDateTime.now();
        final Comment comment = new Comment(creator, course, text, currentTime, rating);
        em.persist(comment);
        return comment;
    }

    @Override
    public List<Comment> getComments(final Course course, final int limit, final int offset) {
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.course = :course " +
                "order by c.created", Comment.class);
        query.setParameter("course", course);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public boolean delete(final Course course) {
        final Course toDelete = em.merge(course);
        em.remove(toDelete);

        return toDelete != null;
    }

    @Override
    public Course modify(final Course course, final String description, final Double price) {
        course.setDescription(description);
        course.setPrice(price);
        return em.merge(course);
    }
}
