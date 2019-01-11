package com.moving.admin.entity.common;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@ApiModel("行业")
@Data
@Entity
@Table(name = "industry")
public class Industry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private Long parentId;

    @Transient
    private List<Industry> industryList;

}
