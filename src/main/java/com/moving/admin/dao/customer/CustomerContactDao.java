package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerContactDao extends JpaRepository<CustomerContact, Long>, JpaSpecificationExecutor<CustomerContact> {
}
