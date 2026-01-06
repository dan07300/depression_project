package io.depression.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.depression.entity.ScaleQuestionOption;
import org.apache.ibatis.annotations.Mapper;

/**
 * 题目选项表 Mapper
 */
@Mapper
public interface ScaleQuestionOptionMapper extends BaseMapper<ScaleQuestionOption> {
}
