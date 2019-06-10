package com.moving.admin.controller;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.service.CommonService;
import com.moving.admin.service.CustomerService;
import com.moving.admin.service.TalentService;
import com.moving.admin.util.ResultUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private TalentService talentService;

    @Autowired
    private CustomerService customerService;

    @ApiOperation("根据tableName，name按需各个列表")
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getListByTableName(Integer type, String name) throws Exception {
        return ResultUtil.success(commonService.getListByTableName(type, name));
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    @IgnoreSecurity
    public Result<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        String md5File = UUID.randomUUID() + file.getOriginalFilename();
        File newFile;
        String filePath = request.getSession().getServletContext().getRealPath("/file");
        try {
            newFile = new File(filePath, md5File);
            file.transferTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success(md5File);
    }

    @ApiOperation("文件下载")
    @GetMapping("/download")
    @IgnoreSecurity
    public HttpServletResponse download(String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String filePath = request.getSession().getServletContext().getRealPath(System.getProperty("file.separator") + "file");
            File file = new File(filePath, path);
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的header
            response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation("人才-客户，上传")
    @GetMapping("/file")
    public Result<Boolean> uploadFile(Integer type, Long id, String path, Boolean flag) throws Exception {
        if (type == 1) {
            talentService.setResume(id, path, flag);
        } else {
            customerService.setContractUrl(id, path);
        }
        return ResultUtil.success(true);
    }

}
