package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CourseForm {

    @NotNull
    private Long subject;

    @NotNull
    @Size(min = 50, max = 300)
    private String description;

    @NotNull
    @DecimalMin("1")
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
