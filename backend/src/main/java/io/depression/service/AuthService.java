package io.depression.service;

import io.depression.dto.LoginDTO;
import io.depression.dto.PasswordChangeDTO;
import io.depression.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param dto 密码修改信息
     * @return 是否成功
     */
    boolean changePassword(Long userId, PasswordChangeDTO dto);
}








