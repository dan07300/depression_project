package io.depression.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.depression.entity.Patient;
import org.apache.ibatis.annotations.Mapper;

/**
 * 患者表 Mapper
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}




