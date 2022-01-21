package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.settings.SettingsDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.category.CreateCategoryForm;
import com.landingis.api.form.category.UpdateCategoryForm;
import com.landingis.api.form.settings.CreateSettingsForm;
import com.landingis.api.form.settings.UpdateSettingsForm;
import com.landingis.api.mapper.CategoryMapper;
import com.landingis.api.mapper.SettingsMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CategoryCriteria;
import com.landingis.api.storage.criteria.SettingsCriteria;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Settings;
import com.landingis.api.storage.repository.CategoryRepository;
import com.landingis.api.storage.repository.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/settings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SettingsController extends ABasicController{
    @Autowired
    SettingsRepository settingsRepository;

    @Autowired
    SettingsMapper settingsMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SettingsDto>> list(SettingsCriteria settingsCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.SETTINGS_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<SettingsDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Settings> listSettings= settingsRepository.findAll(settingsCriteria.getSpecification(), pageable);
        ResponseListObj<SettingsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(settingsMapper.fromEntityListToSettingsDtoList(listSettings.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listSettings.getTotalPages());
        responseListObj.setTotalElements(listSettings.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SettingsDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<SettingsDto> result = new ApiMessageDto<>();

        Settings settings = settingsRepository.findById(id).orElse(null);
        if(settings == null) {
            throw new RequestException(ErrorCode.SETTINGS_ERROR_NOT_FOUND, "Not found settings.");
        }
        result.setData(settingsMapper.fromEntityToAdminDto(settings));
        result.setMessage("Get settings success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateSettingsForm createSettingsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        // Đảm bảo key nhập vào không có khoảng trắng
        String key = createSettingsForm.getKey();
        if (key.contains(" ")) {
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "Key can not have whitespace");
        }

        // check unique bằng key
        Settings settings = settingsRepository.findByKey(createSettingsForm.getKey());
        if(settings != null){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "Settings already existed");
        }
        checkGroup(createSettingsForm);
        settings = settingsMapper.fromCreateSettingsFormToEntity(createSettingsForm);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Create settings success");
        return apiMessageDto;
    }

    private void checkGroup(CreateSettingsForm createSettingsForm) {
        String group = createSettingsForm.getGroup();
        String[] listItem = group.split("::");
        if(listItem.length != 2){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "Group format is wrong");
        }
        String groupNumber = listItem[0];
        if(!NumberUtils.isNumber(groupNumber)){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "Group number value is wrong");
        }
        String groupString = listItem[1];
        if(!(groupString.length() > 1 && !groupString.contains(" "))){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "Group string value is wrong");
        }
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateSettingsForm updateSettingsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings settings = settingsRepository.findById(updateSettingsForm.getId()).orElse(null);
        if(settings == null || !settings.getStatus().equals(LandingISConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.SETTINGS_ERROR_NOT_FOUND, "Not found settings.");
        }
        if(!settings.isEditable()){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_BAD_REQUEST, "This settings can not edit");
        }
        settingsMapper.fromUpdateSettingsFormToEntity(updateSettingsForm,settings);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Update settings success");
        return apiMessageDto;
    }
    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<SettingsDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.SETTINGS_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<SettingsDto> result = new ApiMessageDto<>();

        Settings settings = settingsRepository.findById(id).orElse(null);
        if(settings == null) {
            throw new RequestException(ErrorCode.SETTINGS_ERROR_NOT_FOUND, "Not found settings");
        }
        settingsRepository.delete(settings);
        result.setMessage("Delete settings success");
        return result;
    }
}
