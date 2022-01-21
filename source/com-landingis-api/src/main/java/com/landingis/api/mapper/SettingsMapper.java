package com.landingis.api.mapper;

import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.product.ProductDto;
import com.landingis.api.dto.settings.SettingsDto;
import com.landingis.api.form.product.CreateProductForm;
import com.landingis.api.form.product.UpdateProductForm;
import com.landingis.api.form.settings.CreateSettingsForm;
import com.landingis.api.form.settings.UpdateSettingsForm;
import com.landingis.api.storage.model.Product;
import com.landingis.api.storage.model.Settings;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingsMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "groupId", target = "groupId")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Settings fromCreateSettingsFormToEntity(CreateSettingsForm createSettingsForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateSettingsFormToEntity(UpdateSettingsForm updateSettingsForm, @MappingTarget Settings settings);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "groupId", target = "groupId")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    SettingsDto fromEntityToAdminDto(Settings settings);

    @IterableMapping(elementTargetType = SettingsDto.class, qualifiedByName = "adminGetMapping")
    List<SettingsDto> fromEntityListToSettingsDtoList(List<Settings> settingsList);



}
