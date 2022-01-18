package com.landingis.api.form.collaborator;

import com.landingis.api.validation.CollaboratorProductKind;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class CreateCollaboratorProductForm {
    @NotNull(message = "employeeId cannot be null")
    @ApiModelProperty(required = true)
    private Long collaboratorId;

    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @CollaboratorProductKind
    @NotNull(message = "kind cannot be null")
    @ApiModelProperty(required = true)
    private Integer kind;

    @NotNull(message = "value cannot be null")
    @ApiModelProperty(required = true)
    private Double value;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;
}
