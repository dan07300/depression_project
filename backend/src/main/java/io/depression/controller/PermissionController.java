package io.depression.controller;

import io.depression.common.Result;
import io.depression.service.PermissionService;
import io.depression.vo.PermissionTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取权限树", notes = "获取所有权限的树形结构")
    @GetMapping("/tree")
    public Result<List<PermissionTreeVO>> getPermissionTree() {
        List<PermissionTreeVO> tree = permissionService.getPermissionTree();
        return Result.success(tree);
    }

    @ApiOperation(value = "获取权限详情", notes = "根据ID获取权限详情")
    @GetMapping("/{id}")
    public Result<PermissionTreeVO> getById(@PathVariable Long id) {
        PermissionTreeVO permission = permissionService.getPermissionById(id);
        if (permission == null) {
            return Result.error("权限不存在");
        }
        return Result.success(permission);
    }
}
