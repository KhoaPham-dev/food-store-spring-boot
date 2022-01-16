package com.landingis.api.mapper;

import com.landingis.api.dto.province.ProvinceDto;
import com.landingis.api.form.province.CreateProvinceForm;
import com.landingis.api.form.province.UpdateProvinceForm;
import com.landingis.api.storage.model.Province;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProvinceMapper {

    @Mapping(source = "provinceName", target = "name")
    @Mapping(source = "provinceKind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Province fromCreateProvinceFormToEntity(CreateProvinceForm createProvinceForm);

    @Mapping(source = "provinceName", target = "name")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateProvinceFormToEntity(UpdateProvinceForm updateProvinceForm, @MappingTarget Province province);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "provinceName")
    @Mapping(source = "kind", target = "provinceKind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "parentProvince.id", target = "parentId")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    ProvinceDto fromEntityToAdminDto(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "adminGetMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoList(List<Province> provinces);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "provinceName")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminAutoCompleteMapping")
    ProvinceDto fromEntityToAdminDtoAutoComplete(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "adminAutoCompleteMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoAutoComplete(List<Province> provinces);
}
