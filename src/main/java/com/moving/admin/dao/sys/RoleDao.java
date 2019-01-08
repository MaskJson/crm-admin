package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    List<Role> findByIdIn(List<Long> roleIdList);
}
