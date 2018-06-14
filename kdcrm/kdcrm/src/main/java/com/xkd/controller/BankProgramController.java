package com.xkd.controller;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * 银行方案
 */
@Transactional
@Controller
@RequestMapping("/bankProgram")
public class BankProgramController extends BaseController {

	
	@Autowired
	private BankProgramService bankProgramService;
	@Autowired
	private UserDynamicService userDynamicService;
	@Autowired
	private PagerFileService pagerFileService;
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
	 * @功能描述:查询所有项目信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectBankProgramsByContent")
	public ResponseDbCenter selectBankProgramsByContent(HttpServletRequest req,HttpServletResponse rsp){
		
		String content = req.getParameter("content");
		String programCode = req.getParameter("programCode");
		String writeDate = req.getParameter("writeDate");
		String programTypeId = req.getParameter("programTypeId");
		String departmentId = req.getParameter("departmentId");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");

		if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			currentPage = "1";
			pageSize = "10";
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		List<Map<String,Object>> resultMap = null;
		int num = 0;
		
		try {


			String loginUserId = (String) req.getAttribute("loginUserId");

			String pcCompanyId = null;
			Map<String, Object> mapp = userService.selectUserById(loginUserId);
			if(mapp !=null && mapp.size() > 0){
				String roleId = (String)mapp.get("roleId");
				if(!"1".equals(roleId)){
					pcCompanyId = (String)mapp.get("pcCompanyId");
				}
			}

			List<String> list = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);

			Map<String,Object> paramMap = new HashMap<>();
			
			paramMap.put("content",content);
			paramMap.put("programCode",programCode);
			paramMap.put("writeDate",writeDate);
			paramMap.put("programTypeId",programTypeId);
			paramMap.put("currentPage",currentPageInt);
			paramMap.put("pageSize",pageSizeInt);
			paramMap.put("departmentIdList",list);
			paramMap.put("pcCompanyId",pcCompanyId);

		    resultMap = bankProgramService.selectBankProgramsByContent(paramMap);
		    num = bankProgramService.selectTotalBankProgramsByContent(paramMap);
		    
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(resultMap);
		responseDbCenter.setTotalRows(num+"");
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月13日 
	 * @功能描述:根据id查询银行方案
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectBankProgramById")
	public ResponseDbCenter selectBankProgramById(HttpServletRequest req,HttpServletResponse rsp){
		
		String id = req.getParameter("id");
		
		Map<String,Object> resultMap = null;
		
		if (StringUtils.isBlank(id)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {
			
		    resultMap = bankProgramService.selectBankProgramById(id);
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(resultMap);
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年10月16日 
	 * @功能描述:新增方案
	 * @param req
	 * @param rsp
	 * @param 
	   bankProgram : 
		 {
		    "programCode": "222",
		    "programName": "测试方案",
		    "programTypeId": "1440",
		    "writeDate"："2017-10-12",
		    "userName": "客户名称",
		    "managerName": "项目经理",
		    "programUser": "方案人员"
		 }
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addBankProgram", method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter addBankProgram(HttpServletRequest req, HttpServletResponse rsp,String bankProgram) throws Exception{
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		if (StringUtil.isBlank(bankProgram)) {
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {

			Map<String, Object> map  = JSON.parseObject(bankProgram, new TypeReference<Map<String, Object>>() {
			});

			String programName = map.get("programName") == null?"":(String)map.get("programName");
			String programIndustry = map.get("programIndustry") == null?"":(String)map.get("programIndustry");
			String departmentId = map.get("departmentId") == null?"":(String)map.get("departmentId");

			if(StringUtils.isBlank(departmentId)){
				Map<String, Object> mapp = userService.selectUserById(loginUserId);
				if(mapp.get("departmentId") != null){
					map.put("departmentId",(String) mapp.get("departmentId"));
				}
			}

			String num = bankProgramService.getCountBankProgram();
			//方案编号自动生成 FA-YH170001-20171103
			String programCode = bankProgramService.getProgramCode(programIndustry,(StringUtils.isBlank(num) || num == null)?0:Integer.parseInt(num));

			List<Map<String,Object>> programMap = bankProgramService.selectBankProgramByCodeAllStatus(programCode);
			List<Map<String,Object>> programNameMap = bankProgramService.selectBankProgramByNameAllStatus(programName);
			
			if(programMap != null && programMap.size() > 0){
				
				Integer status = programMap.get(0).get("status") == null?null:(Integer)programMap.get(0).get("status");
				if(status == null || status.intValue() == 0){
					
					return ResponseConstants.BANK_PROGRAMCODE_EXISTS;
				}
			}
			
			if(programNameMap != null && programNameMap.size() > 0){
				
				Integer status = programNameMap.get(0).get("status") == null?null:(Integer)programNameMap.get(0).get("status");
				if(status == null || status.intValue() == 0){
					
					return ResponseConstants.BANK_PROGRAMNAME_EXISTS;
				}
			}
			
			if(programMap.size() > 0 && programNameMap.size() > 0){
				
				String cid = programMap.get(0).get("id") == null?null:(String)programMap.get(0).get("id");
				String nid = programNameMap.get(0).get("id") == null?null:(String)programNameMap.get(0).get("id");
				if(!cid.equals(nid)){
					
					bankProgramService.deleteBankProgramRealByName(programName);
				}
			}

			//通过上面过滤，只有programMap>0 或者 programNameMap>0
			
			map.put("status","0");
			map.put("createdBy",loginUserId );
			map.put("createDate", new Date());
			map.put("updatedBy",loginUserId);
			map.put("updateDate", new Date());
			map.put("programCode",programCode);

			Map<String, Object> mapp = userService.selectUserById(loginUserId);
			if(mapp !=null && mapp.size() > 0){
				String pcCompanyId = (String)mapp.get("pcCompanyId");
				map.put("pcCompanyId",pcCompanyId);
			}
			
			if((programMap == null || programMap.size() == 0) && (programNameMap == null || programNameMap.size() == 0)){
				
				String id = UUID.randomUUID().toString();
				
				map.put("id",id);
				bankProgramService.insertSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "", "添加","添加了方案："+map.get("programName"), 0,null,null,null);
	 			pagerFileService.LodingPagerFile(map.get("programName"),"2",id,loginUserId,map.get("programTypeValue")+"",map.get("departmentId")+"");
				pagerFileService.editFolderDepartment(map.get("id")+"",map.get("departmentId")+"",loginUserId);

	 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
	 			responseDbCenter.setResModel(id);
				return responseDbCenter;
				
			}else if(programMap != null && programMap.size() != 0){
				
				String id = programMap.get(0).get("id") == null?"":(String)programMap.get(0).get("id");
				
				map.put("id",id);
				bankProgramService.updateByIdSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "","添加", "添加了方案："+map.get("programName"), 0,null,null,null);
	 			pagerFileService.updatePagerFile(id,loginUserId);
	 			
	 			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
	 			responseDbCenter.setResModel(id);
				return responseDbCenter;
	 			
			}else {
				
				String id = programNameMap.get(0).get("id") == null?"":(String)programNameMap.get(0).get("id");
				
				map.put("id",id);
				bankProgramService.updateByIdSelective(map);
	 			userDynamicService.addUserDynamic(loginUserId, id, "","添加" ,"添加了方案："+map.get("programName"), 0,null,null,null);
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
	 * @2017年10月16日 
	 * @功能描述:修改方案
	 * @param req
	 * @param rsp
	 * @return
	 * @param bankProgram  : 
	 * {
		"id": "1",
	    "programCode": "222",
	    "programName": "测试方案",
	    "programTypeId": "1440",
	    "writeDate"："2017-10-12",
	    "userName": "客户名称",
	    "managerName": "项目经理",
	    "programUser": "方案人员"
	   }
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateBankProgram",method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseDbCenter updateBankProgram(HttpServletRequest req, HttpServletResponse rsp,String bankProgram) throws Exception{
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		if (StringUtil.isBlank(bankProgram)) {
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		try {
			Map<String, Object> map  = JSON.parseObject(bankProgram, new TypeReference<Map<String, Object>>() {
			});			
			
			String programName = map.get("programName") == null?"":(String)map.get("programName");
			String id = map.get("id") == null?"":(String)map.get("id");
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
			
			
			List<Map<String,Object>> programNameMap = bankProgramService.selectBankProgramByNameAllStatus(programName);
			
			if(programNameMap != null && programNameMap.size() > 0){
				
				String existsId = programNameMap.get(0).get("id") == null?"":(String)programNameMap.get(0).get("id");
				
				if(!existsId.equals(id)){
					return ResponseConstants.BANK_PROGRAMNAME_EXISTS;
				}
			}
			
			map.put("updatedBy",loginUserId);
			bankProgramService.updateByIdSelective(map);		
 			userDynamicService.addUserDynamic(loginUserId, map.get("id").toString(), "","更新", "更新了方案："+map.get("programName"), 0,null,null,null);
			pagerFileService.updatePagerFileName(map.get("id")+"", loginUserId,map.get("programName")+"");
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
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteBankProgramByIds")
	public ResponseDbCenter deleteBankProgramByIds(HttpServletRequest req, HttpServletResponse rsp) throws Exception{
		
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
			
			bankProgramService.deleteBankProgramByIds(idString);
			bankProgramService.clearProjectProgramByIds(idList);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;

		
	}
	
	
}
