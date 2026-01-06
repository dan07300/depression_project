package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 首页统计VO
 */
@Data
@ApiModel(value = "DashboardVO", description = "首页统计视图对象")
public class DashboardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医院数量")
    private Long hospitalCount;

    @ApiModelProperty("用户总数")
    private Long userCount;

    @ApiModelProperty("预警总数")
    private Long warningCount;

    @ApiModelProperty("高风险预警数")
    private Long highRiskWarningCount;

    @ApiModelProperty("患者风险等级分布")
    private Map<String, Long> riskLevelDistribution;

    @ApiModelProperty("近半年趋势数据")
    private List<TrendDataVO> trendData;
}
