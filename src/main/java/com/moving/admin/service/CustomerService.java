package com.moving.admin.service;

import com.moving.admin.dao.customer.CustomerDao;
import com.moving.admin.dao.customer.CustomerRemindDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.customer.CustomerRemind;
import com.moving.admin.entity.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            User user = userDao.findById(customer.getCreateUserId()).get();
            customer.setCreateUser(user.getNickName());
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
                list.add(cb.like(root.get("name"), industry + industry + "%"));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
        if (folderId != null) {
//            result.filter(customer -> {
//
//            });
        }
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
        return remind.getId();
    }

    // 客户跟踪跟进结束
    public Long finishRemindById(Long id) {
        CustomerRemind customerRemind = customerRemindDao.findById(id).get();
        customerRemind.setFinish(true);
        saveRemind(customerRemind);
        return id;
    }
}
