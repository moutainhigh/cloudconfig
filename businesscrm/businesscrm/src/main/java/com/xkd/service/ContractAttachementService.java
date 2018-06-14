package com.xkd.service;

import com.xkd.mapper.ContractAttachementMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/27.
 */
@Service
public class ContractAttachementService {
    @Autowired
    ContractAttachementMapper contractAttachementMapper;



    public int insertList(@Param("items")List<Map<String,Object>> mapList){
        if (mapList.size()==0){
            return 0;
        }
        return contractAttachementMapper.insertList(mapList);
    }

    public int deleteByContractId(@Param("contractId")String contractId){
        return contractAttachementMapper.deleteByContractId(contractId);
    }

}
