package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.dto.import_export.ImportExportDto;
import com.landingis.api.dto.import_export.ResponseListObjImportExport;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.employee.CreateEmployeeForm;
import com.landingis.api.form.employee.UpdateEmployeeForm;
import com.landingis.api.form.import_export.CreateImportExportForm;
import com.landingis.api.form.import_export.UpdateImportExportForm;
import com.landingis.api.mapper.EmployeeMapper;
import com.landingis.api.mapper.ImportExportMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.EmployeeCriteria;
import com.landingis.api.storage.criteria.ImportExportCriteria;
import com.landingis.api.storage.model.*;
import com.landingis.api.storage.repository.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v1/import-export")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ImportExportController extends ABasicController{
    @Autowired
    ImportExportRepository importExportRepository;

    @Autowired
    ImportExportMapper importExportMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObjImportExport> list(ImportExportCriteria importExportCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObjImportExport> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<ImportExport> listImportExport = importExportRepository.findAll(importExportCriteria.getSpecification(), pageable);
        ResponseListObjImportExport responseListObjImportExport = new ResponseListObjImportExport();
        responseListObjImportExport.setData(importExportMapper.fromEntityListToImportExportDtoList(listImportExport.getContent()));
        responseListObjImportExport.setPage(pageable.getPageNumber());
        responseListObjImportExport.setTotalPage(listImportExport.getTotalPages());
        responseListObjImportExport.setTotalElements(listImportExport.getTotalElements());
        Double sum = 0d;
        Integer importExportKind = importExportCriteria.getKind();
        if(importExportKind == null || !(importExportKind.equals(LandingISConstant.IMPORT_KIND)
            && importExportKind.equals(LandingISConstant.EXPORT_KIND))){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "Must have kind");
        }
        Date importExportTo = importExportCriteria.getTo();
        Date importExportFrom = importExportCriteria.getFrom();
        // kind , from ,to
        if(importExportFrom != null && importExportTo != null){
            sum = importExportRepository.sumMoneyByKindAndFromAndTo(importExportKind,importExportFrom,importExportTo);
        }
        // kind , from
        if(importExportFrom != null && importExportTo == null){
            sum = importExportRepository.sumMoneyByKindAndFrom(importExportKind,importExportFrom);
        }

        // kind , to
        if(importExportFrom == null && importExportTo != null){
            sum = importExportRepository.sumMoneyByKindAndTo(importExportKind,importExportTo);
        }
        // kind
        if(importExportFrom == null && importExportTo == null){
            sum = importExportRepository.sumMoneyByKind(importExportKind);
        }
        responseListObjImportExport.setSum(sum);
        responseListObjApiMessageDto.setData(responseListObjImportExport);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ImportExportDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ImportExportDto> result = new ApiMessageDto<>();

        ImportExport importExport = importExportRepository.findById(id).orElse(null);
        if(importExport == null) {
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_NOT_FOUND, "Not found Import-Export.");
        }
        result.setData(importExportMapper.fromEntityToImportExportDto(importExport));
        result.setMessage("Get Import-Export success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateImportExportForm createImportExportForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Account accountCheck = getCurrentAdmin();
        if (accountCheck == null){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "Not have account.");
        }
        String code = createImportExportForm.getCode();
        if (code.contains(" ") || !(code.length() > 1)) {
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "code is wrong");
        }
        checkCategory(createImportExportForm);
        ImportExport importExport = importExportMapper.fromCreateImportExportFormToEntity(createImportExportForm);
        importExport.setAccount(accountCheck);
        importExportRepository.save(importExport);
        apiMessageDto.setMessage("Create Import-Export success");
        return apiMessageDto;
    }

    private void checkCategory(CreateImportExportForm createImportExportForm) {
        Category categoryCheck = categoryRepository.findById(createImportExportForm.getCategoryId()).orElse(null);
        if (categoryCheck == null || !categoryCheck.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "Category not found");
        }
        Integer categoryKind = categoryCheck.getKind();
        if(createImportExportForm.getKind().equals(LandingISConstant.IMPORT_KIND)
            &&(!categoryKind.equals(LandingISConstant.CATEGORY_KIND_IMPORT))){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "Category is not import kind");
        }
        if(createImportExportForm.getKind().equals(LandingISConstant.EXPORT_KIND)
                &&(!categoryKind.equals(LandingISConstant.CATEGORY_KIND_EXPORT))){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_BAD_REQUEST, "Category is not export kind");
        }
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateImportExportForm updateImportExportForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        ImportExport importExport = importExportRepository.findById(updateImportExportForm.getId()).orElse(null);
        if(importExport == null) {
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_NOT_FOUND, "Not found Import-Export.");
        }
        importExportMapper.fromUpdateImportExportFormToEntity(updateImportExportForm,importExport);
        importExportRepository.save(importExport);
        apiMessageDto.setMessage("Update Import-Export success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<ImportExportDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<ImportExportDto> result = new ApiMessageDto<>();

        ImportExport importExport = importExportRepository.findById(id).orElse(null);
        if(importExport == null) {
            throw new RequestException(ErrorCode.IMPORT_EXPORT_ERROR_NOT_FOUND, "Not found Import-Export");
        }
        landingIsApiService.deleteFile(importExport.getFilePath());
        importExportRepository.delete(importExport);
        result.setMessage("Delete Import-Export success");
        return result;
    }
}
