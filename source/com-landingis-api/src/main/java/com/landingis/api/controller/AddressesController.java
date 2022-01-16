package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.addresses.AddressesDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.addresses.CreateAddressesForm;
import com.landingis.api.form.addresses.UpdateAddressesForm;
import com.landingis.api.mapper.AddressesMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.AddressesCriteria;
import com.landingis.api.storage.model.Account;
import com.landingis.api.storage.model.Addresses;
import com.landingis.api.storage.model.Province;
import com.landingis.api.storage.repository.AccountRepository;
import com.landingis.api.storage.repository.AddressesRepository;
import com.landingis.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AddressesController extends ABasicController{
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    AddressesRepository addressesRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AddressesMapper addressesMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    public void checkProvince(Province commune, Province district, Province province) {
        if(commune == null || district == null || province == null){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Not found province.");
        }
        if(!province.getKind().equals(LandingISConstant.PROVINCE_KIND_PROVINCE)){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Wrong province.");
        }
        if(!district.getParentProvince().getId().equals(province.getId())
                || !commune.getParentProvince().getId().equals(district.getId())){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND, "Wrong province.");
        }
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AddressesDto>> list(AddressesCriteria addressesCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<AddressesDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Addresses> listAddresses = addressesRepository.findAll(addressesCriteria.getSpecification(), pageable);
        ResponseListObj<AddressesDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(addressesMapper.fromEntityListToAddressesDtoList(listAddresses.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listAddresses.getTotalPages());
        responseListObj.setTotalElements(listAddresses.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AddressesDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<AddressesDto> result = new ApiMessageDto<>();

        Addresses addresses = addressesRepository.findById(id).orElse(null);
        if(addresses == null) {
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_NOT_FOUND, "Not found addresses.");
        }
        result.setData(addressesMapper.fromEntityToAdminDto(addresses));
        result.setMessage("Get addresses success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateAddressesForm createAddressesForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Addresses addresses = addressesMapper.fromCreateAddressesFormToEntity(createAddressesForm);
        Account accountCheck = accountRepository
                .findAccountByPhone(createAddressesForm.getPhone());
        if (accountCheck == null) {
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Phone is not existed");
        }
        Province commune = provinceRepository.findById(createAddressesForm.getCommune_id()).orElse(null);
        Province district = provinceRepository.findById(createAddressesForm.getDistrict_id()).orElse(null);
        Province province = provinceRepository.findById(createAddressesForm.getProvince_id()).orElse(null);
        checkProvince(commune,district,province);
        addresses.setCommune(commune);
        addresses.setDistrict(district);
        addresses.setProvince(province);
        addresses.setAccount(accountCheck);
        addressesRepository.save(addresses);
        apiMessageDto.setMessage("Create addresses success");
        return apiMessageDto;

    }



    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateAddressesForm updateAddressesForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Addresses addresses = addressesRepository.findById(updateAddressesForm.getAddresses_id()).orElse(null);
        if(addresses == null) {
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_NOT_FOUND, "Not found addresses.");
        }
        addressesMapper.fromUpdateAddressesFormToEntity(updateAddressesForm, addresses);
        Province commune = provinceRepository.findById(updateAddressesForm.getCommune_id()).orElse(null);
        Province district = provinceRepository.findById(updateAddressesForm.getDistrict_id()).orElse(null);
        Province province = provinceRepository.findById(updateAddressesForm.getProvince_id()).orElse(null);
        checkProvince(commune,district,province);
        addresses.setCommune(commune);
        addresses.setDistrict(district);
        addresses.setProvince(province);
        addressesRepository.save(addresses);
        apiMessageDto.setMessage("Update addresses success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<AddressesDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<AddressesDto> result = new ApiMessageDto<>();

        Addresses addresses = addressesRepository.findById(id).orElse(null);
        if(addresses == null) {
            throw new RequestException(ErrorCode.ADDRESSES_ERROR_NOT_FOUND, "Not found addresses");
        }
        addressesRepository.delete(addresses);
        result.setMessage("Delete addresses success");
        return result;
    }
}
