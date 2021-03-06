package com.xkd.service;

import com.xkd.exception.GlobalException;
import com.xkd.mapper.UserInfoMapper;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private SolrService solrService;

	@Autowired
	private CompanyService companyServie;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDynamicService userDynamicService;

	
	public UserInfo selectUserInfoById(String id){
		
		UserInfo userInfo = userInfoMapper.selectUserInfoById(id);
		
		return userInfo;
	}
	
	public List<Map<String,Object>> selectUserInfoByCompanyId(String companyId){
		
		List<Map<String,Object>> userInfos = userInfoMapper.selectUserInfoByCompanyId(companyId);
		
		return userInfos;
	}



	public Integer insertUserInfo(Map<String,Object> userInfo) {
		
		Integer num =  userInfoMapper.insertUserInfo(userInfo);
		
		return num;
	}

	public Integer updateUserInfoById(UserInfo userInfo) {
		
		Integer num =  userInfoMapper.updateUserInfoById(userInfo);
		
		return num;
		
	}

 

	public Integer deleteUserInfo(String userId) {
		
		Integer num =  userInfoMapper.deleteUserInfo(userId);
		
		return num;
	}



 

 

	public Integer deleteUserInfoByCompanyId(String companyId) {
		
		Integer num =  userInfoMapper.deleteUserInfoByCompanyId(companyId);
		
		return num;
		
	}

 

	public Integer updateUserInfoByIdByInvitation(UserInfo userInfo) {
		
		Integer num =  userInfoMapper.updateUserInfoByIdByInvitation(userInfo);
		
		return num;
		
	}
	
	
	public List<Map<String,Object>> selectUserCompanyByMobileAndCompanyId(String mobile,String companyId){
		return userInfoMapper.selectUserCompanyByMobileAndCompanyId(mobile,companyId);
	}
	
	
	public Map<String,Object> selectUserCompanyById(String id){
		return userInfoMapper.selectUserCompanyById(id);
	}
	
	public int deleteUserCompany(String id){
		return userInfoMapper.deleteUserCompany(id);
	}
	
	public int updateUserStationById( String id,String station){
		return	userInfoMapper.updateUserStationById(id,station);
	}
	
	public int deleteByUserIdAndCompanyId(  String userId,  String companyId){
		return userInfoMapper.deleteByUserIdAndCompanyId(userId, companyId);
	}

	public int deleteUserCompanyByUserIds(List<String> userIdList) {
		
		return userInfoMapper.deleteUserCompanyByUserIds(userIdList);
	}
	
	public 	List<Map<String,Object>> selectUserCompanyByCompanyIds( String companyIds){
		return  userInfoMapper.selectUserCompanyByCompanyIds(companyIds);
	}


	public List<String>  selectCompanyIdsByContactUserId(String userId){
		return userInfoMapper.selectCompanyIdsByContactUserId(userId);
	}

  public	List<String> selectCompanyIdsByUseInfoUserId(String userId){
		return userInfoMapper.selectCompanyIdsByUseInfoUserId(userId);
	}


	/**
	 *
	 * @param flag    "insert"表示插入 "update" 表示更新
	 * @param uname  用户名
	 * @param station  职位
	 * @param phone 电话
	 * @param companyId  公司Id
 	 * @param email 邮箱
	 * @param uflag 是否是默认联系人   1  是  0 不是
	 * @param id  公司用户表的主键Id
	 * @param sex 性别    男  女
	 * @param desc  描述
	 * @param mobile 手机
	 * @param userResource  用户资源
	 * @param personAnalysis 人物分析
	 * @param birth 出生日期
	 * @param loginUserId  登录人员Id
	 */
	public ResponseDbCenter changeUserInfo(
			String flag,
			 String uname,
			   String station,
			  String phone,
			  String companyId,
			  String email,
			   String uflag,
			   String id,
			  String sex,
			 String desc,
			 String mobile,
		     String userResource,
			 String personAnalysis,
			 String birth,
			String loginUserId,
			Integer age
	){



		/**
		 * 获取请求参数
		 */


			if ("0".equals(sex)) {

			sex = "男";

		} else if ("1".equals(sex)) {

			sex = "女";
		}

		if (StringUtils.isBlank(flag) || StringUtils.isBlank(uname)
				|| (StringUtils.isBlank(mobile) && StringUtils.isBlank(phone))) {

			throw new GlobalException(ResponseConstants.MISSING_PARAMTER);
		}

		if (StringUtils.isBlank(id) && "update".equals(uflag)) {

			throw new GlobalException(ResponseConstants.FUNC_UPDATENOID);
		}

		try {
			List<String> ucList=new ArrayList<>();
			List<String> ucInfoList=new ArrayList<>();

			if ("update".equals(flag) && StringUtils.isNotBlank(id)) {

				List<Map<String, Object>> alreadyContact = selectUserInfoByCompanyId(companyId);
				if (alreadyContact.size() == 1) {
					uflag = "1";//如果当前是联系人是唯一 的一个联系人，则自动 将其设为默认联系人
				}

				// 通过Id获取员工公司映射关系
				Map<String, Object> userCompany = selectUserCompanyById(id);
				// 通过手机号码查询库中已有用户
				Map<String, Object> existsInDb = userService.selectUserByOnlyMobile(mobile);

				if (existsInDb != null) { //如果库中已经存在手机号码对应的用户

					if (userCompany.get("userId").equals(existsInDb.get("id"))) { // 如果新改的手机号码和修改之前的用户数据是同一条

						/**
						 * 更新旧的用户信息
						 */
						existsInDb.put("uname", uname);
						existsInDb.put("email", email);
						existsInDb.put("phone", phone);
						existsInDb.put("mobile", mobile);

						existsInDb.put("sex", sex);
						existsInDb.put("udesc", desc);
						existsInDb.put("userResource", userResource);
						existsInDb.put("personAnalysis", personAnalysis);
						existsInDb.put("birth", birth);
						existsInDb.put("age", age);
						existsInDb.put("updatedBy", loginUserId);
						existsInDb.put("updateDate", new Date());
						userService.updateDcUser(existsInDb);
						userService.updateDcUserDetail(existsInDb);


						if ("1".equals(uflag)) {// 如果是默认联系人
							/**
							 * 更新公司表中冗余的联系人信息
							 */
							Map<String, Object> company = new HashMap();
							company.put("id", companyId);
							company.put("contactUserId", existsInDb.get("id"));
							company.put("contactName", uname);
							company.put("contactPhone", StringUtils.isBlank(mobile) ? phone
									: mobile);
							company.put("updatedBy", loginUserId);
							company.put("updateDate", new Date());
							companyServie.updateCompanyInfoById(company);
						}

						// 更新员工在某一个公司下的岗位
						updateUserStationById((String) userCompany.get("id"), station);


					} else {// 如果新手机号码对应的用户是另一个未被其关联的用户

						/**
						 * 更新新手机号码对应的用户信息
						 */

						existsInDb.put("uname", uname);
						existsInDb.put("email", email);
						existsInDb.put("phone", phone);
						existsInDb.put("mobile", mobile);
						existsInDb.put("sex", sex);
						existsInDb.put("udesc", desc);
						existsInDb.put("userResource", userResource);
						existsInDb.put("personAnalysis", personAnalysis);
						existsInDb.put("birth", birth);
						existsInDb.put("age", age);
						existsInDb.put("updatedBy", loginUserId);
						existsInDb.put("updateDate", new Date());
						userService.updateDcUser(existsInDb);
						userService.updateDcUserDetail(existsInDb);


						if ("1".equals(uflag)) {// 如果是默认联系人
							/**
							 * 更新公司表中冗余的联系人信息
							 */
							Map<String, Object> company = new HashMap();
							company.put("id", companyId);

							company.put("contactUserId", existsInDb.get("id"));
							company.put("contactName", uname);
							company.put("contactPhone", StringUtils.isBlank(mobile) ? phone
									: mobile);
							company.put("updatedBy", loginUserId);
							company.put("updateDate", new Date());
							companyServie.updateCompanyInfoById(company);
						}

						// 删除旧的绑定关系
						deleteByUserIdAndCompanyId((String) userCompany.get("userId"), companyId);

						//查询新的手机号码是否已经与该企业绑定过
						List<Map<String, Object>> userCompanyList = selectUserCompanyByMobileAndCompanyId(mobile, companyId);

						if (userCompanyList == null || userCompanyList.size() == 0) {//如果未绑定则绑定
							/**
							 * 插入员工公司关联关系
							 */
							String userCompanyId = UUID.randomUUID().toString();
							Map<String, Object> userCompany2 = new HashMap();
							userCompany2.put("id", userCompanyId);
							userCompany2.put("userId", existsInDb.get("id"));
							userCompany2.put("companyId", companyId);
							userCompany2.put("station", station);
							userCompany2.put("createdBy", loginUserId);
							userCompany2.put("createDate", new Date());
							insertUserInfo(userCompany2);
						}


					}

					ucList=selectCompanyIdsByContactUserId((String) existsInDb.get("id"));
					ucInfoList=selectCompanyIdsByUseInfoUserId((String) existsInDb.get("id"));

				} else {// 如果新改的手机号码在库中还没有，则直接更新

					/**
					 * 修改个人信息
					 */
					Map<String, Object> user = new HashMap();
					user.put("id", userCompany.get("userId"));
					user.put("uname", uname);
					user.put("email", email);
					user.put("phone", phone);
					user.put("mobile", mobile);
					user.put("sex", sex);
					user.put("udesc", desc);
					user.put("userResource", userResource);
					user.put("personAnalysis", personAnalysis);
					user.put("birth", birth);
					user.put("age", age);
					user.put("updatedBy", loginUserId);
					user.put("updateDate", new Date());
					userService.updateDcUser(user);
					userService.updateDcUserDetail(user);


					if ("1".equals(uflag)) {// 如果是默认联系人
						/**
						 * 更新公司表中冗余的联系人信息
						 */
						Map<String, Object> company = new HashMap();
						company.put("id", companyId);
						company.put("contactUserId", userCompany.get("userId"));
						company.put("contactName", uname);
						company.put("contactPhone",
								StringUtils.isBlank(mobile) ? phone : mobile);
						company.put("updatedBy", loginUserId);
						company.put("updateDate", new Date());
						companyServie.updateCompanyInfoById(company);

					}


					// 更新员工在某一个公司下的岗位
					updateUserStationById((String) userCompany.get("id"), station);

					ucList=selectCompanyIdsByContactUserId((String) userCompany.get("userId"));
					ucInfoList=selectCompanyIdsByUseInfoUserId((String) userCompany.get("userId"));

				}
				userDynamicService.addUserDynamic(loginUserId, companyId, "", "更新", "修改了员工" + uname, 0,null,null,null);


			} else {

				List<Map<String, Object>> alreadyContact = selectUserInfoByCompanyId(companyId);
				if (alreadyContact.isEmpty()) {
					uflag = "1";//如果当前是联系人是唯一 的一个联系人，则自动 将其设为默认联系人
				}

				// 查询改后的手机是否在库中已经注册
				Map<String, Object> existsInDb = userService.selectUserByOnlyMobile(mobile);

				if (existsInDb != null && !existsInDb.isEmpty()) {// 如果该手机已经注册
					List<Map<String, Object>> userCompanyList = selectUserCompanyByMobileAndCompanyId(mobile, companyId);

					/**
					 * 更新用户信息
					 */
					existsInDb.put("uname", uname);
					existsInDb.put("email", email);
					existsInDb.put("phone", phone);
					existsInDb.put("mobile", mobile);
					existsInDb.put("sex", sex);
					existsInDb.put("udesc", desc);
					existsInDb.put("userResource", userResource);
					existsInDb.put("personAnalysis", personAnalysis);
					existsInDb.put("birth", birth);
					existsInDb.put("age", age);
					existsInDb.put("updatedBy", loginUserId);
					existsInDb.put("updateDate", new Date());
					userService.updateDcUser(existsInDb);
					userService.updateDcUserDetail(existsInDb);


					if ("1".equals(uflag)) {// 如果是默认联系人
						/**
						 * 更新企业表中的联系人冗余字段
						 */
						Map<String, Object> company = new HashMap();
						company.put("id", companyId);
						company.put("contactUserId", existsInDb.get("id"));
						company.put("contactName", uname);
						company.put("contactPhone",
								StringUtils.isBlank(mobile) ? phone : mobile);
						company.put("updatedBy", loginUserId);
						company.put("updateDate", new Date());
						companyServie.updateCompanyInfoById(company);

					}

					if (userCompanyList != null && userCompanyList.size() > 0) { // 如果该注册用户已经与公司绑定
						return ResponseConstants.YICUNZAI
								;
						// 更新员工在某一个公司下的岗位
						//updateUserStationById((String) userCompanyList.get(0).get("id"), station);

					} else {// 如果该注册用户没有与公司绑定

						/**
						 * 插入员工公司关联关系
						 */
						String userId = (String) existsInDb.get("id");
						String userCompanyId = UUID.randomUUID().toString();
						Map<String, Object> userCompany = new HashMap();
						userCompany.put("id", userCompanyId);
						userCompany.put("userId", userId);
						userCompany.put("companyId", companyId);
						userCompany.put("station", station);
						userCompany.put("createdBy", loginUserId);
						userCompany.put("createDate", new Date());
						insertUserInfo(userCompany);

					}
					ucList=selectCompanyIdsByContactUserId((String) existsInDb.get("id"));
					ucInfoList=selectCompanyIdsByUseInfoUserId((String) existsInDb.get("id"));

				} else {
					/**
					 * 如果该手机还没有注册 ，则注册
					 */
					String userId = UUID.randomUUID().toString();


					/**
					 * 插入用户信息
					 */
					Map<String, Object> user = new HashMap();
					user.put("id", userId);
					user.put("uname", uname);
					user.put("email", email);
					user.put("phone", phone);
					user.put("mobile", mobile);
					user.put("sex", sex);
					user.put("udesc", desc);
					user.put("userResource", userResource);
					user.put("personAnalysis", personAnalysis);
					user.put("birth", birth);
					user.put("age", age);
					user.put("createdBy", loginUserId);
					user.put("createDate", new Date());
					user.put("updatedBy", loginUserId);
					user.put("updateDate", new Date());
					userService.insertDcUser(user);
					userService.insertDcUserDetail(user);


					if ("1".equals(uflag)) {// 如果是默认联系人
						/**
						 * 更新企业表中的联系人冗余字段
						 */
						Map<String, Object> company = new HashMap();
						company.put("id", companyId);
						company.put("contactUserId", userId);
						company.put("contactName", uname);
						company.put("contactPhone",
								StringUtils.isBlank(mobile) ? phone : mobile);
						company.put("updatedBy", loginUserId);
						company.put("updateDate", new Date());
						companyServie.updateCompanyInfoById(company);

					}

					/**
					 * 插入员工公司关联关系
					 */
					String userCompanyId = UUID.randomUUID().toString();
					Map<String, Object> userCompany = new HashMap();
					userCompany.put("id", userCompanyId);
					userCompany.put("userId", userId);
					userCompany.put("companyId", companyId);
					userCompany.put("station", station);
					userCompany.put("createdBy", loginUserId);
					userCompany.put("createDate", new Date());
					insertUserInfo(userCompany);


					ucList=selectCompanyIdsByContactUserId(userId);
					ucInfoList=selectCompanyIdsByUseInfoUserId(userId);

				}
				userDynamicService.addUserDynamic(loginUserId, companyId, "", "添加", "添加了员工" + uname, 0,null,null,null);


			}


			/**
			 * 更新公司信息完整度
			 */
			companyServie.updateInfoScore(companyId);
			/**
			 * 重新更新该用户所关联的企业的索引 ，该用户可能关联多家企业
			 */


			//再查询添加的用户
			Map<String, Object> uuu = userService.selectUserByOnlyMobile(mobile);


			//如果联系人基本信息变了，所有该联系人关系的企业冗余的该联系人的信息都要更新
			if (ucList.size()>0){
				for (int i = 0; i <ucList.size() ; i++) {
					Map<String,Object> companyMap=new HashMap<>();
					companyMap.put("id", ucList.get(i));
					companyMap.put("contactUserId", uuu.get("id"));
					companyMap.put("contactName", uuu.get("uname"));
					companyMap.put("contactPhone", StringUtils.isBlank((String) uuu.get("mobile")) ? (String) uuu.get("phone") : (String) uuu.get("mobile"));
					companyServie.updateCompanyInfoById(companyMap);
					solrService.updateCompanyIndex(ucList.get(i));
				}
			}



			//如果员工基本信息变了，所有该联员工关系的企业冗余的该员工的信息都要更新
			if (ucInfoList.size()>0){
				for (int i = 0; i <ucInfoList.size() ; i++) {
					solrService.updateCompanyIndex(ucInfoList.get(i));
				}
			}


		} catch (Exception e) {

			e.printStackTrace();
			//throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		ResponseDbCenter res = new ResponseDbCenter();
		return res;
	}


	public Map<String,Object> selectUserInfoByUserIdAndCompanyId(String userId, String companyId) {
		return  userInfoMapper.selectUserInfoByUserIdAndCompanyId(userId,companyId);
	}

	public Integer updateUserInfoByMap(Map<String, Object> existMap) {
		return  userInfoMapper.updateUserInfoByMap(existMap);
	}

    public void replaceUserInfo(Map<String, Object> userInfoMap) {
		userInfoMapper.replaceUserInfo(userInfoMap);
    }
}
