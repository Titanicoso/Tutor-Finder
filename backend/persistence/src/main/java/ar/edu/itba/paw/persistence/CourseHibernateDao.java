package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.utils.InputSanitizer;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class CourseHibernateDao implements CourseDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseHibernateDao.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private InputSanitizer inputSanitizer;

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
    public long totalByProfessorId(final Long professor_id) {
        LOGGER.trace("Counting for courses belonging to a professor with id {}", professor_id);
        final TypedQuery<Long> query = em.createQuery("select count(*) from Course as c where" +
                " c.professor.id = :id", Long.class);
        query.setParameter("id", professor_id);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
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
    public long totalByAreaId(final Long areaId) {
        LOGGER.trace("Counting for courses from area with id {}", areaId);
        final TypedQuery<Long> query = em.createQuery("select count(*) from Course as c where" +
                " c.subject.area.id = :id", Long.class);
        query.setParameter("id", areaId);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }

    @Override
    public List<Course> filter(final List<Integer> days, final Integer startHour, final Integer endHour,
                               final Double minPrice, final Double maxPrice, final String searchText, final int limit, final int offset) {

        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Course> criteria = builder.createQuery(Course.class);
        final Root<Course> root = criteria.from(Course.class);
        final Join<Course, Subject> subject = root.join("subject");
        final Join<Course, Professor> professors = root.join("professor");
        final Join<Professor, Timeslot> timeslots = professors.join("timeslots");
        criteria.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if(days != null && days.size() != 0) {
            final Stream<Predicate> dayPredicates = days.stream().map(day -> builder.equal(timeslots.get("day"), day));
            final Predicate dayPredicate = builder.or(dayPredicates.toArray(Predicate[]::new));
            predicates.add(dayPredicate);
        }

        final String search;
        if(searchText == null) {
            search = "";
        } else {
            search = inputSanitizer.sanitizeWildcards(searchText.toLowerCase());
        }

        predicates.add(builder.like(builder.lower(subject.get("name")),
                "%" + search + "%"));

        if(startHour != null) {
            predicates.add(builder.greaterThanOrEqualTo(timeslots.get("hour"), startHour));
        }

        if(endHour != null) {
            predicates.add(builder.lessThan(timeslots.get("hour"), endHour));
        }

        if(minPrice != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if(maxPrice != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        criteria.where(builder.and(predicates.toArray(new Predicate[] {})));

        TypedQuery<Course> query = em.createQuery(criteria.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public long totalByFilter(final List<Integer> days, final Integer startHour, final Integer endHour,
                               final Double minPrice, final Double maxPrice, final String searchText) {

        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        final Root<Course> root = criteria.from(Course.class);
        final Join<Course, Subject> subject = root.join("subject");
        final Join<Course, Professor> professors = root.join("professor");
        final Join<Professor, Timeslot> timeslots = professors.join("timeslots");
        criteria.select(builder.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();

        if(days != null && days.size() != 0) {
            final Stream<Predicate> dayPredicates = days.stream().map(day -> builder.equal(timeslots.get("day"), day));
            final Predicate dayPredicate = builder.or(dayPredicates.toArray(Predicate[]::new));
            predicates.add(dayPredicate);
        }

        final String search;
        if(searchText == null) {
            search = "";
        } else {
            search = inputSanitizer.sanitizeWildcards(searchText.toLowerCase());
        }

        predicates.add(builder.like(builder.lower(subject.get("name")),
                "%" + search + "%"));

        if(startHour != null) {
            predicates.add(builder.greaterThanOrEqualTo(timeslots.get("hour"), startHour));
        }

        if(endHour != null) {
            predicates.add(builder.lessThan(timeslots.get("hour"), endHour));
        }

        if(minPrice != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if(maxPrice != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        criteria.where(builder.and(predicates.toArray(new Predicate[] {})));

        TypedQuery<Long> query = em.createQuery(criteria.select(builder.countDistinct(root)));
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
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
        LOGGER.trace("Creating comment for course taught by professor with id {} and subject {}, from user with id {}",
                course.getProfessor().getId(), course.getSubject().getId(), creator.getId());
        final LocalDateTime currentTime = LocalDateTime.now();
        final Comment comment = new Comment(creator, course, text, currentTime, rating);
        em.persist(comment);
        return comment;
    }

    @Override
    public List<Comment> getComments(final Course course, final int limit, final int offset) {
        LOGGER.trace("Getting comments for course taught by professor with id {} and subject {}",
                course.getProfessor().getId(), course.getSubject().getId());
        final TypedQuery<Comment> query = em.createQuery("from Comment as c where c.course = :course " +
                "order by c.created", Comment.class);
        query.setParameter("course", course);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long totalComments(final Course course) {
        LOGGER.trace("Counting comments for course taught by professor with id {} and subject {}",
                course.getProfessor().getId(), course.getSubject().getId());
        final TypedQuery<Long> query = em.createQuery("select count(c.id) from Comment as c where c.course = :course",
                Long.class);
        query.setParameter("course", course);
        final Long result = query.getSingleResult();

        return result == null ? 0 : result;
    }

    @Override
    public boolean delete(final Course course) {
        LOGGER.trace("Deleting course taught by professor with id {} and subject {}",
                course.getProfessor().getId(), course.getSubject().getId());
        final Course toDelete = em.merge(course);
        em.remove(toDelete);

        return toDelete != null;
    }

    @Override
    public Course modify(final Course course, final String description, final Double price) {
        LOGGER.trace("Modifying course taught by professor with id {} and subject {}",
                course.getProfessor().getId(), course.getSubject().getId());
        course.setDescription(description);
        course.setPrice(price);
        return em.merge(course);
    }
}
