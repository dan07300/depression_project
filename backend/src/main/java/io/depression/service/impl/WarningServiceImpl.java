package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.depression.dto.WarningQueryDTO;
import io.depression.entity.CohortAuthorization;
import io.depression.entity.Patient;
import io.depression.entity.WarningRecord;
import io.depression.mapper.CohortAuthorizationMapper;
import io.depression.mapper.PatientMapper;
import io.depression.mapper.WarningRecordMapper;
import io.depression.service.WarningService;
import io.depression.vo.PageVO;
import io.depression.vo.WarningRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils; // 引入 StringUtils

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 预警服务实现
 */
@Service
public class WarningServiceImpl extends ServiceImpl<WarningRecordMapper, WarningRecord> implements WarningService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private CohortAuthorizationMapper authorizationMapper;

    @Override
    public PageVO<WarningRecordVO> pageWarnings(WarningQueryDTO queryDTO) {
        Page<WarningRecord> page = new Page<>(queryDTO.getCurrent().longValue(), queryDTO.getSize().longValue());
        LambdaQueryWrapper<WarningRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getWarningLevel() != null, WarningRecord::getWarningLevel, queryDTO.getWarningLevel())
                .eq(queryDTO.getProcessStatus() != null, WarningRecord::getProcessStatus, queryDTO.getProcessStatus())
                .orderByDesc(WarningRecord::getCreateTime);
        this.page(page, wrapper);

        List<WarningRecord> records = page.getRecords();
        Map<String, Patient> patientMap = Collections.emptyMap(); // Key 是 String (idCard)
        Map<Long, CohortAuthorization> processorMap = Collections.emptyMap();

        if (!CollectionUtils.isEmpty(records)) {
            // 修复 1: 这里的变量类型改为 Set<String>，因为 getIdCard 是 String
            Set<String> patientIdCards = records.stream()
                    .map(WarningRecord::getIdCard)
                    .filter(StringUtils::hasText) // 使用 StringUtils.hasText
                    .collect(Collectors.toSet());

            Set<Long> processorIds = records.stream()
                    .map(WarningRecord::getProcessorId)
                    .filter(id -> id != null && id > 0)
                    .collect(Collectors.toSet());

            if (!CollectionUtils.isEmpty(patientIdCards)) {
                // selectBatchIds 现在接受 String 集合，因为 Patient 主键是 String
                patientMap = patientMapper.selectBatchIds(patientIdCards).stream()
                        .collect(Collectors.toMap(Patient::getIdCard, Function.identity(), (a, b) -> a));
            }
            if (!CollectionUtils.isEmpty(processorIds)) {
                processorMap = authorizationMapper.selectBatchIds(processorIds).stream()
                        .collect(Collectors.toMap(CohortAuthorization::getId, Function.identity(), (a, b) -> a));
            }
        }

        final Map<String, Patient> finalPatientMap = patientMap;
        final Map<Long, CohortAuthorization> finalProcessorMap = processorMap;
        List<WarningRecordVO> voList = records.stream()
                .map(item -> convertToVO(item, finalPatientMap, finalProcessorMap))
                .collect(Collectors.toList());

        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), voList);
    }

    @Override
    public WarningRecordVO getWarningById(Long id) {
        WarningRecord warning = this.getById(id);
        if (warning == null) {
            return null;
        }

        // 修复 2: selectById 需要传入 idCard (String)，而不是 userId (Long)
        Patient patient = null;
        if (StringUtils.hasText(warning.getIdCard())) {
            patient = patientMapper.selectById(warning.getIdCard());
        } else if (warning.getUserId() != null) {
            // 如果只有 userId 没有 idCard（旧数据），可能查不出来，或者你可以尝试用 userId 去查（前提是你写了 findById 方法）
            // 这里我们优先假设用 idCard 关联
        }

        CohortAuthorization processor = warning.getProcessorId() != null
                ? authorizationMapper.selectById(warning.getProcessorId())
                : null;

        // 构造 Map 时 key 也要用 String
        return convertToVO(warning,
                patient == null ? Collections.emptyMap() : Collections.singletonMap(patient.getIdCard(), patient),
                processor == null ? Collections.emptyMap() : Collections.singletonMap(processor.getId(), processor));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningRecordVO processWarning(Long id, Long processorId, String processRemark) {
        WarningRecord warning = this.getById(id);
        if (warning == null) {
            throw new IllegalArgumentException("预警记录不存在");
        }

        warning.setProcessStatus(2); // 已处理
        warning.setProcessorId(processorId);
        warning.setProcessTime(LocalDateTime.now());
        warning.setProcessRemark(processRemark);
        this.updateById(warning);

        return getWarningById(warning.getId());
    }

    private WarningRecordVO convertToVO(WarningRecord warning,
                                        Map<String, Patient> patientMap, // 参数类型确认是 <String, Patient>
                                        Map<Long, CohortAuthorization> processorMap) {
        WarningRecordVO vo = new WarningRecordVO();
        BeanUtils.copyProperties(warning, vo);

        // 设置预警等级名称
        if (warning.getWarningLevel() != null) {
            switch (warning.getWarningLevel()) {
                case 1:
                    vo.setWarningLevelName("低风险");
                    break;
                case 2:
                    vo.setWarningLevelName("中风险");
                    break;
                case 3:
                    vo.setWarningLevelName("高风险");
                    break;
                default:
                    vo.setWarningLevelName("未知");
            }
        }

        // 设置处理状态名称
        if (warning.getProcessStatus() != null) {
            switch (warning.getProcessStatus()) {
                case 0:
                    vo.setProcessStatusName("未处理");
                    break;
                case 1:
                    vo.setProcessStatusName("处理中");
                    break;
                case 2:
                    vo.setProcessStatusName("已处理");
                    break;
                default:
                    vo.setProcessStatusName("未知");
            }
        }

        // 设置患者信息：使用 idCard 获取
        // 注意：warning.getIdCard() 可能为 null，所以要做防空处理，或者 patientMap.get(null) 返回 null 也没事
        Patient patient = patientMap.get(warning.getIdCard());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
            vo.setPatientCode(patient.getPatientCode());
        }

        // 设置处理人信息
        CohortAuthorization processor = processorMap.get(warning.getProcessorId());
        if (processor != null) {
            vo.setProcessorName(processor.getRealName());
        }

        return vo;
    }
}