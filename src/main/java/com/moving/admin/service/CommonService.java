package com.moving.admin.service;

import com.moving.admin.dao.customer.DepartmentDao;
import com.moving.admin.entity.customer.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService extends AbstractService {

    @Autowired
    private DepartmentDao departmentDao;

    // 添加部门，去重
    public Long addDepartmentFromTalentInfo(Long customerId, String name) {
        Department department = departmentDao.findByCustomerIdAndName(customerId, name);
        if (department == null) {
            department = new Department();
            department.setCustomerId(customerId);
            department.setName(name);
            departmentDao.save(department);
        }
        return department.getId();
    }

}
