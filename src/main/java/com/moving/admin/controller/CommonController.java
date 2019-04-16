package com.moving.admin.controller;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.exception.WebException;
import com.moving.admin.service.CommonService;
import com.moving.admin.service.UploadService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(description = "公共数据")
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UploadService uploadService;

    @ApiOperation("根据tableName，name按需各个列表")
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getListByTableName(Integer type, String name) throws Exception {
        return ResultUtil.success(commonService.getListByTableName(type, name));
    }

    @IgnoreSecurity
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String md5File = UUID.randomUUID() + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File newFile = null;
        String filePath = request.getSession().getServletContext().getRealPath("/file");
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            newFile = new File(filePath + "\\" + md5File);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            fos = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return ResultUtil.success(md5File);
    }

}
