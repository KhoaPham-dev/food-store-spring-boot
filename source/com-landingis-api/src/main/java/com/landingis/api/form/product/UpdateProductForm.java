package com.landingis.api.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

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

    @NotNull(message = "hasChild cannot be null")
    @ApiModelProperty(required = true)
    private Boolean hasChild;

    private String productImage;

    @NotNull(message = "price cannot be null")
    @ApiModelProperty(required = true)
    private Double productPrice;

    private Integer status;
}
