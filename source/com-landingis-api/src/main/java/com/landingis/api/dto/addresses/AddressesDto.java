package com.landingis.api.dto.addresses;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.customer.CustomerDto;
import com.landingis.api.dto.province.ProvinceDto;
import com.landingis.api.storage.model.Customer;
import com.landingis.api.storage.model.Province;
import lombok.Data;

@Data
public class AddressesDto extends ABasicAdminDto {
    private CustomerDto customerDto;
    private String name;
    private String phone;
    private String address;
    private ProvinceDto addressesDistrictDto;
    private ProvinceDto addressesCommuneDto;
    private ProvinceDto addressesProvinceDto;
}
