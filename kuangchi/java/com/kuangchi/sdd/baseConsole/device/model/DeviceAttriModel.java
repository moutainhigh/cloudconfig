package com.kuangchi.sdd.baseConsole.device.model;

public class DeviceAttriModel {

	  private Integer deviceAtrrId; //设备属性Id
	  private String deviceNum;  //设备编号
	  private String deviceMac;
	  private String headerCardFlag;  //首止开门标志
	  private String oneOutControlFlag;  //0、1号门双向进出控制标志
	  private String twoOutControlFlag;  // 2、3号门双向进出控制标志
	  private String oneLockControlFlag; //  0、1号门互锁控制标志
	  private String twoLockControlFlag;  //  2、3号门互锁控制标志
	  private String threeLockControlFlag; //  0、1、2号门互锁控制标志
	  private String fourLockControlFlag;  //0、1、2、3号门互锁控制标志
	  private String delayOpenDoorTime;  //  延迟开门时间
	  private String fireFlag; //  消防联动
	  private String deviceCNum;  // 设备记录数
	  private String keepNum;    // 巡更记录数
	  private String userNum;    //  用户记录数
	  private String deviceRootNum; //  设备权限个数
	  private String keepRootNum;   //  巡更权限个数
	  private String deviceName;    // 设备名称
	  private String deviceType;    // 设备类型
	  
	  
	  
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getDeviceAtrrId() {
		return deviceAtrrId;
	}
	public void setDeviceAtrrId(Integer deviceAtrrId) {
		this.deviceAtrrId = deviceAtrrId;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getHeaderCardFlag() {
		return headerCardFlag;
	}
	public void setHeaderCardFlag(String headerCardFlag) {
		this.headerCardFlag = headerCardFlag;
	}
	public String getOneOutControlFlag() {
		return oneOutControlFlag;
	}
	public void setOneOutControlFlag(String oneOutControlFlag) {
		this.oneOutControlFlag = oneOutControlFlag;
	}
	public String getTwoOutControlFlag() {
		return twoOutControlFlag;
	}
	public void setTwoOutControlFlag(String twoOutControlFlag) {
		this.twoOutControlFlag = twoOutControlFlag;
	}
	public String getOneLockControlFlag() {
		return oneLockControlFlag;
	}
	public void setOneLockControlFlag(String oneLockControlFlag) {
		this.oneLockControlFlag = oneLockControlFlag;
	}
	public String getTwoLockControlFlag() {
		return twoLockControlFlag;
	}
	public void setTwoLockControlFlag(String twoLockControlFlag) {
		this.twoLockControlFlag = twoLockControlFlag;
	}
	public String getThreeLockControlFlag() {
		return threeLockControlFlag;
	}
	public void setThreeLockControlFlag(String threeLockControlFlag) {
		this.threeLockControlFlag = threeLockControlFlag;
	}
	public String getFourLockControlFlag() {
		return fourLockControlFlag;
	}
	public void setFourLockControlFlag(String fourLockControlFlag) {
		this.fourLockControlFlag = fourLockControlFlag;
	}
	public String getDelayOpenDoorTime() {
		return delayOpenDoorTime;
	}
	public void setDelayOpenDoorTime(String delayOpenDoorTime) {
		this.delayOpenDoorTime = delayOpenDoorTime;
	}
	public String getFireFlag() {
		return fireFlag;
	}
	public void setFireFlag(String fireFlag) {
		this.fireFlag = fireFlag;
	}
	public String getDeviceCNum() {
		return deviceCNum;
	}
	public void setDeviceCNum(String deviceCNum) {
		this.deviceCNum = deviceCNum;
	}
	public String getKeepNum() {
		return keepNum;
	}
	public void setKeepNum(String keepNum) {
		this.keepNum = keepNum;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getDeviceRootNum() {
		return deviceRootNum;
	}
	public void setDeviceRootNum(String deviceRootNum) {
		this.deviceRootNum = deviceRootNum;
	}
	public String getKeepRootNum() {
		return keepRootNum;
	}
	public void setKeepRootNum(String keepRootNum) {
		this.keepRootNum = keepRootNum;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	  
	  
}
