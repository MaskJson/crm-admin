package com.moving.admin.entity.module.customer;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "customer")
@ApiModel("客户")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "公司网址")
    private String website;

    @ApiModelProperty(value = "公司规模")
    private String scale;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "福利")
    private String welfare;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "合同")
    private String contractUrl;

    @ApiModelProperty(value = "是否关注： 0非关注，1关注")
    private Integer isFollow;

    @ApiModelProperty(value = "类型：0重点客户 1试单客户 2准备签约 3电话沟通 4准备拜访 5无意向 6黑名单")
    private Integer type;

    @ApiModelProperty(value = "录入类型 0人才录入 1签约")
    private Integer inType = 0;

    @ApiModelProperty(value = "创建人id")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
