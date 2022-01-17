package com.landingis.api.dto.product;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.storage.model.Category;
import com.landingis.api.storage.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto extends ABasicAdminDto {
    private String name;
    private Double price;
    private String image;
    private String description;
    private String shortDescription;
    private Long categoryId;
    private Long parentProductId;
    private Boolean hasChild = false;
    private String labelColor;
    private Integer saleoff;
    private List<ProductDto> productDtoList;
}
