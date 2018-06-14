package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.mapper.*;
import com.xkd.model.Company;
import org.apache.ibatis.annotations.Param;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    ContractMapper contractMapper;
    @Autowired
    InspectionMapper inspectionMapper;
    @Autowired
    YDrepaireMapper yDrepaireMapper;

    @Autowired
    DeviceMapper deviceMapper;


    @Autowired
    DeviceGroupService deviceGroupService;


    @Autowired
    UserService userService;

    public List<Company> selectCompanyByNameIncludingDeleted(String companyName, String pcCompanyId) {
        return companyMapper.selectCompanyByNameIncludingDeleted(companyName, pcCompanyId);
    }

    public int updateCompanyInfoById(Map<String, Object> company) {

        int num = companyMapper.updateCompanyInfoById(company);
        if (null != company && null != company.get("companyName") && !"".equals(company.get("companyName"))) {
            companyMapper.updatePagerFileName(company);
        }

        return num;
    }


    public int deleteCompanyById(List<String> idList) {
        int num=0;
        if (idList.size()>0){
           num= companyMapper.deleteCompanyById(idList);

       }
        return num;
    }


    public Company selectCompanyInfoById(String companyId) {

        Company company = companyMapper.selectCompanyInfoById(companyId);

        List<Map<String ,Object>> contactorList=companyContactorService.selectCompanyContactorByCompanyId(companyId,null,null,1,100000);
        List<Map<String,Object>>  contractList=contractMapper.selectContactByCompanyId(companyId);
        Integer repaireCount=yDrepaireMapper.selectRepaireCountByCompanyId(companyId);
        Integer inspectionPlanCount=inspectionMapper.selectInspectionPlanCountByCompanyId(companyId);
        Map<String,Map<String,Object>>  responsibleUserMap=new HashMap<>();


        List<String> companyIdList=new ArrayList<>();
        companyIdList.add(companyId);

        List<Map<String,Object>>   deviceGroupList=deviceGroupService.selectDeviceGroupByCompanyIds(companyIdList);


        for (int i = 0; i <deviceGroupList.size() ; i++) {
            Map<String,Object> deviceGroup=deviceGroupList.get(i);
            Map<String,Object> map=new HashMap<>();
            map.put("id",deviceGroup.get("responsibleUserId"));
            map.put("uname",deviceGroup.get("responsibleUserName"));
            map.put("mobile",deviceGroup.get("responsibleUserMobile"));
            responsibleUserMap.put((String)deviceGroup.get("responsibleUserId"),map);
         }
        /*去除重复技师
         */
        Set<String>  idSet= responsibleUserMap.keySet();
        Iterator<String> iterator=idSet.iterator();
        List<Map<String,Object>> responsibleUserList=new ArrayList<>();
        while (iterator.hasNext()){
            responsibleUserList.add(responsibleUserMap.get(iterator.next()));
        }

         int total=deviceMapper.selectTotalDeviceByCompanyId(companyId);
        company.setTotalDevice(total);
        company.setContactorList(contactorList);
        company.setContractList(contractList);
        company.setRepaireCount(repaireCount);
        company.setInspectionPlanCount(inspectionPlanCount);
        company.setResponsibleUserList(responsibleUserList);


        return company;
    }



    public Company getCompanyById(String companyId) {

        Company company = companyMapper.selectCompanyInfoById(companyId);

        return company;
    }

    public List<Map<String, Object>> searchCompanyByName(String companyName,String pcCompanyId, String userLevel, String fromDate, String toDate, Integer currentPage, Integer pageSize) {

        int start=0;
        if ( null==currentPage){
            currentPage=1;
        }
        if (null==pageSize){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;

        return companyMapper.searchCompanyByName(companyName, pcCompanyId,userLevel,fromDate,toDate, start, pageSize);
    }


    public int searchCompanyCountByName(String companyName,String pcCompanyId, String userLevel, String fromDate, String toDate ) {


        return companyMapper.searchCompanyCountByName(companyName, pcCompanyId,userLevel,fromDate,toDate );
    }


    public Integer insertCompanyInfo(Map<String, Object> company) {

        Integer num = companyMapper.insertCompanyInfo(company);

        return num;
    }


    public void deleteByCompanyById(String id) {
        companyMapper.deleteByCompanyById(id);
    }

    public List<String> selecAllCompanyId() {
        return companyMapper.selecAllCompanyId();
    }

    List<Map<String,Object>>  selectCompanyByIds( List<String> idList){
        if (idList.size()==0){
            return new ArrayList<>();
        }
        return companyMapper.selectCompanyByIds(idList);
    }

    public List<Map<String, Object>> findCompanyByPcCompanyId(String pcCompanyId, String userLevel, String companyName){
        return companyMapper.findCompanyByPcCompanyId(pcCompanyId, userLevel, companyName);
    }


    public List<Map<String,Object>>  selectCompanyIdByTechinicanId( List<String> responsibleUserIdList,String pcCompanyId){
            return companyMapper.selectCompanyIdByTechinicanId(responsibleUserIdList,pcCompanyId);
    }

    public List<Map<String, Object>> selectAllCompany(Integer currentPage, Integer pageSize){
        Integer start = (currentPage - 1) * pageSize;
        return companyMapper.selectAllCompany(start, pageSize);
    }

    public Integer countAllCompany(){
        return companyMapper.countAllCompany();
    }

    public List<Map<String,Object>> selectInspectionCompanyIdByDepartmentId(List<String> departmentIdList,String pcCompanyId){
        if (departmentIdList.size()==0){
            return new ArrayList<>(0);
        }
        return companyMapper.selectInspectionCompanyIdByDepartmentId(departmentIdList,  pcCompanyId);
    }


}
