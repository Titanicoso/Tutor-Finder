package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MessageForm {

    @NotNull(message = "{NotNull.messageForm.message}")
    @Size(min = 1, max = 1024, message = "{Size.messageForm.message}")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
