package ar.edu.itba.paw.webapp.validator;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, FormDataBodyPart> {

   private Long min;

   private Long max;

   public void initialize(FileSize constraint) {
      min = constraint.min();
      max = constraint.max();
   }

   public boolean isValid(FormDataBodyPart file, ConstraintValidatorContext context) {
      if(file == null)
         return false;
      return file.getContentDisposition().getSize() <= max && file.getContentDisposition().getSize() >= min;
   }
}
