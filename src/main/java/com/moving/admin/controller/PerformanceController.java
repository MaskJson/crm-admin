package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.performance.PerformanceNative;
import com.moving.admin.dao.performance.PerformanceTalentRemind;
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

    @Autowired
    private PerformanceTalentRemind performanceTalentRemind;

    @ApiOperation("个人项目进展绩效")
    @GetMapping("/project/info")
    public Result<List<Map<String, Object>>> getPerformance(Long userId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceNative.getPerformance(userId, flag, time));
    }

    @ApiOperation("上级获取项目进展报表")
    @GetMapping("/project/infos")
    public Result<List<Map<String, Object>>> getPerformanceReport(Long userId, Long roleId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceNative.getPerformanceReport(userId, roleId, flag, time));
    }

    @ApiOperation("个人人才常规跟踪绩效")
    @GetMapping("/talent/info")
    public Result<List<Map<String, Object>>> getTalentRemindPerformance(Long userId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceTalentRemind.getPerformance(userId, flag, time));
    }

    @ApiOperation("上级获取人才常规跟踪报表")
    @GetMapping("/talent/infos")
    public Result<List<Map<String, Object>>> getTalentRemindPerformanceReport(Long userId, Long roleId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceTalentRemind.getPerformanceReport(userId, roleId, flag, time));
    }

}
