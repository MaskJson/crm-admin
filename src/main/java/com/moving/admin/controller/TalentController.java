package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.talent.Talent;
import com.moving.admin.service.TalentService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
