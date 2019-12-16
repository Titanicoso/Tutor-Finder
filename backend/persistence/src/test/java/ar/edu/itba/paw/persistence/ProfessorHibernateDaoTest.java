package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class ProfessorHibernateDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProfessorHibernateDao professorDao;

    @PersistenceContext
    private EntityManager em;

    private JdbcTemplate jdbcTemplate;

    private Professor modifyTest;

    private static final String NAME = "Juan";
    private static final String SURNAME = "lopez";
    private static final String USERNAME = "Juancho";
    private static final String EMAIL = "juancito@gmail.com";
    private static final String PASSWORD = "dontbecruel";
    private static final Long ID = 2L;
    private static final String TEST_DESCRIPTION = "Juan es un profesor dedicado";
    private static final String NEW_TEST_DESCRIPTION = "Juan no es un profesor dedicado";
    private static final byte[] TEST_IMAGE = new byte[1];
    private static final byte[] NEW_TEST_IMAGE = {1};
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_USERNAME = "InvalidTestUsername";
    private static final String INVALID_FULL_NAME = "InvalidTestFullName";
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreateValid(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"messages");
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"conversations");
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"courses");
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"schedules");
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"professors");
        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn(EMAIL);
        when(mockUser.getId()).thenReturn(ID);
        when(mockUser.getLastname()).thenReturn(SURNAME);
        when(mockUser.getName()).thenReturn(NAME);
        when(mockUser.getUsername()).thenReturn(USERNAME);
        when(mockUser.getPassword()).thenReturn(PASSWORD);

        Professor professor = professorDao.create(mockUser, TEST_DESCRIPTION, TEST_IMAGE);
        assertNotNull(professor);
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "professors"));
    }

    @Test
    public void testFindByIdValid() {
        final Professor professor = professorDao.findById(ID).orElse(null);
        assertNotNull(professor);
        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFindByIdInvalid() {
        final Professor professor = professorDao.findById(INVALID_ID).orElse(null);
        assertNull(professor);
    }

    @Test
    public void testFindByUsernameValid() {
        final Professor professor = professorDao.findByUsername(USERNAME).orElse(null);
        assertNotNull(professor);
        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFindByUsernameInvalid() {
        final Professor professor = professorDao.findByUsername(INVALID_USERNAME).orElse(null);
        assertNull(professor);
    }

    @Test
    public void testFilterByFullNameValid() {
        final List<Professor> professors = professorDao.filterByFullName(NAME + " " + SURNAME, LIMIT, OFFSET);
        assertNotNull(professors);
        assertEquals(1,professors.size());
        final Professor professor = professors.get(0);

        assertEquals(ID, professor.getId());
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NAME, professor.getName());
        assertEquals(SURNAME, professor.getLastname());
        assertEquals(EMAIL, professor.getEmail());
        assertEquals(PASSWORD, professor.getPassword());
        assertEquals(TEST_DESCRIPTION, professor.getDescription());
    }

    @Test
    public void testFilterByFullNameInvalid() {
        final List<Professor> professors = professorDao.filterByFullName(INVALID_FULL_NAME, LIMIT, OFFSET);
        assertNotNull(professors);
        assertEquals(0,professors.size());
    }

    @Test
    public void testModifyDescription() {

        modifyTest = new Professor(ID, USERNAME, NAME, SURNAME, PASSWORD, EMAIL, TEST_DESCRIPTION, TEST_IMAGE);

        Professor professor = professorDao.modify(modifyTest, NEW_TEST_DESCRIPTION, null);
        assertNotNull(professor);
        assertEquals(USERNAME, professor.getUsername());
        assertEquals(NEW_TEST_DESCRIPTION, professor.getDescription());
    }


    @After
    public void tearDown(){
        cleanDatabase();
    }
}
