package com.xkd.service;

import com.xkd.mapper.RoleMapper;
import com.xkd.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * �����ˣ��׽���
 * ����ʱ�䣺2017-11-23
 * ������������ɫ��صķ��������
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;


    public Integer saveRole(Role role) {

        Integer id = roleMapper.saveRole(role);

        return id;

    }


    public Integer updateRole(Map<String, Object> roleMap) {

        Integer num = roleMapper.updateRole(roleMap);

        return num;

    }


    public List<Map<String, Object>> selectRoles(String content, int currentPage, int pageSize) {

        List<Map<String, Object>> maps = roleMapper.selectRoles(content, currentPage, pageSize);

        return maps;
    }


    public Integer deleteRolesByIds(String ids) {

        Integer num = roleMapper.deleteRolesByIds(ids);

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
        return roleMapper.deleteRole(id);
    }

    public List<Map<String, Object>> selectRoleByName(String roleName) {
        return roleMapper.selectRoleByName(roleName);
    }

    public Map<String,Object> selectRoleById(String id){
        return roleMapper.selectRoleById(id);
    }


   public List<Map<String,Object>> selectRolesUnderCompany(String pcCompanyId,Integer includeSuperAdmin){
        return  roleMapper.selectRolesUnderCompany(pcCompanyId,includeSuperAdmin);
    }


    public void updateUserRole(ArrayList arrayList, String roleId) {
        roleMapper.updateUserRole(arrayList,roleId);
    }
}
