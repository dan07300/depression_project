package io.depression.controller;

import io.depression.common.Result;
import io.depression.service.DashboardService;
import io.depression.vo.DashboardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页统计控制器
 */
@Api(tags = "首页统计")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @ApiOperation(value = "获取首页统计数据", notes = "获取首页的统计数据和图表数据")
    @GetMapping
    public Result<DashboardVO> getDashboardData() {
        try {
            DashboardVO data = dashboardService.getDashboardData();
            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取统计数据失败: " + e.getMessage());
        }
    }
}
