package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "sys_user_role")
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long userId;

    private Long roleId;

    private Date createTime;

    private Date updateTime;

}
