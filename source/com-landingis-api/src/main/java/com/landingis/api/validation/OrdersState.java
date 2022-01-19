package com.landingis.api.validation;

import com.landingis.api.validation.impl.GenderValidation;
import com.landingis.api.validation.impl.OrdersStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrdersStateValidation.class)
@Documented
public @interface OrdersState {
    boolean allowNull() default true;
    String message() default "Order state invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
