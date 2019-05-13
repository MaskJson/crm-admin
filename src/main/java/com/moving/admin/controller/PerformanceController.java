package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.performance.PerformanceNative;
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
@RequestMapping("/performance")
public class PerformanceController extends AbstractController {

    @Autowired
    private PerformanceNative performanceNative;

    @ApiOperation("个人绩效")
    @GetMapping("/info")
    public Result<List<Map<String, Object>>> getPerformance(Long userId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceNative.getPerformance(userId, flag, time));
    }

    @ApiOperation("上级获取绩效报表")
    @GetMapping("/report")
    public Result<List<Map<String, Object>>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceNative.getPerformanceReport(userId, roleId, flag, time));
    }

}
