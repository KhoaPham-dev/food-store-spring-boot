package com.landingis.api.mapper;

import java.util.List;

import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.group.GroupAdminDto;
import com.landingis.api.dto.group.GroupDto;
import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Group;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "permissions", target = "permissions")
    @BeanMapping(ignoreByDefault = true)
    GroupDto fromEntityToGroupDto(Group group);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "permissions", target = "permissions")
    @BeanMapping(ignoreByDefault = true)
    GroupAdminDto fromEntityToGroupAdminDto(Group group);

    @IterableMapping(elementTargetType = GroupAdminDto.class)
    List<GroupAdminDto> fromEntityListToAdminDtoList(List<Group> content);

    @IterableMapping(elementTargetType = GroupDto.class)
    List<GroupDto> fromEntityListToDtoList(List<Group> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "permissions", target = "permissions")
    @Mapping(source = "kind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    @Named("groupAutoCompleteMapping")
    GroupDto fromEntityToAdminDtoAutoComplete(Group group);

    @IterableMapping(elementTargetType = GroupDto.class, qualifiedByName = "groupAutoCompleteMapping")
    List<GroupDto> fromEntityListToGroupDtoAutoComplete(List<Group> groupList);

}
