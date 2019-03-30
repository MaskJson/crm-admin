package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.customer.CustomerRemind;
import com.moving.admin.service.CustomerService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "客户管理")
@RestController
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

    @Autowired
    CustomerService customerService;

    @ApiOperation("客户添加-编辑")
    @PostMapping("/save")
    public Result<Long> save(@RequestBody Customer customer) throws Exception {
        return ResultUtil.success(customerService.save(customer));
    }

    @ApiOperation("客户详情")
    @GetMapping("/get")
    public Result<Customer> get(Long id, String name) throws Exception {
        return ResultUtil.success(customerService.getCustomerByKey(id, name));
    }

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result<Page<Customer>> list(Long id, String name, String industry, Long folderId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        Page<Customer> result = customerService.getCustomerList(id, name, industry, folderId, pageable);
        return ResultUtil.success(result);
    }

    @ApiOperation("修改客户关注状态")
    @PostMapping("/toggle-follow")
    public Result toggleFollow(Long id, Boolean follow) {
        customerService.toggleFollow(id, follow);
        return ResultUtil.success(null);
    }

    @ApiOperation("客户跟踪")
    @PostMapping("/add-remind")
    public Result<Long> save(@RequestBody CustomerRemind customerRemind) throws Exception {
        return ResultUtil.success(customerService.saveRemind(customerRemind));
    }

    @ApiOperation("客户跟踪结束跟进")
    @PostMapping("/finish-remind")
    public Result<Long> save(Long id) throws Exception {
        return ResultUtil.success(customerService.finishRemindById(id));
    }

}
