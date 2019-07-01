package com.moving.admin.entity.talent;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("人才跟踪")
@Data
@Entity
@Table(name = "talent_remind")
public class TalentRemind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "人才ID")
    private Long talentId;

    @ApiModelProperty(value = "跟踪类型：1：电话 2：顾问面试内 3：顾问面试外")
    private Integer type;

    @ApiModelProperty(value = "下次跟踪类型")
    private Integer nextType;

    @ApiModelProperty(value = "下次沟通时间")
    private Date nextRemindTime;

    @ApiModelProperty(value = "人才状态")
    private Integer status;

    @ApiModelProperty(value = "跟踪记录")
    @Column(length = 4096)
    private String remark;

    @ApiModelProperty(value = "候选人基本情况")
    @Column(length = 4096)
    private String situation;

    @ApiModelProperty(value = "离职原因")
    private String cause;

    @ApiModelProperty(value = "薪资结构")
    private String salary;

    @ApiModelProperty(value = "见面时间")
    private Date meetTime;

    @ApiModelProperty(value = "见面地点")
    private String meetAddress;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "跟进时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    @ApiModelProperty(value = "是否完成跟进")
    private Boolean finish = false;

    @ApiModelProperty(value = "是否完成跟进")
    private Long customerId;

    @ApiModelProperty(value = "创建者name")
    @Transient
    private String createUser;

    @ApiModelProperty(value = "跟进的客户跟踪ID")
    @Transient
    private Long followRemindId;

    @ApiModelProperty(value = "推荐给客户")
    @Transient
    private Long projectId;

    @ApiModelProperty(value = "角色id")
    @Transient
    private Long roleId;

    @ApiModelProperty(value = "淘汰跟进的进展人才id")
    @Transient
    private Long projectTalentId;

}
