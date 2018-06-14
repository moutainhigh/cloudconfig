package com.kuangchi.sdd.comm.equipment.common;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Data;
/**
 * 设备状态对象
 * @author yu.yao
 *
 */
public class GateStatusData extends Data{
	private int param;//请求参数
	private int lock;//0xDD：	锁的状态，	0：关；	1：开。	bit0～bit3表示0号门到3号门。
	private int magnetic;//0xMM：	门磁状态，	0：无；	1：有。	bit0～bit3表示0号门到3号门。
	private int key;//0xAA：	按键状态，	0：无；	1：有。	bit0～bit3表示0号门到3号门。
	private int anti;//0xFF：	防撬状态，	0：无；	1：有。	bit0～bit3表示0号门到3号门。
	private int fire;//0xXX：	消防状态，	0：无；	1：有。
	private int infrared;//0xXX：	红外状态，	0：无；	1：。
	private int deviceAnti;//0xXX：	控制器防撬状态，	0：无；	1：。
	/**
	 *  0xCCCCCC：0门刷卡卡号。先高后低，无刷卡事件时，以0xFFFFFF填充。
	 *  0xEE：	  0门读头事件代码：	设备当前事件。0xFF表示无任何事件。
	 *  0xCCCCCC：1门刷卡卡号。先高后低，无刷卡事件时，以0xFFFFFF填充。
	 *  0xEE：	  1门读头事件代码：	设备当前事件。0xFF表示无任何事件。
	 *  0xCCCCCC：2门刷卡卡号。先高后低，无刷卡事件时，以0xFFFFFF填充。
	 *  0xEE：	  2门读头事件代码：	设备当前事件。0xFF表示无任何事件。
	 *  0xCCCCCC：3门刷卡卡号。先高后低，无刷卡事件时，以0xFFFFFF填充。
	 *  0xEE：	  3门读头事件代码：	设备当前事件。0xFF表示无任何事件。
	 */
	private List<Long> event;
	
	public long getCrcFromSum(){
		int result = 0;
		//计算有效数据的校验值
		if(event != null){
			for(Long e:event){
				int temp = e.intValue();
				if(temp>255){
					result +=(temp & 0xFF000000)>>24;
					result +=(temp & 0xFF0000)>>16;
					result +=(temp & 0x00FF00)>>8;
					result +=(temp & 0x0000FF);
				}else{
					result += temp;
				}
			}
		}
		result += param+lock+magnetic+key+anti+fire;
		return result;
	}
	
	public int getLock() {
		return lock;
	}
	public void setLock(int lock) {
		this.lock = lock;
	}
	public int getMagnetic() {
		return magnetic;
	}
	public void setMagnetic(int magnetic) {
		this.magnetic = magnetic;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getAnti() {
		return anti;
	}
	public void setAnti(int anti) {
		this.anti = anti;
	}
	public int getFire() {
		return fire;
	}
	public void setFire(int fire) {
		this.fire = fire;
	}
	public List<Long> getEvent() {
		return event;
	}
	public void setEvent(List<Long> event) {
		this.event = event;
	}
	public int getParam() {
		return param;
	}
	public void setParam(int param) {
		this.param = param;
	}

	public int getInfrared() {
		return infrared;
	}

	public void setInfrared(int infrared) {
		this.infrared = infrared;
	}

	public int getDeviceAnti() {
		return deviceAnti;
	}

	public void setDeviceAnti(int deviceAnti) {
		this.deviceAnti = deviceAnti;
	}
	
}
