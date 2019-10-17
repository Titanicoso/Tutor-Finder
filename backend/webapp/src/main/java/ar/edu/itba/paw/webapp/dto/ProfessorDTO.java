package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class ProfessorDTO extends UserDTO {

    private String description;

    @XmlElement(name = "image_url")
    private URI imageUrl;

    @XmlElement(name = "course_url")
    private URI coursesUrl;

    @XmlElement(name = "schedule_url")
    private URI scheduleUrl;

    private URI url;

    public ProfessorDTO() {}

    public ProfessorDTO(final Professor professor, final UriInfo uriInfo){
        super(professor.getId(), professor.getUsername(), professor.getName(),
                professor.getLastname(), professor.getEmail());

        this.description = professor.getDescription();
        this.imageUrl = uriInfo.getBaseUri().resolve("/professors/" + professor.getUsername() + "/image");
        this.coursesUrl = uriInfo.getAbsolutePathBuilder().path("/courses").build();
        this.scheduleUrl = uriInfo.getAbsolutePathBuilder().path("/schedule").build();
        this.url = uriInfo.getAbsolutePathBuilder().build();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }

    public URI getCoursesUrl() {
        return coursesUrl;
    }

    public void setCoursesUrl(URI coursesUrl) {
        this.coursesUrl = coursesUrl;
    }

    public URI getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(URI scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
