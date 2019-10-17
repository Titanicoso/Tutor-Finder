package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Course;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class CourseDTO {

//    private ProfessorDTO professor;
    private SubjectDTO subject;
    private String description;
    private Double price;
    private Double rating;

    @XmlElement(name = "course_comments_url")
    private URI courseCommentsUrl;

    @XmlElement(name = "course_files_url")
    private URI courseFilesUrl;

    private URI url;

    public CourseDTO() {
    }

    public CourseDTO(final Course course, final URI baseUri) {
        this.description = course.getDescription();
        this.price = course.getPrice();
        this.rating = course.getRating();

        this.subject = new SubjectDTO(course.getSubject(), baseUri);
        this.url = baseUri.resolve("/courses/" + course.getProfessor().getId() + "_" + course.getSubject().getId());
        this.courseCommentsUrl = baseUri.resolve(this.url + "/comments");
        this.courseFilesUrl = baseUri.resolve(this.url + "/files");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public URI getCourseCommentsUrl() {
        return courseCommentsUrl;
    }

    public void setCourseCommentsUrl(URI courseCommentsUrl) {
        this.courseCommentsUrl = courseCommentsUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public URI getCourseFilesUrl() {
        return courseFilesUrl;
    }

    public void setCourseFilesUrl(URI courseFilesUrl) {
        this.courseFilesUrl = courseFilesUrl;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }
}
