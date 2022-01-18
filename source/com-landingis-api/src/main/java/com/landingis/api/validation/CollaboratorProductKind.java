package com.landingis.api.validation;

import com.landingis.api.validation.impl.CategoryKindValidation;
import com.landingis.api.validation.impl.CollaboratorProductKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CollaboratorProductKindValidation.class)
@Documented
public @interface CollaboratorProductKind {
    boolean allowNull() default false;
    String message() default  "Collaborator-Product kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
