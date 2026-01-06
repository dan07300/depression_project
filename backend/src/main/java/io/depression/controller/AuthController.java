package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.LoginDTO;
import io.depression.dto.PasswordChangeDTO;
import io.depression.service.AuthService;
import io.depression.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = authService.login(loginDTO);
            return Result.success("登录成功", loginVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "修改密码", notes = "修改当前用户密码")
    @PostMapping("/password")
    public Result<String> changePassword(@RequestParam Long userId, @Valid @RequestBody PasswordChangeDTO dto) {
        try {
            boolean success = authService.changePassword(userId, dto);
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}








