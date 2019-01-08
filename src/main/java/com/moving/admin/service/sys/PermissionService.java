package com.moving.admin.service.sys;

import com.moving.admin.dao.sys.PermissionDao;
import com.moving.admin.dao.sys.RolePermissionDao;
import com.moving.admin.dao.sys.UserRoleDao;
import com.moving.admin.entity.sys.RolePermission;
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

    // 根据层级获取菜单
    public List<Permission> findByLevelOrderBySortOrder(Integer level) {
        return permissionDao.findByLevelOrderBySortOrder(level);
    }

    // 获取角色
    public List<RolePermission> findRolePermissionByPermissionId(Long id) {
        return rolePermissionDao.findByPermissionId(id);
    }

    // 根据角色获取菜单
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleId(roleId);
        if (permissionIds != null && permissionIds.size() > 0) {
            List<Permission> permissions = permissionDao.findByIdIn(permissionIds);
            return permissions;
        }
        return null;
    }

    // 根据父级菜单id获取菜单
    public List<Permission> findByParentIdOrderBySortOrder(Long parentId) {
        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    // 获取登录用户的菜单
    public List<Permission> findPermissionsByUserId() {
        Long userId = super.getCurrentUserID();
        List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
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
                        //筛选二级页面拥有的按钮权限
                        if (p.getLevel() == 3) {
                            buttonPermissions.add(p);
                        }
                    }

                    //匹配二级页面拥有权限
                    if (secondMenuList != null && secondMenuList.size() > 0) {
                        for (Permission p : secondMenuList) {
                            List<String> permTypes = new ArrayList<>();
                            if (buttonPermissions != null && buttonPermissions.size() > 0) {
                                for (Permission pe : buttonPermissions) {
                                    if (p.getId().equals(pe.getParentId())) {
                                        permTypes.add(pe.getTitle());
                                    }
                                }
                            }
                            p.setPermTypes(permTypes);
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

    // 添加
    public Permission save(Permission permission) {
        return permissionDao.save(permission);
    }

    // 删除
    public void delete(Long id) {
        permissionDao.deleteById(id);
    }

    public void deleteByRoleId(Long roleId) {
        rolePermissionDao.deleteByRoleId(roleId);
    }

    public RolePermission saveRolePermission(RolePermission rolePermission) {
        return rolePermissionDao.save(rolePermission);
    }
}
