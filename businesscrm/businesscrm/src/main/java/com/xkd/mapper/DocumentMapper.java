package com.xkd.mapper;

import com.xkd.model.Document;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentMapper {

	List<Document> selectDocumentsByTtypeId(@Param("ttypeId") String ttypeId);
	
	Integer saveDocument(Document document);

	Integer deleteDocumentById(@Param("id") String id, @Param("createdBy") String createBy);

	Document selectDocumentById(@Param("id") String id);
	
	Integer updateDocumentName(Document document);
	
	
}
