package com.kuangchi.sdd.comm.equipment.common;

public class DeviceTimeData {
		int hour;
		int minute;
		int actionType;
		int retain;
		
		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		public int getActionType() {
			return actionType;
		}

		public void setActionType(int actionType) {
			this.actionType = actionType;
		}

		public int getRetain() {
			return retain;
		}

		public void setRetain(int retain) {
			this.retain = retain;
		}

		public int getCrcFromSum() {
			
			return hour+minute+actionType+retain;
		}
}
