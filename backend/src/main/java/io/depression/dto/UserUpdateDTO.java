package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户DTO
 */
@Data
@ApiModel(value = "UserUpdateDTO", description = "更新用户请求")
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别：0-女 1-男")
    private Integer gender;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("科室")
    private String department;

    @ApiModelProperty("职称")
    private String title;

    @ApiModelProperty("角色类型：1-系统管理员 2-医院管理员 3-医生")
    private Integer roleType;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;
}
