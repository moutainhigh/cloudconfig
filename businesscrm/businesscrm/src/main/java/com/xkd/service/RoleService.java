package com.xkd.service;

import com.xkd.mapper.RoleMapper;
import com.xkd.model.RedisTableKey;
import com.xkd.model.Role;
import com.xkd.utils.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    RedisCacheUtil redisCacheUtil;

    @Autowired
    UserService userService;

    public Integer saveRole(Role role) {

        Integer id = roleMapper.saveRole(role);

        return id;

    }


    public Integer updateRole(Map<String, Object> roleMap) {

        Integer num = roleMapper.updateRole(roleMap);
        //查询角色对对应的用户
        List<String> userIdList= userService.selectUserByRoleIds((String) roleMap.get("id"));
        for (int i = 0; i <userIdList.size() ; i++) {
            redisCacheUtil.delete(RedisTableKey.USER,userIdList.get(i));
        }
        return num;

    }


    public List<Map<String, Object>> selectRoles(String content, int currentPage, int pageSize) {

        List<Map<String, Object>> maps = roleMapper.selectRoles(content, currentPage, pageSize);

        return maps;
    }


    public Integer deleteRolesByIds(List<String> idList) {

        Integer num = roleMapper.deleteRolesByIds(idList);

        //查询角色下对应的用户
        List<String>  userIdList=userService.selectUserByRoleIds(idList);

        /**
         * 删除缓存
         */
        for (int i = 0; i <userIdList.size() ; i++) {
            redisCacheUtil.delete(RedisTableKey.USER,userIdList.get(i));
        }
        return num;

    }


   public List<Map<String,Object>>  selectRolesByIds(  List<String> idList){
       if (idList.size()>0){
           return roleMapper.selectRolesByIds(idList);
       }
       return new ArrayList<>(0);
    }


    public Integer selectRolesCount(String contentStr) {

        Integer num = roleMapper.selectRolesCount(contentStr);

        return num;

    }





    public int deleteRole(String id) {
        int count= roleMapper.deleteRole(id);

        //查询该角色对应的用户
        List<String> userIdList=userService.selectUserByRoleIds(id);

        //删除缓存中受影响的旧数据
        for (int i = 0; i <userIdList.size() ; i++) {
            redisCacheUtil.delete(RedisTableKey.USER,userIdList.get(i));
        }
        return count;
    }


    public Map<String,Object> selectRoleById(String id){
        return roleMapper.selectRoleById(id);
    }


   public List<Map<String,Object>> selectRolesUnderCompany(String pcCompanyId,Integer includeSuperAdmin){
        return  roleMapper.selectRolesUnderCompany(pcCompanyId,includeSuperAdmin);
    }

    public List<Map<String,Object>> selectOperateIdRoleId(String roleId){
        return  roleMapper.selectOperateIdRoleId(roleId);
    }


}
