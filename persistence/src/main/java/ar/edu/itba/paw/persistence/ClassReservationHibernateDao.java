package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.joda.time.LocalDateTime;

@Repository
public class ClassReservationHibernateDao implements ClassReservationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ClassReservation reserve(final LocalDateTime startHour, final LocalDateTime endHour, final Course course, final User student) {
        final ClassReservation classReservation = new ClassReservation(student, course, startHour, endHour, 2, null);
        em.persist(classReservation);
        return classReservation;
    }

    @Override
    public ClassReservation confirm(final ClassReservation classReservation, final String comment) {
        em.merge(classReservation);
        classReservation.confirm(comment);
        return classReservation;
    }

    @Override
    public ClassReservation deny(final ClassReservation classReservation, final String comment) {
        em.merge(classReservation);
        classReservation.deny(comment);
        return classReservation;
    }
}