package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentDao extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Department findByCustomerIdAndName(Long customerId, String name);

}
