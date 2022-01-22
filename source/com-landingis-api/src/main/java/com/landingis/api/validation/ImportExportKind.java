package com.landingis.api.validation;

import com.landingis.api.validation.impl.ImportExportKindValidation;
import com.landingis.api.validation.impl.OrdersStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImportExportKindValidation.class)
@Documented
public @interface ImportExportKind {
    boolean allowNull() default true;
    String message() default "ImportExport kind invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
