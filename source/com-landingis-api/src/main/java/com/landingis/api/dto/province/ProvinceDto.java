package com.landingis.api.dto.province;

import com.landingis.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class ProvinceDto extends ABasicAdminDto {
    private String provinceName;
    private Integer provinceKind;
    private Integer parentId;
}
