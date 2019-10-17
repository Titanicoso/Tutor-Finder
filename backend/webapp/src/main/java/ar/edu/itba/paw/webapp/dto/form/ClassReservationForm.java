package ar.edu.itba.paw.webapp.dto.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ClassReservationForm {

    @NotNull
    @Min(1)
    @Max(23)
    private Integer startHour;

    @NotNull
    @Min(2)
    @Max(24)
    private Integer endHour;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date day;


    public boolean validForm() {
        if(startHour == null || endHour == null)
            return false;
        return startHour < endHour;
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
}
