package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.depression.dto.ProfileUpdateDTO;
import io.depression.dto.UserCreateDTO;
import io.depression.dto.UserPermissionDTO;
import io.depression.dto.UserQueryDTO;
import io.depression.dto.UserUpdateDTO;
import io.depression.entity.CohortAuthorization;
import io.depression.entity.CohortCenter;
import io.depression.mapper.CohortAuthorizationMapper;
import io.depression.mapper.CohortCenterMapper;
import io.depression.service.UserService;
import io.depression.vo.PageVO;
import io.depression.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author system
 * @since 2025-01-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<CohortAuthorizationMapper, CohortAuthorization> implements UserService {

    private final CohortCenterMapper cohortCenterMapper;

    public UserServiceImpl(CohortCenterMapper cohortCenterMapper) {
        this.cohortCenterMapper = cohortCenterMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(UserCreateDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<CohortAuthorization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CohortAuthorization::getUsername, dto.getUsername());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在：" + dto.getUsername());
        }

        CohortAuthorization user = new CohortAuthorization();
        BeanUtils.copyProperties(dto, user);
        String defaultPassword;
        if (StringUtils.hasText(dto.getPassword())) {
            defaultPassword = dto.getPassword();
        } else if (StringUtils.hasText(dto.getPhone()) && dto.getPhone().length() >= 6) {
            defaultPassword = dto.getPhone().substring(dto.getPhone().length() - 6);
        } else {
            defaultPassword = "123456";
        }
        user.setPassword(BCrypt.hashpw(defaultPassword, BCrypt.gensalt()));
        user.setStatus(1);
        this.save(user);

        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        CohortAuthorization user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getRoleType() != null && user.getRoleType() == 1) {
            throw new RuntimeException("不能删除系统管理员账号");
        }
        return this.removeById(id);
    }

    @Override
    public PageVO<UserVO> pageUsers(UserQueryDTO queryDTO) {
        Page<CohortAuthorization> page = new Page<>(queryDTO.getCurrent().longValue(), queryDTO.getSize().longValue());
        LambdaQueryWrapper<CohortAuthorization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(queryDTO.getCohortCode()), CohortAuthorization::getCohortCode, queryDTO.getCohortCode())
                .eq(queryDTO.getRoleType() != null, CohortAuthorization::getRoleType, queryDTO.getRoleType())
                .eq(queryDTO.getStatus() != null, CohortAuthorization::getStatus, queryDTO.getStatus())
                .and(StringUtils.hasText(queryDTO.getKeyword()), w -> w
                        .like(CohortAuthorization::getUsername, queryDTO.getKeyword())
                        .or()
                        .like(CohortAuthorization::getRealName, queryDTO.getKeyword())
                        .or()
                        .like(CohortAuthorization::getPhone, queryDTO.getKeyword()))
                .orderByDesc(CohortAuthorization::getCreateTime);
        this.page(page, wrapper);

        List<UserVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), voList);
    }

    @Override
    public UserVO getUserById(Long id) {
        CohortAuthorization user = this.getById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(UserUpdateDTO dto) {
        CohortAuthorization user = this.getById(dto.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        BeanUtils.copyProperties(dto, user, getNullPropertyNames(dto));
        this.updateById(user);

        return getUserById(user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(UserPermissionDTO dto) {
        CohortAuthorization user = this.getById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String permissionsJson = CollectionUtils.isEmpty(dto.getPermissionIds())
                    ? "[]"
                    : objectMapper.writeValueAsString(dto.getPermissionIds());
            user.setPermissions(permissionsJson);
            this.updateById(user);
            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("权限数据格式错误", e);
        }
    }

    @Override
    public List<UserVO> listUsers(String cohortCode, Integer roleType) {
        LambdaQueryWrapper<CohortAuthorization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(cohortCode), CohortAuthorization::getCohortCode, cohortCode)
                .eq(roleType != null, CohortAuthorization::getRoleType, roleType)
                .eq(CohortAuthorization::getDeleted, 0);

        List<CohortAuthorization> users = this.list(wrapper);
        return users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private UserVO convertToVO(CohortAuthorization user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        if (StringUtils.hasText(user.getCohortCode())) {
            CohortCenter center = cohortCenterMapper.selectById(user.getCohortCode());
            if (center != null) {
                vo.setCohortName(center.getCohortName());
            }
        }
        return vo;
    }

    private String[] getNullPropertyNames(UserUpdateDTO source) {
        java.beans.BeanInfo beanInfo;
        try {
            beanInfo = java.beans.Introspector.getBeanInfo(UserUpdateDTO.class, Object.class);
        } catch (java.beans.IntrospectionException e) {
            throw new RuntimeException("拷贝属性失败", e);
        }
        List<String> nullProps = new java.util.ArrayList<>();
        for (java.beans.PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            try {
                Object value = pd.getReadMethod().invoke(source);
                if (value == null) {
                    nullProps.add(pd.getName());
                }
            } catch (Exception ignored) {
            }
        }
        return nullProps.toArray(new String[0]);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateProfile(Long userId, ProfileUpdateDTO dto) {
        CohortAuthorization user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (dto.getRealName() != null) {
            user.setRealName(dto.getRealName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }

        this.updateById(user);
        return getUserById(user.getId());
    }
}



