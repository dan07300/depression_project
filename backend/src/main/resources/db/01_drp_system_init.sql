-- ====================================================================
-- 抑郁症复发预警系统 
-- 完整SQL代码 - 符合数据字典规范
-- ====================================================================

DROP DATABASE IF EXISTS drp_system;
CREATE DATABASE drp_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE drp_system;

-- ====================================================================
-- 1. 医院表
-- ====================================================================
CREATE TABLE `cohort_centers` (
    `cohort_name` VARCHAR(100) NOT NULL COMMENT '医院名称',
    `cohort_code` VARCHAR(50) NOT NULL COMMENT '医院编码',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`cohort_code`),
    UNIQUE KEY `uk_cohort_code` (`cohort_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院表';

-- ====================================================================
-- 2. 功能权限配置表
-- ====================================================================
CREATE TABLE `sys_permission` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `permission_type` TINYINT(1) DEFAULT 1 COMMENT '类型：1-菜单 2-按钮 3-数据',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父权限ID',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能权限配置表';

-- ====================================================================
-- 3. 用户表（管理员/医生，含权限配置）
-- ====================================================================
CREATE TABLE `cohort_authorization` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `gender` TINYINT(1) DEFAULT NULL COMMENT '性别：0-女 1-男',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `role_type` TINYINT(1) NOT NULL COMMENT '角色：1-系统管理员 2-医院管理员 3-医生',
    `cohort_code` VARCHAR(50) DEFAULT NULL COMMENT '所属医院编码',
    `department` VARCHAR(100) DEFAULT NULL COMMENT '科室',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `permissions` JSON DEFAULT NULL COMMENT '权限列表JSON',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_cohort_code` (`cohort_code`),
    KEY `idx_role_type` (`role_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表（管理员/医生）';

-- ====================================================================
-- 4. 患者表（完整信息 + 账号密码 + 扩展字段）
-- ====================================================================
CREATE TABLE `patients` (
    `id_card` VARCHAR(20) NOT NULL COMMENT '身份证号',
    `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '登录密码',
    `patient_code` VARCHAR(50) DEFAULT NULL COMMENT '患者编号',
    `patient_name` VARCHAR(50) NOT NULL COMMENT '患者姓名',
    `gender` TINYINT(1) DEFAULT NULL COMMENT '性别：0-女 1-男',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '住址',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像文件路径',
    `ethnicity` VARCHAR(20) DEFAULT NULL COMMENT '民族',
    `height` DECIMAL(5,2) DEFAULT NULL COMMENT '身高(CM)',
    `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '体重(KG)',
    `handedness` TINYINT(1) DEFAULT NULL COMMENT '利手：0-左手 1-右手',
    `birth_place` VARCHAR(200) DEFAULT NULL COMMENT '籍贯',
    `work_place` VARCHAR(200) DEFAULT NULL COMMENT '工作/学习地',
    `residence_type` TINYINT(1) DEFAULT NULL COMMENT '长居地类型：1-城市 2-城镇 3-农村',
    `education` VARCHAR(50) DEFAULT NULL COMMENT '学历',
    `occupation` VARCHAR(50) DEFAULT NULL COMMENT '职业',
    `marital_status` TINYINT(1) DEFAULT NULL COMMENT '婚姻状态',
    `emotional_status` TINYINT(1) DEFAULT NULL COMMENT '情感状态',
    `childhood_separation` TINYINT(1) DEFAULT NULL COMMENT '童年是否与父母分离',
    `family_member_count` INT(11) DEFAULT NULL COMMENT '家庭成员人数',
    `family_working_count` INT(11) DEFAULT NULL COMMENT '家庭成员在职人数',
    `family_annual_income` DECIMAL(10,2) DEFAULT NULL COMMENT '家庭年收入(万元)',
    `personal_income` DECIMAL(10,2) DEFAULT NULL COMMENT '个人收入(元/月)',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话',
    `emergency_relation` VARCHAR(20) DEFAULT NULL COMMENT '与患者关系',
    `cohort_code` VARCHAR(50) NOT NULL COMMENT '所属医院编码',
    `doctor_id` BIGINT(20) DEFAULT NULL COMMENT '负责医生ID',
    `diagnosis` VARCHAR(200) DEFAULT NULL COMMENT '诊断',
    `diagnosis_date` DATE DEFAULT NULL COMMENT '确诊日期',
    `disease_course` INT(11) DEFAULT NULL COMMENT '病程（月）',
    `episode_count` INT(11) DEFAULT 1 COMMENT '发作次数',
    `is_hospitalized` TINYINT(1) DEFAULT 0 COMMENT '是否住院病人',
    `is_first_episode` TINYINT(1) DEFAULT 1 COMMENT '是否首发',
    `signed_consent` TINYINT(1) DEFAULT 0 COMMENT '签署知情同意书',
    `medical_history` TEXT DEFAULT NULL COMMENT '既往病史',
    `family_history` TEXT DEFAULT NULL COMMENT '家族史',
    `allergy_history` TEXT DEFAULT NULL COMMENT '过敏史',
    `current_medication` TEXT DEFAULT NULL COMMENT '当前用药',
    `enroll_date` DATE DEFAULT NULL COMMENT '入组时间',
    `enrollment_criteria` JSON DEFAULT NULL COMMENT '入组/排除标准JSON',
    `is_first_screening` TINYINT(1) DEFAULT 1 COMMENT '是否首次筛查',
    `group_name` VARCHAR(100) DEFAULT NULL COMMENT '分组名称',
    `is_relapsed` TINYINT(1) DEFAULT 0 COMMENT '是否复发',
    `relapse_date` DATE DEFAULT NULL COMMENT '复发时间',
    `relapse_count` INT(11) DEFAULT 0 COMMENT '复发次数',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-已排除 1-正常 2-已出组 3-失访 4-康复',
    `exclude_reason` TEXT DEFAULT NULL COMMENT '排除原因',
    `notes` TEXT DEFAULT NULL COMMENT '备注',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id_card`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_patient_code` (`patient_code`),
    KEY `idx_cohort_code` (`cohort_code`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者表';

-- ====================================================================
-- 5. 患者评定记录表
-- ====================================================================
CREATE TABLE `patient_assessments` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `id_card` VARCHAR(20) NOT NULL COMMENT '患者身份证号',
    `cohort_code` VARCHAR(50) NOT NULL COMMENT '所属医院编码',
    `assessment_type` VARCHAR(50) NOT NULL COMMENT '评定类型',
    `assessment_name` VARCHAR(100) DEFAULT NULL COMMENT '评定名称',
    `time_point` VARCHAR(50) NOT NULL COMMENT '时间点',
    `status` TINYINT(1) DEFAULT 0 COMMENT '状态：-1=不适用 0=未完成 1=已完成',
    `score` DECIMAL(10,2) DEFAULT NULL COMMENT '评分',
    `assess_date` DATE DEFAULT NULL COMMENT '评定日期',
    `assessor_id` BIGINT(20) DEFAULT NULL COMMENT '评定员ID',
    `notes` TEXT DEFAULT NULL COMMENT '备注',
    `detail_data` JSON DEFAULT NULL COMMENT '详细数据JSON',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_patient_assess_time` (`id_card`, `assessment_type`, `time_point`),
    KEY `idx_id_card` (`id_card`),
    KEY `idx_cohort_code` (`cohort_code`),
    KEY `idx_assessment_type` (`assessment_type`),
    KEY `idx_time_point` (`time_point`),
    KEY `idx_assess_date` (`assess_date`),
    KEY `idx_assessor_id` (`assessor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者评定记录表';

-- ====================================================================
-- 6. 评定项目配置表
-- ====================================================================
CREATE TABLE `assessment_config` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `assessment_type` VARCHAR(50) NOT NULL COMMENT '评定类型标识',
    `assessment_name` VARCHAR(100) NOT NULL COMMENT '评定名称',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `is_scored` TINYINT(1) DEFAULT 1 COMMENT '是否有评分',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT(1) DEFAULT 1,
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_assessment_type` (`assessment_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评定项目配置表';

-- ====================================================================
-- 7. 字典类型表
-- ====================================================================
CREATE TABLE `sys_dict_type` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典类型名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ====================================================================
-- 8. 字典数据表
-- ====================================================================
CREATE TABLE `sys_dict_data` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典值',
    `dict_sort` INT(11) DEFAULT 0 COMMENT '排序',
    `css_class` VARCHAR(100) DEFAULT NULL COMMENT 'CSS样式类',
    `tag_type` VARCHAR(50) DEFAULT NULL COMMENT '标签类型',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态',
    `deleted` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

DROP TABLE IF EXISTS `warning_records`;
CREATE TABLE `warning_records` (
                                   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID（患者ID）',
                                   `id_card` VARCHAR(20) DEFAULT NULL COMMENT '患者身份证号',
                                   `warning_level` TINYINT(1) DEFAULT NULL COMMENT '预警等级：1-低风险 2-中风险 3-高风险',
                                   `risk_score` DECIMAL(10,2) DEFAULT NULL COMMENT '风险评分',
                                   `warning_reason` TEXT DEFAULT NULL COMMENT '预警原因',
                                   `suggestion` TEXT DEFAULT NULL COMMENT '建议',
                                   `process_status` TINYINT(1) DEFAULT 0 COMMENT '处理状态：0-未处理 1-处理中 2-已处理',
                                   `processor_id` BIGINT(20) DEFAULT NULL COMMENT '处理人ID',
                                   `process_time` DATETIME DEFAULT NULL COMMENT '处理时间',
                                   `process_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
                                   `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                   `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_user_id` (`user_id`),
                                   KEY `idx_id_card` (`id_card`),
                                   KEY `idx_process_status` (`process_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- ====================================================================
-- 初始数据插入
-- ====================================================================

-- 医院数据
INSERT INTO `cohort_centers` (`cohort_name`, `cohort_code`, `address`, `contact_person`, `contact_phone`, `description`) VALUES
('武汉第一医院', 'WH001', '武汉市江汉区胜利街108号', '张主任', '13800138001', '三甲综合医院，精神科重点专科'),
('武汉第二医院', 'WH002', '武汉市武昌区中山路123号', '李主任', '13800138002', '三甲综合医院，心理卫生中心'),
('北京协和医院', 'BJ001', '北京市东城区帅府园1号', '王主任', '13800138003', '国家级重点医院，精神心理科'),
('上海华山医院', 'SH001', '上海市静安区乌鲁木齐中路12号', '陈主任', '13800138004', '三甲医院，神经精神科');

-- 系统用户（管理员/医生）
INSERT INTO `cohort_authorization` (`username`, `password`, `real_name`, `phone`, `email`, `gender`, `birthday`, `role_type`, `cohort_code`, `department`, `title`, `permissions`) VALUES
('admin', 'admin123', '系统管理员', '13800000000', 'admin@drp.com', 1, '1985-01-15', 1, NULL, NULL, NULL, 
 '["dashboard","profile","system","hospital","hospital:view","hospital:add","hospital:edit","hospital:delete","hospital_admin","hospital_admin:view","hospital_admin:add","hospital_admin:edit","hospital_admin:delete","hospital_admin:permission","data_scope:all"]'),
('hospital_admin1', 'hospital123', '张院长', '13800000001', 'zhang@wh001.com', 1, '1978-05-20', 2, 'WH001', '院办', '院长',
 '["dashboard","profile","hospital_manage","doctor","doctor:view","doctor:add","doctor:edit","doctor:delete","doctor:permission","patient_manage","patient_manage:view","patient_manage:add","patient_manage:edit","patient_manage:delete","patient_manage:assign","patient_manage:export","alert_manage","alert_manage:view","alert_manage:handle","statistics","statistics:view","statistics:export","data_scope:hospital"]'),
('hospital_admin2', 'hospital123', '李院长', '13800000002', 'li@wh002.com', 0, '1980-08-12', 2, 'WH002', '院办', '院长',
 '["dashboard","profile","hospital_manage","doctor","doctor:view","doctor:add","doctor:edit","doctor:delete","doctor:permission","patient_manage","patient_manage:view","patient_manage:add","patient_manage:edit","patient_manage:delete","patient_manage:assign","patient_manage:export","alert_manage","alert_manage:view","alert_manage:handle","statistics","statistics:view","statistics:export","data_scope:hospital"]'),
('doctor001', 'doctor123', '王医生', '13900000011', 'wang@wh001.com', 1, '1988-03-10', 3, 'WH001', '精神科', '主治医师',
 '["dashboard","profile","doctor_work","my_patient","my_patient:view","my_patient:detail","my_patient:edit","my_patient:enroll","my_patient:exclude","my_patient:relapse","assessment","assessment:view","assessment:add","assessment:edit","doctor_alert","doctor_alert:view","doctor_alert:handle","data_scope:self"]'),
('doctor002', 'doctor123', '刘医生', '13900000012', 'liu@wh001.com', 0, '1995-11-25', 3, 'WH001', '精神科', '住院医师',
 '["dashboard","profile","doctor_work","my_patient","my_patient:view","my_patient:detail","assessment","assessment:view","assessment:add","doctor_alert","doctor_alert:view","data_scope:self"]'),
('doctor003', 'doctor123', '陈医生', '13900000013', 'chen@wh002.com', 1, '1983-07-18', 3, 'WH002', '心理科', '副主任医师',
 '["dashboard","profile","doctor_work","my_patient","my_patient:view","my_patient:detail","my_patient:edit","my_patient:enroll","my_patient:exclude","my_patient:relapse","assessment","assessment:view","assessment:add","assessment:edit","assessment:delete","doctor_alert","doctor_alert:view","doctor_alert:handle","data_scope:self"]');

-- 患者数据
INSERT INTO `patients` (
    `id_card`, `username`, `password`, `patient_code`, `patient_name`, `gender`, `birthday`, 
    `phone`, `email`, `address`, `ethnicity`, `height`, `weight`, `handedness`,
    `birth_place`, `work_place`, `residence_type`, `education`, `occupation`, 
    `marital_status`, `emotional_status`, `childhood_separation`, 
    `family_member_count`, `family_working_count`, `family_annual_income`, `personal_income`,
    `emergency_contact`, `emergency_phone`, `emergency_relation`,
    `cohort_code`, `doctor_id`, `diagnosis`, `diagnosis_date`, `disease_course`, 
    `episode_count`, `is_hospitalized`, `is_first_episode`, `signed_consent`,
    `medical_history`, `family_history`, `allergy_history`, `current_medication`,
    `enroll_date`, `is_first_screening`, `group_name`, `is_relapsed`, `relapse_count`, `status` 
) VALUES
('420102199705150011', 'patient001', 'patient123', 'WH_D3714', '王明', 1, '1997-05-15', 
 '13900000001', 'wangming@test.com', '武汉市江岸区解放大道100号', '汉族', 175.00, 70.00, 1,
 '湖北省武汉市', '武汉市某科技公司', 1, '3', '6', 
 0, 0, 0, 3, 2, 25.00, 15000.00, '王父', '13900000101', '父亲',
 'WH001', 4, '抑郁症', '2025-10-01', 2, 1, 0, 1, 1,
 '无重大疾病史', '无精神疾病家族史', '无', '舍曲林 50mg/日',
 '2025-11-20', 1, '干预组', 0, 0, 1),

('420102200008200022', 'patient002', 'patient123', 'WH_D3713', '李红', 0, '2000-08-20', 
 '13900000002', 'lihong@test.com', '武汉市武昌区中南路50号', '汉族', 162.00, 55.00, 1,
 '湖北省武汉市', '武汉市某中学', 1, '4', '6', 
 0, 1, 1, 4, 3, 30.00, 12000.00, '李母', '13900000102', '母亲',
 'WH001', 4, '抑郁症', '2025-09-15', 3, 1, 0, 1, 1,
 '甲状腺功能减退', '母亲有抑郁病史', '青霉素过敏', '文拉法辛 75mg/日',
 '2025-11-19', 1, '对照组', 0, 0, 1),

('420102199503100033', 'patient003', 'patient123', 'WH_D3711', '张强', 1, '1995-03-10', 
 '13900000003', 'zhangqiang@test.com', '武汉市洪山区光谷大道200号', '汉族', 178.00, 75.00, 1,
 '湖北省黄石市', '某外企销售部', 1, '3', '8', 
 1, 3, 0, 2, 2, 40.00, 20000.00, '张妻', '13900000103', '配偶',
 'WH001', 5, '抑郁症', '2025-08-20', 6, 2, 0, 0, 1,
 '高血压', '无', '无', '度洛西汀 60mg/日',
 '2025-11-17', 1, '干预组', 1, 1, 1),

('420102199011250044', 'patient004', 'patient123', 'WH_D3710', '赵丽', 0, '1990-11-25', 
 '13900000004', 'zhaoli@test.com', '武汉市汉阳区钟家村88号', '汉族', 165.00, 58.00, 1,
 '湖北省孝感市', '某会计师事务所', 1, '2', '7', 
 1, 3, 0, 3, 2, 35.00, 10000.00, '赵夫', '13900000104', '配偶',
 'WH002', 6, '抑郁症', '2025-07-10', 12, 3, 1, 0, 1,
 '糖尿病', '姐姐有焦虑症', '海鲜过敏', '艾司西酞普兰 10mg/日',
 '2025-11-15', 1, '干预组', 1, 2, 1),

('420102198306080055', 'patient005', 'patient123', 'WH_D3709', '孙伟', 1, '1983-06-08', 
 '13900000005', 'sunwei@test.com', '武汉市青山区和平大道300号', '汉族', 172.00, 80.00, 1,
 '湖北省鄂州市', '某制造企业', 2, '2', '9', 
 2, 6, 1, 4, 2, 15.00, 8000.00, '孙姐', '13900000105', '姐姐',
 'WH002', 6, '抑郁症', '2025-06-01', 24, 4, 0, 0, 1,
 '无', '父亲有酗酒史', '无', '米氮平 30mg/晚',
 '2025-11-10', 1, '对照组', 1, 3, 1),

('420106200311108448', 'patient006', 'patient123', 'WH_D3715', '王佩琦', 0, '2003-11-10', 
 '13397187925', 'wangpeiqi@test.com', '湖北省武汉市武昌区', '汉族', 163.00, 49.00, 1,
 '湖北省武汉市武昌区', '湖北省武汉市武昌区', 1, '3', '1', 
 0, 0, 0, 3, 1, 12.00, 0.00, '王佩琦', '13397187925', '本人',
 'WH001', 4, '抑郁症', '2025-11-01', 1, 1, 0, 1, 1,
 '无', '无', '无', '未用药', NOW(), 1, '干预组', 0, 0, 1);

-- 评定项目配置
INSERT INTO `assessment_config` (`assessment_type`, `assessment_name`, `category`, `is_scored`, `sort_order`) VALUES
('informed_consent', '签署知情同意书（S/V）', '基础评定', 0, 1),
('inclusion_exclusion', '入组/排除标准', '基础评定', 0, 2),
('general_info', '一般信息', '基础评定', 0, 3),
('followup_window', '随访时间窗', '基础评定', 0, 4),
('followup_completion', '随访完成情况', '基础评定', 0, 5),
('hamd', '汉密尔顿抑郁量表', '医生评定', 1, 10),
('hama', '汉密尔顿焦虑量表', '医生评定', 1, 11),
('psf', '个体和社会功能量表', '医生评定', 1, 12),
('dsst', 'DSST评分', '医生评定', 1, 13),
('phq9', '抑郁症筛查量表', '患者自评', 1, 20),
('gad7', '广泛性焦虑障碍量表', '患者自评', 1, 21),
('phq15', '躯体症状量表', '患者自评', 1, 22),
('shaps', '快乐体验量表', '患者自评', 1, 23),
('ctq', '儿童期创伤问卷', '患者自评', 1, 24),
('les', '生活事件量表', '患者自评', 1, 25),
('hcl32', '32项轻躁狂量表', '患者自评', 1, 26),
('ymrs', '杨氏躁狂评定量表', '患者自评', 1, 27),
('epq', '艾森克人格问卷', '患者自评', 1, 28),
('ssrs', '社会支持评定量表', '患者自评', 1, 29),
('iip', '人际问题量表', '患者自评', 1, 30),
('embu', '父母养育方式评价量表', '患者自评', 1, 31),
('das', '功能失调态度量表', '患者自评', 1, 32),
('fad', '家庭功能评定', '患者自评', 1, 33),
('fbs', '疾病家庭负担量表', '患者自评', 1, 34),
('psqi', '匹兹堡睡眠质量指数', '患者自评', 1, 35),
('isi', '失眠严重指数量表', '患者自评', 1, 36),
('meq5', '清晨型与夜晚型量表-5项', '患者自评', 1, 37);

-- 评定记录
INSERT INTO `patient_assessments` (`id_card`, `cohort_code`, `assessment_type`, `assessment_name`, `time_point`, `status`, `score`, `assess_date`, `assessor_id`) VALUES
('420102199705150011', 'WH001', 'informed_consent', '签署知情同意书（S/V）', 'screening', 1, NULL, '2025-11-20', 4),
('420102199705150011', 'WH001', 'inclusion_exclusion', '入组/排除标准', 'screening', 1, NULL, '2025-11-20', 4),
('420102199705150011', 'WH001', 'general_info', '一般信息', 'screening', 1, NULL, '2025-11-20', 4),
('420102199705150011', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'screening', 1, 18.5, '2025-11-20', 4),
('420102199705150011', 'WH001', 'hama', '汉密尔顿焦虑量表', 'screening', 1, 15.0, '2025-11-20', 4),
('420102199705150011', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'baseline', 1, 16.0, '2025-11-26', 4),
('420102199705150011', 'WH001', 'hama', '汉密尔顿焦虑量表', 'baseline', 1, 14.0, '2025-11-26', 4),
('420102199705150011', 'WH001', 'psf', '个体和社会功能量表', 'baseline', 1, 72.0, '2025-11-26', 4),
('420102200008200022', 'WH001', 'informed_consent', '签署知情同意书（S/V）', 'screening', 1, NULL, '2025-11-19', 4),
('420102200008200022', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'screening', 1, 20.0, '2025-11-19', 4),
('420102200008200022', 'WH001', 'hama', '汉密尔顿焦虑量表', 'screening', 1, 16.0, '2025-11-19', 4),
('420102199503100033', 'WH001', 'informed_consent', '签署知情同意书（S/V）', 'screening', 1, NULL, '2025-11-17', 5),
('420102199503100033', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'screening', 1, 22.0, '2025-11-17', 5),
('420102199503100033', 'WH001', 'hama', '汉密尔顿焦虑量表', 'screening', 1, 18.0, '2025-11-17', 5),
('420102199503100033', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'baseline', 1, 19.0, '2025-11-20', 5),
('420102199503100033', 'WH001', 'hama', '汉密尔顿焦虑量表', 'baseline', 1, 15.0, '2025-11-20', 5),
('420102199503100033', 'WH001', 'psf', '个体和社会功能量表', 'baseline', 1, 68.0, '2025-11-20', 5),
('420102199503100033', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'followup_1', 1, 12.0, '2025-11-26', 5),
('420102199503100033', 'WH001', 'psf', '个体和社会功能量表', 'followup_1', 1, 75.0, '2025-11-26', 5),
('420102199503100033', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'followup_2', 0, NULL, NULL, NULL),
('420106200311108448', 'WH001', 'informed_consent', '签署知情同意书（S/V）', 'screening', 1, NULL, NOW(), 4),
('420106200311108448', 'WH001', 'inclusion_exclusion', '入组/排除标准', 'screening', 1, NULL, NOW(), 4),
('420106200311108448', 'WH001', 'general_info', '一般信息', 'screening', 1, NULL, NOW(), 4),
('420106200311108448', 'WH001', 'hamd', '汉密尔顿抑郁量表', 'screening', 1, 16.5, NOW(), 4),
('420106200311108448', 'WH001', 'hama', '汉密尔顿焦虑量表', 'screening', 1, 14.0, NOW(), 4);

-- 字典类型
INSERT INTO `sys_dict_type` (`dict_type`, `dict_name`, `description`, `sort_order`) VALUES
('sys_user_status', '用户状态', '系统用户状态', 1),
('sys_user_gender', '用户性别', '系统用户性别', 2),
('sys_user_role', '用户角色', '系统用户角色类型', 3),
('patient_gender', '患者性别', '患者性别', 20),
('patient_handedness', '利手类型', '患者利手类型', 21),
('patient_residence_type', '居住地类型', '患者长期居住地类型', 22),
('patient_marital_status', '婚姻状况', '患者婚姻状况', 23),
('patient_emotional_status', '情感状态', '患者当前情感状态', 24),
('patient_education', '学历水平', '患者最高学历', 25),
('patient_occupation', '职业类型', '患者职业类型', 26),
('patient_ethnicity', '民族类型', '患者民族', 27),
('patient_status', '患者状态', '患者在系统中的状态', 28),
('assessment_status', '评定状态', '评定记录完成状态', 40),
('assessment_time_point', '评定时间点', '评定的时间阶段', 41);

-- 字典数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `tag_type`) VALUES
('sys_user_status', '禁用', '0', 1, 'danger'),
('sys_user_status', '启用', '1', 2, 'success'),
('sys_user_gender', '女', '0', 1, 'primary'),
('sys_user_gender', '男', '1', 2, 'success'),
('sys_user_role', '系统管理员', '1', 1, 'danger'),
('sys_user_role', '医院管理员', '2', 2, 'warning'),
('sys_user_role', '医生', '3', 3, 'primary'),
('patient_gender', '女', '0', 1, 'primary'),
('patient_gender', '男', '1', 2, 'success'),
('patient_handedness', '左手', '0', 1, 'info'),
('patient_handedness', '右手', '1', 2, 'primary'),
('patient_residence_type', '城市', '1', 1, 'primary'),
('patient_residence_type', '城镇', '2', 2, 'success'),
('patient_residence_type', '农村', '3', 3, 'info'),
('patient_marital_status', '未婚', '0', 1, 'info'),
('patient_marital_status', '已婚', '1', 2, 'success'),
('patient_marital_status', '离异', '2', 3, 'warning'),
('patient_marital_status', '丧偶', '3', 4, 'info'),
('patient_emotional_status', '单身', '0', 1, 'info'),
('patient_emotional_status', '恋爱', '1', 2, 'primary'),
('patient_emotional_status', '同居', '2', 3, 'primary'),
('patient_emotional_status', '初婚有配偶', '3', 4, 'success'),
('patient_emotional_status', '再婚有配偶', '4', 5, 'success'),
('patient_emotional_status', '分居未离婚', '5', 6, 'warning'),
('patient_emotional_status', '离婚', '6', 7, 'warning'),
('patient_emotional_status', '丧偶', '7', 8, 'info');

INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`) VALUES
('patient_education', '初中及以下', '1', 1),
('patient_education', '高中', '2', 2),
('patient_education', '大学/大专', '3', 3),
('patient_education', '硕士', '4', 4),
('patient_education', '博士及以上', '5', 5),
('patient_occupation', '在校学生', '1', 1),
('patient_occupation', '家务', '2', 2),
('patient_occupation', '待业', '3', 3),
('patient_occupation', '离退休人员', '4', 4),
('patient_occupation', '专业技术人员', '6', 6),
('patient_occupation', '商业、服务人员', '8', 8),
('patient_occupation', '其他', '99', 10),
('patient_ethnicity', '汉族', '汉族', 1),
('patient_ethnicity', '其他', '其他', 99);

INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `tag_type`) VALUES
('patient_status', '已排除', '0', 1, 'info'),
('patient_status', '正常', '1', 2, 'success'),
('patient_status', '已出组', '2', 3, 'warning'),
('patient_status', '失访', '3', 4, 'danger'),
('patient_status', '康复', '4', 5, 'success'),
('assessment_status', '不适用', '-1', 1, 'info'),
('assessment_status', '未完成', '0', 2, 'warning'),
('assessment_status', '已完成', '1', 3, 'success'),
('assessment_time_point', '筛查期', 'screening', 1, 'info'),
('assessment_time_point', '基线期', 'baseline', 2, 'primary'),
('assessment_time_point', '随访1', 'followup_1', 3, 'success'),
('assessment_time_point', '随访2', 'followup_2', 4, 'success'),
('assessment_time_point', '随访3', 'followup_3', 5, 'success'),
('assessment_time_point', '复发1', 'relapse_1', 9, 'danger');

-- 验证
SELECT '数据库初始化完成' AS '';
SELECT (SELECT COUNT(*) FROM cohort_centers) AS '医院数', 
       (SELECT COUNT(*) FROM cohort_authorization) AS '用户数', 
       (SELECT COUNT(*) FROM patients) AS '患者数',
       (SELECT COUNT(*) FROM patient_assessments) AS '评定记录数';
