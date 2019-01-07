package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionDao extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {

    RolePermission findByIdIn(List<Long> rolePermissionIdList);

    @Query(" select permissionId from  RolePermission where roleId in :roleIdList")
    List<Long> findPermissionIdsByRoleIdIn(@Param("roleIdList") List<Long> roleIdList);

    @Query(" select permissionId from  RolePermission where roleId = :roleId")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

    List<RolePermission> findByPermissionId(Long id);

    void deleteByRoleId(Long roleId);

}
