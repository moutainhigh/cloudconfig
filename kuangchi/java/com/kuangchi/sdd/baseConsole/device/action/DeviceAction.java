package com.kuangchi.sdd.baseConsole.device.action;

import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.doorinfo.service.IDoorInfoService;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.consumeRecord.service.IConsumeRecordService;
import com.kuangchi.sdd.util.algorithm.ByteToHexUtil;
import com.kuangchi.sdd.util.algorithm.MD5;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EasyUiTable;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.file.DownloadFile;

@Scope("prototype")
@Controller("deviceAction")
public class DeviceAction extends BaseActionSupport {

	private DeviceInfo model;

	public static final Logger LOG = Logger.getLogger(DeviceAction.class);

	@Override
	public Object getModel() {
		return model;
	}

	public DeviceAction() {
		model = new DeviceInfo();
	}

	private File uploadDeviceVersionFile;

	public File getUploadDeviceVersionFile() {
		return uploadDeviceVersionFile;
	}

	public void setUploadDeviceVersionFile(File uploadDeviceVersionFile) {
		this.uploadDeviceVersionFile = uploadDeviceVersionFile;
	}

	@Resource(name = "deviceService")
	DeviceService deviceService;

	@Resource(name = "DoorInfoServiceImpl")
	private IDoorInfoService doorInfoService;

	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	@Resource(name = "ConsumeRecordServiceImpl")
	private IConsumeRecordService consumeRecordService;

	/**
	 * 进入设备监控主页面
	 * 
	 * @author minting.he
	 * @return
	 */
	public String toDeviceListener() {
		return "success";
	}

	/**
	 * 显示设备信息页面
	 * 
	 * @return
	 */
	public String toDeviceInfo() {
		return "success";
	}

	/**
	 * 显示设备名称
	 * 
	 * @author minting.he
	 */
	public void deviceName() {
		HttpServletRequest request = getHttpServletRequest();
		String num = request.getParameter("num");
		Grid<DeviceStateModel> deviceStateGrid = deviceService
				.searchDeviceState(null, num, 0, 1000);
		String StateJson = GsonUtil.toJson(deviceStateGrid);
		printHttpServletResponse(StateJson);
	}

	/**
	 * 设备号精确查询名称
	 * 
	 * @author minting.he
	 */
	public void exactDeviceName() {
		HttpServletRequest request = getHttpServletRequest();
		String num = request.getParameter("num");
		String flag = request.getParameter("flag");
		String[] str = num.split(",");
		Grid<DeviceStateModel> deviceNameGrid = null;
		List list = new ArrayList();
		if ("1".equals(flag)) { // 1：设备，0：设备组
			deviceNameGrid = deviceService.exactDeviceName(str[0], 0, 10000); // 查询设备
			list.add(GsonUtil.toJson(deviceNameGrid));
		} else {
			for (int i = 0; i < str.length; i++) {
				deviceNameGrid = deviceService.searchDeviceByGroup(str[i], 0,
						10000); // 查询设备组下的设备
				list.add(GsonUtil.toJson(deviceNameGrid));
			}
		}
		printHttpServletResponse(list.toString());
	}

	/**
	 * 根据设备号取设备状态
	 * 
	 * @author minting.he
	 */
	public void getState() {
		HttpServletRequest request = getHttpServletRequest();
		String door_num = request.getParameter("door_num");
		String device_num = request.getParameter("device_num");
		DeviceStateModel state = deviceService.getDeviceStateByNum(device_num,
				door_num);
		printHttpServletResponse(GsonUtil.toJson(state));
	}

	/**
	 * 查询设备信息
	 */
	public void getDeviceInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String device_name = request.getParameter("device_name");
		String device_mac = request.getParameter("device_mac");
		int size = Integer.parseInt(request.getParameter("rows"));
		int page = Integer.parseInt(request.getParameter("page"));
		page = (page - 1) * size;
		EasyUiTable easyUiTable = new EasyUiTable();
		try {
			easyUiTable.setTotal(deviceService.getDeviceInfoCount(device_name,
					device_mac));
			List<DeviceInfo> list = deviceService.getDeviceInfo(device_name,
					device_mac, page, size);
			easyUiTable.setRows(list);
			printHttpServletResponse(new Gson().toJson(easyUiTable));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 相应前台请求
	 */
	public String addDeviceInfoToPage() {

		return "success";
	}

	/**
	 * 相应前台请求
	 */
	public String editDeviceInfoToPage() {

		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		DeviceInfo deviceinfo = new DeviceInfo();
		deviceinfo = deviceService.getDeviceInfoByNum(device_num);
		request.setAttribute("deviceinfo", deviceinfo);
		return "success";
	}

	/**
	 * 相应前台请求
	 */
	public String searchDeviceInfoToPage() {

		return "success";
	}

	/**
	 * 修改设备信息
	 */
	public void modifyDeviceInfo() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		DeviceInfo deviceinfo = new DeviceInfo();
		JsonResult result = new JsonResult();
		try {
			BeanUtils.copyProperties(model, deviceinfo);
			result = deviceService.modifyDeviceInfo(deviceinfo, admin_id);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 删除设备信息
	 */
	public void deleteDeviceInfo() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		try {
			result = deviceService.deleteDeviceInfo(device_num, admin_id);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 初始化设备信息
	 */
	public void initializeDeviceInfo() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String device_mac = request.getParameter("device_mac");
		String device_type = request.getParameter("device_type");
		JsonResult result = new JsonResult();
		try {
			result = deviceService.initializeDeviceInfo(device_num, device_mac,
					device_type, admin_id);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 搜索设备信息
	 */
	public void searchDeviceInfo() {
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String admin_id = user.getYhMc();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer count = 0;

		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Gson g = new Gson();
		List<Map> commList = g.fromJson(data,
				new ArrayList<LinkedTreeMap>().getClass());

		try {

			for (Map map2 : commList) {
				String doorType = map2.get("deviceType").toString();
				String beginIp = map2.get("beginIp").toString();
				String endIp = map2.get("endIp").toString();

				String commIp = map2.get("ipAddress").toString();
				String commPort = map2.get("commPort").toString();
				String id = map2.get("id").toString();
				String commId = id.substring(0, id.length() - 2);

				if (("...").equals(beginIp) && ("...").equals(endIp)) {
					InetAddress addr = null;
					try {
						addr = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}

					byte[] ipAddr = addr.getAddress();
					String ipAddrStr = "";
					for (int i = 0; i < ipAddr.length; i++) {
						if (i > 0) {
							ipAddrStr += ".";
						}
						ipAddrStr += ipAddr[i] & 0xFF;
					}
					String[] server_ip = ipAddrStr.split("\\.");
					beginIp = server_ip[0] + "." + server_ip[1] + "."
							+ server_ip[2] + "." + "0";
					endIp = server_ip[0] + "." + server_ip[1] + "."
							+ server_ip[2] + "." + "255";
				}

				LOG.info("request:" + "http://" + commIp + ":" + commPort
						+ "/comm/" + "search/searchEquipment.do?" + "doorType="
						+ doorType + "&beginIp=" + beginIp + "&endIp=" + endIp);
				String str = HttpRequest.sendPost("http://" + commIp + ":"
						+ commPort + "/comm/" + "search/searchEquipment.do?",
						"doorType=" + doorType + "&beginIp=" + beginIp
								+ "&endIp=" + endIp);
				LOG.info("返回信息：" + str);
				ResultMsg msg = new ResultMsg();
				Gson gson = new Gson();
				java.lang.reflect.Type type = new TypeToken<ResultMsg>() {
				}.getType();
				msg = gson.fromJson(str, type);
				if (msg != null) {
					List<EquipmentBean> equipment = msg.getListEquipment();
					for (int i = 0; i < equipment.size(); i++) {
						UUID uuid = UUID.randomUUID();
						equipment.get(i).setCreateuser(admin_id);
						equipment.get(i).setDevicenum(String.valueOf(uuid));
						equipment.get(i).setDevicetype(doorType);
						equipment.get(i).setDeviceName(
								equipment.get(i).getMac());
						equipment.get(i).setCommId(commId);

						if (equipment.get(i).getMac().length() == 1) {
							equipment.get(i).setMac(
									"00000"
											+ equipment.get(i).getMac()
													.toUpperCase());
						} else if (equipment.get(i).getMac().length() == 2) {
							equipment.get(i).setMac(
									"0000"
											+ equipment.get(i).getMac()
													.toUpperCase());
						} else if (equipment.get(i).getMac().length() == 3) {
							equipment.get(i).setMac(
									"000"
											+ equipment.get(i).getMac()
													.toUpperCase());
						} else if (equipment.get(i).getMac().length() == 4) {
							equipment.get(i).setMac(
									"00"
											+ equipment.get(i).getMac()
													.toUpperCase());
						} else if (equipment.get(i).getMac().length() == 5) {
							equipment.get(i).setMac(
									"0"
											+ equipment.get(i).getMac()
													.toUpperCase());
						} else {
							equipment.get(i).setMac(
									equipment.get(i).getMac().toUpperCase());
						}

						// 重置设备的地图信息
						deviceService.resetPosi(equipment.get(i).getMac());

						// 添加门
						try {
							count = deviceService.addDeviceInfo(equipment
									.get(i)) + count;
							// 根据mac查询设备表中设备编号
							String devNum = deviceService
									.getDoorByDeviNum(equipment.get(i).getMac());
							// 根据设备编号查询门信息表中的设备编号
							String devdoorNum = deviceService
									.getDeviceByDeviNum(devNum);
							String deldoorNum = deviceService
									.getDeleteDoorByDeviNum(devNum); // 判断门信息表中有没有该设备被删除
							if (devdoorNum == null) {
								if (deldoorNum != null) {
									deviceService.updateDeleteDoorByNum(devNum);
								} else {
									if (equipment.get(i).getDevicetype()
											.equals("4")) {
										LOG.info(equipment.get(i)
												.getDevicetype());
										for (int j = 1; j < 5; j++) {
											Map<String, Object> map = new HashMap<String, Object>();
											map.put("device_num", devNum);
											map.put("validity_flag", "0");
											map.put("door_name", j);
											map.put("create_user", admin_id);
											map.put("door_num", j);
											map.put("open_delay", 5);
											map.put("open_overtime", 5);
											map.put("use_super_password", 0);
											map.put("super_password", 0);
											map.put("use_force_password", 0);
											map.put("force_password", 0);
											map.put("relock_password", 0);
											map.put("unlock_password", 0);
											map.put("police_password", 0);
											map.put("open_pattern", 0);
											map.put("check_open_pattern", 0);
											map.put("work_pattern", 0);
											map.put("multi_open_number", 0);
											map.put("multi_open_card_num", null);
											// 新增门信息之前调用接口
											/*
											 * Map<String, String> urlmap =
											 * PropertiesToMap .propertyToMap(
											 * "comm_interface.properties");
											 */
											// （1）调用设置门工作参数接口
											String setGateWorkParamUrl = mjCommService
													.getMjCommUrl(devNum)
													+ "gateWorkParam/setGateWorkParam.do";
											String setGateWorkParamStr = HttpRequest
													.sendPost(
															setGateWorkParamUrl,
															"mac="
																	+ equipment
																			.get(i)
																			.getMac()
																	+ "&gateId="
																	+ j
																	+ "&open_delay=5&open_overtime=5"
																	+ "&device_type="
																	+ equipment
																			.get(i)
																			.getDevicetype()
																	+ "&use_super_password=0"
																	+ "&super_password=0&use_force_password=0"
																	+ "&force_password=0&relock_password=0"
																	+ "&unlock_password=0&police_password=0"
																	+ "&open_pattern=0&check_open_pattern=0"
																	+ "&work_pattern=0&multi_open_number=0"
																	+ "&multi_open_card_num=");
											Map setParamResult = GsonUtil
													.toBean(setGateWorkParamStr,
															HashMap.class);

											if ("0".equals(setParamResult
													.get("result_code"))) {
												deviceService.addDoorinfo(map);
											}
										}
									} else if (equipment.get(i).getDevicetype()
											.equals("2")) {
										LOG.info(equipment.get(i)
												.getDevicetype());
										for (int j = 1; j < 3; j++) {
											Map<String, Object> map = new HashMap<String, Object>();
											map.put("device_num", devNum);
											map.put("validity_flag", "0");
											map.put("door_name", j);
											map.put("create_user", admin_id);
											map.put("door_num", j);
											map.put("open_delay", 5);
											map.put("open_overtime", 5);
											map.put("use_super_password", 0);
											map.put("super_password", 0);
											map.put("use_force_password", 0);
											map.put("force_password", 0);
											map.put("relock_password", 0);
											map.put("unlock_password", 0);
											map.put("police_password", 0);
											map.put("open_pattern", 0);
											map.put("check_open_pattern", 0);
											map.put("work_pattern", 0);
											map.put("multi_open_number", 0);
											map.put("multi_open_card_num", null);
											// 新增门信息之前调用接口
											/*
											 * Map<String, String> urlmap =
											 * PropertiesToMap .propertyToMap(
											 * "comm_interface.properties");
											 */
											// （1）调用设置门工作参数接口
											String setGateWorkParamUrl = mjCommService
													.getMjCommUrl(devNum)
													+ "gateWorkParam/setGateWorkParam.do";
											String setGateWorkParamStr = HttpRequest
													.sendPost(
															setGateWorkParamUrl,
															"mac="
																	+ equipment
																			.get(i)
																			.getMac()
																	+ "&gateId="
																	+ j
																	+ "&open_delay=5&open_overtime=5"
																	+ "&device_type="
																	+ equipment
																			.get(i)
																			.getDevicetype()
																	+ "&use_super_password=0"
																	+ "&super_password=0&use_force_password=0"
																	+ "&force_password=0&relock_password=0"
																	+ "&unlock_password=0&police_password=0"
																	+ "&open_pattern=0&check_open_pattern=0"
																	+ "&work_pattern=0&multi_open_number=0"
																	+ "&multi_open_card_num=");
											Map setParamResult = GsonUtil
													.toBean(setGateWorkParamStr,
															HashMap.class);

											if ("0".equals(setParamResult
													.get("result_code"))) {
												deviceService.addDoorinfo(map);
											}
										}
									} else {
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("device_num", equipment.get(i)
												.getDevicenum());
										map.put("validity_flag", "0");
										map.put("door_name", 1);
										map.put("create_user", admin_id);
										map.put("door_num", 1);
										map.put("open_delay", 5);
										map.put("open_overtime", 5);
										map.put("use_super_password", 0);
										map.put("super_password", 0);
										map.put("use_force_password", 0);
										map.put("force_password", 0);
										map.put("relock_password", 0);
										map.put("unlock_password", 0);
										map.put("police_password", 0);
										map.put("open_pattern", 0);
										map.put("check_open_pattern", 0);
										map.put("work_pattern", 0);
										map.put("multi_open_number", 0);
										map.put("multi_open_card_num", null);
										// 新增门信息之前调用接口
										/*
										 * Map<String, String> urlmap =
										 * PropertiesToMap .propertyToMap(
										 * "comm_interface.properties");
										 */
										// （1）调用设置门工作参数接口
										String setGateWorkParamUrl = mjCommService
												.getMjCommUrl(equipment.get(i)
														.getDevicenum())
												+ "gateWorkParam/setGateWorkParam.do";
										String setGateWorkParamStr = HttpRequest
												.sendPost(
														setGateWorkParamUrl,
														"mac="
																+ equipment
																		.get(i)
																		.getMac()
																+ "&gateId=1"
																+ "&open_delay=5&open_overtime=5"
																+ "&device_type="
																+ equipment
																		.get(i)
																		.getDevicetype()
																+ "&use_super_password=0"
																+ "&super_password=0&use_force_password=0"
																+ "&force_password=0&relock_password=0"
																+ "&unlock_password=0&police_password=0"
																+ "&open_pattern=0&check_open_pattern=0"
																+ "&work_pattern=0&multi_open_number=0"
																+ "&multi_open_card_num=");
										Map setParamResult = GsonUtil.toBean(
												setGateWorkParamStr,
												HashMap.class);

										if ("0".equals(setParamResult
												.get("result_code"))) {
											deviceService.addDoorinfo(map);
										}
									}
								}
							}

							deviceService.setDeviceTimeGroup(doorType,
									admin_id, equipment.get(i).getMac());

						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
			resultMap.put("count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}

	/*
	 * public void searchDeviceInfo() { User user = (User)
	 * getHttpSession().getAttribute( GlobalConstant.LOGIN_USER); String
	 * admin_id = user.getYhMc(); HttpServletRequest request =
	 * getHttpServletRequest(); String doorType =
	 * request.getParameter("doorType"); String beginIp =
	 * request.getParameter("beginIp"); String endIp =
	 * request.getParameter("endIp");
	 * 
	 * String commIp = request.getParameter("commIp"); String commPort =
	 * request.getParameter("commPort"); String commId =
	 * request.getParameter("commId");
	 * 
	 * String[] commIps=commIp.split(","); String[]
	 * commPorts=commPort.split(","); String[] commIds=commId.split(",");
	 * 
	 * 
	 * 
	 * if (beginIp.equals("...") && endIp.equals("...")) { InetAddress addr =
	 * null; try { addr = InetAddress.getLocalHost(); } catch
	 * (UnknownHostException e) { e.printStackTrace(); }
	 * 
	 * byte[] ipAddr = addr.getAddress(); String ipAddrStr = ""; for (int i = 0;
	 * i < ipAddr.length; i++) { if (i > 0) { ipAddrStr += "."; } ipAddrStr +=
	 * ipAddr[i] & 0xFF; } String[] server_ip = ipAddrStr.split("\\."); beginIp
	 * = server_ip[0] + "." + server_ip[1] + "." + server_ip[2] + "." + "0";
	 * endIp = server_ip[0] + "." + server_ip[1] + "." + server_ip[2] + "." +
	 * "255"; } Map<String, String> mappro = PropertiesToMap
	 * .propertyToMap("comm_interface.properties"); Map<String, Object>
	 * resultMap = new HashMap<String, Object>(); Integer count = 0; try {
	 * for(int k=0;k<commIps.length;k++){
	 * 
	 * System.out.println("request:" +
	 * "http://"+commIps[k]+":"+commPorts[k]+"/comm/" +
	 * "search/searchEquipment.do?" + "doorType=" + doorType + "&beginIp=" +
	 * beginIp + "&endIp=" + endIp); String str = HttpRequest.sendPost(
	 * "http://"+commIps[k]+":"+commPorts[k]+"/comm/" +
	 * "search/searchEquipment.do?", "doorType=" + doorType + "&beginIp=" +
	 * beginIp + "&endIp=" + endIp); System.out.println("返回信息：" + str);
	 * ResultMsg msg = new ResultMsg(); Gson gson = new Gson();
	 * java.lang.reflect.Type type = new TypeToken<ResultMsg>() { }.getType();
	 * msg = gson.fromJson(str, type); if (msg != null) { List<EquipmentBean>
	 * equipment = msg.getListEquipment(); for (int i = 0; i < equipment.size();
	 * i++) { UUID uuid = UUID.randomUUID();
	 * equipment.get(i).setCreateuser(admin_id);
	 * equipment.get(i).setDevicenum(String.valueOf(uuid));
	 * equipment.get(i).setDevicetype(doorType);
	 * equipment.get(i).setDeviceName(equipment.get(i).getMac());
	 * equipment.get(i).setCommId(commIds[k]);
	 * 
	 * if (equipment.get(i).getMac().length() == 1) { equipment.get(i).setMac(
	 * "00000" + equipment.get(i).getMac() .toUpperCase()); } else if
	 * (equipment.get(i).getMac().length() == 2) { equipment.get(i).setMac(
	 * "0000" + equipment.get(i).getMac() .toUpperCase()); } else if
	 * (equipment.get(i).getMac().length() == 3) { equipment.get(i)
	 * .setMac("000" + equipment.get(i).getMac() .toUpperCase()); } else if
	 * (equipment.get(i).getMac().length() == 4) { equipment.get(i).setMac( "00"
	 * + equipment.get(i).getMac().toUpperCase()); } else if
	 * (equipment.get(i).getMac().length() == 5) { equipment.get(i).setMac( "0"
	 * + equipment.get(i).getMac().toUpperCase()); } else {
	 * equipment.get(i).setMac( equipment.get(i).getMac().toUpperCase()); } try
	 * { count = deviceService.addDeviceInfo(equipment.get(i)) + count; //
	 * 根据mac查询设备表中设备编号 String devNum = deviceService
	 * .getDoorByDeviNum(equipment.get(i).getMac()); // 根据设备编号查询门信息表中的设备编号
	 * String devdoorNum = deviceService .getDeviceByDeviNum(devNum); String
	 * deldoorNum = deviceService .getDeleteDoorByDeviNum(devNum); //
	 * 判断门信息表中有没有该设备被删除 if (devdoorNum == null) { if (deldoorNum != null) {
	 * deviceService.updateDeleteDoorByNum(devNum); } else { if
	 * (equipment.get(i).getDevicetype() .equals("4")) {
	 * System.out.println(equipment.get(i) .getDevicetype()); for (int j = 1; j
	 * < 5; j++) { Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("device_num", devNum); map.put("validity_flag", "0");
	 * map.put("door_name", j); map.put("create_user", admin_id);
	 * map.put("door_num", j); map.put("open_delay", 5);
	 * map.put("open_overtime", 1); map.put("use_super_password", 0);
	 * map.put("super_password", 0); map.put("use_force_password", 0);
	 * map.put("force_password", 0); map.put("relock_password", 0);
	 * map.put("unlock_password", 0); map.put("police_password", 0);
	 * map.put("open_pattern", 0); map.put("check_open_pattern", 0);
	 * map.put("work_pattern", 0); map.put("multi_open_number", 0);
	 * map.put("multi_open_card_num", null); // 新增门信息之前调用接口 Map<String, String>
	 * urlmap = PropertiesToMap .propertyToMap("comm_interface.properties"); //
	 * （1）调用设置门工作参数接口 String setGateWorkParamUrl =
	 * mjCommService.getMjCommUrl(devNum) + "gateWorkParam/setGateWorkParam.do";
	 * String setGateWorkParamStr = HttpRequest .sendPost( setGateWorkParamUrl,
	 * "mac=" + equipment .get(i) .getMac() + "&gateId=" + j +
	 * "&open_delay=1&open_overtime=1" + "&device_type=" + equipment .get(i)
	 * .getDevicetype() + "&use_super_password=0" +
	 * "&super_password=0&use_force_password=0" +
	 * "&force_password=0&relock_password=0" +
	 * "&unlock_password=0&police_password=0" +
	 * "&open_pattern=0&check_open_pattern=0" +
	 * "&work_pattern=0&multi_open_number=0" + "&multi_open_card_num="); Map
	 * setParamResult = GsonUtil.toBean( setGateWorkParamStr, HashMap.class);
	 * 
	 * if ("0".equals(setParamResult .get("result_code"))) {
	 * deviceService.addDoorinfo(map); } } } else { Map<String, Object> map =
	 * new HashMap<String, Object>(); map.put("device_num", equipment.get(i)
	 * .getDevicenum()); map.put("validity_flag", "0"); map.put("door_name", 1);
	 * map.put("create_user", admin_id); map.put("door_num", 1);
	 * map.put("open_delay", 5); map.put("open_overtime", 1);
	 * map.put("use_super_password", 0); map.put("super_password", 0);
	 * map.put("use_force_password", 0); map.put("force_password", 0);
	 * map.put("relock_password", 0); map.put("unlock_password", 0);
	 * map.put("police_password", 0); map.put("open_pattern", 0);
	 * map.put("check_open_pattern", 0); map.put("work_pattern", 0);
	 * map.put("multi_open_number", 0); map.put("multi_open_card_num", null); //
	 * 新增门信息之前调用接口 Map<String, String> urlmap = PropertiesToMap
	 * .propertyToMap("comm_interface.properties"); // （1）调用设置门工作参数接口 String
	 * setGateWorkParamUrl =mjCommService.getMjCommUrl( equipment.get(i)
	 * .getDevicenum()) + "gateWorkParam/setGateWorkParam.do"; String
	 * setGateWorkParamStr = HttpRequest .sendPost( setGateWorkParamUrl, "mac="
	 * + equipment.get(i) .getMac() + "&gateId=1" +
	 * "&open_delay=1&open_overtime=1" + "&device_type=" + equipment .get(i)
	 * .getDevicetype() + "&use_super_password=0" +
	 * "&super_password=0&use_force_password=0" +
	 * "&force_password=0&relock_password=0" +
	 * "&unlock_password=0&police_password=0" +
	 * "&open_pattern=0&check_open_pattern=0" +
	 * "&work_pattern=0&multi_open_number=0" + "&multi_open_card_num="); Map
	 * setParamResult = GsonUtil.toBean( setGateWorkParamStr, HashMap.class);
	 * 
	 * if ("0".equals(setParamResult .get("result_code"))) {
	 * deviceService.addDoorinfo(map); } } } }
	 * 
	 * deviceService.setDeviceTimeGroup(doorType, admin_id,
	 * equipment.get(i).getMac());
	 * 
	 * } catch (Exception e) { e.printStackTrace(); continue; } } } }
	 * resultMap.put("count", count); } catch (Exception e) {
	 * e.printStackTrace(); } printHttpServletResponse(new
	 * Gson().toJson(resultMap)); }
	 */
	/**
	 * 根据设备号查询mac地址
	 * 
	 * @author minting.he
	 */
	public void getMacByDeviNum() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String device_mac = deviceService.getMacByDeviNum(device_num);
		printHttpServletResponse(GsonUtil.toJson(device_mac));
	}

	// 按筛选条件导出设备信息
	public void exportDevice() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		String device_name = request.getParameter("device_name");
		String device_mac = request.getParameter("device_mac");
		List<DeviceInfo> devices = deviceService.exportDevice(device_name,
				device_mac);
		String jsonList = GsonUtil.toJson(devices);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置 列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> cloTitleList = new ArrayList<String>();

		cloTitleList.add("设备名称");
		cloTitleList.add("MAC地址");
		cloTitleList.add("设备类型");
		cloTitleList.add("设备硬件版本");
		cloTitleList.add("设备程序版本");
		cloTitleList.add("子网掩码");
		cloTitleList.add("网关参数");
		cloTitleList.add("本地IP地址");
		cloTitleList.add("记录上报IP地址");
		cloTitleList.add("状态本地端口");
		cloTitleList.add("状态远程端口");
		cloTitleList.add("命令本地端口");
		cloTitleList.add("命令远程端口");
		cloTitleList.add("事件本地端口");
		cloTitleList.add("事件远程端口");
		cloTitleList.add("设备时间");
		cloTitleList.add("创建人员");
		cloTitleList.add("创建时间");
		cloTitleList.add("描述");

		colList.add("device_name");
		colList.add("device_mac");
		colList.add("device_type");
		colList.add("hardware_version");
		colList.add("progarm_version");
		colList.add("subnet_mask");
		colList.add("gateway_param");
		colList.add("local_ip_address");
		colList.add("remote_id_address");
		colList.add("local_state_port");
		colList.add("remote_state_port");
		colList.add("local_order_port");
		colList.add("remote_order_port");
		colList.add("local_event_port");
		colList.add("remote_event_port");
		colList.add("device_time");
		colList.add("create_user");
		colList.add("create_time");
		colList.add("description");

		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = cloTitleList.get(i);
		}

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msecxel");
			String fileName = "门禁设备信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("门禁设备信息表",
					colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空硬件设备数据
	 * 
	 * @author minting.he
	 */
	public void clearDeviceData() {
		HttpServletRequest request = getHttpServletRequest();
		String sign = request.getParameter("sign");
		String mac = request.getParameter("mac");
		String dataType = request.getParameter("dataType");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(sign, mac, dataType)) {
			result.setSuccess(false);
			result.setMsg("清空失败，数据不合法");
		} else {
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = user.getYhMc();
			if (EmptyUtil.atLeastOneIsEmpty(login_user)) {
				result.setSuccess(false);
				result.setMsg("清空失败，请先登录");
			} else {
				boolean r = deviceService.clearDeviceData(sign, mac, dataType,
						login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("清空成功");
				} else {
					result.setSuccess(false);
					result.setMsg("清空失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-26 下午2:56:00
	 * @功能描述:取消消防联动
	 * @参数描述:
	 */
	public void cancelFileConsole() {
		HttpServletRequest request = getHttpServletRequest();
		String devices = request.getParameter("devices");
		JsonResult result = new JsonResult();

		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = user.getYhMc();
		String[] device = devices.split(",");
		Set<String> set = new HashSet<String>();
		for (int j = 0; j < device.length; j++) {
			set.add(device[j]);// 去重
		}
		String[] device_nums = new String[set.size()];
		device_nums = set.toArray(device_nums);
		String strSucc = "";
		String strFail = "";
		for (int i = 0; i < device_nums.length; i++) {
			Map deviceMap = deviceService.getMacByDeviceNum(device_nums[i]);
			if (null == deviceMap) {
				continue;
			}
			String mac = (String) deviceMap.get("deviceMac");
			String deviceType = (String) deviceMap.get("deviceType");
			String deviceName = (String) deviceMap.get("deviceName");
			String useFireFighting = "0";
			Map<String, String> map = PropertiesToMap
					.propertyToMap("comm_interface.properties");
			String fireUrl = mjCommService.getMjCommUrl(device_nums[i])
					+ "fireAction/setFire.do?";
			String fireRespStr = HttpRequest.sendPost(fireUrl,
					"useFireFighting=" + useFireFighting + "&mac=" + mac
							+ "&sign=" + deviceType);
			Map fireMap = GsonUtil.toBean(fireRespStr, HashMap.class);
			if (fireMap != null) {
				if ("0".equals(fireMap.get("result_code"))) {
					Map strMap = new HashMap();
					strMap.put("fire_flag", useFireFighting);
					strMap.put("device_num", device[i]);
					boolean res = deviceService.setOpenFireConsole(strMap,
							login_user);
					if (res) {
						strSucc += deviceName + " ";
					} else {
						strFail += deviceName + " ";
						continue;
					}
				} else {
					strFail += deviceName + " ";
					continue;
				}
			} else {
				strFail += deviceName + " ";
				continue;
			}
		}

		if ("".equals(strSucc)) {
			result.setMsg("设置 " + "<font color='red'>" + strFail + "</font>"
					+ " 设备取消消防联动失败 ");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		if ("".equals(strFail)) {
			result.setMsg("设置 " + "<font color='green'>" + strSucc + "</font>"
					+ " 设备取消消防联动成功 ");
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		result.setMsg("设置 " + "<font color='green'>" + strSucc + "</font>"
				+ "设备取消消防联动成功，" + "<font color='red'>" + strFail + "</font>"
				+ "设备取消消防联动失败");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-26 下午2:56:12
	 * @功能描述:设置消防联动
	 * @参数描述:
	 */
	public void openFileConsole() {
		HttpServletRequest request = getHttpServletRequest();
		String devices = request.getParameter("devices");
		JsonResult result = new JsonResult();
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = user.getYhMc();
		String[] device = devices.split(",");
		Set<String> set = new HashSet<String>();
		for (int j = 0; j < device.length; j++) {
			set.add(device[j]);// 去重
		}
		String[] device_nums = new String[set.size()];
		device_nums = set.toArray(device_nums);
		String strSucc = "";
		String strFail = "";
		for (int i = 0; i < device_nums.length; i++) {
			Map deviceMap = deviceService.getMacByDeviceNum(device_nums[i]);
			if (null == deviceMap) {
				continue;
			}
			String mac = (String) deviceMap.get("deviceMac");
			String deviceType = (String) deviceMap.get("deviceType");
			String deviceName = (String) deviceMap.get("deviceName");
			String useFireFighting = "1";
			Map<String, String> map = PropertiesToMap
					.propertyToMap("comm_interface.properties");
			String fireUrl = mjCommService.getMjCommUrl(device_nums[i])
					+ "fireAction/setFire.do?";
			String fireRespStr = HttpRequest.sendPost(fireUrl,
					"useFireFighting=" + useFireFighting + "&mac=" + mac
							+ "&sign=" + deviceType);

			Map fireMap = GsonUtil.toBean(fireRespStr, HashMap.class);
			if (fireMap != null) {
				if ("0".equals(fireMap.get("result_code"))) {
					Map strMap = new HashMap();
					strMap.put("fire_flag", useFireFighting);
					strMap.put("device_num", device[i]);
					boolean res = deviceService.setOpenFireConsole(strMap,
							login_user);
					if (res) {
						strSucc += deviceName + " ";
					} else {
						strFail += deviceName + " ";
						continue;
					}
				} else {
					strFail += deviceName + " ";
					continue;
				}
			} else {
				strFail += deviceName + " ";
				continue;
			}
		}

		if ("".equals(strSucc)) {
			result.setMsg("设置 " + "<font color='red'>" + strFail + "</font>"
					+ " 设备消防联动失败 ");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		if ("".equals(strFail)) {
			result.setMsg("设置 " + "<font color='green'>" + strSucc + "</font>"
					+ " 设备消防联动成功 ");
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		result.setMsg("设置 " + "<font color='green'>" + strSucc + "</font>"
				+ "设备消防联动成功，" + "<font color='red'>" + strFail + "</font>"
				+ "设备消防联动失败");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 远程开门
	 * 
	 * @author minting.he
	 */
	public void remoteOpenDoor() {
		HttpServletRequest request = getHttpServletRequest();
		String doors = request.getParameter("doors");
		String devices = request.getParameter("devices");
		JsonResult result = new JsonResult();
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		if (BeanUtil.isEmpty(user)) {
			result.setSuccess(false);
			result.setMsg("开门失败，请先登录");
		} else {
			String login_user = user.getYhMc();
			if (EmptyUtil.atLeastOneIsEmpty(doors, devices)) {
				result.setSuccess(false);
				result.setMsg("开门失败，没有门");
			} else {
				boolean r = deviceService.remoteOpenDoor(doors, devices,
						login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("开门成功");
				} else {
					result.setSuccess(false);
					result.setMsg("开门失败，连接异常");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 光钥匙二维码远程开门
	 * 
	 * @author xuewen.deng
	 */
	public void qrCodeRemoteOpenDoor() {
		HttpServletRequest request = getHttpServletRequest();
		String doors = request.getParameter("doors");
		String devices = request.getParameter("devices");
		String card_num = request.getParameter("card_num");
		String staff_name = request.getParameter("staff_name");
		String staff_no = request.getParameter("staff_no");
		String phone = request.getParameter("phone");// 用手机号当登录用户（做日志）
		JsonResult result = new JsonResult();

		if (EmptyUtil.atLeastOneIsEmpty(doors, devices)) {
			result.setSuccess(false);
			result.setMsg("开门失败，没有门");
		} else {
			boolean r = deviceService.qrCodeRemoteOpenDoor(doors, devices,
					phone, card_num, staff_name, staff_no);
			if (r) {
				result.setSuccess(true);
				result.setMsg("开门成功");
			} else {
				result.setSuccess(false);
				result.setMsg("开门失败，连接异常或无权限");
			}

		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @功能：远程开门
	 * @描述：光钥匙对接
	 * @author kaiying.chen
	 */
	public void remoteOpenDoorForLightKey() {
		HttpServletRequest request = getHttpServletRequest();
		String doors = request.getParameter("doors");
		String devices = request.getParameter("devices");
		String phone = request.getParameter("phone");

		JsonResult result = new JsonResult();
		String login_user = phone;
		if (EmptyUtil.atLeastOneIsEmpty(doors, devices)) {
			result.setSuccess(false);
			result.setMsg("开门失败，没有门");
		} else {
			boolean r = deviceService
					.remoteOpenDoor(doors, devices, login_user);
			if (r) {
				result.setSuccess(true);
				result.setMsg("开门成功");
			} else {
				result.setSuccess(false);
				result.setMsg("开门失败，连接异常");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-14 下午4:03:45
	 * @功能描述:获取设备校时时间
	 * @参数描述:
	 */
	public void getDevTime() {
		String mac = getHttpServletRequest().getParameter("mac");
		String device_type = getHttpServletRequest()
				.getParameter("device_type");
		TimeResultMsg result = deviceService.getDevTime(mac, device_type);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 设备校时
	 * 
	 * @author xuewen.deng 2016.7.6
	 */
	public void setTime2() {
		String mac = getHttpServletRequest().getParameter("mac");
		String device_type = getHttpServletRequest()
				.getParameter("device_type");
		ResultMsg result = deviceService.setDeviceTime2(mac, device_type);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 设备复位
	 * 
	 * @author xuewen.deng 2016.7.6
	 */
	public void restartDevice() {
		String mac = getHttpServletRequest().getParameter("mac");
		String device_type = getHttpServletRequest()
				.getParameter("device_type");
		ResultMsg result = deviceService.restartDevice(mac, device_type);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 设置实时上传参数 guofei.lian 2016.07.07
	 * */
	public void setActualTimeParam() {
		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("mac");
		String device_type = request.getParameter("device_type");
		// String actualStatus = request.getParameter("actualStatus");//
		String actualStatus = request.getParameter("actualStatus");// 实时状态
		String cardRecord = request.getParameter("cardRecord");// 刷卡记录
		String intervaltime = request.getParameter("intervaltime");// 上传间隔时间

		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		Integer door_event = 2;// 巡更、门禁事件记录默认值为2
		// Integer actual_status = Integer.parseInt(actualStatus);
		Integer actual_Status = Integer.parseInt(actualStatus);
		Integer card_record = Integer.parseInt(cardRecord);
		Integer actualtime = actual_Status | door_event | card_record;// 做按位或运算

		JsonResult result = new JsonResult();
		/*
		 * Map<String, String> map = PropertiesToMap
		 * .propertyToMap("comm_interface.properties");
		 */
		String actualtimeUrl = mjCommService.getMjCommUrl(deviceNum)
				+ "actualTimeAction/setActualTime.do";
		String actualtimeStr = HttpRequest.sendPost(actualtimeUrl, "mac=" + mac
				+ "&device_type=" + device_type + "&actual_time=" + actualtime
				+ "&intervaltime=" + intervaltime);
		Map actualtimeResult = GsonUtil.toBean(actualtimeStr, HashMap.class);
		if (actualtimeResult != null) {
			if ("0".equals(actualtimeResult.get("result_code"))) {
				result.setMsg("设置成功");
				result.setSuccess(true);
			} else {
				result.setMsg("设置失败");
				result.setSuccess(false);
			}
		} else {
			result.setMsg("设置失败，连接异常");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-8 下午1:37:28
	 * @功能描述: 禁止状态上报接口
	 * @参数描述:
	 */
	public void setForbidDevReportParam() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String data = request.getParameter("data");
		Map dataMap = GsonUtil.toBean(data, HashMap.class);
		String mac = (String) dataMap.get("mac");
		String device_type = (String) dataMap.get("device_type");
		String isStatu = (String) dataMap.get("isStatu");

		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		/*
		 * Map<String, String> map = PropertiesToMap
		 * .propertyToMap("comm_interface.properties");
		 */
		String forbidDevReportUrl = mjCommService.getMjCommUrl(deviceNum)
				+ "forbidDevReportAction/setForbidDevReport.do";
		String forbidDevStr = HttpRequest.sendPost(forbidDevReportUrl, "mac="
				+ mac + "&device_type=" + device_type + "&isStatu=" + isStatu
				+ "&data=" + data);
		Map forbidResult = GsonUtil.toBean(forbidDevStr, HashMap.class);
		if (forbidResult != null) {
			if ("0".equals(forbidResult.get("result_code"))) {
				result.setMsg("设置禁止设备上报成功");
				result.setSuccess(true);
			} else {
				result.setMsg("设置禁止设备上报失败");
				result.setSuccess(false);
			}
		} else {
			result.setMsg("设置禁止设备上报失败，连接异常");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 获取禁止状态上报数据
	 */
	public void getForbidDevReport() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String data = request.getParameter("data");
		Map dataMap = GsonUtil.toBean(data, HashMap.class);
		String mac = (String) dataMap.get("mac");
		String device_type = (String) dataMap.get("device_type");

		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		/*
		 * List<DoorInfoModel> Doors = new ArrayList<DoorInfoModel>(); if (mac
		 * != null && !"".equals(mac)) { Doors =
		 * doorInfoService.selectDoorInfoByDeviceNum(mac); }
		 */
		/*
		 * Map<String, String> map = PropertiesToMap
		 * .propertyToMap("comm_interface.properties");
		 */
		String forbidDevReportUrl = mjCommService.getMjCommUrl(deviceNum)
				+ "forbidDevReportAction/getForbidDevReport.do";
		String forbidDevStr = HttpRequest.sendPost(forbidDevReportUrl, "mac="
				+ mac + "&device_type=" + device_type);
		Map forbidResult = GsonUtil.toBean(forbidDevStr, HashMap.class);
		if (forbidResult == null) {
			result.setSuccess(false);
			result.setMsg("读取禁止设备上报失败，连接异常");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		} else {
			Object str = forbidResult.get("timeForbids");
			Double enableDouble = (Double) forbidResult.get("enable");
			String enable = Integer.toHexString(enableDouble.intValue());
			ArrayList arrays = GsonUtil.toBean(str.toString(), ArrayList.class);
			Map maps1 = GsonUtil
					.toBean(arrays.get(0).toString(), HashMap.class);
			Map maps2 = GsonUtil
					.toBean(arrays.get(1).toString(), HashMap.class);
			Map maps3 = GsonUtil
					.toBean(arrays.get(2).toString(), HashMap.class);
			Map maps4 = GsonUtil
					.toBean(arrays.get(3).toString(), HashMap.class);

			String begin_time_hour1 = Integer.toHexString(((Double) maps1
					.get("startHour")).intValue());
			String begin_time_minute1 = Integer.toHexString(((Double) maps1
					.get("startMinute")).intValue());
			String end_time_hour1 = Integer.toHexString(((Double) maps1
					.get("endHour")).intValue());
			String end_time_minute1 = Integer.toHexString(((Double) maps1
					.get("endMinute")).intValue());
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("begin_time_hour", begin_time_hour1);
			map1.put("begin_time_minute", begin_time_minute1);
			map1.put("end_time_hour", end_time_hour1);
			map1.put("end_time_minute", end_time_minute1);

			String begin_time_hour2 = Integer.toHexString(((Double) maps2
					.get("startHour")).intValue());
			String begin_time_minute2 = Integer.toHexString(((Double) maps2
					.get("startMinute")).intValue());
			String end_time_hour2 = Integer.toHexString(((Double) maps2
					.get("endHour")).intValue());
			String end_time_minute2 = Integer.toHexString(((Double) maps2
					.get("endMinute")).intValue());
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("begin_time_hour", begin_time_hour2);
			map2.put("begin_time_minute", begin_time_minute2);
			map2.put("end_time_hour", end_time_hour2);
			map2.put("end_time_minute", end_time_minute2);

			String begin_time_hour3 = Integer.toHexString(((Double) maps3
					.get("startHour")).intValue());
			String begin_time_minute3 = Integer.toHexString(((Double) maps3
					.get("startMinute")).intValue());
			String end_time_hour3 = Integer.toHexString(((Double) maps3
					.get("endHour")).intValue());
			String end_time_minute3 = Integer.toHexString(((Double) maps3
					.get("endMinute")).intValue());
			Map<String, String> map3 = new HashMap<String, String>();
			map3.put("begin_time_hour", begin_time_hour3);
			map3.put("begin_time_minute", begin_time_minute3);
			map3.put("end_time_hour", end_time_hour3);
			map3.put("end_time_minute", end_time_minute3);

			String begin_time_hour4 = Integer.toHexString(((Double) maps4
					.get("startHour")).intValue());
			String begin_time_minute4 = Integer.toHexString(((Double) maps4
					.get("startMinute")).intValue());
			String end_time_hour4 = Integer.toHexString(((Double) maps4
					.get("endHour")).intValue());
			String end_time_minute4 = Integer.toHexString(((Double) maps4
					.get("endMinute")).intValue());
			Map<String, String> map4 = new HashMap<String, String>();
			map4.put("begin_time_hour", begin_time_hour4);
			map4.put("begin_time_minute", begin_time_minute4);
			map4.put("end_time_hour", end_time_hour4);
			map4.put("end_time_minute", end_time_minute4);

			/*
			 * if (!Doors.isEmpty()) { map1.put("door_num",
			 * Doors.get(0).getDoor_num()); map1.put("door_name",
			 * Doors.get(0).getDoor_name()); map2.put("door_num",
			 * Doors.get(1).getDoor_num()); map2.put("door_name",
			 * Doors.get(1).getDoor_name()); map3.put("door_num",
			 * Doors.get(2).getDoor_num()); map3.put("door_name",
			 * Doors.get(2).getDoor_name()); map4.put("door_num",
			 * Doors.get(3).getDoor_num()); map4.put("door_name",
			 * Doors.get(3).getDoor_name()); }
			 */

			ArrayList array = new ArrayList();
			array.add(0, map1);
			array.add(1, map2);
			array.add(2, map3);
			array.add(3, map4);
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("enable", enable);
			maps.put("timeForbids", array);
			result.setSuccess(true);
			result.setMsg(maps);
			printHttpServletResponse(GsonUtil.toJson(result));

		}

	}

	/** 通过设备编号获取设备信息 */
	public void getDeviceNameByNum() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		DeviceInfo deviceinfo = new DeviceInfo();
		if (!"".equals(device_num) && null != device_num) {
			deviceinfo = deviceService.getDeviceInfoByNum(device_num);
		}
		printHttpServletResponse(GsonUtil.toJson(deviceinfo));

	}

	/**
	 * 读取实时上传参数 guofei.lian 2016-07-12
	 * */
	public void getActualTimeParam() {
		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("mac");
		String device_type = request.getParameter("device_type");
		JsonResult result = new JsonResult();
		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		/*
		 * Map<String, String> map = PropertiesToMap
		 * .propertyToMap("comm_interface.properties");
		 */
		String actualtimeUrl = mjCommService.getMjCommUrl(deviceNum)
				+ "actualTimeAction/getActualTime.do";

		try {
			String actualtimeStr = HttpRequest.sendPost(actualtimeUrl, "mac="
					+ mac + "&device_type=" + device_type);
			Map actualtimeResult = GsonUtil
					.toBean(actualtimeStr, HashMap.class);
			Object data = null;
			if (actualtimeResult != null) {
				if ("0".equals(actualtimeResult.get("result_code"))) {
					// ActualTimeBean atb=(ActualTimeBean)
					// actualtimeResult.get("allActualTime");
					data = actualtimeResult.get("allActualTime");
					result.setSuccess(true);
					result.setMsg(data);
				} else {
					result.setSuccess(false);
					result.setMsg("读取实时上传参数失败");
				}
			} else {
				result.setSuccess(false);
				result.setMsg("读取实时上传参数失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("读取实时上传参数失败，连接异常");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/*
	 * public String getActualTimeParam() { HttpServletRequest request =
	 * getHttpServletRequest(); String mac = request.getParameter("mac"); String
	 * device_type = request.getParameter("device_type");
	 * 
	 * String deviceNum=mjCommService.getTkDevNumByMac(mac);
	 * 
	 * Map<String, String> map = PropertiesToMap
	 * .propertyToMap("comm_interface.properties"); String actualtimeUrl =
	 * mjCommService.getMjCommUrl(deviceNum) +
	 * "actualTimeAction/getActualTime.do"; String actualtimeStr =
	 * HttpRequest.sendPost(actualtimeUrl, "mac=" + mac + "&device_type=" +
	 * device_type); Map actualtimeResult = GsonUtil.toBean(actualtimeStr,
	 * HashMap.class); Object data = null; if
	 * ("0".equals(actualtimeResult.get("result_code"))) { // ActualTimeBean
	 * atb=(ActualTimeBean) // actualtimeResult.get("allActualTime"); data =
	 * actualtimeResult.get("allActualTime"); request.setAttribute("actualtime",
	 * data); } ;
	 * 
	 * return "success"; }
	 */

	/**
	 * @author jihui.deng
	 * @创建时间: 2016-10-25 下午2:56:41
	 * @功能描述: 软件版本更新
	 * @参数描述:
	 */
	public String uploadDeviceVersion() {

		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("device_mac");
		String device_type = request.getParameter("device_type");
		String deviceNum = mjCommService.getTkDevNumByMac(mac);
		if (null == uploadDeviceVersionFile) {
			request.setAttribute("message", "请上传版本更新需要的文件");
			return "success";
		}
		try {
			// 校验文件MD5是否合法
			String calPrint = null;
			String receivedPrint = null;
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(uploadDeviceVersionFile, "r");
				Integer endIndex = Long.valueOf(
						uploadDeviceVersionFile.length()).intValue() - 16;
				raf.seek(0);
				byte[] contents = new byte[endIndex];
				raf.read(contents);
				calPrint = com.kuangchi.sdd.util.algorithm.MD5Util
						.MD5(contents);
				raf.seek(endIndex);
				byte[] digitalFingerPrint = new byte[16];
				raf.read(digitalFingerPrint);
				receivedPrint = ByteToHexUtil
						.bytesToHexString(digitalFingerPrint);
				raf.close();
			} catch (Exception e) {
				request.setAttribute("message", "非法文件");
				return "success";
			} finally {
				if (raf != null) {
					raf.close();
				}
			}
			if (calPrint != null && calPrint.equals(receivedPrint)) {
				if (deviceService.uploadDeviceVersion(mac, device_type,
						uploadDeviceVersionFile)) {
					request.setAttribute("message", "版本更新成功");
					DeviceInfo device = deviceService
							.getDeviceIpByDeviceMac(mac);
					String localAddress = device.getLocal_ip_address();
					String[] localAddIp = localAddress.split("\\.");

					String beginIp = localAddIp[0] + "." + localAddIp[1] + "."
							+ localAddIp[2] + "." + localAddIp[3];
					String endIp = localAddIp[0] + "." + localAddIp[1] + "."
							+ localAddIp[2] + "." + localAddIp[3];
					boolean result = true;
					ResultMsg msg = new ResultMsg();

					while (result) {

						/*
						 * Map<String, String> mappro = PropertiesToMap
						 * .propertyToMap("comm_interface.properties");
						 */
						String str = HttpRequest.sendPost(
								mjCommService.getMjCommUrl(deviceNum)
										+ "search/searchEquipment.do?",
								"doorType=" + device.getDevice_type()
										+ "&beginIp=" + beginIp + "&endIp="
										+ endIp);

						Gson gson = new Gson();
						java.lang.reflect.Type type = new TypeToken<ResultMsg>() {
						}.getType();
						msg = gson.fromJson(str, type);
						if (msg != null) {
							if (msg.getListEquipment().size() > 0) {
								result = false;
							}
						} else {
							request.setAttribute("message", "版本更新失败，连接异常");
							return "success";
						}
					}
					User user = (User) getHttpSession().getAttribute(
							GlobalConstant.LOGIN_USER);
					String login_user = user.getYhMc();
					List<EquipmentBean> equipment = msg.getListEquipment();
					Map map = new HashMap();
					map.put("device_num", device.getDevice_num());
					map.put("hardware_version", equipment.get(0)
							.getMechineVersion());
					map.put("progarm_version", equipment.get(0)
							.getProgramVersion());
					deviceService.updateDeviceVersion(map, login_user);

					return "success";

				} else {
					request.setAttribute("message", "版本更新失败");
					return "success";
				}
			} else {
				request.setAttribute("message", "非法文件");
				return "success";
			}
		} catch (Exception e) {
			request.setAttribute("message", "版本更新失败");
			e.printStackTrace();
			return "success";
		}

	}

	/**
	 * 设备组是否关联到地图
	 * 
	 * @author minting.he
	 */
	public void ifRelatedPic() {
		HttpServletRequest request = getHttpServletRequest();
		String device_group_num = request.getParameter("device_group_num");
		String flag = request.getParameter("flag");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_group_num", "'" + device_group_num + "'");
		map.put("flag", flag);
		Integer count = deviceService.ifRelatedPic(map);
		printHttpServletResponse(GsonUtil.toJson(count));
	}

	/**
	 * @创建人　: jihui.deng
	 * @创建时间: 2016-9-2 下午2:48:44
	 * @功能描述: 密码验证
	 */
	public void validPassword() {
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String yhDm = loginUser.getYhDm();
		String password = request.getParameter("password");
		String md5Password = MD5.getInstance().encryption(password);
		Integer user = consumeRecordService.getUser(yhDm, md5Password);
		if (user > 0) {
			printHttpServletResponse(GsonUtil.toJson(true));
		} else {
			printHttpServletResponse(GsonUtil.toJson(false));
		}
	}

	/**
	 * 获取所有设备的状态
	 * 
	 * @author minting.he
	 */
	public void getAllDeviceState() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("devices");
		List<Map<String, Object>> list = deviceService
				.getAllDeviceState(device_num);
		printHttpServletResponse(GsonUtil.toJson(list));
	}

}
