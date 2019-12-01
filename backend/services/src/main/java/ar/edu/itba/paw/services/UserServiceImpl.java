package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.utils.PaginationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int PAGE_SIZE = 3;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private PaginationResultBuilder pagedResultBuilder;

    @Override
    public User findUserById(final long id) {
        LOGGER.debug("Searching for user with id {}", id);
        return userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            LOGGER.error("Attempted to find user with empty username");
            return null;
        }
        LOGGER.debug("Searching for user with username {}", username);
        return userDao.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public User findByEmail(final String email) {
        if(email == null || email.isEmpty()) {
            LOGGER.error("Attempted to find user with empty email");
            return null;
        }
        LOGGER.debug("Searching for user with email {}", email);
        return userDao.findByEmail(email).orElse(null);
    }

    @Transactional
    @Override
    public User create(final String username, final String password, final String email,
                       final String name, final String lastName) throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {

        if(username == null || password == null || email == null || name == null || lastName == null) {
            LOGGER.error("Attempted to create user with empty fields");
            return null;
        }

        if(username.length() < 1 || username.length() > 128) {
            LOGGER.error("Attempted to create user with invalid username length");
            return null;
        }

        if(!name.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") || !lastName.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") ||
                name.length() < 1 || lastName.length() < 1 || name.length() > 128 || lastName.length() > 128){
            LOGGER.error("Attempted to create user with invalid name");
            return null;
        }

        if(!email.matches("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+") || email.length() < 1 || email.length() > 512){
            LOGGER.error("Attempted to create user with invalid email");
            return null;
        }

        if(password.length() < 8 || password.length() > 64){
            LOGGER.error("Attempted to create user with invalid password length");
            return null;
        }

        LOGGER.debug("Creating user with username {}", username);

        final boolean existsEmail = findByEmail(email) != null;
        final boolean existsUsername = findByUsername(username) != null;

        if(existsUsername) {
            if (existsEmail) {
                LOGGER.error("User with username {} and email {} already exists", username, email);
                throw new UsernameAndEmailAlreadyInUseException();
            }
            LOGGER.error("User with username {} already exists", username);
            throw new UsernameAlreadyInUseException();
        }
        if(existsEmail) {
            LOGGER.error("User with email {} already exists", email);
            throw new EmailAlreadyInUseException();
        }

        final User user = userDao.create(username, encoder.encode(password), email, name, lastName);

        if (user != null){
            LOGGER.debug("Sending welcome mail for user with id {}", user.getId());
            emailService.sendRegistrationEmail(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean changePassword(final Long userId, final String newPassword) {
        if(userId == null || newPassword == null) {
            LOGGER.error("Attempted to change password with invalid parameters");
            return false;
        }

        if(newPassword.length() < 8 || newPassword.length() > 64) {
            LOGGER.error("Attempted to change password with invalid password length");
            return false;
        }
        LOGGER.debug("Changing password for user with id {}", userId);
        return userDao.changePasswordById(userId, encoder.encode(newPassword));
    }

    @Override
    @Transactional
    public PagedResults<ClassReservation> pagedReservations(Long userId, int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for reservations for user with id {}", userId);
        final List<ClassReservation> reservations = userDao.pagedReservations(userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
        final long total = userDao.totalReservations(userId);

        return pagedResultBuilder.getPagedResults(reservations, total, page, PAGE_SIZE);
    }
}
