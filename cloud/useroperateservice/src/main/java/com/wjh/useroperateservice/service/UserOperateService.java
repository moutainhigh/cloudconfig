package com.wjh.useroperateservice.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.RedisKeyConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import com.wjh.useroperateservice.mapper.UserOperateMapper;
import com.wjh.useroperateservicemodel.model.UserOperateDto;
import com.wjh.useroperateservicemodel.model.UserOperatePo;
import com.wjh.useroperateservicemodel.model.UserOperateVo;
import com.wjh.userroleservicemodel.model.UserRoleVo;
import com.wjh.userservicemodel.model.UserVo;
import com.wjh.utils.redis.RedisCacheUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserOperateService {

    @Autowired
    UserOperateMapper userOperateMapper;

    @Autowired
    IdService idService;


    @Autowired
    MenuOperateService menuOperateService;

    @Autowired
    UserService userService;


    @Autowired
    UserOperateService userOperateService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleOperateService roleOperateService;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    public int update(UserOperateDto userOperateDto, Long loginUserId) {
        Long userId = userOperateDto.getUserId();
        List<Long> operateIdList = userOperateDto.getOperateIdList();

        //删除旧的映射
        userOperateMapper.deleteByUserId(userId);
        //添加新的映射
        List<UserOperatePo> userOperatePoList = new ArrayList<UserOperatePo>();
        for (int i = 0; i < operateIdList.size(); i++) {
            UserOperatePo userOperatePo = new UserOperatePo();
            userOperatePo.setUserId(userId);
            userOperatePo.setOperateId(operateIdList.get(i));
            Long id = idService.generateId();
            userOperatePo.setId(id);
            Date date = new Date();
            userOperatePo.setCreateDate(date);
            userOperatePo.setUpdateDate(date);
            userOperatePo.setUpdatedBy(loginUserId);
            userOperatePo.setCreatedBy(loginUserId);
            userOperatePoList.add(userOperatePo);
        }
        int count = userOperateMapper.insertList(userOperatePoList);
        //清除该用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE, userId + "");
        return count;
    }

    public int deleteByUserId(Long userId, Long loginUserId) {
        int count = userOperateMapper.deleteByUserId(userId);
        //清除用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE, userId + "");
        return count;
    }

    public int deleteByOperateId(Long operateId, Long loginUserId) {
        int count = userOperateMapper.deleteByOperateId(operateId);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return count;
    }


    public List<UserOperateVo> listByUserId(Long userId) {


        List<UserOperateVo> userOperateVoList = userOperateMapper.listByUserId(userId);
        List<Long> userIdList = new ArrayList<Long>();
        List<Long> operateIdList = new ArrayList<Long>();
        for (int i = 0; i < userOperateVoList.size(); i++) {
            UserOperateVo userOperateVo = userOperateVoList.get(i);
            userIdList.add(userOperateVo.getUserId());
            operateIdList.add(userOperateVo.getOperateId());
        }

        if (userOperateVoList.size() > 0) {
            ResponseModel<List<OperateVo>> operateVoResponseModel = menuOperateService.selectByIds(operateIdList);
            List<OperateVo> operateVoList = (List<OperateVo>) operateVoResponseModel.getResModel();
            ResponseModel<List<UserVo>> userVoResponseModel = userService.selectByIds(userIdList);
            List<UserVo> userVoList = (List<UserVo>) userVoResponseModel.getResModel();
            for (int i = 0; i < userOperateVoList.size(); i++) {
                UserOperateVo userOperateVo = userOperateVoList.get(i);
                for (int j = 0; j < operateVoList.size(); j++) {
                    if (userOperateVo.getOperateId().equals( operateVoList.get(j).getId())) {
                        userOperateVo.setOperateName(operateVoList.get(j).getOperateName());
                        break;
                    }
                }

                for (int j = 0; j < userVoList.size(); j++) {
                    if (userOperateVo.getUserId().equals(userVoList.get(j).getId())) {
                        userOperateVo.setUserName(userVoList.get(j).getName());
                        break;
                    }
                }

            }

        }


        return userOperateVoList;
    }


    public List<OperateVo> listAllByUserId(Long userId) {


        //先从redis中取，如果redis中没有，则从数据库中取,并缓存到redis

        List<OperateVo> list = (List<OperateVo>) redisCacheUtil.getCacheMapVaue(RedisKeyConstant.USER_OPERATE_TABLE, userId + "");

        if (null == list) {

            //直接对用户设置的权限
            List<UserOperateVo> userOperateVoList = userOperateMapper.listByUserId(userId);
            //获取用户角色
            ResponseModel<List<UserRoleVo>> userRoleVoResponseModel = userRoleService.listByUserId(userId);
            List<UserRoleVo> userRoleVoList = (List<UserRoleVo>) userRoleVoResponseModel.getResModel();
            List<Long> roleIdList = new ArrayList<>();
            for (int i = 0; i < userRoleVoList.size(); i++) {
                roleIdList.add(userRoleVoList.get(i).getRoleId());
            }


            //获取角色对应的权限
            ResponseModel roleOperateVoResponseModel = roleOperateService.listByRoleIds(roleIdList);
            List<RoleOperateVo> roleOperateVoList = (List<RoleOperateVo>) roleOperateVoResponseModel.getResModel();


            List<Long> operateIdList = new ArrayList<>();
            for (int i = 0; i < userOperateVoList.size(); i++) {
                operateIdList.add(userOperateVoList.get(i).getOperateId());
            }

            for (int i = 0; i < roleOperateVoList.size(); i++) {
                operateIdList.add(roleOperateVoList.get(i).getOperateId());
            }

            ResponseModel<List<OperateVo>> operateVoResponseModel = menuOperateService.selectByIds(operateIdList);

            List<OperateVo> operateVoList = (List<OperateVo>) operateVoResponseModel.getResModel();

            for (int i = 0; i < operateVoList.size(); i++) {
                OperateVo operateVo = operateVoList.get(i);
                for (int j = 0; j < userOperateVoList.size(); j++) {
                    if (operateVo.getId().equals(userOperateVoList.get(j).getOperateId())) {
                        operateVo.setFromUser(true);
                        break;
                    }
                }

                for (int j = 0; j < roleOperateVoList.size(); j++) {
                    if (operateVo.getId().equals(roleOperateVoList.get(j).getOperateId())) {
                        operateVo.setFromRole(true);
                        break;
                    }
                }

            }


            //缓存入redis中
            list = operateVoList;
            Map<String, List<OperateVo>> map = new HashMap<>();
            map.put(userId + "", list);
            redisCacheUtil.setCacheMap(RedisKeyConstant.USER_OPERATE_TABLE, map);

        }
        return list;

    }


}
