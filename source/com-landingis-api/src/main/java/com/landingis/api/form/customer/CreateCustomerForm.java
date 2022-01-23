package com.landingis.api.form.customer;

import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateCustomerForm {
    @ApiModelProperty(required = true)
    @NotEmpty(message = "email cannot be null")
    @Email
    private String email;

    private String avatarPath;

    @NotEmpty(message = "fullName cannot be null")
    @ApiModelProperty(required = true)
    private String fullName;

    private String password;

    @NotEmpty(message = "phone cannot be null")
    @ApiModelProperty(required = true)
    private String phone;

    @ApiModelProperty(name = "address")
    @NotEmpty(message = "address cannot be null")
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

    @NotNull(message = "isAdminCreated cannot be null")
    @ApiModelProperty(required = true)
    private Boolean isAdminCreated;
}
