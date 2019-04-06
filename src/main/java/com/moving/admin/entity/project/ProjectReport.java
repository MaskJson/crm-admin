package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @ApiModelProperty(value = "诊断类型：1：提醒顾问诊断、2：项目经理直接诊断")
    private Integer type;

    @ApiModelProperty(value = "是否需要顾问处理")
    private Boolean status = false;

    @ApiModelProperty(value = "诊断备注")
    private String remark;

    @ApiModelProperty(value = "是否已处理")
    private Long createUserId;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
