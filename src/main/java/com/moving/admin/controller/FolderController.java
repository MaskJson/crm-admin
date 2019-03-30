package com.moving.admin.controller;

import com.moving.admin.bean.Result;
import com.moving.admin.entity.folder.Folder;
import com.moving.admin.entity.folder.FolderItem;
import com.moving.admin.service.FolderService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "收藏夹管理")
@RestController
@RequestMapping("/folder")
public class FolderController extends AbstractController {

    @Autowired
    private FolderService folderService;

    @ApiOperation("收藏夹添加-编辑")
    @PostMapping("/save")
    public Result<Long> save(@RequestBody Folder folder) throws Exception {
        return ResultUtil.success(folderService.save(folder));
    }

    @ApiOperation("收藏夹列表")
    @GetMapping("/list")
    public Result<List<Folder>> list(Integer type) throws Exception {
        return ResultUtil.success(folderService.list(type));
    }

    @ApiOperation("根据id 删除")
    @PostMapping("/remove")
    public Result<Long> save(Long id) throws Exception {
        folderService.remove(id);
        return ResultUtil.success(id);
    }

    @ApiOperation("启用-禁用")
    @PostMapping("/toggle")
    public Result<Long> toggle(Long id, Boolean status) throws Exception {
        folderService.toggle(id, status);
        return ResultUtil.success(id);
    }

    @ApiOperation("item-关联收藏夹")
    @PostMapping("/bind")
    public Result<Long> save(@RequestBody FolderItem folderItem) throws Exception {
        return ResultUtil.success(folderService.bind(folderItem));
    }

}
