package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 患者查询DTO
 */
@Data
@ApiModel(value = "PatientQueryDTO", description = "患者查询条件")
public class PatientQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("负责医生ID")
    private Long doctorId;

    @ApiModelProperty("患者编号")
    private String patientCode;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态：0-已排除 1-正常 2-已出组 3-失访 4-康复")
    private Integer status;

    @ApiModelProperty("是否复发：0-否 1-是")
    private Integer isRelapsed;

    @ApiModelProperty("当前页码，默认1")
    private Integer current = 1;

    @ApiModelProperty("每页大小，默认10")
    private Integer size = 10;
}


