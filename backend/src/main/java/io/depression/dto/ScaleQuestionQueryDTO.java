package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 量表题目查询DTO
 */
@Data
@ApiModel(value = "ScaleQuestionQueryDTO", description = "量表题目查询条件")
public class ScaleQuestionQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Integer current = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;

    @ApiModelProperty("评定类型")
    private String assessmentType;

    @ApiModelProperty("关键字（题目内容）")
    private String keyword;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;
}
