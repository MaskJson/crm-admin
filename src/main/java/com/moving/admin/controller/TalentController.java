package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.talent.Talent;
import com.moving.admin.entity.talent.TalentRemind;
import com.moving.admin.service.TalentService;
import com.moving.admin.util.ResultUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "人才管理")
@RestController
@RequestMapping("/talent")
public class TalentController extends AbstractController {

    @Autowired
    private TalentService talentService;

    @ApiOperation("手机号验证")
    @GetMapping("/check")
    public Result<Talent> checkPhone(String phone) throws Exception {
        return ResultUtil.success(talentService.checkPhone(phone));
    }

    @ApiOperation("人才添加-编辑")
    @PostMapping("/save")
    public Result<Long> checkPhone(@RequestBody Talent talent) throws Exception {
        return ResultUtil.success(talentService.save(talent));
    }

    @ApiOperation("人才详情")
    @GetMapping("/get")
    public Result<Talent> getTalentById(Long id) throws Exception {
        Talent talent = talentService.getTalentById(id);
        if (talent != null) {
            return ResultUtil.success(talent);
        } else {
            return ResultUtil.error("该人才ID不存在");
        }
    }

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result<Page<Talent>> list(String city, String name, String industry, String aptness, Long folderId, Boolean follow, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        Page<Talent> result = talentService.getCustomerList(city, name, industry, aptness, folderId, follow, pageable);
        return ResultUtil.success(result);
    }

    @ApiOperation("获取人才跟踪记录")
    @GetMapping("/remind-all")
    public Result<List<TalentRemind>> getAllRemind(Long id) throws Exception {
        return ResultUtil.success(talentService.getAllRemind(id));
    }

    @ApiOperation("修改客户关注状态")
    @PostMapping("/toggle-follow")
    public Result toggleFollow(Long id, Boolean follow) throws Exception {
        talentService.toggleFollow(id, follow);
        return ResultUtil.success(null);
    }

    @ApiOperation("添加人才跟踪记录")
    @PostMapping("/remind-add")
    public Result<Long> addRemind(@RequestBody TalentRemind talentRemind) throws Exception {
        return ResultUtil.success(talentService.saveRemind(talentRemind));
    }

    @ApiOperation("专属人才变动")
    @PostMapping("/toggleType")
    public Result<Boolean> toggleTalentType(Long id, Long userId, Boolean flag) throws Exception {
        talentService.toggleType(id, userId, flag);
        return ResultUtil.success(true);
    }

    @ApiOperation("将跟踪置为已跟进")
    @PostMapping("/remind/finish")
    public Result<Boolean> finishRemind(@RequestBody List<Long> ids) throws Exception {
        talentService.finishRemindByIds(ids);
        return ResultUtil.success(true);
    }

}
