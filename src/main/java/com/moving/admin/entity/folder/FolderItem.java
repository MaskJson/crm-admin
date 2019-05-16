package com.moving.admin.entity.folder;

import com.moving.admin.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ApiModel("收藏夹")
@Data
@Entity
@Table(name = "folder_item")
public class FolderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "实体类ID")
    private Long itemId;

    @ApiModelProperty(value = "收藏夹ID")
    private Long folderId;

    @ApiModelProperty(value = "类型：1-客户 2-人才 3-项目")
    private Integer type;

}
