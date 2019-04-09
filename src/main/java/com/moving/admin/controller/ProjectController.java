package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.dao.natives.AdjustNative;
import com.moving.admin.dao.natives.ProjectPageNative;
import com.moving.admin.entity.project.Project;
import com.moving.admin.entity.project.ProjectRemind;
import com.moving.admin.entity.project.ProjectReport;
import com.moving.admin.entity.project.ProjectTalent;
import com.moving.admin.service.ProjectService;
import com.moving.admin.util.ResultUtil;
import com.moving.admin.util.SqlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "项目管理")
@RestController
@RequestMapping("/project")
public class ProjectController extends AbstractController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private AdjustNative adjustNative;

    @PersistenceContext
    protected EntityManager entityManager;

    @ApiOperation("收藏夹添加-编辑")
    @PostMapping("/save")
    public Result<Long> save(@RequestBody Project project) throws Exception {
        return ResultUtil.success(projectService.save(project));
    }

    @ApiOperation("收藏夹添加-编辑")
    @GetMapping("/info")
    public Result<Project> getInfo(Long id) throws Exception {
        return ResultUtil.success(projectService.getById(id));
    }

    @ApiOperation("获取项目列表")
    @GetMapping("/list")
    public Result<Map<String, Object>> getList(
            Long folderId, Long teamId, Long customerId, String industry, String city, Boolean follow, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            ) throws Exception {
        ProjectPageNative projectNative = new ProjectPageNative();
        if (folderId != null) {
            projectNative.setFolder(SqlUtil.getIn(folderItemDao.findItemIds(folderId, 3), "a.id"));
        }
        if (teamId != null) {
            projectNative.setTeam(teamId);
        }
        if (customerId != null) {
            projectNative.setCustomer(customerId);
        }
        if (!StringUtils.isEmpty(industry)) {
            projectNative.setIndustry(industry);
        }
        if (!StringUtils.isEmpty(city)) {
            projectNative.setCity(city);
        }
        if (follow != null) {
            projectNative.setFollow(follow);
        }
        projectNative.appendSort(pageable);
        return ResultUtil.success(projectNative.getResult(entityManager));
    }

    @ApiOperation("修改客户关注状态")
    @PostMapping("/toggle-follow")
    public Result toggleFollow(Long id, Boolean follow) throws Exception {
        projectService.toggleFollow(id, follow);
        return ResultUtil.success(null);
    }

    @ApiOperation("根据状态获取项目进展人才")
    @GetMapping("/talent/getByStatus")
    public Result<List<Map<String, Object>>> getProjectTalentByStatus(Integer status, Long id) throws Exception {
        return ResultUtil.success(adjustNative.getProjectTalent(status, id));
    }

    @ApiOperation("添加项目人才")
    @PostMapping("/talent/add")
    public Result<Long> addProjectTalent(@RequestBody ProjectTalent projectTalent) throws Exception {
        return ResultUtil.success(projectService.saveProjectTalent(projectTalent));
    }

    @ApiOperation("添加人才进展跟踪记录")
    @PostMapping("/remind/add")
    public Result<Long> addProjectRemind(@RequestBody ProjectRemind projectRemind) throws Exception {
        return ResultUtil.success(projectService.addProjectRemind(projectRemind));
    }

    @ApiOperation("获取该项目已关联的非淘汰人才")
    @GetMapping("/talent/all")
    public Result<List<Long>> getProjectTalentByStatus(Long id) throws Exception {
        return ResultUtil.success(adjustNative.getTalentsByProjectId(id));
    }

    @ApiOperation("获取诊断需要的内容")
    @GetMapping("/report/data")
    public Result<Map<String, Object>> getReportData(Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("counts", projectService.getProjectTalentCounts(id));
        map.put("list", projectService.getReportList(id));
        return ResultUtil.success(map);
    }

    @ApiOperation("添加诊断报告")
    @PostMapping("/report/add")
    public Result<Long> addProjectReport(@RequestBody ProjectReport report) throws Exception {
        return ResultUtil.success(projectService.addProjectReport(report));
    }

    @ApiOperation("推荐给客户二次审核")
    @PostMapping("/talent/review")
    public Result<Boolean> reviewToCustomer(Long id, Boolean flag) throws Exception {
        projectService.reviewToCustomer(id, flag);
        return ResultUtil.success(true);
    }

}
