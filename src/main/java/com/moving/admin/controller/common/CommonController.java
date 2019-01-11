package com.moving.admin.controller.common;


import com.moving.admin.bean.Result;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.common.Aptness;
import com.moving.admin.entity.common.Industry;
import com.moving.admin.service.common.CommonService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "公共接口数据")
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "/获取所有行业")
    @GetMapping("/industry/list")
    public Result<List<Industry>> getAllIndustry() throws Exception {
        return ResultUtil.success(commonService.getAllIndustry());
    }

    @ApiOperation(value = "/获取所有行业")
    @GetMapping("/aptness/list")
    public Result<List<Aptness>> getAllAptness() throws Exception {
        return ResultUtil.success(commonService.getAllAptness());
    }

}
