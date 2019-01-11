package com.moving.admin.service.module.customer;

import com.moving.admin.dao.module.customer.CustomerDao;
import com.moving.admin.entity.module.customer.Customer;
import com.moving.admin.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService extends AbstractService {

    @Autowired
    private CustomerDao customerDao;

    // 添加-编辑客户
    public void save(Customer customer) {
        customerDao.save(customer);
    }

    // 根据id获取客户详情
    public Customer getCustomerById(Long id) {
        return customerDao.findById(id).get();
    }

    // 根据name获取客户，check
    public Customer getCustomerByName(String name) {
        return customerDao.findByName(name);
    }

    // 按条件分页查询客户
    public Page<Customer> getCustomerList(String industry, String city, String name, Integer txnType, Pageable pageable) {
        return customerDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(industry)) {
                list.add(cb.like(root.get("industry"), super.getLikeParam(industry)));
            }
            if (!StringUtils.isEmpty(city)) {
                list.add(cb.equal(root.get("city"), city));
            }
            if (!StringUtils.isEmpty(name)) {
                list.add(cb.like(root.get("name"), super.getLikeParam(name)));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }

    // 获取收藏的客户


}
