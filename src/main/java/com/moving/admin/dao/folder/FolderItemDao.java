package com.moving.admin.dao.folder;

import com.moving.admin.entity.folder.FolderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FolderItemDao extends JpaRepository<FolderItem, Long>, JpaSpecificationExecutor<FolderItem> {

    void removeFolderItemByFolderId(Long folderId);

    FolderItem findByItemIdAndAndFolderIdAndType(Long itemId, Long folderId, Integer type);

}
