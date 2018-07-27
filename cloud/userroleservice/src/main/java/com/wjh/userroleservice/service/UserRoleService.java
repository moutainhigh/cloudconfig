package com.wjh.userroleservice.service;

import com.netflix.discovery.converters.Auto;
import com.wjh.common.model.RedisKeyConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.idconfiguration.model.IdGenerator;
import com.wjh.roleservicemodel.model.RoleVo;
import com.wjh.userroleservice.mapper.UserRoleMapper;
import com.wjh.userroleservicemodel.model.UserRoleDto;
import com.wjh.userroleservicemodel.model.UserRolePo;
import com.wjh.userroleservicemodel.model.UserRoleVo;
import com.wjh.userservicemodel.model.UserVo;
import com.wjh.utils.redis.RedisCacheUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserRoleService {


    @Autowired
    IdGenerator idGenerator;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    public int update(UserRoleDto userRoleDto, Long loginUserId) {
        Long userId = userRoleDto.getUserId();
        List<Long> roleIdList = userRoleDto.getRoleIdList();
        userRoleMapper.deleteByUserId(userId);
        List<UserRolePo> userRolePoList = new ArrayList<UserRolePo>();
        for (int i = 0; i < roleIdList.size(); i++) {
            UserRolePo userRolePo = new UserRolePo();
            Long id = idGenerator.generateId();
            userRolePo.setId(id);
            userRolePo.setUserId(userId);
            userRolePo.setRoleId(roleIdList.get(i));
            Date date = new Date();
            userRolePo.setCreatedBy(loginUserId);
            userRolePo.setUpdatedBy(loginUserId);
            userRolePo.setCreateDate(date);
            userRolePo.setUpdateDate(date);
            userRolePoList.add(userRolePo);
        }
        int count = userRoleMapper.insertList(userRolePoList);
        //清除该用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE,userId+"");
        return count;
    }

    public int deleteByUserId(Long userId) {
        int count= userRoleMapper.deleteByUserId(userId);
        //清除该用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE,userId+"");
        return count;
    }

    public int deleteByRoleId(Long roleId) {
        int count= userRoleMapper.deleteByRoleId(roleId);
        //清除所有用户权限缓存
        redisCacheUtil.delete(RedisKeyConstant.USER_OPERATE_TABLE);
        return count;

    }


    public List<UserRoleVo> listByUserId(Long userId) {
        List<UserRoleVo> userRoleVoList = userRoleMapper.listByUserId(userId);
        List<Long> userIdList = new ArrayList<Long>();
        List<Long> roleIdList = new ArrayList<Long>();

        for (int i = 0; i < userRoleVoList.size(); i++) {
            userIdList.add(userRoleVoList.get(i).getUserId());
            roleIdList.add(userRoleVoList.get(i).getRoleId());
        }


        if (userRoleVoList.size() > 0) {
            ResponseModel<List<UserVo>> userVoResponseModel = userService.selectByIds(userIdList);
            ResponseModel<List<RoleVo>> roleVoResponseModel = roleService.selectByIds(roleIdList);
            List<UserVo> userVoList = (List<UserVo>) userVoResponseModel.getResModel();
            List<RoleVo> roleVoList = (List<RoleVo>) roleVoResponseModel.getResModel();


            for (int i = 0; i < userRoleVoList.size(); i++) {
                UserRoleVo userRoleVo = userRoleVoList.get(i);
                for (int j = 0; j < userVoList.size(); j++) {
                    if (userRoleVo.getUserId().equals(userVoList.get(j).getId())) {
                        userRoleVo.setUserName(userVoList.get(j).getName());
                        break;
                    }
                }

                for (int j = 0; j < roleVoList.size(); j++) {
                    if (userRoleVo.getRoleId().equals(roleVoList.get(j).getId())) {
                        userRoleVo.setRoleName(roleVoList.get(j).getRoleName());
                        break;
                    }
                }
            }

        }
        return userRoleVoList;
    }
}

