package com.moving.admin.service.sys;

import com.moving.admin.entity.sys.Role;
import com.moving.admin.dao.sys.RoleDao;
import com.moving.admin.service.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleService extends AbstractService {

    @Autowired
    private RoleDao roleDao;

    public List<Role> getAll() {
        return roleDao.findAll();
    }

    public Page<Role> getPage(Pageable pageable) {
        return roleDao.findAll(pageable);
    }

    public Role save(Role role) {
        return roleDao.save(role);
    }

    public Role update(Role roleTemp) {
        return roleDao.save(roleTemp);
    }

    public Role update(Long id, String roleName, String description) {
        Role role = roleDao.findById(id).get();
        role.setUpdateTime(new Date());
        role.setRoleName(roleName);
        role.setDescription(description);
        return roleDao.save(role);
    }

    public void delete(Long id) {
        roleDao.deleteById(id);
    }

    public Role getRole(Long id) {
        return roleDao.findById(id).get();
    }

    public Page<Role> findByCondition(Pageable pageable) {
        return roleDao.findAll((Specification<Role>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }

}
