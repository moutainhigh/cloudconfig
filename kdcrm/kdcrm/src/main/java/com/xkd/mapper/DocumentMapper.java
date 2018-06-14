package com.xkd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Document;

public interface DocumentMapper {

	List<Document> selectDocumentsByTtypeId(@Param("ttypeId") String ttypeId);
	
	Integer saveDocument(Document document);

	Integer deleteDocumentById(@Param("id") String id, @Param("createdBy") String createBy);

	Document selectDocumentById(@Param("id") String id);
	
	Integer updateDocumentName(Document document);
	
	
}
