package com.landingis.api.dto.Employee;

import com.landingis.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.Date;
@Data
public class EmployeeDto extends ABasicAdminDto {
    private Long id;
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
    private String identityNumber; //so cmnd
    private Date dateOfIssue; // ngay cap
    private String placeOfIssue; //noi cap
    private String bankNo;
    private String bankName;
    private String branchName;
    private String labelColor;
    private Double salary = 0d;
}
