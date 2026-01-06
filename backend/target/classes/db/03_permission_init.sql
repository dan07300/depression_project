-- ====================================================================
-- 抑郁症复发预警系统 - 权限数据初始化 
-- 功能权限配置 sys_permission 表数据
-- 可安全重复执行，使用 REPLACE INTO 确保数据一致性
-- ====================================================================

USE drp_system;

-- ====================================================================
-- 一级菜单（顶级权限） ID: 1-5
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(1, 'dashboard', '首页', 1, 0, 1, 'el-icon-s-home', '/dashboard', '系统首页'),
(2, 'profile', '个人信息', 1, 0, 2, 'el-icon-setting', '/profile', '个人信息管理'),
(3, 'system', '系统管理', 1, 0, 10, 'el-icon-s-tools', NULL, '系统管理模块'),
(4, 'hospital_manage', '医院管理', 1, 0, 20, 'el-icon-s-management', NULL, '医院管理模块'),
(5, 'doctor_work', '医生工作台', 1, 0, 30, 'el-icon-s-platform', NULL, '医生工作模块');

-- ====================================================================
-- 二级菜单 - 系统管理模块 (parent_id=3) ID: 6-11
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(6, 'hospital', '医院管理', 1, 3, 1, 'el-icon-office-building', '/hospital', '医院信息管理'),
(7, 'hospital_admin', '医院管理员', 1, 3, 2, 'el-icon-user-solid', '/hospital-admin', '医院管理员管理'),
(8, 'all_doctors', '所有医生', 1, 3, 3, 'el-icon-s-custom', '/all-doctors', '查看所有医生'),
(9, 'all_patients', '所有病人', 1, 3, 4, 'el-icon-user', '/all-patients', '查看所有病人'),
(10, 'system_alert', '预警管理', 1, 3, 5, 'el-icon-warning', '/alert-manage', '系统预警管理'),
(11, 'permission_manage', '权限管理', 1, 3, 6, 'el-icon-lock', '/permission-manage', '系统权限管理');

-- ====================================================================
-- 二级菜单 - 医院管理模块 (parent_id=4) ID: 12-15
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(12, 'doctor', '医生管理', 1, 4, 1, 'el-icon-s-custom', '/doctor-manage', '本院医生管理'),
(13, 'patient_manage', '病人管理', 1, 4, 2, 'el-icon-user', '/user-manage', '本院病人管理'),
(14, 'alert_manage', '预警管理', 1, 4, 3, 'el-icon-warning', '/alert-manage', '本院预警管理'),
(15, 'statistics', '统计分析', 1, 4, 4, 'el-icon-data-analysis', '/statistics', '数据统计分析');

-- ====================================================================
-- 二级菜单 - 医生工作模块 (parent_id=5) ID: 16-18
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(16, 'my_patient', '我的病人', 1, 5, 1, 'el-icon-user', '/patient-list', '我负责的病人'),
(17, 'assessment', '评定管理', 1, 5, 2, 'el-icon-document', '/assessment', '患者评定管理'),
(18, 'doctor_alert', '预警处理', 1, 5, 3, 'el-icon-warning', '/doctor-alert', '预警信息处理');

-- ====================================================================
-- 三级权限 - 医院管理按钮权限 (parent_id=6) ID: 19-22
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(19, 'hospital:view', '查看医院', 2, 6, 1, NULL, NULL, '查看医院信息'),
(20, 'hospital:add', '添加医院', 2, 6, 2, NULL, NULL, '添加新医院'),
(21, 'hospital:edit', '编辑医院', 2, 6, 3, NULL, NULL, '编辑医院信息'),
(22, 'hospital:delete', '删除医院', 2, 6, 4, NULL, NULL, '删除医院');

-- ====================================================================
-- 三级权限 - 医院管理员按钮权限 (parent_id=7) ID: 23-27
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(23, 'hospital_admin:view', '查看管理员', 2, 7, 1, NULL, NULL, '查看医院管理员'),
(24, 'hospital_admin:add', '添加管理员', 2, 7, 2, NULL, NULL, '添加医院管理员'),
(25, 'hospital_admin:edit', '编辑管理员', 2, 7, 3, NULL, NULL, '编辑医院管理员'),
(26, 'hospital_admin:delete', '删除管理员', 2, 7, 4, NULL, NULL, '删除医院管理员'),
(27, 'hospital_admin:permission', '分配权限', 2, 7, 5, NULL, NULL, '分配管理员权限');

-- ====================================================================
-- 三级权限 - 所有医生按钮权限 (parent_id=8) ID: 28-31
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(28, 'all_doctors:view', '查看详情', 2, 8, 1, NULL, NULL, '查看医生详情'),
(29, 'all_doctors:add', '添加医生', 2, 8, 2, NULL, NULL, '添加新医生'),
(30, 'all_doctors:edit', '编辑医生', 2, 8, 3, NULL, NULL, '编辑医生信息'),
(31, 'all_doctors:permission', '分配权限', 2, 8, 4, NULL, NULL, '分配医生权限');

-- ====================================================================
-- 三级权限 - 所有病人按钮权限 (parent_id=9) ID: 32-37
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(32, 'all_patients:view', '查看病人', 2, 9, 1, NULL, NULL, '查看所有病人列表'),
(33, 'all_patients:add', '添加病人', 2, 9, 2, NULL, NULL, '添加新病人'),
(34, 'all_patients:detail', '查看详情', 2, 9, 3, NULL, NULL, '查看病人详情'),
(35, 'all_patients:edit', '编辑病人', 2, 9, 4, NULL, NULL, '编辑病人信息'),
(36, 'all_patients:migrate', '迁移病人', 2, 9, 5, NULL, NULL, '迁移病人到其他医院'),
(37, 'all_patients:delete', '删除病人', 2, 9, 6, NULL, NULL, '删除病人');

-- ====================================================================
-- 三级权限 - 系统预警按钮权限 (parent_id=10) ID: 38-40
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(38, 'system_alert:view', '查看预警', 2, 10, 1, NULL, NULL, '查看系统预警'),
(39, 'system_alert:handle', '处理预警', 2, 10, 2, NULL, NULL, '处理系统预警'),
(40, 'system_alert:export', '导出预警', 2, 10, 3, NULL, NULL, '导出预警数据');

-- ====================================================================
-- 三级权限 - 医生管理按钮权限 (parent_id=12) ID: 41-45
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(41, 'doctor:view', '查看医生', 2, 12, 1, NULL, NULL, '查看医生信息'),
(42, 'doctor:add', '添加医生', 2, 12, 2, NULL, NULL, '添加新医生'),
(43, 'doctor:edit', '编辑医生', 2, 12, 3, NULL, NULL, '编辑医生信息'),
(44, 'doctor:delete', '删除医生', 2, 12, 4, NULL, NULL, '删除医生'),
(45, 'doctor:permission', '分配权限', 2, 12, 5, NULL, NULL, '分配医生权限');

-- ====================================================================
-- 三级权限 - 病人管理按钮权限 (parent_id=13) ID: 46-51
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(46, 'patient_manage:view', '查看病人', 2, 13, 1, NULL, NULL, '查看病人信息'),
(47, 'patient_manage:add', '添加病人', 2, 13, 2, NULL, NULL, '添加新病人'),
(48, 'patient_manage:edit', '编辑病人', 2, 13, 3, NULL, NULL, '编辑病人信息'),
(49, 'patient_manage:delete', '删除病人', 2, 13, 4, NULL, NULL, '删除病人'),
(50, 'patient_manage:assign', '分配医生', 2, 13, 5, NULL, NULL, '给病人分配医生'),
(51, 'patient_manage:export', '导出数据', 2, 13, 6, NULL, NULL, '导出病人数据');

-- ====================================================================
-- 三级权限 - 预警管理按钮权限 (parent_id=14) ID: 52-54
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(52, 'alert_manage:view', '查看预警', 2, 14, 1, NULL, NULL, '查看预警信息'),
(53, 'alert_manage:handle', '处理预警', 2, 14, 2, NULL, NULL, '处理预警信息'),
(54, 'alert_manage:export', '导出预警', 2, 14, 3, NULL, NULL, '导出预警数据');

-- ====================================================================
-- 三级权限 - 统计分析按钮权限 (parent_id=15) ID: 55-56
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(55, 'statistics:view', '查看统计', 2, 15, 1, NULL, NULL, '查看统计数据'),
(56, 'statistics:export', '导出统计', 2, 15, 2, NULL, NULL, '导出统计报表');

-- ====================================================================
-- 三级权限 - 我的病人按钮权限 (parent_id=16) ID: 57-62
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(57, 'my_patient:view', '查看病人', 2, 16, 1, NULL, NULL, '查看我的病人列表'),
(58, 'my_patient:detail', '查看详情', 2, 16, 2, NULL, NULL, '查看病人详细信息'),
(59, 'my_patient:edit', '编辑病人', 2, 16, 3, NULL, NULL, '编辑病人信息'),
(60, 'my_patient:enroll', '入组操作', 2, 16, 4, NULL, NULL, '病人入组操作'),
(61, 'my_patient:exclude', '排除操作', 2, 16, 5, NULL, NULL, '病人排除操作'),
(62, 'my_patient:relapse', '复发记录', 2, 16, 6, NULL, NULL, '记录病人复发');

-- ====================================================================
-- 三级权限 - 评定管理按钮权限 (parent_id=17) ID: 63-66
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(63, 'assessment:view', '查看评定', 2, 17, 1, NULL, NULL, '查看评定记录'),
(64, 'assessment:add', '添加评定', 2, 17, 2, NULL, NULL, '添加评定记录'),
(65, 'assessment:edit', '编辑评定', 2, 17, 3, NULL, NULL, '编辑评定记录'),
(66, 'assessment:delete', '删除评定', 2, 17, 4, NULL, NULL, '删除评定记录');

-- ====================================================================
-- 三级权限 - 预警处理按钮权限 (parent_id=18) ID: 67-68
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(67, 'doctor_alert:view', '查看预警', 2, 18, 1, NULL, NULL, '查看我的预警'),
(68, 'doctor_alert:handle', '处理预警', 2, 18, 2, NULL, NULL, '处理预警信息');

-- ====================================================================
-- 数据权限类型 ID: 69-71
-- ====================================================================
REPLACE INTO sys_permission (id, permission_code, permission_name, permission_type, parent_id, sort_order, icon, path, description) VALUES
(69, 'data_scope:all', '全部数据', 3, 0, 100, NULL, NULL, '可访问系统全部数据'),
(70, 'data_scope:hospital', '本院数据', 3, 0, 101, NULL, NULL, '可访问本医院数据'),
(71, 'data_scope:self', '个人数据', 3, 0, 102, NULL, NULL, '只能访问个人负责的数据');

-- ====================================================================
-- 验证数据
-- ====================================================================
SELECT '权限数据导入完成' AS message;
SELECT 
    (SELECT COUNT(*) FROM sys_permission WHERE permission_type = 1) AS '菜单权限数',
    (SELECT COUNT(*) FROM sys_permission WHERE permission_type = 2) AS '按钮权限数',
    (SELECT COUNT(*) FROM sys_permission WHERE permission_type = 3) AS '数据权限数',
    (SELECT COUNT(*) FROM sys_permission) AS '总权限数';

-- 显示权限树结构
SELECT 
    id,
    CASE 
        WHEN parent_id = 0 THEN permission_name
        WHEN parent_id <= 5 THEN CONCAT('  ├─ ', permission_name)
        ELSE CONCAT('    ├─ ', permission_name)
    END AS '权限结构',
    permission_code AS '权限编码',
    CASE permission_type
        WHEN 1 THEN '菜单'
        WHEN 2 THEN '按钮'
        WHEN 3 THEN '数据'
    END AS '类型'
FROM sys_permission
ORDER BY 
    CASE 
        WHEN parent_id = 0 THEN id * 1000
        WHEN parent_id <= 5 THEN parent_id * 1000 + id
        ELSE (SELECT p.parent_id FROM sys_permission p WHERE p.id = sys_permission.parent_id) * 1000 + parent_id * 10 + sort_order
    END;
