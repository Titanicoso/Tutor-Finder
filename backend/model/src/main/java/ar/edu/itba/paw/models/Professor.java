package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "professors")
@PrimaryKeyJoinColumn(referencedColumnName="user_id", foreignKey = @ForeignKey(name = "professors_user_id_fkey"))
public class Professor extends User{

    @Column(length = 512, nullable = false)
    private String description;

    @Column(name = "profile_picture", nullable = false)
    private byte[] picture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
    private List<Course> courses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
    private List<Timeslot> timeslots;

    public Professor(){}

    public Professor(String username, String name, String lastname, String password, String email, String description, byte[] picture) {
        super(username, name, lastname, password, email);
        this.description = description;
        this.picture = picture;
    }

    public Professor(Long id, String username, String name, String lastname, String password, String email, String description, byte[] picture) {
        super(id, username, name, lastname, password, email);
        this.description = description;
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public String getBase64Picture() {
        return new String(Base64.getEncoder().encode(picture));
    }

    public byte[] getPicture() {
        return picture;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(description, professor.description) &&
                Arrays.equals(picture, professor.picture) &&
                Objects.equals(courses, professor.courses) &&
                Objects.equals(timeslots, professor.timeslots);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(description, courses, timeslots);
        result = 31 * result + Arrays.hashCode(picture);
        return result;
    }
}
