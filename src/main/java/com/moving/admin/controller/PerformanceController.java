package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.performance.PerformanceCustomerRemind;
import com.moving.admin.dao.performance.PerformanceNative;
import com.moving.admin.dao.performance.PerformanceReport;
import com.moving.admin.dao.performance.PerformanceTalentRemind;
import com.moving.admin.dao.sys.ReportDao;
import com.moving.admin.entity.sys.Report;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    private PerformanceCustomerRemind performanceCustomerRemind;

    @Autowired
    private PerformanceReport performanceReport;

    @Autowired
    private ReportDao reportDao;

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

    @ApiOperation("个人客户常规跟踪绩效")
    @GetMapping("/customer/info")
    public Result<List<Map<String, Object>>> getCustomerRemindPerformance(Long userId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceCustomerRemind.getPerformance(userId, flag, time));
    }

    @ApiOperation("上级获取客户常规跟踪报表")
    @GetMapping("/customer/infos")
    public Result<List<Map<String, Object>>> getCustomerRemindPerformanceReport(Long userId, Long roleId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceCustomerRemind.getPerformanceReport(userId, roleId, flag, time));
    }

    @ApiOperation("个人报告")
    @GetMapping("/report/info")
    public Result<List<Map<String, Object>>> getReportPerformance(Long userId, Integer flag, String time) throws Exception {
        return ResultUtil.success(performanceReport.getPerformance(userId, flag, time));
    }

    @ApiOperation("上级获取报告报表")
    @GetMapping("/report/infos")
    public Result<Map<String, Object>> getReportPerformanceReport(Long userId, Long roleId, Integer flag, String time) throws Exception {
        List<Map<String, Object>> reports = performanceReport.getPerformanceReport(userId, roleId, flag, time);
        List<Map<String, Object>> members = performanceReport.getMembers(userId, roleId, flag);
        Map<String, Object> map = new HashMap<>();
        map.put("reports", reports);
        map.put("members", members);
        return ResultUtil.success(map);
    }

    @ApiOperation("上级获取客户常规跟踪报表")
    @PostMapping("/report/save")
    public Result<Long> saveReport(@RequestBody Report report) throws Exception {
        report.setCreateTime(new Date());
        Report rp = reportDao.save(report);
        return ResultUtil.success(rp.getId());
    }

}
