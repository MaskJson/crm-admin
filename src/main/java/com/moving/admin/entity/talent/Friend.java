package com.moving.admin.entity.talent;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ApiModel("朋友介绍")
@Data
@Entity
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long talentId;

    private String name;

    private Long customerId;

    private Long departmentId;

    @Transient
    private String company;

    @Transient
    private String department;

    private String phone;

}
