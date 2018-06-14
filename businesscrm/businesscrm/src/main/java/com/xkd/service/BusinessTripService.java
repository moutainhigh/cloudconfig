package com.xkd.service;

import com.xkd.mapper.BusinessTripMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/5/4.
 */
@Service
public class BusinessTripService {
    @Autowired
    BusinessTripMapper businessTripMapper;

    @Autowired
    CompanyRelativeUserService companyRelativeUserService;

    public int insertBusinessTrip(Map<String, Object> map) {
        return businessTripMapper.insertBusinessTrip(map);
    }

    public int updateBusinessTrip(Map<String, Object> map) {

        return businessTripMapper.updateBusinessTrip(map);
    }

    public int deleteBusinessTrip(List<String> idList) {
        if (idList.size() == 0) {
            return 0;
        }
        return businessTripMapper.deleteBusinessTrip(idList);
    }

    public List<Map<String, Object>> searchBusinessTrip(String startDate, String endDate, String responsibleUserId, Integer currentPage, Integer pageSize) {
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        Integer start=(currentPage-1)*pageSize;

        List<Map<String, Object>> list = businessTripMapper.searchBusinessTrip(startDate, endDate, responsibleUserId, start, pageSize);

        List<String> companyIds = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            companyIds.add((String) list.get(i).get("companyId"));
        }
        List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIds);
        //整理数据，将相关人员整合到相应的记录中去
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            List<Map<String, Object>> relUserList = new ArrayList<>();
            data.put("relativeUserList", relUserList);
            for (int j = 0; j < relativeList.size(); j++) {
                if (relativeList.get(j).get("companyId").equals(data.get("companyId"))) {
                    relUserList.add(relativeList.get(j));
                }
            }
        }

        return list;
    }

    public int searchBusinessTripCount(String startDate, String endDate, String responsibleUserId) {
        return businessTripMapper.searchBusinessTripCount(startDate, endDate, responsibleUserId);
    }

}
