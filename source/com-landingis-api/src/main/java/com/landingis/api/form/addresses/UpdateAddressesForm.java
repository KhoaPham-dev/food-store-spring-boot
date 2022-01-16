package com.landingis.api.form.addresses;

import com.landingis.api.validation.ProvinceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAddressesForm {
    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String name;

    @NotEmpty(message = "phone cannot be null")
    @ApiModelProperty(required = true)
    private String phone;

    @NotNull(message = "addresses_id cannot be null")
    @ApiModelProperty(required = true)
    private Long addressesId;

    @NotEmpty(message = "address cannot be null")
    @ApiModelProperty(required = true)
    private String address;

    @NotNull(message = "commune_id cannot be null")
    @ApiModelProperty(required = true)
    private Long communeId;

    @NotNull(message = "district_id cannot be null")
    @ApiModelProperty(required = true)
    private Long districtId;

    @NotNull(message = "province_id cannot be null")
    @ApiModelProperty(required = true)
    private Long provinceId;
}
