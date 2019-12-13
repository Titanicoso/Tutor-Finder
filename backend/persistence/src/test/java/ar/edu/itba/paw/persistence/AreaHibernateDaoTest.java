package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Area;
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
public class AreaHibernateDaoTest {

    private static final String NAME = "matematica";
    private static final String DESCRIPTION = "este area es dificil";
    private static final String INVALID_NAME = "InvalidTestName";
    private static final byte[] TEST_IMAGE = new byte[1];
    private static final Long ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Autowired
    private AreaHibernateDao areaDao;

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
        cleanDatabase();
        final Area area = areaDao.create(NAME, DESCRIPTION, TEST_IMAGE);
        em.flush();

        assertNotNull(area);
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "areas"));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateInvalid() {
        final Area area = areaDao.create(NAME, DESCRIPTION, TEST_IMAGE);
        em.flush();

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "areas"));
    }

    @Test
    public void testFindByIdValid() {
        final Area area = areaDao.findById(ID);
        assertNotNull(area);
        assertEquals(ID, area.getId());
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
    }

    @Test
    public void testFindByIdInvalid() {
        final Area area = areaDao.findById(INVALID_ID);
        assertNull(area);
    }

    @Test
    public void filterAreasByNameValid() {
        final List<Area> areas = areaDao.filterAreasByName(NAME, LIMIT, OFFSET);
        assertNotNull(areas);
        assertEquals(1, areas.size());
        final Area area = areas.get(0);

        assertEquals(ID, area.getId());
        assertEquals(NAME, area.getName());
        assertEquals(DESCRIPTION, area.getDescription());
    }

    @Test
    public void filterAreasByNameInvalid() {
        final List<Area> areas = areaDao.filterAreasByName(INVALID_NAME, LIMIT, OFFSET);
        assertNotNull(areas);
        assertEquals(0, areas.size());
    }

    @After
    public void tearDown(){
        cleanDatabase();
    }

}
