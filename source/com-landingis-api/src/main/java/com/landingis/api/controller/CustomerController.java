package com.landingis.api.controller;


import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.customer.CustomerDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.category.CreateCategoryForm;
import com.landingis.api.form.category.UpdateCategoryForm;
import com.landingis.api.form.customer.CreateCustomerForm;
import com.landingis.api.form.customer.UpdateCustomerForm;
import com.landingis.api.mapper.CustomerMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CategoryCriteria;
import com.landingis.api.storage.criteria.CustomerCriteria;
import com.landingis.api.storage.model.Account;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Customer;
import com.landingis.api.storage.model.Group;
import com.landingis.api.storage.repository.AccountRepository;
import com.landingis.api.storage.repository.CustomerRepository;
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
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController extends ABasicController{
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LandingIsApiService landingIsApiService;

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerDto>>  list(CustomerCriteria customerCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<CustomerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Customer> listCustomer = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        ResponseListObj<CustomerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(customerMapper.fromEntityListToCustomerDtoList(listCustomer.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCustomer.getTotalPages());
        responseListObj.setTotalElements(listCustomer.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();

        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer.");
        }
        result.setData(customerMapper.fromEntityToCustomerDto(customer));
        result.setMessage("Get customer success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Long accountCheck = accountRepository
                .countAccountByPhone(createCustomerForm.getPhone());
        if (accountCheck > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXIST, "Phone is existed");
        }
        Integer groupKind = LandingISConstant.GROUP_KIND_CUSTOMER;
        Group group = groupRepository.findFirstByKind(groupKind);
        if (group == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND, "Group does not exist!");
        }
        Customer customer = customerMapper.fromCreateCustomerFormToEntity(createCustomerForm);
        customer.getAccount().setGroup(group);
        customer.getAccount().setKind(LandingISConstant.USER_KIND_CUSTOMER);
        customer.getAccount().setPassword(passwordEncoder.encode(createCustomerForm.getPassword()));
        customerRepository.save(customer);
        apiMessageDto.setMessage("Create customer success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(updateCustomerForm.getId()).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer.");
        }

        customerMapper.fromUpdateCustomerFormToEntity(updateCustomerForm, customer);
        if (StringUtils.isNoneBlank(updateCustomerForm.getPassword())) {
            customer.getAccount().setPassword(passwordEncoder.encode(updateCustomerForm.getPassword()));
        }
        customer.getAccount().setFullName(updateCustomerForm.getFullName());
        if (StringUtils.isNoneBlank(updateCustomerForm.getAvatarPath())) {
            if(!updateCustomerForm.getAvatarPath().equals(customer.getAccount().getAvatarPath())){
                //delete old image
                landingIsApiService.deleteFile(customer.getAccount().getAvatarPath());
            }
            customer.getAccount().setAvatarPath(updateCustomerForm.getAvatarPath());
        }
        customerRepository.save(customer);
        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CustomerDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();

        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found customer");
        }
        landingIsApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerRepository.delete(customer);
        result.setMessage("Delete customer success");
        return result;
    }

}
