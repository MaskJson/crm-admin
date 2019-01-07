package com.moving.admin.dao;

import com.moving.admin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TestDao extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	/**
	 * 获取用户的订单总数
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT count(1) FROM orders WHERE user_id=?1", nativeQuery = true)
	Integer getCountByUserId(int userId);
}
