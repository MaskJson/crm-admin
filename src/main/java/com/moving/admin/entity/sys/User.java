package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private String avatar;

    @ApiModelProperty(value = "角色类型")
    private Integer roleId;

    @ApiModelProperty(value = "状态：1正常， 0禁用")
    private Integer status;

}
