package com.xkd.service;

import com.xkd.mapper.ContractMapper;
import com.xkd.mapper.DeviceMapper;
import com.xkd.utils.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2018/2/12.
 */
@Service
public class ContractService {
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    AttachmentService attachmentService;

    public int insertContact(Map<String, Object> map) {
        return contractMapper.insertContact(map);
    }


    public int updateContact(Map<String, Object> map) {
        return contractMapper.updateContact(map);
    }




    public int deleteContract(List<String>  idList){
        return contractMapper.deleteContractByIdList(idList);
    }


    public List<Map<String, Object>> selectContractByContractNo(String contractNo) {
        return contractMapper.selectContractByContractNo(contractNo);
    }

    public Map<String,Object> selectContractByContractNoAndPcCompanyId(String contractNo,String pcCompanyId){
        return contractMapper.selectContractByContractNoAndPcCompanyId(contractNo,pcCompanyId);
    }


    public List<Map<String, Object>> searchContract(String pcCompanyId, List<String> companyIdList, String contractName, String contractNo, Integer type, String endDate, Integer currentPage, Integer pageSize) {
        int start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return contractMapper.searchContract(pcCompanyId, companyIdList, contractName, contractNo, type, endDate, start, pageSize);
    }

    public Integer searchContractCount(String pcCompanyId, List<String> companyIdList, String contractName, String contractNo, Integer type, String endDate) {
        return contractMapper.searchContractCount(pcCompanyId, companyIdList, contractName, contractNo, type, endDate);

    }




    public Map<String, Object> selectContractById(String id) {
        Map<String, Object> map = contractMapper.selectContractById(id);
        List<Map<String, Object>> attachementMapList = attachmentService.selectAttachmentByObjectId(id);
        List<String> attachmentList = new ArrayList<>();
        for (int i = 0; i < attachementMapList.size(); i++) {
            attachmentList.add((String) attachementMapList.get(i).get("url"));
        }
        map.put("attachmentList", attachmentList);

        return map;
    }


    public Map<String, Object> getContractById(String id) {
        Map<String, Object> map = contractMapper.selectContractById(id);
        return map;
    }


    public    List<Map<String,Object>> selectContractByGroupId( String groupId, String pcCompanyId){
        return contractMapper.selectContractByGroupId(groupId,pcCompanyId);
    }



    public Integer selectContractCountByGroupId(String groupId, String pcCompanyId){
        return contractMapper.selectContractCountByGroupId(groupId,pcCompanyId);
    }


    public Map<String,Object>  selectContractStatistic(String pcCompanyId){
            Map<String,Object> map=new HashMap<>();

        Calendar  calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr=simpleDateFormat.format(new Date())+" 23:59:59";
        Date nowDate=DateUtils.stringToDate(nowStr,"yyyy-MM-dd HH:mm:ss");
        calendar.setTime(nowDate);
        calendar.add(calendar.MONTH, 1);
        Date oneMonthLater=calendar.getTime();
        String oneMonthLaterStr=simpleDateFormat2.format(oneMonthLater);
        calendar.add(calendar.MONTH,1);
        Date twoMonthLater=calendar.getTime();
        String twoMonthLaterStr=simpleDateFormat2.format(twoMonthLater);

        calendar.add(calendar.MONTH,1);
        Date threeMonthLater=calendar.getTime();
        String threeMonthLaterStr=simpleDateFormat2.format(threeMonthLater);
        Integer dueCount=contractMapper.selectContractStatistic(null,nowStr,pcCompanyId);
            Integer  onemonthCount=contractMapper.selectContractStatistic(nowStr,oneMonthLaterStr,pcCompanyId);
        Integer  twomonthCount=contractMapper.selectContractStatistic(oneMonthLaterStr,twoMonthLaterStr,pcCompanyId);

        Integer  threemonthCount=contractMapper.selectContractStatistic(twoMonthLaterStr,threeMonthLaterStr,pcCompanyId);
        map.put("dueCount",dueCount);
        map.put("oneMonthCount",onemonthCount);
        map.put("twoMonthCount",twomonthCount);
        map.put("threeMonthCount",threemonthCount);
        return map;

     }


   public List<Map<String,Object> > selectDueContract(Integer flag,String pcCompanyId){
       Map<String,Object> map=new HashMap<>();

       Calendar  calendar=Calendar.getInstance();
       SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
       SimpleDateFormat simpleDateFormat2 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String nowStr=simpleDateFormat.format(new Date())+" 23:59:59";
       Date nowDate=DateUtils.stringToDate(nowStr,"yyyy-MM-dd HH:mm:ss");
       calendar.setTime(nowDate);
       calendar.add(calendar.MONTH, 1);
       Date oneMonthLater=calendar.getTime();
       String oneMonthLaterStr=simpleDateFormat2.format(oneMonthLater);
       calendar.add(calendar.MONTH,1);
       Date twoMonthLater=calendar.getTime();
       String twoMonthLaterStr=simpleDateFormat2.format(twoMonthLater);

       calendar.add(calendar.MONTH,1);
       Date threeMonthLater=calendar.getTime();
       String threeMonthLaterStr=simpleDateFormat2.format(threeMonthLater);



        if (flag==0){
            return contractMapper.selectDueContract(null, nowStr,pcCompanyId);
        }else if (flag==1){
            return  contractMapper.selectDueContract(nowStr,oneMonthLaterStr,pcCompanyId);
        }else if (flag==2){
            return  contractMapper.selectDueContract(oneMonthLaterStr,twoMonthLaterStr,pcCompanyId);
        }else if (flag==3){
            return  contractMapper.selectDueContract(twoMonthLaterStr,threeMonthLaterStr,pcCompanyId);
        }
       return new ArrayList<>();

    }

    public Integer countContractByPcCompanyId(String pcCompanyId, Integer start, Integer end){
        return contractMapper.countContractByPcCompanyId(pcCompanyId, start, end);
    }

    public List<Map<String, Object>> listExpiringContractByPcCompanyId(String pcCompanyId, Integer currentPage, Integer pageSize){
        Integer start = (currentPage - 1) * pageSize;
        List<Map<String, Object>> expiringContracts = contractMapper.listExpiringContractByPcCompanyId(pcCompanyId, start, pageSize);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> contract:expiringContracts){
            Timestamp endDate = (Timestamp) contract.get("endDate");
            contract.put("endDate", format.format(endDate));
        }
        return expiringContracts;
    }


}
