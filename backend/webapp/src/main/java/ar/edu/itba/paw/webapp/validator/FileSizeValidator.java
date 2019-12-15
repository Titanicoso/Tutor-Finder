package ar.edu.itba.paw.webapp.validator;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, FormDataBodyPart> {

   private Long min;

   private Long max;

   private boolean required;

   public void initialize(FileSize constraint) {
      min = constraint.min();
      max = constraint.max();
      required = constraint.required();
   }

   public boolean isValid(FormDataBodyPart file, ConstraintValidatorContext context) {
      if(file != null) {
         final int length = file.getValueAs(byte[].class).length;
         return length <= max && length >= min;
      }
      return !required;
   }
}
