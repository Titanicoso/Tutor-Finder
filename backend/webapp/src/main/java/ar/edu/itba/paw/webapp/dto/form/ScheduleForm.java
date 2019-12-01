package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ScheduleForm {

    @NotNull
    @Min(1)
    @Max(23)
    private Integer startHour;

    @NotNull
    @Min(2)
    @Max(24)
    private Integer endHour;

    @NotNull
    @Min(1)
    @Max(7)
    private Integer day;

    public boolean validForm() {
        if(startHour == null || endHour == null)
            return false;
        return startHour < endHour;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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
