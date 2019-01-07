package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "sys_permission")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "页面父组件")
    @Column(nullable = false)
    private String path;

    @ApiModelProperty(value = "二级路由对应组件")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "排序值")
    @Column(precision = 10, scale = 2)
    private BigDecimal sortOrder;

    // @Transient表示不是表的字段
    @Transient
    @ApiModelProperty(value = "子菜单/权限")
    private List<Permission> children;

}
