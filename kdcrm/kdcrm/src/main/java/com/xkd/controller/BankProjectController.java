package com.xkd.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.DateUtils;
/**
 * 
 * @date 2017年10月20日
 * @author xiaoz
 * 银行项目
 */
@Api(description = "银行项目相关接口")
@Transactional
@Controller
@RequestMapping("/bankProject")
public class BankProjectController extends BaseController {

	
	@Autowired
	private BankProjectService bankProjectService;
	@Autowired
	private UserDynamicService userDynamicService;
	@Autowired
	private PagerFileService pagerFileService;
	@Autowired
	private BankPointService bankPointService;

	@Autowired
	private  BankUserService bankUserService;

	@Autowired
	private UserService userService;
	@Autowired
	private UserDataPermissionService userDataPermissionService;
	@Autowired
	private DepartmentService departmentService;


	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月13日 
	 * @功能描述:根据id查询银行项目
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectBankProjectById",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter selectBankProjectById(HttpServletRequest req,HttpServletResponse rsp){
		
		String id = req.getParameter("id");
		
		Map<String,Object> resultMap = null;
		
		if (StringUtils.isBlank(id)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {

			List<Map<String,Object>> userTypeMapList = new ArrayList<>();
			List<Map<String,Object>> objectTypeMapList = new ArrayList<>();
			List<Map<String,Object>> projectTypeMapList = new ArrayList<>();

		    resultMap = bankProjectService.selectBankProjectById(id);

		    if(resultMap != null && resultMap.size() > 0){

				String userTypeOneId = resultMap.get("userTypeOneId") == null?null:(String)resultMap.get("userTypeOneId");
				String userTypeTwoId = resultMap.get("userTypeTwoId") == null?null:(String)resultMap.get("userTypeTwoId");
				String userTypeThreeId = resultMap.get("userTypeThreeId") == null?null:(String)resultMap.get("userTypeThreeId");
				String objectTypeOneId = resultMap.get("objectTypeOneId") == null?null:(String)resultMap.get("objectTypeOneId");
				String objectTypeTwoId = resultMap.get("objectTypeTwoId") == null?null:(String)resultMap.get("objectTypeTwoId");
				String projectTypeId = resultMap.get("projectTypeId") == null?null:(String)resultMap.get("projectTypeId");
				String sonProjectTypeId = resultMap.get("sonProjectTypeId") == null?null:(String)resultMap.get("sonProjectTypeId");

				String userTypeOne = resultMap.get("userTypeOne") == null?null:(String)resultMap.get("userTypeOne");
				String userTypeTwo = resultMap.get("userTypeTwo") == null?null:(String)resultMap.get("userTypeTwo");
				String userTypeThree = resultMap.get("userTypeThree") == null?null:(String)resultMap.get("userTypeThree");
				String objectTypeOne = resultMap.get("objectTypeOne") == null?null:(String)resultMap.get("objectTypeOne");
				String objectTypeTwo = resultMap.get("objectTypeTwo") == null?null:(String)resultMap.get("objectTypeTwo");
				String projectType = resultMap.get("projectType") == null?null:(String)resultMap.get("projectType");
				String sonProjectType = resultMap.get("sonProjectType") == null?null:(String)resultMap.get("sonProjectType");

				if(StringUtils.isNotBlank(userTypeOneId)){
					Map<String,Object> userTypeMap = new HashMap<>();
					userTypeMap.put("id",userTypeOneId);
					userTypeMap.put("value",userTypeOne);
					userTypeMapList.add(userTypeMap);
				}
				if(StringUtils.isNotBlank(userTypeTwoId)){
					Map<String,Object> userTypeMap = new HashMap<>();
					userTypeMap.put("id",userTypeTwoId);
					userTypeMap.put("value",userTypeTwo);
					userTypeMapList.add(userTypeMap);
				}
				if(StringUtils.isNotBlank(userTypeThreeId)){
					Map<String,Object> userTypeMap = new HashMap<>();
					userTypeMap.put("id",userTypeThreeId);
					userTypeMap.put("value",userTypeThree);
					userTypeMapList.add(userTypeMap);
				}

				if(StringUtils.isNotBlank(objectTypeOneId)){
					Map<String,Object> objectTypeMap = new HashMap<>();
					objectTypeMap.put("id",objectTypeOneId);
					objectTypeMap.put("value",objectTypeOne);
					objectTypeMapList.add(objectTypeMap);
				}
				if(StringUtils.isNotBlank(objectTypeTwoId)){
					Map<String,Object> objectTypeMap = new HashMap<>();
					objectTypeMap.put("id",objectTypeTwoId);
					objectTypeMap.put("value",objectTypeTwo);
					objectTypeMapList.add(objectTypeMap);
				}

				if(StringUtils.isNotBlank(projectTypeId)){
					Map<String,Object> projectTypeMap = new HashMap<>();
					projectTypeMap.put("id",projectTypeId);
					projectTypeMap.put("value",projectType);
					projectTypeMapList.add(projectTypeMap);
				}
				if(StringUtils.isNotBlank(sonProjectTypeId)){
					Map<String,Object> projectTypeMap = new HashMap<>();
					projectTypeMap.put("id",sonProjectTypeId);
					projectTypeMap.put("value",sonProjectType);
					projectTypeMapList.add(projectTypeMap);
				}

				resultMap.put("userTypeMapList",userTypeMapList);
				resultMap.put("objectTypeMapList",objectTypeMapList);
				resultMap.put("projectTypeMapList",projectTypeMapList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(resultMap);
		
		return responseDbCenter;
	}
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月13日 
	 * @功能描述:查询所有项目信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/selectBankProjectsByContent",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter selectBankProjectsByContent(HttpServletRequest req,HttpServletResponse rsp,
														@ApiParam(value = "搜索值",required = false) @RequestParam(required = false)String content,
														@ApiParam(value = "项目号",required = false) @RequestParam(required = false)String projectCode,
														@ApiParam(value = "开始时间",required = false) @RequestParam(required = false)String startDate,
														@ApiParam(value = "结束时间",required = false) @RequestParam(required = false)String endDate,
														@ApiParam(value = "项目名称",required = false) @RequestParam(required = false)String projectName,
														@ApiParam(value = "项目类型Id",required = false) @RequestParam(required = false)String projectTypeId,
														@ApiParam(value = "项目经理",required = false) @RequestParam(required = false)String projectManager,
														@ApiParam(value = "省",required = false) @RequestParam(required = false)String province,
														@ApiParam(value = "市",required = false) @RequestParam(required = false)String city,
														@ApiParam(value = "客户类型1",required = false) @RequestParam(required = false)String userTypeOneId,
														@ApiParam(value = "客户类型2",required = false) @RequestParam(required = false)String userTypeTwoId,
														@ApiParam(value = "客户类型3",required = false) @RequestParam(required = false)String userTypeThreeId,
														@ApiParam(value = "公司Id",required = false) @RequestParam(required = false)String companyId,
														@ApiParam(value = "部门Id",required = false) @RequestParam(required = false)String departmentId,
														@ApiParam(value = "当前页",required = false) @RequestParam(required = false)String currentPage,
														@ApiParam(value = "每页多少数",required = false) @RequestParam(required = false)String pageSize

														){


		if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){

			currentPage = "1";
			pageSize = "10";
		}

		int  pageSizeInt = Integer.parseInt(pageSize);

		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;


		try {

			Map<String,Object> paramMap = new HashMap<>();

			paramMap.put("content",content);
			paramMap.put("projectCode",projectCode);
			paramMap.put("startDate",startDate);
			paramMap.put("endDate",endDate);
			paramMap.put("currentPage",currentPageInt);
			paramMap.put("pageSize",pageSizeInt);

			paramMap.put("projectName",projectName);
			paramMap.put("projectManager",projectManager);
			paramMap.put("province",province);
			paramMap.put("city",city);
			paramMap.put("userTypeOneId",userTypeOneId);
			paramMap.put("userTypeTwoId",userTypeTwoId);
			paramMap.put("userTypeThreeId",userTypeThreeId);
			paramMap.put("projectTypeId",projectTypeId);
			paramMap.put("companyId",companyId);
			paramMap.put("departmentId",departmentId);


			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> resultMap=bankProjectService.selectBankProjectsByContent(paramMap,loginUserId);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(resultMap.get("list"));
			responseDbCenter.setTotalRows(resultMap.get("total")+"");
			return responseDbCenter;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}





	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月13日 
	 * @功能描述:新增项目
	 * @param req
	 * @param rsp
	 * @param bankProject  : 
	 * {
	    "projectCode": "222",
	    "projectName": "测试项目",
		"companyId": "公司Id",
	    "projectTypeId":"fjaljfafakjfj",
	    "courseName"："cs",
	    "startDate": "2017-10-25",
	    "endDate": "2017-10-30",
	    "trainObject": "培训对象",
	    "trainPlace"："cs",
	    "userName": "客户名称",
	    "feel": "满意度",
	    "programId": "方案ID",
	    "projectManager": "项目经理",
	    "projecUser": "项目人员"
		}
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addBankProject", method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter addBankProject(HttpServletRequest req, HttpServletResponse rsp,

										   @ApiParam(value = " {\n" +
												   "\t    \"projectCode\": \"222\",\n" +
												   "\t    \"projectName\": \"测试项目\",\n" +
												   "\t\t\"companyId\": \"公司Id\",\n" +
												   "\t    \"projectTypeId\":\"fjaljfafakjfj\",\n" +
												   "\t    \"courseName\"：\"cs\",\n" +
												   "\t    \"startDate\": \"2017-10-25\",\n" +
												   "\t    \"endDate\": \"2017-10-30\",\n" +
												   "\t    \"trainObject\": \"培训对象\",\n" +
												   "\t    \"trainPlace\"：\"cs\",\n" +
												   "\t    \"userName\": \"客户名称\",\n" +
												   "\t    \"feel\": \"满意度\",\n" +
												   "\t    \"programId\": \"方案ID\",\n" +
												   "\t    \"projectManager\": \"项目经理\",\n" +
												   "\t    \"projecUser\": \"项目人员\"\n" +
												   "\t\t}") @RequestParam(required = true)  String bankProject) throws Exception{
		
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		if (StringUtil.isBlank(bankProject)) {
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {
			
			Map<String, Object> map  = JSON.parseObject(bankProject, new TypeReference<Map<String, Object>>() {
			});		
			
			String projectCode = map.get("projectCode") == null?"":(String)map.get("projectCode");
			String projectName = map.get("projectName") == null?"":(String)map.get("projectName");
			String departmentId = map.get("departmentId") == null?"":(String)map.get("departmentId");

			if(StringUtils.isBlank(departmentId)){
				Map<String, Object> mapp = userService.selectUserById(loginUserId);
				if(mapp.get("departmentId") != null){
					map.put("departmentId",(String) mapp.get("departmentId"));
				}
			}
			Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);
			List<String>  departmentIdList=departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"),loginUserMap);
			List<Map<String,Object>> projectNameMap = bankProjectService.selectBankProjectByName(projectName,departmentIdList);
			List<Map<String,Object>> projectMap = bankProjectService.selectBankProjectByCodeAllStatus(projectCode);
			
			if(projectMap != null && projectMap.size() > 0){
				
				Integer status = projectMap.get(0).get("status") == null?null:(Integer)projectMap.get(0).get("status");
				if(status == null || status.intValue() == 0){
					
					return ResponseConstants.BANK_PROJECTCODE_EXISTS;
				}
			}
			
			if(projectNameMap != null && projectNameMap.size() > 0){

				Integer status = projectNameMap.get(0).get("status") == null?null:(Integer)projectNameMap.get(0).get("status");
				if(status == null || status.intValue() == 0){

					return ResponseConstants.BANK_PROJECTNAME_EXISTS;
				}
			}
			
			if(projectMap.size() > 0 && projectNameMap.size() > 0){
				
				String cid = projectMap.get(0).get("id") == null?null:(String)projectMap.get(0).get("id");
				String nid = projectNameMap.get(0).get("id") == null?null:(String)projectNameMap.get(0).get("id");
				if(!cid.equals(nid)){
					bankProjectService.deleteBankProjectRealByName(projectName);
				}
			}
			
			map.put("status","0");
			map.put("createdBy",loginUserId );
			map.put("createDate", new Date());
			map.put("updatedBy",loginUserId );
			map.put("updateDate", new Date());

			Map<String, Object> mapp = userService.selectUserById(loginUserId);
			if(mapp !=null && mapp.size() > 0){
				String pcCompanyId = (String)mapp.get("pcCompanyId");
				map.put("pcCompanyId",pcCompanyId);
			}

			if((projectMap == null || projectMap.size() == 0 ) && (projectNameMap == null || projectNameMap.size() == 0 ) ){
				
				String id = UUID.randomUUID().toString();
				
				map.put("id",id);
				bankProjectService.insertSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "", "添加","添加了项目："+map.get("projectName"), 0,null,null,null);
	 			pagerFileService.LodingPagerFile(map.get("projectName"),"1",id,loginUserId,map.get("projectTypeValue")+"",map.get("departmentId")+"");
	 			
	 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
	 			responseDbCenter.setResModel(id);
				return responseDbCenter;
	 			
			}else if(projectMap != null && projectMap.size() != 0 ) {
				
				String id = projectMap.get(0).get("id") == null?"":(String)projectMap.get(0).get("id");
				
				map.put("id",id);
				bankProjectService.updateByIdSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "","添加", "添加了项目："+map.get("projectName"), 0,null,null,null);
	 			pagerFileService.updatePagerFile(id,loginUserId);
	 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
	 			responseDbCenter.setResModel(id);
				return responseDbCenter;
	 			
			}else {
				
				String id = projectNameMap.get(0).get("id") == null?"":(String)projectNameMap.get(0).get("id");
				
				map.put("id",id);
				bankProjectService.updateByIdSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "","添加", "添加了项目："+map.get("projectName"), 0,null,null,null);
	 			pagerFileService.updatePagerFile(id,loginUserId);
	 			
	 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
	 			responseDbCenter.setResModel(id);
				return responseDbCenter;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月13日 
	 * @功能描述:编辑项目
	 * @param req
	 * @param rsp
	 * @param bankProject  : 
	 * {
		"id": "1",
	    "projectCode": "222",
	    "projectName": "测试项目",
	    "projectTypeId":"fjaljfafakjfj",
	    "courseName"："cs",
	    "startDate": "2017-10-25",
	    "endDate": "2017-10-30",
	    "trainObject": "培训对象",
	    "trainPlace"："cs",
	    "userName": "客户名称",
	    "feel": "满意度",
	    "programId": "方案ID",
	    "projectManager": "项目经理",
	    "projecUser": "项目人员"
		}
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateBankProject",method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter updateProject(HttpServletRequest req, HttpServletResponse rsp,String bankProject) throws Exception{
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		if (StringUtil.isBlank(bankProject)) {
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {
			Map<String, Object> map  = JSON.parseObject(bankProject, new TypeReference<Map<String, Object>>() {
			});			
			
			String id = map.get("id") == null?"":(String)map.get("id");
			String projectName = map.get("projectName") == null?"":(String)map.get("projectName");
			Long createDate = map.get("createDate") == null?0:(Long)map.get("createDate");
			Long updateDate = map.get("updateDate") == null?0:(Long)map.get("updateDate");
			String departmentId = map.get("departmentId") == null?"":(String)map.get("departmentId");

			if(StringUtils.isBlank(departmentId)){
				Map<String, Object> mapp = userService.selectUserById(loginUserId);
				if(mapp.get("departmentId") != null){
					map.put("departmentId",(String) mapp.get("departmentId"));
				}
			}


			if(createDate != 0){
				Date date = DateUtils.getDateByLong(createDate);
				map.put("createDate", date);
			}
			if(updateDate != 0){
				Date date = DateUtils.getDateByLong(updateDate);
				map.put("updateDate", date);
			}


			Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);
			List<String>  departmentIdList=departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"),loginUserMap);

			List<Map<String,Object>> projectNameMap = bankProjectService.selectBankProjectByName(projectName,departmentIdList);

			if(projectNameMap != null && projectNameMap.size() > 0){

				String existsId = projectNameMap.get(0).get("id") == null?"":(String)projectNameMap.get(0).get("id");

				if(!existsId.equals(id)){
					return ResponseConstants.BANK_PROJECTNAME_EXISTS;
				}
			}
			
			
			
			map.put("updatedBy",loginUserId);
			bankProjectService.updateByIdSelective(map);		
 			userDynamicService.addUserDynamic(loginUserId, map.get("id").toString(), "","更新", "更新了项目："+map.get("projectName"), 0,null,null,null);
 			pagerFileService.updatePagerFileName(map.get("id")+"", loginUserId,map.get("projectName")+"");

 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
 			
 			return responseDbCenter;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

	}
	
	/**
	 * 
	 * @param req
	 * @param rsp
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteBankProjectByIds",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter deleteBankProjectByIds(HttpServletRequest req, HttpServletResponse rsp) throws Exception{
		
		String ids = req.getParameter("ids");

		if (StringUtils.isBlank(ids)) {

			return ResponseConstants.MISSING_PARAMTER;
		}

		List<String> idList = new ArrayList<>();

		String[] cids = ids.split(",");

		String idString = "";
		for (int i = 0; i < cids.length; i++) {

			idString += "'" + cids[i] + "',";
			idList.add(cids[i]);
		}

		if (StringUtils.isNotBlank(idString)) {
			idString = "id in (" + idString.substring(0, idString.lastIndexOf(",")) + ")";
		}
		
		try {
			
			bankProjectService.deleteBankProjectByIds(idString);

			bankPointService.deleteProjectPointByProjectIds(idList);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;

	}

	/**
	 *
	 * @param req
	 * @param rsp
	 * @param
	 * @return
	 * 根据项目id查询网点信息
	 */
	@ResponseBody
	@RequestMapping(value="/selectPointsByProjectId",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter selectPointsByProjectId(@RequestParam(value = "projectId",required = false) String projectId,
													HttpServletRequest req, HttpServletResponse rsp){

		if (StringUtils.isBlank(projectId)) {
			return ResponseConstants.MISSING_PARAMTER;
		}

		try {

			List<Map<String,Object>> maps = bankProjectService.selectPointsByProjectId(projectId);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(maps);
			return responseDbCenter;

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

	/**
	 *
	 * @param req
	 * @param rsp
	 * @param
	 * @return
	 * 根据项目ID查询没有关联的网点
	 */
	@ResponseBody
	@RequestMapping(value="/selectExcludePointsByProjectId",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter selectExcludePointsByProjectId(@RequestParam(value = "projectId",required = false) String projectId,
													@RequestParam(value = "content",required = false) String content,
													@RequestParam(value = "currentPage",required = false) String currentPage,
													@RequestParam(value = "pageSize",required = false) String pageSize,HttpServletRequest req, HttpServletResponse rsp){

		if (StringUtils.isBlank(projectId)) {
			return ResponseConstants.MISSING_PARAMTER;
		}
		String loginUserId = (String) req.getAttribute("loginUserId");

		if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			currentPage = "1";
			pageSize = "10";
		}

		int  pageSizeInt = Integer.parseInt(pageSize);

		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;

		try {

			Map<String,Object>  bankProject= bankProjectService.selectBankProjectById(projectId);
			List<String > departmentIdList=userDataPermissionService.getDataPermissionDepartmentIdList(null, loginUserId);
			List<Map<String,Object>> maps = bankProjectService.selectExcludePointsByProjectId((String)bankProject.get("companyId"),projectId,content,departmentIdList,pageSizeInt,currentPageInt);
			Integer num = bankProjectService.selectExcludePointsTotalByProjectId((String)bankProject.get("companyId"),projectId,content,departmentIdList);

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(maps);
			responseDbCenter.setTotalRows(num.toString());
			return responseDbCenter;

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

	/**
	 *
	 * @param req
	 * @param rsp
	 * @param
	 * @return
	 * 根据项目ID绑定网点信息
	 */
	@ResponseBody
	@RequestMapping(value="/saveProjectPoints",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter saveProjectPoints(@RequestParam(value = "projectId",required = false) String projectId,
											  @RequestParam(value = "pointIds",required = false) String pointIds,
											  @RequestParam(value = "pointNames",required = false) String pointNames,
											  HttpServletRequest req, HttpServletResponse rsp) throws Exception{

		String loginUserId = (String) req.getAttribute("loginUserId");

		if (StringUtils.isBlank(projectId) || StringUtils.isBlank(pointIds)) {
			return ResponseConstants.MISSING_PARAMTER;
		}

		String[] idss = pointIds.split(",");

		List<String> idList = new ArrayList<>();
		for(String id : idss){
			idList.add(id);
		}

		try {

			bankProjectService.saveProjectPoints(projectId,idList);
			userDynamicService.addUserDynamic(loginUserId, projectId, "","新增", "新增了项目网点："+pointNames, 0,null,null,null);

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			return responseDbCenter;

		} catch (DuplicateKeyException e) {
			throw new GlobalException(ResponseConstants.BANK_PROJECT_POINT_HAS_EXISTS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}

	/**
	 *
	 * @param req
	 * @param rsp
	 * @param
	 * @return
	 * 删除项目网点
	 */
	@ResponseBody
	@RequestMapping(value="/deleteProjectPointsByPPointIds",method =  {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter deleteProjectPointsByPointIds(@RequestParam(value = "ppointIds",required = false) String ppointIds,
														  @RequestParam(value = "projectId",required = false) String projectId,
														  @RequestParam(value = "pointNames",required = false) String pointNames,
														  HttpServletRequest req, HttpServletResponse rsp) throws  Exception{

		String loginUserId = (String) req.getAttribute("loginUserId");

		if (StringUtils.isBlank(ppointIds) || StringUtils.isBlank(projectId)) {
			return ResponseConstants.MISSING_PARAMTER;
		}

		String[] idss = ppointIds.split(",");

		List<String> idList = new ArrayList<>();
		for(String id : idss){
			idList.add(id);
		}

		try {

			userDynamicService.addUserDynamic(loginUserId, projectId, "","删除", "删除了项目网点："+pointNames, 0,null,null,null);

			bankPointService.deleteProjectPointByPointIds(idList);


			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			return responseDbCenter;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	@ApiOperation(value = "查询银行项目列表")
	@ResponseBody
	@RequestMapping(value = "selectBankProjectList",method = RequestMethod.POST)
	public  ResponseDbCenter selectBankProjectList(
			@ApiParam(value = "银行网点Id",required = true) @RequestParam(required = true)  String pointId,
			@ApiParam(value = "网点名称",required = false) @RequestParam(required = false)  String projectName,
			@ApiParam(value = "关联状态  已关联  ，已撤销   多个以逗号隔开",required = false) @RequestParam(required = false)  String revokeStatus,
			@ApiParam(value = "当前页",required = true) @RequestParam(required = true)  Integer currentPage,
			@ApiParam(value = "每页数量",required = true) @RequestParam(required = true)  Integer pageSize ){
 		try {
			List<String> revokeStatusList=new ArrayList<>();

			  if (!StringUtil.isBlank(revokeStatus)){
					String[] revokeArra=revokeStatus.split(",")	  ;
				  for (int i = 0; i <revokeArra.length ; i++) {
					  revokeStatusList.add(revokeArra[i]);
				  }
			  }
			   List<Map<String,Object> > list= bankProjectService.selectBankProjectList(pointId, projectName, revokeStatusList, currentPage, pageSize);
			   Integer count=  bankProjectService.selectBankProjectCount(pointId, projectName, revokeStatusList);
				ResponseDbCenter responseDbCenter=new ResponseDbCenter();
				responseDbCenter.setResModel(list);
			   responseDbCenter.setTotalRows(count+"");
			   return  responseDbCenter;

		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;

		}
	}
	@ApiOperation(value = "更新网点与项目的关联状态")
	@ResponseBody
	@RequestMapping(value = "updateDcBankProjectPointRevokeStatus",method = RequestMethod.POST)
	public  ResponseDbCenter updateDcBankProjectPointRevokeStatus(
			@ApiParam(value = "网点项目Id",required = true) @RequestParam(required = true)String id ,
			@ApiParam(value = "关联状态  已关联，已撤销",required = true) @RequestParam(required = true)String revokeStatus ){
		try {


			Map<String,Object>  bankProjectPointMap=bankProjectService.selectBankProjectPointById(id);
			Map<String ,Object> bankProjectMap=bankProjectService.selectBankProjectById((String)bankProjectPointMap.get("bankProjectId"));
			if ("已关联".equals(revokeStatus)){
				if (2==(Integer)bankProjectMap.get("status")){
					return  ResponseConstants.PROJECT_ALLREADY_DELETED;
				}
			}
			bankProjectService.updateDcBankProjectPointRevokeStatus(id,revokeStatus);
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
 			return  responseDbCenter;

		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;

		}
	}







	@ApiOperation(value = "查询未关联某一个项目的问卷列表")
	@ResponseBody
	@RequestMapping(value = "selectQuestionaire",method = RequestMethod.POST)
	public  ResponseDbCenter selectQuestionaire(
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true) String bankProjectId,
			@ApiParam(value = "标题",required = false) @RequestParam(required = false) String title,
			@ApiParam(value = "问卷类型: wcdt问卷类 zzt分值类 pxt排序题    comment测评类",required = false) @RequestParam(required = false) String ttype ,
			@ApiParam(value = "当前页",required = true) @RequestParam(required = true) Integer currentPage,
			@ApiParam(value = "每页数量" ,required = true) @RequestParam(required = true) Integer pageSize ){
		try {
			List<String>  ttypeList=new ArrayList<>();
			if (!StringUtil.isBlank(ttype)){
				String[] ttypes=ttype.split(",");
				for (int i = 0; i <ttypes.length ; i++) {
					ttypeList.add(ttypes[i]);
				}
			}

			List<Map<String,Object>>   list=bankProjectService.selectQuestionaire(bankProjectId,title,ttypeList,currentPage,pageSize);
			Integer count=bankProjectService.selectQuestionaireCount(bankProjectId,title,ttypeList);
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(list);
			responseDbCenter.setTotalRows(count+"");
			return  responseDbCenter;

		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;

		}
	}


	@ApiOperation(value = "查询已经关联某一个项目的问卷列表")
	@ResponseBody
	@RequestMapping(value = "selectQuestionaireByProjectId",method = RequestMethod.POST)
	public  ResponseDbCenter selectQuestionaireByProjectId(
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true) String bankProjectId,
			@ApiParam(value = "当前页" ,required = false) @RequestParam(required = false) Integer currentPage,
			@ApiParam(value = "每页多少数据" ,required = false) @RequestParam(required =false) Integer pageSize){
		try {
			List<Map<String,Object>>   list=bankProjectService.selectQuestionaireByProjectId(bankProjectId,currentPage,pageSize);
			int count=bankProjectService.selectQuestionaireCountByProjectId(bankProjectId);
 			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(list);
			responseDbCenter.setTotalRows(count+"");
 			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}




	@ApiOperation(value = "关联项目与问卷")
	@ResponseBody
	@RequestMapping(value = "insertBankProjectExerciseList",method = RequestMethod.POST)
	public  ResponseDbCenter insertBankProjectExerciseList(
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true) String bankProjectId ,
			@ApiParam(value = "问卷Id列表 多个以逗号分隔" ,required = true) @RequestParam(required = true) String excerciseIds ){
		try {
			if (!StringUtil.isBlank(excerciseIds)) {
				String[] excerciseIdList = excerciseIds.split(",");
				List<Map<String, Object>> mapList = new ArrayList<>();
				for (int i = 0; i < excerciseIdList.length; i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", UUID.randomUUID().toString());
					map.put("bankProjectId", bankProjectId);
					map.put("exerciseId", excerciseIdList[i]);
					mapList.add(map);
				}
				bankProjectService.insertBankProjectExerciseList(mapList);
			}

			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
 			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}



	@ApiOperation(value = "取消项目与问卷关联")
	@ResponseBody
	@RequestMapping(value = "deleteBankProjectExerciseList",method = RequestMethod.POST)
	public  ResponseDbCenter deleteBankProjectExerciseList(
			@ApiParam(value = "关联关系Id,多个值以逗号分隔 " ,required = true) @RequestParam(required = true) String ids ){
		try {
			if (!StringUtil.isBlank(ids)) {
				List<String> list=new ArrayList<>();
				String[] idArra=ids.split(",");
				for (int i = 0; i <idArra.length ; i++) {
					list.add(idArra[i]);
				}

				bankProjectService.deleteBankProjectExerciseList(list);
			}

			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}


	@ApiOperation(value = "查询网点下用户答卷列表")
	@ResponseBody
	@RequestMapping(value = "selectWjExamByBankPointId",method = RequestMethod.POST)
	public  ResponseDbCenter selectWjExamByBankPointId(
			@ApiParam(value = "网点Id" ,required = true) @RequestParam(required = true)String pointId,
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true)String bankProjectId,
			@ApiParam(value = "问卷名称" ,required = false) @RequestParam(required = false)String title,
			@ApiParam(value = "问卷类型 : wcdt问卷类 zzt分值类 pxt排序题    comment测评类 多个以逗号分隔" ,required = false) @RequestParam(required = false)String ttype,
			@ApiParam(value = "开始时间" ,required = false) @RequestParam(required = false)String startDate,
			@ApiParam(value = "结束时间" ,required = false) @RequestParam(required = false)String endDate,
			@ApiParam(value = "人员姓名" ,required = false) @RequestParam(required = false)String uname,
			@ApiParam(value = "当前页" ,required = true) @RequestParam(required = true)Integer currentPage,
			@ApiParam(value = "每页多少条" ,required = true) @RequestParam(required = true)Integer pageSize
			){
		try {


			List<String> ttypeList=new ArrayList<>();
			if (!StringUtil.isBlank(ttype)){
				String[] ttypes=ttype.split(",");
				for (int i = 0; i <ttypes.length ; i++) {
					ttypeList.add(ttypes[i]);
				}
			}

			if (StringUtil.isBlank(startDate)){
				startDate=null;
			}
			if (StringUtil.isBlank(endDate)){
				endDate=null;
			}

			List<Map<String,Object>> list=bankProjectService.selectWjExamByBankPointId(pointId,bankProjectId,title,ttypeList,startDate,endDate,uname,currentPage,pageSize);
			Integer count=bankProjectService.selectWjExamCountByBankPointId(pointId,bankProjectId,title,ttypeList,startDate,endDate,uname);
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(list);
			responseDbCenter.setTotalRows(count+"");
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}




	@ApiOperation(value = "插入问卷记录")
	@ResponseBody
	@RequestMapping(value = "insertProjectWjExamRecord",method = RequestMethod.POST)
	public  ResponseDbCenter insertProjectWjExamRecord(
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true)String bankProjectId,
			@ApiParam(value = "网点Id" ,required = true) @RequestParam(required = true)String pointId,
			@ApiParam(value = "人员姓名" ,required = true) @RequestParam(required = true)String uname,
			@ApiParam(value = "联系方式" ,required = true) @RequestParam(required = true)String mobile,
			@ApiParam(value = "调研岗位" ,required = true) @RequestParam(required = true)String station,
			@ApiParam(value = "问卷Id" ,required = true) @RequestParam(required = true)String exerciseId
			){
		try {
			bankUserService.insertBankUser("insert",uname,station,mobile,pointId,null,null,null,null,mobile,null,null,null,null);
			Map<String,Object> user=userService.selectUserByOnlyMobile(mobile);

           List<String> alreadyList=bankProjectService.selectBankProjectWjByUserIdProjectIdPointIdExerciseId(bankProjectId,exerciseId,pointId,(String)user.get("userId"));
			Map<String,String> map=new HashMap<>();
			if(alreadyList.size()>0){
				map.put("id",alreadyList.get(0));
			}else{

				Map<String,Object> record=new HashMap<>();

				String recordId=UUID.randomUUID().toString();
				record.put("id", recordId);
				record.put("bankProjectId", bankProjectId);
				record.put("exerciseId", exerciseId);
				record.put("pointId", pointId);
				record.put("userId",user.get("id"));
				record.put("createDate",new Date());


				bankProjectService.insertBankProjectWjExcerciseRecord(record);
				map.put("id",recordId);

			}


			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(map);
 			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

	@ApiOperation(value = "根据项目查询网点")
	@ResponseBody
	@RequestMapping(value = "selectBankPointByProjectId",method = RequestMethod.POST)
	public  ResponseDbCenter selectBankPointByProjectId(
			@ApiParam(value = "项目Id" ,required = true) @RequestParam(required = true)String bankProjectId
	){
		try {
			List<Map<String,Object>> list=bankProjectService.selectBankPointByProjectId(bankProjectId);



 			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(list);
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}




	@ApiOperation(value = "查询银行岗位")
	@ResponseBody
	@RequestMapping(value = "selectBankPosition",method = RequestMethod.POST)
	public  ResponseDbCenter selectBankPosition(
	){
		try {
			List<Map<String,Object>> list=bankProjectService.selectBankPosition();
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(list);
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

	@ApiOperation(value = "查询网点统计")
	@ResponseBody
	@RequestMapping(value = "selectBankPointStatistic",method = RequestMethod.POST)
	public  ResponseDbCenter selectBankPointStatistic(
			@ApiParam(value = "网点Id" ,required = true) @RequestParam(required = true)String pointId
	){
		try {
			Map<String,Object> map=bankProjectService.selectBankPointStatistic(pointId);
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(map);
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}


	@ApiOperation(value = "查询网点项目关系名称")
	@ResponseBody
	@RequestMapping(value = "selectBankProjectRelation",method = RequestMethod.POST)
	public  ResponseDbCenter selectBankProjectRelation(
			@ApiParam(value = "id" ,required = true) @RequestParam(required = true)String id
	){
		try {
			Map<String,Object> map=bankProjectService.selectBankProjectRelation(id);
			ResponseDbCenter responseDbCenter=new ResponseDbCenter();
			responseDbCenter.setResModel(map);
			return  responseDbCenter;
		}catch (Exception e){
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
	}

}
