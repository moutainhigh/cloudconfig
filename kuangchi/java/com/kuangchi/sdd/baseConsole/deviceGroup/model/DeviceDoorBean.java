package com.kuangchi.sdd.baseConsole.deviceGroup.model;

/*
 * 树状态Bean
 * 
 * ***/
public class DeviceDoorBean {
		private String num;      // 编号
		private String name;     //名称
		private Integer type;    //类型  0 设备组，1 设备  ,2 门
		private String parentNum;   //父级编号
		
		private String ip;//如果是设备 ，保存设备Ip by gengji.yang
		
		private String doorId;//如果是门，保存 door_id  by gengji.yang
		
		private String deviceType; //设备类型   by yuman.gao  2017-03-30
		
		
		public String getDeviceType() {
			return deviceType;
		}
		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}
		public String getDoorId() {
			return doorId;
		}
		public void setDoorId(String doorId) {
			this.doorId = doorId;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getNum() {
			return num;
		}
		public void setNum(String num) {
			this.num = num;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public String getParentNum() {
			return parentNum;
		}
		public void setParentNum(String parentNum) {
			this.parentNum = parentNum;
		}
		
		
}
