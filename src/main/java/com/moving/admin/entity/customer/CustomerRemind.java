package com.moving.admin.entity.customer;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("客户跟踪")
@Data
@Entity
@Table(name = "customer_remind")
public class CustomerRemind extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "跟踪类型：1 电话， 2 拜访客户， 3 客户上门")
    private Integer type;

    @ApiModelProperty(value = "客户状态: 0: 普通公司，6：客户，2：联系中，3：合作洽谈，4：先推人再签约，5：签约")
    private Integer status;

    @ApiModelProperty(value = "沟通记录")
    private String remark;

    @ApiModelProperty(value = "下次跟踪类型：1 电话， 2 拜访客户， 3 客户上门")
    private Integer nextType;

    @ApiModelProperty(value = "下次沟通时间")
    private Date nextRemindTime;

    @ApiModelProperty(value = "见面时间")
    private Date meetTime;

    @ApiModelProperty(value = "见面地点")
    private String meetAddress;

    @ApiModelProperty(value = "见面内容")
    private String meetNotice;

    @ApiModelProperty(value = "创建者ID")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间=跟进时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否完成跟进")
    private Boolean finish = false;

    @ApiModelProperty(value = "创建者name")
    @Transient
    private String createUser;

    @ApiModelProperty(value = "跟进的客户跟踪ID")
    @Transient
    private Long followRemindId;

}
