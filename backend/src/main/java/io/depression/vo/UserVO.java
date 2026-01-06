package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户视图对象
 */
@Data
@ApiModel(value = "UserVO", description = "用户视图对象")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

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

    @ApiModelProperty("所属医院名称")
    private String cohortName;

    @ApiModelProperty("科室")
    private String department;

    @ApiModelProperty("职称")
    private String title;

    @ApiModelProperty("角色类型：1-系统管理员 2-医院管理员 3-医生")
    private Integer roleType;

    @ApiModelProperty("权限列表JSON")
    private String permissions;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime updateTime;
}


