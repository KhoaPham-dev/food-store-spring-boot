package com.landingis.api.form.employee;

import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateEmployeeForm {
    private String avatarPath;

    private String password;

    @NotEmpty(message = "username cannot be null")
    @ApiModelProperty(required = true)
    private String username;

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotEmpty(message = "fullName cannot be null")
    @ApiModelProperty(required = true)
    private String fullName;

    @NotEmpty(message = "address cannot be null")
    @ApiModelProperty(required = true)
    private String address;

    @NotEmpty(message = "birthDay cannot be null")
    @ApiModelProperty(required = true)
    private Date birthDay;

    private String note;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;

    @NotEmpty(message = "identityNumber cannot be null")
    @ApiModelProperty(required = true)
    private String identityNumber; //so cmnd

    @NotEmpty(message = "dateOfIssue cannot be null")
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
