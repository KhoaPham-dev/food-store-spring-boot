package com.landingis.api.dto.collaborator;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.group.GroupDto;
import lombok.Data;

import java.util.Date;

@Data
public class CollaboratorDto extends ABasicAdminDto {
    private Long employeeId;
    //update password --> lấy session
    private String username;// 1
    private String address;// 1
    private Date birthDay;// 1
    private Integer sex;// 1
    private String note;
    private Integer status;
    private Integer kind;// 1
    private String email;// 1
    private String avatarPath;// 1
    private String fullName;// 1
    private String phone;// 1
    private String identityNumber; //so cmnd// 1
    private Date dateOfIssue; // ngay cap// 1
    private String placeOfIssue; //noi cap// 1
    private GroupDto groupDto; //
    private String bankNo;// 1
    private String bankName;// 1
    private String branchName;// 1
}
