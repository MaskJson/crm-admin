package com.moving.admin.controller.module.customer;

import com.moving.admin.bean.Result;
import com.moving.admin.entity.module.customer.Customer;
import com.moving.admin.service.module.customer.CustomerService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "客户管理")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation("添加-编辑客户")
    @PostMapping("/save")
    public Result save(@RequestBody Customer customer) throws Exception {
        customerService.save(customer);
        return ResultUtil.success(null);
    }

    @ApiOperation("根据id获取客户详情")
    @GetMapping("/getCustomerById")
    public Result<Customer> getCustomerById(Long id) throws Exception {
        return ResultUtil.success(customerService.getCustomerById(id));
    }

    @ApiOperation("根据name获取客户详情")
    @GetMapping("/getCustomerByName")
    public Result<Customer> getCustomerByName(String name) throws Exception {
        return ResultUtil.success(customerService.getCustomerByName(name));
    }

}
