package io.depression.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 评定项目配置
 */
@Data
@TableName("assessment_config")
@ApiModel(value = "AssessmentConfig", description = "评定项目配置")
public class AssessmentConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("评定类型标识")
    private String assessmentType;

    @ApiModelProperty("评定名称")
    private String assessmentName;

    @ApiModelProperty("分类")
    private String category;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否有评分")
    private Integer isScored;

    @ApiModelProperty("排序")
    private Integer sortOrder;

    @ApiModelProperty("状态")
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




