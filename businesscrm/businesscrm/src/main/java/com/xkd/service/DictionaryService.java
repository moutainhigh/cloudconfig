package com.xkd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.DictionaryMapper;
import com.xkd.model.Dictionary;

@Service
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;


    public Dictionary selectDictionaryById(String id) {

        Dictionary dictionary = dictionaryMapper.selectDictionaryById(id);

        return dictionary;
    }


    public List<Map<String, Object>> selectDictionaryTtypes(String pcCompanyId) {

        List<Map<String, Object>> maps = dictionaryMapper.selectDictionaryTtypes(pcCompanyId);

        return maps;
    }

    public List<Dictionary> selectDictionaryByTtypeValue(String ttype, String value, String pcCompanyId) {

        List<Dictionary> dictionarys = dictionaryMapper.selectDictionaryByTtypeValue(ttype, value, pcCompanyId);

        return dictionarys;
    }

    public List<Map<String, Object>> selectDictionarysByTtypes(String ttypes, int pageSize, int start, String pcCompanyId) {

        List<Map<String, Object>> maps = dictionaryMapper.selectDictionarysByTtypes(ttypes, pageSize, start, pcCompanyId);




        return maps;
    }









    public Integer saveDictionarys(String id, String ttype, String ttypeName, String value, Integer level, String pcCompanyId, String module) {

        Integer num = dictionaryMapper.saveDictionarys(id, ttype, ttypeName, value, level, pcCompanyId, module);

        return num;
    }

    public Integer updateDictionaryLevel(String id, Integer level) {

        Integer num = dictionaryMapper.updateDictionaryLevel(id, level);

        return num;
    }

    public Integer clearColumnData(String sql) {

        Integer num = dictionaryMapper.clearColumnData(sql);

        return num;
    }

    public Integer deleteDictionaryById(String id) {

        Integer num = dictionaryMapper.deleteDictionaryById(id);

        return num;
    }

    public Integer selectMaxLevelByTtype(String ttype, String pcCompanyId) {

        Integer maxLevel = dictionaryMapper.selectMaxLevelByTtype(ttype, pcCompanyId);

        return maxLevel;
    }

    public String selectModuleByTtype(String ttype) {
        return dictionaryMapper.selectModuleByTtype(ttype);
    }

    public List<Map<String, Object>> selectAllDictionaryExcludeIndustry(String pcCompanyId) {
        return dictionaryMapper.selectAllDictionaryExcludeIndustry(pcCompanyId);
    }
    public List<Map<String,Object>> selectAllDictionary(){
        return dictionaryMapper.selectAllDictionary();
    }

}
