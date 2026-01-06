-- ============================================================
-- assessment_config 表更新脚本（合并版）
-- 执行顺序：在 01_drp_system_init.sql 之后执行
-- 最后更新：2025-12-03
-- ============================================================

USE drp_system;

-- ============================================================
-- 一、添加新字段（如果字段已存在会报错，可忽略）
-- ============================================================

-- 使用存储过程安全添加字段
DELIMITER //

DROP PROCEDURE IF EXISTS add_column_if_not_exists//

CREATE PROCEDURE add_column_if_not_exists()
BEGIN
    -- 添加 intro_text 字段
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = DATABASE() 
                   AND TABLE_NAME = 'assessment_config' 
                   AND COLUMN_NAME = 'intro_text') THEN
        ALTER TABLE `assessment_config` ADD COLUMN `intro_text` TEXT DEFAULT NULL COMMENT '问卷引导语' AFTER `description`;
    END IF;
    
    -- 添加 applicable_time_points 字段
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = DATABASE() 
                   AND TABLE_NAME = 'assessment_config' 
                   AND COLUMN_NAME = 'applicable_time_points') THEN
        ALTER TABLE `assessment_config` ADD COLUMN `applicable_time_points` VARCHAR(500) DEFAULT NULL COMMENT '适用的时间点JSON数组' AFTER `intro_text`;
    END IF;
    
    -- 添加 remark 字段
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = DATABASE() 
                   AND TABLE_NAME = 'assessment_config' 
                   AND COLUMN_NAME = 'remark') THEN
        ALTER TABLE `assessment_config` ADD COLUMN `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注' AFTER `status`;
    END IF;
END//

DELIMITER ;

-- 执行存储过程
CALL add_column_if_not_exists();

-- 删除临时存储过程
DROP PROCEDURE IF EXISTS add_column_if_not_exists;

-- ============================================================
-- 二、更新各评定项目的适用时间点
-- ============================================================

-- 基础评定项目
UPDATE `assessment_config` SET `applicable_time_points` = '["screening"]' WHERE `assessment_type` = 'informed_consent';
UPDATE `assessment_config` SET `applicable_time_points` = '["screening"]' WHERE `assessment_type` = 'inclusion_exclusion';
UPDATE `assessment_config` SET `applicable_time_points` = '["screening"]' WHERE `assessment_type` = 'general_info';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'followup_window';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'followup_completion';

-- 医生评定项目
UPDATE `assessment_config` SET `applicable_time_points` = '["screening","baseline","followup","relapse"]' WHERE `assessment_type` = 'hamd';
UPDATE `assessment_config` SET `applicable_time_points` = '["screening","baseline","followup","relapse"]' WHERE `assessment_type` = 'hama';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup","relapse"]' WHERE `assessment_type` = 'psf';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'dsst';

-- 患者自评项目
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup","relapse"]' WHERE `assessment_type` = 'phq9';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup","relapse"]' WHERE `assessment_type` = 'gad7';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup","relapse"]' WHERE `assessment_type` = 'phq15';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup","relapse"]' WHERE `assessment_type` = 'shaps';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'ctq';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'les';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'hcl32';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'ymrs';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'epq';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'ssrs';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'iip';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'embu';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'das';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'fad';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'fbs';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'psqi';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline","followup"]' WHERE `assessment_type` = 'isi';
UPDATE `assessment_config` SET `applicable_time_points` = '["baseline"]' WHERE `assessment_type` = 'meq5';

-- ============================================================
-- 三、设置默认引导语
-- ============================================================

UPDATE `assessment_config` SET `intro_text` = '在过去两个星期，有多少时间您被以下问题所困扰？' WHERE `assessment_type` = 'phq9';
UPDATE `assessment_config` SET `intro_text` = '在过去两个星期，有多少时间您被以下问题所困扰？' WHERE `assessment_type` = 'gad7';
UPDATE `assessment_config` SET `intro_text` = '在过去一个月内，以下问题困扰您的程度如何？' WHERE `assessment_type` = 'phq15';
UPDATE `assessment_config` SET `intro_text` = '请根据您的实际感受回答以下问题' WHERE `assessment_type` IN ('shaps', 'meq5');

-- ============================================================
-- 四、补充可能缺失的评定项目
-- ============================================================

INSERT IGNORE INTO `assessment_config` (`assessment_type`, `assessment_name`, `category`, `is_scored`, `applicable_time_points`, `sort_order`) VALUES
('comorbid_mental', '共患精神障碍', '基础评定', 0, '["screening"]', 6),
('physical_history', '躯体疾病史', '基础评定', 0, '["screening"]', 7),
('family_mental_history', '精神疾病家族史', '基础评定', 0, '["screening"]', 8),
('substance_use', '精神活性物质使用', '基础评定', 0, '["screening"]', 9),
('past_mdd_info', '既往MDD诊疗信息', '医生评定', 0, '["screening"]', 14),
('current_treatment', '目前治疗情况', '医生评定', 0, '["screening","baseline","followup","relapse"]', 15),
('medication_info', '患者药物情况', '医生评定', 0, '["baseline","followup","relapse"]', 16);

-- ============================================================
-- 五、更新各量表的描述和引导语
-- ============================================================

-- 医生评定量表描述
UPDATE `assessment_config` SET `description` = '汉密尔顿抑郁量表(HAMD)是临床上评定抑郁状态的经典量表，共17项', `intro_text` = '请根据患者表现进行评定' WHERE `assessment_type` = 'hamd';
UPDATE `assessment_config` SET `description` = '汉密尔顿焦虑量表(HAMA)用于评定焦虑症状的严重程度，共14项', `intro_text` = '请根据患者表现进行评定' WHERE `assessment_type` = 'hama';
UPDATE `assessment_config` SET `description` = '个体和社会功能量表(PSF)评估患者的社会功能状态', `intro_text` = '请根据患者近期社会功能表现进行评定' WHERE `assessment_type` = 'psf';
UPDATE `assessment_config` SET `description` = '数字符号替代测试(DSST)评估认知功能', `intro_text` = '请指导患者完成测试并记录分数' WHERE `assessment_type` = 'dsst';

-- 患者自评量表描述
UPDATE `assessment_config` SET `description` = '抑郁症筛查量表(PHQ-9)，共9项，用于抑郁症的快速筛查和严重程度评估' WHERE `assessment_type` = 'phq9';
UPDATE `assessment_config` SET `description` = '广泛性焦虑障碍量表(GAD-7)，共7项，用于焦虑症状的筛查' WHERE `assessment_type` = 'gad7';
UPDATE `assessment_config` SET `description` = '躯体症状量表(PHQ-15)，共15项，评估躯体化症状' WHERE `assessment_type` = 'phq15';
UPDATE `assessment_config` SET `description` = '快乐体验量表(SHAPS)评估快感缺失程度' WHERE `assessment_type` = 'shaps';
UPDATE `assessment_config` SET `description` = '儿童期创伤问卷(CTQ)评估童年创伤经历' WHERE `assessment_type` = 'ctq';
UPDATE `assessment_config` SET `description` = '生活事件量表(LES)评估近期生活事件对心理的影响' WHERE `assessment_type` = 'les';
UPDATE `assessment_config` SET `description` = '匹兹堡睡眠质量指数(PSQI)评估睡眠质量' WHERE `assessment_type` = 'psqi';
UPDATE `assessment_config` SET `description` = '失眠严重指数量表(ISI)评估失眠严重程度' WHERE `assessment_type` = 'isi';

-- ============================================================
-- 六、验证更新结果
-- ============================================================
SELECT '评定配置更新完成' AS message;
SELECT assessment_type, assessment_name, category, applicable_time_points, intro_text 
FROM assessment_config 
ORDER BY sort_order;

-- ============================================================
-- 七、添加自定义表单模板字段
-- ============================================================

-- 使用存储过程安全添加 form_template 字段
DELIMITER //

DROP PROCEDURE IF EXISTS add_form_template_field//

CREATE PROCEDURE add_form_template_field()
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_SCHEMA = DATABASE() 
                   AND TABLE_NAME = 'assessment_config' 
                   AND COLUMN_NAME = 'form_template') THEN
        ALTER TABLE `assessment_config` ADD COLUMN `form_template` JSON DEFAULT NULL COMMENT '自定义表单模板JSON' AFTER `applicable_time_points`;
    END IF;
END//

DELIMITER ;

CALL add_form_template_field();
DROP PROCEDURE IF EXISTS add_form_template_field;

-- ============================================================
-- 八、设置自定义表单模板示例
-- ============================================================

-- 为"签署知情同意书"添加自定义表单模板
UPDATE `assessment_config` 
SET `form_template` = JSON_OBJECT(
  'fields', JSON_ARRAY(
    JSON_OBJECT(
      'label', '是否已签署',
      'field', 'signed',
      'type', 'radio',
      'options', JSON_ARRAY('是', '否'),
      'required', true
    )
  )
)
WHERE `assessment_type` = 'informed_consent';

-- 为"随访完成情况"添加自定义表单模板
UPDATE `assessment_config` 
SET `form_template` = JSON_OBJECT(
  'fields', JSON_ARRAY(
    JSON_OBJECT(
      'label', '完成随访',
      'field', 'completed',
      'type', 'radio',
      'options', JSON_ARRAY('是', '否'),
      'required', true
    ),
    JSON_OBJECT(
      'label', '未完成原因',
      'field', 'reason',
      'type', 'textarea',
      'placeholder', '请填写未完成原因',
      'required', false,
      'visibleWhen', JSON_OBJECT('field', 'completed', 'value', '否')
    )
  )
)
WHERE `assessment_type` = 'followup_completion';

-- ============================================================
-- 备注：如果 MySQL 版本不支持 IF NOT EXISTS，请使用以下替代语句
-- ============================================================
/*
-- 检查并添加字段（手动执行）
ALTER TABLE `assessment_config` ADD COLUMN `intro_text` TEXT DEFAULT NULL COMMENT '问卷引导语' AFTER `description`;
ALTER TABLE `assessment_config` ADD COLUMN `applicable_time_points` VARCHAR(500) DEFAULT NULL COMMENT '适用的时间点JSON数组' AFTER `intro_text`;
ALTER TABLE `assessment_config` ADD COLUMN `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注' AFTER `status`;
ALTER TABLE `assessment_config` ADD COLUMN `form_template` JSON DEFAULT NULL COMMENT '自定义表单模板JSON' AFTER `applicable_time_points`;
*/
