package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ApiModel("项目顾问")
@Data
@Entity
@Table(name = "project_adviser")
public class ProjectAdviser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long userId;

}
