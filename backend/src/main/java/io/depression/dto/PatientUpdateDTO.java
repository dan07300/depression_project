package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank; // 注意引入 NotBlank
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 更新患者DTO
 */
@Data
@ApiModel(value = "PatientUpdateDTO", description = "更新患者请求")
public class PatientUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "患者身份证号(主键)", required = true)
    @NotNull(message = "患者身份证号不能为空")
    private String idCard;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("性别：0-女 1-男")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("负责医生ID")
    private Long doctorId;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("确诊日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty("病程（月）")
    private Integer diseaseCourse;

    @ApiModelProperty("是否复发：0-否 1-是")
    private Integer isRelapsed;

    @ApiModelProperty("复发日期")
    private LocalDate relapseDate;

    @ApiModelProperty("复发次数")
    private Integer relapseCount;

    @ApiModelProperty("状态：0-已排除 1-正常 2-已出组 3-失访 4-康复")
    private Integer status;

    @ApiModelProperty("备注")
    private String notes;
}


