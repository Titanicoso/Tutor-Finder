package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.ClassReservationDao;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@Transactional
public class ClassReservationServiceImpl implements ClassReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassReservationServiceImpl.class);

    @Autowired
    private ClassReservationDao crd;

    @Autowired
    private UserService us;

    @Autowired
    private CourseService cs;


    @Override
    @Transactional
    public ClassReservation reserve(final LocalDateTime startHour, final LocalDateTime endHour,
                                    final Long professorId, final Long subjectId,
                                    final Long studentId) throws SameUserException, NonexistentCourseException, NonExistentUserException, ReservationTimeOutOfRange {
        final User student = us.findUserById(studentId);
        final Course course = cs.findCourseByIds(professorId, subjectId);

        if(!startHour.isAfter(LocalDateTime.now()) || !endHour.isAfter(startHour)) {
            LOGGER.error("Attempted to reserve class with invalid times");
            return null;
        }

        if(course == null) {
            LOGGER.error("Attempted to reserve class for nonExistentCourse");
            throw new NonexistentCourseException();
        }

        if(course.getProfessor().getId().equals(studentId)){
            LOGGER.error("Professor with id {} attempted to reserve his own class", studentId);
            throw new SameUserException();
        }

        if(student == null) {
            LOGGER.error("NonExistent student attempted to reserve class");
            throw new NonExistentUserException();
        }

        final Professor professor = course.getProfessor();

        final boolean inRange = professorHasRange(professor,
                startHour.getDayOfWeek(), startHour.getHourOfDay(), endHour.getHourOfDay());

        if(!inRange) {
            LOGGER.error("Student attempted to reserve a time range not allowed by professor");
            throw new ReservationTimeOutOfRange();
        }


        LOGGER.debug("Making reservation for course taught by professor with id {} of subject {} by student {}",
                course.getProfessor().getId(), course.getSubject().getId(), studentId);
        return crd.reserve(startHour, endHour, course, student);
    }

    @Override
    @Transactional
    public ClassReservation confirm(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException {
        final ClassReservation classReservation = findById(classReservationId);
        if(classReservation == null) {
            LOGGER.error("Attempted to confirm non existent class reservation");
            return null;
        }

        if(!classReservation.getCourse().getProfessor().getId().equals(professorId)) {
            LOGGER.error("Attempted to confirm not owned class");
            throw new UserAuthenticationException();
        }
        LOGGER.debug("Confirming reservation with id {}", classReservationId);
        return crd.confirm(classReservation, comment);
    }

    @Override
    @Transactional
    public ClassReservation deny(final Long classReservationId, final Long professorId, final String comment) throws UserAuthenticationException {
        ClassReservation classReservation = findById(classReservationId);
        if(classReservation == null) {
            LOGGER.error("Attempted to deny non existent class reservation");
            return null;
        }

        if(!classReservation.getCourse().getProfessor().getId().equals(professorId)) {
            LOGGER.error("Attempted to deny not owned class");
            throw new UserAuthenticationException();
        }
        LOGGER.debug("Denying reservation with id {}", classReservationId);
        return crd.deny(classReservation, comment);
    }

    @Override
    public boolean hasAcceptedReservation(final User student, final Course course) {
        if(student == null) {
            return false;
        }
        LOGGER.debug("Verifying if user with id {} has an accepted reservation for course" +
                        " taught by professor with id {} of subject {}", course.getProfessor().getId(),
                course.getSubject().getId(), student.getId());
        return crd.hasAcceptedReservation(student, course);
    }

    @Override
    public ClassReservation findById(final Long classReservationId) {
        LOGGER.debug("Finding reservation with id {}", classReservationId);
        return crd.findById(classReservationId);
    }

    private boolean professorHasRange(final Professor professor, final int day,
                                      final int start, final int end) {

        return IntStream.range(start, end)
                .allMatch(i -> professor.getTimeslots().stream()
                        .anyMatch(timeslot -> timeslot.getDay().equals(day)
                                && timeslot.getHour().equals(i)));
    }

}
