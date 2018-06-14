package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

/**
 * 发送数据包中的有效数据包装类
 * @author yu.yao
 *
 */
public class SendData extends Data{
	private EquipmentData equipmentData;//设备参数
	private TimeData timeData;//时间
	private GateParamData gateParamData;//门参数
	private GateLimitData gateLimitData;//装载门禁权限信息
	private GateRecordData gateRecordData;//装载门禁指针记录信息
	private GateStatusData gateStatusData;//装载门禁设备状态信息
	private TimeBlock timeBlock;//装载时段组信息
	private UserTimeBlock userTimeBlock;
	private RespondRecord respondRecord;
	private DeviceTimeBlock deviceTimeBlock;
	private ClearData clearData;
	private RemoteOpenDoor doorData;
	private TimeGroupForbid timeGroupForbid;
    private Down down;
    private PackageDown packageDown;
    private Over over;

	public GateRecordData getGateRecordData() {
		return gateRecordData;
	}
	
	public void setGateRecordData(GateRecordData gateRecordData) {
		this.gateRecordData = gateRecordData;
	}

	public  GateParamData getGateParamData() {
		return gateParamData;
	}

	public void setGateParamData(GateParamData gateParamData) {
		this.gateParamData = gateParamData;
	}

	public TimeData getTimeData() {
		return timeData;
	}

	public void setTimeData(TimeData timeData) {
		this.timeData = timeData;
	}

	public EquipmentData getEquipmentData() {
		return equipmentData;
	}

	public void setEquipmentData(EquipmentData equipmentData) {
		this.equipmentData = equipmentData;
	}

	public GateLimitData getGateLimitData() {
		return gateLimitData;
	}

	public void setGateLimitData(GateLimitData gateLimitData) {
		this.gateLimitData = gateLimitData;
	}

	public TimeBlock getTimeBlock() {
		return timeBlock;
	}

	public void setTimeBlock(TimeBlock timeBlock) {
		this.timeBlock = timeBlock;
	}

	public GateStatusData getGateStatusData() {
		return gateStatusData;
	}

	public void setGateStatusData(GateStatusData gateStatusData) {
		this.gateStatusData = gateStatusData;
	}

	public UserTimeBlock getUserTimeBlock() {
		return userTimeBlock;
	}

	public void setUserTimeBlock(UserTimeBlock userTimeBlock) {
		this.userTimeBlock = userTimeBlock;
	}

	public RespondRecord getRespondRecord() {
		return respondRecord;
	}

	public void setRespondRecord(RespondRecord respondRecord) {
		this.respondRecord = respondRecord;
	}

	public DeviceTimeBlock getDeviceTimeBlock() {
		return deviceTimeBlock;
	}

	public void setDeviceTimeBlock(DeviceTimeBlock deviceTimeBlock) {
		this.deviceTimeBlock = deviceTimeBlock;
	}

	public ClearData getClearData() {
		return clearData;
	}

	public void setClearData(ClearData clearData) {
		this.clearData = clearData;
	}

	public RemoteOpenDoor getDoorData() {
		return doorData;
	}

	public void setDoorData(RemoteOpenDoor doorData) {
		this.doorData = doorData;
	}

	public TimeGroupForbid getTimeGroupForbid() {
		return timeGroupForbid;
	}

	public void setTimeGroupForbid(TimeGroupForbid timeGroupForbid) {
		this.timeGroupForbid = timeGroupForbid;
	}

	public Down getDown() {
		return down;
	}

	public void setDown(Down down) {
		this.down = down;
	}

	public PackageDown getPackageDown() {
		return packageDown;
	}

	public void setPackageDown(PackageDown packageDown) {
		this.packageDown = packageDown;
	}

	public Over getOver() {
		return over;
	}

	public void setOver(Over over) {
		this.over = over;
	}

	
	
}
