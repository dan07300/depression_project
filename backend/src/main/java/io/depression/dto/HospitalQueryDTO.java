package io.depression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 医院查询DTO
 */
@Data
@ApiModel(value = "HospitalQueryDTO", description = "医院查询条件")
public class HospitalQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页")
    private Integer current = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;

    @ApiModelProperty("关键字（医院名称/编码）")
    private String keyword;

    @ApiModelProperty("状态：0-禁用 1-启用")
    private Integer status;
}
