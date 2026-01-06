package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应VO
 */
@Data
@ApiModel(value = "LoginVO", description = "登录响应")
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("用户信息")
    private UserVO userInfo;
}


