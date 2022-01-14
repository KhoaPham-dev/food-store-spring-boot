package com.landingis.api.mapper;

import com.landingis.api.dto.permission.PermissionAdminDto;
import com.landingis.api.dto.permission.PermissionDto;
import com.landingis.api.storage.model.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    PermissionDto fromEntityToDto(Permission permission);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @BeanMapping(ignoreByDefault = true)
    PermissionAdminDto fromEntityToAdminDto(Permission permission);

    @IterableMapping(elementTargetType = PermissionAdminDto.class)
    List<PermissionAdminDto> fromEntityListToAdminDtoList(List<Permission> content);

    @IterableMapping(elementTargetType = PermissionDto.class)
    List<PermissionDto> fromEntityToDtoList(List<Permission> list);

    @IterableMapping(elementTargetType = PermissionDto.class)
    List<PermissionDto> fromEntityListToDtoList(List<Permission> content);
}
