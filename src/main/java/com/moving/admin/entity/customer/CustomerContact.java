package com.moving.admin.entity.customer;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ApiModel("客户联系人")
@Data
@Entity
@Table(name = "customer_contact")
public class CustomerContact extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    private Long customerId;

    private Long departmentId;

    private String name;

    private String position;

    @ApiModelProperty(value = "重要性：1：非常重要 2：重要 3：一般 4：较弱 5：离职")
    private Integer important;

    private String phone;

    private Long createUserId;

    private Date createTime = new Date(System.currentTimeMillis());

    private Date updateTime;

    @ApiModelProperty(value = "部门")
    @Transient
    private String department;

}
