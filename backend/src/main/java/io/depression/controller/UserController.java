package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.UserCreateDTO;
import io.depression.dto.UserPermissionDTO;
import io.depression.dto.UserQueryDTO;
import io.depression.dto.UserUpdateDTO;
import io.depression.service.UserService;
import io.depression.vo.PageVO;
import io.depression.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author system
 * @since 2025-01-01
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询用户列表", notes = "根据条件分页查询用户列表")
    @GetMapping
    public Result<PageVO<UserVO>> page(UserQueryDTO queryDTO) {
        PageVO<UserVO> page = userService.pageUsers(queryDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "获取用户列表（不分页）", notes = "根据医院或角色筛选用户")
    @GetMapping("/list")
    public Result<List<UserVO>> list(@RequestParam(required = false) String cohortCode,
                                     @RequestParam(required = false) Integer roleType) {
        List<UserVO> users = userService.listUsers(cohortCode, roleType);
        return Result.success(users);
    }

    @ApiOperation(value = "获取用户详情", notes = "根据ID获取用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @ApiOperation(value = "创建用户", notes = "创建新的管理员或医生账号")
    @PostMapping
    public Result<UserVO> create(@Valid @RequestBody UserCreateDTO dto) {
        try {
            UserVO user = userService.createUser(dto);
            return Result.success("创建成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    @PutMapping("/{id}")
    public Result<UserVO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        try {
            dto.setId(id);
            UserVO user = userService.updateUser(dto);
            return Result.success("更新成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除用户", notes = "根据ID删除用户（不能删除系统管理员）")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = userService.deleteUser(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "分配权限", notes = "为用户分配权限")
    @PostMapping("/permissions")
    public Result<String> assignPermissions(@Valid @RequestBody UserPermissionDTO dto) {
        try {
            boolean success = userService.assignPermissions(dto);
            if (success) {
                return Result.success("权限分配成功");
            } else {
                return Result.error("权限分配失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "更新个人信息", notes = "更新当前用户的个人信息")
    @PutMapping("/profile/{userId}")
    public Result<UserVO> updateProfile(@PathVariable Long userId, @Valid @RequestBody io.depression.dto.ProfileUpdateDTO dto) {
        try {
            UserVO user = userService.updateProfile(userId, dto);
            return Result.success("更新成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}



