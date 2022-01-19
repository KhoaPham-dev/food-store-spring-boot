package com.landingis.api.dto.orders;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.product.ProductDto;
import com.landingis.api.storage.model.Orders;
import com.landingis.api.storage.model.Product;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {
    private Long id;
    private OrdersDto ordersDto;
    private ProductDto productDto;
    private Double price;
    private Integer amount;
    private String note;
    private Integer kind;
    private Double value;
    private Double collaboratorCommission;
}
