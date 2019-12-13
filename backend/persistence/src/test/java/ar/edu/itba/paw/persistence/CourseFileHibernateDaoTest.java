package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class CourseFileHibernateDaoTest {

    private static final Long SUBJECT_ID = 1L;
    private static final Long PROFESSOR_ID = 5L;
    private static final String DESCRIPTION = "Curso de algebra";
    private static final String FILENAME = "test.xml";
    private static final String TYPE = "xml";
    private static final byte[] TEST_FILE = {1};
    private static final Double PRICE = 240.0;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CourseFileHibernateDao cfd;

    private JdbcTemplate jdbcTemplate;

    private Course courseTest;
    private Professor professorTest;
    private Subject subjectTest;


    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        subjectTest = em.find(Subject.class, SUBJECT_ID);
        professorTest = em.find(Professor.class, PROFESSOR_ID);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreate() {

        courseTest = new Course(professorTest, subjectTest, DESCRIPTION, PRICE);
        courseTest.setComments(new LinkedList<>());

        final CourseFile file = cfd.save(courseTest, FILENAME, DESCRIPTION, TYPE, TEST_FILE);
        em.flush();

        assertNotNull(file);
        assertEquals(FILENAME, file.getName());
        assertEquals(DESCRIPTION, file.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_files"));
    }

    @After
    public void tearDown(){
        cleanDatabase();
    }

}
