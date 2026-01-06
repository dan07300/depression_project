package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 题目选项DTO
 */
@Data
@ApiModel(value = "QuestionOptionDTO", description = "题目选项")
public class QuestionOptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "选项标签（如A、B、C、D）", required = true)
    @NotBlank(message = "选项标签不能为空")
    private String optionLabel;

    @ApiModelProperty(value = "选项内容", required = true)
    @NotBlank(message = "选项内容不能为空")
    private String optionContent;

    @ApiModelProperty("选项分值")
    private Integer score = 0;

    @ApiModelProperty("排序号")
    private Integer sortOrder;
}
