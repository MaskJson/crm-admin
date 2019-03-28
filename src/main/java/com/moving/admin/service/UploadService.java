package com.moving.admin.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.moving.admin.util.HM;

/**
 * 文件上传服务
 * 
 * @author Administrator
 *
 */
@Service
public class UploadService extends AbstractService {

//	@Resource
//	private HttpServletRequest request;
//
//	@Resource
//	private HttpSession session;
//	/**
//	 * 文件磁盘存放路径
//	 */
//	@Value("${upload.file.path}")
//	private String uploadFilePath;

//	/**
//	 * 文件访问路径（http://xxxx/path）
//	 */
//	@Value("${upload.file.access}")
//	private String uploadFileAccess;
//	private String uploadFilePath = session.getServletContext().getRealPath("/img");
//	private String uploadFilePath = request.getSession().getServletContext().getRealPath("/img");
	/**
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Object saveFile(MultipartFile file, HttpServletRequest request) throws Exception {
		System.out.println(request.getSession().getServletContext().getRealPath("/"));
		String uploadFilePath = request.getSession().getServletContext().getRealPath("/img");
		String path = generateFileName(file.getOriginalFilename());
		File dest = new File(uploadFilePath, path);
		try {
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
//		String accessUrl = uploadFilePath + "/" + path;
		return HM.map().put("name", dest.getName()).put("url", path).end();
	}
	

	/**
	 * 生成存放路径
	 * 
	 * @return
	 */
//	private String generateSavePath(String oname) {
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		String path = format.format(new Date());
//		File file = new File(uploadFilePath, path);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		String name = generateFileName(oname);
//		return path + "/" + name;
//	}

	/**
	 * 重新生成文件的名称（后缀不变）
	 * 
	 * @param oname
	 * @return
	 */
	private String generateFileName(String oname) {
		String ext = Files.getFileExtension(oname);
		return UUID.randomUUID().toString() + "." + ext;
	}
}
