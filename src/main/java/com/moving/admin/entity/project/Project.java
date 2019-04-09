package com.moving.admin.entity.project;

import com.moving.admin.entity.BaseEntity;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel("项目")
@Data
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    @ApiModelProperty(value = "关联客户")
    private Long customerId;

    @ApiModelProperty(value = "关联客户的部门")
    private Long departmentId;

    @ApiModelProperty(value = "是否关注")
    private Boolean follow;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "匹配条件")
    private String matches;

    @ApiModelProperty(value = "招聘数量")
    private Long amount;

    @ApiModelProperty(value = "项目费用")
    private double fee;

    @ApiModelProperty(value = "优先级：1：高 2：中 3：低")
    private Integer priority;

    @ApiModelProperty(value = "首推时间")
    private Date firstApplyTime;

    @ApiModelProperty(value = "计划完成时间")
    private Date finishTime;

    @ApiModelProperty(value = "保证期 1：一个月、2：两个月、3：三个月、4：六个月、5：转正时、6：低于一个月")
    private Integer qualityType;

    @ApiModelProperty(value = "开放属性")
    private Integer openType;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "职能")
    private String aptness;

    @ApiModelProperty(value = "名企背景")
    private Integer background;

    @ApiModelProperty(value = "国籍")
    private Integer country;

    @ApiModelProperty(value = "学历")
    private Integer education;

    @ApiModelProperty(value = "语言能力")
    private Integer language;

    @ApiModelProperty(value = "起始年龄")
    private Integer ageStart;

    @ApiModelProperty(value = "截止年龄")
    private Integer ageEnd;

    @ApiModelProperty(value = "起始工作年限")
    private Integer yearStart;

    @ApiModelProperty(value = "截止工作年限")
    private Integer yearEnd;

    @ApiModelProperty(value = "起始年薪")
    private double salaryStart;

    @ApiModelProperty(value = "截止年薪")
    private double salaryEnd;

    @ApiModelProperty(value = "年龄要求：1：无要求、2：男、3：女")
    private Integer sex;

    @ApiModelProperty(value = "是否保密")
    private Integer isSecrecy;

    @ApiModelProperty(value = "是否独家")
    private Integer isOneself;

    @ApiModelProperty(value = "新增或替代岗位")
    private Integer isNew;

    @ApiModelProperty(value = "职责和任职资格")
    private String situation;

    @ApiModelProperty(value = "职位亮点")
    private String advantage;

    @ApiModelProperty(value = "面试流程")
    private String introduce;

    @ApiModelProperty(value = "薪资架构")
    private String structure;

    @ApiModelProperty(value = "寻访方向")
    private String visit;

    @ApiModelProperty(value = "客户在该领域带情况")
    private String actuality;

    @ApiModelProperty(value = "项目总监")
    private Long teamId;

    @ApiModelProperty(value = "兼职人id")
    private Long partId;

    @ApiModelProperty(value = "创建者")
    private Long createUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "部门名称")
    @Transient
    private String department;

}
