package com.moving.admin.entity.talent;

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

@ApiModel("工作经历")
@Data
@Entity
@Table(name = "experience")
public class Experience extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "至今")
    private Boolean status;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "业绩")
    private String performance;

    @ApiModelProperty(value = "评价")
    private String remark;

    @ApiModelProperty(value = "人才ID")
    private Long talentId;

}
