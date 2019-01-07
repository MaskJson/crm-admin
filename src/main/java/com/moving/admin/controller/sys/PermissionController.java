package com.moving.admin.controller.sys;

import com.moving.admin.bean.Result;
import com.moving.admin.service.sys.PermissionService;
import com.moving.admin.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/getMenuListByUserId")
    public Result<List> getMenuListByUserId() throws Exception {
        return ResultUtil.success(permissionService.findPermissionsByUserId());
    }

}
