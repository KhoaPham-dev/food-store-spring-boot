package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.ProvinceKind;
import com.landingis.api.validation.SettingsGroup;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class SettingsGroupValidation implements ConstraintValidator<SettingsGroup,Integer> {
    private boolean allowNull;
    @Override
    public void initialize(SettingsGroup constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer settingsGroup, ConstraintValidatorContext constraintValidatorContext) {
        if(settingsGroup == null && !allowNull){
            return false;
        }
        if(!Objects.equals(settingsGroup, LandingISConstant.SETTING_GROUP_ID_ADMIN)
                && !Objects.equals(settingsGroup, LandingISConstant.SETTING_GROUP_ID_CUSTOMER)){
            return false;
        }
        return true;
    }
}
