package io.depression.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.depression.dto.QuestionOptionDTO;
import io.depression.dto.ScaleQuestionCreateDTO;
import io.depression.dto.ScaleQuestionQueryDTO;
import io.depression.entity.ScaleQuestion;
import io.depression.entity.ScaleQuestionOption;
import io.depression.mapper.ScaleQuestionMapper;
import io.depression.mapper.ScaleQuestionOptionMapper;
import io.depression.service.ScaleQuestionService;
import io.depression.vo.PageVO;
import io.depression.vo.QuestionOptionVO;
import io.depression.vo.ScaleQuestionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 量表题目服务实现
 */
@Service
public class ScaleQuestionServiceImpl extends ServiceImpl<ScaleQuestionMapper, ScaleQuestion> implements ScaleQuestionService {

    @Autowired
    private ScaleQuestionOptionMapper optionMapper;

    @Override
    public PageVO<ScaleQuestionVO> pageQuestions(ScaleQuestionQueryDTO queryDTO) {
        Page<ScaleQuestion> page = new Page<>(queryDTO.getCurrent().longValue(), queryDTO.getSize().longValue());
        LambdaQueryWrapper<ScaleQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(queryDTO.getAssessmentType()), ScaleQuestion::getAssessmentType, queryDTO.getAssessmentType())
                .like(StringUtils.hasText(queryDTO.getKeyword()), ScaleQuestion::getContent, queryDTO.getKeyword())
                .eq(queryDTO.getStatus() != null, ScaleQuestion::getStatus, queryDTO.getStatus())
                .orderByAsc(ScaleQuestion::getAssessmentType)
                .orderByAsc(ScaleQuestion::getQuestionNo)
                .orderByAsc(ScaleQuestion::getSortOrder);
        this.page(page, wrapper);

        List<ScaleQuestion> records = page.getRecords();
        Map<Long, List<ScaleQuestionOption>> optionMap = Collections.emptyMap();
        if (!CollectionUtils.isEmpty(records)) {
            Set<Long> questionIds = records.stream()
                    .map(ScaleQuestion::getId)
                    .collect(Collectors.toSet());
            List<ScaleQuestionOption> options = optionMapper.selectList(
                    new LambdaQueryWrapper<ScaleQuestionOption>()
                            .in(ScaleQuestionOption::getQuestionId, questionIds)
                            .orderByAsc(ScaleQuestionOption::getSortOrder)
            );
            optionMap = options.stream()
                    .collect(Collectors.groupingBy(ScaleQuestionOption::getQuestionId));
        }

        final Map<Long, List<ScaleQuestionOption>> finalOptionMap = optionMap;
        List<ScaleQuestionVO> voList = records.stream()
                .map(item -> convertToVO(item, finalOptionMap.getOrDefault(item.getId(), Collections.emptyList())))
                .collect(Collectors.toList());

        return new PageVO<>(page.getCurrent(), page.getSize(), page.getTotal(), voList);
    }

    @Override
    public ScaleQuestionVO getQuestionById(Long id) {
        ScaleQuestion question = this.getById(id);
        if (question == null) {
            return null;
        }
        List<ScaleQuestionOption> options = optionMapper.selectList(
                new LambdaQueryWrapper<ScaleQuestionOption>()
                        .eq(ScaleQuestionOption::getQuestionId, id)
                        .orderByAsc(ScaleQuestionOption::getSortOrder)
        );
        return convertToVO(question, options);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScaleQuestionVO createQuestion(ScaleQuestionCreateDTO dto) {
        // 检查题目编号是否重复
        Long count = this.lambdaQuery()
                .eq(ScaleQuestion::getAssessmentType, dto.getAssessmentType())
                .eq(ScaleQuestion::getQuestionNo, dto.getQuestionNo())
                .count();
        if (count != null && count > 0) {
            throw new IllegalArgumentException("该量表中题目编号已存在");
        }

        ScaleQuestion question = new ScaleQuestion();
        BeanUtils.copyProperties(dto, question);
        this.save(question);

        // 保存选项
        if (!CollectionUtils.isEmpty(dto.getOptions())) {
            int sortOrder = 1;
            for (QuestionOptionDTO optionDTO : dto.getOptions()) {
                ScaleQuestionOption option = new ScaleQuestionOption();
                option.setQuestionId(question.getId());
                option.setOptionLabel(optionDTO.getOptionLabel());
                option.setOptionContent(optionDTO.getOptionContent());
                option.setScore(optionDTO.getScore() != null ? optionDTO.getScore() : 0);
                option.setSortOrder(optionDTO.getSortOrder() != null ? optionDTO.getSortOrder() : sortOrder++);
                optionMapper.insert(option);
            }
        }

        return getQuestionById(question.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScaleQuestionVO updateQuestion(Long id, ScaleQuestionCreateDTO dto) {
        ScaleQuestion question = this.getById(id);
        if (question == null) {
            throw new IllegalArgumentException("题目不存在");
        }

        // 检查题目编号是否重复（排除自己）
        if (!question.getQuestionNo().equals(dto.getQuestionNo()) || 
            !question.getAssessmentType().equals(dto.getAssessmentType())) {
            Long count = this.lambdaQuery()
                    .eq(ScaleQuestion::getAssessmentType, dto.getAssessmentType())
                    .eq(ScaleQuestion::getQuestionNo, dto.getQuestionNo())
                    .ne(ScaleQuestion::getId, id)
                    .count();
            if (count != null && count > 0) {
                throw new IllegalArgumentException("该量表中题目编号已存在");
            }
        }

        BeanUtils.copyProperties(dto, question);
        this.updateById(question);

        // 删除旧选项，插入新选项
        optionMapper.delete(new LambdaQueryWrapper<ScaleQuestionOption>()
                .eq(ScaleQuestionOption::getQuestionId, id));
        if (!CollectionUtils.isEmpty(dto.getOptions())) {
            int sortOrder = 1;
            for (QuestionOptionDTO optionDTO : dto.getOptions()) {
                ScaleQuestionOption option = new ScaleQuestionOption();
                option.setQuestionId(question.getId());
                option.setOptionLabel(optionDTO.getOptionLabel());
                option.setOptionContent(optionDTO.getOptionContent());
                option.setScore(optionDTO.getScore() != null ? optionDTO.getScore() : 0);
                option.setSortOrder(optionDTO.getSortOrder() != null ? optionDTO.getSortOrder() : sortOrder++);
                optionMapper.insert(option);
            }
        }

        return getQuestionById(question.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(Long id) {
        ScaleQuestion question = this.getById(id);
        if (question == null) {
            throw new IllegalArgumentException("题目不存在");
        }
        // 逻辑删除题目（选项会通过外键级联删除，但这里我们也逻辑删除）
        optionMapper.delete(new LambdaQueryWrapper<ScaleQuestionOption>()
                .eq(ScaleQuestionOption::getQuestionId, id));
        return this.removeById(id);
    }

    private ScaleQuestionVO convertToVO(ScaleQuestion question, List<ScaleQuestionOption> options) {
        ScaleQuestionVO vo = new ScaleQuestionVO();
        BeanUtils.copyProperties(question, vo);
        
        // 设置题目类型名称
        if (question.getQuestionType() != null) {
            switch (question.getQuestionType()) {
                case 1:
                    vo.setQuestionTypeName("单选");
                    break;
                case 2:
                    vo.setQuestionTypeName("多选");
                    break;
                case 3:
                    vo.setQuestionTypeName("填空");
                    break;
                case 4:
                    vo.setQuestionTypeName("量表评分");
                    break;
                default:
                    vo.setQuestionTypeName("未知");
            }
        }

        // 转换选项
        if (!CollectionUtils.isEmpty(options)) {
            List<QuestionOptionVO> optionVOs = options.stream()
                    .map(option -> {
                        QuestionOptionVO optionVO = new QuestionOptionVO();
                        BeanUtils.copyProperties(option, optionVO);
                        return optionVO;
                    })
                    .collect(Collectors.toList());
            vo.setOptions(optionVOs);
            vo.setOptionCount(optionVOs.size());
        } else {
            vo.setOptions(Collections.emptyList());
            vo.setOptionCount(0);
        }

        return vo;
    }
}
