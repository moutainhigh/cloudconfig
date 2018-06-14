package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sun.mail.imap.protocol.ID;
import com.xkd.model.Dictionary;

public interface DictionaryMapper {

	List<Dictionary> selectDictionaryByTtype(@Param("ttype") String ttype,@Param("pcCompanyId")String pcCompanyId);
	
	List<Dictionary> selectDictionaryByValue(@Param("value") String value,@Param("pcCompanyId")String pcCompanyId);



	List<Dictionary> selectDictionaryByParentId(@Param("parentId") String parentId, @Param("ttype") String ttype,@Param("pcCompanyId")String pcCompanyId);

	Dictionary selectDictionaryById(@Param("id") String id);

	List<Dictionary> selectParentDictionaryByTtype(@Param("ttype") String ttype,@Param("pcCompanyId")String pcCompanyId);

	List<String> selectDictionaryParentValueByType(@Param("ttype") String ttype,@Param("pcCompanyId")String pcCompanyId);

	List<Map<String, Object>> selectDictionaryTtypes(@Param("pcCompanyId")String  pcCompanyId);

	List<Dictionary> selectDictionaryByTtypeValue(@Param("ttype") String ttype, @Param("value") String value,@Param("pcCompanyId")String  pcCompanyId);

	List<Map<String, Object>> selectDictionarysByTtypes(@Param("ttypes") String ttypes,
														@Param("pageSize") int pageSizeInt, @Param("currentPage") int currentPageInt,@Param("pcCompanyId")String pcCompanyId);

	Integer saveDictionarys(@Param("id") String id, @Param("ttype") String ttype, @Param("ttypeName") String ttypeName, @Param("value") String value, @Param("level") Integer level,@Param("pcCompanyId")String pcCompanyId);



	Integer updateDictionaryLevel(@Param("id") String id, @Param("level") Integer level);

	Integer clearColumnData(@Param("sql") String sql);

	Integer deleteDictionaryById(@Param("id") String id);

	Integer selectMaxLevelByTtype(@Param("ttype") String ttype,@Param("pcCompanyId")String  pcCompanyId);

}
