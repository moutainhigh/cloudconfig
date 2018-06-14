package com.kuangchi.sdd.elevator.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.elevator.dllInterfaces.TKInterfaceFunctions;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.FloorConfigBean;
import com.kuangchi.sdd.elevator.model.FloorOpenTimeZone;
import com.kuangchi.sdd.elevator.model.HardWareParam;
import com.kuangchi.sdd.elevator.model.OpenTimeZone;
import com.kuangchi.sdd.elevator.model.Record;
import com.kuangchi.sdd.elevator.model.Result;
import com.kuangchi.sdd.util.network.ElevatorDeviceUtil;

/**
 * 梯控设备action
 * 
 * @author xuewen.deng
 */
@Controller("elevatorDeviceAction")
public class ElevatorDeviceAction extends BaseActionSupport {

	private static final long serialVersionUID = -2561333382202397836L;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 搜索梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SearchControls() {
		// HttpServletRequest request = getHttpServletRequest();

		// String ip = request.getParameter("ip");

		List<Device> allDeviceList = new ArrayList<Device>();
		List<String> allCommIp = ElevatorDeviceUtil.getAllIp();
		for (String ip : allCommIp) {
			List<Device> deviceList = null;
			try {
				deviceList = TKInterfaceFunctions.KKTK_SearchControls(ip);
				if (deviceList != null) {
					for (Device dev : deviceList) {
						allDeviceList.add(dev);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		printHttpServletResponse(new Gson().toJson(allDeviceList));
	}

	/**
	 * 修改梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public void tk_UpdateControl() {
		HttpServletRequest request = getHttpServletRequest();

		List<String> ipList = ElevatorDeviceUtil.getAllIp();

		String deviceStr = request.getParameter("device");
		String value = request.getParameter("newAddress");
		Result result = null;
		try {
			Device device0 = GsonUtil.toBean(deviceStr, Device.class);// 转换
			Device device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
			result = TKInterfaceFunctions.KKTK_UpdateControl(device, value);// 更新设备

			if (result != null && result.getResultCode() == -13) {// 判断设备是否正在重启
				Thread.sleep(8000);

				List<Device> deviceList = null;
				try {
					// List<String> ipList = ElevatorDeviceUtil.getAllIp();
					int flag1 = 0;
					for (String ip : ipList) {
						deviceList = TKInterfaceFunctions
								.KKTK_SearchControls(ip);

						for (Device dev : deviceList) {
							if (ElevatorDeviceUtil.compareDevParam(device, dev)) {// 比较两个设备的属性（参数）是否相同
								result.setResultCode(0);
								result.setResultTxt("修改梯控设备成功");
								printHttpServletResponse(new Gson()
										.toJson(result));
								flag1 = 1;
								break;
							}
						}
						if (flag1 == 1) {
							break;
						}
					}
					if (flag1 == 0) {
						result.setResultCode(1);
						result.setResultTxt("修改梯控设备失败");
						printHttpServletResponse(new Gson().toJson(result));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {// 修改失败
				result.setResultCode(1);
				result.setResultTxt("修改梯控设备失败");
				printHttpServletResponse(new Gson().toJson(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SystemInit() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		Result result = new Result();
		try {
			result = TKInterfaceFunctions.KKTK_SystemInit(device);
			if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("初始化设备成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("初始化设备失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 发送动作参数（硬件参数）给梯控设备
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendHardwareParam() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		String hardWareParamStr = request.getParameter("hardWareParam");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		HardWareParam hardWareParam = GsonUtil.toBean(hardWareParamStr,
				HardWareParam.class);
		Result reslut = new Result();

		try {
			reslut = TKInterfaceFunctions.KKTK_SendHardwareParam(device,
					hardWareParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(reslut));
	}

	/**
	 * 发送节假日
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendHolidayTable() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		String holidayDateListStr = request.getParameter("holidayDateList");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		List<String> holidayDateList = GsonUtil.toBean(holidayDateListStr,
				new ArrayList<String>().getClass());
		Result result = new Result();

		try {
			result = TKInterfaceFunctions.KKTK_SendHolidayTable(device,
					holidayDateList);
			if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("下发节假日成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("下发节假日失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 发送楼层开放时区
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendFloorOpenTimezone() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		String floorOpenTimeZoneStr = request.getParameter("floorOpenTimeZone");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		FloorOpenTimeZone floorOpenTimeZone = GsonUtil.toBean(
				floorOpenTimeZoneStr, FloorOpenTimeZone.class);
		Result result = new Result();

		try {
			result = TKInterfaceFunctions.KKTK_SendFloorOpenTimezone(device,
					floorOpenTimeZone);
			/*
			 * if (result != null && result.getResultCode() == -4) {
			 * 
			 * } else if (result != null && result.getResultCode() == -13) {
			 * result.setResultCode(0); result.setResultTxt("下发楼层开放时区成功"); }
			 * else
			 */if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("下发楼层开放时区成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("下发楼层开放时区失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 发送楼层配置表
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendCongfigTable() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		String configStr = request.getParameter("config");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		FloorConfigBean configBean = GsonUtil.toBean(configStr,
				FloorConfigBean.class);
		Result result = new Result();

		try {
			result = TKInterfaceFunctions.KKTK_SendCongfigTable(device,
					configBean);
			/*
			 * if (result != null && result.getResultCode() == -4) {
			 * result.setResultCode(1); result.setResultTxt("下发楼层配置表失败"); } else
			 * if (result != null && result.getResultCode() == -13) {
			 * result.setResultCode(0); result.setResultTxt("下发楼层配置表成功"); } else
			 */if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("下发楼层配置表成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("下发楼层配置表失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));

	}

	/**
	 * 发送电梯开放时区
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendElevatorOpenTimezone() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		String openTimeZoneStr = request.getParameter("openTimeZone");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		OpenTimeZone openTimeZone = GsonUtil.toBean(openTimeZoneStr,
				OpenTimeZone.class);
		Result result = new Result();

		try {
			result = TKInterfaceFunctions.KKTK_SendElevatorOpenTimezone(device,
					openTimeZone);
			/*
			 * if (result != null && result.getResultCode() == -4) {
			 * result.setResultCode(1); result.setResultTxt("下发电梯开放时区失败"); }
			 * else if (result != null && result.getResultCode() == -13) {
			 * result.setResultCode(0); result.setResultTxt("下发电梯开放时区成功"); }
			 * else
			 */if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("下发电梯开放时区成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("下发电梯开放时区失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));

	}

	/**
	 * 读取动作参数
	 * 
	 * @author xuewen.deng
	 */
	public void tk_GetHardWareParam() {
		HttpServletRequest request = getHttpServletRequest();
		String dataObject = request.getParameter("device");
		Device device = GsonUtil.toBean(dataObject, Device.class);
		device = ElevatorDeviceUtil.setDeviceDefault(device);// 设置默认值
		HardWareParam param = new HardWareParam();
		try {
			param = TKInterfaceFunctions.KKTK_RecHardWareParam(device);
		} catch (Exception e) {
			e.printStackTrace();
		}

		printHttpServletResponse(new Gson().toJson(param));
	}

	/**
	 * 发送时钟（设备校时）
	 * 
	 * @author xuewen.deng
	 */
	public void tk_SendClock() {
		HttpServletRequest request = getHttpServletRequest();
		String dataObject = request.getParameter("device");
		Device device = GsonUtil.toBean(dataObject, Device.class);
		device = ElevatorDeviceUtil.setDeviceDefault(device);// 设置默认值
		Result result = new Result();
		try {
			result = TKInterfaceFunctions.KKTK_SendClock(device);
			if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
				result.setResultTxt("发送时钟成功");
			} else {
				result.setResultCode(1);
				result.setResultTxt("发送时钟失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 接收时钟
	 * 
	 * @author xuewen.deng
	 */
	public void tk_RecvClock() {
		HttpServletRequest request = getHttpServletRequest();
		String dataObject = request.getParameter("device");
		Device device = GsonUtil.toBean(dataObject, Device.class);
		device = ElevatorDeviceUtil.setDeviceDefault(device);// 设置默认值
		Result result = new Result();
		try {
			result = TKInterfaceFunctions.KKTK_RecvClock(device);
			if (result != null && result.getResultCode() == 0) {
				result.setResultCode(0);
			} else if (result == null || result.getResultCode() != 0) {
				result.setResultCode(1);
				result.setResultTxt("接收时钟失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 获取通讯服务器（即本机）所有网卡的IP
	 * 
	 * @author xuewen.deng
	 */
	public void getCommServerIp() {
		printHttpServletResponse(new Gson().toJson(ElevatorDeviceUtil
				.getAllIp()));
	}

	/**
	 * 读取配置楼层表（未对）
	 * 
	 * @author xuewen.deng
	 */
	public void tk_RecvConfigTable() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		FloorConfigBean floorConfig = new FloorConfigBean();
		try {
			floorConfig = TKInterfaceFunctions.KKTK_RecvConfigTable(device, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(floorConfig));
	}

	/**
	 * 接收记录
	 * 
	 * @author xuewen.deng
	 */
	public void tk_RecvEvent() {
		HttpServletRequest request = getHttpServletRequest();
		String deviceStr = request.getParameter("device");
		Device device0 = GsonUtil.toBean(deviceStr, Device.class);
		Device device = new Device();
		device = ElevatorDeviceUtil.setDeviceDefault(device0);// 设置默认值
		Record record = new Record();
		List<Record> recordList = new ArrayList<Record>();
		try {
			while (record != null) {
				record = TKInterfaceFunctions.KKTK_RecvEvent(device);
				if (record != null) {
					recordList.add(record);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(recordList));
	}
	
	/**
	 * 读取控制器版本
	 * @author minting.he
	 */
	public void tk_RecvVersion(){
		HttpServletRequest request = getHttpServletRequest();
		String dataObject = request.getParameter("device");
		Device device = GsonUtil.toBean(dataObject, Device.class);
		Result result = null;
		try {
			result = TKInterfaceFunctions.KKTK_RecvVer(device);
			if (result==null || result.getResultCode()!=0) {
				result.setResultCode(1);
			}else if (result.getResultCode()==0) {
				result.setResultCode(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
}
