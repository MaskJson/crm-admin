package com.moving.admin.entity.common;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@ApiModel("收藏夹")
@Data
@Entity
@Table(name = "folder")
public class Folder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String remark;

    @ApiModelProperty(value = "状态 0启用 -1禁用")
    private Integer status;

    @ApiModelProperty(value = "类型 0客户 1人才 2项目")
    private Integer type;

}
