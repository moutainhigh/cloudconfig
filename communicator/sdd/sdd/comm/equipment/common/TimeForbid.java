package com.kuangchi.sdd.comm.equipment.common;

public class TimeForbid {
	  int startHour;
	  int startMinute;
	  int endHour;
	  int endMinute;
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}
	public int getCrcFromSum() {
		
        return startHour+startMinute+endHour+endMinute;
	}
}
