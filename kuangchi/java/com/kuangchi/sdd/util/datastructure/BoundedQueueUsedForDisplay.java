package com.kuangchi.sdd.util.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class BoundedQueueUsedForDisplay implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	int size=0;
	private LinkedList<Object> list=new LinkedList<Object>();
	
	public LinkedList<Object> getList() {
		return list;
	}
	public void setList(LinkedList<Object> list) {
		this.list = list;
	}
	
	
	public BoundedQueueUsedForDisplay(int size) {
		super();
		this.size = size;
	}
	public  synchronized void  offer(Object object){
		if(list.size()>=size){
			list.poll();
		} 
		list.offer(object);
	}
	
	public  ArrayList<Object>  list(){
		 ArrayList<Object> arrayList=new ArrayList<Object>();
		 for (int i = list.size()-1; i >= 0; i--) {
			arrayList.add(list.get(i));
		}
		 return arrayList;
	}
	

}
