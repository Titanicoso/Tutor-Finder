package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

public interface ClassReservationDao {

    ClassReservation reserve(int day, int startHour, int endHour, Professor professor, User student);

    ClassReservation confirm(ClassReservation classReservation, String comment);

    ClassReservation deny(ClassReservation classReservation, String comment);

}
