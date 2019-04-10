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

}
