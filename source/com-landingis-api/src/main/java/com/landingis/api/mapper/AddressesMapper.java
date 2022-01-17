package com.landingis.api.mapper;

import com.landingis.api.dto.addresses.AddressesDto;
import com.landingis.api.form.addresses.CreateAddressesForm;
import com.landingis.api.form.addresses.UpdateAddressesForm;
import com.landingis.api.storage.model.Addresses;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses={CustomerMapper.class,ProvinceMapper.class})
public interface AddressesMapper {
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "provinceId", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Addresses fromCreateAddressesFormToEntity(CreateAddressesForm createAddressesForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "addressesId", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "provinceId", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateAddressesFormToEntity(UpdateAddressesForm updateAddressesForm, @MappingTarget Addresses addresses);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customerDto",qualifiedByName="customerAutoCompleteMapping")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune", target = "addressesCommuneDto",qualifiedByName="provinceAutoCompleteMapping")
    @Mapping(source = "district", target = "addressesDistrictDto",qualifiedByName="provinceAutoCompleteMapping")
    @Mapping(source = "province ", target = "addressesProvinceDto",qualifiedByName="provinceAutoCompleteMapping")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    AddressesDto fromEntityToAdminDto(Addresses addresses);

    @IterableMapping(elementTargetType = AddressesDto.class, qualifiedByName = "adminGetMapping")
    List<AddressesDto> fromEntityListToAddressesDtoList(List<Addresses> addresses);

}
