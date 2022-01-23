package com.landingis.api.dto.customer;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.account.AccountDto;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto extends ABasicAdminDto {

    private Long id;
    private AccountDto accountDto;
    private String address;
    private Date birthDay;
    private Integer sex;
    private String note;
    private Integer status;
    private Integer kind;
    private String email;
    private String avatarPath;
    private String fullName;
    private String phone;
    private Boolean isAdminCreated;
}
