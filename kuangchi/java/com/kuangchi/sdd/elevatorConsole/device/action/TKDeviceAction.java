package com.kuangchi.sdd.elevatorConsole.device.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.CommResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.device.service.TKDeviceOpenTimeService;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

/**
 * 梯控设备管理页面
 * 
 * @author
 * 
 */
@Controller("tkDeviceAction")
public class TKDeviceAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name = "tkDeviceService")
	ITKDeviceService deviceService;

	@Resource(name = "tKDeviceOpenTimeAction")
	TKDeviceOpenTimeAction tKDeviceOpenTimeAction;

	@Resource(name = "tKDeviceOpenTimeServiceImpl")
	private TKDeviceOpenTimeService tKDeviceOpenTimeService;
	
	@Resource(name = "tkCommServiceImpl")
	private TkCommService tkCommService;
	

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 梯控设备信息分页
	 * 
	 * @author minting.he
	 */
	public void getTKDeviceListParam() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Device device = GsonUtil.toBean(beanObject, Device.class);
		device.setPage(Integer.parseInt(request.getParameter("page")));
		device.setRows(Integer.parseInt(request.getParameter("rows")));
		Grid<Device> deviceGrid = deviceService.getTKDeviceListParam(device);
		printHttpServletResponse(GsonUtil.toJson(deviceGrid));
	}

	/**
	 * 获取设备树、设备的树
	 * 
	 * @author xuewen.deng
	 */
	public void deviceAndGroupTree() {
		Tree deviceAndGroupTree = deviceService.deviceAndGroupTree();
		printTree(deviceAndGroupTree);
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(deviceAndGroupTree));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}

	public void printTree(Tree tree) {
		List<Tree> childrenList = tree.getChildren();
		for (Tree treeNode : childrenList) {
			printTree(treeNode);
		}
	}

	/**
	 * 点击树查看设备
	 * 
	 * @author minting.he
	 */
	public void getTKDeviceByTree() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Device deviceSearch = GsonUtil.toBean(beanObject, Device.class);
		deviceSearch.setPage(Integer.parseInt(request.getParameter("page")));
		deviceSearch.setRows(Integer.parseInt(request.getParameter("rows")));
		Grid<Device> deviceGrid = deviceService.getTKDeviceByTree(deviceSearch);
		printHttpServletResponse(GsonUtil.toJson(deviceGrid));
	}

	/**
	 * 修改设备关联的设备组
	 * 
	 * @author minting.he
	 */
	public void changeTKDeviceGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_group_num = request.getParameter("device_group_num");
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("修改失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				boolean r = deviceService.changeTKDeviceGroup(device_group_num,
						device_num, login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("修改成功");
				} else {
					result.setSuccess(false);
					result.setMsg("修改失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 选择ip搜索
	 * 
	 * @author minting.he
	 */
	public void chooseIp() {
		try {
			//服务器ip
		/*	Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			List<Map> ipList = new ArrayList<Map>();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ip", ip.getHostAddress());
						ipList.add(map);
					}
				}
			}
		*/	
			//通讯服务器ip
			String url = PropertiesToMap.propertyToMap(
					"comm_interface.properties").get("comm_url");
			String message = HttpRequest.sendPost(url
					+ "TKDevice/getCommServerIp.do?", "");
			Gson gson = new Gson();
			List<String> commIp = gson.fromJson(message, ArrayList.class);
			List<Map<String, Object>> ipList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < commIp.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ip", commIp.get(i).toString());
				ipList.add(i, map);
			}
			printHttpServletResponse(GsonUtil.toJson(ipList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 搜索设备
	 * 
	 * @author minting.he
	 */
	public void seekTKDevice() {
		HttpServletRequest request = getHttpServletRequest();
		/*获取通讯服务器ip,端口号和地址*/
		String commIp=request.getParameter("commIp");
		String commPort=request.getParameter("commPort");
		String commId=request.getParameter("commId");
		
		JsonResult json = new JsonResult();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		
		if(EmptyUtil.atLeastOneIsEmpty(commIp, commPort, commId)){
			json.setSuccess(false);
			json.setMsg("操作失败，数据不合法");
		}else if (BeanUtil.isEmpty(loginUser)) {
			json.setSuccess(false);
			json.setMsg("操作失败，请先登录");
		} else {
			try {
				Integer count = 0;
				String login_user = loginUser.getYhMc();
				
				String[] commIps=commIp.split(",");
				String[] commPorts=commPort.split(",");
				String[] commIds=commId.split(",");
				
				List<Device> devices = new ArrayList<Device>();
				for(int k=0;k<commIps.length;k++){
					// 连接服务器
					String url ="http://"+commIps[k]+":"+commPorts[k]+"/comm/";
					
					String message = HttpRequest.sendPost(url
							+ "TKDevice/tk_SearchControls.do?", "");
					Gson gson = new Gson();
					List<LinkedTreeMap> commDevices = gson.fromJson(message,
							ArrayList.class);
					if (commDevices != null) {
						for (int i = 0; i < commDevices.size(); i++) {
							LinkedTreeMap map = commDevices.get(i);
							Device device = new Device();
							device.setMac(map.get("mac").toString());
							device.setDevice_ip(map.get("ip").toString());
							device.setDevice_port(map.get("port").toString());
							device.setDevice_name(map.get("mac").toString());
							device.setNetwork_gateway(map.get("gateway")
									.toString());
							device.setNetwork_mask(map.get("subnet").toString());
							device.setDevice_sequence(map.get("serialNo")
									.toString());
							device.setAddress(map.get("address").toString());
							device.setComm_id(commIds[k]);
							
							//获取控制器版本，再连接一次通讯服务器
							CommDevice comm = new CommDevice();
							comm.setMac(device.getMac());
							comm.setIp(device.getDevice_ip());
							comm.setPort(device.getDevice_port());
							comm.setGateway(device.getNetwork_gateway());
							comm.setSerialNo(device.getDevice_sequence());
							comm.setSubnet(device.getNetwork_mask());
							comm.setAddress(device.getAddress());
							String jsonDevice = GsonUtil.toJson(comm);
							String message2 = HttpRequest.sendPost(url
									+ "TKDevice/tk_RecvVersion.do?", "device="+ jsonDevice);
							CommResult commRes = GsonUtil.toBean(message2, CommResult.class);
							if (commRes.getResultCode() == 0) { 
								device.setDevice_version(commRes.getResultTxt());
							}else {
								device.setDevice_version("");
							}
							
							devices.add(device);
						}
					}
				}
				count = deviceService.seekTKDevice(devices, login_user);
				json.setSuccess(true);
				json.setMsg(count);
			
			} catch (Exception e) {
				e.printStackTrace();
				json.setSuccess(false);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(json));
	}

	/**
	 * 删除梯控设备
	 * 
	 * @author minting.he
	 */
	public void deleteTKDevice() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("删除失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				boolean r = deviceService
						.deleteTKDevice(device_num, login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("删除成功");
				} else {
					result.setSuccess(false);
					result.setMsg("删除失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 根据设备号获取设备信息
	 * 
	 * @author minting.he
	 */
	public void getInfoByTKDeviceNum() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		Device device = deviceService.getInfoByTKDeviceNum(device_num);
		printHttpServletResponse(GsonUtil.toJson(device));
	}

	/**
	 * 设备名称是否存在
	 * 
	 * @author minting.he
	 */
	public void ifExistTKDeviName() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String device_name = request.getParameter("device_name");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(id, id);
		map.put(device_name, device_name);
		Integer count = deviceService.ifExistTKDeviName(map);
		printHttpServletResponse(GsonUtil.toJson(count));
	}

	/**
	 * 修改梯控设备
	 * 
	 * @author minting.he
	 */
	public void updateTKDevice() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("dataForm");
		Device device = GsonUtil.toBean(beanObject, Device.class);
		JsonResult result = new JsonResult();
		if (BeanUtil.isEmpty(device)) {
			result.setSuccess(false);
			result.setMsg("修改失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				try {
					String login_user = loginUser.getYhMc();
					Device oldDevi = deviceService.getInfoByTKDeviceNum(device
							.getDevice_num());
					if (oldDevi.getDevice_ip().equals(device.getDevice_ip())
							&& oldDevi.getNetwork_gateway().equals(
									device.getNetwork_gateway())
							&& oldDevi.getNetwork_mask().equals(
									device.getNetwork_mask())) { // 网络设置不变，不需要调接口
						device.setDelete_flag("0");
						boolean r = deviceService.updateTKDevice(device,
								login_user);
						if (r) {
							result.setSuccess(true);
							result.setMsg("修改成功");
						} else {
							result.setSuccess(false);
							result.setMsg("修改失败");
						}
					} else {
						CommDevice comm = new CommDevice();
						comm.setMac(device.getMac());
						comm.setIp(device.getDevice_ip());
						comm.setPort(device.getDevice_port());
						comm.setGateway(device.getNetwork_gateway());
						comm.setSerialNo(device.getDevice_sequence());
						comm.setSubnet(device.getNetwork_mask());
						comm.setAddress(device.getAddress());
						String jsonDevice = GsonUtil.toJson(comm);
						String url = tkCommService.getTkCommUrl(device
							.getDevice_num());
						String message = HttpRequest.sendPost(url
								+ "TKDevice/tk_UpdateControl.do?", "device="
								+ jsonDevice + "&newAddress=1");
						CommResult commRes = GsonUtil.toBean(message,
								CommResult.class);
						if (commRes!=null && commRes.getResultCode() == 0) { // 连接成功
							device.setDelete_flag("0");
							boolean r = deviceService.updateTKDevice(device,
									login_user);
							if (r) {
								result.setSuccess(true);
								result.setMsg("修改成功");
							} else {
								result.setSuccess(false);
								result.setMsg("修改失败");
							}
						} else {
							result.setSuccess(false);
							result.setMsg("修改失败，连接异常");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.setSuccess(false);
					result.setMsg("修改失败，连接异常");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 修改设备状态
	 */
	public void updateTKDeviceState() {
		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("mac");
		String state = request.getParameter("state");
		Device device = new Device();
		device.setMac(mac);
		device.setOnline_state(state);
		deviceService.updateTKDevcieState(device);
	}

	/**
	 * 获取设备配置的楼层
	 * 
	 * @author minting.he
	 */
	public void getDeviceFloor() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Floor> floors = deviceService.getDeviceFloor(device_num);
		printHttpServletResponse(GsonUtil.toJson(floors));
	}

	/**
	 * 获取楼层名称
	 * 
	 * @author minting.he
	 */
	public void getFloorName() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Floor> floors = deviceService.getFloorName(device_num);
		printHttpServletResponse(GsonUtil.toJson(floors));
	}

	/**
	 * 16进制转list
	 * 
	 * @param jsonHexStr
	 * @return
	 */
	private static String encodeToJsonStr(String jsonHexStr) {
		String[] jsonHexArr = jsonHexStr.split(",");
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < jsonHexArr.length; i++) {
			String str;
			try {
				str = new String(jsonHexArr[i].getBytes("iso-8859-1"), "UTF-8");
				char ele = (char) Integer.parseInt(str, 16);
				build.append(ele);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String jsonStr = new String(build);
		return jsonStr;
	}

	/**
	 * 修改配置楼层
	 * 
	 * @author minting.he
	 */
	public void updateDeviceFloor() {
		HttpServletRequest request = getHttpServletRequest();
		String floorStr = request.getParameter("floorStr");
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(floorStr, device_num)) {
			result.setSuccess(false);
			result.setMsg("配置失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				List<Floor> floors = new ArrayList<Floor>();
				Gson gson = new Gson();
				List<Map<String, Object>> floorList = gson.fromJson(encodeToJsonStr(floorStr),
						new ArrayList<LinkedTreeMap>().getClass());
				for (int i = 0; i < floorList.size(); i++) {
					Floor floor = new Floor();
					String floor_num = (String) floorList.get(i).get(
							"floor_num");
					String floor_name = (String) floorList.get(i).get(
							"floor_name");
					String configuration_floor = (String) floorList.get(i).get(
							"configuration_floor");
					String description = (String) floorList.get(i).get(
							"description");
					floor.setFloor_num(floor_num);
					floor.setFloor_name(floor_name);
					floor.setConfiguration_floor(configuration_floor);
					floor.setDevice_num(device_num);
					floor.setDescription(description);
					floors.add(floor);
				}
				String login_user = loginUser.getYhMc();
				boolean r = deviceService.updateDeviceFloor(floors, login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("配置成功");
				} else {
					result.setSuccess(false);
					result.setMsg("配置失败，网络异常");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 设置楼层开放时区
	 * 
	 * @author minting.he
	 */
	public void floorOpenArea() {
		HttpServletRequest request = getHttpServletRequest();
		String dateObject = request.getParameter("dates");
		Gson gson = new Gson();
		HashMap floorMap = gson.fromJson(dateObject, HashMap.class);
		String floorNums = request.getParameter("floorNums");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(dateObject, floorNums)) {
			result.setSuccess(false);
			result.setMsg("下发失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				List<Floor> floors = new ArrayList<Floor>();
				String[] floorStr = floorNums.split(",");
				for (int i = 0; i < floorStr.length; i++) {
					Floor floor = new Floor();
					floor.setFloor_num(floorStr[i]);
					floor.setDevice_num(floorMap.get("device_num").toString());
					floor.setStart_time1(floorMap.get("startHour1") + ":"
							+ floorMap.get("startMinu1"));
					floor.setEnd_time1(floorMap.get("endHour1") + ":"
							+ floorMap.get("endMinu1"));
					floor.setStart_time2(floorMap.get("startHour2") + ":"
							+ floorMap.get("startMinu2"));
					floor.setEnd_time2(floorMap.get("endHour2") + ":"
							+ floorMap.get("endMinu2"));
					floor.setStart_time3(floorMap.get("startHour3") + ":"
							+ floorMap.get("startMinu3"));
					floor.setEnd_time3(floorMap.get("endHour3") + ":"
							+ floorMap.get("endMinu3"));
					floor.setStart_time4(floorMap.get("startHour4") + ":"
							+ floorMap.get("startMinu4"));
					floor.setEnd_time4(floorMap.get("endHour4") + ":"
							+ floorMap.get("endMinu4"));
					floors.add(floor);
				}
				boolean r = deviceService.setFloorOpenArea(floors, login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("下发成功");
				} else {
					result.setSuccess(false);
					result.setMsg("下发失败，网络异常");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 查看楼层开放时区
	 * 
	 * @author minting.he
	 */
	public void getFloorInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Floor> floors = deviceService.getFloorInfo(device_num);
		printHttpServletResponse(GsonUtil.toJson(floors));
	}

	/*
	 * public void copyEleAuthority(){ HttpServletRequest request =
	 * getHttpServletRequest(); String device_nums =
	 * request.getParameter("device_nums"); String[] device_num =
	 * device_nums.split(",") }
	 */

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-19 下午9:08:44
	 * @功能描述: 复制梯控设备设置
	 * @参数描述:
	 */
	public void copyFloorInfo() {
		HttpServletRequest request = getHttpServletRequest();
		String flags = request.getParameter("flags");
		String[] flag = flags.split(",");
		Arrays.sort(flag);
		String device_num = request.getParameter("device_num");
		String copy_device_num = request.getParameter("nums");
		JsonResult result = new JsonResult();
		String[] nums = copy_device_num.split(",");
		boolean res = false;
		if (EmptyUtil.atLeastOneIsEmpty(device_num, copy_device_num)) {
			result.setSuccess(false);
			result.setMsg("复制失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				Integer resVal0 = 0;// 配置楼层flag
				Integer resVal1 = 0;// 电梯开放时区flag
				Integer resVal2 = 0;// 楼层开放时区flag
				Integer resVal3 = 0;// 节假日flag
				Integer resVal4 = 0;// 权限flag
				String resultSucc = "";
				String resultFail = "";
				
				for(int k=0;k<nums.length;k++){
					Device targetNumInfo = deviceService.getInfoByTKDeviceNum(nums[k]);
					for (int i = 0; i < flag.length; i++) {
						if ("4".equals(flag[i])) { // 复制配置楼层
							if (deviceService.copyConfiFloor(device_num,
									nums[k])) {
								resVal0 = 1;
							} else {
								resVal0 = 2;
							}
						} else if ("3".equals(flag[i])) { // 复制楼层开放时区
							if (deviceService.copyFloorOpenArea(device_num,
									nums[k])) {
								resVal2 = 1;
							} else {
								resVal2 = 2;
							}
						} else if ("2".equals(flag[i])) { // 复制节假日
							if (deviceService.copyHoliday(device_num,
									nums[k])) {
								resVal3 = 1;
							} else {
								resVal3 = 2;
							}
						} else if ("1".equals(flag[i])) {// 复制电梯开放时区
							if (deviceService.copyEleOpenTime(device_num,
									nums[k])) {
								resVal1 = 1;
							} else {
								resVal1 = 2;
							}
						} else if ("0".equals(flag[i])) {// 复制动作参数
							Map<String, Object> map = new HashMap<String, Object>();
							Device device = deviceService
									.getInfoByTKDeviceNum(device_num);
							map.put("multi_opt_interval",
									device.getMulti_opt_interval());
							map.put("single_opt_interval",
									device.getSingle_opt_interval());
							map.put("device_up", device.getDevice_up());
							map.put("device_down", device.getDevice_down());
							map.put("device_open", device.getDevice_open());
							map.put("device_close", device.getDevice_close());
							if (deviceService.copyEleMoveParam(map,
									nums[k])) {
								resVal4 = 1;
							} else {
								resVal4 = 2;
							}
	
						}
					}
						if(2==resVal4||2==resVal1||2==resVal3||2==resVal0||2==resVal2){
							resultFail += targetNumInfo.getDevice_name()+" ";
							continue;
							
						}else{
						//下发设备都成功
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("resVal0", resVal0);
							map.put("resVal1", resVal1);
							map.put("resVal2", resVal2);
							map.put("resVal3", resVal3);
							map.put("resVal4", resVal4);
							
							map.put("targetNum", nums[k]);
							map.put("device_num", device_num);
							
							try {
								 res = tKDeviceOpenTimeService.copyElevatorSet(map,login_user);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
						if(res){
							resultSucc +=targetNumInfo.getDevice_name()+" ";
						}else{
							resultFail +=targetNumInfo.getDevice_name()+" ";
					}
				}
				result.setSuccess(true);
				if("".equals(resultSucc)){
					result.setMsg("复制 "+"<font color='red'>"+resultFail+"</font>"+" 设备失败 ");
					printHttpServletResponse(GsonUtil.toJson(result)); 
					return;
				}
				if("".equals(resultFail)){
					result.setMsg("复制 "+"<font color='green'>"+resultSucc+"</font>"+" 设备成功 ");
					printHttpServletResponse(GsonUtil.toJson(result)); 
					return;
				}
				
				result.setMsg("复制 "+"<font color='green'>"+resultSucc+"</font>"+"设备成功，复制 "+"<font color='red'>"+resultFail+"</font>"+"设备失败");
				printHttpServletResponse(GsonUtil.toJson(result)); 
			}
		}
	}

	/**
	 * 查询设备的节假日
	 * 
	 * @author minting.he
	 */
	public void getHolidayByDevice() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Holiday> list = deviceService.getHolisByDevice(device_num);
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 下发节假日
	 * 
	 * @author xuewen.deng
	 */
	public void issuedHoliday() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String holiday = request.getParameter("holiday");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(device_num, holiday)) {
			result.setSuccess(false);
			result.setMsg("下发失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				List<Holiday> holiList = new ArrayList<Holiday>();
				Gson gson = new Gson();
				List<Map<String, Object>> holiPageList = gson.fromJson(
						encodeToJsonStr(holiday),
						new ArrayList<LinkedTreeMap>().getClass());
				for (int i = 0; i < holiPageList.size(); i++) {
					Holiday holi = new Holiday();
					holi.setDevice_num(device_num);
					UUID uuid = UUID.randomUUID();
					holi.setHoliday_num(uuid.toString());
					holi.setHoliday_date(holiPageList.get(i)
							.get("holiday_date").toString());
					holi.setDescription(holiPageList.get(i).get("description") == null ? ""
							: holiPageList.get(i).get("description").toString());
					holiList.add(holi);
				}

				boolean r = deviceService.issuedHoliday(device_num, holiList,
						login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("下发成功");
				} else {
					result.setSuccess(false);
					result.setMsg("下发失败，网络异常");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 更新所有设备状态
	 * 
	 * @author minting.he
	 */
	public void updateAllState() {
		HttpServletRequest request = getHttpServletRequest();
		String online_state = request.getParameter("online_state");
		boolean result = deviceService.updateAllState(online_state);
		CommResult commRes = new CommResult();
		if (result) {
			commRes.setResultCode(0);
		} else {
			commRes.setResultCode(1);
		}
		printHttpServletResponse(GsonUtil.toJson(commRes));
	}

	/**
	 * 获取所有的设备信息
	 * 
	 * @author minting.he
	 */
	public void getAllTKDeviceInfo() {
		List<Device> list = deviceService.getAllTKDeviceInfo();
		List<CommDevice> commList = new ArrayList<CommDevice>();
		for (Device device : list) {
			CommDevice commDevice = new CommDevice();
			commDevice.setMac(device.getMac());
			commDevice.setIp(device.getDevice_ip());
			commDevice.setPort(device.getDevice_port());
			commDevice.setGateway(device.getNetwork_gateway());
			commDevice.setSerialNo(device.getDevice_sequence());
			commDevice.setSubnet(device.getNetwork_mask());
			commDevice.setAddress(device.getAddress());
			commList.add(commDevice);
		}
		printHttpServletResponse(GsonUtil.toJson(commList));
	}
	
	/**
	 * 获取是否启用的楼层
	 * @author minting.he
	 */
	public void getFlagFloor(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Map<String, Object>> list = deviceService.getFlagFloor(device_num);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 设置楼层是否启用
	 * @author minting.he
	 */
	public void setIfEnabledFloor(){
		JsonResult result = new JsonResult();
		try{
			HttpServletRequest request = getHttpServletRequest();
			String floorStr = request.getParameter("floorStr");
			String device_num = request.getParameter("device_num");
			if (EmptyUtil.atLeastOneIsEmpty(floorStr, device_num)) {
				result.setSuccess(false);
				result.setMsg("设置失败，数据不合法");
			} else {
				User loginUser = (User) request.getSession().getAttribute(
						GlobalConstant.LOGIN_USER);
				if (BeanUtil.isEmpty(loginUser)) {
					result.setSuccess(false);
					result.setMsg("操作失败，请先登录");
				} else {
					List<Map<String, Object>> floors = new ArrayList<Map<String, Object>>();
					Gson gson = new Gson();
					List<Map<String, Object>> floorList = gson.fromJson(encodeToJsonStr(floorStr),
							new ArrayList<LinkedTreeMap>().getClass());
					for (int i = 0; i < floorList.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						Floor floor = new Floor();
						String floor_num = (String) floorList.get(i).get("floor_num");
						String enabled_flag = (String) floorList.get(i).get("enabled_flag");
						map.put("device_num", device_num);
						map.put("floor_num", floor_num);
						map.put("enabled_flag", enabled_flag);
						floors.add(map);
					}
					String login_user = loginUser.getYhMc();
					boolean r = deviceService.setIfEnabledFloor(floors, login_user);
					if (r) {
						result.setSuccess(true);
						result.setMsg("设置成功");
					} else {
						result.setSuccess(false);
						result.setMsg("设置失败");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("设置失败");
		}finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 获取启用/不启用的楼层
	 * @author minting.he
	 */
	public void getIfEnabledFloor(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String enabled_flag = request.getParameter("enabled_flag");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("enabled_flag", enabled_flag);
		List<String> list = deviceService.getIfEnabledFloor(map);
		printHttpServletResponse(GsonUtil.toJson(list));
	}

}
