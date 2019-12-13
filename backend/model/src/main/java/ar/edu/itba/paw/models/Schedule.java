package ar.edu.itba.paw.models;

import java.util.List;
import java.util.Map;

public class Schedule {

    private final Map<Integer, List<TimeRange>> days;

    public Schedule(Map<Integer, List<TimeRange>> days) {
        this.days = days;
    }

    public Map<Integer, List<TimeRange>> getDays() {
        return days;
    }
}
