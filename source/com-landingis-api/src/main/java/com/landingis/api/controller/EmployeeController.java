package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.Employee.EmployeeDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.customer.CustomerDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.customer.CreateCustomerForm;
import com.landingis.api.form.customer.UpdateCustomerForm;
import com.landingis.api.form.employee.CreateEmployeeForm;
import com.landingis.api.form.employee.UpdateEmployeeForm;
import com.landingis.api.mapper.CustomerMapper;
import com.landingis.api.mapper.EmployeeMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CustomerCriteria;
import com.landingis.api.storage.criteria.EmployeeCriteria;
import com.landingis.api.storage.model.Customer;
import com.landingis.api.storage.model.Employee;
import com.landingis.api.storage.model.Group;
import com.landingis.api.storage.repository.AccountRepository;
import com.landingis.api.storage.repository.CustomerRepository;
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
@RequestMapping("/v1/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class EmployeeController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<EmployeeDto>> list(EmployeeCriteria employeeCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<EmployeeDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Employee> listEmployee = employeeRepository.findAll(employeeCriteria.getSpecification(), pageable);
        ResponseListObj<EmployeeDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(employeeMapper.fromEntityListToEmployeeDtoList(listEmployee.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listEmployee.getTotalPages());
        responseListObj.setTotalElements(listEmployee.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<EmployeeDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<EmployeeDto> result = new ApiMessageDto<>();

        Employee employee = employeeRepository.findById(id).orElse(null);
        if(employee == null) {
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_NOT_FOUND, "Not found employee.");
        }
        result.setData(employeeMapper.fromEntityToEmployeeDto(employee));
        result.setMessage("Get employee success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateEmployeeForm createEmployeeForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long accountCheck = accountRepository
                .countAccountByUsername(createEmployeeForm.getUsername());
        if (accountCheck > 0) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Phone is existed");
        }
        Integer groupKind = LandingISConstant.GROUP_KIND_EMPLOYEE;
        Group group = groupRepository.findFirstByKind(groupKind);
        if (group == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Group does not exist!");
        }
        Employee employee = employeeMapper.fromCreateEmployeeFormToEntity(createEmployeeForm);
        employee.getAccount().setGroup(group);
        employee.getAccount().setKind(LandingISConstant.USER_KIND_EMPLOYEE);
        employee.getAccount().setPassword(passwordEncoder.encode(createEmployeeForm.getPassword()));
        employeeRepository.save(employee);
        apiMessageDto.setMessage("Create employee success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateEmployeeForm updateEmployeeForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Employee employee = employeeRepository.findById(updateEmployeeForm.getId()).orElse(null);
        if(employee == null) {
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_NOT_FOUND, "Not found employee.");
        }

        employeeMapper.fromUpdateEmployeeFormToEntity(updateEmployeeForm, employee);
        if (StringUtils.isNoneBlank(updateEmployeeForm.getPassword())) {
            employee.getAccount().setPassword(passwordEncoder.encode(updateEmployeeForm.getPassword()));
        }
        employee.getAccount().setFullName(updateEmployeeForm.getFullName());
        if (StringUtils.isNoneBlank(updateEmployeeForm.getAvatarPath())) {
            if(!updateEmployeeForm.getAvatarPath().equals(employee.getAccount().getAvatarPath())){
                //delete old image
                landingIsApiService.deleteFile(employee.getAccount().getAvatarPath());
            }
            employee.getAccount().setAvatarPath(updateEmployeeForm.getAvatarPath());
        }
        employeeRepository.save(employee);
        apiMessageDto.setMessage("Update employee success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<EmployeeDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<EmployeeDto> result = new ApiMessageDto<>();

        Employee employee = employeeRepository.findById(id).orElse(null);
        if(employee == null) {
            throw new RequestException(ErrorCode.EMPLOYEE_ERROR_NOT_FOUND, "Not found employee");
        }
        landingIsApiService.deleteFile(employee.getAccount().getAvatarPath());
        employeeRepository.delete(employee);
        result.setMessage("Delete employee success");
        return result;
    }
}
