package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.collaborator.CollaboratorDto;
import com.landingis.api.dto.collaborator.CollaboratorProductDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.collaborator.CreateCollaboratorForm;
import com.landingis.api.form.collaborator.CreateCollaboratorProductForm;
import com.landingis.api.form.collaborator.UpdateCollaboratorForm;
import com.landingis.api.form.collaborator.UpdateCollaboratorProductForm;
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
        if(kind == 1){
            if(!(value >= LandingISConstant.MIN_OF_PERCENT)||!(value <= LandingISConstant.MAX_OF_PERCENT)){
                throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Wrong value of kind percent");
            }
        }
        if(kind == 2){
            if(!(value > LandingISConstant.MIN_PRICE)){
                throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Price can not be negative");
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
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCollaboratorProductForm createCollaboratorProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Collaborator collaborator = collaboratorRepository.findById(createCollaboratorProductForm.getCollaboratorId()).orElse(null);
        if(collaborator == null ||!collaborator.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Collaborator does not exist!");
        }
        Product product = productRepository.findById(createCollaboratorProductForm.getProductId()).orElse(null);
        if(product == null ||!product.getStatus().equals(LandingISConstant.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.GENERAL_ERROR_NOT_FOUND, "Product does not exist!");
        }
        Integer kind = createCollaboratorProductForm.getKind();
        Double valueCheck = createCollaboratorProductForm.getValue();
        checkKindAndValue(kind,valueCheck);
        CollaboratorProduct collaboratorProduct = collaboratorProductMapper.fromCreateCollaboratorProductFormToEntity(createCollaboratorProductForm);
        collaboratorProductRepository.save(collaboratorProduct);
        apiMessageDto.setMessage("Create collaborator-product success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCollaboratorProductForm updateCollaboratorProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        CollaboratorProduct collaboratorProduct = collaboratorProductRepository.findById(updateCollaboratorProductForm.getId()).orElse(null);
        if(collaboratorProduct == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "Not found collaborator-product.");
        }
        collaboratorProductMapper.fromUpdateCollaboratorProductFormToEntity(updateCollaboratorProductForm, collaboratorProduct);
        checkKindAndValue(collaboratorProduct.getKind(),collaboratorProduct.getValue());
        collaboratorProductRepository.save(collaboratorProduct);
        apiMessageDto.setMessage("Update collaborator-product success");
        return apiMessageDto;
    }



    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CollaboratorProductDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<CollaboratorProductDto> result = new ApiMessageDto<>();

        CollaboratorProduct collaboratorProduct = collaboratorProductRepository.findById(id).orElse(null);
        if(collaboratorProduct == null) {
            throw new RequestException(ErrorCode.COLLABORATOR_PRODUCT_ERROR_NOT_FOUND, "Not found collaborator-product");
        }
        collaboratorProductRepository.delete(collaboratorProduct);
        result.setMessage("Delete collaborator-product success");
        return result;
    }
}
