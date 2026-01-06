package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询DTO
 */
@Data
@ApiModel(value = "UserQueryDTO", description = "用户查询条件")
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Integer current = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;

    @ApiModelProperty("医院编码")
    private String cohortCode;

    @ApiModelProperty("角色类型：1-系统管理员 2-医院管理员 3-医生")
    private Integer roleType;

    @ApiModelProperty("用户状态：0-禁用 1-启用")
    private Integer status;

    @ApiModelProperty("关键字（用户名/姓名/手机号）")
    private String keyword;
}
