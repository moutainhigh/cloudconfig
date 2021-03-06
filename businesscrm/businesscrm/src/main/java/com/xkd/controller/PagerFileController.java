package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.Document;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@Api(description = "文档管理")
@Controller
@RequestMapping(method =  { RequestMethod.GET, RequestMethod.POST},value = "/pagerFile")
public class PagerFileController extends BaseController {

	@Autowired
	PagerFileService pagerFileService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private UserDynamicService userDynamicService;

	@Autowired
	private CompanyService companyService;
	@Autowired
	BusinessOpportunityService businessOpportunityService;

	@Autowired
	UserService userService;

	@Autowired
	UserDataPermissionService userDataPermissionService;


	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存企业文档管理上传的文档
	 */
	@ApiOperation(value = "保存企业文档管理上传的文档")
	@ResponseBody
	@RequestMapping(method = { RequestMethod.POST}, value = "/saveCompanyPagerFiles")
	public ResponseDbCenter saveCompanyPagerFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
												  @RequestHeader  String token, String pagerFileId, String appFlag, HttpServletRequest req) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		try {
			String userId = (String) req.getSession().getAttribute(token);

			pagerFileService.initializationCompanyFolder(pagerFileId,userId);


			List<Map<String, String>> listFile = savePagerFiles(files, userId, pagerFileId, appFlag);
			if (null == listFile) {
				return ResponseConstants.FUNC_UPLOADFILEFAIL;
			}
			responseDbCenter.setResModel(listFile);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;

	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存项目文档管理上传的文档
	 */
	@ApiOperation(value = "保存项目文档管理上传的文档")
	@ResponseBody
	@RequestMapping(method = {  RequestMethod.POST}, value = "/saveProjectPagerFiles")
	public ResponseDbCenter saveProjectPagerFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
												  @RequestHeader  String token, String pagerFileId, String appFlag, HttpServletRequest req) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Map<String, String>> listFile = savePagerFiles(files, (String) req.getSession().getAttribute(token), pagerFileId, appFlag);
			if (null == listFile) {
				return ResponseConstants.FUNC_UPLOADFILEFAIL;
			}
			responseDbCenter.setResModel(listFile);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;

	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存项目文档管理上传的文档
	 */
	@ApiOperation(value = "保存任务上传的文档")
	@ResponseBody
	@RequestMapping(method = {  RequestMethod.POST}, value = "/saveTaskPagerFiles")
	public ResponseDbCenter saveTaskPagerFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
											   @RequestHeader String token, String pagerFileId, String appFlag, HttpServletRequest req) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Map<String, String>> listFile = savePagerFiles(files, (String) req.getSession().getAttribute(token), pagerFileId, appFlag);
			if (null == listFile) {
				return ResponseConstants.FUNC_UPLOADFILEFAIL;
			}
			responseDbCenter.setResModel(listFile);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;

	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存方案文档管理上传的文档
	 */
	@ApiOperation(value = "保存方案文档管理上传的文档")
	@ResponseBody
	@RequestMapping(method = {  RequestMethod.POST}, value = "/saveProgramPagerFiles")
	public ResponseDbCenter saveProgramPagerFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
												  @RequestHeader  String token, String pagerFileId, String appFlag, HttpServletRequest req) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Map<String, String>> listFile = savePagerFiles(files, (String) req.getSession().getAttribute(token), pagerFileId, appFlag);
			if (null == listFile) {
				return ResponseConstants.FUNC_UPLOADFILEFAIL;
			}
			responseDbCenter.setResModel(listFile);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;

	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存自定义文档管理上传的文档
	 */
	@ApiOperation(value = "保存自定义文档管理上传的文档")
	@ResponseBody
	@RequestMapping(method = {  RequestMethod.POST}, value = "/saveOtherPagerFiles")
	public ResponseDbCenter saveOtherPagerFiles(@RequestParam(value = "files", required = false) MultipartFile[] files,
												@RequestHeader String token, String pagerFileId, String appFlag, HttpServletRequest req) {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Map<String, String>> listFile = savePagerFiles(files, (String) req.getSession().getAttribute(token), pagerFileId, appFlag);
			if (null == listFile) {
				return ResponseConstants.FUNC_UPLOADFILEFAIL;
			}
			responseDbCenter.setResModel(listFile);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		return responseDbCenter;

	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:保存文档管理上传的文档
	 */

	public List<Map<String, String>> savePagerFiles(MultipartFile[] files,
													String userId, String pagerFileId, String appFlag) {
		List<Map<String, String>> listFile = new ArrayList<>();
		Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
		if (pagerFile == null) {
			return null;
		}
		String path = pagerFile.get("pagerPath");
		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path;
		FileUtil.newFolder(uploadPath);// 没有就创建文件夹，有就不创建
		for (MultipartFile file : files) {
			Map<String, String> fileObj = FileUtil.getFileNameAdd(file.getOriginalFilename(), false);
			String fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
			String size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			if (size.equals("fail")) {
				return null;
			} else if (size.equals("error")) {
				fileObj = FileUtil.getFileNameAdd(fileName, true);
				fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
				size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			}
			// 前台用户显示的文件名
			String httpPath = PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/" + path + "/" + fileName;

			Document document = new Document();
			document.setId(UUID.randomUUID().toString());
			document.setFileName(fileName);
			document.setPagerFileId(pagerFileId);
			document.setPath(httpPath);
			document.setCreatedBy(userId);
			document.setFileSize(FileUtil.getPrintSize(size));
			document.setFileByte(size);
			document.setExt(fileObj.get("foot"));
			document.setTtypeId(pagerFile == null ? null : pagerFile.get("ttype"));
			document.setTtype(0);
			Map<String, String> fileAttr = new HashMap<>();
			fileAttr.put("imgSrc", httpPath);
			fileAttr.put("imgName", fileName);
			fileAttr.put("id", document.getId());
			listFile.add(fileAttr);
			documentService.saveDocument(document);
			//企业文档动态-------------
			if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {

				userDynamicService.addUserDynamic(userId, pagerFile.get("ttype"), fileName, "上传", "上传了 \"" + document.getFileName() + "\"", 0,null,null,null);
			}
		}
		return listFile;

	}

	//方案文件夹重命名Program
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateProgramFileName")
	public ResponseDbCenter updateProgramFileName(HttpServletRequest req,
												  @RequestHeader String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//项目文件夹重命名Project
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateProjectFileName")
	public ResponseDbCenter updateProjectFileName(HttpServletRequest req,
												  @RequestHeader 	String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//商机文件夹重命名Business
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateBusinessFileName")
	public ResponseDbCenter updateBusinessFileName(HttpServletRequest req,
												   @RequestHeader 	String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//任务文件夹重命名Task
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateTaskFileName")
	public ResponseDbCenter updateTaskFileName(HttpServletRequest req,
											   @RequestHeader String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//企业文件夹重命名Company
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateCompanyFileName")
	public ResponseDbCenter updateCompanyFileName(HttpServletRequest req,
												  @RequestHeader   String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//自定义文件名称重命名
	@ApiOperation(value = "修改文件名称")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/updateOtherFileName")
	public ResponseDbCenter updateOtherFileName(HttpServletRequest req,
												@RequestHeader   String token, String pagerFileId, String fileId, String fileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return updateFileName(req,token,pagerFileId,fileId,fileName);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	private ResponseDbCenter updateFileName(HttpServletRequest req,@RequestHeader  String token, String pagerFileId, String fileId, String fileName) {
		Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
		if (null == pagerFile) {
			return ResponseConstants.ILLEGAL_PARAM;
		}
		Document document = documentService.selectDocumentById(fileId);
		String path = pagerFile.get("pagerPath");

		fileName = fileName + "." + document.getExt();
		if ((fileName).equals(document.getFileName())) {
			return ResponseConstants.SUCCESS;
		}
		File targetFile = new File(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path + "/" + fileName); // 新建文件
		//判断将要修改的名称是否已存在
		if (targetFile.exists()) {
			//Map<String, String> fileObj = FileUtil.getFileNameAdd(fileName,true);
			//已存在的文件名称加上时间戳
			//fileName = fileObj.get("name");

			return ResponseConstants.PAGERFILE_NAME_ERROR;
		}
		String userId = (String) req.getSession().getAttribute(token);


		//企业文档动态-------------

		System.out.println(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH));
		System.out.println(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path + "/" + fileName);
		//修改文件名称
		Boolean renameStattus = FileUtil.renameFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),
				PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path + "/" + fileName);
		if(renameStattus == false){
			return ResponseConstants.USER_UPLOAD_ERROR;
		}
		if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {
			userDynamicService.addUserDynamic(userId, pagerFile.get("ttype"), fileName, "更新", "修改了 \"" + document.getFileName() + "\" 为 \"" + fileName + "\"", 0,null,null,null);
		}
		document.setFileName(fileName);
		document.setPath(PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/" + path + "/" + fileName);
		document.setCreatedBy(userId);
		documentService.updateDocumentName(document);
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel("SUCCESS");
		return responseDbCenter;
	}

	//企业下面的文档管理
	@ApiOperation(value = "查询企业下面的文档管理")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getCompanyFileAndPagerFile2")
	public ResponseDbCenter getCompanyFileAndPagerFile2(@RequestHeader String token, HttpServletRequest req,
														@RequestParam(required = false) String likeName, // 修改时传的Id
														@RequestParam(required = false) String ttype, // 保存的目录
														@RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
														@RequestParam(required = true) String pagerFileId) throws Exception {
		try {

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "0", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//企业下面基础文档下载
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadOtherCompFileZip")
	public ResponseDbCenter downloadOtherCompFileZip(HttpServletRequest req, String pagerFileId, String fileId,@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	//企业下面的文档管理
	@ApiOperation(value = "查询商机的文档管理")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getBusinessOpportunityFileAndPagerFile")
	public ResponseDbCenter getBusinessOpportunityFileAndPagerFile(@RequestHeader String token, HttpServletRequest req,
														@RequestParam(required = false) String likeName, // 修改时传的Id
														@RequestParam(required = false) String ttype, // 保存的目录
														@RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
														@RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "0", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	//企业
	@ApiOperation(value = "查询文档管理里面的企业文档")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getCompanyFileAndPagerFile")
	public ResponseDbCenter getCompanyFileAndPagerFile(@RequestHeader String token, HttpServletRequest req,
													   @RequestParam(required = false) String likeName, // 修改时传的Id
													   @RequestParam(required = false) String ttype, // 保存的目录
													   @RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
													   @RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "0", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			log.error("异常栈:",e);
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//方案2
	@ApiOperation(value = "查询文档管理下面的方案")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getProgramFileAndPagerFile")
	public ResponseDbCenter getProgramFileAndPagerFile(@RequestHeader String token, HttpServletRequest req,
													   @RequestParam(required = false) String likeName, // 修改时传的Id
													   @RequestParam(required = false) String ttype, // 保存的目录
													   @RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
													   @RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "2", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	@ApiOperation(value = "查询方案管理下面的方案")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getProgramFileAndPagerFile2")
	public ResponseDbCenter getProgramFileAndPagerFile2(@RequestHeader String token, HttpServletRequest req,
														@RequestParam(required = false) String likeName, // 修改时传的Id
														@RequestParam(required = false) String ttype, // 保存的目录
														@RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
														@RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "2", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//项目1
	@ApiOperation(value = "查询文档管理下面的项目")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getProjectFileAndPagerFile")
	public ResponseDbCenter getProjectFileAndPagerFile(@RequestHeader  String token, HttpServletRequest req,
													   @RequestParam(required = false) String likeName, // 修改时传的Id
													   @RequestParam(required = false) String ttype, // 保存的目录
													   @RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
													   @RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "1", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//任务3
	@ApiOperation(value = "查询任务")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getTaskFileAndPagerFile")
	public ResponseDbCenter getTaskFileAndPagerFile(@RequestHeader String token, HttpServletRequest req,
													   @RequestParam(required = false) String likeName, // 修改时传的Id
													   @RequestParam(required = false) String ttype, // 排序方式
													   @RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
													   @RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "3", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//项目1
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getProjectFileAndPagerFile2")
	public ResponseDbCenter getProjectFileAndPagerFile2(@RequestHeader String token, HttpServletRequest req,
														@RequestParam(required = false) String likeName, // 修改时传的Id
														@RequestParam(required = false) String ttype, // 保存的目录
														@RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
														@RequestParam(required = true) String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, "1", pagerFile,req);
			responseDbCenter.setResModel(fileAndPagerFile);
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//自定义
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getOtherFileAndPagerFile")
	public ResponseDbCenter getOtherFileAndPagerFile(@RequestHeader String token, HttpServletRequest req,
													 @RequestParam(required = false) String likeName, // 修改时传的Id
													 @RequestParam(required = false) String ttype, // 排序的方式
													 @RequestParam(required = false) String deleteFlag,//0可以删除 1不可以删除
													 @RequestParam(required = false) String share,//是否是分享的
													 @RequestParam(required = false) String pagerFileStatus,//是否是分享的
													 String pagerFileId) throws Exception {
		try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			if(StringUtils.isBlank(pagerFileId) || (pagerFileId.equals("00") && StringUtils.isBlank(likeName))){
				Map<String, Object> fileAndPagerFile = new HashMap<>();
				String userId = req.getSession().getAttribute(token).toString();
				Map<String, Object> user = userService.selectUserById(userId);
				String departmentId = user.get("departmentId").toString();
				Map<String,Object> pareMap = new HashMap();
				pareMap.put("departmentId",user.get("roleId").equals("1")?null : departmentId);
				pareMap.put("userId",user.get("roleId").equals("1")?null : userId);
				pareMap.put("ttype","updateDate");
				List<HashMap<String, Object>> pagerFile = null;
				if(StringUtils.isBlank(pagerFileId)){
					//获取共享文档的tree
					pareMap.put("status","tree");
				}else if(pagerFileId.equals("00") && StringUtils.isBlank(likeName)){
					//共享文档排序，可删不可删
					pareMap.put("status","share");
					pareMap.put("shareDelete",deleteFlag);
					pareMap.put("ttype",StringUtils.isNotBlank(ttype) && ttype.equals("fileName") ?"fileName":"updateDate");
					pareMap.put("pagerFileStatus","share");

					fileAndPagerFile.put("pagerPath", "00");
					fileAndPagerFile.put("pathName", "所有共享文档");
					fileAndPagerFile.put("totalRows",pagerFileService.getMyPagerFileListTotal(pareMap));
				}
				pagerFile = pagerFileService.getMyPagerFileList(pareMap);
				fileAndPagerFile.put("pagerFile",pagerFile);
				responseDbCenter.setResModel(fileAndPagerFile);
			}else{
				Map<String, Object> fileAndPagerFile = getFileAndPagerFile(likeName, ttype, pagerFileId, deleteFlag, null, StringUtils.isNotBlank(pagerFileId) ? pagerFileService.getPagerFileById(pagerFileId) : null,req);
				if(StringUtils.isBlank(likeName) ){
					String pathName = fileAndPagerFile.get("pathName").toString();
					if(StringUtils.isNotBlank(share) && share.equals("share") && !pathName.equals("所有共享文档")){
						fileAndPagerFile.put("pathName", "所有共享文档/"+pathName);
						fileAndPagerFile.put("pagerPath", "00/"+fileAndPagerFile.get("pagerPath"));
					}
				}
				responseDbCenter.setResModel(fileAndPagerFile);
			}

			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			log.error("异常栈:",e);

			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日 pagerFileId=0查询未分类文件
	 * @功能描述:查询文件夹和文件夹下面的文件
	 */

	public Map<String, Object> getFileAndPagerFile(
			String likeName, // 修改时传的Id
			String ttype, // 排序的方式
			String pagerFileId,
			String deleteFlag,//0可以删除 1不可以删除
			String flag,//系统文档还是自定义
			Map<String, String> pagerFile,HttpServletRequest req) throws Exception {
		Map<String, Object> fileAndPagerFile = new HashMap<>();
		ttype = StringUtils.isBlank(ttype) ? "updateDate" : ttype;
		List<HashMap<String, Object>> documentList = null;
		String pageNo1 = req.getParameter("currentPage");
		String pageSize1 = req.getParameter("pageSize");

		int pageNo = 0;
		int pageSize = 60;
		int pagerTotal = 0;
		int documentTotal = 0;
		if(StringUtils.isNotBlank(pageNo1)){
			pageNo = Integer.valueOf(pageNo1);
			if(StringUtils.isNotBlank(pageSize1)){
				pageSize = Integer.valueOf(pageSize1);
			}
			pageNo = (pageNo -1)*pageSize;
		}
		Map<String, List<HashMap<String, Object>>> all = null;
		Map<String,Object> documentMap = new HashMap();
		documentMap.put("pageNo",pageNo);
		documentMap.put("pageSize",pageSize);
		documentMap.put("pagerFileId",pagerFileId);
		documentMap.put("likeName",likeName);
		documentMap.put("ttype",ttype.equals("fileByte")?"fileByte+0":ttype);
		documentMap.put("deleteFlag",deleteFlag);
		documentMap.put("flag",flag);

		String token = req.getHeader("token");
		String userId = req.getSession().getAttribute(token).toString();
		Map<String, Object> user = userService.selectUserById(userId);
		String departmentId = user.get("departmentId").toString();
		if(StringUtils.isBlank(flag)){//非系统文档相关参数
			documentMap.put("departmentId",user.get("roleId").equals("1")?null:departmentId);
			documentMap.put("userId",user.get("roleId").equals("1")?null:userId);
			documentMap.put("shareDelete",deleteFlag);
			documentMap.put("deleteFlag",null);
			documentMap.put("pagerFileStatus",pagerFileId.equals("00") ? "share":req.getParameter("pagerFileStatus"));
		}else{
			documentMap.put("userId",null);
			documentMap.put("departmentId",null);
			documentMap.put("pagerFileStatus",null);
			documentMap.put("pcCompanyId",user.get("roleId").equals("1") ? null:user.get("pcCompanyId").toString());
			//List<String> depList =  user.get("roleId").equals("1") ? null : userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,userId);
			//documentMap.put("depList",depList);
			departmentId = null;
			userId = null;
		}
		//文件
		if ((StringUtils.isNotBlank(deleteFlag) && "1".equals(deleteFlag))) {
			documentList = new ArrayList();
		} else {
			ttype = StringUtils.isBlank(ttype) ? "updateDate":ttype;

			all = pagerFileService.getDocumentAndPagerFileList(documentMap);
			//documentList = pagerFileService.getDocumentList(documentMap);
			documentList = all.get("documentList");
			fileAndPagerFile.put("file", documentList);
			documentTotal = pagerFileService.getDocumentListTotal(documentMap);
		}
		//获取文件所在路径
		if (documentList != null && StringUtils.isNotBlank(likeName)) {
			for (HashMap<String, Object> document : documentList) {
				getPathNameAndPagerPath(document,document.get("pagerPath").toString(),departmentId,userId);
			}
		}
		//文件夹
		List<HashMap<String, Object>> pagerFileList = new ArrayList<>();
		if (StringUtils.isNotBlank(ttype) && ttype.equals("fileName")) {
			ttype = "pagerName";
		} else {
			ttype = "updateDate";
		}
		documentMap.put("ttype",ttype);

		pagerFileList = pagerFileService.getPagerFileListInIds(all == null ? pagerFileService.getDocumentAndPagerFileList(documentMap).get("pagerFileIdsList"):all.get("pagerFileIdsList"),ttype,documentMap.get("userId"),documentMap.get("departmentId"));
		fileAndPagerFile.put("pagerFile", pagerFileList);
		pagerTotal = pagerFileService.getPagerFileListTotal(documentMap);
		//获取所有检索文件夹所在路径
		if (StringUtils.isNotBlank(likeName) && null != pagerFileList) {
			for (HashMap<String, Object> pf : pagerFileList) {
				getPathNameAndPagerPath(pf,pf.get("pagerPath").toString(),departmentId,userId);
			}
		}

		//获取当前文件夹路径
		if (StringUtils.isNotBlank(pagerFileId) && StringUtils.isBlank(likeName) && null != pagerFile) {
				String path = pagerFile.get("pagerPath");
				getPathNameAndPagerPath(fileAndPagerFile,path,departmentId,userId);
		}
		fileAndPagerFile.put("totalRows",pagerTotal+documentTotal);
		return fileAndPagerFile;
	}
	//加载文件的中文路径
	private void getPathNameAndPagerPath(Map<String, Object> fileAndPagerFile, String path, String departmentId, String userId) {
		List<Map<String, String>> fileList = pagerFileService.getPagerFileInId("'" + path.replaceAll("/", "','") + "'",departmentId,userId);

		Map<String, String> tempMap = new HashMap<>();
		for (Map<String, String> files : fileList) {
			String id = files.get("id");
			tempMap.put(id, files.get("pagerName"));
		}
		if(tempMap.size() == 0){
			fileAndPagerFile.put("pathName", "所有共享文档");
			fileAndPagerFile.put("pagerPath", "00");
		}else if(tempMap.size() != path.split("/").length){
			String newsPath = getPagerFileNamePath(path, tempMap);
			fileAndPagerFile.put("pathName", newsPath.substring(1, newsPath.length()));
			fileAndPagerFile.put("pagerPath", path.substring(37,path.length()));
		}else{
			String newsPath = getPagerFileNamePath(path, tempMap);
			fileAndPagerFile.put("pathName", newsPath.substring(1, newsPath.length()));
			fileAndPagerFile.put("pagerPath", path);
		}
	}
	//新增方案文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeProgramPagerFile")
	public ResponseDbCenter changeProgramPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
												 String parentId, // 上级文件夹ID
												   @RequestHeader  String token,
												 String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//新增项目文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeProjectPagerFile")
	public ResponseDbCenter changeProjectPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
												   String parentId, // 上级文件夹ID
												   @RequestHeader   String token,
												   String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//新增商机文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeBusinessPagerFile")
	public ResponseDbCenter changeBusinessPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
												   String parentId, // 上级文件夹ID
												  @RequestHeader String token,
												   String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//新增任务文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeTaskPagerFile")
	public ResponseDbCenter changeTaskPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
													String parentId, // 上级文件夹ID
												@RequestHeader 	String token,
													String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//新增企业文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeCompanyPagerFile")
	public ResponseDbCenter changeCompanyPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
												String parentId, // 上级文件夹ID
												@RequestHeader String token,
												String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//新增自定义文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeOtherPagerFile")
	public ResponseDbCenter changeOtherPagerFile(HttpServletRequest req, @RequestParam(required = false) String id, // 修改时传的Id
											String parentId, // 上级文件夹ID
												 @RequestHeader String token,
											String pagerFileName// 要保存的文件夹名称
	) throws Exception {
		try {
			return changePagerFile(req,token,pagerFileName,parentId,id);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	private ResponseDbCenter changePagerFile(HttpServletRequest req,@RequestHeader  String token, String pagerFileName, String parentId, String id) {
		Map<String, String> pagerFile = pagerFileService.getPagerFileById(parentId);

		String userId = (String) req.getSession().getAttribute(token);
		if (null == pagerFile) {
			// 当前登录用户的Id
			pagerFileService.initializationCompanyFolder(parentId, userId);
			pagerFile = pagerFileService.getPagerFileById(parentId);
		}

		Map<String, String> pagerFileObj = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			pagerFileObj.put("flag", "add");
			id = UUID.randomUUID().toString();
			String path = pagerFile.get("pagerPath") + "/" + id;
			pagerFileObj.put("parentId", parentId);
			pagerFileObj.put("pagerPath", path);

			FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path);
		} else {
			pagerFileObj = pagerFileService.getPagerFileById(id);

			pagerFileObj.put("flag", "edit");
			if (pagerFileName.equals(pagerFileObj.get("pagerName"))) {
				return ResponseConstants.SUCCESS;
			}else{
				List<Map<String, String>> pagerFileByName = pagerFileService.getPagerFileByPidAndName(parentId,pagerFileName);
				if(pagerFileByName.size() > 0){
					return ResponseConstants.PAGERFILE_PAGERFILE_NAME_ERROR;
				}
			}
		}


		String uname = (String) req.getSession().getAttribute(token + "uname");

		//企业文档动态-------------
		if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", UUID.randomUUID().toString());
			paramMap.put("groupId", pagerFile.get("ttype"));
			paramMap.put("groupName", pagerFileName);
			paramMap.put("contentValue", pagerFileObj.get("flag").equals("add") ?
					uname + "新增了 文件夹 \"" + pagerFileName + "\" " :
					uname + "修改了 文件夹  \"" + pagerFileObj.get("pagerName") + "\" 为 \"" + pagerFileName + "\"");
			paramMap.put("updatedBy", userId);
			paramMap.put("createdBy", userId);
			paramMap.put("ttype", 0);
			paramMap.put("operateType", "添加");
			userDynamicService.saveUserDynamic(paramMap);
		}

		pagerFileObj.put("id", id);
		pagerFileObj.put("pagerName", pagerFileName);
		pagerFileObj.put("ttype", pagerFile == null ? parentId : pagerFile.get("ttype"));
		pagerFileObj.put("userId", userId);
		List<Map<String, String>> pagerFileList = pagerFileService.getPagerFileByPidAndName(parentId, pagerFileName);
		if (pagerFileList != null && pagerFileList.size() > 0) {// 新建文件夹
			return ResponseConstants.PAGERFILE_PAGERFILE_NAME_ERROR;
		}
		pagerFileService.changePagerFile(pagerFileObj);
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel("SUCCESS");
		return responseDbCenter;
	}


	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:批量新增文件夹分类
	 */
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/changeFolder")
	public ResponseDbCenter changeFolder(HttpServletRequest req,
										 @RequestHeader  String token, String fileList// 要保存的文件夹名称
	) throws Exception {
		//try {
		/*Map<String,Object> pagerFileMap = new HashMap<>();
		pagerFileMap.put("ttype","updateDate");
		pagerFileMap.put("pageNo",0);
		pagerFileMap.put("pageSize",600);
		List<HashMap<String, Object>> folderlist = pagerFileService.getPagerFileList(pagerFileMap);
		List<Object> tempList = new ArrayList<>();
		Map<Object, Object> tempMap = new HashMap<>();
		for (HashMap<String, Object> hashMap : folderlist) {
			if (hashMap.get("pagerFileNumber").toString().equals("0") && hashMap.get("fileNumber").toString().equals("0")) {
				tempList.add(hashMap.get("id"));
			}
			tempMap.put(hashMap.get("id"), hashMap.get("pagerName"));
		}*/
		List<Map<String, String>> list = (List<Map<String, String>>) JSON.parse(fileList);

		for (Map<String, String> map : list) {
			SolrLogger.loggerInfo(map.toString());
			String id = map.get("id");
			String pagerFileName = map.get("pagerName");
			Map<String, String> pagerFileObj = new HashMap<>();
			if (StringUtils.isBlank(id) || id.equals("")) {
				pagerFileObj.put("flag", "add");
				id = UUID.randomUUID().toString();
				pagerFileObj.put("pagerPath", id);
				FileUtil.newFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + id);// 新建文件夹
				List<Map<String, String>> pagerObj = pagerFileService.getPagerFileByPidAndName(null, pagerFileName);
				if (null != pagerObj && pagerObj.size() > 0) {
					//pagerFileName = pagerFileName + "(" + DateUtils.currtimeTolong19() + ")";
					return ResponseConstants.PAGERFILE_PAGERFILE_NAME_ERROR;
				}
			} else {
				//tempList.remove(id);
				pagerFileObj.put("flag", "edit");
//				if (tempMap.get(id).equals(pagerFileName)) {
//					pagerFileObj.put("flag", "no");
//				} else {
					List<Map<String, String>> pagerObj = pagerFileService.getPagerFileByPidAndName(null, pagerFileName);
					if (null != pagerObj && pagerObj.size() > 0) {
						//pagerFileName = pagerFileName + "(" + DateUtils.currtimeTolong19() + ")";
						return ResponseConstants.PAGERFILE_PAGERFILE_NAME_ERROR;
					}
				//}
			}
			pagerFileObj.put("id", id);
			pagerFileObj.put("pagerName", pagerFileName);
			pagerFileObj.put("userId", req.getSession().getAttribute(token).toString());
			pagerFileService.changePagerFile(pagerFileObj);
		}
//		if (null != tempList) {
//
//			for (Object id : tempList) {
//				pagerFileService.deletePagerFileById(String.valueOf(id), null, null);
//				FileUtil.moveFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + id, PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + id + DateUtils.currtimeTolong19());
//			}
//		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel("SUCCESS");
		return responseDbCenter;
		/*} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}

	//删除方案文件夹Program
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteProgramFile")
	public ResponseDbCenter deleteProgramFile(HttpServletRequest req, String fileList,@RequestHeader  String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//删除项目文件夹Project
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteProjectFile")
	public ResponseDbCenter deleteProjectFile(HttpServletRequest req, String fileList,@RequestHeader  String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//删除企业文件夹Company
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteCompanyFile")
	public ResponseDbCenter deleteCompanyFile(HttpServletRequest req, String fileList,@RequestHeader  String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//删除商机文件夹Business
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteBusinessFile")
	public ResponseDbCenter deleteBusinessFile(HttpServletRequest req, String fileList, @RequestHeader String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//删除任务文件夹Task
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteTaskFile")
	public ResponseDbCenter deleteTaskFile(HttpServletRequest req, String fileList,@RequestHeader  String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
	//删除文件
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/deleteOtherFile")
	public ResponseDbCenter deleteOtherFile(HttpServletRequest req, String fileList,@RequestHeader  String token) throws Exception {
		try {
			deleteFile(req,fileList,token);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel("SUCCESS");
			return responseDbCenter;
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	private void deleteFile(HttpServletRequest req, String fileList,@RequestHeader  String token) {
		List<Map<String, String>> deleteList = (List<Map<String, String>>) JSON.parse(fileList);
		String userId = (String) req.getSession().getAttribute(token);
		String uname = (String) req.getSession().getAttribute(token + "uname");
		for (Map<String, String> map : deleteList) {
			String id = map.get("id");
			if (map.get("ttype").equals("file")) {
				Document document = documentService.selectDocumentById(id);
				FileUtil.moveFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH), PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/delete/" + document.getFileName());
				documentService.deleteDocumentById(id, req.getSession().getAttribute(token).toString());

				//企业文档动态-------------
				if (StringUtils.isNotBlank(document.getTtypeId())) {
					userDynamicService.addUserDynamic(userId, document.getTtypeId(), document.getFileName(), "删除", "删除了 \"" + document.getFileName() + "\"", 0,null,null,null);
				}
			} else if (map.get("ttype").equals("pagerFile")) {

				Map<String, String> pagerFile = pagerFileService.getPagerFileById(id);
				String deleleNewsPath = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/delete/";
				String deleleNewsName = DateUtils.currtimeToString8() + "/" + pagerFile.get("id");
				FileUtil.moveFolder(PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + pagerFile.get("pagerPath"), deleleNewsPath + deleleNewsName);
				pagerFileService.deletePagerFileById(id, deleleNewsPath, deleleNewsName);
				//企业文档动态-------------
				if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {
					String pagerName = pagerFile.get("pagerName");

					userDynamicService.addUserDynamic(userId, pagerFile.get("ttype"), pagerName, "删除", "删除了 \"" + pagerName + "\" 所有文件", 0,null,null,null);
				}
				if(StringUtils.isNotBlank(pagerFile.get("pagerPath")) && pagerFile.get("pagerPath").length() < 75){
					pagerFileService.deleteSetShare(id,null);
				}

			}
		}
	}
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadCompanyFileZip")
	public ResponseDbCenter downloadCompanyFileZip(HttpServletRequest req,
												   @ApiParam(value = "文件夹id,id,id") String pagerFileId,
												   @ApiParam(value = "文件id,id,id") String fileId,
												   @RequestHeader   String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}
//任务
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadTaskFileZip")
//3a444cb7-fee5-48df-8ba9-fff20366c907
	public ResponseDbCenter downloadTaskFileZip(HttpServletRequest req,
												   @ApiParam(value = "文件夹id,id,id") String pagerFileId,
												   @ApiParam(value = "文件id,id,id") String fileId,
												@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//项目
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadProjectFileZip")
//3a444cb7-fee5-48df-8ba9-fff20366c907
	public ResponseDbCenter downloadProjectFileZip(HttpServletRequest req, String pagerFileId, String fileId,@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//方案
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadProgramFileZip")
	public ResponseDbCenter downloadProgramFileZip(HttpServletRequest req, String pagerFileId, String fileId,@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}





	//商机
	@ApiOperation(value = "下载企业下商机下文件")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadBusinessOpportunityFileZipUnderCompany")
	public ResponseDbCenter downloadBusinessOpportunityFileZipUnderCompany(HttpServletRequest req, String pagerFileId, String fileId, @RequestHeader String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	//商机
	@ApiOperation(value = "下载商机下文件")
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadBusinessOpportunityFileZip")
//3a444cb7-fee5-48df-8ba9-fff20366c907
	public ResponseDbCenter downloadBusinessOpportunityFileZip(HttpServletRequest req, String pagerFileId, String fileId,@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}






	@ApiOperation(value = "上传企业下商机文档")
	@ResponseBody
	@RequestMapping(value = "uploadBusinessOpportunityDocumentUnderCompany",method = RequestMethod.POST)
	public ResponseDbCenter uploadBusinessOpportunityDocumentUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
																	   @RequestParam MultipartFile[] files,
																	   @ApiParam(value = "商机Id",required = true) @RequestParam(required = true) String pagerFileId,
																	   String appFlag) {
		try{
		List<Map<String, String>> listFile = new ArrayList<>();

		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");


		//查看是否有公司下某个指定的商机文件夹
		Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);


		String path =pagerFile.get("pagerPath");
		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path;
		FileUtil.newFolder(uploadPath);// 没有就创建文件夹，有就不创建

		for (MultipartFile file : files) {
			Map<String, String> fileObj = FileUtil.getFileNameAdd(file.getOriginalFilename(), false);
			String fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
			String size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			if (size.equals("fail")) {
				return null;
			} else if (size.equals("error")) {
				fileObj = FileUtil.getFileNameAdd(fileName, true);
				fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
				size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			}
			// 前台用户显示的文件名
			String httpPath = PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/" + path + "/" + fileName;

			Document document = new Document();
			document.setId(UUID.randomUUID().toString());
			document.setFileName(fileName);
			document.setPagerFileId(pagerFileId);
			document.setPath(httpPath);
			document.setCreatedBy(loginUserId);
			document.setFileSize(FileUtil.getPrintSize(size));
			document.setFileByte(size);
			document.setExt(fileObj.get("foot"));
			document.setTtypeId(pagerFile == null ? null : pagerFile.get("ttype"));
			document.setTtype(0);
			Map<String, String> fileAttr = new HashMap<>();
			fileAttr.put("imgSrc", httpPath);
			fileAttr.put("imgName", fileName);
			fileAttr.put("id", document.getId());
			listFile.add(fileAttr);
			documentService.saveDocument(document);
			//企业文档动态-------------
			if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {

				userDynamicService.addUserDynamic(loginUserId, document.getTtypeId(), fileName, "上传", "上传了 \"" + document.getFileName() + "\"", 0,null,null,null);

			}

		}

		ResponseDbCenter responseDbCenter=new ResponseDbCenter();
		responseDbCenter.setResModel(listFile);
		return responseDbCenter;
	}catch (Exception e){
		throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
	}

	}




	@ApiOperation(value = "上传商机文档")
	@ResponseBody
	@RequestMapping(value = "uploadBusinessOpportunityDocument",method = RequestMethod.POST)
	public ResponseDbCenter uploadBusinessOpportunityDocument(HttpServletRequest req, HttpServletResponse rsp,
																	   @RequestParam MultipartFile[] files,
 																	   @ApiParam(value = "商机Id",required = true) @RequestParam(required = true) String pagerFileId,
																	   String appFlag) {
		try {

		List<Map<String, String>> listFile = new ArrayList<>();

		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		//查看是否有公司下某个指定的商机文件夹
		Map<String, String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
		String path =pagerFile.get("pagerPath");
		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/" + path;
		FileUtil.newFolder(uploadPath);// 没有就创建文件夹，有就不创建

		for (MultipartFile file : files) {
			Map<String, String> fileObj = FileUtil.getFileNameAdd(file.getOriginalFilename(), false);
			String fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
			String size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			if (size.equals("fail")) {
				return null;
			} else if (size.equals("error")) {
				fileObj = FileUtil.getFileNameAdd(fileName, true);
				fileName = StringUtils.isNotBlank(appFlag) && appFlag.equals("xcx") ? "xcxFile-" + UUID.randomUUID().toString() + "." + fileObj.get("foot") : fileObj.get("name");// 获取上传的文件名字
				size = FileUtil.saveFile(file, uploadPath + "/" + fileName);// 保存文件并返回文件大小
			}
			// 前台用户显示的文件名
			String httpPath = PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/" + path + "/" + fileName;

			Document document = new Document();
			document.setId(UUID.randomUUID().toString());
			document.setFileName(fileName);
			document.setPagerFileId(pagerFileId);
			document.setPath(httpPath);
			document.setCreatedBy(loginUserId);
			document.setFileSize(FileUtil.getPrintSize(size));
			document.setFileByte(size);
			document.setExt(fileObj.get("foot"));
			document.setTtypeId(pagerFile == null ? null : pagerFile.get("ttype"));
			document.setTtype(0);
			Map<String, String> fileAttr = new HashMap<>();
			fileAttr.put("imgSrc", httpPath);
			fileAttr.put("imgName", fileName);
			fileAttr.put("id", document.getId());
			listFile.add(fileAttr);
			documentService.saveDocument(document);
			//企业文档动态-------------
			if (pagerFile != null && StringUtils.isNotBlank(pagerFile.get("ttype"))) {

				userDynamicService.addUserDynamic(loginUserId, pagerFileId, fileName, "上传", "上传了 \"" + document.getFileName() + "\"", 0,null,null,null);

			}

		}

;		ResponseDbCenter responseDbCenter=new ResponseDbCenter();
		  	responseDbCenter.setResModel(listFile);
			return responseDbCenter;
		}catch (Exception e){
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
 	}


	//自定义
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/downloadOtherFileZip")
//3a444cb7-fee5-48df-8ba9-fff20366c907
	public ResponseDbCenter downloadOtherFileZip(HttpServletRequest req, String pagerFileId, String fileId,@RequestHeader  String token) throws Exception {
		try {
			if (StringUtils.isBlank(pagerFileId) && StringUtils.isBlank(fileId)) {
				return ResponseConstants.MISSING_PARAMTER;
			}
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(downloadFileZip(pagerFileId, fileId));
			return responseDbCenter;
		} catch (Exception e) {

			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	/**
	 * @return
	 * @author: gaoddO
	 * @2017年3月22日
	 * @功能描述:下载
	 */
	public String downloadFileZip(String pagerFileId, String fileId) throws Exception {
		String data = DateUtils.currtimeToString14();
		Map<String, String> pagerFile = null;
		String pagerName = "";
		String url = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/";
		Map<String,Integer> onlyMy = new HashMap<>();
		if (StringUtils.isNotBlank(fileId)) {
			FileUtil.newFolder(url + "download/" + data);

			if (!fileId.contains(",")) {
				Document document = documentService.selectDocumentById(fileId);
				pagerFile = pagerFileService.getPagerFileById(document.getPagerFileId());
				pagerName = pagerFile.get("pagerName");
				//String newsPath = "/" + pagerName;

				//FileUtil.newFolder(url + "download/" + data + newsPath);
				//FileUtil.copyFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),url + "download/" + data + newsPath + "/" + document.getFileName());
				FileUtil.copyFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),url + "download/" + data  + "/" + document.getFileName());
			} else {
				int sum = 0;
				String newsPath = "";
				for (String id : fileId.split(",")) {
					Document document = documentService.selectDocumentById(id);
					/*if (sum == 0) {
						pagerFile = pagerFileService.getPagerFileById(document.getPagerFileId());
						pagerName = pagerFile.get("pagerName");
						newsPath = "/" + pagerName;
						FileUtil.newFolder(url + "download/" + data);
						FileUtil.newFolder(url + "download/" + data + newsPath);
					}*/
					sum++;//选择下载的文件个数
					//FileUtil.copyFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),url + "download/" + data + newsPath + "/" + document.getFileName());
					String tempPath = url + "download/" + data  + "/" + document.getFileName();
					if(onlyMy.get(tempPath+"wenjian") == null ){
						FileUtil.copyFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),tempPath);
						onlyMy.put(tempPath+"wenjian",1);
					}else{
						int onlyMyCnt = onlyMy.get(tempPath+"wenjian");
						String newsFileName = url + "download/" + data  + "/" + (document.getFileName().replace(".","("+(onlyMyCnt++)+")."));
						FileUtil.copyFile(document.getPath().replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),newsFileName);
						onlyMy.put(tempPath+"wenjian",onlyMyCnt);
					}

				}
			}
			//data = data + "/" + pagerName;
		}

		if (StringUtils.isNotBlank(pagerFileId)) {
			if (!pagerFileId.contains(",")) {
				//pagerFile = pagerFileService.getPagerFileById(pagerFileId);
				downloadPre(pagerFileId, data,onlyMy);
				/*if (StringUtils.isBlank(fileId)) {
					pagerName = pagerFile.get("pagerName");
					data = data + "/" + pagerName;
				}*/
			} else {
				int sum = 0;
				for (String id : pagerFileId.split(",")) {
					//pagerFile = pagerFileService.getPagerFileById(id);
					/*if (sum == 0 && StringUtils.isBlank(fileId)) {
						String parentPagerFileId = pagerFile.get("parentPagerFileId");
						pagerFile = pagerFileService.getPagerFileById(parentPagerFileId);
						pagerName = pagerFile.get("pagerName");
						FileUtil.newFolder(url + "download/" + data);
						data = data + "/" + pagerName;
					}
					sum++;//选择下载的文件jia个数*/

					downloadPre(id, data,onlyMy);
				}
			}
		}
		String filePathZip = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/download/" + data;
		ZipUtils.compress(filePathZip, new File(filePathZip + ".zip"));

		return PropertiesUtil.FILE_HTTP_PATH + "wendanguanli/download/" + data + ".zip";

	}

	//打包这个文件甲里面所有的
	private void downloadPre(String pagerFileId, String data,Map<String,Integer> onlyMy) {
		List<Map<String, String>> fileList = pagerFileService.getPagerFileAndDocumentLikePath(pagerFileId);

		String url = PropertiesUtil.FILE_UPLOAD_PATH + "wendanguanli/";

		FileUtil.newFolder(url + "download/" + data);
		Map<String, String> tempMap = new HashMap<>();

		Map<String,String> mmap = new HashMap<>();
		for (Map<String, String> map : fileList) {
			String pathId = map.get("pagerPath");
			tempMap.put(map.get("id"), map.get("pagerName"));
			String newsPath = getPagerFileNamePath(pathId, tempMap);//map.get("pagerName");

			String tempPath = url + "download/" + data + newsPath;

			if(null == mmap.get(pathId)){
				mmap.put(pathId,"true");
				if(onlyMy.get(tempPath+"wenjianjia") == null){
					FileUtil.newFolder(tempPath);
					onlyMy.put(tempPath+"wenjianjia",0);
				}else{
					int onlyMyCnt = onlyMy.get(tempPath+"wenjianjia");
					onlyMyCnt++;
					onlyMy.put(tempPath+"wenjianjia",onlyMyCnt);
					tempPath = tempPath+"("+onlyMyCnt+")";
					FileUtil.newFolder(tempPath);
				}
			}
			String path = map.get("path");
			if (StringUtils.isNotBlank(path)) {
				FileUtil.copyFile(path.replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH),
						tempPath + "/" + map.get("fileName"));
			}
		}
	}


	private String getPagerFileNamePath(String pathId, Map<String, String> tempMap) {
		if (!pathId.contains("/")) {
			return "/" + tempMap.get(pathId);
		} else {
			String paths[] = pathId.split("/");
			String pathTemp = "";
			for (String id : paths) {
				String name = tempMap.get(id);
				if (StringUtils.isNotBlank(name)) {
					pathTemp += "/" + tempMap.get(id);
				}
			}
			return pathTemp;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/downloadFile")//3a444cb7-fee5-48df-8ba9-fff20366c907
	public ResponseEntity<byte[]> downloadFile(HttpServletRequest req, HttpServletResponse response,String path){
		if(StringUtils.isNotBlank(path)){
			File file = new File(path.replaceAll(PropertiesUtil.FILE_HTTP_PATH, PropertiesUtil.FILE_UPLOAD_PATH));
			if (file.exists()) { // 文件存在时
				org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
				String fileName = null;//为了解决中文名称乱码问题
				try {
					fileName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
					httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					httpHeaders.setContentDispositionFormData("attachment", fileName);
					return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
				} catch (Exception e) {
					log.error("异常栈:",e);
				}
			}
		}
		return null;
	}
	//获取分享给我的文件夹
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getShareMyPagerFileList")
	public ResponseDbCenter getShareMyPagerFileList(HttpServletRequest req,@RequestHeader String token) throws Exception {
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		String userId = req.getSession().getAttribute(token).toString();
		Map<String, Object> user = userService.selectUserById(userId);
		String departmentId = user.get("departmentId").toString();
		Map<String, Object> fileAndPagerFile = new HashMap<>();

		Map<String,Object> pareMap = new HashMap();
		pareMap.put("departmentId",departmentId);
		pareMap.put("userId",userId);
		pareMap.put("status","share");
		pareMap.put("ttype","updateDate");
		List<HashMap<String,Object>>  pagerFileList =pagerFileService.getMyPagerFileList(pareMap);
		fileAndPagerFile.put("pagerFile",pagerFileList);
		fileAndPagerFile.put("pagerPath", "00");
		fileAndPagerFile.put("pathName", "所有共享文档");
		fileAndPagerFile.put("totalRows",pagerFileService.getMyPagerFileListTotal(pareMap));

		responseDbCenter.setResModel(fileAndPagerFile);
		return responseDbCenter;
	}

	//分享文件夹给其他人
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/setShare")
	public ResponseDbCenter setShare(HttpServletRequest req,@RequestHeader  String token,String pagerFileId,String departmentIds,String userIds,String level,String pagerName) throws Exception {
		if(StringUtils.isBlank(pagerFileId) || StringUtils.isBlank(level) ){
			return ResponseConstants.MISSING_PARAMTER;
		}
		if(StringUtils.isNotBlank(pagerName)){
			Map<String,String> pagerFile = pagerFileService.getPagerFileById(pagerFileId);
			if(!pagerFile.get("pagerName").equals(pagerName)){
				List<Map<String, String>> pagerObj = pagerFileService.getPagerFileByPidAndName(null, pagerName);
				if (null != pagerObj && pagerObj.size() > 0) {
					return ResponseConstants.PAGERFILE_PAGERFILE_NAME_ERROR;
				}
				pagerFile.put("flag","edit");
				pagerFile.put("pagerName",pagerName);
				pagerFileService.changePagerFile(pagerFile);
			}

		}
		pagerFileService.deleteSetShare(pagerFileId,level);
		if(StringUtils.isNotBlank(departmentIds)){
			for (String departmentId:departmentIds.split(",")) {
				Map<String,String> pagerFileUser = new HashMap<>();
				pagerFileUser.put("userOrDepartmentId",departmentId);
				pagerFileUser.put("ttype","3");
				pagerFileUser.put("level",level);
				pagerFileUser.put("pagerFileId",pagerFileId);
				pagerFileUser.put("id",UUID.randomUUID().toString());
				pagerFileService.savePagerFileUser(pagerFileUser);
			}
		}
		if(StringUtils.isNotBlank(userIds)){
			for (String userId:userIds.split(",")) {
				Map<String,String> pagerFileUser = new HashMap<>();
				pagerFileUser.put("userOrDepartmentId",userId);
				pagerFileUser.put("ttype","2");
				pagerFileUser.put("level",level);
				pagerFileUser.put("pagerFileId",pagerFileId);
				pagerFileUser.put("id",UUID.randomUUID().toString());
				pagerFileService.savePagerFileUser(pagerFileUser);
			}
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

	//获取文件夹分享给的用户和部门
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getShareUserAndDepartment")
	public ResponseDbCenter getShareUserAndDepartment(HttpServletRequest req,@RequestHeader String token,String pagerFileId,String level) throws Exception {
		if(StringUtils.isBlank(pagerFileId)  || StringUtils.isBlank(level)  ){
			return ResponseConstants.MISSING_PARAMTER;
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(pagerFileService.getUserAndDepartment(pagerFileId,level));
		return responseDbCenter;
	}
	//获取中文文件名文件的英文名称
	@ResponseBody
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/getXcxFileEnglishName")
	public ResponseDbCenter getXcxFileEnglishName(HttpServletRequest req,String path,String id) throws Exception {
		if(StringUtils.isBlank(path)  || StringUtils.isBlank(id)  ){
			return ResponseConstants.MISSING_PARAMTER;
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();

		int endIndex = path.lastIndexOf(".");
		String foot = path.substring(endIndex, path.length());
		String fileEnglishName = "wendanguanli/delete/"+id+foot;

		String http = PropertiesUtil.FILE_HTTP_PATH;
		String upload = PropertiesUtil.FILE_UPLOAD_PATH;

		File oldfile = new File(upload+fileEnglishName);
		if (!oldfile.exists()) { // 文件存在时
			FileUtil.copyFile(path.replaceAll(http, upload),upload+fileEnglishName);
		}
		responseDbCenter.setResModel(http+fileEnglishName);
		return responseDbCenter;
	}


}