package com.moving.admin.controller.sys;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.bean.TokenInformation;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.dao.natives.TeamNative;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.sys.Role;
import com.moving.admin.service.sys.RoleService;
import com.moving.admin.service.sys.UserService;
import com.moving.admin.util.JwtUtil;
import com.moving.admin.util.MD5Util;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "系统用户管理")
@RestController
@RequestMapping("/sys/user")
public class UserController extends AbstractController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private TeamNative teamNative;

    @PersistenceContext
    private EntityManager entityManager;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "password", value = "密码")
    })
    @ApiOperation("系统用户登录")
    @PostMapping("/login")
    @IgnoreSecurity
    public Result<Map> login(String username, String password) throws Exception {
        User user = userService.login(username, password);
        if (user!=null) {
            if (!user.getStatus()) {
                return ResultUtil.error("该用户已被禁用");
            }
            String token = jwtUtil.encode(new TokenInformation(user.getId(), user.getRoleId()));
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
    public Result<Page<User>> getByCondition(String username, Integer type, String startDate, String endDate, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable) {
        Page<User> page = userService.getUserByPage(username, type, startDate, endDate, pageable);
        for (User u : page.getContent()) {
            Role role = roleService.getRole(u.getRoleId());
            u.setRole(role);
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
                || mgrUserTemp.getRoleId() == null) {
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
        return ResultUtil.success(mgrUser);
    }

    @ApiOperation(value = "修改资料", notes = "需要通过id获取原用户信息 需要username更新缓存")
    @Transactional
    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody User user) throws Exception {
        if (StringUtils.isEmpty(user.getUsername()) || user.getRoleId() == null) {
            return ResultUtil.error("缺少必需表单字段:用户名、角色");
        }
        User oldUser = userService.get(user.getId());
        // 所修改了用户名
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
        return ResultUtil.success(null);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "id"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "password", value = "状态")
    })
    @ApiOperation(value = "修改用户状态")
    @PostMapping("/editStatus")
    public Result editStatus(Long id, Boolean status) throws Exception {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userService.update(user);
        return ResultUtil.success(null);
    }

    @ApiOperation(value = "批量删除用户")
    @Transactional
    @DeleteMapping("/delByIds/{ids}")
    public Result deleteUser(@PathVariable Long[] ids) throws Exception {
        for (Long id: ids) {
            userService.delete(id);
        }
        return ResultUtil.success(null);
    }

    @ApiOperation(value = "根据角色获取用户列表")
    @GetMapping("/getByRole")
    public Result<List<Map<String, Object>>> getUserByRole(Long roleId) throws Exception {
        return ResultUtil.success(teamNative.getUserByRoleId(roleId));
    }

}
