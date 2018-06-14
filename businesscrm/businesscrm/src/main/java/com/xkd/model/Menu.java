package com.xkd.model;

import java.util.Date;

public class Menu {

	private String id;
	
	private String parentMenuId;

	private String menuId;

	private String menuName;
	
	//菜单内部排序
	private Integer menuLevel;
	
	//菜单等级，一级菜单、二级菜单、三级菜单
	private Integer menuGrade;
	
	private String path;

	private String content;

	private String createdBy;
	
	private String updatedBy;
	
	private String createByName;
	
	private String updateByName;

	private String parentChain;

	private Date updateDate;

	private Date createDate;
	
	private String createDateStr;

	private String updateDateStr;

	private Object childMenu;

	private Integer status;
	
	private String updateTimeStr;
	
	private String routerName;
	
	private String routerPath;
	
	private String componentsPath;
	
	private String iconNormalPath;
	
	private String iconCheckPath;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public Object getChildMenu() {
		return childMenu;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getUpdateDateStr() {
		return updateDateStr;
	}

	public void setUpdateDateStr(String updateDateStr) {
		this.updateDateStr = updateDateStr;
	}

	public void setChildMenu(Object childMenu) {
		this.childMenu = childMenu;
	}

	public String getMenuId() {
		return menuId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public String getCreateBy() {
		return createdBy;
	}

	public void setCreateBy(String createBy) {
		this.createdBy = createBy;
	}

	public String getUpdateBy() {
		return updatedBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updatedBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getParentChain() {
		return parentChain;
	}

	public void setParentChain(String parentChain) {
		this.parentChain = parentChain;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getMenuGrade() {
		return menuGrade;
	}

	public void setMenuGrade(Integer menuGrade) {
		this.menuGrade = menuGrade;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContent() {
		return content;
	}

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public String getRouterPath() {
		return routerPath;
	}

	public void setRouterPath(String routerPath) {
		this.routerPath = routerPath;
	}

	public String getComponentsPath() {
		return componentsPath;
	}

	public void setComponentsPath(String componentsPath) {
		this.componentsPath = componentsPath;
	}

	public String getIconNormalPath() {
		return iconNormalPath;
	}

	public void setIconNormalPath(String iconNormalPath) {
		this.iconNormalPath = iconNormalPath;
	}

	public String getIconCheckPath() {
		return iconCheckPath;
	}

	public void setIconCheckPath(String iconCheckPath) {
		this.iconCheckPath = iconCheckPath;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
