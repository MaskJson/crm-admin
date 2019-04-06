package com.moving.admin.entity.sys;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@ApiModel("团队")
@Data
@Entity
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(name = "id_generator", strategy = "identity")
    private Long id;

    private Long userId;

    @ApiModelProperty(value = "成员等级：1：总监、2：项目经理、3：高级顾问、4：中级顾问、5：兼职 --- 中级顾问和兼职没有下级，下级只能为顾问和助理")
    private Integer level;

    private Long parentId;

    private Long teamId;

    @Transient
    @ApiModelProperty(value = "项目经理列表")
    private List<Map<String, Object>> pms;

    @Transient
    @ApiModelProperty(value = "高级顾问列表")
    private List<Map<String, Object>> ipls;

    @Transient
    @ApiModelProperty(value = "中级顾问列表")
    private List<Long> mpls;

    @Transient
    @ApiModelProperty(value = "兼职列表")
    private List<Long> pts;

}
