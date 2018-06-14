package com.xkd.model;

public class Document {

	
	private String id;
	
	private String ttypeId;
	
	private String pagerFileId;
	
	private String path;

	private String fileName;
	
	private Integer status;
	
	private Integer ttype;
	
	private String fileSize;
	
	private String fileByte;
	
	private String ext;
	
	
	private String createdBy;
	
	private String createDate;
	
	private String updatedBy;
	
	private String updateDate;
	
	/*
	 * 用于前端返回
	 */
	private String userName;

	private String documentUpdateTime;
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public String getTtypeId() {
		return ttypeId;
	}

	public void setTtypeId(String ttypeId) {
		this.ttypeId = ttypeId;
	}

	public Integer getTtype() {
		return ttype;
	}

	public void setTtype(Integer ttype) {
		this.ttype = ttype;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDocumentUpdateTime() {
		return documentUpdateTime;
	}

	public void setDocumentUpdateTime(String documentUpdateTime) {
		this.documentUpdateTime = documentUpdateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPagerFileId() {
		return pagerFileId;
	}

	public void setPagerFileId(String pagerFileId) {
		this.pagerFileId = pagerFileId;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getFileByte() {
		return fileByte;
	}

	public void setFileByte(String fileByte) {
		this.fileByte = fileByte;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
