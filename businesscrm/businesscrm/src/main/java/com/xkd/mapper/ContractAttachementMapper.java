package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/27.
 */
public interface ContractAttachementMapper {

    public int insertList(@Param("items")List<Map<String,Object>> mapList);

    public int deleteByContractId(@Param("contractId")String contractId);
}
