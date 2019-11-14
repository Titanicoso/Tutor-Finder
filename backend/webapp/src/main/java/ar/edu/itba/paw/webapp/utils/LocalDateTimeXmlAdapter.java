package ar.edu.itba.paw.webapp.utils;

import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.LocalDateTime;

@XmlTransient
public class LocalDateTimeXmlAdapter extends XmlAdapter<Date, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(Date date) {
        return new LocalDateTime(date);
    }

    @Override
    public Date marshal(LocalDateTime dateTime) {
        return dateTime.toDate();
    }

}