package com.example.job_system.dto.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = OneOfValidator.class)
@Documented
public @interface OneOf {

    /**
     * Contains all possible values
     */
    String[] value();

    String message() default "{com.example.job_system.dto.validation.OneOf.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
