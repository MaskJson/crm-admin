package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Data
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private String avatar = "https://s1.ax1x.com/2018/05/19/CcdVQP.png";

    private Integer type;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "状态：0正常， -1禁用")
    private Integer status;

    private Date createTime;

    private Date updateTime;

    @Transient
    @ApiModelProperty(value = "团队ID")
    private Long teamId;

    @Transient
    @ApiModelProperty(value = "团队成员人数")
    private Long count;

    @Transient
    @ApiModelProperty(value = "用户角色")
    private Role role;

    @Transient
    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;



}
