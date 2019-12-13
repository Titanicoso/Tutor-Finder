package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EmailAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAlreadyInUseException;
import ar.edu.itba.paw.exceptions.UsernameAndEmailAlreadyInUseException;
import ar.edu.itba.paw.models.ClassReservation;
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
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class UserHibernateDaoTest {

    private static final String NAME = "Juan";
    private static final String SURNAME = "lopez";
    private static final String USERNAME = "Juancho";
    private static final String EMAIL = "juancito@gmail.com";
    private static final String PASSWORD = "dontbecruel";
    private static final String NEW_PASSWORD = "becruel";
    private static final Long ID = 2L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_USERNAME = "InvalidTestUsername";
    private static final String INVALID_EMAIL = "InvalidTestEmail";
    private static final Long SUBJECT_ID = 1L;
    private static final Long PROFESSOR_ID = 5L;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHibernateDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreateValid() throws UsernameAndEmailAlreadyInUseException, EmailAlreadyInUseException, UsernameAlreadyInUseException {
        cleanDatabase();
        final User user = userDao.create(USERNAME, PASSWORD, EMAIL, NAME, SURNAME);
        em.flush();

        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

    @Test(expected = PersistenceException.class)
    public void testCreateRepeatUsername() throws UsernameAndEmailAlreadyInUseException, EmailAlreadyInUseException, UsernameAlreadyInUseException {
        final String otherEmail = "OtherEmailTest";
        final User user = userDao.create(USERNAME, PASSWORD, otherEmail, NAME, SURNAME);
        em.flush();

        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateRepeatEmail() throws UsernameAndEmailAlreadyInUseException, EmailAlreadyInUseException, UsernameAlreadyInUseException {
        final String otherUsername = "OtherUsernameTest";
        final User user = userDao.create(otherUsername, PASSWORD, EMAIL, NAME, SURNAME);
        em.flush();

        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateRepeatEmailAndPassword() throws UsernameAndEmailAlreadyInUseException, EmailAlreadyInUseException, UsernameAlreadyInUseException {
        final User user = userDao.create(USERNAME, PASSWORD, EMAIL, NAME, SURNAME);
        em.flush();

        assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testFindByIdValid() {
        final User user = userDao.findById(ID).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByIdInvalid() {
        final User user = userDao.findById(INVALID_ID).orElse(null);
        assertNull(user);
    }

    @Test
    public void testFindByUsernameValid() {
        final User user = userDao.findByUsername(USERNAME).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByUsernameInvalid() {
        final User user = userDao.findByUsername(INVALID_USERNAME).orElse(null);
        assertNull(user);
    }

    @Test
    public void testFindByEmailValid() {
        final User user = userDao.findByEmail(EMAIL).orElse(null);
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(NAME, user.getName());
        assertEquals(SURNAME, user.getLastname());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testFindByEmailInvalid() {
        final User user = userDao.findByEmail(INVALID_EMAIL).orElse(null);
        assertNull(user);
    }

    @Test
    public void testChangePasswordByIdValid() {
        final boolean changed = userDao.changePasswordById(ID, NEW_PASSWORD);
        assertTrue(changed);
    }

    @Test
    public void testChangePasswordByIdInvalid() {
        final boolean changed = userDao.changePasswordById(INVALID_ID, NEW_PASSWORD);
        assertFalse(changed);
    }

    @Test
    public void testPagedReservationsValid() {
        final List<ClassReservation> classReservations = userDao.pagedReservations(ID, 3, 0);
        assertNotNull(classReservations);
        assertEquals(2, classReservations.size());
        final ClassReservation classReservation = classReservations.get(0);

        assertEquals(USERNAME, classReservation.getStudent().getUsername());
        assertEquals(SUBJECT_ID, classReservation.getCourse().getSubject().getId());
        assertEquals(PROFESSOR_ID, classReservation.getCourse().getProfessor().getId());
    }

    @Test
    public void testPagedReservationsInvalid() {
        final List<ClassReservation> classReservations = userDao.pagedReservations(INVALID_ID, 3, 0);
        assertNotNull(classReservations);
        assertEquals(0, classReservations.size());
    }

    @After
    public void tearDown(){
        cleanDatabase();
    }

}
