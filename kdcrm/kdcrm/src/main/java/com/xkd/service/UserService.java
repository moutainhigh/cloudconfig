package com.xkd.service;

import java.util.*;

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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.xkd.mapper.UserMapper;
import com.xkd.model.Operate;

@Service
public class UserService {


	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	SysUserOperateService sysUserOperateService;

	@Autowired
	UserDataPermissionService userDataPermissionService;

	@Autowired
	SysRoleOperateService sysRoleOperateService;

	public Map<String,Object> selectUserByMobile(String mobile, Integer platform){

		Map<String,Object> map = userMapper.selectUserByMobile(mobile,platform);

		return map;
	}





	/*
	 * platform：来源，0表示用户来自手机端、1表示来自pc端
	 */
	public Map<String, Object> selectUserByEmail(String email, Integer platform) {

		Map<String, Object> maps = userMapper.selectUserByEmail(email,platform);

		return maps;
	}


	public Map<String, Object> selectUserById(String id) {

		Map<String,Object> map = userMapper.selectUserById(id);

		return map;
	}


	public List<Map<String, Object>> selectUserByTeacherType(String teacherType,String loginUserId) {
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);
		List<Map<String, Object>> maps=null;
		if ("1".equals(loginUserMap.get("roleId"))){
			maps = userMapper.selectUserByTeacherType(teacherType,null);
		}else{
			maps = userMapper.selectUserByTeacherType(teacherType,(String)loginUserMap.get("pcCompanyId"));
		}

		return maps;
	}


	public List<Map<String, Object>> selectTeachers(int pageSizeInt, int currentPageInt,String loginUserId) {
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);
		List<Map<String, Object>> maps = null;

		if ("1".equals(loginUserMap.get("roleId"))){
			maps=userMapper.selectTeachers(pageSizeInt,currentPageInt,null);
		}else{
			maps=userMapper.selectTeachers(pageSizeInt,currentPageInt,(String)loginUserMap.get("pcCompanyId"));
		}
		return maps;
	}
	public Integer selectTeacherCount(String loginUserId) {
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);
		Integer num=0;
		if ("1".equals(loginUserMap.get("roleId"))){
			num= userMapper.selectTeacherCount(null);
		}else{
			num= userMapper.selectTeacherCount((String)loginUserMap.get("pcCompanyId"));
		}

		return num;
	}


	public List<Map<String, Object>> selectUsersByContent(String contentStr, int currentPageInt, int pageSizeInt) {

		List<Map<String, Object>> maps = userMapper.selectUsersByContent(contentStr, currentPageInt, pageSizeInt);

		return maps;
	}


	public Integer selectUsersByContentCount(String content) {

		Integer num = userMapper.selectUsersByContentCount(content);

		return num;
	}


	public Integer updateUserPasswordById(String id, String enPassWord) {

		Integer num = userMapper.updateUserPasswordById(id,enPassWord);

		return num;
	}


	public Integer saveUser(Map<String, Object> paramMap) {

		Integer num = userMapper.saveUser(paramMap);

		return num;
	}


	public Integer updateUserById(Map<String, Object> paramMap) {

		Integer num = userMapper.updateUserById(paramMap);

		return num;
	}


	public Integer deleteUserRolesByRoles(String roleIds) {

		Integer num = userMapper.deleteUserRolesByRoles(roleIds);

		return num;
	}


	public Integer updateDcUserDetail(Map<String,Object> user){
		return userMapper.updateDcUserDetail(user);
	}
	public Integer insertDcUserDetail(Map<String,Object> user){
		return userMapper.insertDcUserDetail(user);
	}
	public Integer updateDcUser(Map<String,Object> user){
		return userMapper.updateDcUser(user);
	}
	public Integer insertDcUser(Map<String,Object> user){
		return userMapper.insertDcUser(user);
	}


	public Integer repeatPcUserPasswordsByIds(String idsString, String encodeRepeatPassWord, String updateBy) {

		Integer num = userMapper.repeatPcUserPasswordsByIds(idsString,encodeRepeatPassWord,updateBy);

		return num;
	}


	public Integer updateTeacherInfoById(Map<String, Object> paramMap) {

		Integer num = userMapper.updateTeacherInfoById(paramMap);

		return num;
	}


	public Integer saveTeacherInfo(Map<String, Object> paramMap) {

		Integer num = userMapper.saveTeacherInfo(paramMap);

		return num;
	}


	public Integer deleteUserByIds(List<String> idList) {

		Integer num = userMapper.deleteUserByIds(idList);

		return num;
	}


	public Integer updateStationByCompanyIdUserId(String companyId, String userId, String station) {

		return userMapper.updateStationByCompanyIdUserId(companyId,userId,station);
	}

	public List<Map<String, Object>> selectTeacherByTeacherName(String teacherName,String pcCompanyId) {

		return userMapper.selectTeacherByTeacherName(teacherName,pcCompanyId);
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


	public List<Map<String,Object>> selectUsersUnderRole(String roleId,String userName,Integer pageNum, Integer pageSize ){

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





	public List<Map<String,Object>> searchPcUserbyName(  String userName,String pcCompanyId){
		return userMapper.searchPcUserbyName(userName,pcCompanyId);
	}


	public int changeDepartmentByUserIds( List<String> userIds,String departmentId,String pcCompanyId ){
		return userMapper.changeDepartmentByUserIds(userIds, departmentId,pcCompanyId);
	}


	public List<Operate> selectAllOperateByUSerId(String userId){
		Map<String,Object> loginUser=selectUserById(userId);

		//读取用户对应的权限信息
		List<Operate> userOperateList = sysUserOperateService.selectOperateByUserId(userId);

		if (loginUser!=null&&loginUser.get("roleId")!=null) {
			/**
			 * 查询出用户角色下的权限列表
			 */
			List<Operate> roleOperateList=sysRoleOperateService.selectOperateByRoleId(loginUser.get("roleId").toString());
			//合并角色对应的权限列表
			userOperateList.addAll(roleOperateList);
		}
		return userOperateList;
	}



	public boolean hasPrivatePermission(String token,String url){
		List<Operate> list= OperateCacheUtil.getUserOperates(token);
		if (list==null) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (url.equals(list.get(i).getUrl())) {
				return true;
			}
		}
		return false;

	}


	public List<Map<String,Object>>  selectUserDepartmentStatistic(List<String> departmentIds){
		if (departmentIds.size()>0) {
			return userMapper.selectUserDepartmentStatistic(departmentIds);
		}else {
			return new ArrayList<>(0);
		}
	}


	public Integer deleteUserAdviserByIds(String ids) {

		return userMapper.deleteUserAdviserByIds(ids);
	}


	public List<Map<String, Object>> searchUserNoTitleByName(String userName, int currentPageInt, int pageSizeInt,String loginUserId) {

		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);
		if ("1".equals(loginUserMap.get("roleId"))){
			return userMapper.searchUserNoTitleByName(userName,currentPageInt,pageSizeInt,null);
		}else{
			return userMapper.searchUserNoTitleByName(userName,currentPageInt,pageSizeInt,(String)loginUserMap.get("pcCompanyId"));

		}
	}


	public Integer searchUserNoTitleByNameTotal(String userName,String loginUserId) {
		Map<String,Object>  loginUserMap=userMapper.selectUserById(loginUserId);
		if ("1".equals(loginUserMap.get("roleId"))){
			return userMapper.searchUserNoTitleByNameTotal(userName,null);

		}else{
			return userMapper.searchUserNoTitleByNameTotal(userName, (String) loginUserMap.get("pcCompanyId"));


		}
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
		return  1;
	}

	public List<Map<String,Object>> selectUsers(String pcCompanyId,String content) {
		return userMapper.selectUsers(pcCompanyId,content);
	}

	public boolean getPrivatePermission(String loginUserId, String url) {
		return userMapper.getPrivatePermission(loginUserId,url) == 0 ?false:true;
	}
}
