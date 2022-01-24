package com.landingis.api.mapper;

import com.landingis.api.dto.orders.OrdersDetailDto;
import com.landingis.api.dto.orders.OrdersDto;
import com.landingis.api.form.orders.*;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.Orders;
import com.landingis.api.storage.model.OrdersDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrdersMapper.class,ProductMapper.class})
public interface OrdersDetailMapper {

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    OrdersDetail fromCreateOrdersDetailFormToEntity(CreateOrdersDetailForm createOrdersDetailForm);

    @IterableMapping(elementTargetType = OrdersDetail.class, qualifiedByName = "adminCreateMapping")
    List<OrdersDetail> fromCreateOrdersDetailFormListToOrdersDetailList(List<CreateOrdersDetailForm> createOrdersDetailFormList);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    OrdersDetail fromUpdateOrdersDetailFormToEntity(UpdateOrdersDetailForm updateOrdersDetailForm);

    @IterableMapping(elementTargetType = OrdersDetail.class, qualifiedByName = "adminUpdateMapping")
    List<OrdersDetail> fromUpdateOrdersDetailFormListToOrdersDetailList(List<UpdateOrdersDetailForm> updateOrdersDetailFormList);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "orders", target = "ordersDto",qualifiedByName = "ordersAutoCompleteMapping")
    @Mapping(source = "product", target = "productDto")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "collaboratorCommission", target = "collaboratorCommission")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDetailDto fromEntityToOrdersDetailDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailDtoList(List<OrdersDetail> ordersDetailList);
}
