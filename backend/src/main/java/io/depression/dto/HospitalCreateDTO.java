package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建医院DTO
 */
@Data
@ApiModel(value = "HospitalCreateDTO", description = "创建医院请求")
public class HospitalCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "医院编码", required = true)
    @NotBlank(message = "医院编码不能为空")
    private String cohortCode;

    @ApiModelProperty(value = "医院名称", required = true)
    @NotBlank(message = "医院名称不能为空")
    private String cohortName;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("联系人")
    private String contactPerson;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status = 1;
}
