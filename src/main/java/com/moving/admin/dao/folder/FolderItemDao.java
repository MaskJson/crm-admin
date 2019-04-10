package com.moving.admin.dao.folder;

import com.moving.admin.entity.folder.FolderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderItemDao extends JpaRepository<FolderItem, Long>, JpaSpecificationExecutor<FolderItem> {

    void removeFolderItemByFolderId(Long folderId);

    FolderItem findByItemIdAndAndFolderIdAndType(Long itemId, Long folderId, Integer type);

    List<FolderItem> findAllByFolderIdAndAndType(Long folderId, Integer type);

    @Query("select itemId from FolderItem where folderId=:folderId and type=:type")
    List<Long> findItemIds(@Param("folderId") Long folderId, @Param("type") Integer type);

    // 获取所有收藏的人才id
    @Query("select itemId from FolderItem where type=2")
    List<Long> findFolderTalentIds();

}
