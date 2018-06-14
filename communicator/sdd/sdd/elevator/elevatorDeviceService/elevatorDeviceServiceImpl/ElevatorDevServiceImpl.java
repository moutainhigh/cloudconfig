package com.kuangchi.sdd.elevator.elevatorDeviceService.elevatorDeviceServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.elevator.dllInterfaces.TKInterfaceFunctions;
import com.kuangchi.sdd.elevator.elevatorDeviceService.IElevatorDeviceService;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.HardWareParam;
import com.kuangchi.sdd.elevator.model.Result;

@Transactional
@Service("elevatorDevServiceImpl")
public class ElevatorDevServiceImpl implements IElevatorDeviceService {

	@Override
	public List<Device> tk_SearchControls(String ip) {
		List<Device> deviceList = TKInterfaceFunctions.KKTK_SearchControls(ip);
		return deviceList;
	}

	@Override
	public Result tk_UpdateControl(Device device, String value) {
		Result reslut = new Result();

		reslut = TKInterfaceFunctions.KKTK_UpdateControl(device, value);
		return reslut;
	}

	@Override
	public Result tk_SystemInit(Device device) {
		Result reslut = new Result();
		reslut = TKInterfaceFunctions.KKTK_SystemInit(device);
		return reslut;
	}

	@Override
	public Result tk__SendHardwareParam(Device device,
			HardWareParam hardWareParam) {
		Result reslut = new Result();

		reslut = TKInterfaceFunctions.KKTK_SendHardwareParam(device,
				hardWareParam);
		return reslut;
	}

}
