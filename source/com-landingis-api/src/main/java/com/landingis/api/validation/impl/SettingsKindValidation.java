package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.ProvinceKind;
import com.landingis.api.validation.SettingsKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SettingsKindValidation implements ConstraintValidator<SettingsKind,Integer> {
    private boolean allowNull;
    @Override
    public void initialize(SettingsKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer settingsKind, ConstraintValidatorContext constraintValidatorContext) {
        if(settingsKind == null && !allowNull){
            return false;
        }
        if(!Objects.equals(settingsKind,LandingISConstant.SETTING_KIND_ON_OFF)
            && !Objects.equals(settingsKind, LandingISConstant.SETTING_KIND_TEXT)
                && !Objects.equals(settingsKind, LandingISConstant.SETTING_KIND_DATE)
                && !Objects.equals(settingsKind, LandingISConstant.SETTING_KIND_TIME)
                && !Objects.equals(settingsKind, LandingISConstant.SETTING_KIND_TIMESTAMP)
                && !Objects.equals(settingsKind, LandingISConstant.SETTING_KIND_UPLOAD)){
            return false;
        }
        return true;
    }
}
