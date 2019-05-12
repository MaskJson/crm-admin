package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@ApiModel("绩效报告")
@Data
@Entity
@Table(name = "report")
public class Report extends BaseEntity {


    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    // 类型 1 日报 2 周报 3 月报
    private Integer type;

    private Integer userId;

    private String content;

    private Date createTime;

}
