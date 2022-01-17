package com.landingis.api.mapper;

import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.form.collaborator.CreateCollaboratorForm;
import com.landingis.api.form.collaborator.UpdateCollaboratorForm;
import com.landingis.api.storage.model.Collaborator;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollaboratorMapper {
    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "phone", target = "account.phone")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDay", target = "birthday")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "dateOfIssue", target = "dateOfIssue")
    @Mapping(source = "placeOfIssue", target = "placeOfIssue")
    @Mapping(source = "bankNo", target = "bankNo")
    @Mapping(source = "bankName", target = "bankName")
    @Mapping(source = "branchName", target = "branchName")
    @Mapping(source = "employeeId", target = "employee.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Collaborator fromCreateCollaboratorFormToEntity(CreateCollaboratorForm createCollaboratorForm);

    @Mapping(source = "username", target = "account.username")
    @Mapping(source = "avatarPath", target = "account.avatarPath")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "account.fullName")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "birthDay", target = "birthday")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "dateOfIssue", target = "dateOfIssue")
    @Mapping(source = "placeOfIssue", target = "placeOfIssue")
    @Mapping(source = "bankNo", target = "bankNo")
    @Mapping(source = "bankName", target = "bankName")
    @Mapping(source = "branchName", target = "branchName")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateCollaboratorFormToEntity(UpdateCollaboratorForm updateCollaboratorForm, @MappingTarget Collaborator collaborator);

    @Mapping(source = "account.username", target = "username")
    @Mapping(source = "account.kind", target = "kind")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.avatarPath", target = "avatarPath")
    @Mapping(source = "account.fullName", target = "fullName")
    @Mapping(source = "account.phone", target = "phone")
    @Mapping(source = "identityNumber", target = "identityNumber")
    @Mapping(source = "dateOfIssue", target = "dateOfIssue")
    @Mapping(source = "placeOfIssue", target = "placeOfIssue")
    @Mapping(source = "bankNo", target = "bankNo")
    @Mapping(source = "bankName", target = "bankName")
    @Mapping(source = "branchName", target = "branchName")
    @Mapping(source = "employee.id", target = "employeeId")
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
    CollaboratorDto fromEntityToCollaboratorDto(Collaborator collaborator);

    @IterableMapping(elementTargetType = CollaboratorDto.class, qualifiedByName = "adminGetMapping")
    List<CollaboratorDto> fromEntityListToCollaboratorDtoList(List<Collaborator> collaborators);

}
