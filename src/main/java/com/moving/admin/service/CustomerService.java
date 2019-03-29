package com.moving.admin.service;

import com.moving.admin.dao.CustomerDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.util.DateUtil;
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
        return customer;
    }

    // 分页查询
    public Page<Customer> getCustomerList(Long id, String name, String industry, Long folderId, Pageable pageable) {
        return customerDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

//            if (!StringUtils.isEmpty(user.getUsername())) {
//                list.add(cb.equal(root.get("username"), user.getUsername()));
//            }
//            if (user.getType() != null) {
//                list.add(cb.equal(root.get("type"), user.getType()));
//            }
//            if (user.getStatus() != null) {
//                list.add(cb.equal(root.get("status"), user.getStatus()));
//            }
//            if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
//                Date start = DateUtil.strToDate(startDate);
//                Date end = DateUtil.strToDate(endDate);
//                list.add(cb.between(root.get("createTime").as(Date.class), start, end));
//            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }
}
