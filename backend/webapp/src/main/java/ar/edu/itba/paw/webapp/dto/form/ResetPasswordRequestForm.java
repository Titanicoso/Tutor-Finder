package ar.edu.itba.paw.webapp.dto.form;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResetPasswordRequestForm {

    @NotNull(message = "{NotNull.resetPasswordRequestForm.email}")
    @Size(min = 1, max = 512, message = "{Size.resetPasswordRequestForm.email}")
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
