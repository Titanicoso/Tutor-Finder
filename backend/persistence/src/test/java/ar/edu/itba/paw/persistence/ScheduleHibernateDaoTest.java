package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateTestConfig.class)
@Sql("classpath:schema.sql")
@Transactional
public class ScheduleHibernateDaoTest {


    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ScheduleHibernateDao hibernateDao;

    private JdbcTemplate jdbcTemplate;

    private final String TEST_DESCRIPTION = "test description";
    private Professor testProfessor;
    private Professor testProfessorOcupied;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        testProfessor = em.find(Professor.class, 2L);
        testProfessorOcupied = em.find(Professor.class, 5L);
    }

    public void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK");
    }
    
    @Test
    public void testReserveValid() {
        Integer DAY = 1;
        Integer HOUR = 4;

        Timeslot reservedTimeSlot = hibernateDao.reserveTimeSlot(testProfessor, DAY,HOUR);
        em.flush();

        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }

    @Test
    public void testGetTimeslotsForProfessor() {
        final Integer DAY = 2;
        final Integer HOUR = 2;

        List<Timeslot> reservedTimeSlots = hibernateDao.getTimeslotsForProfessor(testProfessorOcupied);
        assertNotNull(reservedTimeSlots);
        assertEquals(1, reservedTimeSlots.size());
        Timeslot reservedTimeSlot = reservedTimeSlots.get(0);

        assertEquals(DAY, reservedTimeSlot.getDay());
        assertEquals(HOUR, reservedTimeSlot.getHour());
    }

    @Test
    public void testDeleteValid() {
        final int DAY = 2;
        final int HOUR = 2;

        final boolean removed = hibernateDao.removeTimeSlot(testProfessorOcupied, DAY,HOUR);
        em.flush();

        assertTrue(removed);
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }

    @Test
    public void testDeleteInvalid() {
        Integer DAY = 1;
        Integer HOUR = 4;

        final boolean removed = hibernateDao.removeTimeSlot(testProfessorOcupied, DAY,HOUR);
        em.flush();

        assertFalse(removed);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "schedules"));
    }


    @After
    public void tearDown(){
        cleanDatabase();
    }
}
