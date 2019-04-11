package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.natives.CountNative;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Api(description = "统计接口")
@RestController
@RequestMapping("/count")
public class CountController extends AbstractController {

    @Autowired
    private CountNative countNative;

    @ApiOperation("人才待办")
    @GetMapping("/talent/pending")
    public Result<Map<String, Object>> getTalentPendingList(Long userId, Integer type, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(countNative.talentRemindPendingList(userId, type, pageable));
    }

    @ApiOperation("客户待办")
    @GetMapping("/customer/pending")
    public Result<Map<String, Object>> getCustomerPendingList(Long userId, Integer type, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(countNative.customerRemindPendingList(userId, type, pageable));
    }

    @ApiOperation("人才地图")
    @GetMapping("/talent/map")
    public Result<List<Map<String, Object>>> getTalentPendingList(Long userId) throws Exception {
        return ResultUtil.success(countNative.getTalentMapByUserId(userId));
    }

    @ApiOperation("人才地图-统计各个状态下的人才")
    @GetMapping("/talent/map/status")
    public Result<List<Map<String, Object>>> getProjectStatusTalents(Long userId) throws Exception {
        return ResultUtil.success(countNative.getProjectStatusTalents(userId));
    }

    @ApiOperation("人才地图-获取收藏的人才")
    @GetMapping("/talent/map/folder")
    public Result<List<Map<String, Object>>> getFolderTalents(Long userId) throws Exception {
        return ResultUtil.success(countNative.getFolderTalentsByUserId(userId));
    }

    @ApiOperation("项目诊断待办")
    @GetMapping("/project/report/pending")
    public Result<List<Map<String, Object>>> getReportPending(Long userId,  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(countNative.getReportPending(userId, pageable));
    }

    @ApiOperation("总监-查看诊断记录")
    @GetMapping("/project/report/list")
    public Result<List<Map<String, Object>>> getReportList(Long userId,  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(countNative.getReports(userId, pageable));
    }

    @ApiOperation("首页统计")
    @GetMapping("/home")
    public Result<Map<String, BigInteger>> getHomeCount(Long userId) throws Exception {
        return ResultUtil.success(countNative.homeCount(userId));
    }

}
