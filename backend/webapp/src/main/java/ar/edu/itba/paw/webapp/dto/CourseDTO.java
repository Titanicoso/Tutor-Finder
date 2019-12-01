package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Course;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class CourseDTO {

    private ProfessorDTO professor;
    private SubjectDTO subject;
    private String description;
    private Double price;
    private Double rating;

    @XmlElement(name = "course_comments_url")
    private URI courseCommentsUrl;

    @XmlElement(name = "course_files_url")
    private URI courseFilesUrl;

    @XmlElement(name = "image_url")
    private URI imageUrl;

    private URI url;

    private boolean canComment;

    public CourseDTO() {
    }

    public CourseDTO(final Course course, final UriInfo uriInfo) {
        this.description = course.getDescription();
        this.price = course.getPrice();
        this.rating = course.getRating();

        final URI baseUri = uriInfo.getBaseUri();

        this.subject = new SubjectDTO(course.getSubject(), baseUri);
        this.url = baseUri.resolve("courses/" + course.getProfessor().getId() + "_" + course.getSubject().getId());
        this.courseCommentsUrl = baseUri.resolve(this.url + "/comments");
        this.courseFilesUrl = baseUri.resolve(this.url + "/files");
        this.professor = new ProfessorDTO(course.getProfessor(), uriInfo);
        this.imageUrl = baseUri.resolve("areas/" + course.getSubject().getArea().getId() + "/image");
        this.canComment = false;
    }

    public CourseDTO(final Course course, final UriInfo uriInfo, final boolean canComment) {
        this(course, uriInfo);
        this.canComment = canComment;
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

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }

    public URI getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(boolean canComment) {
        this.canComment = canComment;
    }
}
