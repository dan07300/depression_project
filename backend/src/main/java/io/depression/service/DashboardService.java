package io.depression.service;

import io.depression.vo.DashboardVO;

/**
 * 首页统计服务接口
 */
public interface DashboardService {

    /**
     * 获取首页统计数据
     *
     * @return 统计数据
     */
    DashboardVO getDashboardData();
}
