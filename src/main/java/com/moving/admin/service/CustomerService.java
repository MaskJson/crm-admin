package com.moving.admin.service;

import com.moving.admin.dao.customer.CustomerDao;
import com.moving.admin.dao.customer.CustomerRemindDao;
import com.moving.admin.dao.customer.DepartmentDao;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.dao.talent.ExperienceDao;
import com.moving.admin.dao.talent.TalentDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.customer.CustomerRemind;
import com.moving.admin.entity.customer.Department;
import com.moving.admin.entity.folder.FolderItem;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.talent.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CustomerService extends AbstractService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemindDao customerRemindDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private ExperienceDao experienceDao;

    @Autowired
    private TalentDao talentDao;

    // 添加、编辑
    public Long save(Customer customer) {
        customer.setUpdateTime(new Date(System.currentTimeMillis()));
        customerDao.save(customer);
        return customer.getId();
    }

    // 查询（id、或 name）
    public Customer getCustomerByKey(Long id, String name) {
        Customer customer = null;
        if (id != null) {
            customer = customerDao.findById(id).get();
        } else {
            customer = customerDao.findByName(name);
        }
        if (customer != null) {
            Long userId = customer.getCreateUserId();
            if (userId != null) {
                customer.setCreateUser(userDao.findById(userId).get().getNickName());
            }
        }
        return customer;
    }

    // 分页查询
    public Page<Customer> getCustomerList(Long id, String name, String industry, Long folderId, Pageable pageable) {
        Page<Customer> result = customerDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (id != null) {
                list.add(cb.equal(root.get("id"), id));
            }
            if (!StringUtils.isEmpty(name)) {
                list.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (!StringUtils.isEmpty(industry)) {
                list.add(cb.like(root.get("industry"), "%" + industry + "%"));
            }
            if (folderId != null) {
                List<FolderItem> folderItems = folderItemDao.findAllByFolderIdAndAndType(folderId, 1);
                List<Long> ids = new ArrayList<>();
                folderItems.forEach(folderItem -> {
                    ids.add(folderItem.getItemId());
                });
                System.err.println(ids.size());
                Expression<Long> exp = root.<Long>get("id");
                list.add(cb.and(exp.in(ids)));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
        return result;
    }

    // 关注装修改
    public void toggleFollow(Long id, Boolean follow) {
        Customer customer = customerDao.findById(id).get();
        customer.setFollow(follow);
        save(customer);
    }

    // 添加客户跟踪
    public Long saveRemind(CustomerRemind remind) {
        remind.setCreateTime(new Date(System.currentTimeMillis()));
        remind.setUpdateTime(new Date(System.currentTimeMillis()));
        customerRemindDao.save(remind);
        if (remind.getFollowRemindId() != null) {
            finishRemindById(remind.getFollowRemindId());
        }
        return remind.getId();
    }

    // 客户跟踪跟进结束
    public Long finishRemindById(Long id) {
        CustomerRemind customerRemind = customerRemindDao.findById(id).get();
        customerRemind.setFinish(true);
        return id;
    }

    // 获取客户跟踪记录
    public List<CustomerRemind> getAllRemind(Long customerId) {
        List<CustomerRemind> list = customerRemindDao.findAllByCustomerIdOrderByIdDesc(customerId);
        list.forEach(customerRemind -> {
            Long userId = customerRemind.getCreateUserId();
            if (userId != null) {
                User user = userDao.findById(userId).get();
                if (user != null) {
                    customerRemind.setCreateUser(user.getNickName());
                }
            }
        });
        return list;
    }

    // 获取所有客户
    public List<Customer> getAllCustomer() {
        return customerDao.findAll();
    }

    // 获取所有部门
    public List<Department> getAllDepartment() {
        return departmentDao.findAll();
    }

    // 获取该公司下所有相关人才,talentId 去重
    public List<Experience> getCustomerTalents(Long id) {
        List<Experience> experiences = experienceDao.findAllByCustomerIdOrderByDepartmentId(id);
        experiences.forEach(experience -> {
            if (experience.getTalentId() != null) {
                experience.setTalent(talentDao.findById(experience.getTalentId()).get());
            }
            if (experience.getDepartmentId() != null) {
                experience.setDepartment(departmentDao.findById(experience.getDepartmentId()).get().getName());
            }
        });
        return experiences;
    }

}
