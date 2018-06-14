package com.xkd.controller;

import com.xkd.model.Document;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.CompanyService;
import com.xkd.service.DocumentService;
import com.xkd.service.PagerFileService;
import com.xkd.service.UserDynamicService;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/document")
public class DocumentController  extends BaseController{

	
	@Autowired
	private DocumentService documentService;
	
	
	@Autowired
	private UserDynamicService userDynamicService;
	
	
	@Autowired
	private PagerFileService pagerFileService;
	
	
	@Autowired
	private CompanyService companyService;
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月4日 
	 * @功能描述:查询出该企业的文档信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDocumentsByTtypeId")
	public ResponseDbCenter selectDocumentsByCompanyId(HttpServletRequest req, HttpServletResponse rsp) {

		String ttypeId = req.getParameter("ttypeId");

		if (StringUtils.isBlank(ttypeId)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		ResponseDbCenter dbCenter = new ResponseDbCenter();
		try {
			
			dbCenter.setResModel(documentService.selectDocumentsByTtypeId(ttypeId));

			
		} catch (Exception e) {

			System.out.println(e);

		}
		return dbCenter;
		
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月4日 
	 * @功能描述:上传文档
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveDocuments")
	public ResponseDbCenter saveDocuments(@RequestParam(value = "files", required = false) MultipartFile[] files,HttpServletRequest req) {
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");	
		String companyId = req.getParameter("companyId");
		String token = req.getParameter("token");

		if (StringUtils.isBlank(token)) {
			// 富文本编辑器的格式，要这样返回
			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;

		try {

			userId = req.getSession().getAttribute(token).toString();

		} catch (Exception e) {

			System.out.println(e);
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(userId)) {

			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(companyId) || files == null) {

			return ResponseConstants.MISSING_PARAMTER;
		}

		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH +"wendanguanli/"+ companyId;

		List<Document> documents = new ArrayList<>();

		for (MultipartFile file : files) {
			try {
				Map<String, String> fileObj = FileUtil.getFileNameAdd(file.getOriginalFilename(),false);
				String fileName = fileObj.get("name");// 获取上传的文件名字
				FileUtil.newFolder(uploadPath);
				String size = FileUtil.saveFile(file, uploadPath + "/" + fileName);
				if (size.equals("fail")) {//上传失败
					return ResponseConstants.FUNC_UPLOADFILEFAIL;
				}else if(size.equals("error")){//上传时名称重复修改名称后继续上传
					fileObj = FileUtil.getFileNameAdd(fileName,true);
					fileName = fileObj.get("name");
					size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
				}
				
				//保存文件夹
				Map<String, String> pagerFile = pagerFileService.getPagerFileById(companyId);
				if(pagerFile == null){
					pagerFile = new HashMap<>();
					pagerFile.put("flag", "add");
					pagerFile.put("parentId", "0");
					pagerFile.put("pagerPath", companyId);
					FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + companyId);// 新建文件夹
					pagerFile.put("id", companyId);
					pagerFile.put("pagerName", companyService.selectCompanyInfoById(companyId).getCompanyName());
					pagerFile.put("userId", userId);
					pagerFileService.changePagerFile(pagerFile);
				}else if(pagerFile != null &&pagerFile.get("status").equals("2")){
					pagerFile.put("flag", "edit");
					pagerFile.put("userId", userId);
					pagerFile.put("pagerName", companyService.selectCompanyInfoById(companyId).getCompanyName());
					pagerFileService.changePagerFile(pagerFile);
					
				}
					
				//保存文件
				String httpPath = PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/"+companyId+"/"+fileName;
				Document document = new Document();
				document.setTtypeId(companyId);
				document.setPagerFileId(companyId);
				document.setPath(httpPath);
				document.setStatus(0);
				document.setTtype(1);
				document.setCreatedBy(userId);
				document.setFileName(fileName);
				document.setFileSize(FileUtil.getPrintSize(size));
				document.setFileByte(size);
				document.setExt(fileObj.get("foot"));
				
				documentService.saveDocument(document);
				
				//编辑新增企业会添加动态记录信息
				String updateBy = req.getParameter("updateBy");
				String updateByName = req.getParameter("updateByName");
				
				
				userDynamicService.addUserDynamic(loginUserId, companyId, fileName, "添加", "上传了\""+fileName+"\"文档", 0,null,null,null);
				
	
				documents.add(document);
					
				
			} catch (Exception e) {

				e.printStackTrace();
				return ResponseConstants.FUNC_SERVERERROR;
			}
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(documents);
		
		return responseDbCenter;

	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月11日 
	 * @功能描述:上传会务文档
	 * @param files
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveMeetingDocuments")
	public ResponseDbCenter saveMeetingDocuments(@RequestParam(value = "files", required = false) MultipartFile[] files,HttpServletRequest req) {
			
		String meetingId = req.getParameter("meetingId");
		String token = req.getParameter("token");

		if (StringUtils.isBlank(token)) {
			// 富文本编辑器的格式，要这样返回
			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;

		try {

			userId = req.getSession().getAttribute(token).toString();

		} catch (Exception e) {

			System.out.println(e);
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(userId)) {

			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(meetingId) || files == null) {

			return ResponseConstants.MISSING_PARAMTER;
		}

		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + meetingId;

		List<Document> documents = new ArrayList<>();

		for (MultipartFile file : files) {

			String fileName = file.getOriginalFilename();// 获取上传的文件名字
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String dataString = sf.format(date);
			
			String newFileName = dataString+"_"+fileName;

			File targetFileDir = new File(uploadPath); // 新建文件夹
			File targetFile = new File(uploadPath + "/" + newFileName); // 新建文件

			try {

				String os = System.getProperty("os.name");
				
				if (!targetFileDir.exists()) { // 判断文件的路径是否存在

					targetFileDir.mkdirs(); // 如果文件不存在 在目录中创建文件夹

					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + targetFileDir.getPath());
					} 

				}

				file.transferTo(targetFile); // 传送 失败就抛异常

				if (targetFile.exists()) {

					targetFile.setExecutable(true);// 设置可执行权限
					targetFile.setReadable(true);// 设置可读权限
					targetFile.setWritable(true);// 设置可写权限

					String saveFilename = targetFile.getPath();
					
					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + saveFilename);
					} 

					Document document = new Document();

					String httpPath = PropertiesUtil.FILE_HTTP_PATH + meetingId+"/"+newFileName;
					
					String id = UUID.randomUUID().toString();
					
					document.setId(id);
					document.setTtypeId(meetingId);
					document.setPath(httpPath);
					document.setStatus(0);
					document.setTtype(1);
					document.setCreatedBy(userId);
					document.setFileName(newFileName);
					
					documentService.saveDocument(document);
					
					documents.add(document);
					
				} else {

					return ResponseConstants.FUNC_UPLOADFILEFAIL;
				}

			} catch (Exception e) {

				e.printStackTrace();
				return ResponseConstants.FUNC_SERVERERROR;
			}
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(documents);
		
		return responseDbCenter;

	}
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月4日 
	 * @功能描述:删除文档
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDocument")
	public ResponseDbCenter deleteDocument(HttpServletRequest req, HttpServletResponse rsp) {
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		String id = req.getParameter("id");
		String token = req.getParameter("token");

		if (StringUtils.isBlank(token)) {

			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;

		try {

			userId = req.getSession().getAttribute(token).toString();

		} catch (Exception e) {

			System.out.println(e);
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(userId)) {

			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(id)) {

			return ResponseConstants.MISSING_PARAMTER;
		}

		try {

			Document document =  documentService.selectDocumentById(id);

			/*
			 * 编辑新增企业会添加动态记录信息
			 */
			
			if(document != null){
				
				String updateBy = req.getParameter("updateBy");
				String updateByName = req.getParameter("updateByName");
				
				
				
				userDynamicService.addUserDynamic(loginUserId, document.getTtypeId(), document.getFileName(), "删除", "删除了\""+document.getFileName()+"\"文档", 0,null,null,null);
				
				
				
			}
			
			documentService.deleteDocumentById(id, userId);
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}

//		List<String> paths = new ArrayList<>();

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
//		responseDbCenter.setResModel(paths);

		return responseDbCenter;

	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月4日 
	 * @功能描述:上传文档
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveCompanysLogo")
	public ResponseDbCenter saveCompanysLogo(@RequestParam(value = "files", required = false) MultipartFile[] files,HttpServletRequest req) {
			
		String companyId = req.getParameter("companyId");
		String token = req.getParameter("token");

		if (StringUtils.isBlank(token)) {
			// 富文本编辑器的格式，要这样返回
			return ResponseConstants.FUNC_USER_NOTOKEN;
		}

		String userId = null;

		try {

			userId = req.getSession().getAttribute(token).toString();

		} catch (Exception e) {

			System.out.println(e);
			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(userId)) {

			return ResponseConstants.FUNC_DBCENTER_TOKENISWRONG;
		}

		if (StringUtils.isBlank(companyId) || files == null) {

			return ResponseConstants.MISSING_PARAMTER;
		}

		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + companyId;

		List<Document> documents = new ArrayList<>();

		for (MultipartFile file : files) {

			String fileName = file.getOriginalFilename();// 获取上传的文件名字
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String dataString = sf.format(date);
			
			String newFileName = dataString+"_"+fileName;

			File targetFileDir = new File(uploadPath); // 新建文件夹
			File targetFile = new File(uploadPath + "/" + newFileName); // 新建文件

			try {

				String os = System.getProperty("os.name");
				
				if (!targetFileDir.exists()) { // 判断文件的路径是否存在

					targetFileDir.mkdirs(); // 如果文件不存在 在目录中创建文件夹

					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + targetFileDir.getPath());
					} 
				}

				file.transferTo(targetFile); // 传送 失败就抛异常

				if (targetFile.exists()) {

					targetFile.setExecutable(true);// 设置可执行权限
					targetFile.setReadable(true);// 设置可读权限
					targetFile.setWritable(true);// 设置可写权限

					String saveFilename = targetFile.getPath();
					
					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + saveFilename);
					}

					Document document = new Document();

					String httpPath = PropertiesUtil.FILE_HTTP_PATH + companyId+"/"+newFileName;
					
					document.setTtypeId(companyId);
					document.setPath(httpPath);
					document.setStatus(0);
					document.setTtype(1);
					document.setCreatedBy(userId);
					document.setFileName(newFileName);
					
					documentService.saveDocument(document);
					
					documents.add(document);
					
				} else {

					return ResponseConstants.FUNC_UPLOADFILEFAIL;
				}

			} catch (Exception e) {

				e.printStackTrace();
				return ResponseConstants.FUNC_SERVERERROR;
			}
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(documents);
		
		return responseDbCenter;

	}
	
}
