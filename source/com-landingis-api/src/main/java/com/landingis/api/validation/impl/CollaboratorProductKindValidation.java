package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.CategoryKind;
import com.landingis.api.validation.CollaboratorProductKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CollaboratorProductKindValidation implements ConstraintValidator<CollaboratorProductKind, Integer> {
    private boolean allowNull;
    @Override
    public void initialize(CollaboratorProductKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer collaboratorProductKind, ConstraintValidatorContext constraintValidatorContext) {
        if(collaboratorProductKind == null) {
            return true;
        }
        if(!Objects.equals(collaboratorProductKind, LandingISConstant.COLLABORATOR_KIND_PERCENT)
            ||!Objects.equals(collaboratorProductKind, LandingISConstant.COLLABORATOR_KIND_DOLLAR)) {
            return false;
        }
        return true;
    }
}
