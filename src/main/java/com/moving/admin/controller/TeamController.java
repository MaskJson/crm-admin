package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.entity.sys.Team;
import com.moving.admin.entity.sys.User;
import com.moving.admin.service.TeamService;
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

@Api(description = "团队管理")
@RestController
@RequestMapping("/team")
public class TeamController extends AbstractController {

    @Autowired
    private TeamService teamService;

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result<Page<User>> getTeamList(String name, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResultUtil.success(teamService.getTeamList(name, pageable));
    }

    @ApiOperation("查看团队成员列表")
    @GetMapping("/members")
    public Result<List<Team>> getTeamMembers(Long id) throws Exception {
        return ResultUtil.success(teamService.getTeamMembersByTeamId(id));
    }

    @ApiOperation("团队添加-编辑")
    @PostMapping("/members")
    public Result<Long> saveTeam(@RequestBody Team team) throws Exception {
        return ResultUtil.success(teamService.save(team));
    }

}
