package com.xkd.service;

import com.xkd.mapper.RivalMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/28.
 */
@Service
public class RivalService {
    @Autowired
    RivalMapper rivalMapper;

    public int insertRival(Map<String, Object> map) {
        return rivalMapper.insertRival(map);
    }

    public int updateRival(Map<String, Object> map) {
        return rivalMapper.updateRival(map);
    }

    public Map<String,Object> selectRivalByName(String rivalName,String pcCompanyId){
        return rivalMapper.selectRivalByName(rivalName,pcCompanyId);
    }
    public int deleteByIds(List<String > idList){
        if (idList.size()==0){
            return  0;
        }
        return rivalMapper.deleteByIds(idList);
    }



    public List<Map<String,Object>> searchRival(List<String> departmentIdList,String searchValue,Integer currentPage,Integer pageSize){
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        int start=(currentPage-1)*pageSize;
        return rivalMapper.searchRival(departmentIdList,searchValue,start,pageSize);
    }

    public int searchRivalCount(@Param("departmentIdList")List<String> departmentIdList,@Param("searchValue")String searchValue){
        return rivalMapper.searchRivalCount(departmentIdList,searchValue);
    }



    public Map<String,Object> selectRivalById(String id){
        return rivalMapper.selectRivalById(id);
    }

}
