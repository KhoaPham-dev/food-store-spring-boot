package com.landingis.api.form.product;

import com.landingis.api.storage.model.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProductForm {
    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String productName;

    @NotEmpty(message = "description cannot be null")
    @ApiModelProperty(required = true)
    private String productDescription;

    @NotEmpty(message = "shortDescription cannot be null")
    @ApiModelProperty(required = true)
    private String productShortDescription;

    private Integer productSaleoff;

    @ApiModelProperty(name = "parentId")
    private Long parentProductId;

    private String productImage;

    @NotNull(message = "price cannot be null")
    @ApiModelProperty(required = true)
    private Double productPrice;

    @NotNull(message = "categoryId cannot be null")
    @ApiModelProperty(required = true)
    private Long categoryId;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;
}
