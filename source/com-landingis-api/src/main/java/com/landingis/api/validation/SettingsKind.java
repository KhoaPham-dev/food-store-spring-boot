package com.landingis.api.validation;

import com.landingis.api.validation.impl.ProvinceKindValidation;
import com.landingis.api.validation.impl.SettingsKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SettingsKindValidation.class)
@Documented
public @interface SettingsKind {
    boolean allowNull() default true;
    String message() default "Settings kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
