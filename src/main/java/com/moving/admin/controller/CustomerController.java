package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.entity.customer.Customer;
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

@Api(description = "系统用户管理")
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

    @ApiOperation("客户添加-编辑")
    @GetMapping("/get")
    public Result<Customer> get(Long id, String name) throws Exception {
        return ResultUtil.success(customerService.getCustomerByKey(id, name));
    }

    @ApiOperation("分页查询")
    @GetMapping("/get")
    public Result<Page<Customer>> list(Long id, String name, String industry, Long folderId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        return null;
    }

}
