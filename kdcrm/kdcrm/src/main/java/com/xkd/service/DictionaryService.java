package com.xkd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.DictionaryMapper;
import com.xkd.model.Dictionary;

@Service
public class DictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	public List<Dictionary> selectDictionaryByTtype(String ttype,String pcCompanyId){
		
		List<Dictionary>  dictionaryList = dictionaryMapper.selectDictionaryByTtype(ttype,pcCompanyId);
		
		return dictionaryList;
		
	}


	
	


	public List<Dictionary> selectDictionaryByParentId(String parentId, String ttype,String pcCompanyId) {
		
		List<Dictionary> dictionaries = dictionaryMapper.selectDictionaryByParentId(parentId,ttype,pcCompanyId);
		
		return dictionaries;
	}

	public Dictionary selectDictionaryById(String id) {
		
		Dictionary dictionary = dictionaryMapper.selectDictionaryById(id);
		
		return dictionary;
	}

	public List<Dictionary> selectParentDictionaryByTtype(String ttype,String pcCompanyId) {
		
		List<Dictionary> dictionaries = dictionaryMapper.selectParentDictionaryByTtype(ttype,pcCompanyId);
		
		return dictionaries;
	}

	public List<String> selectDictionaryParentValueByType(String ttype,String pcCompanyId) {
		
		List<String> dictionaries = dictionaryMapper.selectDictionaryParentValueByType(ttype,pcCompanyId);
		
		return dictionaries;
	}

	public List<Map<String, Object>> selectDictionaryTtypes(String pcCompanyId) {
		
		List<Map<String, Object>> maps = dictionaryMapper.selectDictionaryTtypes(pcCompanyId);
		
		return maps;
	}

	public List<Dictionary> selectDictionaryByTtypeValue(String ttype, String value,String pcCompanyId) {
		
		List<Dictionary> dictionarys = dictionaryMapper.selectDictionaryByTtypeValue(ttype,value,pcCompanyId);
		
		return dictionarys;
	}

	public List<Map<String, Object>> selectDictionarysByTtypes(String ttypes, int pageSizeInt, int currentPageInt,String pcCompanyId) {
		
		List<Map<String, Object>> maps = dictionaryMapper.selectDictionarysByTtypes(ttypes,pageSizeInt,currentPageInt,pcCompanyId);
		
		return maps;
	}

	public Integer saveDictionarys(String id,String ttype, String ttypeName, String value, Integer level,String pcCompanyId) {
		
		Integer num = dictionaryMapper.saveDictionarys(id,ttype,ttypeName,value,level,pcCompanyId);
		
		return num;
	}

	public Integer updateDictionaryLevel(String id, Integer level) {
		
		Integer num = dictionaryMapper.updateDictionaryLevel(id,level);
		
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

	public Integer selectMaxLevelByTtype(String ttype,String pcCompanyId) {
		
		Integer maxLevel = dictionaryMapper.selectMaxLevelByTtype(ttype,pcCompanyId);
		
		return maxLevel;
	}

	public List<Dictionary> getParentAndSonRlation(List<Dictionary> dictionaryParents,List<Dictionary> allDictionary) {

		if(allDictionary == null || dictionaryParents == null || dictionaryParents.size() ==0){
			return null;
		}

		for(Dictionary parentDictionary : dictionaryParents){
			List<Dictionary> dictionaryTemts = new ArrayList<>();
			for(Dictionary dictionary : allDictionary){
				if(parentDictionary.getId().equals(dictionary.getParentId())){
					dictionaryTemts.add(dictionary);
				}
			}

			if(dictionaryTemts.size() > 0){
				parentDictionary.setObject(getParentAndSonRlation(dictionaryTemts,allDictionary));
			}
		}


		return dictionaryParents;
	}


	public void saveBusinessTemplate(Map<String, Object> map) {
		dictionaryMapper.saveBusinessTemplate(map);
	}

	public void editeBusinessTemplate(String parentId, String ttypeName) {
		dictionaryMapper.editeBusinessTemplate(parentId,ttypeName);
	}

	public void deleteBusinessTemplate(String id,String parentId) {
		dictionaryMapper.deleteBusinessTemplate(id,parentId);
	}

	public List<Map<String, Object>> getBusinessTemplate(String pcCompanyId,List<String> depList) {
		return dictionaryMapper.getBusinessTemplate(pcCompanyId,depList);
	}

	public List<Map<String,String>> getBusinessTemplateByParentId(String parentId) {
		return dictionaryMapper.getBusinessTemplateByParentId(parentId);
	}
}
