package com.moving.admin.service.sys;

import com.moving.admin.dao.sys.PermissionDao;
import com.moving.admin.dao.sys.RolePermissionDao;
import com.moving.admin.dao.sys.UserRoleDao;
import com.moving.admin.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

import com.moving.admin.entity.sys.Permission;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService extends AbstractService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    public List<Permission> findPermissionsByUserId() {
        Long userId = super.getCurrentUserID();
        List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
        System.err.println(roleIds);
        if (roleIds != null && roleIds.size() > 0) {
            List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleIdIn(roleIds);
            if (permissionIds != null && permissionIds.size() > 0) {
                List<Permission> list = permissionDao.findByIdIn(permissionIds);
                List<Permission> menuList = new ArrayList<>();
                List<Permission> secondMenuList = new ArrayList<>();
                List<Permission> buttonPermissions = new ArrayList<>();

                if (list != null && list.size() > 0) {
                    for (Permission p : list) {
                        //筛选一级页面
                        if (p.getLevel() == 1) {
                            menuList.add(p);
                        }
                        //筛选二级页面
                        if (p.getLevel() == 2) {
                            secondMenuList.add(p);
                        }
                    }

                    if (menuList != null && menuList.size() > 0) {
                        //匹配一级页面拥有二级页面
                        for (Permission p : menuList) {
                            List<Permission> secondMenu = new ArrayList<>();
                            if (secondMenuList != null && secondMenuList.size() > 0) {
                                for (Permission pe : secondMenuList) {
                                    if (p.getId().equals(pe.getParentId())) {
                                        secondMenu.add(pe);
                                    }
                                }
                            }
                            p.setChildren(secondMenu);
                        }
                    }
                }
                return menuList;
            }
        }
        return null;
    }
}
