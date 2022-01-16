package com.landingis.api.form.addresses;

import com.landingis.api.validation.ProvinceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAddressesForm {
    @NotNull(message = "addresses_id cannot be null")
    @ApiModelProperty(required = true)
    private Long addresses_id;

    @NotEmpty(message = "address cannot be null")
    @ApiModelProperty(required = true)
    private String address;

    @NotNull(message = "commune_id cannot be null")
    @ApiModelProperty(required = true)
    private Long commune_id;

    @NotNull(message = "district_id cannot be null")
    @ApiModelProperty(required = true)
    private Long district_id;

    @NotNull(message = "province_id cannot be null")
    @ApiModelProperty(required = true)
    private Long province_id;
}
