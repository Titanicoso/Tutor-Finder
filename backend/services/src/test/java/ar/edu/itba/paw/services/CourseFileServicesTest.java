package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.persistence.CourseFileDao;
import ar.edu.itba.paw.interfaces.service.ClassReservationService;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CourseFileServicesTest {

    private static final byte[] TEST_FILE = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68
            , 82, 0, 0, 0, 2, 0, 0, 0, 3, 8, 6, 0, 0, 0, -71, -22, -34, -127, 0, 0, 0, 1, 115, 82
            , 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, -79, -113, 11
            , -4, 97, 5, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -62, 0, 0, 14, -62, 1
            , 21, 40, 74, -128, 0, 0, 0, 24, 73, 68, 65, 84, 24, 87, 99, -4, -13, -25, -45, 127
            , 6, 32, 96, 2, 17, 32, 0, 101, 48, 48, 0, 0, 96, 57, 3, -17, 68, 106, -31, 0, 0, 0, 0, 0
            , 73, 69, 78, 68, -82, 66, 96, -126};

    private static final Long   TEST_FILE_ID        = 1L;
    private static final Long   USER_ID             = 1L;
    private static final Long   PROFESSOR_ID        = 2L;
    private static final Long   SUBJECT_ID          = 2L;
    private static final String FILE_NAME           = "TEST FILE";
    private static final String FILE_DESCRIPTION    = "TEST FILE DESCRIPTION";
    private static final String FILE_CONTENT_TYPE   = "text/plain";



    @InjectMocks
    @Autowired
    private CourseFileService cfs;

    @Mock
    private CourseFileDao courseFileDao;

    @Mock
    private ClassReservationService crs;

    @Mock
    private CourseService cs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void userWithAccessCanGetFile() throws UserAuthenticationException {

        final User          user        = mock(User.class);
        final Course        course      = mock(Course.class);
        final CourseFile    courseFile  = mock(CourseFile.class);
        final Professor     professor   = mock(Professor.class);

        when(courseFile.getCourse()).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(courseFileDao.findById(TEST_FILE_ID)).thenReturn(courseFile);
        when(crs.hasAcceptedReservation(user,course)).thenReturn(true);

        CourseFile retFile = cfs.findByIdForUser(TEST_FILE_ID, user);

        assertNotNull(retFile);
        assertEquals(courseFile, retFile);
    }

    @Test(expected = UserAuthenticationException.class)
    public void userWithoutAccessCannotGetFile() throws UserAuthenticationException {
        final User          user        = mock(User.class);
        final Course        course      = mock(Course.class);
        final CourseFile    courseFile  = mock(CourseFile.class);
        final Professor     professor   = mock(Professor.class);

        when(courseFile.getCourse()).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(courseFileDao.findById(TEST_FILE_ID)).thenReturn(courseFile);
        when(crs.hasAcceptedReservation(user,course)).thenReturn(false);
        CourseFile retFile = cfs.findByIdForUser(TEST_FILE_ID, user);

    }

    @Test
    public void userWithAccessCanGetCourseFiles() throws UserAuthenticationException {
        final User          user            = mock(User.class);
        final Course        course          = mock(Course.class);
        final CourseFile    courseFile      = mock(CourseFile.class);
        final Professor     professor       = mock(Professor.class);
        final Subject       subject         = mock(Subject.class);
        final List<CourseFile> courseFiles  = new LinkedList<>();

        courseFiles.add(courseFile);
        when(courseFile.getCourse()).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(course.getSubject()).thenReturn(subject);
        when(subject.getId()).thenReturn(SUBJECT_ID);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(courseFileDao.findForCourse(course)).thenReturn(courseFiles);
        when(crs.hasAcceptedReservation(user,course)).thenReturn(true);

        List<CourseFile> retFiles = cfs.findForCourse(course, user);
        assertEquals(courseFiles, retFiles);
    }

    @Test(expected = UserAuthenticationException.class)
    public void userWithoutAccessCannotGetCourseFiles() throws UserAuthenticationException {
        final User          user            = mock(User.class);
        final Course        course          = mock(Course.class);
        final CourseFile    courseFile      = mock(CourseFile.class);
        final Professor     professor       = mock(Professor.class);
        final Subject       subject         = mock(Subject.class);
        final List<CourseFile> courseFiles  = new LinkedList<>();

        courseFiles.add(courseFile);
        when(courseFile.getCourse()).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(course.getSubject()).thenReturn(subject);
        when(subject.getId()).thenReturn(SUBJECT_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(courseFileDao.findForCourse(course)).thenReturn(courseFiles);
        when(crs.hasAcceptedReservation(user,course)).thenReturn(false);

        List<CourseFile> retFiles = cfs.findForCourse(course, user);
        assertEquals(courseFiles, retFiles);
    }

    @Test(expected = UserAuthenticationException.class)
    public void NonProfessorSaveFileTest() throws UserAuthenticationException {
        final User user = mock(User.class);
        final Course        course          = mock(Course.class);
        final Professor professor = mock(Professor.class);
        final CourseFile courseFile  = mock(CourseFile.class);
        when(cs.findCourseByIds(PROFESSOR_ID, SUBJECT_ID)).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(courseFileDao.save(course, FILE_NAME, FILE_DESCRIPTION,
                FILE_CONTENT_TYPE, TEST_FILE)).thenReturn(courseFile);

        CourseFile retFile = cfs.save(PROFESSOR_ID, SUBJECT_ID, user, FILE_NAME,
                FILE_DESCRIPTION, FILE_CONTENT_TYPE, TEST_FILE);
        assertEquals(courseFile, retFile);
    }

    @Test
    public void ValidSaveFileTest() throws UserAuthenticationException {
        final Course        course          = mock(Course.class);
        final Professor professor = mock(Professor.class);
        final CourseFile courseFile  = mock(CourseFile.class);
        when(cs.findCourseByIds(PROFESSOR_ID, SUBJECT_ID)).thenReturn(course);
        when(course.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(PROFESSOR_ID);
        when(courseFileDao.save(course, FILE_NAME, FILE_DESCRIPTION,
                FILE_CONTENT_TYPE, TEST_FILE)).thenReturn(courseFile);

        CourseFile retFile = cfs.save(PROFESSOR_ID, SUBJECT_ID, professor, FILE_NAME,
                FILE_DESCRIPTION, FILE_CONTENT_TYPE, TEST_FILE);
        assertEquals(courseFile, retFile);
    }
}
