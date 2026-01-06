package io.depression.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 医院表
 */
@Data
@TableName("cohort_centers")
@ApiModel(value = "CohortCenter", description = "医院/协作中心")
public class CohortCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cohort_code")
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




