package com.moving.admin.controller;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.service.CommonService;
import com.moving.admin.service.CustomerService;
import com.moving.admin.service.TalentService;
import com.moving.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
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
//        String filePath = request.getSession().getServletContext().getRealPath(File.separator + "file");
        String filePath = File.separator + "crmfile";
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
//            String filePath = request.getSession().getServletContext().getRealPath(File.separator + "file");
            String filePath = File.separator + "crmfile";
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

    @ApiOperation("文件下载")
    @GetMapping("/base64")
    @IgnoreSecurity
    public String base64(String path) throws Exception {
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] data = null;
        try {
//            String filePath = request.getSession().getServletContext().getRealPath(File.separator + "file");
            String filePath = File.separator + "crmfile";
            File file = new File(filePath, path);
            InputStream in = null;
            // 读取图片字节数组
            try {
                in = new FileInputStream(file);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoder.encode(data);
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
