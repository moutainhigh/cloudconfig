package com.xkd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepartmentTreeNode {
	String id;//Id
	String parentId; //父部门Id
	String departmentName;//部门名称
	String remark; //备注
	String principalId;//负责人ID
	String principal;
	String userNumber;
	boolean canAddChild;
	boolean canDelete=false;
	boolean checked=false;
	boolean canchoose=true;
	List<DepartmentTreeNode> childrenList=new ArrayList<>(); //子部门
	List<Map<String,Object>> userList=new ArrayList<>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}
	public List<DepartmentTreeNode> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<DepartmentTreeNode> childrenList) {
		this.childrenList = childrenList;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public boolean isCanAddChild() {
		return canAddChild;
	}
	public void setCanAddChild(boolean canAddChild) {
		this.canAddChild = canAddChild;
	}
	public boolean isCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public List<Map<String, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, Object>> userList) {
		this.userList = userList;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isCanchoose() {
		return canchoose;
	}

	public void setCanchoose(boolean canchoose) {
		this.canchoose = canchoose;
	}


}
