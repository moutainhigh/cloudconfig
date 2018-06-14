package com.xkd.service;

import com.xkd.exception.GlobalException;
import com.xkd.mapper.BankUserMapper;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 巫建辉 on 2017/12/11.
 */
@Service
public class BankUserService {


    @Autowired
    UserService userService;
    @Autowired
    BankUserMapper bankUserMapper;

    /**
     * 导入银行人员，这里是以电话号码作为唯一标识来进行判断的
     * @param flag
     * @param uname
     * @param station
     * @param phone
     * @param pointId
     * @param email
     * @param id
     * @param sex
     * @param desc
     * @param mobile
     * @param age
     * @param workAge
     * @param degree
     * @param loginUserId
     */
    public void insertBankUser(
            String flag,
            String uname,
            String station,
            String phone,
            String pointId,
            String email,
            String id,
            String sex,
            String desc,
            String mobile,
            String age,
            String workAge,
            String degree,
            String loginUserId) {


        if(StringUtils.isNotBlank(mobile)){






            if ("update".equals(flag)) {

                List<Map<String, Object>> alreadyContact = bankUserMapper.selectUserInfoByPointId(pointId);


                // 通过Id获取员工公司映射关系
                Map<String, Object> userPoint = bankUserMapper.selectUserPointById(id);
                // 通过手机号码查询库中已有用户
                Map<String, Object> existsInDb = userService.selectUserByOnlyMobile(mobile);

                if (existsInDb != null) { //如果库中已经存在手机号码对应的用户

                    if (userPoint.get("userId").equals(existsInDb.get("id"))) { // 如果新改的手机号码和修改之前的用户数据是同一条

                        /**
                         * 更新旧的用户信息
                         */
                        existsInDb.put("uname", uname);
                        existsInDb.put("email", email);
                        existsInDb.put("phone", phone);
                        existsInDb.put("mobile", mobile);

                        existsInDb.put("sex", sex);
                        existsInDb.put("udesc", desc);
                        existsInDb.put("workAge", workAge);
                        existsInDb.put("degree", degree);
                         existsInDb.put("age",age);
                        existsInDb.put("updatedBy", loginUserId);
                        existsInDb.put("updateDate", new Date());
                        userService.updateDcUser(existsInDb);
                        userService.updateDcUserDetail(existsInDb);

                        // 更新员工在某一个公司下的岗位
                        bankUserMapper.updateUserStationById((String) userPoint.get("id"), station);


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
                        existsInDb.put("workAge", workAge);
                        existsInDb.put("degree", degree);
                        existsInDb.put("age",age);
                        existsInDb.put("updatedBy", loginUserId);
                        existsInDb.put("updateDate", new Date());
                        userService.updateDcUser(existsInDb);
                        userService.updateDcUserDetail(existsInDb);


                        // 删除旧的绑定关系
                        bankUserMapper.deleteByUserIdAndPointId((String) userPoint.get("userId"), pointId);

                        //查询新的手机号码是否已经与该企业绑定过
                        List<Map<String, Object>> userPointList = bankUserMapper.selectUserPointByMobileAndPointId(mobile, pointId);

                        if (userPointList == null || userPointList.size() == 0) {//如果未绑定则绑定
                            /**
                             * 插入员工公司关联关系
                             */
                            String userPointId = UUID.randomUUID().toString();
                            Map<String, Object> userPoint2 = new HashMap();
                            userPoint2.put("id", userPointId);
                            userPoint2.put("userId", existsInDb.get("id"));
                            userPoint2.put("pointId", pointId);
                            userPoint2.put("station", station);
                            userPoint2.put("createdBy", loginUserId);
                            userPoint2.put("createDate", new Date());
                            bankUserMapper.insertBankUserInfo(userPoint2);
                        }


                    }


                } else {// 如果新改的手机号码在库中还没有，则直接更新

                    /**
                     * 修改个人信息
                     */
                    Map<String, Object> user = new HashMap();
                    user.put("id", userPoint.get("userId"));
                    user.put("uname", uname);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("mobile", mobile);
                    user.put("sex", sex);
                    user.put("udesc", desc);
                    user.put("workAge", workAge);
                    user.put("degree", degree);
                     user.put("age",age);
                    user.put("updatedBy", loginUserId);
                    user.put("updateDate", new Date());
                    userService.updateDcUser(user);
                    userService.updateDcUserDetail(user);


                    // 更新员工在某一个公司下的岗位
                    bankUserMapper.updateUserStationById((String) userPoint.get("id"), station);


                }


            } else {

                List<Map<String, Object>> alreadyContact = bankUserMapper.selectUserInfoByPointId(pointId);


                // 查询改后的手机是否在库中已经注册
                Map<String, Object> existsInDb = userService.selectUserByOnlyMobile(mobile);

                if (existsInDb != null && !existsInDb.isEmpty()) {// 如果该手机已经注册
                    List<Map<String, Object>> userPointList = bankUserMapper.selectUserPointByMobileAndPointId(mobile, pointId);

                    /**
                     * 更新用户信息
                     */
                    existsInDb.put("uname", uname);
                    existsInDb.put("email", email);
                    existsInDb.put("phone", phone);
                    existsInDb.put("mobile", mobile);
                    existsInDb.put("sex", sex);
                    existsInDb.put("udesc", desc);
                    existsInDb.put("workAge", workAge);
                    existsInDb.put("degree", degree);
                     existsInDb.put("age",age);
                    existsInDb.put("updatedBy", loginUserId);
                    existsInDb.put("updateDate", new Date());
                    userService.updateDcUser(existsInDb);
                    userService.updateDcUserDetail(existsInDb);


                    if (userPointList != null && userPointList.size() > 0) { // 如果该注册用户已经与公司绑定

                        // 更新员工在某一个公司下的岗位
                        bankUserMapper.updateUserStationById((String) userPointList.get(0).get("id"), station);

                    } else {// 如果该注册用户没有与公司绑定

                        /**
                         * 插入员工公司关联关系
                         */
                        String userId = (String) existsInDb.get("id");
                        String userPointId = UUID.randomUUID().toString();
                        Map<String, Object> userPoint = new HashMap();
                        userPoint.put("id", userPointId);
                        userPoint.put("userId", userId);
                        userPoint.put("pointId", pointId);
                        userPoint.put("station", station);
                        userPoint.put("createdBy", loginUserId);
                        userPoint.put("createDate", new Date());
                        bankUserMapper.insertBankUserInfo(userPoint);

                    }


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
                    user.put("workAge", workAge);
                    user.put("degree", degree);
                     user.put("age",age);
                    user.put("createdBy", loginUserId);
                    user.put("createDate", new Date());
                    user.put("updatedBy", loginUserId);
                    user.put("updateDate", new Date());
                    userService.insertDcUser(user);
                    userService.insertDcUserDetail(user);


                    /**
                     * 插入员工网点关联关系
                     */
                    String userPointId = UUID.randomUUID().toString();
                    Map<String, Object> userPoint = new HashMap();
                    userPoint.put("id", userPointId);
                    userPoint.put("userId", userId);
                    userPoint.put("pointId", pointId);
                    userPoint.put("station", station);
                    userPoint.put("createdBy", loginUserId);
                    userPoint.put("createDate", new Date());
                    bankUserMapper.insertBankUserInfo(userPoint);

                }


            }


        }else{





            if ("update".equals(flag)) {
                // 通过Id获取员工公司映射关系
                Map<String, Object> userPoint = bankUserMapper.selectUserPointById(id);

                Map<String, Object> user = new HashMap();
                user.put("id", userPoint.get("userId"));
                user.put("uname", uname);
                user.put("email", email);
                user.put("phone", phone);
                user.put("mobile", mobile);
                user.put("sex", sex);
                user.put("udesc", desc);
                user.put("workAge", workAge);
                user.put("degree", degree);
                 user.put("age",age);
                user.put("updatedBy", loginUserId);
                user.put("updateDate", new Date());
                userService.updateDcUser(user);
                userService.updateDcUserDetail(user);
            } else {

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
                user.put("workAge", workAge);
                user.put("degree", degree);
                 user.put("age",age);
                user.put("createdBy", loginUserId);
                user.put("createDate", new Date());
                user.put("updatedBy", loginUserId);
                user.put("updateDate", new Date());
                userService.insertDcUser(user);
                userService.insertDcUserDetail(user);


                /**
                 * 插入员工网点关联关系
                 */
                String userPointId = UUID.randomUUID().toString();
                Map<String, Object> userPoint = new HashMap();
                userPoint.put("id", userPointId);
                userPoint.put("userId", userId);
                userPoint.put("pointId", pointId);
                userPoint.put("station", station);
                userPoint.put("createdBy", loginUserId);
                userPoint.put("createDate", new Date());
                bankUserMapper.insertBankUserInfo(userPoint);

            }
        }


    }





    public List<Map<String, Object>> selectBankUserList(String pointId,String uname, String sex, Integer currentPage, Integer pageSize) {
        if (null == currentPage) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Integer start = (currentPage - 1) * pageSize;
        return bankUserMapper.selectBankUserList(pointId,uname, sex, start, pageSize);
    }

    public Integer selectBankUserListCount(String pointId,String uname, String sex) {
        return bankUserMapper.selectBankUserListCount(pointId,uname, sex);
    }


    public int deleteBankUserInfo(List<String> ids) {
        return bankUserMapper.deleteBankUserInfo(ids);
    }

}
