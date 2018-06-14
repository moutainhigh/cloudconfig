package com.xkd.model;

public class Dictionary {

	public static int TTYPE_QUESTION_REDIO = 1;
	
	private String id;
	
	private String ttype;

	private String ttypeName;

	private String value;
	
	private Integer useCount;

	private String parentId;
	
	private Boolean isSelected;

	private  Object object;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

 

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTtype() {
		return ttype;
	}

	public String getTtypeName() {
		return ttypeName;
	}

	public void setTtypeName(String ttypeName) {
		this.ttypeName = ttypeName;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
