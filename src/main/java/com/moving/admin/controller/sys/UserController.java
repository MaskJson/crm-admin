package com.moving.admin.controller.sys;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.bean.TokenInformation;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.sys.Role;
import com.moving.admin.entity.sys.UserRole;
import com.moving.admin.service.sys.RoleService;
import com.moving.admin.service.sys.UserService;
import com.moving.admin.util.JwtUtil;
import com.moving.admin.util.MD5Util;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtil jwtUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "password", value = "密码")
    })
    @ApiOperation("系统用户登录")
    @IgnoreSecurity
    @PostMapping("/login")
    public Result<Map> login(String username, String password) throws Exception {
        User user = userService.login(username, password);
        if (user!=null) {
            if (user.getStatus() == -1) {
                return ResultUtil.error("该用户已被禁用");
            }
            String token = jwtUtil.encode(new TokenInformation(user.getId()));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", user);
            map.put("token", token);
            return ResultUtil.success(map);
        } else {
            return ResultUtil.error("登录失败");
        }
    }

    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo() {
        User user = userService.findByUserId();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        user.setPassword(null);
        return ResultUtil.success(user);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "type", value = "用户类型"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "status", value = "用户状态"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "startDate", value = "创建开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endDate", value = "创建结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "size", value = "每页条数")
    })
    @ApiOperation(value = "系统用户分页查询")
    @GetMapping("getByCondition")
    public Result<Page<User>> getByCondition(User user, String startDate, String endDate, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC)Pageable pageable) {
        Page<User> page = userService.getUserByPage(user, startDate, endDate, pageable);
        for (User u : page.getContent()) {
            // 关联角色
            List<Role> list = roleService.findRolesByUserId(u.getId());
            u.setRoles(list);
            // 清除持久上下文环境 避免后面语句导致持久化
            entityManager.clear();
            u.setPassword(null);
        }
        return ResultUtil.success(page);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public Result<User> add(@RequestBody User mgrUserTemp) {
        if (StringUtils.isEmpty(mgrUserTemp.getUsername()) || StringUtils.isEmpty(mgrUserTemp.getPassword())
                || mgrUserTemp.getRoles() == null) {
            return ResultUtil.error("确实必要表单字段");
        }

        User byUsername = userService.findByUsername(mgrUserTemp.getUsername());
        if (byUsername != null) {
            return ResultUtil.error("该用户名已被注册");
        }

        String encryptPass = MD5Util.getMD5String(mgrUserTemp.getPassword());
        mgrUserTemp.setPassword(encryptPass);
        mgrUserTemp.setCreateTime(new Date());
        mgrUserTemp.setUpdateTime(null);
        User mgrUser = userService.save(mgrUserTemp);
        if (mgrUser == null) {
            return ResultUtil.error("添加失败");
        }
        if (mgrUserTemp.getRoles() != null && mgrUserTemp.getRoles().size() > 0) {
            //添加角色
            for (Role role : mgrUserTemp.getRoles()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(mgrUser.getId());
                userRole.setRoleId(role.getId());
                roleService.saveUserRole(userRole);
            }
        }
        return ResultUtil.success(mgrUser);
    }

    @ApiOperation(value = "修改资料", notes = "需要通过id获取原用户信息 需要username更新缓存")
    @Transactional
    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getUsername()) || user.getRoles() == null) {
            return ResultUtil.error("缺少必需表单字段:用户名、角色");
        }
        User oldUser = userService.get(user.getId());
        //所修改了用户名
        if (!oldUser.getUsername().equals(user.getUsername())) {
            //判断新用户名是否存在
            if (userService.findByUsername(user.getUsername()) != null) {
                return ResultUtil.error("该用户名已被存在");
            }
        }
        user.setCreateTime(oldUser.getCreateTime());
        user.setUpdateTime(new Date());
        User mgrUser = userService.update(user);
        if (mgrUser == null) {
            return ResultUtil.error("修改失败");
        }
        //删除该用户角色
        roleService.deleteRolesByUserId(user.getId());
        if (user.getRoles() != null && user.getRoles().size() > 0) {
            //新角色
            for (Role role : user.getRoles()) {
                UserRole ur = new UserRole();
                ur.setRoleId(role.getId());
                ur.setUserId(user.getId());
                roleService.saveUserRole(ur);
            }
        }
        //手动删除缓存
        return ResultUtil.success(null);
    }
}
