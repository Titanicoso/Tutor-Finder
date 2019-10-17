package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.validator.FileSize;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UploadClassFileForm {

    //TODO: File size max not working
    @NotNull(message = "{NotNull.uploadClassFileForm.file}")
//    @FileSize(min = 1, max = 10 * 1024 * 1024)
    @FormDataParam("file")
    private FormDataBodyPart file;

    @NotNull(message = "{NotNull.uploadClassFileForm.description}")
    @Size(min = 5, max = 255, message = "{Size.uploadClassFileForm.description}")
    @FormDataParam("description")
    private String description;

    public FormDataBodyPart getFile() {
        return file;
    }

    public void setFile(FormDataBodyPart file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
