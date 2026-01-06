package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 创建量表题目DTO
 */
@Data
@ApiModel(value = "ScaleQuestionCreateDTO", description = "创建量表题目请求")
public class ScaleQuestionCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评定类型", required = true)
    @NotBlank(message = "评定类型不能为空")
    private String assessmentType;

    @ApiModelProperty(value = "题目编号", required = true)
    @NotNull(message = "题目编号不能为空")
    private Integer questionNo;

    @ApiModelProperty(value = "题目内容", required = true)
    @NotBlank(message = "题目内容不能为空")
    private String content;

    @ApiModelProperty(value = "题目类型：1-单选 2-多选 3-填空 4-量表评分", required = true)
    @NotNull(message = "题目类型不能为空")
    private Integer questionType;

    @ApiModelProperty("是否必答：0-否 1-是")
    private Integer required = 1;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status = 1;

    @ApiModelProperty("备注说明")
    private String remark;

    @ApiModelProperty("选项列表")
    private List<QuestionOptionDTO> options;
}
