package com.landingis.api.mapper;

import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.dto.orders.OrdersDto;
import com.landingis.api.form.collaborator.CreateCollaboratorForm;
import com.landingis.api.form.employee.UpdateEmployeeForm;
import com.landingis.api.form.orders.CreateOrdersForm;
import com.landingis.api.form.orders.UpdateOrdersForm;
import com.landingis.api.form.orders.UpdateStateOrdersForm;
import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.Orders;
import org.mapstruct.*;
import org.springframework.core.annotation.Order;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CustomerMapper.class,CollaboratorMapper.class,EmployeeMapper.class})
public interface OrdersMapper {
    @Mapping(source = "saleOff", target = "saleOff")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "prevState", target = "prevState")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Orders fromCreateOrdersFormToEntity(CreateOrdersForm createOrdersForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "receiverPhone", target = "receiverPhone")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateOrdersFormToEntity(UpdateOrdersForm updateOrdersForm, @MappingTarget Orders orders);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customerDto",qualifiedByName="customerAutoCompleteMapping")
    @Mapping(source = "saleOff", target = "saleOff")
    @Mapping(source = "totalMoney", target = "totalMoney")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "prevState", target = "prevState")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "employee", target = "employeeDto",qualifiedByName="employeeAutoCompleteMapping")
    @Mapping(source = "collaborator", target = "collaboratorDto",qualifiedByName="collaboratorAutoCompleteMapping")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDto fromEntityToOrdersDto(Orders orders);

    @IterableMapping(elementTargetType = OrdersDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDto> fromEntityListToOrdersDtoList(List<Orders> ordersList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customerDto",qualifiedByName = "customerAutoCompleteMapping")
    @Mapping(source = "totalMoney", target = "totalMoney")
    @Mapping(source = "state", target = "state")
    @BeanMapping(ignoreByDefault = true)
    @Named("ordersAutoCompleteMapping")
    OrdersDto fromEntityToAdminDtoAutoComplete(Orders orders);
}
