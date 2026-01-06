package io.depression.controller;

import io.depression.common.Result;
import io.depression.dto.WarningQueryDTO;
import io.depression.service.WarningService;
import io.depression.vo.PageVO;
import io.depression.vo.WarningRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 预警管理控制器
 */
@Api(tags = "预警管理")
@RestController
@RequestMapping("/api/warnings")
public class WarningController {

    @Autowired
    private WarningService warningService;

    @ApiOperation(value = "分页查询预警列表", notes = "根据条件分页查询预警列表")
    @GetMapping
    public Result<PageVO<WarningRecordVO>> page(WarningQueryDTO queryDTO) {
        PageVO<WarningRecordVO> page = warningService.pageWarnings(queryDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "获取预警详情", notes = "根据ID获取预警详情")
    @GetMapping("/{id}")
    public Result<WarningRecordVO> getById(@PathVariable Long id) {
        WarningRecordVO warning = warningService.getWarningById(id);
        if (warning == null) {
            return Result.error("预警记录不存在");
        }
        return Result.success(warning);
    }

    @ApiOperation(value = "处理预警", notes = "处理预警记录")
    @PostMapping("/{id}/process")
    public Result<WarningRecordVO> process(@PathVariable Long id,
                                           @RequestParam Long processorId,
                                           @RequestParam(required = false) String processRemark) {
        try {
            WarningRecordVO warning = warningService.processWarning(id, processorId, processRemark);
            return Result.success("处理成功", warning);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
