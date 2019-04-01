package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Customer findByName(String name);

//    @Query("select id, name from Customer")
//    List<Customer> findAllCustomer();

}