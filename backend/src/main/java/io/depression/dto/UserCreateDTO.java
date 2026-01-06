package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建用户DTO
 */
@Data
@ApiModel(value = "UserCreateDTO", description = "创建用户请求")
public class UserCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "真实姓名", required = true)
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @ApiModelProperty("初始密码（为空则默认手机号后6位）")
    private String password;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
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

    @ApiModelProperty(value = "角色类型：1-系统管理员 2-医院管理员 3-医生", required = true)
    @NotNull(message = "角色类型不能为空")
    private Integer roleType;

    @ApiModelProperty("权限编码列表(JSON字符串或逗号分隔均可)")
    private String permissions;
}


