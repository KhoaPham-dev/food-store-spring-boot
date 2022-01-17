package com.landingis.api.form.employee;

import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateEmployeeForm {
    @ApiModelProperty(required = true)
    @Email
    private String email;

    private String avatarPath;

    @NotEmpty(message = "fullName cannot be null")
    @ApiModelProperty(required = true)
    private String fullName;

    @NotEmpty(message = "password cannot be null")
    @ApiModelProperty(required = true)
    private String password;

    @NotEmpty(message = "username cannot be null")
    @ApiModelProperty(required = true)
    private String username;

    @NotEmpty(message = "phone cannot be null")
    @ApiModelProperty(required = true)
    private String phone;

    @ApiModelProperty(name = "address")
    private String address;

    @NotNull(message = "birthDay cannot be null")
    @ApiModelProperty(required = true)
    private Date birthDay;

    @NotNull(message = "Sex cannot be null")
    @ApiModelProperty(required = true)
    private Integer sex;

    private String note;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;

    @NotEmpty(message = "identityNumber cannot be null")
    @ApiModelProperty(required = true)
    private String identityNumber; //so cmnd

    @NotNull(message = "dateOfIssue cannot be null")
    @ApiModelProperty(required = true)
    private Date dateOfIssue; // ngay cap

    @NotEmpty(message = "placeOfIssue cannot be null")
    @ApiModelProperty(required = true)
    private String placeOfIssue; //noi cap

    @NotEmpty(message = "bankNo cannot be null")
    @ApiModelProperty(required = true)
    private String bankNo;

    @NotEmpty(message = "bankName cannot be null")
    @ApiModelProperty(required = true)
    private String bankName;

    @NotEmpty(message = "branchName cannot be null")
    @ApiModelProperty(required = true)
    private String branchName;

    private String labelColor;

    @NotNull(message = "salary cannot be null")
    @ApiModelProperty(required = true)
    private Double salary = 0d;
}
