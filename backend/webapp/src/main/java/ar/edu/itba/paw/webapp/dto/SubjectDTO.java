package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Subject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class SubjectDTO {

    private long id;
    private String name;
    private String description;

    @XmlElement(name = "area_url")
    private URI areaUrl;

    public SubjectDTO() {
    }

    public SubjectDTO(final Subject subject, final URI baseUri) {
        this.id = subject.getId();
        this.description = subject.getDescription();
        this.name = subject.getName();
        this.areaUrl = baseUri.resolve("areas/" + subject.getArea().getId());
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

    public URI getAreaUrl() {
        return areaUrl;
    }

    public void setAreaUrl(URI areaUrl) {
        this.areaUrl = areaUrl;
    }
}
