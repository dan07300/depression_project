package io.depression.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目选项表
 */
@Data
@TableName("scale_question_option")
@ApiModel(value = "ScaleQuestionOption", description = "题目选项")
public class ScaleQuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的题目ID")
    private Long questionId;

    @ApiModelProperty("选项标签（如A、B、C、D或0、1、2、3）")
    private String optionLabel;

    @ApiModelProperty("选项内容")
    private String optionContent;

    @ApiModelProperty("选项分值")
    private Integer score;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @TableLogic
    @ApiModelProperty("逻辑删除：0-未删除 1-已删除")
    private Integer deleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
