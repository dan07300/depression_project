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
 * 量表题目表
 */
@Data
@TableName("scale_question")
@ApiModel(value = "ScaleQuestion", description = "量表题目")
public class ScaleQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的评定类型")
    private String assessmentType;

    @ApiModelProperty("题目编号（在该量表中的序号）")
    private Integer questionNo;

    @ApiModelProperty("题目内容")
    private String content;

    @ApiModelProperty("题目类型：1-单选 2-多选 3-填空 4-量表评分")
    private Integer questionType;

    @ApiModelProperty("是否必答：0-否 1-是")
    private Integer required;

    @ApiModelProperty("排序号")
    private Integer sortOrder;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;

    @ApiModelProperty("备注说明")
    private String remark;

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
