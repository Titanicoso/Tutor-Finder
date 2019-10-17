package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class ValidationErrorDTO {

    @XmlElement
    private String message;

    @XmlElement
    private List<ErrorDTO> errors;

    public ValidationErrorDTO() {
        errors = new ArrayList<>();
    }

    public ValidationErrorDTO(final String message) {
        this.message = message;
    }

    public ValidationErrorDTO(final Set<? extends ConstraintViolation<?>> violations) {
        this();
        violations.forEach((violation) -> errors.add(new ErrorDTO(violation)));
    }

    public void addError(final ErrorDTO error) {
        errors.add(error);
    }

    public void addError(final String field, final String error) {
        errors.add(new ErrorDTO(field, error));
    }
}
