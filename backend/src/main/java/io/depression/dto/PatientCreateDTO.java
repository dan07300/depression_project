package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 创建患者DTO
 */
@Data
@ApiModel(value = "PatientCreateDTO", description = "创建患者请求")
public class PatientCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "身份证号", required = true)
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty(value = "登录用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "患者姓名", required = true)
    @NotBlank(message = "患者姓名不能为空")
    private String patientName;

    @ApiModelProperty("患者编号")
    private String patientCode;

    @ApiModelProperty(value = "所属医院编码", required = true)
    @NotBlank(message = "所属医院不能为空")
    private String cohortCode;

    @ApiModelProperty(value = "负责医生ID", required = true)
    @NotNull(message = "负责医生不能为空")
    private Long doctorId;

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

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("确诊日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty("病程（月）")
    private Integer diseaseCourse;

    @ApiModelProperty("入组日期")
    private LocalDate enrollDate;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("备注")
    private String notes;
}


