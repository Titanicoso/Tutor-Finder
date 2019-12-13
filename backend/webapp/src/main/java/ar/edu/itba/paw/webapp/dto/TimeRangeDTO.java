package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeRangeDTO {

    private Integer start;
    private Integer end;

    public TimeRangeDTO() {
    }

    public TimeRangeDTO(final Integer startHour, final Integer endHour) {
        this.start = startHour;
        this.end = endHour;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer startHour) {
        this.start = startHour;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer endHour) {
        this.end = endHour;
    }
}
