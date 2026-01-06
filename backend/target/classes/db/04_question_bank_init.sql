-- =====================================================
-- 题库管理模块 - 数据库初始化脚本
-- 创建时间: 2025
-- 说明: 创建量表题目表和题目选项表
-- =====================================================

-- 创建量表题目表
CREATE TABLE IF NOT EXISTS `scale_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `assessment_type` VARCHAR(50) NOT NULL COMMENT '关联的评定类型',
  `question_no` INT NOT NULL COMMENT '题目编号（在该量表中的序号）',
  `content` TEXT NOT NULL COMMENT '题目内容',
  `question_type` TINYINT NOT NULL DEFAULT 1 COMMENT '题目类型：1-单选 2-多选 3-填空 4-量表评分',
  `required` TINYINT NOT NULL DEFAULT 1 COMMENT '是否必答：0-否 1-是',
  `sort_order` INT DEFAULT 0 COMMENT '排序号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注说明',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_assessment_type` (`assessment_type`),
  INDEX `idx_question_no` (`question_no`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='量表题目表';

-- 创建题目选项表
CREATE TABLE IF NOT EXISTS `scale_question_option` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question_id` BIGINT NOT NULL COMMENT '关联的题目ID',
  `option_label` VARCHAR(20) NOT NULL COMMENT '选项标签（如A、B、C、D或0、1、2、3）',
  `option_content` VARCHAR(500) NOT NULL COMMENT '选项内容',
  `score` INT DEFAULT 0 COMMENT '选项分值',
  `sort_order` INT DEFAULT 0 COMMENT '排序号',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_question_id` (`question_id`),
  CONSTRAINT `fk_option_question` FOREIGN KEY (`question_id`) REFERENCES `scale_question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='题目选项表';

-- =====================================================
-- 示例数据：抑郁症筛查量表(PHQ-9)题目
-- =====================================================

-- 插入PHQ-9量表题目（注意：assessment_type 需要与 assessment_config 表中的一致，使用小写）
INSERT INTO `scale_question` (`assessment_type`, `question_no`, `content`, `question_type`, `required`, `sort_order`, `status`) VALUES
('phq9', 1, '做事时提不起劲或没有兴趣', 4, 1, 1, 1),
('phq9', 2, '感到心情低落、沮丧或绝望', 4, 1, 2, 1),
('phq9', 3, '入睡困难、睡不安稳或睡眠过多', 4, 1, 3, 1),
('phq9', 4, '感觉疲倦或没有活力', 4, 1, 4, 1),
('phq9', 5, '食欲不振或吃太多', 4, 1, 5, 1),
('phq9', 6, '觉得自己很糟——或觉得自己很失败，或让自己或家人失望', 4, 1, 6, 1),
('phq9', 7, '对事物专注有困难，例如阅读报纸或看电视时', 4, 1, 7, 1),
('phq9', 8, '动作或说话速度缓慢到别人已经觉察？或正好相反——烦躁或坐立不安、动来动去的情况更胜于平常', 4, 1, 8, 1),
('phq9', 9, '有不如死掉或用某种方式伤害自己的念头', 4, 1, 9, 1);

-- 为PHQ-9题目插入选项（使用字母标签）
-- 为每个phq9题目添加标准选项
INSERT INTO `scale_question_option` (`question_id`, `option_label`, `option_content`, `score`, `sort_order`)
SELECT q.id, 'A', '完全不会', 0, 1 FROM scale_question q WHERE q.assessment_type = 'phq9'
UNION ALL
SELECT q.id, 'B', '好几天', 1, 2 FROM scale_question q WHERE q.assessment_type = 'phq9'
UNION ALL
SELECT q.id, 'C', '一半以上的天数', 2, 3 FROM scale_question q WHERE q.assessment_type = 'phq9'
UNION ALL
SELECT q.id, 'D', '几乎每天', 3, 4 FROM scale_question q WHERE q.assessment_type = 'phq9';

-- =====================================================
-- 示例数据：广泛性焦虑障碍量表(GAD-7)题目
-- =====================================================

INSERT INTO `scale_question` (`assessment_type`, `question_no`, `content`, `question_type`, `required`, `sort_order`, `status`) VALUES
('gad7', 1, '感觉紧张、焦虑或急切', 4, 1, 1, 1),
('gad7', 2, '不能够停止或控制担忧', 4, 1, 2, 1),
('gad7', 3, '对各种各样的事情担忧过多', 4, 1, 3, 1),
('gad7', 4, '很难放松下来', 4, 1, 4, 1),
('gad7', 5, '由于不安而无法静坐', 4, 1, 5, 1),
('gad7', 6, '变得容易烦恼或急躁', 4, 1, 6, 1),
('gad7', 7, '感到似乎将有可怕的事情发生而害怕', 4, 1, 7, 1);

-- 为GAD-7题目插入选项（使用字母标签）
INSERT INTO `scale_question_option` (`question_id`, `option_label`, `option_content`, `score`, `sort_order`)
SELECT q.id, 'A', '完全不会', 0, 1 FROM scale_question q WHERE q.assessment_type = 'gad7'
UNION ALL
SELECT q.id, 'B', '好几天', 1, 2 FROM scale_question q WHERE q.assessment_type = 'gad7'
UNION ALL
SELECT q.id, 'C', '一半以上的天数', 2, 3 FROM scale_question q WHERE q.assessment_type = 'gad7'
UNION ALL
SELECT q.id, 'D', '几乎每天', 3, 4 FROM scale_question q WHERE q.assessment_type = 'gad7';

-- =====================================================
-- 权限配置：添加题库管理权限（使用正确的字段名）
-- =====================================================

-- 添加题库管理一级菜单权限（挂在系统管理模块下，parent_id=3）
INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `icon`, `path`, `description`)
SELECT 'question_bank', '题库管理', 1, 3, 7, 'el-icon-document-copy', '/question-bank', '量表题目管理'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank');

-- 添加题库管理的按钮权限
INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `description`)
SELECT 'question_bank:view', '查看题库', 2, 
    (SELECT id FROM sys_permission WHERE permission_code = 'question_bank'), 1, '查看题目列表'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank:view');

INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `description`)
SELECT 'question_bank:add', '添加题目', 2, 
    (SELECT id FROM sys_permission WHERE permission_code = 'question_bank'), 2, '添加新题目'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank:add');

INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `description`)
SELECT 'question_bank:edit', '编辑题目', 2, 
    (SELECT id FROM sys_permission WHERE permission_code = 'question_bank'), 3, '编辑题目内容'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank:edit');

INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `description`)
SELECT 'question_bank:delete', '删除题目', 2, 
    (SELECT id FROM sys_permission WHERE permission_code = 'question_bank'), 4, '删除题目'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank:delete');

INSERT INTO `sys_permission` (`permission_code`, `permission_name`, `permission_type`, `parent_id`, `sort_order`, `description`)
SELECT 'question_bank:import', '导入题目', 2, 
    (SELECT id FROM sys_permission WHERE permission_code = 'question_bank'), 5, '批量导入题目'
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE permission_code = 'question_bank:import');
