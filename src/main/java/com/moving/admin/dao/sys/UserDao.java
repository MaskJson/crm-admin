package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("from User where username=:username and password=:password")
    List<User> login(@Param("username") String username, @Param("password") String password);

    User findByUsername(String userName);

    User findByRoleId(Long roleId);
}
