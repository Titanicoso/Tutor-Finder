package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement
public class UserDTO {

    private long id;
    private String username;
    private String name;
    private String lastname;
    private String email;

    public UserDTO(){}

    public UserDTO(final User user, final URI baseUri){
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
    }

    public UserDTO(Long id, String username, String name, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
