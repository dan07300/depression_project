package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 患者视图对象
 */
@Data
@ApiModel(value = "PatientVO", description = "患者视图对象")
public class PatientVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("患者编号")
    private String patientCode;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("所属医院名称")
    private String cohortName;

    @ApiModelProperty("负责医生ID")
    private Long doctorId;

    @ApiModelProperty("负责医生姓名")
    private String doctorName;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("确诊日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty("入组日期")
    private LocalDate enrollDate;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("状态：0-已排除 1-正常 2-已出组 3-失访 4-康复")
    private Integer status;

    @ApiModelProperty("是否复发：0-否 1-是")
    private Integer isRelapsed;

    @ApiModelProperty("复发日期")
    private LocalDate relapseDate;

    @ApiModelProperty("复发次数")
    private Integer relapseCount;

    @ApiModelProperty("备注")
    private String notes;
}


