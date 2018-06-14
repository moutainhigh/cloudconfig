package com.xkd.service;

import com.xkd.mapper.BankPointMapper;
import com.xkd.model.User;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankPointService {


    @Autowired
    BankPointMapper bankPointMapper;
    @Autowired
    UserDataPermissionService userDataPermissionService;
    @Autowired
    CompanyRelativeUserService companyRelativeUserService;
    @Autowired
    UserService userService;

    public Integer saveBankPoint(Map<String, Object> map) {
        return bankPointMapper.saveBankPoint(map);
    }

    public Integer updateBankPointById(Map<String, Object> map) {
        return bankPointMapper.updateBankPointById(map);
    }

    public List<Map<String,Object>> selectBankPointsByContent(String content, int pageSize, int start, List<String> list,
                                                              String companyId) {
        return bankPointMapper.selectBankPointsByContent(content,pageSize,start,list,companyId);
    }

    public Integer deleteBankPointByIds(List<String> idList) {
        return bankPointMapper.updateBankPointStatusByIds(idList);
    }

    public Integer deleteProjectPointByPointIds(List<String> pointIdList) {
        return bankPointMapper.updateBankPointProjectRevokeStatusByPointIds(pointIdList);
    }

    public Map<String,Object> selectBankPointByName(String pointName,List<String > departmentIdList) {
        return bankPointMapper.selectBankPointByName(pointName,departmentIdList);
    }

    public Map<String,Object> selectBankPointsTotalByContent(String content, String loginUserId,String companyId,Integer currentPage,Integer pageSize) {
       Integer start=0;
        if(currentPage==null || pageSize==null){
            currentPage =1;
            pageSize = 10;
        }
        start=(currentPage-1)*pageSize;

        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
       Map<String,Object>  map=userService.selectUserById(loginUserId);
        List<String> list=null;
        if ("1".equals(map.get("roleId"))){
            list = userDataPermissionService.getDataPermissionDepartmentIdList("1",loginUserId);
        }else{
            list = userDataPermissionService.getDataPermissionDepartmentIdList((String)loginUserMap.get("pcCompanyId"),loginUserId);
        }
        System.out.println(list.toString());
        List<Map<String,Object>> listMap =  bankPointMapper.selectBankPointsByContent(content,pageSize,start,list,companyId);
        Integer num = bankPointMapper.selectBankPointsTotalByContent(content,list,companyId);

        //查询相关人员
        List<String> companyIds = new ArrayList<>();
        for (int i = 0; i < listMap.size(); i++) {
            Map<String, Object> data = listMap.get(i);
            String comId=(String) data.get("companyId");
            if (StringUtils.isNotBlank(comId)){
                if (!companyIds.contains(comId)) {
                    companyIds.add(comId);
                }
            }
        }

        List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);
        //整理数据，将相关人员整合到相应的记录中去
        for (int i = 0; i < listMap.size(); i++) {
            Map<String, Object> data = listMap.get(i);
            List<Map<String, Object>> relUserList = new ArrayList<>();
            data.put("relativeUserList", relUserList);
            for (int j = 0; j < relativeList.size(); j++) {
                if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
                    relUserList.add(relativeList.get(j));
                }
            }
        }


        Map<String,Object>  resultMap=new HashMap<>();
        resultMap.put("list",listMap);
        resultMap.put("total",num);
        return resultMap;
     }









    public Integer deleteProjectPointByProjectIds(List<String> idList) {

        return bankPointMapper.updateProjectPointRevokeStatusByProjectIds(idList);
    }
}
