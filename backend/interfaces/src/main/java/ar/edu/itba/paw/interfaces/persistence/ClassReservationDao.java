package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;

public interface ClassReservationDao {

    ClassReservation reserve(LocalDateTime startTime, LocalDateTime endTime, Course course, User student);

    ClassReservation confirm(ClassReservation classReservation, String comment);

    ClassReservation deny(ClassReservation classReservation, String comment);

    boolean hasAcceptedReservation(User student, Course course);

    ClassReservation findById(final Long classReservationId);

}
