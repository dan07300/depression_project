package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 预警查询DTO
 */
@Data
@ApiModel(value = "WarningQueryDTO", description = "预警查询条件")
public class WarningQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Integer current = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;

    @ApiModelProperty("预警等级：1-低风险 2-中风险 3-高风险")
    private Integer warningLevel;

    @ApiModelProperty("处理状态：0-未处理 1-处理中 2-已处理")
    private Integer processStatus;
}
