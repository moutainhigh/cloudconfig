package com.xkd.service;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.*;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkd.mapper.UserMapper;
import com.xkd.model.Operate;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {


	static String keyPrefix="userId_";
	@Autowired
	RedisCacheUtil redisCacheUtil;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DepartmentService departmentService;


	@Autowired
	SysUserOperateService sysUserOperateService;




	@Autowired
	SysRoleOperateService sysRoleOperateService;


	@Autowired
	CompanyService companyService;

	public Map<String,Object> selectUserByMobile(String mobile, Integer platform){

		Map<String,Object> map = userMapper.selectUserByMobile(mobile,platform);

		return map;
	}


    public  void saveLastLoginIp(String ip,String loginUserId){

		Map<String,Object> userMap=new HashMap<>();
		userMap.put("id",loginUserId);
		userMap.put("lastLoginIp",ip);
		userMap.put("lastLoginTime",new Date());
		redisCacheUtil.deleteCacheByKey(keyPrefix+loginUserId);
		userMapper.updateDcUser(userMap);
	}


	/*
	 * platform：来源，0表示用户来自手机端、1表示来自pc端
	 */
	public Map<String, Object> selectUserByEmail(String email, Integer platform) {

		Map<String, Object> maps = userMapper.selectUserByEmail(email,platform);

		return maps;
	}


	public Map<String, Object> selectUserById(String id) {
		String key=keyPrefix+id;
		String userJson= (String) redisCacheUtil.getCacheObject(key);
		Map<String,Object> map=null;
		if (StringUtils.isBlank(userJson)){
			 map= userMapper.selectUserById(id);
			if (map!=null) {
				redisCacheUtil.setCacheObject(key, JSON.toJSONString(map));
			}
		}else {
			  map = JSON.parseObject(userJson, new TypeReference<Map<String, Object>>() {
			});
		}
		return map;
	}













	public Integer updateUserPasswordById(String id, String enPassWord) {
		redisCacheUtil.deleteCacheByKey(keyPrefix+id);
		Integer num = userMapper.updateUserPasswordById(id,enPassWord);

		return num;
	}








	public Integer deleteUserRolesByRoles(String roleIds) {

		Integer num = userMapper.deleteUserRolesByRoles(roleIds);

		return num;
	}


	public Integer updateDcUserDetail(Map<String,Object> user){
		//清除用户信息缓存
		redisCacheUtil.deleteCacheByKey(keyPrefix+user.get("id"));
		return userMapper.updateDcUserDetail(user);
	}
	public Integer insertDcUserDetail(Map<String,Object> user){
		return userMapper.insertDcUserDetail(user);
	}
	public Integer updateDcUser(Map<String,Object> user){
		//清除用户缓存
		redisCacheUtil.deleteCacheByKey(keyPrefix+user.get("id"));
		return userMapper.updateDcUser(user);
	}
	public Integer insertDcUser(Map<String,Object> user){
		return userMapper.insertDcUser(user);
	}


	public Integer repeatPcUserPasswordsByIds(String idsString, String encodeRepeatPassWord, String updateBy) {

		Integer num = userMapper.repeatPcUserPasswordsByIds(idsString,encodeRepeatPassWord,updateBy);

		return num;
	}





	public Integer deleteUserByIds(List<String> idList) {
		/**
		 * 清除redis中的用户缓存信息
		 */
		for (int i = 0; i <idList.size() ; i++) {
			redisCacheUtil.deleteCacheByKey(keyPrefix+idList.get(i));
		}
		Integer num = userMapper.deleteUserByIds(idList);

		return num;
	}




	public List<Map<String,Object>>  selectDirectUserByDepartmentId(String departmentId){
		List<String> list=new ArrayList<>();
		list.add(departmentId);
		return userMapper.selecUserByDepartmentIds( list, null,0,100000);
	}


	public Map<String,Object> selectUserByOnlyMobile(  String mobile){
		return userMapper.selectUserByOnlyMobile(mobile);
	}


	public List<Map<String,Object>> selecUserByDepartmentId(String departmentId,String userName,@RequestParam Integer pageSize,@RequestParam  Integer currentPage,String loginUserId){

		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);


		if (currentPage<1) {
			currentPage=1;
		}
		Integer start=(currentPage-1)*pageSize;


		if ("1".equals(loginUserMap.get("roleId"))){//如果是超级管理员，则可以看到所有用户
			if (StringUtil.isBlank(departmentId)){  //如果departmentId为空，则默认为根结点
				return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds("1",loginUserMap), userName,start,pageSize);
			}else{
				return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId,loginUserMap), userName,start,pageSize);
			}
		}else{ //非超级管理员
			if (StringUtil.isBlank(departmentId)){//非超级管理员则按其所属公司显示
				return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"),loginUserMap), userName,start,pageSize);
			}else{
				return userMapper.selecUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId,loginUserMap), userName,start,pageSize);
			}
		}
	}
	public Integer selecTotalUserByDepartmentId (String departmentId,String userName,String loginUserId){
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);


		if ("1".equals(loginUserMap.get("roleId"))){//如果是超级管理员，则可以看到所有用户
			if (StringUtil.isBlank(departmentId)){  //如果departmentId为空，则默认为根结点
				return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds("1", loginUserMap), userName);
			}else{
				return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName);
			}
		}else{ //非超级管理员
			if (StringUtil.isBlank(departmentId)){//非超级管理员则按其所属公司显示
				return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap), userName);
			}else{
				return userMapper.selecTotalUserByDepartmentIds(departmentService.selectChildDepartmentIds(departmentId, loginUserMap), userName);
			}
		}


	}


	public List<Map<String,Object>> selectUsersUnderRole(String roleId ,String userName,Integer pageNum, Integer pageSize ){

		if (pageNum<1) {
			pageNum=1;
		}
		return userMapper.selectUsersUnderRole(roleId,userName, (pageNum-1)*pageSize, pageSize);

	}

	public Integer selectTotalUsersCountUnderRole(String roleId,String userName ){


		return userMapper.selectTotalUsersCountUnderRole(roleId, userName);
	}



	public List<Map<String,Object>> selectUsersNotUnderRole(String roleId ,String userName,Integer pageNum, Integer pageSize,String departmentId,String loginUserId){
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);





		if (pageNum<1) {
			pageNum=1;
		}
		Integer  excludeSuperRoleUser=0;  //是否排除超级管理员用户 1 是

		List<String> departmentIds=null;
		if ("1".equals(loginUserMap.get("roleId"))){
			//在此处，超级管理员只能修改本公司的角色成员
				departmentIds = departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"), loginUserMap);
			excludeSuperRoleUser=0;
		}else{
			if (StringUtil.isBlank(departmentId)){
				departmentIds= departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"), loginUserMap);
			}else{
				departmentIds=departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
			}
			excludeSuperRoleUser=1;
		}
 		return userMapper.selectUsersNotUnderRole(roleId, userName, (pageNum-1)*pageSize, pageSize, departmentIds,excludeSuperRoleUser);
	}

	public int selectTotalUsersCountNotUnderRole(String roleId,String userName,String departmentId,String loginUserId){
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);

		List<String> departmentIds=null;
		Integer  excludeSuperRoleUser=0; //是否排除超级管理员用户   1  是

		if ("1".equals(loginUserMap.get("roleId"))){
			if (StringUtil.isBlank(departmentId)) {
				departmentIds = departmentService.selectChildDepartmentIds("1", loginUserMap);
			}else{
				departmentIds=departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
			}
			   excludeSuperRoleUser=0;

		}else{
			if (StringUtil.isBlank(departmentId)){
				departmentIds= departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"), loginUserMap);
			}else{
				departmentIds=departmentService.selectChildDepartmentIds(departmentId, loginUserMap);
			}
			   excludeSuperRoleUser=1;

		}
		return userMapper.selectTotalUsersCountNotUnderRole(roleId, userName, departmentIds,excludeSuperRoleUser);
	}























	/**
	 *
	 * @param map
	 * @return 0更新失败，1更新成功，2手机号已经存在，3邮箱已经存在
	 */
	public Integer updatePcUserInfoById(Map<String, Object> map) {

		if(map == null || map.size() == 0 || map.get("id") == null){
			return 0;
		}

		String mobile = map.get("mobile") == null?null:(String) map.get("mobile");
		String email = map.get("email") == null?null:(String) map.get("email");
		String id = (String) map.get("id");

		if(mobile !=null){
			Map<String, Object>  userMobileMap =  selectUserByMobile(mobile,null);
			if(userMobileMap != null && userMobileMap.size() > 0){
				String existsUserId = (String)userMobileMap.get("id");
				if(!id.equals(existsUserId)){
					return 2;
				}
			}
		}

		if(email !=null){
			Map<String, Object>  userEmailMap =  selectUserByEmail(email,null);
			if(userEmailMap != null && userEmailMap.size() > 0){
				String existsUserId = (String)userEmailMap.get("id");
				if(!id.equals(existsUserId)){
					return 3;
				}
			}
		}
		updateDcUser(map);
		updateDcUserDetail(map);
		return  1;
	}

	public List<Map<String,Object>> selectUsers(String pcCompanyId,String content) {
		return userMapper.selectUsers(pcCompanyId,content);
	}



	public List<Map<String,Object>> searchUserByMobile(String mobile){
		return  userMapper.searchUserByMobile(mobile);
	}








	public List<Map<String,Object>> searchAllUserByMobileAndRole( String roleId, String mobile,Integer excludeExists,String pcCompanyId,Integer currentPage,Integer pageSize){
		Integer start=0;
		if (currentPage!=null){
			currentPage=1;
		}
		if (pageSize!=null){
			pageSize=10;
		}
		start=(currentPage-1)*pageSize;
		return userMapper.searchAllUserByMobileAndRole(roleId,mobile,excludeExists,pcCompanyId,start,pageSize);
	}

	public  	Integer searchAllUserCountByMobileAndRole( String roleId, String mobile,Integer excludeExists,String pcCompanyId){
		return userMapper.searchAllUserCountByMobileAndRole(roleId,mobile,  excludeExists,  pcCompanyId);
	}

	public List<Map<String,Object>> selectStaff(  String pcCompanyId,  String uname, Integer level,String departmentId,Integer currentPage,Integer pageSize){
		Integer start=0;
		   if (currentPage==null){
			   currentPage=1;
		   }
		if (pageSize==null){
			pageSize=10;
		}
		    start=(currentPage-1)*pageSize;
		    List<String> departIdList=new ArrayList<>();
			if (StringUtils.isNotBlank(departmentId)){
				departIdList.add(departmentId);
			}
			List<Map<String,Object>>  staffList=userMapper.selectStaff(pcCompanyId,uname,level,departIdList,3,start,pageSize);
			List<String> userIdList=new ArrayList<>();
			List<String> departmentIdList=new ArrayList<>();
		for (int i = 0; i <staffList.size() ; i++) {
			userIdList.add((String) staffList.get(i).get("id"));
			if (staffList.get(i).get("departmentId")!=null) {
				departmentIdList.add((String) staffList.get(i).get("departmentId"));
			}
		}


		  if (userIdList.size()>0){
				List<Map<String,Object>> companyList=companyService.selectCompanyIdByTechinicanId(userIdList,pcCompanyId);



			  for (int i = 0; i <staffList.size() ; i++) {
				  Map<String,Object> st=staffList.get(i);
				  for (int j = 0; j < companyList.size() ; j++) {
					  Map<String,Object> mm=companyList.get(j);
					  if (st.get("id").equals(mm.get("responsibleUserId"))){
						  if (st.get("companyName")==null){
							  st.put("companyName",mm.get("companyName"));
						  }else {
							  st.put("companyName",st.get("companyName")+" "+mm.get("companyName"));
						  }
					  }
				  }
			  }

		  }

		//该人负责的巡检的设备组对应的客户
//		   if (departmentIdList.size()>0){
//				List<Map<String,Object>>  departmentMapList=  companyService.selectInspectionCompanyIdByDepartmentId(departmentIdList,pcCompanyId);
//			    if (departmentMapList.size()>0){
//					for (int i = 0; i <staffList.size() ; i++) {
//						Map<String,Object> st=staffList.get(i);
//
//						for (int j = 0; j <departmentMapList.size() ; j++) {
//							Map<String,Object> mm=departmentMapList.get(j);
//							if (StringUtils.isNotBlank((String) st.get("departmentId"))){
//								if (st.get("departmentId").equals(mm.get("departmentId"))){
//									if (st.get("companyName")==null){
//										st.put("companyName",mm.get("companyName")+"（巡检）");
//									}else {
//										st.put("companyName",st.get("companyName")+" "+mm.get("companyName")+"（巡检）");
//									}
//								}
//							}
//						}
//
//					}
//				}
//
//		   }

			return staffList;
	}




	public int selectStaffCount(  String pcCompanyId,String uname,Integer level,String departmentId){
		List<String> departIdList=new ArrayList<>();
		if (StringUtils.isNotBlank(departmentId)){
			departIdList.add(departmentId);
		}
		return userMapper.selectStaffCount(pcCompanyId, uname, level,departIdList,3);
	}


	/**
	 * 查询服务商帐号
	 * @return
	 */
	public List<Map<String,Object>> selectServerAccount(String pcCompanyId){
		return  userMapper.selectStaff(pcCompanyId,null,null,null,2,0,100000);
	}

	/**
	 * 查询技师端通讯录
 	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String,Object>> searchTechinicanContactor(String departmentId,Integer currentPage,Integer pageSize){
		Integer start=0;
		if (currentPage==null){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		start=(currentPage-1)*pageSize;
		if (!StringUtil.isBlank(departmentId)){
			List<String> departmentIdList=new ArrayList<>();
			departmentIdList.add(departmentId);
			return userMapper.selectStaff(null, null, null, departmentIdList,3, start, pageSize);
		}
		return new ArrayList<>();
	}



	/**
	 * 查询技师端所在班组人员

	 * @return
	 */
	public Integer  searchTechinicanContactorCount(String departmentId){

		if (!StringUtil.isBlank(departmentId)){
			List<String> departmentIdList=new ArrayList<>();
			departmentIdList.add(departmentId);
			return userMapper.selectStaffCount(null,null,null,departmentIdList,3);
		}
		return 0;
	}



	public Integer deleteStaff(@Param("id")String id){
		return  userMapper.deleteStaff(id);
	}


    public Map<String,Object> selectUserByMobileAndappFlag(String tel, int platform, String appFlag) {
		return userMapper.selectUserByMobileAndappFlag(tel,platform,appFlag);
    }


	/**
	 * 查询客户对应的联系人
	 * @param companyIdList 客户公司ID列表
	 * @param pcCompanyId 某一个服务商Id
 	 * @param pageSize
	 * @return
	 */
	public List<Map<String,Object>> selectCustomerContactor( List<String> companyIdList, String pcCompanyId, Integer currentPage, Integer pageSize){
		Integer start=0;
		if (currentPage==null){
			currentPage=1;
		}
		if (pageSize==null){
			pageSize=10;
		}
		start=(currentPage-1)*pageSize;
		if (companyIdList.size()==0||StringUtil.isBlank(pcCompanyId)){
			return new ArrayList<>();
		}
		return userMapper.selectCustomerContactor(companyIdList,pcCompanyId,start,pageSize);
	}
	public int selectCustomerContactorCount( List<String> companyIdList,String pcCompanyId){
		if (companyIdList.size()==0||StringUtil.isBlank(pcCompanyId)){
			return 0;
		}
		return userMapper.selectCustomerContactorCount(companyIdList,pcCompanyId);
	}


	public List<Map<String,Object>> selectAllUserIdByPcCompanyId(@Param("pcCompanyId")String pcCompanyId){
		return userMapper.selectAllUserIdByPcCompanyId(pcCompanyId);
	}

	public List<Map<String, String>> selectAllRoleId3(){
		return userMapper.selectAllRoleId3();
	}

	public List<String> selectUserByPcCompanyIdAndRoleId(String pcCompanyId, String roleId){
		return userMapper.selectUserByPcCompanyIdAndRoleId(pcCompanyId, roleId);
	}
	public  List<Map<String,Object>> selectAllUsers(){
		return userMapper.selectAllUsers();
	}


}
