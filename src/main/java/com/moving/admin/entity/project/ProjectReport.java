package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("项目诊断报告")
@Data
@Entity
@Table(name = "project_report")
public class ProjectReport extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "诊断类型：1：提醒顾问诊断、2：诊断报告")
    private Integer type;

    @ApiModelProperty(value = "是否需要顾问处理")
    private Boolean status = false;

    @ApiModelProperty(value = "诊断备注")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private Long createUserId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "总数")
    private int allCount;

    @ApiModelProperty(value = "推荐人数")
    private int recommendCount;

    @ApiModelProperty(value = "面试人数")
    private int interviewCount;

    @ApiModelProperty(value = "offer人数")
    private int offerCount;

    @ApiModelProperty(value = "入职人数")
    private int workingCount;

    @ApiModelProperty(value = "保证期人数")
    private int qualityCount;

    @ApiModelProperty(value = "通过保证期人数")
    private int qualityPassCount;

    @ApiModelProperty(value = "跟进的经历提醒")
    @Transient
    private Long followId;

}
