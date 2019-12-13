package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PasswordResetToken;
import org.joda.time.LocalDateTime;
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
import java.util.UUID;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class PasswordResetTokenHibernateDaoTest {

    private static final String RANDOM_UUID = UUID.randomUUID().toString();
    private static final String TOKEN = "123e4567-e89b-12d3-a456-556642440000";
    private static final String EXPIRED_TOKEN = "123e4567-e89b-10d1-a112-131415161718";
    private static final Long ID = 1L;
    private static final Long USER_ID = 5L;
    private static final Long INVALID_ID = 666L;
    private static final String INVALID_TOKEN = "123e4567-e89b-12d3-a456-666666666666";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordResetTokenHibernateDao passwordResetTokenDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }

    @Test
    public void testCreateValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"reset_password_tokens");
        final PasswordResetToken token = passwordResetTokenDao.create(USER_ID, RANDOM_UUID,
                LocalDateTime.now().plusDays(1));

        assertNotNull(token);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }

    @Test
    public void testCreateInvalid() {
        final PasswordResetToken token = passwordResetTokenDao.create(INVALID_ID, RANDOM_UUID,
                LocalDateTime.now().plusDays(1));
        assertNull(token);
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }


    @Test
    public void testFindByTokenValid() {
        final PasswordResetToken token = passwordResetTokenDao.findByToken(TOKEN);
        assertNotNull(token);
        assertEquals(ID, token.getId());
        assertEquals(USER_ID, token.getUser().getId());
        assertEquals(TOKEN, token.getToken());
    }

    @Test
    public void testFindByTokenInvalid() {
        final PasswordResetToken token = passwordResetTokenDao.findByToken(INVALID_TOKEN);
        assertNull(token);
    }

    @Test
    public void testFindByTokenExpired() {
        final PasswordResetToken token = passwordResetTokenDao.findByToken(EXPIRED_TOKEN);
        assertNull(token);
    }

    @Test
    public void testPurgeExpiredTokens() {
        passwordResetTokenDao.purgeExpiredTokens();
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }

    @Test
    public void testDeleteUsedTokenValid() {
       passwordResetTokenDao.deleteUsedToken(TOKEN);
       assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }

    @Test
    public void testDeleteUsedTokenInvalid() {
        passwordResetTokenDao.deleteUsedToken(INVALID_TOKEN);
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "reset_password_tokens"));
    }

    @After
    public void tearDown(){
        cleanDatabase();
    }
}
