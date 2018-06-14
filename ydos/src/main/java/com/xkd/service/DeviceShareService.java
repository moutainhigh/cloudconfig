package com.xkd.service;

import com.xkd.mapper.DeviceShareMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/6.
 */
@Service
public class DeviceShareService {
    @Autowired
    ApiCallFacadeService apiCallFacadeService;
    @Autowired
    DeviceShareMapper deviceShareMapper;
    public List<Map<String,Object>> selectShareListBySharer( String userId){
        return deviceShareMapper.selectShareListBySharer(userId);
    }

    public void delete( List<String> idList)  {
        if (idList.size()>0){
            for (int i = 0; i <idList.size() ; i++) {
               Map<String,Object> map= deviceShareMapper.selectById(idList.get(i));
                apiCallFacadeService.deleteDeviceWarningWhenUnShareDevice((String)map.get("sharer"),(String)map.get("sharee"));
            }


            deviceShareMapper.delete(idList);
        }
    }

    public void insertList(List<Map<String,Object>> mapList)   {
        if (mapList.size()>0) {
            deviceShareMapper.insertList(mapList);
            for (int i = 0; i <mapList.size() ; i++) {
                String sharer= (String) mapList.get(i).get("sharer");
                String sharee= (String) mapList.get(i).get("sharee");
                Integer control= (Integer) mapList.get(i).get("control");
                Integer warningLevel= (Integer) mapList.get(i).get("warningLevel");

                apiCallFacadeService.addDeviceWarningWhenShareDevice( sharer,sharee,control,warningLevel);
            }

        }
    }


    public List<String> selectShareeByCompanyIdAndPcCompanyId(String pcCompanyId,String companyId){
        return deviceShareMapper.selectShareeByCompanyIdAndPcCompanyId(pcCompanyId,companyId);
    }


    public List<Map<String,Object>>  selectBySharerAndSharee(String sharee,String sharer){
        return deviceShareMapper.selectBySharerAndSharee(sharee,sharer);
    }



}
