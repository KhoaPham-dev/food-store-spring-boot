package com.landingis.api.form.orders;

import com.landingis.api.validation.OrdersState;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateOrdersDetailForm {
    /*@NotNull(message = "ordersId cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;*/

    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @NotNull(message = "amount cannot be null")
    @ApiModelProperty(required = true)
    private Integer amount;

    private String note;

}
