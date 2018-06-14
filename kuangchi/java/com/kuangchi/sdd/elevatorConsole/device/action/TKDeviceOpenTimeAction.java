package com.kuangchi.sdd.elevatorConsole.device.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.elevatorConsole.device.model.CommIpInfoModel;
import com.kuangchi.sdd.elevatorConsole.device.model.CommResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.HardWareParam;
import com.kuangchi.sdd.elevatorConsole.device.model.HardWareResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.model.OpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.device.service.TKDeviceOpenTimeService;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

@Controller("tKDeviceOpenTimeAction")
public class TKDeviceOpenTimeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name = "tKDeviceOpenTimeServiceImpl")
	private TKDeviceOpenTimeService tKDeviceOpenTimeService;

	@Resource(name = "tkDeviceService")
	ITKDeviceService deviceService;

	
	@Resource(name = "tkCommServiceImpl")
	private TkCommService tkCommService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-10 上午8:59:28
	 * @功能描述: 初始化设备
	 * @参数描述:
	 */
	public void initDeviceParam() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = user.getYhMc();
		String beanObject = request.getParameter("device");
		if (EmptyUtil.isEmpty(beanObject)) {
			result.setSuccess(false);
		} else {
			Device device = GsonUtil.toBean(beanObject, Device.class);
			if (tKDeviceOpenTimeService.initDeviceParam(device, login_user)) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-10 上午10:11:30
	 * @功能描述: 获取设备校时时间
	 * @参数描述:
	 */
	public void getDevTime() {
		String beanObject = getHttpServletRequest().getParameter("device");
		Device device = GsonUtil.toBean(beanObject, Device.class);
		TimeResultMsg result = tKDeviceOpenTimeService.getDevTime(device);
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-10 上午10:11:58
	 * @功能描述: 设置设备校时
	 * @参数描述:
	 */
	public void setDevTime() {
		String beanObject = getHttpServletRequest().getParameter("device");
		Device device = GsonUtil.toBean(beanObject, Device.class);
		CommResult res = new CommResult();
		CommResult result = tKDeviceOpenTimeService.setDevTime(device);
		if (null == result) {
			res.setResultCode(1);
			res.setResultTxt("校时失败");
			printHttpServletResponse(GsonUtil.toJson(res));
		} else {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-10 下午4:08:36
	 * @功能描述: 获取设备时段组信息
	 * @参数描述:
	 */
	public void getAllTimesGroupByDevNum() {
		Grid<TimesGroupModel> grid = tKDeviceOpenTimeService
				.getAllTimesGroupByDevNum();
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-13 上午10:55:42
	 * @功能描述: 查看开放时区
	 * @参数描述:
	 */
	public void checkDevTimeGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.isEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("设置失败，数据不合法");
			printHttpServletResponse(GsonUtil.toJson(result));
		} else {
			List<TKDeviceOpenTimeModel> list = tKDeviceOpenTimeService
					.checkDevTimeGroup(device_num);
			printHttpServletResponse(GsonUtil.toJson(list));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-12 下午5:54:03
	 * @功能描述:设置电梯开放时区
	 * @参数描述:
	 */
	public void motifyDeviceOpenTime() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String device_num = request.getParameter("device_num");
		/* String week_days = request.getParameter("week_days"); */
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("设置失败，数据不合法");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}

		Device device = tKDeviceOpenTimeService.getEleMoveParam(device_num);

		for (int i = 0; i < 8; i++) {
			OpenTimeZone openTimeZone = new OpenTimeZone();
			String beanObject = request.getParameter("data" + i);// 遍历获取form
			if (null == beanObject) {
				continue;
			} else {
				Map map = GsonUtil.toBean(beanObject, HashMap.class);
				Integer week_day = Integer.parseInt((String) map
						.get("week_day"));
				openTimeZone.setWeekDay(week_day);// 设置星期几
				for (int j = 1; j <= 5; j++) {// 一个form有五个时段
					TimeZone timeZone = new TimeZone();
					String start_time1 = (String) map.get("start_time" + j);
					String[] startTime1 = start_time1.split(":");
					String end_time1 = (String) map.get("end_time" + j);
					String[] endTime1 = end_time1.split(":");
					timeZone.setStartHour(startTime1[0]);
					timeZone.setStartMinute(startTime1[1]);
					timeZone.setEndHour(endTime1[0]);
					timeZone.setEndMinute(endTime1[1]);
					openTimeZone.getTimeZoneList().add(timeZone);
				}

				if (!tKDeviceOpenTimeService.motifyDeviceOpenTime(openTimeZone,
						device, map, login_user)) {
					result.setSuccess(false);
					result.setMsg("设置失败");
					printHttpServletResponse(GsonUtil.toJson(result));
					return;
				}
			}
		}
		result.setSuccess(true);
		result.setMsg("设置成功");
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-13 下午2:47:29
	 * @功能描述: 新增电梯开放时区
	 * @参数描述:
	 */
	public void initInsertDevOpenTime() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String device_num = request.getParameter("device_num");
		try {
			tKDeviceOpenTimeService
					.insertDeviceOpenTime(device_num, login_user);
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-13 下午2:47:29
	 * @功能描述: 删除电梯开放时区
	 * @参数描述:
	 */
	public void deleteDevOpenTime() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		String device_num = request.getParameter("device_num");

		try {
			tKDeviceOpenTimeService.deleteDevOpenTime(device_num, login_user);
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-14 上午10:42:28
	 * @功能描述: 设置梯控动作参数
	 * @参数描述:
	 */
	public void setEleMoveParam() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Map<String, Comparable> map = new HashMap<String, Comparable>();
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("设置失败，数据异常");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		Device device = tKDeviceOpenTimeService.getEleMoveParam(device_num);
		String mop = request.getParameter("multi_opt_interval");
		String sop = request.getParameter("single_opt_interval");
		//String vop = request.getParameter("visitor_opt_interval");
		String deU = request.getParameter("device_up");
		String deD = request.getParameter("device_down");
		String deO = request.getParameter("device_open");
		String deC = request.getParameter("device_close");
		if (EmptyUtil.atLeastOneIsEmpty(mop, sop)) {
			result.setSuccess(false);
			result.setMsg("设置失败，带*号为必填项");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		if (EmptyUtil.atLeastOneIsEmpty(deU, deD,
				deO, deC)) {
			result.setSuccess(false);
			result.setMsg("设置失败，有效电平参数不能为空");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		Integer multi_opt_interval = Integer.parseInt(mop);
		Integer single_opt_interval = Integer.parseInt(sop);
	//	Integer visitor_opt_interval = Integer.parseInt(vop);
		Integer device_up = null;
		Integer device_down = null;
		Integer device_open = null;
		Integer device_close = null;

		if ("低电平有效".equals(deU)) {
			device_up = 0;
		} else {
			device_up = 1;
		}
		if ("低电平有效".equals(deD)) {
			device_down = 0;
		} else {
			device_down = 1;
		}
		if ("低电平有效".equals(deO)) {
			device_open = 0;
		} else {
			device_open = 1;
		}
		if ("低电平有效".equals(deC)) {
			device_close = 0;
		} else {
			device_close = 1;
		}

		map.put("multi_opt_interval", multi_opt_interval);
		map.put("single_opt_interval", single_opt_interval);
		//map.put("visitor_opt_interval", visitor_opt_interval);
		map.put("device_up", device_up);
		map.put("device_down", device_down);
		map.put("device_open", device_open);
		map.put("device_close", device_close);
		map.put("device_num", device_num);

		boolean isResult = tKDeviceOpenTimeService.setEleMoveParam(device, map,
				login_user);
		if (isResult) {
			result.setSuccess(true);
			result.setMsg("设置成功");
		} else {
			result.setSuccess(false);
			result.setMsg("设置失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-14 下午1:13:31
	 * @功能描述: 根据设备编号查看设备信息
	 * @参数描述:
	 */
	public void getEleMoveParam() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("设置失败，数据异常");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		Device device = tKDeviceOpenTimeService.getEleMoveParam(device_num);
		printHttpServletResponse(GsonUtil.toJson(device));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-14 下午3:55:06
	 * @功能描述: 根据设备编号清除权限
	 * @参数描述:
	 */
	public void clearAuthorityByDevNum() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String deviceNums = request.getParameter("device_nums");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		if (EmptyUtil.atLeastOneIsEmpty(deviceNums)) {
			result.setSuccess(false);
			result.setMsg("清除权限失败，数据异常");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		String[] device_nums = deviceNums.split(",");
		for (int i = 0; i < device_nums.length; i++) {
			List<TkDevAuthorityCardModel> list = tKDeviceOpenTimeService
					.getAuthorityCardByDevNum(device_nums[i]);
			if (list.isEmpty() || null == list) {
				result.setMsg("清除权限失败，该设备没有权限");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
			} else {
				if (!tKDeviceOpenTimeService.clearAuthorityByDevNum(
						device_nums[i], list, login_user)) {
					result.setMsg("清除权限失败");
					printHttpServletResponse(GsonUtil.toJson(result));
					return;
				}
			}
			result.setSuccess(true);
			result.setMsg("清楚权限成功");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-17 下午7:36:00
	 * @功能描述: 读取梯控动作参数
	 * @参数描述:
	 */
	public void getEleLevel() {
		HttpServletRequest request = getHttpServletRequest();
		HardWareResult hardWareResult = new HardWareResult();
		String device_num = request.getParameter("device_num");
		if (EmptyUtil.isEmpty(device_num)) {
			hardWareResult.setSuccess(false);
			hardWareResult.setMsg("读取动作参数失败，数据异常");
			printHttpServletResponse(GsonUtil.toJson(hardWareResult));
			return;
		}
		Device device = tKDeviceOpenTimeService.getEleMoveParam(device_num);
		HardWareParam hardWareParam = tKDeviceOpenTimeService
				.getEleLevel(device);
		if (EmptyUtil.isEmpty(hardWareParam)) {
			hardWareResult.setMsg("读取动作参数失败");
			hardWareResult.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(hardWareResult));
		} else {
			hardWareResult.setMsg("读取动作参数成功");
			hardWareResult.setSuccess(true);
			hardWareResult.setHardWareParam(hardWareParam);
			printHttpServletResponse(GsonUtil.toJson(hardWareResult));
		}
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-31 上午9:32:31
	 * @功能描述: 根据设备编号查询设备权限
	 * @参数描述:
	 */
	public void checkAuthorityByDeviceNum(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<TkDevAuthorityCardModel> list = tKDeviceOpenTimeService
				.getAuthorityCardByDevNum(device_num);//获取设备绑定的权限
		JsonResult result = new JsonResult();
		if (list.isEmpty() || null == list) {
			result.setMsg("清除设备权限失败,该设备没有权限");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}else{
			result.setSuccess(true);
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-31 上午9:32:31
	 * @功能描述: 清除设置
	 * @参数描述:
	 */
	public void deleteEleParam() {
		HttpServletRequest request = getHttpServletRequest();
		String flags = request.getParameter("flags");
		String[] flag = flags.split(",");
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(device_num)) {
			result.setSuccess(false);
			result.setMsg("清除失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = loginUser.getYhMc();
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				Integer resVal = 0;// 清除楼层flag
				Integer resVal1 = 0;// 电梯开放时区flag
				Integer resVal2 = 0;// 楼层开放时区flag
				Integer resVal3 = 0;// 节假日flag
				Integer resVal4 = 0;// 权限flag
				Integer resVal5 = 0;// 动作参数flag
				Device device = tKDeviceOpenTimeService
						.getEleMoveParam(device_num);
				List<Floor> floors = new ArrayList<Floor>();
				List<TkDevAuthorityCardModel> list = tKDeviceOpenTimeService
						.getAuthorityCardByDevNum(device_num);//获取设备绑定的权限
				
				Device device1 = deviceService.getInfoByTKDeviceNum(device_num);
				
				
				for (int i = 0; i < flag.length; i++) {
					if ("0".equals(flag[i])) { // 清除配置楼层
						for (int j = 1; j <= 48; j++) {
							Floor floor = new Floor();
							floor.setFloor_num("" + j);
							floor.setFloor_name("第" + j + "层");
							floor.setDevice_num(device_num);
							floor.setConfiguration_floor("" + j);
							floor.setDescription("");
							floors.add(floor);
						}
						if (deviceService.updateDeviceFloorComm(floors, login_user)) {
							resVal = 1;// 成功
						} else {
							resVal = 2;// 失败
						}
						
						 // 清除楼层开放时区
					} else if ("2".equals(flag[i])) {
						boolean res = true;
						for (int k = 1; k <= 48; k++) {
							Floor floor = new Floor();
							floor.setFloor_num("" + k);
							floor.setDevice_num(device_num);
							floor.setStart_time1("00:00");
							floor.setEnd_time1("00:00");
							floor.setStart_time2("00:00");
							floor.setEnd_time2("00:00");
							floor.setStart_time3("00:00");
							floor.setEnd_time3("00:00");
							floor.setStart_time4("00:00");
							floor.setEnd_time4("00:00");
							// floorsOpen.add(floor);
							if (!deviceService.cleanFloorOpenArea(floor,
									login_user)) {
								res = false;
								break;
							}
						}
						if (res) {
							resVal2 = 1;
						} else {
							resVal2 = 2;
						}
						
						// 清除节假日
					} else if ("3".equals(flag[i])) { 
						List<Holiday> holiList = new ArrayList<Holiday>(); // 直接设置空对象
						if (deviceService.clearHoliday(device_num, holiList,
								login_user)) {
							resVal3 = 1;
						} else {
							resVal3 = 2;
						}
						
						// 清除电梯开放时区
					} else if ("1".equals(flag[i])) {
						for (int j = 1; j <= 8; j++) {
							OpenTimeZone openTimeZone = new OpenTimeZone();
							openTimeZone.setWeekDay(j);
							for (int k = 1; k <= 5; k++) {// 一个form有五个时段
								TimeZone timeZone = new TimeZone();
								timeZone.setStartHour("00");
								timeZone.setStartMinute("00");
								timeZone.setEndHour("00");
								timeZone.setEndMinute("00");
								openTimeZone.getTimeZoneList().add(timeZone);
							}
							if (deviceService.updateDeviceOpenTime(
									openTimeZone, device)) {
								resVal1 = 1;
							} else {
								resVal1 = 2;
							}
						}
						
						// 清除动作参数
					} else if ("5".equals(flag[i])) {
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put("multi_opt_interval", 5);
						map.put("single_opt_interval", 2);
						map.put("device_up", 1);
						map.put("device_down", 1);
						map.put("device_open", 0);
						map.put("device_close", 0);
						if (deviceService.setEleMoveParam(device, map)) {
							resVal5 = 1;
						} else {
							resVal5 = 2;
						}
						
						//清除权限
					}else if ("4".equals(flag[i])) {
						
						if(list!=null && !list.isEmpty()) {
							for(int z=0;z<list.size();z++){
								Map map1 = new HashMap();
								map1.put("card_num", list.get(z).getCard_num());
								map1.put("card_type", list.get(z).getCard_type());
								map1.put("start_time", list.get(z).getBegin_valid_time());
								map1.put("end_time", list.get(z).getEnd_valid_time());
								map1.put("floor_list", list.get(z).getFloor_list());
								map1.put("action_flag", 1);
								map1.put("result_flag", 0);
								map1.put("object_num", list.get(z).getObject_num());
								map1.put("object_type", list.get(z).getObject_type());
								map1.put("floor_group_num", list.get(z).getFloor_group_num());
								map1.put("device_num", list.get(z).getDevice_num());
								map1.put("try_times", 1);
								map1.put("address", device1.getAddress());
								map1.put("device_ip", device1.getDevice_ip());
								map1.put("device_port", Integer.parseInt(device1.getDevice_port()));
								resVal4 = delDeviceAuthority(map1);
								if(resVal4!=1){
									break;
								}
							}
						}
					} 
					/***************************************下发设备结束***********************************/
				}
							/*if (!deviceService.clearAuthorityByDevNum(
									device_num, list, login_user)) {
								resVal4 = 2;
							} else {
								resVal4 = 1;
							}*/
						
					if(2==resVal5||2==resVal4||2==resVal1||2==resVal3||2==resVal||2==resVal2){
						
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}else{
					//下发设备都成功
						Map map = new HashMap();
						map.put("resVal", resVal);
						map.put("resVal1", resVal1);
						map.put("resVal2", resVal2);
						map.put("resVal3", resVal3);
						map.put("resVal4", resVal4);
						map.put("resVal5", resVal5);
						map.put("device_num", device_num);
						map.put("floor", floors);
						map.put("authorityList", list);
						boolean resValid = false;
						try {
							resValid = tKDeviceOpenTimeService.cleanElevatorSet(map,login_user);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(resValid){
							result.setMsg("清除成功");
							result.setSuccess(true);
							printHttpServletResponse(GsonUtil.toJson(result));
						}else{
							result.setMsg("清除成功，保存失败");
							result.setSuccess(false);
							printHttpServletResponse(GsonUtil.toJson(result));
						}
				}
			}
		}
	}
	
	
	
	
	
	/**
	 * 删除设备权限
	 */
	public Integer delDeviceAuthority(Map map){
		Integer res = 1;
		//Map<String, String> map0 = PropertiesToMap.propertyToMap("comm_interface.properties");
		Map delAuthMap=null;
		if(!"".equals(tkCommService.getTkCommUrl(map.get("device_num").toString()))){
			String delAuthUrl = tkCommService.getTkCommUrl(map.get("device_num").toString())+ "delEleAuth.do?";
			//准备参数
			String dataMap = GsonUtil.toJson(map);
			
			//发送请求
			String delAuthStr = HttpRequest.sendPost(delAuthUrl, "data="+dataMap);
			//处理响应
			delAuthMap = GsonUtil.toBean(delAuthStr, HashMap.class);
		}
		if(delAuthMap!=null){
			Integer result_code=(int) Math.pow((Double) delAuthMap.get("resultCode"), 1);
			if (!(result_code == 0)) {
				res = 2;
			}
		}else{
			res = 2;
		}
		return res;
	}
	
	
	public void getCommIpInfoById(){
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		CommIpInfoModel commIpInfoModel = tKDeviceOpenTimeService.getCommIpInfoById(id);
		printHttpServletResponse(GsonUtil.toJson(commIpInfoModel));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-5 上午10:22:55
	 * @功能描述:	获取通讯服务器的ip 
	 * @参数描述:
	 */
	public void getCommIpInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String ip_address = request.getParameter("ip_address");
		Map map = new HashMap();
		map.put("ip_address", ip_address);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("skips", skips);
		Grid<CommIpInfoModel> grid = tKDeviceOpenTimeService.getCommIpInfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-12-5 上午10:22:55
	 * @功能描述:	新增通讯服务器的ip信息 
	 * @参数描述:
	 */
	public void addCommIpInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		JsonResult result = new JsonResult();
		if(EmptyUtil.isEmpty(beanObject)){
			result.setMsg("新增失败，数据异常");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		boolean valid = tKDeviceOpenTimeService.addCommIpInfo(map,login_user);
		if(valid){
			result.setMsg("新增成功");
			result.setSuccess(true);
		}else{
			result.setMsg("新增失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	public void updateCommIpInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		String id = request.getParameter("id");
		JsonResult result = new JsonResult();
		if(EmptyUtil.isEmpty(beanObject)){
			result.setMsg("修改失败，数据异常");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		map.put("id", id);
		boolean valid = tKDeviceOpenTimeService.updateCommIpInfo(map,login_user);
		if(valid){
			result.setMsg("修改成功");
			result.setSuccess(true);
		}else{
			result.setMsg("修改失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public void delCommIpInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String ids = request.getParameter("ids");
		JsonResult result = new JsonResult();
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		boolean valid = tKDeviceOpenTimeService.delCommIpInfo(ids,login_user);
		if(valid){
			result.setMsg("删除成功");
			result.setSuccess(true);
		}else{
			result.setMsg("删除失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
}
				/*	
					// 成功下发配置楼层到设备并且更新数据库
					if(0!=resVal){
					if (1 == resVal) {
						try {
							if (tKDeviceOpenTimeService.cleanConfigFloor(floors,
									login_user)) {
								resVal = 11;
							} else {
								resVal = 12;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}else{
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				}

					// 成功下发楼层开放时区到设备并且插入数据库
					if(0!=resVal2){
					if (1 == resVal2) {
						boolean res = false;
						List<Floor> list = new ArrayList<Floor>();
					for (int k = 1; k <= 48; k++) {
							Floor floor = new Floor();
							floor.setFloor_num("" + k);
							floor.setDevice_num(device_num);
							floor.setStart_time1("00:00");
							floor.setEnd_time1("00:00");
							floor.setStart_time2("00:00");
							floor.setEnd_time2("00:00");
							floor.setStart_time3("00:00");
							floor.setEnd_time3("00:00");
							floor.setStart_time4("00:00");
							floor.setEnd_time4("00:00");
							list.add(floor);
					}
							try {
								if (tKDeviceOpenTimeService.cleanFloorOpenArea(
										list, login_user)) {//更新数据库成功
									res = true;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						if (res) {
							resVal2 = 11;
						} else {
							resVal2 = 12;
						}
						
					}else{
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				}
					
					if(0!=resVal3){
					// 成功下发节假日到设备并且更新数据库
					if (1 == resVal3) {
						try {
							if (tKDeviceOpenTimeService.cleanHoliday(device_num,
									login_user)) {
								resVal3 = 11;
							} else {
								resVal3 = 12;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}else{
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				}

					if(0 != resVal5){
					// 成功下发动作参数到设备并且更新数据库
					if (1 == resVal5) {
						Map map = new HashMap();
						map.put("multi_opt_interval", 5);
						map.put("single_opt_interval", 2);
						map.put("visitor_opt_interval", 1);
						map.put("device_up", 1);
						map.put("device_down", 1);
						map.put("device_open", 0);
						map.put("device_close", 0);
						map.put("device_num", device_num);
						try {
							if (tKDeviceOpenTimeService.cleanEleMoveParam(map,
									login_user)) {
								resVal5 = 11;
							} else {
								resVal5 = 12;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
					}
					// 成功下发电梯开放时区到设备并且更新数据库
					if(0 != resVal1){
					if (1 == resVal1) {
						Map map = new HashMap();
						map.put("device_num", device_num);
						try {
							if(tKDeviceOpenTimeService.resetEleOpenTime(map,
									login_user)){
								resVal1 = 11;
							} else {
								resVal1 = 12;
							}	
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}else{
						result.setMsg("清除失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
						return;
					}
				}

					if (12 == resVal || 12 == resVal1 || 12 == resVal2
							|| 12 == resVal3 || 12 == resVal5) {
						result.setMsg("清除失败！下发到设备成功，保存失败");
						result.setSuccess(false);
						printHttpServletResponse(GsonUtil.toJson(result));
					} else {
						result.setMsg("清除成功");
						result.setSuccess(true);
						printHttpServletResponse(GsonUtil.toJson(result));
					}*/



/*
 * if(1==resVal && 1==resVal1 && 1==resVal2 && 1==resVal3 && 1==resVal4 &&
 * 1==resVal5){//下发全部设备成功
 * if(tKDeviceOpenTimeService.cleanConfigFloor(floors,login_user)){
 * System.out.println("清除配置楼层成功"); } }else{ result.setMsg("清除设备失败");
 * result.setSuccess(false); printHttpServletResponse(GsonUtil.toJson(result));
 * }
 */

/*
 * for(int j=1;j<=8;j++){ OpenTimeZone openTimeZone = new OpenTimeZone();
 * openTimeZone.setWeekDay(j); for(int k=1;k<=5;k++){//一个form有五个时段 TimeZone
 * timeZone = new TimeZone(); timeZone.setStartHour("00");
 * timeZone.setStartMinute("00"); timeZone.setEndHour("00");
 * timeZone.setEndMinute("00"); openTimeZone.getTimeZoneList().add(timeZone); }
 * Map map = new HashMap(); map.put("device_num", device_num);
 * map.put("week_day", j);
 * if(tKDeviceOpenTimeService.updateDeviceOpenTime(openTimeZone
 * ,device,login_user)&&tKDeviceOpenTimeService.resetEleOpenTime(map,
 * login_user)){ resVal1 = 1; }else{ resVal1 = 2; } }
 */

/*
 * String strSucc = ""; String strFail = ""; String str = "";
 * if(1==resVal&&1==resVal1&&1==resVal2&&1==resVal3&&resVal4 ==1){
 * result.setSuccess(true); result.setMsg("清除成功");
 * printHttpServletResponse(GsonUtil.toJson(result)); return; }else
 * if(2==resVal&&2==resVal1&&2==resVal2&&2==resVal3&&2==resVal4){//全部失败
 * result.setSuccess(false); result.setMsg("清除失败");
 * printHttpServletResponse(GsonUtil.toJson(result)); return; }else{
 * if(2==resVal){ strFail+=" 配置楼层"; }else if(1==resVal){ strSucc+=" 配置楼层"; }
 * 
 * if(2==resVal1) { strFail+=" 电梯开放时区"; }else if(1==resVal1){
 * strSucc+=" 电梯开放时区"; }
 * 
 * if(2==resVal2){ strFail+=" 楼层开放时区"; }else if(1==resVal2){ strSucc+=" 楼层开放时区";
 * }
 * 
 * if(2==resVal3){ strFail+=" 节假日"; } else if(1==resVal3){ strSucc+=" 节假日"; }
 * if(2==resVal4){ strFail+=" 设备权限"; }else if(1==resVal4){ strSucc+=" 设备权限"; }
 * 
 * if("".equals(strFail)){ str =
 * "清除<font color='green'>"+strSucc+"</font>"+" 成功"; result.setSuccess(true);
 * }else if("".equals(strSucc)){ str =
 * "清除<font color='red'>"+strFail+"</font>"+" 失败 "; result.setSuccess(false);
 * }else{ str =
 * "清除"+"<font color='red'>"+strFail+"</font> 失败 "+"，<font color='green'>"
 * +strSucc+"</font>"+" 成功"; result.setSuccess(false); }
 * 
 * result.setMsg(str); printHttpServletResponse(GsonUtil.toJson(result));
 */

