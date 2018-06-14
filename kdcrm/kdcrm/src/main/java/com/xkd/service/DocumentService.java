package com.xkd.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.DocumentMapper;
import com.xkd.model.Document;

@Service
public class DocumentService {

	@Autowired
	private DocumentMapper documentMapper;
	
	
	public List<Document> selectDocumentsByTtypeId(String companyId){
		
		List<Document> documents = documentMapper.selectDocumentsByTtypeId(companyId);
		
		return documents;
		
	} 
	
	
	public Integer saveDocument(Document document){
		document.setId(StringUtils.isNotBlank(document.getId()) ? document.getId() : UUID.randomUUID().toString());
		Integer num = documentMapper.saveDocument(document);
		
		return num;
		
	} 
	
	public Integer deleteDocumentById(String id,String userId){
		
		Integer num = documentMapper.deleteDocumentById(id,userId);
		
		return num;
		
	}


	public Document selectDocumentById(String id) {
		
		Document document = documentMapper.selectDocumentById(id);
		
		return document;
		
	}
	
	public Integer updateDocumentName(Document document){
		
		Integer num = documentMapper.updateDocumentName(document);
		
		return num;
		
	}

}
