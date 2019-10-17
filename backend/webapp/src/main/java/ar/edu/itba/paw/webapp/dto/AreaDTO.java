package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Area;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class AreaDTO {

    private long id;
    private String name;
    private String description;
    private URI url;

    @XmlElement(name = "area_courses_url")
    private URI areaCoursesUrl;

    @XmlElement(name = "area_image_url")
    private URI areaImageUrl;

    public AreaDTO() {
    }

    public AreaDTO(final Area area, final URI baseUri) {
        this.id = area.getId();
        this.description = area.getDescription();
        this.name = area.getName();
        this.url = baseUri.resolve("areas/" + id);
        this.areaCoursesUrl = baseUri.resolve("areas/" + id + "/courses");
        this.areaImageUrl = baseUri.resolve("areas/" + id + "/image");
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

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public URI getAreaCoursesUrl() {
        return areaCoursesUrl;
    }

    public void setAreaCoursesUrl(URI areaCoursesUrl) {
        this.areaCoursesUrl = areaCoursesUrl;
    }

    public URI getAreaImageUrl() {
        return areaImageUrl;
    }

    public void setAreaImageUrl(URI areaImageUrl) {
        this.areaImageUrl = areaImageUrl;
    }
}
