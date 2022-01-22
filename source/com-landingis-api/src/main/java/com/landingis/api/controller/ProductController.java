package com.landingis.api.controller;

import com.landingis.api.constant.LandingISConstant;
import com.landingis.api.dto.ApiMessageDto;
import com.landingis.api.dto.ErrorCode;
import com.landingis.api.dto.ResponseListObj;
import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.news.NewsDto;
import com.landingis.api.dto.product.ProductDto;
import com.landingis.api.exception.RequestException;
import com.landingis.api.form.category.CreateCategoryForm;
import com.landingis.api.form.category.UpdateCategoryForm;
import com.landingis.api.form.import_export.CreateImportExportForm;
import com.landingis.api.form.news.CreateNewsForm;
import com.landingis.api.form.news.UpdateNewsForm;
import com.landingis.api.form.product.CreateProductForm;
import com.landingis.api.form.product.UpdateProductForm;
import com.landingis.api.mapper.NewsMapper;
import com.landingis.api.mapper.ProductMapper;
import com.landingis.api.service.LandingIsApiService;
import com.landingis.api.storage.criteria.CategoryCriteria;
import com.landingis.api.storage.criteria.NewsCriteria;
import com.landingis.api.storage.criteria.ProductCriteria;
import com.landingis.api.storage.model.Account;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.News;
import com.landingis.api.storage.model.Product;
import com.landingis.api.storage.repository.AccountRepository;
import com.landingis.api.storage.repository.CategoryRepository;
import com.landingis.api.storage.repository.NewsRepository;
import com.landingis.api.storage.repository.ProductRepository;
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
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductController extends ABasicController{
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    LandingIsApiService landingIsApiService;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> list(ProductCriteria productCriteria, Pageable pageable) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<ProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> productList = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListObj<ProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoList(productList.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(productList.getTotalPages());
        responseListObj.setTotalElements(productList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductDto>> autoComplete(ProductCriteria productCriteria) {
        ApiMessageDto<ResponseListObj<ProductDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        productCriteria.setStatus(LandingISConstant.STATUS_ACTIVE);
        Page<Product> listProduct = productRepository.findAll(productCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<ProductDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoAutoComplete(listProduct.getContent()));
        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ProductDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product.");
        }
        result.setData(productMapper.fromEntityToAdminDto(product));
        result.setMessage("Get product success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Product product = productMapper.fromCreateProductFormToEntity(createProductForm);
        checkCategory(createProductForm);
        if(createProductForm.getParentProductId() != null) {
            Product parentProduct = productRepository.findById(createProductForm.getParentProductId()).orElse(null);
            if(parentProduct == null) {
                throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product parent");
            }
            parentProduct.setHasChild(true);
            productRepository.save(parentProduct);
            product.setParentProduct(parentProduct);
        }
        productRepository.save(product);
        apiMessageDto.setMessage("Create product success");
        return apiMessageDto;

    }
    private void checkCategory(CreateProductForm createProductForm) {
        Category categoryCheck = categoryRepository.findById(createProductForm.getCategoryId()).orElse(null);
        if (categoryCheck == null || categoryCheck.getStatus()==0){
            throw new RequestException(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Category not found");
        }
        if(!categoryCheck.getKind().equals(LandingISConstant.CATEGORY_KIND_PRODUCT)){
            throw new RequestException(ErrorCode.CATEGORY_ERROR_BAD_REQUEST, "Category is not product kind");
        }
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productRepository.findById(updateProductForm.getId()).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product.");
        }
        if(!product.getStatus().equals(updateProductForm.getStatus())) {
            if(product.getHasChild()){
                product.getProductList().forEach(child -> child.setStatus(updateProductForm.getStatus()));
                productRepository.saveAll(product.getProductList());
            }
            product.setStatus(updateProductForm.getStatus());
            productRepository.save(product);
        }
        if(StringUtils.isNoneBlank(product.getImage()) && !updateProductForm.getProductImage().equals(product.getImage())) {
            landingIsApiService.deleteFile(product.getImage());
        }
        productMapper.fromUpdateProductFormToEntity(updateProductForm, product);
        productRepository.save(product);
        apiMessageDto.setMessage("Update product success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<ProductDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_ERROR_UNAUTHORIZED, "Not allowed to delete.");
        }
        ApiMessageDto<ProductDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new RequestException(ErrorCode.PRODUCT_ERROR_NOT_FOUND, "Not found product");
        }
        landingIsApiService.deleteFile(product.getImage());
        productRepository.delete(product);
        result.setMessage("Delete product success");
        return result;
    }
}
