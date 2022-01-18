package com.landingis.api.form.collaborator;

import com.landingis.api.validation.CollaboratorProductKind;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateCollaboratorProductForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

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
