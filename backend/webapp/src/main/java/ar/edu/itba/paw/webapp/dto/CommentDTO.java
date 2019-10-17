package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Course;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class CommentDTO {

    private long id;
//    private UserDTO sender;
    private String comment;
    //TODO: Check created data type
    private String created;
    private int rating;

    @XmlElement(name = "course_url")
    private URI courseUrl;
    private URI url;

    public CommentDTO() {
    }

    public CommentDTO(final Comment comment, final URI baseUri) {
        this.id = comment.getId();
        this.created = comment.getCreated().toString();
        this.comment = comment.getComment();

        final Course course = comment.getCourse();

        this.courseUrl = baseUri.resolve("/courses/" + course.getProfessor().getId() + "_" +
                course.getSubject().getId());
        this.url = baseUri.resolve(this.courseUrl + "/comments");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public URI getCourseUrl() {
        return courseUrl;
    }

    public void setCourseUrl(URI courseUrl) {
        this.courseUrl = courseUrl;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
