package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.CustomerContactRemark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerContactRemarkDao extends JpaRepository<CustomerContactRemark, Long>, JpaSpecificationExecutor<CustomerContactRemark> {
}
