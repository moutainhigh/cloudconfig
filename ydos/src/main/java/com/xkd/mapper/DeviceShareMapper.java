package com.xkd.mapper;

import com.xkd.model.Label;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/6.
 */
public interface DeviceShareMapper {


    public List<Map<String,Object>> selectShareListBySharer(@Param("userId")String userId);

    public void delete(@Param("idList")List<String> idList);

    public void insertList(@Param("shareList")List<Map<String,Object>> shareList);

    public Map<String,Object> selectById(@Param("id")String id);

    public List<String> selectShareeByCompanyIdAndPcCompanyId(@Param("pcCompanyId")String pcCompanyId,@Param("companyId")String companyId);



    public List<Map<String,Object>>  selectBySharerAndSharee(@Param("sharee")String sharee,@Param("sharer")String sharer);

}
