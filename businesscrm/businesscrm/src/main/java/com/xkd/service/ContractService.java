package com.xkd.service;

import com.xkd.mapper.ContractAttachementMapper;
import com.xkd.mapper.ContractMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2018/4/27.
 */
@Service
public class ContractService {
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    ContractAttachementService contractAttachementService;

    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    public void insertContract(Map<String, Object> map, String loginUserId) {
        String contractId = UUID.randomUUID().toString();
        map.put("id", contractId);
        map.put("createdBy", loginUserId);
        map.put("createDate", new Date());
        map.put("updatedBy", loginUserId);
        map.put("updateDate", new Date());
        contractMapper.insertContract(map);
        List<String> urls = (List<String>) map.get("urls");
        List<Map<String, Object>> attachmentList = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            Map<String, Object> mm = new HashMap<>();
            mm.put("id", UUID.randomUUID().toString());
            mm.put("contractId", contractId);
            mm.put("url", urls.get(i));
            mm.put("createdBy", loginUserId);
            mm.put("createDate", new Date());
            attachmentList.add(mm);
        }
        contractAttachementService.insertList(attachmentList);

    }


    public void updateContract(Map<String, Object> map, String loginUserId) {
        String contractId = (String) map.get("id");
        map.put("updatedBy", loginUserId);
        map.put("updateDate", new Date());
        contractMapper.updateContract(map);
        List<String> urls = (List<String>) map.get("urls");
        List<Map<String, Object>> attachmentList = new ArrayList<>();
        contractAttachementService.deleteByContractId(contractId);
        for (int i = 0; i < urls.size(); i++) {
            Map<String, Object> mm = new HashMap<>();
            mm.put("id", UUID.randomUUID().toString());
            mm.put("contractId", contractId);
            mm.put("url", urls.get(i));
            mm.put("createdBy", loginUserId);
            mm.put("createDate", new Date());
            attachmentList.add(mm);
        }
        contractAttachementService.insertList(attachmentList);

    }


    public List<Map<String, Object>> searchContract(String companyId,List<String> departmentIdList, String searchValue, String startDate, String endDate,String loginUserId, Integer currentPage, Integer pageSize) {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        int start = (currentPage - 1) * pageSize;
        List<Map<String,Object>> contractList= contractMapper.searchContract(companyId,departmentIdList,searchValue, startDate, endDate,loginUserId,start,pageSize);
        List<String> companyIds=new ArrayList<>();
        for (int i = 0; i <contractList.size() ; i++) {
            companyIds.add((String) contractList.get(i).get("companyId"));
        }

        List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);
        //整理数据，将相关人员整合到相应的记录中去
        for (int i = 0; i < contractList.size(); i++) {
            Map<String, Object> data = contractList.get(i);
            List<Map<String, Object>> relUserList = new ArrayList<>();
            data.put("relativeUserList", relUserList);
            for (int j = 0; j < relativeList.size(); j++) {
                if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
                    relUserList.add(relativeList.get(j));
                }
            }
        }

        return contractList;
    }

    public int searchContractCount(String companyId,List<String> departmentIdList, String searchValue, String startDate, String endDate,String loginUserId) {
        return contractMapper.searchContractCount(companyId,departmentIdList,searchValue, startDate, endDate,loginUserId);
    }



    public Map<String,Object> contactDetail(String id){
        Map<String,Object> map= contractMapper.contactDetail(id);
        String urls= (String) map.get("urls");
        List<Map<String ,Object>> list=new ArrayList<>();
        map.put("urls",list);
        if (StringUtils.isNotBlank(urls)){
            String[] urlArray=urls.split("\\?");
            for (int i = 0; i <urlArray.length ; i++) {
                Map<String,Object> m=new HashMap<>();
                m.put("url",urlArray[i]);
                String[] strA=urlArray[i].split("___");
                m.put("name",strA[1]);
                list.add(m);
            }
        }

        return map;

    }


    public Map<String,Object> selectByContractNo(String contractNo,String pcCompanyId){
        return contractMapper.selectByContractNo(contractNo,pcCompanyId);
    }

    public int deleteContractByIds(List<String> idList){
        if (idList.size()==0){
            return 0;
        }
        return contractMapper.deleteContractByIds(idList);
    }


    public List<Map<String,Object>> selectContractAttachement(String contractId){
        return contractMapper.selectContractAttachement(contractId);
    }



}
