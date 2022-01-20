package com.landingis.api.form.orders;

import com.landingis.api.validation.OrdersState;
import com.landingis.api.validation.PaymentMethod;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateOrdersForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotEmpty(message = "address cannot be empty")
    @ApiModelProperty(required = true)
    private String address;

    @NotEmpty(message = "receiverName cannot be empty")
    @ApiModelProperty(required = true)
    private String receiverName;

    @NotEmpty(message = "receiverPhone cannot be empty")
    @ApiModelProperty(required = true)
    private String receiverPhone;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;

    /*@NotEmpty(message = "updateOrdersDetailFormList cannot be empty")
    @ApiModelProperty(required = true)*/
    private List<@Valid UpdateOrdersDetailForm> updateOrdersDetailFormList;

    /*@NotEmpty(message = "deleteOrdersDetailFormList cannot be empty")
    @ApiModelProperty(required = true)*/
    private List<@Valid DeleteOrdersDetailForm> deleteOrdersDetailFormList;
}
