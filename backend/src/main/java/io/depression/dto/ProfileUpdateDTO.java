package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 个人信息更新DTO
 */
@Data
@ApiModel(value = "ProfileUpdateDTO", description = "个人信息更新请求")
public class ProfileUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别：0-女 1-男")
    private Integer gender;
}
