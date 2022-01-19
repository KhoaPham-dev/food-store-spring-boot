package com.landingis.api.validation;

import com.landingis.api.validation.impl.OrdersStateValidation;
import com.landingis.api.validation.impl.PaymentMethodValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentMethodValidation.class)
@Documented
public @interface PaymentMethod {
    boolean allowNull() default true;
    String message() default "Payment method invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
