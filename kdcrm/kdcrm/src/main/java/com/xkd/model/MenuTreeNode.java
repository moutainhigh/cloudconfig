package com.xkd.model;

import java.util.ArrayList;
import java.util.List;

public class MenuTreeNode implements Comparable<MenuTreeNode> {
    String path;
    String iconNormalPath;
    String routerName;
    List<MenuTreeNode> children=new ArrayList<>();
    String menuId;
    String parentMenuId;
    String id;
    String title;
    String routerPath;
    String componentsPath;
    String iconCheckPath;
    Integer menuLevel;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIconNormalPath() {
		return iconNormalPath;
	}
	public void setIconNormalPath(String iconNormalPath) {
		this.iconNormalPath = iconNormalPath;
	}
	public String getRouterName() {
		return routerName;
	}
	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}
	public List<MenuTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<MenuTreeNode> children) {
		this.children = children;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
 
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getIconCheckPath() {
		return iconCheckPath;
	}
	public void setIconCheckPath(String iconCheckPath) {
		this.iconCheckPath = iconCheckPath;
	}
	
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	@Override
	public int compareTo(MenuTreeNode o) {
		if (menuLevel==null) {
			menuLevel=0;
		} 
		
		if (o.getMenuLevel()==null) {
			o.setMenuLevel(0);
		}
		return menuLevel-o.getMenuLevel();
	}
    
    
}
