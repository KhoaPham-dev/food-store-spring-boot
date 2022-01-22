package com.landingis.api.validation.impl;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.validation.CategoryKind;
import com.landingis.api.validation.ImportExportKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ImportExportKindValidation implements ConstraintValidator<ImportExportKind, Integer> {
    private boolean allowNull;
    @Override
    public void initialize(ImportExportKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer importExportKind, ConstraintValidatorContext constraintValidatorContext) {
        if(importExportKind == null && allowNull) {
            return true;
        }
        if(!Objects.equals(importExportKind, LandingISConstant.EXPORT_KIND)
            &&!Objects.equals(importExportKind, LandingISConstant.IMPORT_KIND)) {
            return false;
        }
        return true;
    }
}
