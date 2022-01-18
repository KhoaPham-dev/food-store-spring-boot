package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.collaborator.CollaboratorProductDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.collaborator.*;
import com.landingis.api.mapper.CollaboratorMapper;
import com.landingis.api.mapper.CollaboratorProductMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CollaboratorCriteria;
import com.landingis.api.storage.criteria.CollaboratorProductCriteria;
import com.landingis.api.storage.model.*;
import com.landingis.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/v1/collaborator-product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CollaboratorProductController extends ABasicController {
    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    CollaboratorProductRepository collaboratorProductRepository;

    @Autowired
    CollaboratorProductMapper collaboratorProductMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LandingIsApiService landingIsApiService;

    private void checkKindAndValue(Integer kind, Double value) {
        if(kind.equals(LandingISConstant.COLLABORATOR_KIND_PERCENT)){
            if(!(value >= LandingISConstant.MIN_OF_PERCENT)||!(value <= LandingISConstant.MAX_OF_PERCENT)){
                throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_BAD_REQUEST, "Wrong value of kind percent");
            }
        }
        if(kind.equals(LandingISConstant.COLLABORATOR_KIND_DOLLAR)){
            if(!(value > LandingISConstant.MIN_PRICE)){
                throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_BAD_REQUEST, "Price can not be negative");
            }
        }
    }

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CollaboratorProductDto>> list(CollaboratorProductCriteria collaboratorProductCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<CollaboratorProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<CollaboratorProduct> listCollaboratorProduct = collaboratorProductRepository.findAll(collaboratorProductCriteria.getSpecification(), pageable);
        ResponseListObj<CollaboratorProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(collaboratorProductMapper.fromEntityListToCollaboratorProductDtoList(listCollaboratorProduct.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCollaboratorProduct.getTotalPages());
        responseListObj.setTotalElements(listCollaboratorProduct.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CollaboratorProductDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CollaboratorProductDto> result = new ApiMessageDto<>();

        CollaboratorProduct collaboratorProduct = collaboratorProductRepository.findById(id).orElse(null);
        if(collaboratorProduct == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "Not found collaborator-product.");
        }
        result.setData(collaboratorProductMapper.fromEntityToCollaboratorProductDto(collaboratorProduct));
        result.setMessage("Get collaborator-product success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCollaboratorProductListForm createCollaboratorProductListForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        int check = 0;
        List<CollaboratorProduct> collaboratorProductList = collaboratorProductMapper
                .fromCreateCollaboratorProductListFormToEntityList(createCollaboratorProductListForm.getCreateCollaboratorProductFormList()) ;
        for (CollaboratorProduct collaboratorProduct : collaboratorProductList){
            Collaborator collaborator = collaboratorRepository.findById(collaboratorProduct.getCollaborator().getId()).orElse(null);
            if(collaborator == null ||!collaborator.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
                throw new RequestException(ErrorCode.COLLABORATOR_ERROR_NOT_FOUND, "Collaborator of index " + check + " does not exist!");
            }
            Product product = productRepository.findById(collaboratorProduct.getProduct().getId()).orElse(null);
            if(product == null ||!product.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
                throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Product of index "+ check + " does not exist!");
            }
            Integer kind = collaboratorProduct.getKind();
            Double valueCheck = collaboratorProduct.getValue();
            checkKindAndValue(kind,valueCheck);
            check++;
        }
        collaboratorProductRepository.saveAll(collaboratorProductList);
        apiMessageDto.setMessage("Create collaborator-product list success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCollaboratorProductListForm updateCollaboratorProductListForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        List<CollaboratorProduct> collaboratorProductList = collaboratorProductMapper
                .fromUpdateCollaboratorProductListFormToEntityList(updateCollaboratorProductListForm.getUpdateCollaboratorProductFormList());
        int check = 0;
        for (CollaboratorProduct collaboratorProduct : collaboratorProductList){
            CollaboratorProduct collaboratorProductCheck = collaboratorProductRepository.findById(collaboratorProduct.getId()).orElse(null);
            if(collaboratorProductCheck == null || !collaboratorProductCheck.getStatus().equals(LandingISConstant.STATUS_ACTIVE)) {
                throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "Not found collaborator-product in index " + check);
            }
            checkKindAndValue(collaboratorProduct.getKind(),collaboratorProduct.getValue());
            check++;
        }
        collaboratorProductRepository.saveAll(collaboratorProductList);
        apiMessageDto.setMessage("Update collaborator-product list success");
        return apiMessageDto;
    }



    @DeleteMapping(value = "/delete")
    public ApiMessageDto<CollaboratorProductDto> delete(@RequestBody DeleteCollaboratorProductListForm deleteCollaboratorProductListForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CollaboratorProductDto> result = new ApiMessageDto<>();
        List<CollaboratorProduct> collaboratorProductList = collaboratorProductMapper
                .fromDeleteCollaboratorProductListFormToEntityList(deleteCollaboratorProductListForm.getDeleteCollaboratorProductFormList());
        int check = 0;
        for (CollaboratorProduct collaboratorProduct : collaboratorProductList){
            CollaboratorProduct collaboratorProductCheck = collaboratorProductRepository.findById(collaboratorProduct.getId()).orElse(null);
            if(collaboratorProductCheck == null) {
                throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "Not found collaborator-product in index " + check);
            }
            check++;
        }
        collaboratorProductRepository.deleteAll(collaboratorProductList);
        result.setMessage("Delete collaborator-product success");
        return result;
    }
}
