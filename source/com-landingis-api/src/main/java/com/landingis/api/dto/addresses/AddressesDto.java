package com.landingis.api.dto.addresses;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.storage.model.Province;
import lombok.Data;

@Data
public class AddressesDto extends ABasicAdminDto {
    private String fullName;
    private String phone;
    private String address;
    private Province addressesDistrict;
    private Province addressesCommune;
    private Province addressesProvince;
}
