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
        Map<Long, Patient> patientMap = Collections.emptyMap();
        Map<Long, CohortAuthorization> processorMap = Collections.emptyMap();

        if (!CollectionUtils.isEmpty(records)) {
            Set<Long> userIds = records.stream()
                    .map(WarningRecord::getUserId)
                    .filter(id -> id != null && id > 0)
                    .collect(Collectors.toSet());
            Set<Long> processorIds = records.stream()
                    .map(WarningRecord::getProcessorId)
                    .filter(id -> id != null && id > 0)
                    .collect(Collectors.toSet());

            if (!CollectionUtils.isEmpty(userIds)) {
                patientMap = patientMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(Patient::getId, Function.identity(), (a, b) -> a));
            }
            if (!CollectionUtils.isEmpty(processorIds)) {
                processorMap = authorizationMapper.selectBatchIds(processorIds).stream()
                        .collect(Collectors.toMap(CohortAuthorization::getId, Function.identity(), (a, b) -> a));
            }
        }

        final Map<Long, Patient> finalPatientMap = patientMap;
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
        Patient patient = warning.getUserId() != null ? patientMapper.selectById(warning.getUserId()) : null;
        CohortAuthorization processor = warning.getProcessorId() != null
                ? authorizationMapper.selectById(warning.getProcessorId())
                : null;
        return convertToVO(warning,
                patient == null ? Collections.emptyMap() : Collections.singletonMap(patient.getId(), patient),
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
                                       Map<Long, Patient> patientMap,
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

        // 设置患者信息
        Patient patient = patientMap.get(warning.getUserId());
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
