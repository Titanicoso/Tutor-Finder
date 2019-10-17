package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CourseForm {

    @NotNull(message = "{NotNull.courseForm.subject}")
    private Long subject;

    @NotNull(message = "{NotNull.courseForm.description}")
    @Size(min = 50, max = 300, message = "{Size.courseForm.description}")
    private String description;

    @NotNull(message = "{NotNull.courseForm.price}")
    @DecimalMin(value = "1", message = "{DecimalMin.courseForm.price}")
    private Double price;

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
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
}
