package io.depression.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统用户（管理员/医生）
 */
@Data
@TableName("cohort_authorization")
@ApiModel(value = "CohortAuthorization", description = "系统用户（管理员/医生）")
public class CohortAuthorization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码（BCrypt哈希）")
    private String password;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别：0-女 1-男")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("角色：1-系统管理员 2-医院管理员 3-医生")
    private Integer roleType;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("科室")
    private String department;

    @ApiModelProperty("职称")
    private String title;

    @ApiModelProperty("权限列表JSON")
    private String permissions;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;

    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    private Integer deleted;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}




