package com.moving.admin.dao.folder;

import com.moving.admin.entity.folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FolderDao extends JpaRepository<Folder, Long>, JpaSpecificationExecutor<Folder> {

    List<Folder> findByType(Integer type);

}
