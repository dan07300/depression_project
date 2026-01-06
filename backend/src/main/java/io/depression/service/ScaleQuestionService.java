package io.depression.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.depression.dto.ScaleQuestionCreateDTO;
import io.depression.dto.ScaleQuestionQueryDTO;
import io.depression.entity.ScaleQuestion;
import io.depression.vo.PageVO;
import io.depression.vo.ScaleQuestionVO;

/**
 * 量表题目服务接口
 */
public interface ScaleQuestionService extends IService<ScaleQuestion> {

    /**
     * 分页查询题目列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageVO<ScaleQuestionVO> pageQuestions(ScaleQuestionQueryDTO queryDTO);

    /**
     * 根据ID获取题目详情（包含选项）
     *
     * @param id 题目ID
     * @return 题目VO
     */
    ScaleQuestionVO getQuestionById(Long id);

    /**
     * 创建题目（包含选项）
     *
     * @param dto 创建DTO
     * @return 题目VO
     */
    ScaleQuestionVO createQuestion(ScaleQuestionCreateDTO dto);

    /**
     * 更新题目
     *
     * @param id 题目ID
     * @param dto 更新DTO
     * @return 题目VO
     */
    ScaleQuestionVO updateQuestion(Long id, ScaleQuestionCreateDTO dto);

    /**
     * 删除题目（逻辑删除，同时删除选项）
     *
     * @param id 题目ID
     * @return 是否成功
     */
    boolean deleteQuestion(Long id);
}
