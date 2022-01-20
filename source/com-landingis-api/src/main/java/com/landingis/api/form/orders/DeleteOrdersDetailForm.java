package com.landingis.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class DeleteOrdersDetailForm {
    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;
}
