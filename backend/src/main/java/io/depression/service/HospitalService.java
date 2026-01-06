package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.dto.HospitalCreateDTO;
import io.depression.dto.HospitalQueryDTO;
import io.depression.entity.CohortCenter;
import io.depression.vo.HospitalVO;
import io.depression.vo.PageVO;

import java.util.List;

/**
 * 医院服务接口
 */
public interface HospitalService extends IService<CohortCenter> {

    /**
     * 分页查询医院列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageVO<HospitalVO> pageHospitals(HospitalQueryDTO queryDTO);

    /**
     * 获取所有启用的医院列表
     *
     * @return 医院列表
     */
    List<HospitalVO> listAllHospitals();

    /**
     * 根据编码获取医院详情
     *
     * @param cohortCode 医院编码
     * @return 医院VO
     */
    HospitalVO getHospitalByCode(String cohortCode);

    /**
     * 创建医院
     *
     * @param dto 创建DTO
     * @return 医院VO
     */
    HospitalVO createHospital(HospitalCreateDTO dto);

    /**
     * 更新医院
     *
     * @param cohortCode 医院编码
     * @param dto 更新DTO
     * @return 医院VO
     */
    HospitalVO updateHospital(String cohortCode, HospitalCreateDTO dto);

    /**
     * 删除医院（逻辑删除）
     *
     * @param cohortCode 医院编码
     * @return 是否成功
     */
    boolean deleteHospital(String cohortCode);
}
