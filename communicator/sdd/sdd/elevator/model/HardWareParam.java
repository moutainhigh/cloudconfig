package com.kuangchi.sdd.elevator.model;

public class HardWareParam {
		Integer floorRelayTime;  //层控继电器动作时间
		Integer directRelayTime; //直达继电器动作时间
		Integer opositeRelayTime;//对讲继电器动作 时间
		Integer totalFloor;  //楼层总数
		Integer up;    //上
		Integer down;//下 
		Integer open;// 开
		Integer close;//关
		public Integer getFloorRelayTime() {
			return floorRelayTime;
		}
		public void setFloorRelayTime(Integer floorRelayTime) {
			this.floorRelayTime = floorRelayTime;
		}
		public Integer getDirectRelayTime() {
			return directRelayTime;
		}
		public void setDirectRelayTime(Integer directRelayTime) {
			this.directRelayTime = directRelayTime;
		}
		public Integer getOpositeRelayTime() {
			return opositeRelayTime;
		}
		public void setOpositeRelayTime(Integer opositeRelayTime) {
			this.opositeRelayTime = opositeRelayTime;
		}
		public Integer getTotalFloor() {
			return totalFloor;
		}
		public void setTotalFloor(Integer totalFloor) {
			this.totalFloor = totalFloor;
		}
		public Integer getUp() {
			return up;
		}
		public void setUp(Integer up) {
			this.up = up;
		}
		public Integer getDown() {
			return down;
		}
		public void setDown(Integer down) {
			this.down = down;
		}
		public Integer getOpen() {
			return open;
		}
		public void setOpen(Integer open) {
			this.open = open;
		}
		public Integer getClose() {
			return close;
		}
		public void setClose(Integer close) {
			this.close = close;
		}
		
		
}
