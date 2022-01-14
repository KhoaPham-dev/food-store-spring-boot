package com.landingis.api.mapper;

import com.landingis.api.dto.customer.CustomerDto;
import com.landingis.api.form.customer.CreateCustomerForm;
import com.landingis.api.form.customer.UpdateCustomerForm;
import com.landingis.api.storage.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDay", target = "birthday")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Customer fromCreateCustomerFormToEntity(CreateCustomerForm createCustomerForm);

    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDay", target = "birthday")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateCustomerFormToEntity(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

    @Mapping(source = "account.kind", target = "kind")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthday", target = "birthDay")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    CustomerDto fromEntityToCustomerDto(Customer customer);

    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "adminGetMapping")
    List<CustomerDto> fromEntityListToCustomerDtoList(List<Customer> customers);


}
