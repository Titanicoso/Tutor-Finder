package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.validator.FileSize;
import ar.edu.itba.paw.webapp.validator.FileType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.Size;

public class EditProfessorProfileForm {

    @Size(min = 50, max = 300, message = "{Size.editProfessorProfileForm.description}")
    @FormDataParam("description")
    private String description;

    @FileSize(min= 1, max = 81920, message = "{FileSize.editProfessorProfileForm.picture}")
    @FileType(message = "{FileType.editProfessorProfileForm.picture}")
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

    public byte[] getPictureBytes() {
        if(picture == null)
            return null;
        return picture.getValueAs(byte[].class);
    }

    public void setPicture(FormDataBodyPart picture) {
        this.picture = picture;
    }
}
