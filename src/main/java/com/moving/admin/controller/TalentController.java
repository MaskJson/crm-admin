package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.controller.AbstractController;
import com.moving.admin.entity.talent.Talent;
import com.moving.admin.service.TalentService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
