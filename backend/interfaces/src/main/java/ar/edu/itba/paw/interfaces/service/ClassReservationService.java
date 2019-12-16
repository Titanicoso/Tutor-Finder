package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;

public interface ClassReservationService {

    ClassReservation reserve(LocalDateTime startHour, LocalDateTime endHour,
                             Long professorId, Long subjectId,
                             Long studentId) throws SameUserException, NonexistentCourseException, NonExistentUserException, ReservationTimeOutOfRange;

    ClassReservation confirm(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException;


    boolean hasAcceptedReservation(User student, Course course);

    ClassReservation deny(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException;

    ClassReservation findById(final Long classReservationId);
}

