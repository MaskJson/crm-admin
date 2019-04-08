package com.moving.admin.service;

import com.moving.admin.dao.customer.DepartmentDao;
import com.moving.admin.dao.natives.CommonNative;
import com.moving.admin.entity.customer.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService extends AbstractService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private CommonNative commonNative;

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

    // 获取单表所有数据 id + name
    public List<Map<String, Object>> getListByTableName(Integer type, String name) {
        String tableName = null;
        switch (type) {
            case 1: tableName = "customer";break; // 所有客户
            case 2: tableName = "talent";break; // 所有人才
            case 3: tableName = "project";break; // 所有项目
            case 4: tableName = "sys_user";break; // 所有兼职
            case 5: tableName = "team";break; // 所有团队
        }
        if (tableName == null) {
            return null;
        }
        return commonNative.getListByTableName(tableName, name);
    }
}
