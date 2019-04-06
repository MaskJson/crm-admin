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
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "人才id")
    private Long talentId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "人才进展状态")
    private Integer status;

    @ApiModelProperty(value = "创建人时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

}
