package com.kuangchi.sdd.comm.container;

import java.util.HashMap;
import java.util.Map;

import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.common.PackageDown;
import com.kuangchi.sdd.comm.equipment.gate.actualtime.GetActualTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.actualtime.SetActualTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.clear.ClearDataConnector;
import com.kuangchi.sdd.comm.equipment.gate.deviceGroup.GetDeviceGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.deviceGroup.SetDeviceGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.doubleInOut.GetDoubleInOutConnector;
import com.kuangchi.sdd.comm.equipment.gate.doubleInOut.SetDoubleInOutConnector;
import com.kuangchi.sdd.comm.equipment.gate.fire.SetFireFlagConnector;
import com.kuangchi.sdd.comm.equipment.gate.first.SetFirstConnector;
import com.kuangchi.sdd.comm.equipment.gate.group.GetGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.group.SetGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.holiday.GetHolidayConnector;
import com.kuangchi.sdd.comm.equipment.gate.holiday.SetHolidayConnector;
import com.kuangchi.sdd.comm.equipment.gate.init.InitEquipmentConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.DelLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.GetLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.SetLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordConnector;
import com.kuangchi.sdd.comm.equipment.gate.remoteOpenDoor.SetRemoteOpenDoorConnector;
import com.kuangchi.sdd.comm.equipment.gate.reset.ResetConnector;
import com.kuangchi.sdd.comm.equipment.gate.respondRecord.RespondRecordConnector;
import com.kuangchi.sdd.comm.equipment.gate.restoreDevice.RestoreDeviceConnector;
import com.kuangchi.sdd.comm.equipment.gate.search.SearchEquipmentConnector;
import com.kuangchi.sdd.comm.equipment.gate.software.down.DownConnector;
import com.kuangchi.sdd.comm.equipment.gate.software.over.OverConnector;
import com.kuangchi.sdd.comm.equipment.gate.software.over.OverHandler;
import com.kuangchi.sdd.comm.equipment.gate.software.packageDown.PackageDownConnector;
import com.kuangchi.sdd.comm.equipment.gate.status.GetStatusConnector;
import com.kuangchi.sdd.comm.equipment.gate.time.ReadTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.time.SetTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid.GetTimeGroupForbidConnector;
import com.kuangchi.sdd.comm.equipment.gate.timeGroupForbid.SetTimeGroupForbidConnector;
import com.kuangchi.sdd.comm.equipment.gate.trigger.SetTriggerConnector;
import com.kuangchi.sdd.comm.equipment.gate.userGroup.GetUserGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.userGroup.SetUserGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.workparam.GetWorkParamConnector;
import com.kuangchi.sdd.comm.equipment.gate.workparam.SetWorkParamConnector;

/**
 * 通讯服务器的连接器工厂
 * 
 * @author yu.yao
 * 
 * @param <T extends>
 */
public class ConnectorFactory {
	private static final Map<String, Connector> cache = new HashMap();

	public static synchronized Connector getConnector(String key) {
		Connector connector = null;
		
			if (key.equals("search")) {
				connector = new SearchEquipmentConnector();
			} else if (key.equals("init")) {
				connector = new InitEquipmentConnector();
			} else if (key.equals("get_time")) {
				connector = new ReadTimeConnector();
			} else if (key.equals("set_time")) {
				connector = new SetTimeConnector();
			} else if (key.equals("get_limit")) {
				connector = new GetLimitConnector();
			} else if (key.equals("set_limit")) {
				connector = new SetLimitConnector();
			} else if (key.equals("get_group")) {
				connector = new GetGroupConnector();
			} else if (key.equals("set_group")) {
				connector = new SetGroupConnector();
			} else if (key.equals("get_holiday")) {
				connector = new GetHolidayConnector();
			} else if (key.equals("set_holiday")) {
				connector = new SetHolidayConnector();
			} else if (key.equals("get_record")) {
				connector = new GetRecordConnector();
			} else if (key.equals("set_record")) {
				connector = null;
			} else if (key.equals("reset")) {
				connector = new ResetConnector();
			} else if (key.equals("get_workparam")) {
				connector = new GetWorkParamConnector();
			} else if (key.equals("set_workparam")) {
				connector = new SetWorkParamConnector();
			} else if (key.equals("set_fire")) {
				connector = new SetFireFlagConnector();
			} else if (key.equals("set_trigger")) {
				connector = new SetTriggerConnector();
			} else if (key.equals("set_actualtime")) {
				connector = new SetActualTimeConnector();
			} else if (key.equals("get_actualtime")) {
				connector = new GetActualTimeConnector();
			} else if (key.equals("set_first")) {
				connector = new SetFirstConnector();
			} else if (key.equals("get_status")) {
				connector = new GetStatusConnector();
			}else if(key.equals("del_limit")){
				connector=new DelLimitConnector();
			}else if(key.equals("set_double_in_out")){
				connector=new SetDoubleInOutConnector();
			}else if(key.equals("get_double_in_out")){
				connector=new GetDoubleInOutConnector();
			}else if(key.equals("set_user_group")){
				connector=new SetUserGroupConnector();
			}else if(key.equals("get_user_group")){
				connector=new GetUserGroupConnector();
			}else if(key.equals("respond_record")){
				connector=new RespondRecordConnector();
			}else if(key.equals("set_device_group")){
				connector=new SetDeviceGroupConnector();
			}else if(key.equals("get_device_group")){
				connector=new GetDeviceGroupConnector();
			}else if(key.equals("clear_data")){
				connector=new ClearDataConnector();
			}else if(key.equals("set_remote_open_door")){
				connector=new SetRemoteOpenDoorConnector();
			}else if(key.equals("restore_device")){
				connector=new RestoreDeviceConnector();
			}else if(key.equals("set_state_forbid_time")){
				connector=new SetTimeGroupForbidConnector();
			}else if(key.equals("get_state_forbid_time")){
				connector=new GetTimeGroupForbidConnector();
			}else if(key.equals("down")){
				connector=new DownConnector();
			}else if(key.equals("package_down")){
				connector=new PackageDownConnector();
			}else if(key.equals("over")){
				connector=new OverConnector();
			}
			//cache.put(key, connector);
		
		return connector;
	}
}
