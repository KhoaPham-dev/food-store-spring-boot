package com.landingis.api.dto.employee;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.account.AccountDto;
import com.landingis.api.dto.group.GroupDto;
import lombok.Data;

import java.util.Date;
@Data
public class EmployeeDto extends ABasicAdminDto {
    private Long id;  //
    private AccountDto accountDto;
    //update password
    private String username; //
    private String address;//
    private Date birthDay;//
    private Integer sex;//
    private String note;
    private Integer status;
    private Integer kind; //
    private String email; //
    private String avatarPath; //
    private String fullName; //
    private String phone; //
    private String identityNumber; //so cmnd //
    private Date dateOfIssue; // ngay cap //
    private String placeOfIssue; //noi cap //
    private String bankNo;//
    private String bankName;//
    private String branchName;//
    private String labelColor;
    private GroupDto groupDto; // --> lấy permission
    private Double salary = 0d;//
}
