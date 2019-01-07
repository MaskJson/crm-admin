package com.moving.admin.controller.sys;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.bean.TokenInformation;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.sys.User;
import com.moving.admin.service.sys.UserService;
import com.moving.admin.util.JwtUtil;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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
            String token = jwtUtil.encode(new TokenInformation(user.getId()));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", user);
            map.put("token", token);
            return ResultUtil.success(map);
        } else {
            return ResultUtil.error(null);
        }
    }

}
