package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.Gender;
import com.landingis.api.validation.OrdersState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrdersStateValidation implements ConstraintValidator<OrdersState, Integer> {
    private boolean allowNull;
    @Override
    public void initialize(OrdersState constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer ordersState, ConstraintValidatorContext constraintValidatorContext) {
        if(ordersState == null && allowNull){
            return true;
        }
        if(!ordersState.equals(LandingISConstant.ORDERS_STATE_CREATED)
            &&!ordersState.equals(LandingISConstant.ORDERS_STATE_ACCEPTED)
            &&!ordersState.equals(LandingISConstant.ORDERS_STATE_DONE)
            &&!ordersState.equals(LandingISConstant.ORDERS_STATE_CANCELED)
            &&!ordersState.equals(LandingISConstant.ORDERS_STATE_SHIPPING)){
                return false;
        }
        return true;
    }
}
