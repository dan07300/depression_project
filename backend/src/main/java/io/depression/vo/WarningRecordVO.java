package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预警记录VO
 */
@Data
@ApiModel(value = "WarningRecordVO", description = "预警记录视图对象")
public class WarningRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("患者编号")
    private String patientCode;

    @ApiModelProperty("预警等级：1-低风险 2-中风险 3-高风险")
    private Integer warningLevel;

    @ApiModelProperty("预警等级名称")
    private String warningLevelName;

    @ApiModelProperty("风险评分")
    private Double riskScore;

    @ApiModelProperty("预警原因")
    private String warningReason;

    @ApiModelProperty("建议")
    private String suggestion;

    @ApiModelProperty("处理状态：0-未处理 1-处理中 2-已处理")
    private Integer processStatus;

    @ApiModelProperty("处理状态名称")
    private String processStatusName;

    @ApiModelProperty("处理人ID")
    private Long processorId;

    @ApiModelProperty("处理人姓名")
    private String processorName;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("处理备注")
    private String processRemark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
