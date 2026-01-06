package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.entity.SysPermission;
import io.depression.vo.PermissionTreeVO;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends IService<SysPermission> {

    /**
     * 获取权限树
     *
     * @return 权限树列表
     */
    List<PermissionTreeVO> getPermissionTree();

    /**
     * 根据ID获取权限详情
     *
     * @param id 权限ID
     * @return 权限VO
     */
    PermissionTreeVO getPermissionById(Long id);
}
