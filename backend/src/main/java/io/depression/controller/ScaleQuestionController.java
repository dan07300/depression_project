package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.ScaleQuestionCreateDTO;
import io.depression.dto.ScaleQuestionQueryDTO;
import io.depression.service.ScaleQuestionService;
import io.depression.vo.PageVO;
import io.depression.vo.ScaleQuestionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 量表题目管理控制器
 */
@Api(tags = "量表题目管理")
@RestController
@RequestMapping("/api/scale-questions")
public class ScaleQuestionController {

    @Autowired
    private ScaleQuestionService scaleQuestionService;

    @ApiOperation(value = "分页查询题目列表", notes = "根据条件分页查询题目列表")
    @GetMapping
    public Result<PageVO<ScaleQuestionVO>> page(ScaleQuestionQueryDTO queryDTO) {
        PageVO<ScaleQuestionVO> page = scaleQuestionService.pageQuestions(queryDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "获取题目详情", notes = "根据ID获取题目详情（包含选项）")
    @GetMapping("/{id}")
    public Result<ScaleQuestionVO> getById(@PathVariable Long id) {
        ScaleQuestionVO question = scaleQuestionService.getQuestionById(id);
        if (question == null) {
            return Result.error("题目不存在");
        }
        return Result.success(question);
    }

    @ApiOperation(value = "创建题目", notes = "创建新题目（包含选项）")
    @PostMapping
    public Result<ScaleQuestionVO> create(@Valid @RequestBody ScaleQuestionCreateDTO dto) {
        try {
            ScaleQuestionVO question = scaleQuestionService.createQuestion(dto);
            return Result.success("创建成功", question);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "更新题目", notes = "更新题目信息（包含选项）")
    @PutMapping("/{id}")
    public Result<ScaleQuestionVO> update(@PathVariable Long id, @Valid @RequestBody ScaleQuestionCreateDTO dto) {
        try {
            ScaleQuestionVO question = scaleQuestionService.updateQuestion(id, dto);
            return Result.success("更新成功", question);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "删除题目", notes = "逻辑删除题目（同时删除选项）")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = scaleQuestionService.deleteQuestion(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
