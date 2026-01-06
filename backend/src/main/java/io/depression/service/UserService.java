package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.dto.UserCreateDTO;
import io.depression.dto.UserPermissionDTO;
import io.depression.dto.UserQueryDTO;
import io.depression.dto.UserUpdateDTO;
import io.depression.entity.CohortAuthorization;
import io.depression.vo.PageVO;
import io.depression.vo.UserVO;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author system
 * @since 2025-01-01
 */
public interface UserService extends IService<CohortAuthorization> {

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageVO<UserVO> pageUsers(UserQueryDTO queryDTO);

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long id);

    /**
     * 创建用户
     *
     * @param dto 创建DTO
     * @return 用户VO
     */
    UserVO createUser(UserCreateDTO dto);

    /**
     * 更新用户
     *
     * @param dto 更新DTO
     * @return 用户VO
     */
    UserVO updateUser(UserUpdateDTO dto);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 分配权限
     *
     * @param dto 权限分配DTO
     * @return 是否成功
     */
    boolean assignPermissions(UserPermissionDTO dto);

    /**
     * 获取用户列表（不包括课题和单位负责人）
     *
     * @param cohortCode 医院编码（可选）
     * @param roleType 角色类型（可选）
     * @return 用户列表
     */
    List<UserVO> listUsers(String cohortCode, Integer roleType);

    /**
     * 更新个人信息
     *
     * @param userId 用户ID
     * @param dto 更新DTO
     * @return 用户VO
     */
    UserVO updateProfile(Long userId, io.depression.dto.ProfileUpdateDTO dto);
}



