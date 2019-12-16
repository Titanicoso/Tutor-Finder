package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.NonexistentSubjectException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
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
import java.util.Optional;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CourseServiceImplTest {

    private static final int PAGE_SIZE = 5;
    private static final Long PROFESSOR_ID = 1L;
    private static final Long SUBJECT_ID = 2L;
    private static final Long INVALID_ID = 666L;
    private static final Long AREA_ID = 3L;
    private static final String DESCRIPTION = "Omne duo vim sum nudi uno quod. Latera nullam ad realem passim ii essent ut patere";
    private static final Double PRICE = 40.5;
    private static final String NAME = "Alg";

    @InjectMocks
    @Autowired
    private CourseService courseService;

    @Mock
    private CourseDao courseDao;

    @Mock
    private ProfessorService professorService;

    @Mock
    private SubjectService subjectService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCoursesByNameHasNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByFilter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn((long) PAGE_SIZE + 1);

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterCoursesByNameNoNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }

        when(courseDao.filter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString(), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByFilter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn((long) RESULT_NUMBER);

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test
    public void testFilterCoursesByNamePageOutOfBounds() {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);

        when(courseDao.filter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), eq(NAME), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByFilter(anyListOf(Integer.class), anyInt(), anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn(0L);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, INVALID_PAGE);
        assertEquals(0L, results.getResults().size());
    }

    @Test
    public void testFilterCoursesByNameNegativePage() {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.filterCourses(null,null, null, null, null, NAME, INVALID_PAGE);
        assertNull(results);
    }

    @Test
    public void testCreateValid() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        final Course course = mock(Course.class);
        when(course.getProfessor()).thenReturn(professor);
        when(course.getSubject()).thenReturn(subject);
        when(courseDao.create(professor, subject, DESCRIPTION, PRICE)).thenReturn(course);
        when(courseDao.findByIds(professor.getId(), subject.getId())).thenReturn(Optional.empty());

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
        assertNotNull(created);
        assertEquals(PROFESSOR_ID, course.getProfessor().getId());
        assertEquals(SUBJECT_ID, course.getSubject().getId());
    }

    @Test
    public void testCreateInvalidPrice() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Double INVALID_PRICE = -5.0;

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, INVALID_PRICE);
        assertNull(created);
    }

    @Test
    public void testCreateInvalidDescription() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final String shortDescription = "short";
        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, shortDescription, PRICE);
        assertNull(created);
    }

    @Test(expected = NonexistentProfessorException.class)
    public void testCreateNonExistentProfessor() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {
        when(professorService.findById(PROFESSOR_ID)).thenReturn(null);

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test(expected = NonexistentSubjectException.class)
    public void testCreateNonExistentSubject() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(null);

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test(expected = CourseAlreadyExistsException.class)
    public void testCreateCourseAlreadyExists() throws NonexistentProfessorException, CourseAlreadyExistsException, NonexistentSubjectException {

        final Professor professor = mock(Professor.class);
        when(professor.getId()).thenReturn(PROFESSOR_ID);

        final Subject subject = mock(Subject.class);
        when(subject.getId()).thenReturn(SUBJECT_ID);

        when(professorService.findById(PROFESSOR_ID)).thenReturn(professor);
        when(subjectService.findSubjectById(SUBJECT_ID)).thenReturn(subject);

        final Course course = mock(Course.class);
        when(course.getProfessor()).thenReturn(professor);
        when(course.getSubject()).thenReturn(subject);
        when(courseDao.create(professor, subject, DESCRIPTION, PRICE)).thenThrow(new CourseAlreadyExistsException());
        when(courseDao.findByIds(professor.getId(), subject.getId())).thenReturn(Optional.of(course));

        final Course created = courseService.create(PROFESSOR_ID, SUBJECT_ID, DESCRIPTION, PRICE);
    }

    @Test
    public void testFindCoursesByProfessorIdHasNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByProfessorId(eq(PROFESSOR_ID))).thenReturn((long) PAGE_SIZE + 1);

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFindCoursesByProfessorIdNoNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByProfessorId(eq(PROFESSOR_ID))).thenReturn((long) RESULT_NUMBER);

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test
    public void testFindCoursesByProfessorIdPageOutOfBounds() {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);
        when(courseDao.findByProfessorId(eq(PROFESSOR_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByProfessorId(eq(PROFESSOR_ID))).thenReturn(0L);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, INVALID_PAGE);
        assertEquals(0L, results.getResults().size());
    }

    @Test
    public void testFindCoursesByProfessorIdNegativePage() {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.findCourseByProfessorId(PROFESSOR_ID, INVALID_PAGE);
        assertNull(results);
    }

    @Test
    public void testFilterByAreaIdHasNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        for (int i = 0; i < PAGE_SIZE; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByAreaId(eq(AREA_ID))).thenReturn((long) PAGE_SIZE + 1);

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, PAGE);
        assertTrue(results.isHasNext());
        assertEquals(PAGE_SIZE, results.getResults().size());
    }

    @Test
    public void testFilterByAreaIdNoNext() {
        final List<Course> courses = new LinkedList<>();
        final Integer PAGE = 1;
        final int RESULT_NUMBER = PAGE_SIZE - 1;
        for (int i = 0; i < RESULT_NUMBER; i++) {
            courses.add(mock(Course.class));
        }
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByAreaId(eq(AREA_ID))).thenReturn((long) RESULT_NUMBER);

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, PAGE);
        assertFalse(results.isHasNext());
        assertEquals(RESULT_NUMBER, results.getResults().size());
    }

    @Test
    public void testFilterByAreaIdPageOutOfBounds() {
        final List<Course> courses = mock(List.class);
        when(courses.size()).thenReturn(0);
        when(courseDao.filterByAreaId(eq(AREA_ID), anyInt(), anyInt())).thenReturn(courses);
        when(courseDao.totalByAreaId(eq(AREA_ID))).thenReturn(0L);

        final Integer INVALID_PAGE = 999;

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, INVALID_PAGE);
        assertEquals(0L, results.getResults().size());
    }

    @Test
    public void testFilterByAreaIdNegativePage() {

        final Integer INVALID_PAGE = -2;

        final PagedResults<Course> results = courseService.filterByAreaId(AREA_ID, INVALID_PAGE);
        assertNull(results);
    }
}
