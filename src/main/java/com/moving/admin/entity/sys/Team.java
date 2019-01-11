package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@ApiModel("团队")
@Data
@Entity
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "客户总监id")
    private Long cpId;

    @ApiModelProperty(value = "项目经理id")
    private Long pmId;

    @ApiModelProperty(value = "项目顾问id")
    private Long plId;

    @ApiModelProperty(value = "关联项目经理id")
    private Long parentPmId;

    @ApiModelProperty(value = "关联总监id")
    private Long parentCpId;

}
