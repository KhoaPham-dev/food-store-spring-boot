package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.ProvinceKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ProvinceKindValidation implements ConstraintValidator<ProvinceKind,Integer> {
    private boolean allowNull;

    @Override
    public void initialize(ProvinceKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer provinceKind, ConstraintValidatorContext constraintValidatorContext) {
        if(provinceKind == null){
            return false;
        }
        if(!Objects.equals(provinceKind, LandingISConstant.PROVINCE_KIND_COMMUNE)
                && !Objects.equals(provinceKind, LandingISConstant.PROVINCE_KIND_DISTRICT)
                && !Objects.equals(provinceKind, LandingISConstant.PROVINCE_KIND_PROVINCE)){
            return false;
        }
        return true;
    }
}
