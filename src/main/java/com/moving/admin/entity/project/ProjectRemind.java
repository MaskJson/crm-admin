package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import com.moving.admin.entity.talent.TalentRemind;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("项目人才跟踪记录")
@Data
@Entity
@Table(name = "project_remind")
public class ProjectRemind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "跟踪类型")
    private Integer type;

    @ApiModelProperty(value = "是否为跟踪备注")
    private Boolean remind = false;

    @ApiModelProperty(value = "改变状态为")
    private Integer status;

    @ApiModelProperty(value = "上次状态")
    private Integer prevStatus;

    @ApiModelProperty(value = "上次跟踪类型")
    private Integer prevType;

    @ApiModelProperty(value = "跟踪备注")
    private String remark;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "项目人才id")
    private Long projectTalentId;

    // 特定状态下的乱七八糟字段
    // 推荐
    @ApiModelProperty(value = "推荐理由")
    private String recommendation;
    // 推荐
    @ApiModelProperty(value = "淘汰理由")
    private String killRemark;
    // 面试
    @ApiModelProperty(value = "面试时间")
    private Date interviewTime;
    @ApiModelProperty(value = "提醒类型")
    private Integer remindType;
    @ApiModelProperty(value = "面试官")
    private String interviewTone;
    @ApiModelProperty(value = "是否是终试")
    private Boolean isLast;

    // 签订offer
    @ApiModelProperty(value = "岗位")
    private String position;
    @ApiModelProperty(value = "年薪")
    private double yearSalary;
    @ApiModelProperty(value = "收费")
    private double charge;
    @ApiModelProperty(value = "确认日期")
    private Date sureTime;
    @ApiModelProperty(value = "预计上班时间")
    private Date workTime;

    // 入职
    @ApiModelProperty(value = "入职时间")
    private Date entryTime;
    @ApiModelProperty(value = "试用期结束")
    private Date probationTime;

    // 反馈
    @ApiModelProperty(value = "人才反馈")
    private String talentRemark;
    @ApiModelProperty(value = "客户反馈")
    private String customerRemark;
    @ApiModelProperty(value = "反馈状态")
    private Integer remarkStatus;

    @ApiModelProperty(value = "淘汰跟进状态")
    @Transient
    private Integer killStatus;

    @ApiModelProperty(value = "淘汰跟进状态")
    @Transient
    private TalentRemind talentRemind;

    @ApiModelProperty(value = "角色id")
    @Transient
    private Integer roleId;

}
