package com.moving.admin.entity.talent;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ApiModel("人才")
@Data
@Entity
@Table(name = "talent")
public class Talent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "头像")
    private String header;

    @ApiModelProperty(value = "身份证号")
    private String card;

    @ApiModelProperty(value = "婚姻状况 0：未婚 1：已婚")
    private Integer marry;

    @ApiModelProperty(value = "头像 0：男 1：女")
    private Integer sex;

    @ApiModelProperty(value = "身高")
    private String height;

    @ApiModelProperty(value = "简历")
    private String resume;

    @ApiModelProperty(value = "附件")
    private String resume2;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "国籍")
    private Integer country;

    @ApiModelProperty(value = "年薪")
    private String salary;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "类型 0: 普通人才、1：专属人才")
    private Integer type;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "学校")
    private String school;

    @ApiModelProperty(value = "学历")
    private Integer education;

    @ApiModelProperty(value = "语言能力")
    private Integer language;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "意向城市")
    private String intentionCity;

    @ApiModelProperty(value = "意向职位")
    private String position;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "职能")
    private String aptness;

    @ApiModelProperty(value = "教育经历")
    @Column(length = 2048)
    private String educationExperience;

    @ApiModelProperty(value = "项目经历")
    @Column(length = 2048)
    private String projectExperience;

    @ApiModelProperty(value = "职能介绍")
    @Column(length = 2048)
    private String occupationSkill;

    @ApiModelProperty(value = "来源类型")
    private Integer sourceType;

    @ApiModelProperty(value = "来源")
    private Integer sourceFrom;

    @ApiModelProperty(value = "评价")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private Long createUserId;

    @ApiModelProperty(value = "是否关注")
    private Boolean follow;

    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private Long followUserId;

    @ApiModelProperty(value = "创建者name")
    @Transient
    private String createUser;

    @ApiModelProperty(value = "关联项目ID")
    @Transient
    private Long projectId;

    @ApiModelProperty(value = "工作经历")
    @Transient
    private List<Experience> experienceList;

    @ApiModelProperty(value = "朋友介绍")
    @Transient
    private List<Friend> friends;

    @ApiModelProperty(value = "接触机会")
    @Transient
    private List<Chance> chances;

    @ApiModelProperty(value = "跟踪记录")
    @Transient
    private TalentRemind remind;

    @ApiModelProperty(value = "最近一条待跟进跟踪")
    @Transient
    private TalentRemind followRemind;

    @ApiModelProperty(value = "关联项目数")
    @Transient
    private Long projectCount;

    @ApiModelProperty(value = "offer项目数")
    @Transient
    private Long offerCount;

    @ApiModelProperty(value = "本次操作用户id")
    @Transient
    private Long actionUserId;

    @ApiModelProperty(value = "人才处于非结束进展的项目")
    @Transient
    private List<Long> projects;

    @ApiModelProperty(value = "角色id")
    @Transient
    private Long roleId;

    @ApiModelProperty(value = "项目进展数")
    @Transient
    private Long progress;

    @ApiModelProperty(value = "推荐理由")
    @Transient
    private String recommendation;

}
