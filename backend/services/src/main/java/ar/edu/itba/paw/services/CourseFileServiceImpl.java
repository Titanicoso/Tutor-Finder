package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.persistence.CourseFileDao;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CourseFileServiceImpl implements CourseFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseFileServiceImpl.class);

    @Autowired
    private CourseFileDao cfd;

    @Autowired
    private ClassReservationService crs;

    @Autowired
    private CourseService cs;

    @Override
    public List<CourseFile> findForCourse(final Course course, final User user) throws UserAuthenticationException {
        if(!canReadFiles(course, user)) {
            LOGGER.error("User with id {} attempted to access files with no read permission", user.getId());
            throw new UserAuthenticationException();
        }
        LOGGER.debug("Getting files for course taught by professor with id {} of subject {}",
                course.getProfessor().getId(), course.getSubject().getId());
        return cfd.findForCourse(course);
    }

    @Override
    public CourseFile findByIdForUser(final long id, final User user) throws UserAuthenticationException {
        final CourseFile courseFile = cfd.findById(id);
        if(!canReadFiles(courseFile.getCourse(), user)){
            LOGGER.error("User with id {} attempted to access file with id {} with no read permission", user.getId());
            throw new UserAuthenticationException();
        }
        LOGGER.debug("Getting file with id {}", id);
        return courseFile;
    }

    @Override
    public CourseFile save(final Long professorId, final Long subjectId, final User currentUser,
                           final String fileName, final String description, final String contentType,
                           final byte[] file) throws UserAuthenticationException {
        
        LOGGER.debug("Saving file with name {} for profesor with id {} and subject id {}", fileName, professorId, subjectId);
        final Course course = cs.findCourseByIds(professorId, subjectId);


        if(course.getProfessor().getId().compareTo(currentUser.getId()) != 0) {
            LOGGER.error("User attempted to save file without permission");
            throw new UserAuthenticationException();
        }

        if(fileName == null || fileName.length() < 1 || description == null ||
                contentType == null || file == null || file.length < 1) {
            LOGGER.error("User attempted to save file with invalid parameters");
            return null;
        }

        if(description.length() > 255 || description.length() < 5){
            LOGGER.error("User attempted to save file with invalid description");
            return null;
        }
        return cfd.save(course, fileName, description, contentType, file);
    }

    @Override
    public void deleteById(final long id, final User user) throws UserAuthenticationException {
        final CourseFile courseFile = cfd.findById(id);
        if(!canWriteFile(courseFile, user)){
            LOGGER.error("User with id {} attempted to delete file with id {} with no permission", user.getId());
            throw new UserAuthenticationException();
        }
        LOGGER.debug("Deleting file with id {}", id);
        cfd.deleteById(id);
    }

    private boolean canReadFiles(final Course course, final User user){
        if(user == null) {
            return false;
        }
        return crs.hasAcceptedReservation(user, course) || course.getProfessor().getId().compareTo(user.getId()) == 0;
    }

    private boolean canWriteFile(final CourseFile courseFile, final User user) {
        if(user == null) {
            return false;
        }
        return courseFile.getCourse().getProfessor().getId().compareTo(user.getId()) == 0;
    }

}
