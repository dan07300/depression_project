package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.depression.dto.PatientCreateDTO;
import io.depression.dto.PatientQueryDTO;
import io.depression.dto.PatientUpdateDTO;
import io.depression.entity.CohortAuthorization;
import io.depression.entity.CohortCenter;
import io.depression.entity.Patient;
import io.depression.mapper.CohortAuthorizationMapper;
import io.depression.mapper.CohortCenterMapper;
import io.depression.mapper.PatientMapper;
import io.depression.service.PatientService;
import io.depression.vo.PageVO;
import io.depression.vo.PatientVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    private final CohortCenterMapper cohortCenterMapper;
    private final CohortAuthorizationMapper authorizationMapper;

    public PatientServiceImpl(CohortCenterMapper cohortCenterMapper,
                              CohortAuthorizationMapper authorizationMapper) {
        this.cohortCenterMapper = cohortCenterMapper;
        this.authorizationMapper = authorizationMapper;
    }

    @Override
    public PageVO<PatientVO> pagePatients(PatientQueryDTO queryDTO) {
        Page<Patient> page = new Page<>(queryDTO.getCurrent().longValue(), queryDTO.getSize().longValue());
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(queryDTO.getCohortCode()), Patient::getCohortCode, queryDTO.getCohortCode())
                .eq(queryDTO.getDoctorId() != null, Patient::getDoctorId, queryDTO.getDoctorId())
                .like(StringUtils.hasText(queryDTO.getPatientCode()), Patient::getPatientCode, queryDTO.getPatientCode())
                .like(StringUtils.hasText(queryDTO.getPatientName()), Patient::getPatientName, queryDTO.getPatientName())
                .like(StringUtils.hasText(queryDTO.getPhone()), Patient::getPhone, queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, Patient::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getIsRelapsed() != null, Patient::getIsRelapsed, queryDTO.getIsRelapsed())
                .orderByDesc(Patient::getEnrollDate)
                .orderByDesc(Patient::getCreateTime); // id 没了，改用 createTime 排序
        this.page(page, wrapper);

        List<Patient> records = page.getRecords();

        Map<String, CohortCenter> centerMap = Collections.emptyMap();
        Map<Long, CohortAuthorization> doctorMap = Collections.emptyMap();

        if (!CollectionUtils.isEmpty(records)) {
            Set<String> cohortCodes = records.stream()
                    .map(Patient::getCohortCode)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
            Set<Long> doctorIds = records.stream()
                    .map(Patient::getDoctorId)
                    .filter(id -> id != null && id > 0)
                    .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(cohortCodes)) {
                centerMap = cohortCenterMapper.selectBatchIds(cohortCodes).stream()
                        .collect(Collectors.toMap(CohortCenter::getCohortCode, Function.identity(), (a, b) -> a));
            }
            if (!CollectionUtils.isEmpty(doctorIds)) {
                doctorMap = authorizationMapper.selectBatchIds(doctorIds).stream()
                        .collect(Collectors.toMap(CohortAuthorization::getId, Function.identity(), (a, b) -> a));
            }
        }

        final Map<String, CohortCenter> finalCenterMap = centerMap;
        final Map<Long, CohortAuthorization> finalDoctorMap = doctorMap;
        List<PatientVO> voList = records.stream()
                .map(item -> convertToVO(item, finalCenterMap, finalDoctorMap))
                .collect(Collectors.toList());

        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), voList);
    }

    @Override
    public PatientVO getPatientById(String idCard) {
        Patient patient = this.getById(idCard); // MyBatis Plus 会根据 @TableId 用 String 查
        if (patient == null) {
            return null;
        }
        CohortCenter center = StringUtils.hasText(patient.getCohortCode())
                ? cohortCenterMapper.selectById(patient.getCohortCode())
                : null;
        CohortAuthorization doctor = patient.getDoctorId() != null
                ? authorizationMapper.selectById(patient.getDoctorId())
                : null;
        return convertToVO(patient,
                center == null ? Collections.emptyMap() : Collections.singletonMap(center.getCohortCode(), center),
                doctor == null ? Collections.emptyMap() : Collections.singletonMap(doctor.getId(), doctor));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientVO createPatient(PatientCreateDTO dto) {
        // 创建时 idCard 是必填的
        validatePatientUniqueness(dto.getIdCard(), dto.getUsername(), dto.getPatientCode());

        Patient patient = new Patient();
        patient.setIdCard(dto.getIdCard()); // 手动设置 ID
        patient.setUsername(dto.getUsername());
        patient.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        patient.setPatientName(dto.getPatientName());
        patient.setPatientCode(dto.getPatientCode());
        patient.setCohortCode(dto.getCohortCode());
        patient.setDoctorId(dto.getDoctorId());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setGender(dto.getGender());
        patient.setBirthday(dto.getBirthday());
        patient.setDiagnosis(dto.getDiagnosis());
        patient.setDiagnosisDate(dto.getDiagnosisDate());
        patient.setDiseaseCourse(dto.getDiseaseCourse());
        patient.setEnrollDate(dto.getEnrollDate() == null ? LocalDate.now() : dto.getEnrollDate());
        patient.setGroupName(dto.getGroupName());
        patient.setNotes(dto.getNotes());
        patient.setStatus(1);
        patient.setIsRelapsed(0);
        patient.setEpisodeCount(1);
        this.save(patient);

        return getPatientById(patient.getIdCard());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientVO updatePatient(PatientUpdateDTO dto) {
        Patient patient = this.getById(dto.getIdCard());
        if (patient == null) {
            throw new IllegalArgumentException("患者不存在");
        }
        BeanUtils.copyProperties(dto, patient, getNullPropertyNames(dto));
        this.updateById(patient);
        return getPatientById(patient.getIdCard());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePatient(String idCard) {
        Patient patient = this.getById(idCard);
        if (patient == null) {
            throw new IllegalArgumentException("患者不存在");
        }
        return this.removeById(idCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientVO transferPatient(String idCard, String targetCohortCode, Long targetDoctorId) {
        Patient patient = this.getById(idCard);
        if (patient == null) {
            throw new IllegalArgumentException("患者不存在");
        }

        CohortCenter targetCenter = cohortCenterMapper.selectById(targetCohortCode);
        if (targetCenter == null) {
            throw new IllegalArgumentException("目标医院不存在");
        }

        if (targetDoctorId != null) {
            CohortAuthorization doctor = authorizationMapper.selectById(targetDoctorId);
            if (doctor == null || doctor.getRoleType() != 3) {
                throw new IllegalArgumentException("目标医生不存在或不是医生角色");
            }
            if (!targetCohortCode.equals(doctor.getCohortCode())) {
                throw new IllegalArgumentException("目标医生不属于目标医院");
            }
        }

        patient.setCohortCode(targetCohortCode);
        patient.setDoctorId(targetDoctorId);
        this.updateById(patient);

        return getPatientById(patient.getIdCard());
    }

    private void validatePatientUniqueness(String idCard, String username, String patientCode) {
        if (StringUtils.hasText(idCard)) {
            // idCard 是主键，check exist 要排除自己吗？创建时不需要，这里只是 check 存在性
            Long count = this.lambdaQuery().eq(Patient::getIdCard, idCard).count();
            if (count != null && count > 0) {
                throw new IllegalArgumentException("身份证号已存在");
            }
        }
        if (StringUtils.hasText(username)) {
            Long count = this.lambdaQuery().eq(Patient::getUsername, username).count();
            if (count != null && count > 0) {
                throw new IllegalArgumentException("用户名已存在");
            }
        }
        if (StringUtils.hasText(patientCode)) {
            Long count = this.lambdaQuery().eq(Patient::getPatientCode, patientCode).count();
            if (count != null && count > 0) {
                throw new IllegalArgumentException("患者编号已存在");
            }
        }
    }

    private PatientVO convertToVO(Patient patient,
                                  Map<String, CohortCenter> centerMap,
                                  Map<Long, CohortAuthorization> doctorMap) {
        PatientVO vo = new PatientVO();
        // vo.setId(patient.getId()); // ID 字段已删除
        vo.setIdCard(patient.getIdCard());
        vo.setPatientCode(patient.getPatientCode());
        vo.setPatientName(patient.getPatientName());
        vo.setPhone(patient.getPhone());
        vo.setEmail(patient.getEmail());
        vo.setCohortCode(patient.getCohortCode());
        vo.setDoctorId(patient.getDoctorId());
        vo.setDiagnosis(patient.getDiagnosis());
        vo.setDiagnosisDate(patient.getDiagnosisDate());
        vo.setEnrollDate(patient.getEnrollDate());
        vo.setGroupName(patient.getGroupName());
        vo.setStatus(patient.getStatus());
        vo.setIsRelapsed(patient.getIsRelapsed());
        vo.setRelapseDate(patient.getRelapseDate());
        vo.setRelapseCount(patient.getRelapseCount());
        vo.setNotes(patient.getNotes());

        CohortCenter center = centerMap.get(patient.getCohortCode());
        if (center != null) {
            vo.setCohortName(center.getCohortName());
        }
        CohortAuthorization doctor = doctorMap.get(patient.getDoctorId());
        if (doctor != null) {
            vo.setDoctorName(doctor.getRealName());
        }
        return vo;
    }

    private String[] getNullPropertyNames(PatientUpdateDTO source) {
        java.beans.BeanInfo beanInfo;
        try {
            beanInfo = java.beans.Introspector.getBeanInfo(PatientUpdateDTO.class, Object.class);
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
}