package com.landingis.api.mapper;

import com.landingis.api.dto.account.AccountAdminDto;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.dto.import_export.ImportExportDto;
import com.landingis.api.form.employee.CreateEmployeeForm;
import com.landingis.api.form.employee.UpdateEmployeeForm;
import com.landingis.api.form.import_export.CreateImportExportForm;
import com.landingis.api.form.import_export.UpdateImportExportForm;
import com.landingis.api.storage.model.Account;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.ImportExport;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class,CategoryMapper.class})
public interface ImportExportMapper {
    @Mapping(source = "code", target = "code")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    ImportExport fromCreateImportExportFormToEntity(CreateImportExportForm createImportExportForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateImportExportFormToEntity(UpdateImportExportForm updateImportExportForm, @MappingTarget ImportExport importExport);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "category", target = "categoryDto",qualifiedByName = "categoryAutoCompleteMapping")
    @Mapping(source = "account", target = "accountDto",qualifiedByName = "accountAutoCompleteMapping")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    ImportExportDto fromEntityToImportExportDto(ImportExport importExport);

    @IterableMapping(elementTargetType = ImportExportDto.class, qualifiedByName = "adminGetMapping")
    List<ImportExportDto> fromEntityListToImportExportDtoList(List<ImportExport> importExportList);

}
