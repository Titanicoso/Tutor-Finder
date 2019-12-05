package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.ProfessorDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.exceptions.ProfessorWithoutUserException;
import ar.edu.itba.paw.services.utils.PaginationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfessorServiceImpl implements ProfessorService {

    private static final int PAGE_SIZE = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorServiceImpl.class);

    @Autowired
    private ProfessorDao professorDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PaginationResultBuilder pagedResultBuilder;

    @Override
    @Transactional
    public Professor findById(final Long id) {
        LOGGER.debug("Searching for professor with id {}", id);
        return professorDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Professor findByUsername(final String username) {
        if(username == null || username.isEmpty()) {
            LOGGER.error("Attempted to find professor with empty username");
            return null;
        }
        LOGGER.debug("Searching for professor with username {}", username);
        return professorDao.findByUsername(username).orElse(null);
    }

    @Override
    @Transactional
    public PagedResults<Professor> filterByFullName(final String fullName, final int page) {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for professors with full name containing {}", fullName);
        final List<Professor> professors = professorDao.filterByFullName(fullName, PAGE_SIZE, PAGE_SIZE * (page - 1));
        final long total = professorDao.totalProfessorsByFullName(fullName);

        return pagedResultBuilder.getPagedResults(professors, total, page, PAGE_SIZE);
    }

    @Override
    @Transactional
    public Professor create(final Long userId, final String description, final byte[] picture)
            throws ProfessorWithoutUserException, DownloadFileException {
        LOGGER.debug("Adding user with id {} as professor", userId);
        final User user = userDao.findById(userId).orElse(null);
        if(user == null) {
            LOGGER.error("Attempted to add a non existent user to professors");
            throw new ProfessorWithoutUserException("A valid user id must be provided in order to ");
        }

        if(description == null) {
            LOGGER.error("Attempted to create professor with null description");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempted to create professor with invalid description of size {}", description.length());
            return null;
        }
        if(picture == null || picture.length < 1) {
            LOGGER.error("Attempted to create professor without profile picture");
            return null;
        }

        final byte[] newPicture;
        try {
            newPicture = cropImageSquare(picture);
        } catch (IOException e) {
            throw new DownloadFileException();
        }

        if(newPicture == null) {
            throw new DownloadFileException();
        }


        final Professor professor = professorDao.create(user, description, newPicture);
        if(professor == null) {
            LOGGER.error("Attempted to add a non existent user to professors");
            throw new ProfessorWithoutUserException("A valid user id must be provided in order to ");
        }
        return professor;
    }


    private byte[] cropImageSquare(final byte[] image) throws IOException {

        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        if(originalImage == null) {
            return null;
        }

        final byte[] newPicture;

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        if (height == width) {
            return image;
        }

        int squareSize = (height > width ? width : height);

        int xc = width / 2;
        int yc = height / 2;

        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2),
                yc - (squareSize / 2),
                squareSize,
                squareSize
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(croppedImage,"png", baos);
        baos.flush();
        newPicture = baos.toByteArray();
        baos.close();

        return newPicture;
    }

    @Override
    public Professor modify(final Long userId, final String description, final byte[] picture)
            throws ProfessorWithoutUserException, NonexistentProfessorException, DownloadFileException {
        LOGGER.debug("Editing professor with id {}", userId);

        Optional<Professor> professor = professorDao.findById(userId);

        if(professor.isPresent()){
            String newDescription = null;
            byte[] newPicture = null;
            if(description.length() > 50 && description.length() < 300) {
                newDescription = description;
            } else {
                LOGGER.debug("Attempted to modify profile for professor with id {} with invalid description size {}, " +
                                "keeping old description",
                        professor.get().getId(), description.length());
            }

            if(picture == null || picture.length < 1) {
                LOGGER.debug("Attempted to modify profile for professor with id {} with no profile picture, " +
                                "keeping old one",
                        professor.get().getId());
            } else {
                try {
                    newPicture = cropImageSquare(picture);
                } catch (IOException e) {
                    throw new DownloadFileException();
                }

                if(newPicture == null) {
                    throw new DownloadFileException();
                }
            }

            return professorDao.modify(professor.get(), newDescription, newPicture);

        } else {
            throw new NonexistentProfessorException();
        }

    }

    @Transactional
    @Override
    public Professor createWithUser(final Long id, final String username, final String name,
                                    final String lastname, final String password, final String email,
                                    final String description, final byte[] picture)
            throws EmailAlreadyInUseException, UsernameAlreadyInUseException, UsernameAndEmailAlreadyInUseException {

        if(username == null || password == null || email == null || name == null || lastname == null) {
            LOGGER.error("Attempted to create professor with empty fields");
            return null;
        }

        if(username.length() < 1 || username.length() > 128) {
            LOGGER.error("Attempted to create professor with invalid username length");
            return null;
        }

        if(!name.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") || !lastname.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") ||
                name.length() < 1 || lastname.length() < 1 || name.length() > 128 || lastname.length() > 128){
            LOGGER.error("Attempted to create professor with invalid name");
            return null;
        }

        if(!email.matches("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+") || email.length() < 1 || email.length() > 512){
            LOGGER.error("Attempted to create professor with invalid email");
            return null;
        }

        if(password.length() < 8 || password.length() > 64){
            LOGGER.error("Attempted to create professor with invalid password length");
            return null;
        }

        if(description.length() < 50 || description.length() > 300) {
            LOGGER.error("Attempting to create professor with invalid description of size {}", description.length());
            return null;
        }

        if(picture == null) {
            LOGGER.error("Attempted to create professor without profile picture");
            return null;
        }

        final User user;
        user = userDao.create(username, password, email, name, lastname);

        LOGGER.debug("Adding user with id {} as professor", user.getId());
        return professorDao.create(user, description, picture);
    }

    @Override
    @Transactional
    public Professor initializeCourses(final Professor professor) {
        final Professor merged = professorDao.merge(professor);
        LOGGER.debug("Initializing courses for professor with id {} ", merged.getId());
        merged.getCourses().size();
        return merged;
    }

    @Transactional
    public PagedResults<ClassReservation> getPagedClassRequests(Long professorId, Integer page) throws NonexistentProfessorException {
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }
        if(findById(professorId) == null){
            LOGGER.error("attempted to get class requests for an invalid professor id");
            throw new NonexistentProfessorException();
        }

        LOGGER.debug("Searching for reservations for user with id {}", professorId);
        final List<ClassReservation> reservations = professorDao.getPagedClassRequests(professorId, PAGE_SIZE, PAGE_SIZE * (page - 1));
        final long total = professorDao.totalClassRequests(professorId);

        return pagedResultBuilder.getPagedResults(reservations, total, page, PAGE_SIZE);
    }
}
