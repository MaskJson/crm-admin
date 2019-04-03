package com.moving.admin.entity.customer;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("客户联系人备注记录")
@Data
@Entity
@Table(name = "customer_contact_remark")
public class CustomerContactRemark extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "联系人ID")
    private Long customerContactId;

    @ApiModelProperty(value = "备注内容")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "下次联系时间")
    private Date nextContactTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "创建人昵称")
    @Transient
    private String createUser;

}
