package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.employee.EmployeeDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.collaborator.CreateCollaboratorForm;
import com.landingis.api.form.collaborator.UpdateCollaboratorForm;
import com.landingis.api.form.collaborator.UpdateCollaboratorProfileForm;
import com.landingis.api.mapper.CollaboratorMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CollaboratorCriteria;
import com.landingis.api.storage.model.Collaborator;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.Group;
import com.landingis.api.storage.repository.AccountRepository;
import com.landingis.api.storage.repository.CollaboratorRepository;
import com.landingis.api.storage.repository.EmployeeRepository;
import com.landingis.api.storage.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/collaborator")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CollaboratorController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    CollaboratorMapper collaboratorMapper;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CollaboratorDto>> list(CollaboratorCriteria collaboratorCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<CollaboratorDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Collaborator> listCollaborator = collaboratorRepository.findAll(collaboratorCriteria.getSpecification(), pageable);
        ResponseListObj<CollaboratorDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(collaboratorMapper.fromEntityListToCollaboratorDtoList(listCollaborator.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCollaborator.getTotalPages());
        responseListObj.setTotalElements(listCollaborator.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CollaboratorDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CollaboratorDto> result = new ApiMessageDto<>();

        Collaborator collaborator = collaboratorRepository.findById(id).orElse(null);
        if(collaborator == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Not found collaborator.");
        }
        result.setData(collaboratorMapper.fromEntityToCollaboratorDto(collaborator));
        result.setMessage("Get collaborator success");
        return result;
    }
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CollaboratorDto> getProfile() {
        if(!isCollaborator()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CollaboratorDto> result = new ApiMessageDto<>();
        Long id = getCurrentUserId();
        Collaborator collaborator = collaboratorRepository.findByAccountId(id);
        if(collaborator == null || !collaborator.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Not found collaborator.");
        }
        result.setData(collaboratorMapper.fromEntityToCollaboratorProfileDto(collaborator));
        result.setMessage("Get collaborator success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCollaboratorForm createCollaboratorForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long accountCheck = accountRepository
                .countAccountByUsername(createCollaboratorForm.getUsername());
        if (accountCheck > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXIST, "Username is existed");
        }
        Employee employee = employeeRepository.findById(createCollaboratorForm.getEmployeeId()).orElse(null);
        if(employee == null ||!employee.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_NOT_FOUND, "Employee does not exist!");
        }
        Integer groupKind = LandingISConstant.GROUP_KIND_COLLABORATOR;
        Group group = groupRepository.findFirstByKind(groupKind);
        if (group == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND, "Group does not exist!");
        }
        Collaborator collaborator = collaboratorMapper.fromCreateCollaboratorFormToEntity(createCollaboratorForm);
        collaborator.setEmployee(employee);
        collaborator.getAccount().setGroup(group);
        collaborator.getAccount().setKind(LandingISConstant.USER_KIND_COLLABORATOR);
        if(StringUtils.isNoneBlank(createCollaboratorForm.getPassword())){
            collaborator.getAccount().setPassword(passwordEncoder.encode(createCollaboratorForm.getPassword()));
        }
        collaboratorRepository.save(collaborator);
        apiMessageDto.setMessage("Create collaborator success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCollaboratorForm updateCollaboratorForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Collaborator collaborator = collaboratorRepository.findById(updateCollaboratorForm.getId()).orElse(null);
        if(collaborator == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Not found collaborator.");
        }

        collaboratorMapper.fromUpdateCollaboratorFormToEntity(updateCollaboratorForm, collaborator);
        if (StringUtils.isNoneBlank(updateCollaboratorForm.getPassword())) {
            collaborator.getAccount().setPassword(passwordEncoder.encode(updateCollaboratorForm.getPassword()));
        }
        collaborator.getAccount().setFullName(updateCollaboratorForm.getFullName());
        if (StringUtils.isNoneBlank(updateCollaboratorForm.getAvatarPath())) {
            if(!updateCollaboratorForm.getAvatarPath().equals(collaborator.getAccount().getAvatarPath())){
                //delete old image
                landingIsApiService.deleteFile(collaborator.getAccount().getAvatarPath());
            }
            collaborator.getAccount().setAvatarPath(updateCollaboratorForm.getAvatarPath());
        }
        collaboratorRepository.save(collaborator);
        apiMessageDto.setMessage("Update collaborator success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateCollaboratorProfileForm updateCollaboratorForm, BindingResult bindingResult) {
        if(!isCollaborator()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long id = getCurrentUserId();
        Collaborator collaborator = collaboratorRepository.findByAccountId(id);
        if(collaborator == null || !collaborator.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Not found collaborator.");
        }
        if (StringUtils.isNoneBlank(updateCollaboratorForm.getCollaboratorOldPassword())) {
            if(!passwordEncoder.matches(updateCollaboratorForm.getCollaboratorOldPassword(), collaborator.getAccount().getPassword())){
                throw new RequestException(ErrorCode.COLLABORATOR_ERROR_BAD_REQUEST, "Old password is wrong.");
            }
        }
        if (StringUtils.isNoneBlank(updateCollaboratorForm.getCollaboratorNewPassword())) {
            if(passwordEncoder.matches(updateCollaboratorForm.getCollaboratorNewPassword(), collaborator.getAccount().getPassword())){
                throw new RequestException(ErrorCode.COLLABORATOR_ERROR_BAD_REQUEST, "New password is already in use.");
            }
            collaborator.getAccount().setPassword(passwordEncoder.encode(updateCollaboratorForm.getCollaboratorNewPassword()));
        }
        collaboratorRepository.save(collaborator);
        apiMessageDto.setMessage("Update collaborator success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CollaboratorDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CollaboratorDto> result = new ApiMessageDto<>();

        Collaborator collaborator = collaboratorRepository.findById(id).orElse(null);
        if(collaborator == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Not found collaborator");
        }
        landingIsApiService.deleteFile(collaborator.getAccount().getAvatarPath());
        collaboratorRepository.delete(collaborator);
        result.setMessage("Delete collaborator success");
        return result;
    }
}
