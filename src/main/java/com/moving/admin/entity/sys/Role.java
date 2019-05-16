package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "sys_role")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String description;

    @Transient
    @ApiModelProperty(value = "拥有权限")
    private List<Permission> permissions;

}
