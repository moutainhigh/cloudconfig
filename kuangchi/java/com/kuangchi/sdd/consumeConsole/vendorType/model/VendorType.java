package com.kuangchi.sdd.consumeConsole.vendorType.model;

public class VendorType {
	private String id;
	private String vendor_type_num;//商户类型编号
	private String vendor_type_name;//商户类型名称
	private String poundage;//手续费
	private String delete_flag;//删除标志
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVendor_type_num() {
		return vendor_type_num;
	}
	public void setVendor_type_num(String vendor_type_num) {
		this.vendor_type_num = vendor_type_num;
	}
	public String getVendor_type_name() {
		return vendor_type_name;
	}
	public void setVendor_type_name(String vendor_type_name) {
		this.vendor_type_name = vendor_type_name;
	}
	public String getPoundage() {
		return poundage;
	}
	public void setPoundage(String poundage) {
		this.poundage = poundage;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	
}
