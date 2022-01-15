package com.landingis.api.form.province;

import com.landingis.api.validation.ProvinceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProvinceForm {
    @NotEmpty(message = "provinceName cannot be null")
    @ApiModelProperty(required = true)
    private String provinceName;

    @ProvinceKind
    @NotNull(message = "provinceKind cannot be null")
    @ApiModelProperty(required = true)
    private Integer provinceKind;

    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
