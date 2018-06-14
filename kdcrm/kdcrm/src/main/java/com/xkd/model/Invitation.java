package com.xkd.model;

import java.util.List;

public class Invitation {

	 private String id;  //邀请函名称',
	 private String name ; //邀请函',
	 private String nameDes ; //邀请函描述',
	 private String codeSize ; //二维码尺寸',
	 private String bgMusic ; //背景音乐',
	 private String isTemplet ; //是否是模板',
	 private String shareTitle;
	 private String shareContent;
	 private String shareImgpath;
	 private String createdBy ;
	 private String createDate ;
	 private String updatedBy ;
	 private String updateDate;
	 
	 List<InvitationPages> pages;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameDes() {
		return nameDes;
	}
	public void setNameDes(String nameDes) {
		this.nameDes = nameDes;
	}
	public String getCodeSize() {
		return codeSize;
	}
	public void setCodeSize(String codeSize) {
		this.codeSize = codeSize;
	}
	public String getBgMusic() {
		return bgMusic;
	}
	public void setBgMusic(String bgMusic) {
		this.bgMusic = bgMusic;
	}
	
	public String getIsTemplet() {
		return isTemplet;
	}
	public void setIsTemplet(String isTemplet) {
		this.isTemplet = isTemplet;
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
	public List<InvitationPages> getPages() {
		return pages;
	}
	public void setPages(List<InvitationPages> pages) {
		this.pages = pages;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareName(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	public String getShareContent() {
		return shareContent;
	}
	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}
	public String getShareImgpath() {
		return shareImgpath;
	}
	public void setShareImgpath(String shareImgpath) {
		this.shareImgpath = shareImgpath;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	
	
	 
	 
}
