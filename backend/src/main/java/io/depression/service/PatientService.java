package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.dto.PatientCreateDTO;
import io.depression.dto.PatientQueryDTO;
import io.depression.dto.PatientUpdateDTO;
import io.depression.entity.Patient;
import io.depression.vo.PageVO;
import io.depression.vo.PatientVO;

/**
 * <p>
 * 患者表 服务类
 * </p>
 *
 * @author system
 * @since 2025-01-01
 */
public interface PatientService extends IService<Patient> {

    /**
     * 分页查询患者列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageVO<PatientVO> pagePatients(PatientQueryDTO queryDTO);

    /**
     * 根据ID获取患者详情
     *
     * @param id 患者ID
     * @return 患者VO
     */
    PatientVO getPatientById(Long id);

    /**
     * 创建患者
     *
     * @param dto 创建DTO
     * @return 患者VO
     */
    PatientVO createPatient(PatientCreateDTO dto);

    /**
     * 更新患者
     *
     * @param dto 更新DTO
     * @return 患者VO
     */
    PatientVO updatePatient(PatientUpdateDTO dto);

    /**
     * 删除患者（逻辑删除）
     *
     * @param id 患者ID
     * @return 是否成功
     */
    boolean deletePatient(Long id);

    /**
     * 患者迁移
     *
     * @param patientId 患者ID
     * @param targetCohortCode 目标医院编码
     * @param targetDoctorId 目标医生ID（可选）
     * @return 患者VO
     */
    PatientVO transferPatient(Long patientId, String targetCohortCode, Long targetDoctorId);
}



