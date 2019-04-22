package com.moving.admin.controller;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.dao.natives.TeamNative;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Api(description = "团队管理")
@RestController
@RequestMapping("/team")
public class TeamController extends AbstractController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamNative teamNative;

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

    @ApiOperation("查看团队成员列表带信息")
    @GetMapping("/membersWithInfo")
    public Result<List<Map<String, Object>>> getTeamMembersWithInfo(Long id) throws Exception {
        return ResultUtil.success(teamService.getTeamMembersWithInfo(id));
    }

    @ApiOperation("团队添加-编辑")
    @PostMapping("/save")
    public Result<Long> saveTeam(@RequestBody Team team) throws Exception {
        return ResultUtil.success(teamService.save(team));
    }

    @ApiOperation("获取团队管理所需user")
    @GetMapping("/users")
    public Result<List<Map<String, Object>>> getTeamManagerUser() throws Exception {
        return ResultUtil.success(teamService.getTeamManagerUsers());
    }

    @ApiOperation("获取所有团队")
    @GetMapping("/all")
    public Result<List<Map<String, Object>>> getTeams() throws Exception {
        return ResultUtil.success(teamNative.getTeams());
    }

    @Transactional
    @ApiOperation("团队交接-总监离职")
    @PostMapping("/connect/team")
    public Result<Boolean> teamConnect(Long teamId, Long connectTeamId, Long userId, Long connectUserId) throws Exception {
        teamService.teamConnect(teamId, connectTeamId, userId, connectUserId);
        return ResultUtil.success(true);
    }

    @Transactional
    @ApiOperation("成员交接-普通用户离职")
    @PostMapping("/connect/member")
    public Result<Boolean> memberConnect(Long userId, Long connectUserId) throws Exception {
        teamService.memberConnect(userId, connectUserId);
        return ResultUtil.success(true);
    }

    @ApiOperation("根据总监用户id-查看团队成员列表")
    @GetMapping("/getMemberByUserId")
    public Result<List<Team>> getMemberByUserId(Long id) throws Exception {
        return ResultUtil.success(teamService.getMembersByUserId(id));
    }

}
