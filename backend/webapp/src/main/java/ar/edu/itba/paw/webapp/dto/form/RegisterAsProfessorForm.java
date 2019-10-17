package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.validator.FileType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterAsProfessorForm {

    @NotNull(message = "{NotNull.description}")
    @Size(min = 50, max = 300, message = "{Size.registerAsProfessorForm.description}")
    @FormDataParam("description")
    private String description;

//    @FileSize(max = 81920)
    @NotNull(message = "{NotNull.registerAsProfessorForm.picture}")
    @FileType(message = "{FileType.registerAsProfessorForm.picture}")
    @FormDataParam("picture")
    private FormDataBodyPart picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FormDataBodyPart getPicture() {
        return picture;
    }

    public void setPicture(FormDataBodyPart picture) {
        this.picture = picture;
    }
}
