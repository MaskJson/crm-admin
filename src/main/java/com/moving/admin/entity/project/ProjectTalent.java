package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("项目人才")
@Data
@Entity
@Table(name = "project_talent")
public class ProjectTalent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "人才id")
    private Long talentId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    // 特殊状态  100 候选审核  200 保护性淘汰
    @ApiModelProperty(value = "人才跟踪状态")
    private Integer type;

    @ApiModelProperty(value = "人才进展状态")
    private Integer status;

    @ApiModelProperty(value = "创建人时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "修改时间=最后跟踪时间")
    private Date updateTime;

    @ApiModelProperty(value = "反馈状态")
    private Integer remarkStatus;

    @ApiModelProperty(value = "试用期结束")
    private Date probationTime;

    @ApiModelProperty(value = "推荐理由")
    private String recommendation;

    @ApiModelProperty(value = "淘汰理由")
    private String killRemark;

    @ApiModelProperty(value = "角色id")
    @Transient
    private String remark;

    @ApiModelProperty(value = "角色id")
    @Transient
    private Integer roleId;

}
