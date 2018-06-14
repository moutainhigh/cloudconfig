package com.kuangchi.sdd.consumeConsole.fundPool.model;

import java.util.ArrayList;
import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.department.model.Department;

public class JsonTreeResult {
	private Tree tree;//部门树
	private List<Department> list = new ArrayList<Department>(); //其他已绑定的部门子节点
	private List<Department> myBandList = new ArrayList<Department>();//自己绑定的部门子节点
	
	

	public List<Department> getMyBandList() {
		return myBandList;
	}
	public void setMyBandList(List<Department> myBandList) {
		this.myBandList = myBandList;
	}
	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public List<Department> getList() {
		return list;
	}
	public void setList(List<Department> list) {
		this.list = list;
	}
}
