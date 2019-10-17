package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResetPasswordForm {

    @NotNull(message = "{NotNull.resetPasswordForm.password}")
    @Size(min = 8, max = 64, message = "{Size.resetPasswordForm.password}")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
