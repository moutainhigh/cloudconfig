package com.xkd.service;

import com.xkd.mapper.CustomerMapper;
import com.xkd.model.RedisTableKey;
import com.xkd.utils.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2018/1/9.
 */
@Service
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    SysRoleOperateService sysRoleOperateService;

    @Autowired
    UserService userService;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    /**
     * 保存客户公司菜单，即客户公司具有哪些菜单的权限
     * 客户公司是指部门表中的二级结点，凡处于二级节点的部门都表示客户公司
     * @param menuIdList
     * @param pcCompanyId
     * @param loginUserId
     */
    public void saveCustomerMenu( List<String> menuIdList,String  pcCompanyId,String loginUserId){

        /**
         * 查看该公司下有没有创建管理员角色，如果没有则自动创建一个
         */
        List<Map<String,Object>> list= customerMapper.selectPcAdminRole(pcCompanyId);
        if (list.size()==0){
            Map<String,Object> roleMap=new HashMap<>();
            roleMap.put("id", UUID.randomUUID().toString());
            roleMap.put("roleName","管理员");
            roleMap.put("content","管理员");
            roleMap.put("createdBy",loginUserId);
            roleMap.put("createDate",new Date());
            roleMap.put("updatedBy",loginUserId);
            roleMap.put("updateDate",new Date());
            roleMap.put("pcCompanyId",pcCompanyId);
            roleMap.put("isAdmin",1);
            customerMapper.insertAdminRole(roleMap);
        }


        /**
         * 插入公司菜单关系
         */
        List<Map<String,Object>> customerMenuList=new ArrayList<>();
        for (int i = 0; i <menuIdList.size() ; i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",UUID.randomUUID().toString());
            map.put("menuId",menuIdList.get(i));
            map.put("departmentId",pcCompanyId);
            customerMenuList.add(map);
        }


        customerMapper.deleteCustomerMenuByDepartmentId(pcCompanyId);
        if (customerMenuList.size()>0) {
            customerMapper.insertCustomerMenuList(customerMenuList);
        }

         updateAdminRole(pcCompanyId,loginUserId);

     }



    public void updateAdminRole(String pcCompanyId,String loginUserId){

        /**
         * 查看该公司下有没有创建管理员角色，如果没有则自动创建一个
         */
        List<Map<String,Object>> list= customerMapper.selectPcAdminRole(pcCompanyId);
        if (list.size()==0){
            Map<String,Object> roleMap=new HashMap<>();
            roleMap.put("id", UUID.randomUUID().toString());
            roleMap.put("roleName","管理员");
            roleMap.put("content","管理员");
            roleMap.put("createdBy",loginUserId);
            roleMap.put("createDate",new Date());
            roleMap.put("updatedBy",loginUserId);
            roleMap.put("updateDate",new Date());
            roleMap.put("pcCompanyId",pcCompanyId);
            roleMap.put("isAdmin",1);
            customerMapper.insertAdminRole(roleMap);
        }
        list= customerMapper.selectPcAdminRole(pcCompanyId);
        List<String> operateIdList=customerMapper.selectOperateIdsByPcCompanyId(pcCompanyId);
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (int i = 0; i <operateIdList.size() ; i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("roleId", list.get(0).get("id"));
            map.put("operateId", operateIdList.get(i));
            mapList.add(map);
        }
        //删除旧的角色权限关系
        sysRoleOperateService.deleteByRoleId( (String)list.get(0).get("id"));
        //插入新的权限关系
        sysRoleOperateService.insertList(mapList);

    }




    public List<Map<String,Object>> searchPcCompany(String startDate,String endDate,String departmentName,String province,String city,Integer status,Integer currentPage,Integer pageSize){

        if (currentPage==null){
            currentPage=1;
        }
        Integer start=(currentPage-1)*pageSize;
        return customerMapper.searchPcCompany(startDate,endDate,departmentName,province,city,status,start,pageSize);
    }
    public int  searchPcCompanyCount(String startDate, String endDate,String departmentName,String province,String city,Integer status){
        return customerMapper.searchPcCompanyCount(startDate,endDate,departmentName,province,city,status);
    }
    public int savePcCompany(Map<String,Object> map,String loginUserId){


        /**
         * 添加该公司下的管理员帐号
         */
        Map<String,Object> roleMap=new HashMap<>();
        roleMap.put("id", UUID.randomUUID().toString());
        roleMap.put("roleName","管理员");
        roleMap.put("content","管理员");
        roleMap.put("createdBy",loginUserId);
        roleMap.put("createDate",new Date());
        roleMap.put("updatedBy",loginUserId);
        roleMap.put("updateDate",new Date());
        roleMap.put("pcCompanyId",map.get("id"));
        roleMap.put("isAdmin",1);
        customerMapper.insertAdminRole(roleMap);

        return customerMapper.insertPcCompany(map);
    }
    public int updatePcCompany(Map<String,Object> map){
        int count= customerMapper.updatePcCompany(map);
        /**
         * 删除缓存中的数据
         */
        List<String> userIdList=userService.selectUserIdByDepartmentId((String) map.get("id"));
        for (int i = 0; i <userIdList.size() ; i++) {
            redisCacheUtil.delete(RedisTableKey.USER,userIdList.get(i));
        }
        return count;
    }
    public List<Map<String,Object>> selectPcAdminUserByPcCompanyId(String pcCompanyId){
        return customerMapper.selectPcAdminUserByPcCompanyId(pcCompanyId);
    }


   public List<Map<String,Object>> selectPcAdminRole(String  pcCompanyId){
       return customerMapper.selectPcAdminRole(pcCompanyId);
   }


    public Map<String,Object> selectPcCompanyById(String id){
        return customerMapper.selectPcCompanyById(id);
    }

    public List<String> selectCustomerMenuByPcCompanyId(String pcCompanyId){
        return customerMapper.selectCustomerMenuByPcCompanyId(pcCompanyId);
    }

}
