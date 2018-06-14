package com.xkd.mapper;

import com.xkd.model.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictionaryMapper {





	Dictionary selectDictionaryById(@Param("id") String id);



	List<Map<String, Object>> selectDictionaryTtypes(@Param("pcCompanyId")String  pcCompanyId);

	List<Dictionary> selectDictionaryByTtypeValue(@Param("ttype") String ttype, @Param("value") String value,@Param("pcCompanyId")String  pcCompanyId);

	List<Map<String, Object>> selectDictionarysByTtypes(@Param("ttypes") String ttypes,
														@Param("pageSize") int pageSize, @Param("start") int start,@Param("pcCompanyId")String pcCompanyId);

	Integer saveDictionarys(@Param("id") String id, @Param("ttype") String ttype, @Param("ttypeName") String ttypeName, @Param("value") String value, @Param("level") Integer level,@Param("pcCompanyId")String pcCompanyId,@Param("module")String module);

	String selectModuleByTtype(@Param("ttype")String ttype);

	Integer updateDictionaryLevel(@Param("id") String id, @Param("level") Integer level);

	Integer clearColumnData(@Param("sql") String sql);

	Integer deleteDictionaryById(@Param("id") String id);

	Integer selectMaxLevelByTtype(@Param("ttype") String ttype,@Param("pcCompanyId")String  pcCompanyId);


	List<Map<String,Object>> selectAllDictionaryExcludeIndustry(@Param("pcCompanyId")String pcCompanyId);

	List<Map<String,Object>> selectAllDictionary();

}
