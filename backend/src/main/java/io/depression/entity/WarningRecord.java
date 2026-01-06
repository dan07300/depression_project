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
 * 预警记录表
 */
@Data
@TableName("warning_records")
@ApiModel(value = "WarningRecord", description = "预警记录")
public class WarningRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户ID（患者ID）")
    private Long userId;

    @ApiModelProperty("患者身份证号")
    private String idCard;

    @ApiModelProperty("预警等级：1-低风险 2-中风险 3-高风险")
    private Integer warningLevel;

    @ApiModelProperty("风险评分")
    private Double riskScore;

    @ApiModelProperty("预警原因")
    private String warningReason;

    @ApiModelProperty("建议")
    private String suggestion;

    @ApiModelProperty("处理状态：0-未处理 1-处理中 2-已处理")
    private Integer processStatus;

    @ApiModelProperty("处理人ID")
    private Long processorId;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("处理备注")
    private String processRemark;

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
