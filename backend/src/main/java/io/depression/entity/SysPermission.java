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
import java.time.LocalDateTime;

/**
 * 功能权限
 */
@Data
@TableName("sys_permission")
@ApiModel(value = "SysPermission", description = "功能权限配置")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("权限ID")
    private Long id;

    @ApiModelProperty("权限编码")
    private String permissionCode;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("类型：1-菜单 2-按钮 3-数据")
    private Integer permissionType;

    @ApiModelProperty("父权限ID")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("路由路径")
    private String path;

    @ApiModelProperty("描述")
    private String description;

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




