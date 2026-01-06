package io.depression.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 医院VO
 */
@Data
@ApiModel(value = "HospitalVO", description = "医院视图对象")
public class HospitalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医院编码")
    private String cohortCode;

    @ApiModelProperty("医院名称")
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
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
