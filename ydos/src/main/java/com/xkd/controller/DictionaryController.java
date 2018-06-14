package com.xkd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.xkd.model.Dictionary;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.DictionaryService;

@RequestMapping("/dictionary")
@Controller
@Transactional
public class DictionaryController  extends BaseController{

	@Autowired
	private DictionaryService dictionaryService;


	@Autowired
	UserService userService;
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月3日 
	 * @功能描述:获得数据字典行业信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDictionaryByTtype")
	public ResponseDbCenter selectDictionaryByTtype(HttpServletRequest req,HttpServletResponse rsp){
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

		String ttype = req.getParameter("ttype");
		//flag=parentIndustry,sonIndustry
		List<Dictionary> dictionaryList = null;
		List<Dictionary> parentIndustryList = null;
		List<Dictionary> industryList = null;
		List<Dictionary> projectLabelList = null;
		Map<String,Object> map = new HashMap<>();
		
		try {
			
			if(StringUtils.isNotBlank(ttype)){
				
				if("industry".equals(ttype)){
					
					parentIndustryList = dictionaryService.selectDictionaryByTtype(ttype,(String)loginUserMap.get("pcCompanyId"));
					
					map.put("parentIndustryList", parentIndustryList);
					
				}else if("projectLabel".equals(ttype)){
					
					projectLabelList = dictionaryService.selectDictionaryByTtype("projectLabel",(String)loginUserMap.get("pcCompanyId"));
					
					map.put("dictionaryList", projectLabelList);
				}
				
			}else{
				
//				industryList = dictionaryService.selectDictionaryByTtype("industry");
				parentIndustryList = dictionaryService.selectParentDictionaryByTtype("industry",(String)loginUserMap.get("pcCompanyId"));
				projectLabelList = dictionaryService.selectDictionaryByTtype("projectLabel",(String)loginUserMap.get("pcCompanyId"));
				
				map.put("parentIndustryList", parentIndustryList);
//				map.put("industryList", industryList);
				map.put("projectLabelList", projectLabelList);
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		responseDbCenter.setResModel(map);
		
		return responseDbCenter;
	}
	
	
	/**
	 * 根据类型查询数据
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "查询数据字典类型")
	@ResponseBody
	@RequestMapping("/selectDictionaryTtypes")
	public ResponseDbCenter selectDictionaryTtypes(HttpServletRequest req,HttpServletResponse rsp
 												   ){
	
		List<Map<String,Object>>  maps = null;

		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

//		List<Map<String,Object>>  returnMaps = new ArrayList<>();
 		
		try {
			if ("1".equals(loginUserMap.get("roleId"))){ //如果是超级管理员，则返回公共数据字典
				maps = dictionaryService.selectDictionaryTtypes(null);
			}else {  //否则返回该登录用户所在公司的数据字典
				maps = dictionaryService.selectDictionaryTtypes((String)loginUserMap.get("pcCompanyId"));
			}

		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		responseDbCenter.setResModel(maps);
		
		return responseDbCenter;
	}
	
	/**
	 * 查询数据字典根据ttypes
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDictionarysByTtypes")
	public ResponseDbCenter selectDictionarysByTtypes(HttpServletRequest req,HttpServletResponse rsp){
	
		String ttypes = req.getParameter("ttypes");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);


		if(StringUtils.isBlank(ttypes)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] types = ttypes.split(",");
		
		String ttypesStr = "" ;
		
		for(int i = 0;i<types.length;i++){
			
			ttypesStr += "'"+types[i]+"', ";
		}
		
		if(StringUtils.isNotBlank(ttypesStr)){
			
			ttypesStr = "ttype in("+ttypesStr.substring(0, ttypesStr.lastIndexOf(","))+")";
		}
		
		int  pageSizeInt = 0;
		int  currentPageInt = 0;
		
		if(StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(currentPage)){
		
			pageSizeInt = Integer.parseInt(pageSize);
			
			currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		}
		
		
		List<Map<String,Object>>  maps = null;
		Map<String,Object> returnMap = new HashMap<>();
		
		try {
			
			maps = dictionaryService.selectDictionarysByTtypes(ttypesStr,pageSizeInt,currentPageInt,(String)loginUserMap.get("pcCompanyId"));
			
			
			for(int i = 0;i<types.length;i++){
				
				List<Map<String,Object>> typesList = new ArrayList<>();
				
				for(Map<String,Object> map : maps){
					
					if(types[i].equals(map.get("ttype")==null?null:(String)map.get("ttype"))){
						
						typesList.add(map);
					}
					
				}
				
				returnMap.put(types[i], typesList);
				
			}
			
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		responseDbCenter.setResModel(returnMap);
		
		return responseDbCenter;
	}

	/**
	 * 查询数据字典根据ttypes
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectParentAndSonByTtype")
	public ResponseDbCenter selectParentAndSonByTtype(HttpServletRequest req,HttpServletResponse rsp){
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

		String ttype = req.getParameter("ttype");

		if(StringUtils.isBlank(ttype)){
			return ResponseConstants.MISSING_PARAMTER;
		}

		List<Dictionary>  returnDictionarys = new ArrayList<>();
		try {

			List<Dictionary> dictionaries = dictionaryService.selectDictionaryByTtype(ttype,(String)loginUserMap.get("pcCompanyId"));
			List<Dictionary> dictionaryParents = new ArrayList<>();

			for(Dictionary dictionary : dictionaries){
				if("0".equals(dictionary.getParentId())){
					dictionaryParents.add(dictionary);
				}
			}

			returnDictionarys = dictionaryService.getParentAndSonRlation(dictionaryParents,dictionaries);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}

		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(returnDictionarys);
		return responseDbCenter;
	}
	
	
		/**
		 * 保存数据字典信息
		 * @param req
		 * @param rsp
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/saveDictionarys")
		public ResponseDbCenter saveDictionarys(HttpServletRequest req,HttpServletResponse rsp ){
			// 当前登录用户的Id
			String loginUserId = (String) req.getAttribute("loginUserId");
			Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);


			String ttype = req.getParameter("ttype");
			String ttypeName = req.getParameter("ttypeName");
			String values = req.getParameter("values");
			
			if(StringUtils.isBlank(ttype) || StringUtils.isBlank(values) || StringUtils.isBlank(ttypeName)){
				
				return ResponseConstants.MISSING_PARAMTER;
			}
			
			String[] vs = values.split(",");
			
			
			
			try {
				
				for(int i=0;i<vs.length;i++){
					
					List<Dictionary> checkDictionarys = dictionaryService.selectDictionaryByTtypeValue(ttype,vs[i],(String)loginUserMap.get("pcCompanyId"));
					
					if(checkDictionarys != null && checkDictionarys.size() > 0){
						
						return  ResponseConstants.DICTIONARY_HAS_EXISTS;
					}
					
					String id = UUID.randomUUID().toString();
					
					Integer maxLevel = dictionaryService.selectMaxLevelByTtype(ttype,(String)loginUserMap.get("pcCompanyId"));
					
					if(maxLevel == null){
						if ("1".equals(loginUserMap.get("roleId"))) { //如果是超级管理员，则添加的一定是公共数据字典
							dictionaryService.saveDictionarys(id, ttype, ttypeName, vs[i], 1,null);
						}else {
							dictionaryService.saveDictionarys(id, ttype, ttypeName, vs[i], 1,(String)loginUserMap.get("pcCompanyId"));
						}
					}else{
						if ("1".equals(loginUserMap.get("roleId"))) {//如果是超级管理员，则添加的一定是公共数据字典

							dictionaryService.saveDictionarys(id, ttype, ttypeName, vs[i], maxLevel.intValue() + 1,null);
						}else {
							dictionaryService.saveDictionarys(id, ttype, ttypeName, vs[i], maxLevel.intValue() + 1,(String)loginUserMap.get("pcCompanyId"));

						}
					}
					
					
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return ResponseConstants.FUNC_SERVERERROR;
			}
			
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			
			return responseDbCenter;
		}
		
		/**
		 * 编辑数据字典顺序
		 * @param req
		 * @param rsp
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/updateDictionaryLevels")
		public ResponseDbCenter updateDictionaryLevels(HttpServletRequest req,HttpServletResponse rsp){
		
			
			String dictionarys = req.getParameter("dictionarys");
			
			if(StringUtils.isBlank(dictionarys)){
				
				return ResponseConstants.MISSING_PARAMTER;
			}
			
			try {
				
				List<Map<String, Object>> results = (List<Map<String, Object>>)JSON.parseObject(dictionarys,Object.class);
				
				for(Map<String, Object> map : results){
					
					if((map.get("id") == null) || (map.get("level") == null)){
						
						continue;
					}
					
					String id = (String) map.get("id");
					Integer level = (Integer) map.get("level");
					
					dictionaryService.updateDictionaryLevel(id,level);
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return ResponseConstants.FUNC_SERVERERROR;
			}
			
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			
			return responseDbCenter;
		}
		
		
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月5日 
	 * @功能描述:根据父ID查询子信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDictionaryByParentId")
	public ResponseDbCenter selectDictionaryByParentId(HttpServletRequest req,HttpServletResponse rsp){
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

		String parentId = req.getParameter("parentId");
		String ttype = req.getParameter("ttype");
		
		if(StringUtils.isBlank(parentId) || StringUtils.isBlank(ttype)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		List<Dictionary> dictionaries = null;
		
		try {
			
			dictionaries = dictionaryService.selectDictionaryByParentId(parentId,ttype,(String)loginUserMap.get("pcCompanyId"));
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(dictionaries);
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月9日 
	 * @功能描述:通过Id查询数据字典
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectDictionaryById")
	public ResponseDbCenter selectDictionaryById(HttpServletRequest req,HttpServletResponse rsp){
	
		String id = req.getParameter("id");
		
		if(StringUtils.isBlank(id)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		Dictionary dictionarie = null;
		
		try {
			
			dictionarie = dictionaryService.selectDictionaryById(id);
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(dictionarie);
		
		return responseDbCenter;
	}
	
	

	
	/**
	 * 删除数据字典同时清除数据字典关联的其他表的数据
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteDictionarysByIds")
	public ResponseDbCenter deleteDictionarysByIds(HttpServletRequest req,HttpServletResponse rsp){
	
		String ids = req.getParameter("ids");
		
		if(StringUtils.isBlank(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] iids = ids.split(",");
		
		try {
			
			for(int i = 0;i<iids.length;i++){
				
				Dictionary dictionary = dictionaryService.selectDictionaryById(iids[i]);
				
				if(dictionary.getTtype() != null &&StringUtils.isNotBlank(dictionary.getValue())){

						String deleteSql = "delete from dc_dictionary where id = '"+iids[i]+"'";
						
						dictionaryService.clearColumnData(deleteSql);

				}
				
				dictionaryService.deleteDictionaryById(iids[i]);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
}
