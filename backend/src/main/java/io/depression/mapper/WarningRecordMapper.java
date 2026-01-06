package io.depression.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.depression.entity.WarningRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预警记录表 Mapper
 */
@Mapper
public interface WarningRecordMapper extends BaseMapper<WarningRecord> {
}
