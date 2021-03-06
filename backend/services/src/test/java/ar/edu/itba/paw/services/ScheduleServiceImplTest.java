package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Timeslot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ScheduleServiceImplTest {

    @InjectMocks
    @Autowired
    private ScheduleService scheduleService;

    @Mock
    private ScheduleDao scheduleDao;

    @Mock
    private ProfessorService ps;

    private static final Long PROFESSOR_ID = 2L;
    private static final Long INVALID_ID = 666L;

    @Before
    public void setUp() throws TimeslotAllocatedException {
        MockitoAnnotations.initMocks(this);
        final Professor professor = new Professor(2L, "username","Carlos","Ramos",
                "password","carlitos@gmail.com","test description", new byte[1]);
        when(scheduleDao.reserveTimeSlot(professor,1, 12)).thenReturn( new Timeslot(1,12, professor));
        when(scheduleDao.reserveTimeSlot(professor,1, 14)).thenReturn( new Timeslot(1,14, professor));
        when(scheduleDao.reserveTimeSlot(professor,1, 15)).thenReturn( new Timeslot(1,15, professor));
        when(scheduleDao.reserveTimeSlot(professor,1, 16)).thenReturn( new Timeslot(1,16, professor));
        when(scheduleDao.reserveTimeSlot(professor,1, -3)).thenReturn( new Timeslot(1,-3, professor));
        when(scheduleDao.reserveTimeSlot(professor,1, 27)).thenReturn( new Timeslot(1,27, professor));
        when(ps.findById(2L)).thenReturn(professor);
    }

    @Test
    public void testValidRange() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 16;
        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertEquals(2, timeslots.size());
    }

    @Test
    public void testUnitaryRange() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 15;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);

        assertEquals(1, timeslots.size());
        Timeslot timeslot = timeslots.iterator().next();

        //The timeslot hour is the start hour and timeslots are all one hr long
        assertEquals(START_HOUR, timeslot.getHour());
        assertEquals(DAY, timeslot.getDay());
    }

    @Test
    public void testInvalidRange() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertNull(timeslots);
    }

    @Test
    public void testInvalidHourUpper() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = -3;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertNull(timeslots);
    }

    @Test
    public void testInvalidHourLower() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 27;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertNull(timeslots);
    }


    @Test
    public void testInvalidDayUpper() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 8;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;

        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertNull(timeslots);
    }

    @Test
    public void testInvalidDayLower() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = -4;
        Integer START_HOUR = 14;
        Integer END_HOUR = 12;
        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(PROFESSOR_ID, DAY, START_HOUR, END_HOUR);
        assertNull(timeslots);
    }

    @Test(expected = NonexistentProfessorException.class)
    public void testInvalidProfessor() throws TimeslotAllocatedException, NonexistentProfessorException {
        Integer DAY = 1;
        Integer START_HOUR = 14;
        Integer END_HOUR = 16;
        List<Timeslot> timeslots = scheduleService.reserveTimeSlot(INVALID_ID, DAY, START_HOUR, END_HOUR);

    }
}
