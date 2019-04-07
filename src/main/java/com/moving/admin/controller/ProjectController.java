package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.dao.natives.ProjectNative;
import com.moving.admin.entity.project.Project;
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
            Long folderId, Long teamId, Long customerId, String industry, String city, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            ) throws Exception {
        ProjectNative projectNative = new ProjectNative();
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
        projectNative.appendSort(pageable);
        return ResultUtil.success(projectNative.getResult(entityManager));
    }

    @ApiOperation("修改客户关注状态")
    @PostMapping("/toggle-follow")
    public Result toggleFollow(Long id, Boolean follow) {
        projectService.toggleFollow(id, follow);
        return ResultUtil.success(null);
    }
}
