package com.landingis.api.form.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateEmployeeProfileForm {
    @NotEmpty(message = "employeeOldPassword cannot be null")
    @ApiModelProperty(required = true)
    private String employeeOldPassword;

    @NotEmpty(message = "employeeNewPassword cannot be null")
    @ApiModelProperty(required = true)
    private String employeeNewPassword;
}
