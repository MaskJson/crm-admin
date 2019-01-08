package com.moving.admin.controller;

import com.moving.admin.dao.natives.TestNative;
import com.moving.admin.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moving.admin.annotation.IgnoreSecurity;
import com.moving.admin.bean.Result;
import com.moving.admin.bean.request.UploadReq;
import com.moving.admin.dao.TestDao;
import com.moving.admin.entity.Order;
import com.moving.admin.service.TestService;
import com.moving.admin.util.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@RestController
@Api(description = "JPA分页查询测试")
public class Test {
	
	@Autowired
	private TestService testService;
	@Autowired
	private TestDao testDao;
	@Autowired
    private FileUploadService fileUploadService;

	@ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "page", value = "页码"),
        @ApiImplicitParam(paramType = "query", dataType = "Long", name = "size", value = "每页条数")
    })
    @ApiOperation("订单分页查询")
    @IgnoreSecurity
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Page<Order> test(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
		System.err.println(pageable.getPageSize() + "---" + pageable.getPageNumber()); // 每页条数   + 页码
        return testService.getPage(pageable);
    }
	
	@ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType = "int", name = "userId", value = "用户id"),
    })
    @ApiOperation("原生sql测试")
    @IgnoreSecurity
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public int getCount(int userId) {
        return testDao.getCountByUserId(userId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "userId", value = "用户id"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "size", value = "每页条数")
    })
    @ApiOperation("entityManager创建原生查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> getNativeList(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,Long userId) {
	    TestNative testNative = new TestNative();
        testNative.setUserId(userId);
        testNative.appendSort(pageable);
	    return testService.getNativeList(testNative);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "userId", value = "用户id"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "size", value = "每页条数")
    })
    @ApiOperation("jpa_predicate单表带参分页查询")
    @RequestMapping(value = "/predicate", method = RequestMethod.GET)
    public Page<Order> getListByParams(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,Long userId) {
	    return testService.getList(pageable, userId);
    }
    
    @IgnoreSecurity
    @RequestMapping(value = "/order-detail", method = RequestMethod.GET)
    public Result<Order> getDetailById(Long id) {
    	System.err.println(id);
    	return ResultUtil.success(testService.getDetail(id));
    }
    
    @IgnoreSecurity
    @RequestMapping(value = "/json", method = RequestMethod.POST)
    public Result<Object> jsonTest(@RequestBody Map<String, Object> map) {
    	System.err.println(map.get("hobbies"));
    	return ResultUtil.success(null);
    }
    
    @IgnoreSecurity
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload (@RequestBody UploadReq req, HttpSession session) {
   
    	byte[] bytes = req.getBytes();
    	BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String filePath = session.getServletContext().getRealPath("/img");
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            return fileName;
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 上传文件
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/file",method = RequestMethod.POST)
    public Object singleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session) throws Exception {
        return fileUploadService.saveFile(file, session);
    }

}
