package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class CourseFileDTO {

    private long id;
    private String name;
    private String description;

    @XmlElement(name = "course_url")
    private URI courseUrl;

    @XmlElement(name = "content_url")
    private URI contentUri;

    public CourseFileDTO() {
    }

    public CourseFileDTO(final CourseFile file, final URI baseUri) {
        this.id = file.getId();
        this.name = file.getName();
        this.description = file.getDescription();

        final Course course = file.getCourse();

        this.courseUrl = baseUri.resolve("/courses/" + course.getProfessor().getId() + "_" +
                course.getSubject().getId());
        this.contentUri = baseUri.resolve(this.courseUrl + "/files/" + id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getCourseUrl() {
        return courseUrl;
    }

    public void setCourseUrl(URI courseUrl) {
        this.courseUrl = courseUrl;
    }

    public URI getContentUri() {
        return contentUri;
    }

    public void setContentUri(URI contentUri) {
        this.contentUri = contentUri;
    }
}
