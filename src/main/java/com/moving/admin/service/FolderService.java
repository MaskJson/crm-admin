package com.moving.admin.service;

import com.moving.admin.dao.folder.FolderDao;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.entity.folder.Folder;
import com.moving.admin.entity.folder.FolderItem;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FolderService extends AbstractService {

    @Autowired
    FolderDao folderDao;

    @Autowired
    FolderItemDao folderItemDao;

    // 添加-保存
    public Long save(Folder folder) {
        folderDao.save(folder);
        return folder.getId();
    }

    // 列表
    public List<Folder> list(Integer type) {
        return folderDao.findByType(type);
    }

    // 删除
    @Transactional
    public void remove(Long id) {
        folderDao.deleteById(id);
        folderItemDao.removeFolderItemByFolderId(id);
    }

    // 启用-禁用
    @Transactional
    public void toggle(Long id, Boolean status) {
        Folder folder = folderDao.findById(id).get();
        folder.setStatus(status);
        save(folder);
    }

    public Long bind(FolderItem folderItem) {
        FolderItem f = folderItemDao.findByItemIdAndAndFolderIdAndType(folderItem.getItemId(), folderItem.getFolderId(), folderItem.getType());
        if (f != null) {
            return f.getId();
        } else {
            folderItemDao.save(folderItem);
            return folderItem.getId();
        }
    }

}
