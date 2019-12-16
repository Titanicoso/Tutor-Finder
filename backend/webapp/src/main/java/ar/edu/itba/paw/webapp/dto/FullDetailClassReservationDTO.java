package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.ClassReservationStatus;
import ar.edu.itba.paw.webapp.utils.LocalDateTimeXmlAdapter;
import org.joda.time.LocalDateTime;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;

@XmlRootElement
public class FullDetailClassReservationDTO {

    private long id;
    private UserDTO student;
    private CourseDTO course;

    @XmlElement(name = "start_time")
    @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeXmlAdapter.class)
    private LocalDateTime startTime;

    @XmlElement(name = "end_time")
    @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeXmlAdapter.class)
    private LocalDateTime endTime;

    private ClassReservationStatus status;
    private String comment;

    private URI url;

    public FullDetailClassReservationDTO() {
    }

    public FullDetailClassReservationDTO(final ClassReservation cs, final UriInfo uriInfo) {
        this.id = cs.getClassRequestId();
        this.comment = cs.getComment();
        this.endTime = cs.getEndTime();
        this.startTime = cs.getStartTime();
        this.student = new UserDTO(cs.getStudent(), uriInfo.getBaseUri(), false);

        switch(cs.getStatus()) {
            case 0: this.status = ClassReservationStatus.APPROVED; break;
            case 1: this.status = ClassReservationStatus.DENIED; break;
            case 2: this.status = ClassReservationStatus.UNSPECIFIED; break;
        }

        this.course = new CourseDTO(cs.getCourse(), uriInfo);

        this.url = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public ClassReservationStatus getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public URI getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(ClassReservationStatus status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }
}
