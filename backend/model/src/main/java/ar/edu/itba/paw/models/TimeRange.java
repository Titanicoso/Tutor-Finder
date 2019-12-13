package ar.edu.itba.paw.models;

public class TimeRange {

    private Integer startHour;

    private Integer endHour;

    public TimeRange() { }

    public TimeRange(final Integer start, final Integer end) {
        this.startHour = start;
        this.endHour = end;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }
}
