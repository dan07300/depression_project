package io.depression.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者表
 */
@Data
@TableName("patients")
@ApiModel(value = "Patient", description = "患者信息（含账号字段）")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("自增ID")
    private Long id;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("登录用户名")
    private String username;

    @ApiModelProperty("登录密码（BCrypt哈希）")
    private String password;

    @ApiModelProperty("患者编号")
    private String patientCode;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别：0-女 1-男")
    private Integer gender;

    @ApiModelProperty("出生日期")
    private LocalDate birthday;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("民族")
    private String ethnicity;

    @ApiModelProperty("身高(CM)")
    private BigDecimal height;

    @ApiModelProperty("体重(KG)")
    private BigDecimal weight;

    @ApiModelProperty("利手：0-左手 1-右手")
    private Integer handedness;

    @ApiModelProperty("籍贯")
    private String birthPlace;

    @ApiModelProperty("工作/学习地")
    private String workPlace;

    @ApiModelProperty("居住类型：1-城市 2-城镇 3-农村")
    private Integer residenceType;

    @ApiModelProperty("学历")
    private String education;

    @ApiModelProperty("职业")
    private String occupation;

    @ApiModelProperty("婚姻状态")
    private Integer maritalStatus;

    @ApiModelProperty("情感状态")
    private Integer emotionalStatus;

    @ApiModelProperty("童年是否与父母分离：0-否 1-是")
    private Integer childhoodSeparation;

    @ApiModelProperty("家庭成员人数")
    private Integer familyMemberCount;

    @ApiModelProperty("家庭在职人数")
    private Integer familyWorkingCount;

    @ApiModelProperty("家庭年收入(万元)")
    private BigDecimal familyAnnualIncome;

    @ApiModelProperty("个人收入(元/月)")
    private BigDecimal personalIncome;

    @ApiModelProperty("紧急联系人")
    private String emergencyContact;

    @ApiModelProperty("紧急联系电话")
    private String emergencyPhone;

    @ApiModelProperty("紧急联系人关系")
    private String emergencyRelation;

    @ApiModelProperty("所属医院编码")
    private String cohortCode;

    @ApiModelProperty("负责医生ID")
    private Long doctorId;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("确诊日期")
    private LocalDate diagnosisDate;

    @ApiModelProperty("病程（月）")
    private Integer diseaseCourse;

    @ApiModelProperty("发作次数")
    private Integer episodeCount;

    @ApiModelProperty("是否住院：0-否 1-是")
    private Integer isHospitalized;

    @ApiModelProperty("是否首发：0-否 1-是")
    private Integer isFirstEpisode;

    @ApiModelProperty("是否签署知情同意书：0-否 1-是")
    private Integer signedConsent;

    @ApiModelProperty("既往病史")
    private String medicalHistory;

    @ApiModelProperty("家族史")
    private String familyHistory;

    @ApiModelProperty("过敏史")
    private String allergyHistory;

    @ApiModelProperty("当前用药")
    private String currentMedication;

    @ApiModelProperty("入组时间")
    private LocalDate enrollDate;

    @ApiModelProperty("入组/排除标准JSON")
    private String enrollmentCriteria;

    @ApiModelProperty("是否首次筛查：0-否 1-是")
    private Integer isFirstScreening;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("是否复发：0-否 1-是")
    private Integer isRelapsed;

    @ApiModelProperty("复发时间")
    private LocalDate relapseDate;

    @ApiModelProperty("复发次数")
    private Integer relapseCount;

    @ApiModelProperty("状态：0-已排除 1-正常 2-已出组 3-失访 4-康复")
    private Integer status;

    @ApiModelProperty("排除原因")
    private String excludeReason;

    @ApiModelProperty("备注")
    private String notes;

    @TableLogic
    @ApiModelProperty("逻辑删除标记")
    private Integer deleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}

