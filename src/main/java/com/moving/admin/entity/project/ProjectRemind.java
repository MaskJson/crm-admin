package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
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
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "跟踪类型")
    private Integer type;

    @ApiModelProperty(value = "改变状态为")
    private Integer status;

    @ApiModelProperty(value = "跟踪备注")
    private String remark;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "项目人才id")
    private Long projectTalentId;

    @ApiModelProperty(value = "角色id")
    @Transient
    private Integer roleId;

}
