package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.depression.dto.LoginDTO;
import io.depression.dto.PasswordChangeDTO;
import io.depression.entity.CohortAuthorization;
import io.depression.entity.CohortCenter;
import io.depression.mapper.CohortAuthorizationMapper;
import io.depression.mapper.CohortCenterMapper;
import io.depression.service.AuthService;
import io.depression.vo.LoginVO;
import io.depression.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final CohortAuthorizationMapper authorizationMapper;
    private final CohortCenterMapper cohortCenterMapper;

    public AuthServiceImpl(CohortAuthorizationMapper authorizationMapper,
                           CohortCenterMapper cohortCenterMapper) {
        this.authorizationMapper = authorizationMapper;
        this.cohortCenterMapper = cohortCenterMapper;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<CohortAuthorization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CohortAuthorization::getUsername, loginDTO.getUsername())
                .eq(CohortAuthorization::getStatus, 1)
                .eq(CohortAuthorization::getDeleted, 0);
        CohortAuthorization user = authorizationMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 更新最后登录时间
        user.setUpdateTime(LocalDateTime.now());
        authorizationMapper.updateById(user);

        // 生成Token（简化处理，实际应该使用JWT）
        String token = generateToken(user);

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        UserVO userVO = new UserVO();
        convertUser(user, userVO);
        loginVO.setUserInfo(userVO);

        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, PasswordChangeDTO dto) {
        CohortAuthorization user = authorizationMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!BCrypt.checkpw(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新新密码
        user.setPassword(BCrypt.hashpw(dto.getNewPassword(), BCrypt.gensalt()));
        authorizationMapper.updateById(user);

        return true;
    }

    /**
     * 生成Token（简化版，实际应该使用JWT）
     */
    private String generateToken(CohortAuthorization user) {
        // TODO: 使用JWT生成token
        return "Bearer_" + user.getId() + "_" + System.currentTimeMillis();
    }

    private void convertUser(CohortAuthorization source, UserVO target) {
        BeanUtils.copyProperties(source, target);
        if (StringUtils.hasText(source.getCohortCode())) {
            CohortCenter center = cohortCenterMapper.selectById(source.getCohortCode());
            if (center != null) {
                target.setCohortName(center.getCohortName());
            }
        }
    }
}



