package com.landingis.api.mapper;

import com.landingis.api.dto.addresses.AddressesDto;
import com.landingis.api.form.addresses.CreateAddressesForm;
import com.landingis.api.form.addresses.UpdateAddressesForm;
import com.landingis.api.storage.model.Addresses;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressesMapper {
    @Mapping(source = "customer_id", target = "customer.id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune_id", target = "commune.id")
    @Mapping(source = "district_id", target = "district.id")
    @Mapping(source = "province_id", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Addresses fromCreateAddressesFormToEntity(CreateAddressesForm createAddressesForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "addresses_id", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune_id", target = "commune.id")
    @Mapping(source = "district_id", target = "district.id")
    @Mapping(source = "province_id", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    void fromUpdateAddressesFormToEntity(UpdateAddressesForm updateAddressesForm, @MappingTarget Addresses addresses);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer.id", target = "customer_id")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune.name", target = "addressesCommune.name")
    @Mapping(source = "district.name", target = "addressesDistrict.name")
    @Mapping(source = "province.name ", target = "addressesProvince.name")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    AddressesDto fromEntityToAdminDto(Addresses addresses);

    @IterableMapping(elementTargetType = AddressesDto.class, qualifiedByName = "adminGetMapping")
    List<AddressesDto> fromEntityListToAddressesDtoList(List<Addresses> addresses);

}
