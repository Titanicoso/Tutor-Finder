package ar.edu.itba.paw.webapp.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class RangeListDTO
{
    @XmlElement
    private Integer day;

    @XmlElement
    private List<TimeRangeDTO> ranges;

    private RangeListDTO() {}

    public RangeListDTO(Integer key, List<TimeRangeDTO> value)
    {
        this.day = key;
        this.ranges = value;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer key) {
        this.day = key;
    }

    public List<TimeRangeDTO> getRanges() {
        return ranges;
    }

    public void setRanges(List<TimeRangeDTO> value) {
        this.ranges = value;
    }
}
