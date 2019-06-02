package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.natives.CustomerNative;
import com.moving.admin.entity.customer.*;
import com.moving.admin.entity.talent.Experience;
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
import java.util.Map;

@Api(description = "客户管理")
@RestController
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

    @Autowired
    CustomerService customerService;

    @Autowired
    private CustomerNative customerNative;

    @ApiOperation("客户添加-编辑")
    @PostMapping("/save")
    public Result<Long> save(@RequestBody Customer customer) throws Exception {
        return ResultUtil.success(customerService.save(customer));
    }

    @ApiOperation("客户详情")
    @GetMapping("/get")
    public Result<Customer> get(Long id, String name) throws Exception {
        Customer customer = customerService.getCustomerByKey(id, name);
        if (name != null) {
            return ResultUtil.success(customer);
        } else {
            if (customer != null) {
                return ResultUtil.success(customer);
            } else {
                return ResultUtil.error("该客户ID不存在");
            }
        }
    }

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result<Page<Customer>> list(Long id, String name, String industry, Long folderId, Boolean follow, Integer type, Long userId, Long roleId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        Page<Customer> result = customerService.getCustomerList(id, name, industry, folderId, follow, type, userId, roleId, pageable);
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
    public Result save(@RequestBody List<Long> ids) throws Exception {
        customerService.finishRemindByIds(ids);
        return ResultUtil.success(null);
    }

    @ApiOperation("客户跟踪记录")
    @GetMapping("/remind-list")
    public Result<List<CustomerRemind>>getAllRemind(Long id) throws Exception {
        List<CustomerRemind> list = customerService.getAllRemind(id);
        return ResultUtil.success(list);
    }

    @ApiOperation("获取所有客户")
    @GetMapping("/customer-all")
    public Result<List<Customer>>getAllCustomer() throws Exception {
        List<Customer> list = customerService.getAllCustomer();
        return ResultUtil.success(list);
    }

    @ApiOperation("获取所有可新建项目的客户")
    @GetMapping("/customerOfProject")
    public Result<List<Customer>>getProjectCustomers() throws Exception {
        List<Customer> list = customerService.findProjectCustomers();
        return ResultUtil.success(list);
    }

    @ApiOperation("获取所有部门")
    @GetMapping("/department/byId")
    public Result<List<Department>>getAllDepartmentByCustomerId(Long id) throws Exception {
        List<Department> list = customerService.getCustomerDepartments(id);
        return ResultUtil.success(list);
    }

    @ApiOperation("获取所有部门")
    @GetMapping("/department-all")
    public Result<List<Department>>getAllDepartment() throws Exception {
        List<Department> list = customerService.getAllDepartment();
        return ResultUtil.success(list);
    }

    @ApiOperation("获取公司下所有关联人才")
    @GetMapping("/talent-all")
    public Result<List<Map<String, Object>>> getCustomerTalent(Long id) throws Exception {
        return ResultUtil.success(customerService.getCustomerTalents(id));
    }

    @ApiOperation("添加-编辑客户联系人")
    @PostMapping("/contact/save")
    public Result<Long> saveContact(@RequestBody CustomerContact customerContact) throws Exception {
        return ResultUtil.success(customerService.saveCustomerContact(customerContact));
    }

    @ApiOperation("添加客户联系人联系记录")
    @PostMapping("/contact/remark/save")
    public Result<CustomerContactRemark> saveContactRemark(@RequestBody CustomerContactRemark customerContactRemark) throws Exception {
        return ResultUtil.success(customerService.saveCustomerContactRemark(customerContactRemark));
    }

    @ApiOperation("获取公司下所有联系人及其联系记录")
    @GetMapping("/contact/all")
    public Result<List<Map<String, Object>>> getCustomerContact(Long id, String name, Long departmentId, String position, String phone) throws Exception {
        return ResultUtil.success(customerService.getAllCustomerContact(id, name, departmentId, position, phone));
    }

    @ApiOperation("删除联系人")
    @PostMapping("/contact/del")
    public Result delCustomerContactByIds(@RequestBody Long [] ids) throws Exception {
        customerService.delCustomerContactByIds(ids);
        return ResultUtil.success(null);
    }

    @ApiOperation("客户列名-取消列名")
    @PostMapping("/contact/bindFollowUser-toggle")
    public Result<Long> toggleBindFollowUser(Long customerId, Long userId, Boolean status) throws Exception {
        return ResultUtil.success(customerService.toggleBindFollowUser(customerId, userId, status));
    }

    @ApiOperation("获取公司的相关项目")
    @GetMapping("/project/page")
    public Result<Map<String, Object>> getCustomerProjects(Long id, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(customerNative.getProjectByCustomerId(id, pageable));
    }

    @ApiOperation("待审核公司列表")
    @GetMapping("/audit/list")
    public Result<Map<String, Object>> getAuditList(Long userId, Long roleId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(customerNative.getAuditList(userId, roleId, pageable));
    }

    @ApiOperation("通过审核")
    @PostMapping("/audit/pass")
    public Result auditCustomer(@RequestBody List<Long> ids) throws Exception {
        customerService.auditCustomer(ids, 2);
        return ResultUtil.success(null);
    }

    @ApiOperation("审核拒绝")
    @PostMapping("/audit/refuse")
    public Result refuseCustomer(@RequestBody List<Long> ids) throws Exception {
        customerService.auditCustomer(ids, 1);
        return ResultUtil.success(null);
    }

    @ApiOperation("修改名称")
    @GetMapping("/checkName")
    public Result<List<Customer>> checkCustomerName(Long id, String name) throws Exception {
        return ResultUtil.success(customerService.filterCustomer(id, name));
    }

    @ApiOperation("修改名称")
    @PostMapping("/changeName")
    public Result<Boolean> changeCustomerName(Long id, String name) throws Exception {
        customerService.getCustomerByNameAndId(id, name);
        return ResultUtil.success(true);
    }

}
