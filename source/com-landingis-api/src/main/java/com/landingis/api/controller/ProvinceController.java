package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.province.ProvinceDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.category.CreateCategoryForm;
import com.landingis.api.form.category.UpdateCategoryForm;
import com.landingis.api.form.province.CreateProvinceForm;
import com.landingis.api.form.province.UpdateProvinceForm;
import com.landingis.api.mapper.CategoryMapper;
import com.landingis.api.mapper.ProvinceMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CategoryCriteria;
import com.landingis.api.storage.criteria.ProvinceCriteria;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Province;
import com.landingis.api.storage.repository.CategoryRepository;
import com.landingis.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/v1/province")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProvinceController extends ABasicController {
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> list(ProvinceCriteria provinceCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Province> listProvince = provinceRepository.findAll(provinceCriteria.getSpecification(), pageable);
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoList(listProvince.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listProvince.getTotalPages());
        responseListObj.setTotalElements(listProvince.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> autoComplete(ProvinceCriteria provinceCriteria) {
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        provinceCriteria.setStatus(LandingISConstant.STATUS_ACTIVE);
        Page<Province> listProvince = provinceRepository.findAll(provinceCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoAutoComplete(listProvince.getContent()));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProvinceDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province.");
        }
        result.setData(provinceMapper.fromEntityToAdminDto(province));
        result.setMessage("Get province success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProvinceForm createProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Province province = provinceMapper.fromCreateProvinceFormToEntity(createProvinceForm);
        if(createProvinceForm.getParentId() == null){
            if(!(createProvinceForm.getProvinceKind() == LandingISConstant.PROVINCE_KIND_PROVINCE)){
                throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province parent");
            }
        }
        if(createProvinceForm.getParentId() != null) {
            Province parentProvince = provinceRepository.findById(createProvinceForm.getParentId()).orElse(null);
            if(parentProvince == null) {
                throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province parent");
            }
            // Check xem parentKind có bằng ProvinceKind của create form +1 không --> nếu bằng thì là parent đúng thứ tự
            Integer parentKindId = parentProvince.getKind();
            if(!parentKindId.equals(createProvinceForm.getProvinceKind() + 1)){
                throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province parent");
            }
            province.setParentProvince(parentProvince);
        }
        provinceRepository.save(province);
        apiMessageDto.setMessage("Create province success");
        return apiMessageDto;

    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProvinceForm updateProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Province province = provinceRepository.findById(updateProvinceForm.getId()).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province.");
        }
        if(province.getStatus().equals(updateProvinceForm.getStatus()) && province.getParentProvince() == null) {
            province.getProvinceList().forEach(child -> child.setStatus(updateProvinceForm.getStatus()));
            provinceRepository.saveAll(province.getProvinceList());
        }
        provinceMapper.fromUpdateProvinceFormToEntity(updateProvinceForm, province);
        provinceRepository.save(province);
        apiMessageDto.setMessage("Update province success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<ProvinceDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not allowed to delete.");
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province");
        }
        provinceRepository.delete(province);
        result.setMessage("Delete province success");
        return result;
    }
}
