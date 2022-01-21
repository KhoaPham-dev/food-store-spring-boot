package com.landingis.api.validation;

import com.landingis.api.validation.impl.ProvinceKindValidation;
import com.landingis.api.validation.impl.SettingsGroupValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SettingsGroupValidation.class)
@Documented
public @interface SettingsGroup {
    boolean allowNull() default true;
    String message() default "Settings group invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
