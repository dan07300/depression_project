package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目选项VO
 */
@Data
@ApiModel(value = "QuestionOptionVO", description = "题目选项视图对象")
public class QuestionOptionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("选项标签")
    private String optionLabel;

    @ApiModelProperty("选项内容")
    private String optionContent;

    @ApiModelProperty("选项分值")
    private Integer score;

    @ApiModelProperty("排序号")
    private Integer sortOrder;
}
