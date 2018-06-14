package com.xkd.model;

import java.io.Serializable;

public class Operate implements Serializable {
	  	private String id;
	    private String menuId;
	    private String menuName;
	    private String url;
	    private String operateName;
	    private String operateCode;
	    private Integer orderNo;
	    private boolean checked=false;
	    private boolean isFromRole=false;
	    private String updateDate;
	    private String updator;
	    private String updatedBy;

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public String getMenuId() {
	        return menuId;
	    }

	    public void setMenuId(String menuId) {
	        this.menuId = menuId;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getOperateName() {
	        return operateName;
	    }

	    public void setOperateName(String operateName) {
	        this.operateName = operateName;
	    }

	    public String getOperateCode() {
	        return operateCode;
	    }

	    public void setOperateCode(String operateCode) {
	        this.operateCode = operateCode;
	    }

		public Integer getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(Integer orderNo) {
			this.orderNo = orderNo;
		}

		public String getMenuName() {
			return menuName;
		}

		public void setMenuName(String menuName) {
			this.menuName = menuName;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public boolean isFromRole() {
			return isFromRole;
		}

		public void setFromRole(boolean isFromRole) {
			this.isFromRole = isFromRole;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

		public String getUpdator() {
			return updator;
		}

		public void setUpdator(String updator) {
			this.updator = updator;
		}

		public String getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}

	   
	    
	    
	    
	    
}
