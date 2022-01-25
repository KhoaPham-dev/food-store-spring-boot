package com.landingis.api.form.collaborator;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateCollaboratorProfileForm {
    @NotEmpty(message = "collaboratorOldPassword cannot be null")
    @ApiModelProperty(required = true)
    private String collaboratorOldPassword;

    @NotEmpty(message = "collaboratorNewPassword cannot be null")
    @ApiModelProperty(required = true)
    private String collaboratorNewPassword;
}
