package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;

public interface UserService {

    User findUserById(final long id);

    User findByUsername(final String username);

    User findByEmail(final String email);

    User create(final String username, final String password,
                final String email, final String name, final String lastName) throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException;

    boolean changePassword(final Long userId, String password);

    PagedResults<ClassReservation> pagedReservations(final Long userId, final int page);
}
