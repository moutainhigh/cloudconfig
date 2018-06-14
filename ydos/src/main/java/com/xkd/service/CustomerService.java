package com.xkd.service;

import com.xkd.mapper.CustomerMapper;
import com.xkd.model.Role;
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
    AttachmentService attachmentService;








    public List<Map<String,Object>> searchPcCompany(String startDate,String endDate,String departmentName,String province,String city,Integer status,List<String> idList,Integer currentPage,Integer pageSize){

        if (currentPage==null){
            currentPage=1;
        }
        Integer start=(currentPage-1)*pageSize;
        return customerMapper.searchPcCompany(startDate,endDate,departmentName,province,city,status,idList,start,pageSize);
    }
    public int  searchPcCompanyCount(String startDate, String endDate,String departmentName,String province,String city,Integer status,List<String> idList){
        return customerMapper.searchPcCompanyCount(startDate,endDate,departmentName,province,city,status,idList);
    }
    public int savePcCompany(Map<String,Object> map,String loginUserId){
        return customerMapper.insertPcCompany(map);
    }
    public int updatePcCompany(Map<String,Object> map){
        return customerMapper.updatePcCompany(map);
    }
    public List<Map<String,Object>> selectPcAdminUserByPcCompanyId(String pcCompanyId){
        return customerMapper.selectPcAdminUserByPcCompanyId(pcCompanyId);
    }





    public Map<String,Object> selectPcCompanyById(String id){
        List<Map<String,Object>> honorMapList=attachmentService.selectAttachmentByObjectId(id);
        List<String> honorImgList=new ArrayList<>();
        for (int i = 0; i <honorMapList.size() ; i++) {
            honorImgList.add((String)honorMapList.get(i).get("url"));
        }
        Map<String,Object> map= customerMapper.selectPcCompanyById(id);
        map.put("honorImgList",honorImgList);
        return map;
    }



}
