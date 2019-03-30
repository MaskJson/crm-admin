package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.CustomerRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRemindDao  extends JpaRepository<CustomerRemind, Long>, JpaSpecificationExecutor<CustomerRemind> {
}
