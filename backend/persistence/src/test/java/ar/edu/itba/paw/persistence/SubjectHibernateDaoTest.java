package ar.edu.itba.paw.persistence;

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
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class SubjectHibernateDaoTest {

    private static final String NAME = "Algebra";
    private static final String DESCRIPTION = "Complicado";
    private static final Long ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final Long AREA_ID = 1L;
    private static final String AREA_NAME = "matematica";
    private static final String AREA_DESCRIPTION = "este area es dificil";
    private static final Long USER_ID = 5L;

    @Autowired
    private DataSource ds;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SubjectHibernateDao subjectDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "messages");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "conversations");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "courses");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
        final Subject subject = subjectDao.create(NAME, DESCRIPTION, AREA_ID);
        em.flush();

        assertNotNull(subject);
        assertEquals(NAME, subject.getName());
        assertEquals(DESCRIPTION, subject.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "subjects"));
    }

    @Test
    public void testFindByIdValid() {
        final Subject subject = subjectDao.findById(ID).orElse(null);
        assertNotNull(subject);
        assertEquals(ID, subject.getId());
        assertEquals(NAME, subject.getName());
        assertEquals(DESCRIPTION, subject.getDescription());
        assertEquals(AREA_ID, subject.getArea().getId());
        assertEquals(AREA_DESCRIPTION, subject.getArea().getDescription());
        assertEquals(AREA_NAME, subject.getArea().getName());
    }

    @Test
    public void testFindByIdInvalid() {
        final Subject subject = subjectDao.findById(INVALID_ID).orElse(null);
        assertNull(subject);
    }

    @Test
    public void testFilterSubjectsByNameValid() {
        final String PARTIAL_NAME = "alg";
        final int SUBJECTS = 2;

        final List<Subject> subjects = subjectDao.filterSubjectsByName(PARTIAL_NAME);
        assertNotNull(subjects);
        assertEquals(SUBJECTS, subjects.size());
        final Subject subject = subjects.get(0);
        assertEquals(ID, subject.getId());
        assertEquals(NAME, subject.getName());
        assertEquals(DESCRIPTION, subject.getDescription());
        assertEquals(AREA_ID, subject.getArea().getId());
        assertEquals(AREA_DESCRIPTION, subject.getArea().getDescription());
        assertEquals(AREA_NAME, subject.getArea().getName());
    }

    @Test
    public void testFilterSubjectsByNameInvalid() {
        final String PARTIAL_NAME = "InvalidSubjectTest";

        final List<Subject> subjects = subjectDao.filterSubjectsByName(PARTIAL_NAME);
        assertNotNull(subjects);
        assertEquals(0, subjects.size());
    }

    @Test
    public void testGetAvailableSubjects() {
        final int REMAINING_SUBJECTS = 2;

        final List<Subject> subjects = subjectDao.getAvailableSubjects(USER_ID);
        assertNotNull(subjects);
        assertEquals(REMAINING_SUBJECTS, subjects.size());
        assertNotEquals(subjects.get(0).getId(), ID);
        assertNotEquals(subjects.get(1).getId(), ID);
    }
    
    @After
    public void tearDown(){
        cleanDatabase();
    }
}
