package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 患者迁移DTO
 */
@Data
@ApiModel(value = "PatientTransferDTO", description = "患者迁移请求")
public class PatientTransferDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "患者身份证号", required = true)
    @NotNull(message = "患者身份证号不能为空")
    private String idCard;

    @ApiModelProperty(value = "目标医院编码", required = true)
    @NotNull(message = "目标医院编码不能为空")
    private String targetCohortCode;

    @ApiModelProperty("目标医生ID")
    private Long targetDoctorId;
}
