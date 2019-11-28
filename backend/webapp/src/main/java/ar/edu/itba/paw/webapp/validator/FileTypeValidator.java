package ar.edu.itba.paw.webapp.validator;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, FormDataBodyPart> {
   public void initialize(FileType constraint) {
   }

   public boolean isValid(FormDataBodyPart file, ConstraintValidatorContext context) {
      if(file != null)
         return (file.getMediaType().toString().equals("image/jpeg") || file.getMediaType().toString().equals("image/png"));
      return true;
   }
}
