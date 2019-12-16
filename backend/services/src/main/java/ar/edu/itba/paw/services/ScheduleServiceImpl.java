package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.NonexistentProfessorException;
import ar.edu.itba.paw.exceptions.TimeslotAllocatedException;
import ar.edu.itba.paw.interfaces.persistence.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.ScheduleService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Schedule;
import ar.edu.itba.paw.models.TimeRange;
import ar.edu.itba.paw.models.Timeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleDao sd;

    @Autowired
    private ProfessorService ps;

    @Transactional
    @Override
    public List<Timeslot> reserveTimeSlot(final Long professor_id, final Integer day, final Integer startTime, final Integer endTime)
            throws TimeslotAllocatedException, NonexistentProfessorException {

        if(startTime >= endTime) {
            LOGGER.error("Attempted to reserve timeslot with an invalid time range");
            return null;
        }
        if(startTime > 23 || startTime < 1 || endTime > 24 || endTime < 2) {
            LOGGER.error("Attempted to reserve timeslot with invalid start time or end time");
            return null;
        }

        Professor professor = ps.findById(professor_id);

        if(professor == null) {
            LOGGER.error("Attempted to reserve timeslot for non existent professor");
            throw new NonexistentProfessorException();
        }

        List<Timeslot> list = new ArrayList<>();
        for (int i = startTime; i < endTime; i++) {
            LOGGER.debug("Reserving timeslot for professor with id {}, with day {}, at hour {}", professor_id,
                    day, i);
            Timeslot timeslot = sd.reserveTimeSlot(professor, day, i);

            list.add(timeslot);
        }
        return list;
    }

    @Override
    @Transactional
    public boolean removeTimeSlot(final Long professor_id, final Integer day, final Integer startTime, final Integer endTime) throws NonexistentProfessorException {
        Professor professor = ps.findById(professor_id);
        if(professor == null)
            throw new NonexistentProfessorException();

        for (int i = startTime; i < endTime; i++) {
            LOGGER.debug("Removing timeslot for professor with id {}, with day {}, at hour {}", professor_id,
                    day, i);
            sd.removeTimeSlot(professor, day, i);
        }
        return true;
    }

    @Override
    @Transactional
    public Schedule getScheduleForProfessor(final Long professorId) {

        LOGGER.debug("Getting schedule for professor with id {}", professorId);
        final Professor professor = ps.findById(professorId);

        List<Timeslot> timeslots = sd.getTimeslotsForProfessor(professor);

        Map<Integer, List<TimeRange>> days = new HashMap<>();

        timeslots.stream()
                .sorted(Comparator.comparingInt(Timeslot::getDay).thenComparingInt(Timeslot::getHour))
                .forEachOrdered(t -> {
                    List<TimeRange> timeRanges = days.get(t.getDay());

                    if (timeRanges == null) {
                        timeRanges = new ArrayList<>();
                    }

                    final Optional<TimeRange> range = timeRanges.stream().filter(r ->
                            r.getStartHour() == t.getHour() + 1 ||
                            r.getEndHour().equals(t.getHour())).findFirst();

                    if (range.isPresent()) {
                        TimeRange timeRange = range.get();
                        if (t.getHour().equals(timeRange.getEndHour())) {
                            timeRange.setEndHour(t.getHour() + 1);
                        } else {
                            timeRange.setStartHour(t.getHour());
                        }
                    } else {
                        TimeRange timeRange = new TimeRange(t.getHour(), t.getHour() + 1);
                        timeRanges.add(timeRange);
                    }
                    days.put(t.getDay(), timeRanges);
                });

        return new Schedule(days);
    }
}
