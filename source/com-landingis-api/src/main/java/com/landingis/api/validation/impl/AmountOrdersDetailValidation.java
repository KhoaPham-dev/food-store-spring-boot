package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.AmountOrdersDetail;
import com.landingis.api.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AmountOrdersDetailValidation implements ConstraintValidator<AmountOrdersDetail, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(AmountOrdersDetail constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer amount, ConstraintValidatorContext constraintValidatorContext) {
        if(amount == null && allowNull) {
            return true;
        }
        if(!(amount > LandingISConstant.MIN_OF_AMOUNT)) {
            return false;
        }
        return true;
    }
}
