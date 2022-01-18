package com.landingis.api.mapper;

import com.landingis.api.dto.addresses.AddressesDto;
import com.landingis.api.dto.category.CategoryDto;
import com.landingis.api.dto.product.ProductDto;
import com.landingis.api.form.addresses.CreateAddressesForm;
import com.landingis.api.form.addresses.UpdateAddressesForm;
import com.landingis.api.form.product.CreateProductForm;
import com.landingis.api.form.product.UpdateProductForm;
import com.landingis.api.storage.model.Addresses;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "productShortDescription", target = "shortDescription")
    @Mapping(source = "productSaleoff", target = "saleoff")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Product fromCreateProductFormToEntity(CreateProductForm createProductForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productDescription", target = "description")
    @Mapping(source = "productShortDescription", target = "shortDescription")
    @Mapping(source = "productSaleoff", target = "saleoff")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    void fromUpdateProductFormToEntity(UpdateProductForm updateProductForm, @MappingTarget Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "hasChild", target = "hasChild")
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    ProductDto fromEntityToAdminDto(Product product);

    List<ProductDto> fromEntityListToProductDtoList(List<Product> products);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "hasChild", target = "hasChild")
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "productList", target = "productDtoList",qualifiedByName="productGetListAutoCompleteMapping")
    @BeanMapping(ignoreByDefault = true)
    @Named("productAutoCompleteMapping")
    ProductDto fromEntityToAdminDtoAutoComplete(Product product);

    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "productAutoCompleteMapping")
    @Named("productGetListAutoCompleteMapping")
    List<ProductDto> fromEntityListToProductDtoAutoComplete(List<Product> products);


}
