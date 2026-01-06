package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 量表题目VO
 */
@Data
@ApiModel(value = "ScaleQuestionVO", description = "量表题目视图对象")
public class ScaleQuestionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("评定类型")
    private String assessmentType;

    @ApiModelProperty("题目编号")
    private Integer questionNo;

    @ApiModelProperty("题目内容")
    private String content;

    @ApiModelProperty("题目类型：1-单选 2-多选 3-填空 4-量表评分")
    private Integer questionType;

    @ApiModelProperty("题目类型名称")
    private String questionTypeName;

    @ApiModelProperty("是否必答：0-否 1-是")
    private Integer required;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;

    @ApiModelProperty("备注说明")
    private String remark;

    @ApiModelProperty("选项列表")
    private List<QuestionOptionVO> options;

    @ApiModelProperty("选项数量")
    private Integer optionCount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
