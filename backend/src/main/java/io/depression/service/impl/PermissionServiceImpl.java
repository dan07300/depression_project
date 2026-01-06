package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.depression.entity.SysPermission;
import io.depression.mapper.SysPermissionMapper;
import io.depression.service.PermissionService;
import io.depression.vo.PermissionTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements PermissionService {

    @Override
    public List<PermissionTreeVO> getPermissionTree() {
        List<SysPermission> allPermissions = this.list(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getStatus, 1)
                .orderByAsc(SysPermission::getSortOrder));

        if (CollectionUtils.isEmpty(allPermissions)) {
            return new ArrayList<>();
        }

        // 转换为VO
        List<PermissionTreeVO> voList = allPermissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 构建树结构
        Map<Long, List<PermissionTreeVO>> childrenMap = voList.stream()
                .filter(vo -> vo.getParentId() != null && vo.getParentId() > 0)
                .collect(Collectors.groupingBy(PermissionTreeVO::getParentId));

        // 设置子节点
        voList.forEach(vo -> {
            List<PermissionTreeVO> children = childrenMap.get(vo.getId());
            vo.setChildren(children != null ? children : new ArrayList<>());
        });

        // 返回根节点（parentId为0或null）
        return voList.stream()
                .filter(vo -> vo.getParentId() == null || vo.getParentId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionTreeVO getPermissionById(Long id) {
        SysPermission permission = this.getById(id);
        if (permission == null) {
            return null;
        }
        return convertToVO(permission);
    }

    private PermissionTreeVO convertToVO(SysPermission permission) {
        PermissionTreeVO vo = new PermissionTreeVO();
        BeanUtils.copyProperties(permission, vo);

        // 设置类型名称
        if (permission.getPermissionType() != null) {
            switch (permission.getPermissionType()) {
                case 1:
                    vo.setPermissionTypeName("菜单");
                    break;
                case 2:
                    vo.setPermissionTypeName("按钮");
                    break;
                case 3:
                    vo.setPermissionTypeName("数据");
                    break;
                default:
                    vo.setPermissionTypeName("未知");
            }
        }

        return vo;
    }
}
