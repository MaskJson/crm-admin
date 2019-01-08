package com.moving.admin.service.sys;

import com.moving.admin.dao.sys.UserRoleDao;
import com.moving.admin.entity.sys.Role;
import com.moving.admin.entity.sys.UserRole;
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

    @Autowired
    private UserRoleDao userRoleDao;

    public List<Role> findRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
        if (roleIds != null && roleIds.size() > 0) {
            List<Role> roles = roleDao.findByIdIn(roleIds);
            return roles;
        }
        return null;
    }

    public List<UserRole> findUserRoleByRoleId(Long roleId) {
        return userRoleDao.findByRoleId(roleId);
    }

    public List<Role> getAll() {
        return roleDao.findAll();
    }

    public void saveUserRole(UserRole userRole) {
        userRoleDao.save(userRole);
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

    public void deleteRolesByUserId(Long id) {
        userRoleDao.deleteByUserId(id);
    }

    public Page<Role> findByCondition(Pageable pageable) {
        return roleDao.findAll((Specification<Role>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }

}
