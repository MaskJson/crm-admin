package com.moving.admin.controller.sys;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.sys.RolePermissionDao;
import com.moving.admin.service.sys.PermissionService;
import com.moving.admin.util.ResultUtil;
import com.moving.admin.entity.sys.Permission;
import com.moving.admin.entity.sys.RolePermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "权限菜单")
@RestController
@RequestMapping("/sys/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    public List<RolePermission> findRolePermissionByPermissionId(Long id) {
        return rolePermissionDao.findByPermissionId(id);
    }

    @ApiOperation(value = "获取用户权限菜单")
    @GetMapping("/getMenuListByUserId")
    public Result<List> getMenuListByUserId() throws Exception {
        return ResultUtil.success(permissionService.findPermissionsOfUser(null));
    }

    @ApiOperation(value = "获取权限菜单树")
    @GetMapping("/getAllList")
    public Result<List<Permission>> getAllList() throws Exception {
        //一级
        List<Permission> list = permissionService.findByLevelOrderBySortOrder(1);
        //二级
        for (Permission p1 : list) {
            List<Permission> children1 = permissionService.findByParentIdOrderBySortOrder(p1.getId());
            p1.setChildren(children1);
        }
        return ResultUtil.success(list);
    }

    @ApiOperation(value = "添加-编辑")
    @PostMapping("/save")
    public Result<Permission> add(@RequestBody Permission permissionTemp) throws Exception {
        Permission permission = permissionService.save(permissionTemp);
        return ResultUtil.success(permission);
    }

    @DeleteMapping("/delByIds/{ids}")
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@PathVariable Long[] ids) {
        for (Long id : ids) {
            List<RolePermission> list = permissionService.findRolePermissionByPermissionId(id);
            if (list != null && list.size() > 0) {
                return ResultUtil.error("删除失败，包含正被角色使用关联的菜单或权限");
            }
        }
        for (Long id : ids) {
            permissionService.delete(id);
        }
        return ResultUtil.success(null);
    }

}
