package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DepartmentDao extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Department findByCustomerIdAndName(Long customerId, String name);

    List<Department> findAllByCustomerId(Long customerId);

}
