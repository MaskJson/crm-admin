package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleDao extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByRoleId(Long roleId);

    @Query(" select roleId from UserRole where userId=:userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);

}
