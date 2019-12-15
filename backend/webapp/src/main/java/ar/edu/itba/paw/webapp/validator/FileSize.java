package ar.edu.itba.paw.webapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FileSizeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSize {
    String message() default "El tamaño del archivo debe tener un tamaño maximo de 80KB";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    long min() default 0;

    long max() default Long.MAX_VALUE;

    boolean required() default true;

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FileSize[] value();
    }
}
