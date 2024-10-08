package com.demo_crud.demo.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.FIELD;


@Target({ FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DobValidator.class})
public @interface DobConstraint {
    //nhung thuoc thich co ban cua annotation  danh cho validate

    String message() default "invalid date of birth";
    int min();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
