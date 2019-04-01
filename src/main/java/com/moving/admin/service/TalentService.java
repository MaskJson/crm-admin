package com.moving.admin.service;

import com.moving.admin.dao.customer.CustomerDao;
import com.moving.admin.dao.customer.DepartmentDao;
import com.moving.admin.dao.talent.*;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.customer.Department;
import com.moving.admin.entity.talent.Talent;
import com.moving.admin.entity.talent.TalentRemind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class TalentService extends AbstractService {

    @Autowired
    private TalentDao talentDao;

    @Autowired
    private TalentRemindDao talentRemindDao;

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private ChanceDao chanceDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ExperienceDao experienceDao;

    @Autowired
    private CustomerDao customerDao;

    // 手机号验证重复
    public Talent checkPhone(String phone) {
        Talent talent = null;
        talent = talentDao.findTalentByPhone(phone);
        return talent;
    }

    // 添加人才
    @Transactional
    public Long save(Talent talent) {
        talent.setUpdateTime(new Date(System.currentTimeMillis()));
        talentDao.save(talent);
        Long id = talent.getId();
        experienceDao.removeAllByTalentId(id);
        talent.getExperienceList().forEach(item -> {
            item.setTalentId(id);
            experienceDao.save(item);
            Long customerId = addCustomerFromTalentInfo(item.getCompany());
            addDepartmentFromTalentInfo(customerId, item.getDepartment());
        });
        friendDao.removeAllByTalentId(id);
        talent.getFriends().forEach(friend -> {
            friend.setTalentId(id);
            friendDao.save(friend);
            Long customerId = addCustomerFromTalentInfo(friend.getCompany());
            addDepartmentFromTalentInfo(customerId, friend.getDepartment());
        });
        chanceDao.removeAllByTalentId(id);
        talent.getChances().forEach(chance -> {
            chance.setTalentId(id);
            chanceDao.save(chance);
            addCustomerFromTalentInfo(chance.getCompany());
        });
        TalentRemind remind = talent.getRemind();
        if (remind != null) {
            remind.setTalentId(id);
            talentRemindDao.save(remind);
        }
        return id;
    }

    // 添加客户，去重
    public Long addCustomerFromTalentInfo(String name) {
        Customer customer = customerDao.findByName(name);
        if (customer != null) {
            return customer.getId();
        } else {
            customer = new Customer();
            customer.setName(name);
            customer.setType(0);
            customerDao.save(customer);
            return customer.getId();
        }
    }

    // 添加部门，去重
    public void addDepartmentFromTalentInfo(Long customerId, String name) {
        Department department = departmentDao.findByCustomerIdAndName(customerId, name);
        if (department == null) {
            department = new Department();
            department.setCustomerId(customerId);
            department.setName(name);
            departmentDao.save(department);
        }
    }

    // 获取人才详情
    public Talent getTalentById(Long id) {
        Talent talent = talentDao.findById(id).get();
        if (talent != null) {
            talent.setExperienceList(experienceDao.findAllByTalentId(id));
            talent.setFriends(friendDao.findAllByTalentId(id));
            talent.setChances(chanceDao.findAllByTalentId(id));
        }
        return talent;
    }
}
