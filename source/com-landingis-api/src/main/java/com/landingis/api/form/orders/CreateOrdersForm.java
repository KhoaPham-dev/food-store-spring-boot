package com.landingis.api.form.orders;

import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.OrdersDetail;
import com.landingis.api.validation.OrdersState;
import com.landingis.api.validation.PaymentMethod;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.core.annotation.Order;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrdersForm {
    @NotNull(message = "customerId cannot be null")
    @ApiModelProperty(required = true)
    private Long customerId;


    @NotNull(message = "saleOff cannot be null")
    @ApiModelProperty(required = true)
    private Integer saleOff;

    @OrdersState
    @NotNull(message = "state cannot be null")
    @ApiModelProperty(required = true)
    private Integer state;

    @OrdersState
    private Integer prevState;

    @NotEmpty(message = "address cannot be empty")
    @ApiModelProperty(required = true)
    private String address;

    @NotEmpty(message = "receiverName cannot be empty")
    @ApiModelProperty(required = true)
    private String receiverName;

    @NotEmpty(message = "receiverPhone cannot be empty")
    @ApiModelProperty(required = true)
    private String receiverPhone;

    private Long employeeId;

    private Long collaboratorId;

    @PaymentMethod
    @NotNull(message = "paymentMethod cannot be null")
    @ApiModelProperty(required = true)
    private Integer paymentMethod;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;

    @NotEmpty(message = "createOrdersDetailFormList cannot be empty")
    @ApiModelProperty(required = true)
    private List<CreateOrdersDetailForm> createOrdersDetailFormList;

}
