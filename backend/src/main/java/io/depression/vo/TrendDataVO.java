package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 趋势数据VO
 */
@Data
@ApiModel(value = "TrendDataVO", description = "趋势数据视图对象")
public class TrendDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份（格式：YYYY-MM）")
    private String month;

    @ApiModelProperty("入组人数")
    private Long enrollmentCount;

    @ApiModelProperty("高风险预警数")
    private Long highRiskWarningCount;
}
