package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model;

public class PeopleAuthorityManager {
	   private  Integer  id;  //人员授权Id
	   private  String   card_num;  //卡编号
	   private  String   card_type;  //卡类型
	   private  String   action_flag;  //动作类型
	   private  String   object_num; //对象编号（人员编号）
	   private  String   object_type; //对象类型
	   private  String   floor_group_num;  //楼层组编号
	   private  String   floor_list;       //楼层列表
	   private  String   device_num;       //设备编号
	   private  String   device_ip;       //设备IP
	   private  String   device_port;       //设备端口
	   private  String   address;       //设备地址
	   private  String   try_times;       //尝试次数
       private  String   start_time; //开始生效时间
       private  String   end_time;   //结束生效时间
       private  String   create_time;   //结束生效时间
       
       
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getCard_num() {
			return card_num;
		}
		public void setCard_num(String card_num) {
			this.card_num = card_num;
		}
		public String getCard_type() {
			return card_type;
		}
		public void setCard_type(String card_type) {
			this.card_type = card_type;
		}
		
		public String getAction_flag() {
			return action_flag;
		}
		public void setAction_flag(String action_flag) {
			this.action_flag = action_flag;
		}
		public String getObject_num() {
			return object_num;
		}
		public void setObject_num(String object_num) {
			this.object_num = object_num;
		}
		public String getObject_type() {
			return object_type;
		}
		public void setObject_type(String object_type) {
			this.object_type = object_type;
		}
		public String getFloor_group_num() {
			return floor_group_num;
		}
		public void setFloor_group_num(String floor_group_num) {
			this.floor_group_num = floor_group_num;
		}
		public String getFloor_list() {
			return floor_list;
		}
		public void setFloor_list(String floor_list) {
			this.floor_list = floor_list;
		}
		public String getDevice_num() {
			return device_num;
		}
		public void setDevice_num(String device_num) {
			this.device_num = device_num;
		}
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public String getTry_times() {
			return try_times;
		}
		public void setTry_times(String try_times) {
			this.try_times = try_times;
		}
		public String getDevice_ip() {
			return device_ip;
		}
		public void setDevice_ip(String device_ip) {
			this.device_ip = device_ip;
		}
		public String getDevice_port() {
			return device_port;
		}
		public void setDevice_port(String device_port) {
			this.device_port = device_port;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
        
		
        
       
}
