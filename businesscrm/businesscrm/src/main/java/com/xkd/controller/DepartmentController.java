package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.DepartmentTreeNode;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(description = "部门相关接口")
@Controller
@RequestMapping("/department")
@Transactional
public class DepartmentController extends BaseController {

	@Autowired
	DepartmentService departmentService;

	@Autowired
	SolrService solrService;

	@Autowired
	UserDataPermissionService userDataPermissionService;

	@Autowired
	UserService userService;

	@Autowired
	CompanyService companyService;


	static List<String> tableNames=new ArrayList<>();

	static {
		tableNames.add("dc_company");
	}

	@ApiOperation(value = "查询部门树，公开")
	@ResponseBody
	@RequestMapping(value = "/tree",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter selectAllDepartment(HttpServletRequest req, HttpServletResponse rsp) throws Exception{
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
			DepartmentTreeNode departmentTreeNode=null;
			if ("1".equals(loginUserMap.get("roleId"))) {
				departmentTreeNode = departmentService.selectSuperDepartment();
			}else {
				departmentTreeNode = departmentService.selectAllDepartment(loginUserMap);

			}

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(departmentTreeNode);
			return responseDbCenter;

		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}




	@ApiOperation(value = "查询部门树,有权限控制")
	@ResponseBody
	@RequestMapping(value = "control/tree",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter getAllDepartment(HttpServletRequest req, HttpServletResponse rsp) throws Exception{
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
			DepartmentTreeNode departmentTreeNode=null;
			if ("1".equals(loginUserMap.get("roleId"))) {
				  departmentTreeNode = departmentService.selectSuperDepartment();
			}else {
				  departmentTreeNode = departmentService.selectAllDepartment(loginUserMap);

			}

			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(departmentTreeNode);
			return responseDbCenter;

		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}







	@ApiOperation(value = "添加用户时功能处部门树")
	@ResponseBody
	@RequestMapping(value = "user/departmentTree",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter departmentTree(HttpServletRequest req, HttpServletResponse rsp,
										   @ApiParam(value = "部门Id",required = true)  @RequestParam(required = true) String departmentId
										   ) throws Exception{
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> companyMap=departmentService.getCompanyIdByDepartmentId(departmentId);

			DepartmentTreeNode departmentTreeNode=departmentService.selectAllDepartmentUnderCompany((String)companyMap.get("id"));
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(departmentTreeNode);
			return responseDbCenter;

		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
	}


	@ApiOperation(value = "更新部门")
	@ResponseBody
	@RequestMapping(value="/update",method={RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter update(HttpServletRequest req, HttpServletResponse rsp,@ApiParam(value = "部门 如  {" +
			"\"id\":\"1\"" +
			"\"departmentName\":\"开发部\"" +
			"\"remark\":\"这是一个神奇的部门\"" +
			"\"principalId\":\"12\"" +
			"\"parentId\":\"1\"" +
			"}",required = true)  @RequestParam(required = true) String department) throws Exception{
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

			Map<String, Object> map  = JSON.parseObject(department, new TypeReference<Map<String, Object>>() {
			});
			String departmentName=(String)map.get("departmentName");

			Map<String,Object> oldDepartment=departmentService.selectDepartmentById((String)map.get("id"));


//			/**
//			 * 判断企业名称是否已经存在于库中
//			 */
//			List<Map<String,Object>> list=departmentService.selectDepartmentByName(departmentName);
//			for (int i = 0; i <list.size(); i++) {
//				if (list.get(i).get("departmentName").equals(departmentName)) {
//					if (!list.get(i).get("id").equals(map.get("id"))) {
//						return ResponseConstants.DEPARTMENT_EXISTS;
//					}
//				}
//			}


			departmentService.update(map);



			/**
			 * 如果部门名称改了，则需要更新该部门下企业信息的Solr索引
			 */
			if (!departmentName.equals((String)oldDepartment.get("departmentName"))){
				//查询要删除的部门Id及其子公司Id列表
				List<String> childCompanyIds=departmentService.selectChildDepartmentIds((String)oldDepartment.get("id"),loginUserMap);

				//将要删除的部门对应的企业数据先临时存放到中间表中
				int total=	companyService.insertDcSolrCompany(childCompanyIds,1);

				/**
				 * 分批更新Solr索引
				 */
				int batchSize=500;
				List<String>  companyIds=companyService.selectSolrCompanyIdsByDepartmentIdsAndOperate(childCompanyIds, 1, 0, batchSize);
				while (companyIds.size()>0){
					solrService.updateIndex(companyIds);
					int deletedCount=companyService.deleteSolrCompanyIds(companyIds,1);
					companyIds=companyService.selectSolrCompanyIdsByDepartmentIdsAndOperate(childCompanyIds, 1, 0, batchSize);
				}


			}

		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}


	@ApiOperation(value = "添加部门")
	@ResponseBody
	@RequestMapping(value="/save" ,method={RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter save(HttpServletRequest req, HttpServletResponse rsp,@ApiParam(value = "部门 如  {" +
 			"\"departmentName\":\"开发部\"" +
			"\"remark\":\"这是一个神奇的部门\"" +
			"\"principalId\":\"12\"" +
			"\"parentId\":\"1\"" +
			"}",required = true)  @RequestParam(required = true) String department) throws Exception{
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");

			Map<String, Object> map  = JSON.parseObject(department, new TypeReference<Map<String, Object>>() {
			});

			if ("1".equals(map.get("parentId"))){
				return ResponseConstants.CANNOT_ADD_CHILD;
			}

			map.put("id", UUID.randomUUID().toString());
			departmentService.insert(map);


			//清除所有用户缓存的部门权限信息
			userDataPermissionService.clearCacheData();



		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}

	/**
	 * 删除部门
	 * @param req
	 * @param rsp
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "删除部门")
	@ResponseBody
	@RequestMapping(value="/delete" ,method={RequestMethod.GET})
	public ResponseDbCenter delete(HttpServletRequest req, HttpServletResponse rsp,
								   @ApiParam(value = "部门Id",required = true)  @RequestParam(required = true) String id) throws Exception{
		try {

			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

			//查询要删除的部门Id及其子公司Id列表
			List<String> childCompanyIds=departmentService.selectChildDepartmentIds(id,loginUserMap);


			//查询部门下的员工,如果有员工就不能删除
			List<Map<String,Object>> userList=userService.selecUserByDepartmentId(id, null,200000,1,loginUserId);

			if (userList.size()>0){
				return ResponseConstants.DEPARTMENT_HAS_USER;
			}


			//如果有部门下有数据就不能删除
			for (int i = 0; i <tableNames.size() ; i++) {
				int count=departmentService.selectDataCount(tableNames.get(i),childCompanyIds);
				if (count>0){
					return  ResponseConstants.DATA_DEPARTMENT_HAS_DATA;
				}
			}


			List<String> departmentIdList=departmentService.selectChildDepartmentIds(id,loginUserMap);
			for (int i = 0; i < departmentIdList.size(); i++) {
				departmentService.delete( departmentIdList.get(i));
			}

			//清除权限缓存
			userDataPermissionService.clearCacheData();

		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}




	/**
	 * 删除部门
	 * @param req
	 * @param rsp
	 * @param oldDepartmentId
	 * @param newDepartmentId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "迁移部门数据")
	@ResponseBody
	@RequestMapping(value="/migrateData" ,method={RequestMethod.POST})
	public ResponseDbCenter migrateData(HttpServletRequest req, HttpServletResponse rsp,
								   @ApiParam(value = "旧部门Id",required = true)  @RequestParam(required = true) String oldDepartmentId,
								   @ApiParam(value = "新部门Id",required = true)  @RequestParam(required = true) String newDepartmentId) throws Exception{
		try {

			/**
			 * 数据迁移
			 */
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

			Map<String,Object> newDepartment=departmentService.selectDepartmentById(newDepartmentId);
 			if (newDepartment==null){
				return ResponseConstants.FUNC_SERVERERROR;
			}


			//查询要删除的部门Id及其子公司Id列表
			List<String> childCompanyIds=departmentService.selectChildDepartmentIds(oldDepartmentId,loginUserMap);


			//将要删除的部门对应的企业数据先临时存放到中间表中
			companyService.insertDcSolrCompany(childCompanyIds,0);


			/**
			 * 将要删除的部门及其子部门下的数据迁移到要删除的部门的父级部门下去
			 */
			for (int i = 0; i < tableNames.size(); i++) {
				departmentService.updateDataDepartmentId(tableNames.get(i), newDepartmentId, childCompanyIds);
			}



			/**
			 * 分批更新Solr索引
			 */
			int batchSize=500;
			List<String>  companyIds=companyService.selectSolrCompanyIdsByDepartmentIdsAndOperate(childCompanyIds, 0, 0, batchSize);
			while (companyIds.size()>0){
				solrService.updateIndex(companyIds);
				int deletedCount=companyService.deleteSolrCompanyIds(companyIds,0);
				companyIds=companyService.selectSolrCompanyIdsByDepartmentIdsAndOperate(childCompanyIds, 0, 0, batchSize);
			}









		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		return responseDbCenter;
	}







	/**
	 * 根据部门id查询部门跟用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "根据部门id查询部门跟用户")
	@ResponseBody
	@RequestMapping(value="/getTreeByPid" ,method={RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter getTreeByPid(HttpServletRequest req,
								   @ApiParam(value = "部门Id",required = false)  @RequestParam(required = false) String id) throws Exception{
		Map<String,List> map = new HashMap<>();
		try {
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

			List<Map<String,Object>> userList=null;
			List<Map<String,Object>> treeList=null;
            if ("1".equals(loginUserMap.get("roleId"))){
				if (StringUtils.isBlank(id)){
					treeList= departmentService.getTreeByPid("1");
					userList=userService.selectDirectUserByDepartmentId("1");
				}else{
					treeList= departmentService.getTreeByPid(id);
					userList=userService.selectDirectUserByDepartmentId(id);
				}
			}else{
				if (StringUtils.isBlank(id)||(StringUtils.isNotBlank(id)&&"1".equals(id))){
					treeList= departmentService.getTreeByPid((String)loginUserMap.get("pcCompanyId"));
					userList=userService.selectDirectUserByDepartmentId((String) loginUserMap.get("pcCompanyId"));
				}else{
					treeList= departmentService.getTreeByPid(id);
					userList=userService.selectDirectUserByDepartmentId(id);
				}
			}
			for (int i = 0; i < treeList.size(); i++) {
				List<String> childUserId = departmentService.selectChildDepartmentIds(treeList.get(i).get("id").toString(),loginUserMap);

				if(null == childUserId){
					childUserId = new ArrayList<>();
					childUserId.add(treeList.get(i).get("id").toString());
				}
				treeList.get(i).put("sumUser",departmentService.getChildTreeUserSum(childUserId));
			}
			map.put("userList",userList);
			map.put("treeList", treeList);
		} catch (Exception e) {
			log.error("异常栈:", e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(map);
		return responseDbCenter;
	}






}
