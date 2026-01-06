package io.depression.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者评定记录
 */
@Data
@TableName("patient_assessments")
@ApiModel(value = "PatientAssessment", description = "患者评定记录")
public class PatientAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("患者身份证号")
    private String idCard;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("评定类型")
    private String assessmentType;

    @ApiModelProperty("评定名称")
    private String assessmentName;

    @ApiModelProperty("时间点")
    private String timePoint;

    @ApiModelProperty("状态：-1=不适用 0=未完成 1=已完成")
    private Integer status;

    @ApiModelProperty("评分")
    private BigDecimal score;

    @ApiModelProperty("评定日期")
    private LocalDate assessDate;

    @ApiModelProperty("评定员ID")
    private Long assessorId;

    @ApiModelProperty("备注")
    private String notes;

    @ApiModelProperty("详细数据JSON")
    private String detailData;

    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    private Integer deleted;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}




