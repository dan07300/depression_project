package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限树VO
 */
@Data
@ApiModel(value = "PermissionTreeVO", description = "权限树视图对象")
public class PermissionTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("权限ID")
    private Long id;

    @ApiModelProperty("权限编码")
    private String permissionCode;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("类型：1-菜单 2-按钮 3-数据")
    private Integer permissionType;

    @ApiModelProperty("类型名称")
    private String permissionTypeName;

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

    @ApiModelProperty("子权限列表")
    private List<PermissionTreeVO> children;
}
