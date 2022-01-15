package com.landingis.api.validation;

import com.landingis.api.validation.impl.GenderValidation;
import com.landingis.api.validation.impl.ProvinceKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProvinceKindValidation.class)
@Documented
public @interface ProvinceKind {
    boolean allowNull() default true;
    String message() default "Province kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
