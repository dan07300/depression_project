package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.depression.dto.HospitalCreateDTO;
import io.depression.dto.HospitalQueryDTO;
import io.depression.entity.CohortCenter;
import io.depression.mapper.CohortCenterMapper;
import io.depression.service.HospitalService;
import io.depression.vo.HospitalVO;
import io.depression.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 医院服务实现
 */
@Service
public class HospitalServiceImpl extends ServiceImpl<CohortCenterMapper, CohortCenter> implements HospitalService {

    @Override
    public PageVO<HospitalVO> pageHospitals(HospitalQueryDTO queryDTO) {
        Page<CohortCenter> page = new Page<>(queryDTO.getCurrent().longValue(), queryDTO.getSize().longValue());
        LambdaQueryWrapper<CohortCenter> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StringUtils.hasText(queryDTO.getKeyword()), w -> w
                .like(CohortCenter::getCohortName, queryDTO.getKeyword())
                .or()
                .like(CohortCenter::getCohortCode, queryDTO.getKeyword()))
                .eq(queryDTO.getStatus() != null, CohortCenter::getStatus, queryDTO.getStatus())
                .orderByDesc(CohortCenter::getCreateTime);
        this.page(page, wrapper);

        List<HospitalVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), voList);
    }

    @Override
    public List<HospitalVO> listAllHospitals() {
        List<CohortCenter> hospitals = this.list(new LambdaQueryWrapper<CohortCenter>()
                .eq(CohortCenter::getStatus, 1)
                .orderByAsc(CohortCenter::getCohortCode));
        return hospitals.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public HospitalVO getHospitalByCode(String cohortCode) {
        CohortCenter hospital = this.getById(cohortCode);
        if (hospital == null) {
            return null;
        }
        return convertToVO(hospital);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HospitalVO createHospital(HospitalCreateDTO dto) {
        // 检查编码是否已存在
        CohortCenter existing = this.getById(dto.getCohortCode());
        if (existing != null) {
            throw new IllegalArgumentException("医院编码已存在");
        }

        CohortCenter hospital = new CohortCenter();
        BeanUtils.copyProperties(dto, hospital);
        this.save(hospital);

        return getHospitalByCode(hospital.getCohortCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HospitalVO updateHospital(String cohortCode, HospitalCreateDTO dto) {
        CohortCenter hospital = this.getById(cohortCode);
        if (hospital == null) {
            throw new IllegalArgumentException("医院不存在");
        }

        // 如果修改了编码，检查新编码是否已存在
        if (!cohortCode.equals(dto.getCohortCode())) {
            CohortCenter existing = this.getById(dto.getCohortCode());
            if (existing != null) {
                throw new IllegalArgumentException("医院编码已存在");
            }
        }

        BeanUtils.copyProperties(dto, hospital);
        this.updateById(hospital);

        return getHospitalByCode(hospital.getCohortCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHospital(String cohortCode) {
        CohortCenter hospital = this.getById(cohortCode);
        if (hospital == null) {
            throw new IllegalArgumentException("医院不存在");
        }
        return this.removeById(cohortCode);
    }

    private HospitalVO convertToVO(CohortCenter hospital) {
        HospitalVO vo = new HospitalVO();
        BeanUtils.copyProperties(hospital, vo);
        return vo;
    }
}
