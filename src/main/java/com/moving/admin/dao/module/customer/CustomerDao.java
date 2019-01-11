package com.moving.admin.dao.module.customer;

import com.moving.admin.entity.module.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    List<Customer> findByIdIn(List<Long> idList);

    Customer findByName(String name);

}
