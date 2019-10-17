package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ClassReservation;
import ar.edu.itba.paw.models.ClassReservationStatus;
import ar.edu.itba.paw.models.Course;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class ClassReservationDTO {

    private long id;
//    private UserDTO student;

    @XmlElement(name = "course_url")
    private URI courseUrl;

    //TODO: Check data type
    @XmlElement(name = "start_time")
    private String startTime;

    //TODO: Check data type
    @XmlElement(name = "end_time")
    private String endTime;

    //TODO: Check representation
    private ClassReservationStatus status;
    private String comment;

    private URI url;

    public ClassReservationDTO() {
    }

    public ClassReservationDTO(final ClassReservation cs, final UriInfo uriInfo) {
        this.id = cs.getClassRequestId();
        this.comment = cs.getComment();
        this.endTime = cs.getEndTime().toString();
        this.startTime = cs.getStartTime().toString();

        switch(cs.getStatus()) {
            case 0: this.status = ClassReservationStatus.APPROVED; break;
            case 1: this.status = ClassReservationStatus.DENIED; break;
            case 2: this.status = ClassReservationStatus.UNSPECIFIED; break;
        }

        final Course course = cs.getCourse();
        this.courseUrl = uriInfo.getBaseUri().resolve("/courses/" + course.getProfessor().getId() + "_" +
                course.getSubject().getId());

        this.url = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
    }

    public long getId() {
        return id;
    }

    public URI getCourseUrl() {
        return courseUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
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

    public void setCourseUrl(URI courseUrl) {
        this.courseUrl = courseUrl;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
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
}
