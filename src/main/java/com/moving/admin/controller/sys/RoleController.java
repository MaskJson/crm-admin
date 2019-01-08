package com.moving.admin.controller.sys;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.entity.Order;
import com.moving.admin.service.sys.PermissionService;
import com.moving.admin.service.sys.RoleService;
import com.moving.admin.service.sys.UserService;
import com.moving.admin.bean.Result;
import com.moving.admin.entity.sys.Role;
import com.moving.admin.entity.sys.UserRole;
import com.moving.admin.entity.sys.RolePermission;
import com.moving.admin.entity.sys.Permission;
import com.moving.admin.util.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(description = "菜单/权限管理接口")
@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取全部角色")
    @GetMapping("/getAllList")
    public Result<Object> roleGetAll() throws Exception {
        List<Role> list = roleService.getAll();
        return ResultUtil.success(list);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "page", value = "页码"),
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "size", value = "每页条数")
    })
    @ApiOperation("角色分页查询")
    @GetMapping("/getRoleByPage")
    public Result<Page<Role>> test(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        Page<Role> page = roleService.findByCondition(pageable);
        for (Role role : page.getContent()) {
            List<Permission> permissions = permissionService.findPermissionsByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        return ResultUtil.success(page);
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/add")
    public Result<Role> add(@RequestBody Role roleTemp) throws Exception {
        roleTemp.setCreateTime(new Date());
        Role role = roleService.save(roleTemp);
        return ResultUtil.success(role);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "id"),
        @ApiImplicitParam(paramType = "query", dataType = "String", name = "roleName", value = "角色名称"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "roleName", value = "角色描述")
    })
    @ApiOperation(value = "更新角色")
    @PostMapping("/edit")
    public Result<Role> edit(@RequestBody Role roleTemp) {
        roleTemp.setUpdateTime(new Date());
        Role role = roleService.save(roleTemp);
        return ResultUtil.success(role);
    }

    @ApiOperation(value = "批量通过ids删除")
    @Transactional
    @DeleteMapping("/delAllByIds/{ids}")
    public Result delByIds(@PathVariable Long[] ids) {
        for (Long id : ids) {
            List<UserRole> list = roleService.findUserRoleByRoleId(id);
            if (list != null && list.size() > 0) {
                return ResultUtil.error("删除失败，包含正被用户使用关联的角色");
            }
        }
        for (Long id : ids) {
            roleService.delete(id);
            //删除关联权限
            permissionService.deleteByRoleId(id);
        }
        return ResultUtil.success(null);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "roleId", value = "角色id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "permIds", value = "菜单ids")
    })
    @ApiOperation(value = "编辑角色分配权限")
    @PostMapping("/editRolePerm")
    public Result editRolePerm(Long roleId, String permIds) {
        //删除其关联权限
        List<Long> ids = new ArrayList();
        String[] strings = permIds.split(",");
        for (int j = 0; j < strings.length; j++) {
            ids.add(Long.parseLong(strings[j]));
        }
        permissionService.deleteByRoleId(roleId);
        //分配新权限
        for (Long permId : ids) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            permissionService.saveRolePermission(rolePermission);
        }
        return ResultUtil.success(null);
    }
}
