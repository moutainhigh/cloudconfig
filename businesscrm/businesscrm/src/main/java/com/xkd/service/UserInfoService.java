package com.xkd.service;

import java.util.*;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.UserInfoMapper;
import com.xkd.model.UserInfo;

@Service
public class UserInfoService {


    Logger log= LoggerFactory.getLogger(UserInfoService.class);


    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private SolrService solrService;

    @Autowired
    private CompanyService companyServie;


    @Autowired
    private UserDynamicService userDynamicService;


    public Map<String,Object> selectUserInfoById(String id) {

        Map<String,Object> map = userInfoMapper.selectUserInfoById(id);

        return map;
    }

    public List<Map<String, Object>> selectUserInfoByCompanyId(String companyId) {

        List<Map<String, Object>> userInfos = userInfoMapper.selectUserInfoByCompanyId(companyId);

        return userInfos;
    }



    public int deleteUserInfo(String id) {
        return userInfoMapper.deleteUserInfo(id);
    }














    /**
     * @param uname          用户名
     * @param station        职位
     * @param phone          电话
     * @param companyId      公司Id
     * @param email          邮箱
     * @param uflag          是否是默认联系人   1  是  0 不是
     * @param sex            性别    男  女
     * @param udesc          描述
     * @param mobile         手机
     * @param userResource   用户资源
     * @param personAnalysis 人物分析
     * @param birth          出生日期
     * @param createdBy      创建人Id
     */
    public String addUserInfo(
            String uname,
            String station,
            String phone,
            String companyId,
            String email,
            String uflag,
            String sex,
            String udesc,
            String mobile,
            String userResource,
            String personAnalysis,
            String birth,
            String createdBy,
            Integer age,
            String qq,
            String weixin,
            String hometown
    ) {

        sex = "0".equals(sex) ? "男" : sex;
        sex = "1".equals(sex) ? "女" : sex;

        try {
            //查询该公司下面的客户联系人
           List<Map<String,Object>> alreadyContact = selectUserInfoByCompanyId(companyId);
            if (alreadyContact.isEmpty()) {
                uflag = "1";//如果当前是联系人是唯一 的一个联系人，则自动 将其设为默认联系人
            }

            String userCompanyId = UUID.randomUUID().toString();

            /**
             *
             * 插入公司联系人
             *
             */
            Map<String, Object> userCompany = new HashMap();
            userCompany.put("id", userCompanyId);
            userCompany.put("companyId", companyId);
            userCompany.put("uname", uname);
            userCompany.put("station", station);
            userCompany.put("mobile", mobile);
            userCompany.put("phone", phone);
            userCompany.put("email", email);
            userCompany.put("sex", sex);
            userCompany.put("age", age);
            userCompany.put("birth", birth);
            userCompany.put("udesc", udesc);
            userCompany.put("userResource", userResource);
            userCompany.put("personAnalysis", personAnalysis);
            userCompany.put("createdBy", createdBy);
            userCompany.put("createDate", new Date());
            userCompany.put("updatedBy", createdBy);
            userCompany.put("updateDate", new Date());
            userCompany.put("qq", qq);
            userCompany.put("weixin", weixin);
            userCompany.put("hometown", hometown);
            userInfoMapper.insertUserInfo(userCompany);
            if ("1".equals(uflag)) {// 如果是默认联系人
                /**
                 * 更新企业表中的联系人冗余字段
                 */
                Map<String, Object> company = new HashMap();
                company.put("id", companyId);
                company.put("contactUserId", userCompanyId);
                company.put("contactName", uname);
                company.put("contactPhone", StringUtils.isBlank(mobile) ? phone : mobile);
                company.put("updatedBy", createdBy);
                company.put("updateDate", new Date());
                companyServie.updateCompanyInfoById(company);
            }



            /**
             * 更新公司信息完整度
             */
            companyServie.updateInfoScore(companyId);

            //添加动态
            userDynamicService.addUserDynamic(createdBy, userCompanyId, null, "添加", "添加了联系人\"" + uname + "\"", 0, null, null, null);


            /**
             * 更新公司索引
             */
            solrService.updateCompanyIndex(companyId);

            return userCompanyId;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


    }


    /**
     * @param uname          用户名
     * @param station        职位
     * @param phone          电话
     * @param companyId      公司Id
     * @param email          邮箱
     * @param uflag          是否是默认联系人   1  是  0 不是
     * @param id             公司用户表的主键Id
     * @param sex            性别    男  女
     * @param udesc           描述
     * @param mobile         手机
     * @param userResource   用户资源
     * @param personAnalysis 人物分析
     * @param birth          出生日期
     * @param createdBy      创建人Id
     */
    public void updateUserInfo(
            String id,
            String uname,
            String station,
            String phone,
            String companyId,
            String email,
            String uflag,
            String sex,
            String udesc,
            String mobile,
            String userResource,
            String personAnalysis,
            String birth,
            String createdBy,
            Integer age,
            String qq,
            String weixin,
            String hometown
    ) {
        sex = "0".equals(sex) ? "男" : sex;
        sex = "1".equals(sex) ? "女" : sex;


        try {
            List<Map<String, Object>> alreadyContact = selectUserInfoByCompanyId(companyId);
            if (alreadyContact.size() == 1) {
                uflag = "1";//如果当前是联系人是唯一 的一个联系人，则自动 将其设为默认联系人
            }


            /**
             * 修改个人信息
             */
            Map<String, Object> userCompany = new HashMap();
            userCompany.put("id", id);
            userCompany.put("companyId", companyId);
            userCompany.put("uname", uname);
            userCompany.put("station", station);
            userCompany.put("mobile", mobile);
            userCompany.put("phone", phone);
            userCompany.put("email", email);
            userCompany.put("sex", sex);
            userCompany.put("age", age);
            userCompany.put("birth", birth);
            userCompany.put("udesc", udesc);
            userCompany.put("userResource", userResource);
            userCompany.put("personAnalysis", personAnalysis);
            userCompany.put("updatedBy", createdBy);
            userCompany.put("updateDate", new Date());
            userCompany.put("qq", qq);
            userCompany.put("weixin", weixin);
            userCompany.put("hometown", hometown);

            userInfoMapper.updateUserInfoById(userCompany);
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
                company.put("updatedBy", createdBy);
                company.put("updateDate", new Date());
                companyServie.updateCompanyInfoById(company);

            }



            //添加动态
            userDynamicService.addUserDynamic(createdBy, id, null, "添加", "修改了联系人\"" + uname + "\"", 0, null, null, null);


            /**
             * 更新公司信息完整度
             */
            companyServie.updateInfoScore(companyId);

            /**
             * 更新企业索引
             */

            solrService.updateCompanyIndex(companyId);


        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


    }










    public List<Map<String, Object>> searchUserInfo(String  companyId,String searchValue, List<String> departmentIdList,Integer publicFlag,String loginUserId, Integer currentPage, Integer pageSize) {

        int start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        start = (currentPage - 1) * pageSize;
        return userInfoMapper.searchUserInfo(companyId,searchValue, departmentIdList,publicFlag,loginUserId, start, pageSize);
    }


    public Integer searchUserInfoCount(String companyId,String searchValue, List<String> departmentIdList,Integer publicFlag,String loginUserId) {

        return userInfoMapper.searchUserInfoCount(companyId,searchValue, departmentIdList,publicFlag,loginUserId);
    }


    public Map selectUserInfoByMobileAndCompanyId(String companyId, String mobile) {
        return userInfoMapper.selectUserInfoByMobileAndCompanyId(companyId, mobile);
    }




}
