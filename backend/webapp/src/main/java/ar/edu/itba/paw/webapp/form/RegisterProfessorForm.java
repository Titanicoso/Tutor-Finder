package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validator.FileSize;
import ar.edu.itba.paw.webapp.validator.FileType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterProfessorForm {

    @NotNull
    @Size(min = 50, max = 300)
    private String description;

    @NotNull
    @FileSize(min = 1, max = 81920)
    @FileType
    private MultipartFile picture;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

}
