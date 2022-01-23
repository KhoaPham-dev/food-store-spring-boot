package com.landingis.api.dto.orders;

import com.landingis.api.dto.ResponseListObj;
import lombok.Data;

@Data
public class ResponseListObjOrders extends ResponseListObj<OrdersDto> {
    private Double sumMoney;
}
