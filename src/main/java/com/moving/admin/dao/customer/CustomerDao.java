package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Customer findByName(String name);

    @Query(value = "select count(1) from customer where follow_user_id = ?", nativeQuery = true)
    Integer getCountByFollowUserId(Long userId);

    List<Customer> findAllByTypeAfterAndFollowUserIdIn(Integer type, List<Long> ids);

    // 获取所有相关客户，createUserId - followUserId
    List<Customer> findAllByCreateUserIdOrFollowUserId(Long createUserId, Long followUserId);

    // 根据id和name排重
    List<Customer> findAllByNameLikeAndIdIsNot(String name, Long id);

}
