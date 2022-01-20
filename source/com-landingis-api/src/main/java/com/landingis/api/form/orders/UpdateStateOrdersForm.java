package com.landingis.api.form.orders;

import com.landingis.api.validation.OrdersState;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateStateOrdersForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @OrdersState
    @NotNull(message = "state cannot be null")
    @ApiModelProperty(required = true)
    private Integer state;
}
