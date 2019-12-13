package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validator.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UploadClassFileForm {

    @NotNull
    @FileSize(min = 1, max = 10 * 1024 * 1024)
    private MultipartFile file;

    @NotNull
    @Size(min = 5, max = 255)
    private String description;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
