package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.service.CommonService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(description = "公共数据")
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractController {

    @Autowired
    private CommonService commonService;

    @ApiOperation("根据tableName，name按需各个列表")
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getListByTableName(Integer type, String name) throws Exception {
        return ResultUtil.success(commonService.getListByTableName(type, name));
    }

}
