package com.kuangchi.sdd.consumeConsole.device.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.device.model.DataDown;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceConsumeSet;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGood;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.consumeConsole.device.model.ParameterDownResponse;
import com.kuangchi.sdd.consumeConsole.device.model.PersonCardTask;
import com.kuangchi.sdd.consumeConsole.device.model.ResultMsg;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 设备管理页面
 * 
 * @author minting.he
 * 
 */
@Controller("cDeviceAction")
public class CDeviceAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name = "cDeviceService")
	IDeviceService deviceService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 根据条件查询设备信息
	 * 
	 * @author minting.he
	 */
	public void getDeviceByParam() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Device deviceSearch = GsonUtil.toBean(beanObject, Device.class);
		if (BeanUtil.isNotEmpty(deviceSearch)) {
			if (!EmptyUtil.atLeastOneIsEmpty(deviceSearch.getDevice_name())) {
				deviceSearch.setDevice_name(deviceSearch.getDevice_name()
						.trim().replace("<", "&lt").replace(">", "&gt"));
			}
		}
		deviceSearch.setPage(Integer.parseInt(request.getParameter("page")));
		deviceSearch.setRows(Integer.parseInt(request.getParameter("rows")));
		Grid<Device> deviceGrid = deviceService.getDeviceByParam(deviceSearch);
		printHttpServletResponse(GsonUtil.toJson(deviceGrid));
	}

	/**
	 * 加载查看设备信息
	 * 
	 * @author minting.he
	 * @return
	 */
	public String toViewDialog() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		Device device = deviceService.selDeviceById(id);
		request.setAttribute("device", device);
		return "success";
	}

	/**
	 * 根据id查询设备信息
	 * 
	 * @author minting.he
	 */
	public void selDeviceById() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		Device device = deviceService.selDeviceById(id);
		printHttpServletResponse(GsonUtil.toJson(device));
	}

	/**
	 * 根据设备编号获取关联信息
	 */
	public void selByDeviceNum() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		DeviceGood deviceGood = deviceService.selByDeviceNum(device_num);
		printHttpServletResponse(GsonUtil.toJson(deviceGood));
	}

	/**
	 * 设备编号是否存在
	 * 
	 * @author minting.he
	 */
	public void validNum() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String device_num = request.getParameter("device_num");
		Device device = new Device();
		device.setDevice_num(device_num);
		if (!EmptyUtil.atLeastOneIsEmpty(id)) {
			device.setId(id);
		}
		Integer count = deviceService.validNum(device);
		printHttpServletResponse(GsonUtil.toJson(count));
	}

	/**
	 * 新增设备
	 * 
	 * @author minting.he
	 */
	public void insertDevice() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("dataForm");
		Device pageBean = GsonUtil.toBean(beanObject, Device.class);
		String good_or_type_num = request.getParameter("good_or_type_num");
		String is_type = request.getParameter("is_type");
		JsonResult result = new JsonResult();
		if (BeanUtil.isEmpty(pageBean) || deviceService.validNum(pageBean) != 0
				|| EmptyUtil.atLeastOneIsEmpty(is_type)) {
			result.setSuccess(false);
			result.setMsg("新增失败，数据不合法");
		} else if (!"2".equals(is_type)
				&& EmptyUtil.atLeastOneIsEmpty(good_or_type_num)) { // 商品或商品类别
			result.setSuccess(false);
			result.setMsg("新增失败，数据不合法");
		} else {
			pageBean.setDevice_name(pageBean.getDevice_name().trim()
					.replace("<", "&lt").replace(">", "&gt"));
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceService.insertDevice(pageBean, is_type,
						good_or_type_num, create_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("新增成功");
				} else {
					result.setSuccess(false);
					result.setMsg("新增失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 修改设备
	 * 
	 * @author minting.he
	 */
	public void updateDevice() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String beanObject = request.getParameter("dataForm");
		Device device = GsonUtil.toBean(beanObject, Device.class);
		String good_or_type_num = request.getParameter("good_or_type_num");
		String is_type = request.getParameter("is_type");
		if (BeanUtil.isEmpty(device) || deviceService.validNum(device) != 0
				|| EmptyUtil.atLeastOneIsEmpty(is_type)) {
			result.setSuccess(false);
			result.setMsg("修改失败，数据不合法");
		} else if (!"2".equals(is_type)
				&& EmptyUtil.atLeastOneIsEmpty(good_or_type_num)) {
			result.setSuccess(false);
			result.setMsg("修改失败，数据不合法");
		} else {
			device.setDevice_name(device.getDevice_name().trim()
					.replace("<", "&lt").replace(">", "&gt"));
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceService.updateDevice(device, is_type,
						good_or_type_num, create_user);
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
	 * 删除设备
	 * 
	 * @author minting.he
	 */
	public void deleteDevice() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String nums = request.getParameter("nums");
		if (EmptyUtil.atLeastOneIsEmpty(nums)) {
			result.setSuccess(false);
			result.setMsg("删除失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String create_user = loginUser.getYhMc();
				boolean r = deviceService.deleteDevice(nums, create_user);
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
	 * 设备类型编号是否被引用
	 * 
	 * @author minting.he
	 */
	public void typeNumIsExist() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_type_num = request.getParameter("device_type_num");
		if (EmptyUtil.atLeastOneIsEmpty(device_type_num)) {
			result.setSuccess(false);
			result.setMsg("数据不合法");
		} else {
			Integer count = deviceService.typeNumIsExist(device_type_num);
			printHttpServletResponse(GsonUtil.toJson(count));
		}
	}

	public void printTree(Tree tree) {
		List<Tree> childrenList = tree.getChildren();
		for (Tree treeNode : childrenList) {
			printTree(treeNode);
		}
	}

	/**
	 * 获取设备树、设备的树
	 * 
	 * @author minting.he
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

	/**
	 * 获取设备组的树
	 * 
	 * @author minting.he
	 */
	public void onlyGroupTree() {
		Tree onlyGroup = deviceService.onlyGroupTree();
		printTree(onlyGroup);
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(onlyGroup));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}

	/**
	 * 根据设备组号显示组信息
	 * 
	 * @author minting.he
	 */
	public void getXFGroupInfoByNum() {
		HttpServletRequest request = getHttpServletRequest();
		String group_num = request.getParameter("group_num");
		DeviceGroup deviceGroup = deviceService.getXFGroupInfoByNum(group_num);
		printHttpServletResponse(GsonUtil.toJson(deviceGroup));
	}

	/**
	 * 修改设备组信息
	 * 
	 * @author minting.he
	 */
	public void updateXFDeviceGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String beanObject = request.getParameter("dataForm");
		DeviceGroup deviceGroup = GsonUtil
				.toBean(beanObject, DeviceGroup.class);
		if (BeanUtil.isEmpty(deviceGroup)) {
			result.setSuccess(false);
			result.setMsg("修改失败，数据不合法");
		} else {
			deviceGroup.setGroup_name(deviceGroup.getGroup_name().trim()
					.replace("<", "&lt").replace(">", "&gt"));
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				boolean r = deviceService.updateXFDeviceGroup(deviceGroup,
						login_user);
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
	 * 新增设备组
	 * 
	 * @author minting.he
	 */
	public void insertXFDeviceGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String beanObject = request.getParameter("dataForm");
		DeviceGroup deviceGroup = GsonUtil
				.toBean(beanObject, DeviceGroup.class);
		if (BeanUtil.isEmpty(deviceGroup)) {
			result.setSuccess(false);
			result.setMsg("新增失败，数据不合法");
		} else {
			deviceGroup.setGroup_name(deviceGroup.getGroup_name().trim()
					.replace("<", "&lt").replace(">", "&gt"));
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (loginUser == null) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				deviceGroup.setCreate_user(login_user);
				boolean r = deviceService.insertXFDeviceGroup(deviceGroup,
						login_user);
				if (r) {
					result.setSuccess(true);
					result.setMsg("新增成功");
				} else {
					result.setSuccess(false);
					result.setMsg("新增失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 删除设备组
	 * 
	 * @author minting.he
	 */
	public void deleteXFDeviceGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String group_num = request.getParameter("group_num");
		if (EmptyUtil.isEmpty(group_num)) {
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
				boolean r = deviceService.deleteXFDeviceGroup(group_num,
						login_user);
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
	 * 修改设备关联的设备组
	 * 
	 * @author minting.he
	 */
	public void changeDeviceGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_group_num = request.getParameter("device_group_num");
		String device_num = request.getParameter("device_num");
		if (BeanUtil.isEmpty(device_num)) {
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
				boolean r = deviceService.changeDeviceGroup(device_group_num,
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
	 * 修改设备组的父设备组
	 * 
	 * @author minting.he
	 */
	public void changeParentGroup() {
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String parent_group_num = request.getParameter("parent_group_num");
		String group_num = request.getParameter("group_num");
		if (BeanUtil.isEmpty(group_num)) {
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
				boolean r = deviceService.changeParentGroup(parent_group_num,
						group_num, login_user);
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
	 * 点击树查看设备
	 * 
	 * @author minting.he
	 */
	public void getDeviceByTree() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Device deviceSearch = GsonUtil.toBean(beanObject, Device.class);
		if (BeanUtil.isNotEmpty(deviceSearch)) {
			if (!EmptyUtil.atLeastOneIsEmpty(deviceSearch.getDevice_name())) {
				deviceSearch.setDevice_name(deviceSearch.getDevice_name()
						.trim().replace("<", "&lt").replace(">", "&gt"));
			}
		}
		deviceSearch.setPage(Integer.parseInt(request.getParameter("page")));
		deviceSearch.setRows(Integer.parseInt(request.getParameter("rows")));
		Grid<Device> deviceGrid = deviceService.getDeviceByTree(deviceSearch);
		printHttpServletResponse(GsonUtil.toJson(deviceGrid));
	}

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
	 * 新增下发名单任务
	 * 
	 * @author minting.he
	 */
	public void insertNameTask() {
		HttpServletRequest request = getHttpServletRequest();
		String cardStr = request.getParameter("cardStr");
		String nums = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(nums)) {
			result.setSuccess(false);
			result.setMsg("下发失败，数据不合法");
		} else {
			List<PersonCardTask> pctList = new ArrayList<PersonCardTask>();
			if (EmptyUtil.atLeastOneIsEmpty(cardStr)) { // 所有卡
				List<String> cardList = deviceService.getAllCardNum();
				for (int i = 0; i < cardList.size(); i++) {
					PersonCardTask model = new PersonCardTask();
					String card_num = cardList.get(i);
					model.setCard_num(card_num);
					pctList.add(model);
				}
			} else { // 选择卡
				Gson gson = new Gson();
				List<Map> cardList = gson.fromJson(encodeToJsonStr(cardStr),
						new ArrayList<LinkedTreeMap>().getClass()); // 获取下发的卡片信息
				for (int i = 0; i < cardList.size(); i++) {
					PersonCardTask model = new PersonCardTask();
					String card_num = (String) cardList.get(i).get("card_num");
					model.setCard_num(card_num);
					pctList.add(model);
				}
			}
			String[] device_num = nums.split(",");
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				int existSucc = 1;
				int existFail = 1;
				for (int i = 0; i < device_num.length; i++) { // 分别对多个设备下发
					if (device_num[i] != null && !"".equals(device_num[i])) {
						for (int j = 0; j < pctList.size(); j++) {
							pctList.get(j).setDevice_num(device_num[i]);
						}
						if (deviceService.insertPCTask(pctList, login_user)) {
							existSucc = 0;
						} else {
							existFail = 0;
						}
					}
				}
				if (existSucc == 0 && existFail == 1) {
					result.setSuccess(true);
					result.setMsg("操作成功，下发结果请查看已下发名单");
				} else if (existSucc == 1 && existFail == 0) {
					result.setSuccess(false);
					result.setMsg("操作失败");
				} else if (existSucc == 0 && existFail == 0) {
					result.setSuccess(false);
					result.setMsg("部分设备操作失败，下发结果请查看已下发名单");
				} else {
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 查询设备已下发的名单
	 * 
	 * @author minting.he
	 */
	public void getNameByDevice() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		if (BeanUtil.isNotEmpty(map)) {
			if (!EmptyUtil.atLeastOneIsEmpty(map.get("staff_no").toString())) {
				map.put("staff_no", map.get("staff_no").toString().trim()
						.replace("<", "&lt").replace(">", "&gt"));
			}
			if (!EmptyUtil.atLeastOneIsEmpty(map.get("staff_name").toString())) {
				map.put("staff_name", map.get("staff_name").toString().trim()
						.replace("<", "&lt").replace(">", "&gt"));
			}
		}
		map.put("skip", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid<Map> grid = deviceService.getNameByDevice(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	/**
	 * 根据mac地址查询设备消费参数xuewen.deng
	 */
	public void getParamByDevNum() {
		HttpServletRequest request = getHttpServletRequest();
		String DevNum = request.getParameter("DevNum");
		String req = request.getParameter("req");
		ResultMsg result = deviceService.getParamByMac(DevNum, req);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 设置设备消费参数xuewen.deng
	 */
	public void setTerminalParam() {
		HttpServletRequest request = getHttpServletRequest();
		ResultMsg result = new ResultMsg();
		ParameterDownResponse paramDown = new ParameterDownResponse();
		DataDown dataDown = new DataDown();
		try {
			paramDown.setMachine(Integer.valueOf(request
					.getParameter("machine")));
			dataDown.setLimit(Integer.parseInt(request.getParameter("limit")));
			dataDown.setMoney(Integer.parseInt(request.getParameter("money")));
			dataDown.setPassword(Integer.parseInt(
					"" + (Integer.parseInt(request.getParameter("password"))),
					16));
			dataDown.setConfirm(Integer.parseInt(request
					.getParameter("confirm")));
			dataDown.setMultiUse(Integer.parseInt(request
					.getParameter("multiUse")));
			dataDown.setTimeLimit(Integer.parseInt(request
					.getParameter("timeLimit")));
			dataDown.setOnOffLineWay(Integer.parseInt(request
					.getParameter("onOffLineWay")));
			String[] meal1Start = request.getParameter("meal1Start").split(":");
			String[] meal1End = request.getParameter("meal1End").split(":");
			String[] meal2Start = request.getParameter("meal2Start").split(":");
			String[] meal2End = request.getParameter("meal2End").split(":");
			String[] meal3Start = request.getParameter("meal3Start").split(":");
			String[] meal3End = request.getParameter("meal3End").split(":");
			String[] meal4Start = request.getParameter("meal4Start").split(":");
			String[] meal4End = request.getParameter("meal4End").split(":");
			int[] meal1 = new int[4];// 餐次起止时间 4
			meal1[0] = Integer.parseInt(meal1Start[0]);
			meal1[1] = Integer.parseInt(meal1Start[1]);
			meal1[2] = Integer.parseInt(meal1End[0]);
			meal1[3] = Integer.parseInt(meal1End[1]);
			dataDown.setMeal1(meal1);

			int[] meal2 = new int[4];// 餐次起止时间 4
			meal2[0] = Integer.parseInt(meal2Start[0]);
			meal2[1] = Integer.parseInt(meal2Start[1]);
			meal2[2] = Integer.parseInt(meal2End[0]);
			meal2[3] = Integer.parseInt(meal2End[1]);
			dataDown.setMeal2(meal2);

			int[] meal3 = new int[4];// 餐次起止时间 4
			meal3[0] = Integer.parseInt(meal3Start[0]);
			meal3[1] = Integer.parseInt(meal3Start[1]);
			meal3[2] = Integer.parseInt(meal3End[0]);
			meal3[3] = Integer.parseInt(meal3End[1]);
			dataDown.setMeal3(meal3);

			int[] meal4 = new int[4];// 餐次起止时间 4
			meal4[0] = Integer.parseInt(meal4Start[0]);
			meal4[1] = Integer.parseInt(meal4Start[1]);
			meal4[2] = Integer.parseInt(meal4End[0]);
			meal4[3] = Integer.parseInt(meal4End[1]);
			dataDown.setMeal4(meal4);

			int[] timeLimitMoney = new int[4];// 4个餐次时段的定额值
			timeLimitMoney[0] = Integer.parseInt(request
					.getParameter("timeLimitMoney1"));
			timeLimitMoney[1] = Integer.parseInt(request
					.getParameter("timeLimitMoney2"));
			timeLimitMoney[2] = Integer.parseInt(request
					.getParameter("timeLimitMoney3"));
			timeLimitMoney[3] = Integer.parseInt(request
					.getParameter("timeLimitMoney4"));
			dataDown.setTimeLimitMoney(timeLimitMoney);

			dataDown.setGroupsuport(Integer.parseInt(request
					.getParameter("groupsuport")));// 终端支持的卡分组 1

			int[] goodNumMoney = new int[10]; // 菜号金额 30
			goodNumMoney[0] = Integer.parseInt(request
					.getParameter("goodNumMoney0"));
			goodNumMoney[1] = Integer.parseInt(request
					.getParameter("goodNumMoney1"));
			goodNumMoney[2] = Integer.parseInt(request
					.getParameter("goodNumMoney2"));
			goodNumMoney[3] = Integer.parseInt(request
					.getParameter("goodNumMoney3"));
			goodNumMoney[4] = Integer.parseInt(request
					.getParameter("goodNumMoney4"));
			goodNumMoney[5] = Integer.parseInt(request
					.getParameter("goodNumMoney5"));
			goodNumMoney[6] = Integer.parseInt(request
					.getParameter("goodNumMoney6"));
			goodNumMoney[7] = Integer.parseInt(request
					.getParameter("goodNumMoney7"));
			goodNumMoney[8] = Integer.parseInt(request
					.getParameter("goodNumMoney8"));
			goodNumMoney[9] = Integer.parseInt(request
					.getParameter("goodNumMoney9"));
			dataDown.setGoodNumMoney(goodNumMoney);
			// 设置餐次消费模式
			dataDown.setMeal1Mode(Integer.parseInt(request
					.getParameter("meal1Mode")));
			dataDown.setMeal2Mode(Integer.parseInt(request
					.getParameter("meal2Mode")));
			dataDown.setMeal3Mode(Integer.parseInt(request
					.getParameter("meal3Mode")));
			dataDown.setMeal4Mode(Integer.parseInt(request
					.getParameter("meal4Mode")));

			paramDown.setDataDown(dataDown);
			result = deviceService.setTerminalParam(paramDown);
			printHttpServletResponse(GsonUtil.toJson(result));

		} catch (NumberFormatException e) {

			result.setResult_code("1");
			result.setResult_msg("消费参数转换过程中出错");
			e.printStackTrace();
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 读取任务名单xuewen.deng
	 */
	/*
	 * public void getTask() { HttpServletRequest request =
	 * getHttpServletRequest(); String machine =
	 * request.getParameter("machine"); String pack =
	 * request.getParameter("pack");
	 * 
	 * List<PersonCardTask> taskList1 = new ArrayList<PersonCardTask>();
	 * 
	 * try { Integer maxPack = deviceService.getMaxPack(machine);// 获取最大的包号
	 * Integer tryTimes = deviceService.getTry_times(maxPack, machine); if
	 * (tryTimes == null) tryTimes = 0;// 防止出现空值 if (maxPack <
	 * Integer.valueOf(pack)) { if (maxPack != null && maxPack != -1) {
	 * deviceService.deletePersonCardTask(machine, String.valueOf(maxPack),
	 * "1");// 将数据删除，放入历史表和改为“执行成功”状态 } // 开始下发 taskList1 =
	 * deviceService.getPersonCardTask(machine);// 获取名单，根据卡号和创建时间排序
	 * List<PersonCardTask> taskList2 = new ArrayList<PersonCardTask>();//
	 * 用来保存截取的结果（<=8条记录） if (taskList1 != null && taskList1.size() > 0) { int i
	 * = 0; for (PersonCardTask pcT : taskList1) { if (i < 8) {
	 * taskList2.add(pcT); i++; } else { break; } } }
	 * 
	 * printHttpServletResponse(GsonUtil.toJson(taskList2));// 返回<=8条名单
	 * 
	 * if (taskList2 != null && taskList2.size() > 0) {// 拼装id字符串 boolean flag =
	 * true; String idStr = ""; for (PersonCardTask pcTask : taskList2) { if
	 * (flag) { idStr = pcTask.getId(); flag = false; } else { idStr = idStr +
	 * "," + pcTask.getId(); } }
	 * 
	 * deviceService.updateTaskState(idStr, pack);// 将数据改为“正在执行”和修改包号 if
	 * (taskList1 == null || taskList1.size() < 8) {// 若拿到的条数不足8，则预先将其状态置为成功
	 * deviceService.deletePersonCardTask(machine, String.valueOf(pack), "1");//
	 * 将数据改为“删除状态”和“执行成功”状态 } } } else if (tryTimes != null && tryTimes <= 3)
	 * {// 判断尝试次数，如果有次数，则继续尝试将这些名单下发
	 * 
	 * List<PersonCardTask> taskList3 = deviceService
	 * .getRunningTask(machine);// 选出有效的，并且正在执行的记录 List<PersonCardTask>
	 * taskList4 = new ArrayList<PersonCardTask>();// 用来保存截取的结果（<=8条记录）
	 * 
	 * if (taskList3 != null && taskList3.size() > 0) { int i = 0; for
	 * (PersonCardTask pcT : taskList3) { if (i < 8) { taskList4.add(pcT); i++;
	 * } else { break; } } }
	 * 
	 * printHttpServletResponse(GsonUtil.toJson(taskList4));// 返回<=8条名单
	 * 
	 * // 将尝试次数加一 if (taskList4 != null && taskList4.size() > 0) {// 拼装id字符串
	 * boolean flag = true; String idStr = ""; for (PersonCardTask pcTask :
	 * taskList4) { if (flag) { idStr = pcTask.getId(); flag = false; } else {
	 * idStr = idStr + "," + pcTask.getId(); } }
	 * deviceService.updateTaskTrytimes(idStr, String.valueOf(tryTimes + 1));//
	 * 尝试次数加一 } } else {// 如果没有尝试次数，则判为执行失败
	 * 
	 * List<PersonCardTask> taskList5 = deviceService
	 * .getRunningTask(machine);// 选出有效的，并且正在执行的记录 List<PersonCardTask>
	 * taskList6 = new ArrayList<PersonCardTask>();// 用来保存截取的结果（<=8条记录）
	 * 
	 * if (taskList5 != null && taskList5.size() > 0) { int i = 0; for
	 * (PersonCardTask pcT : taskList5) { if (i < 8) { taskList6.add(pcT); i++;
	 * } else { break; } } } // 将记录判为失败 if (taskList6 != null &&
	 * taskList6.size() > 0) {// 拼装id字符串 boolean flag = true; String idStr = "";
	 * for (PersonCardTask pcTask : taskList6) { if (flag) { idStr =
	 * pcTask.getId(); flag = false; } else { idStr = idStr + "," +
	 * pcTask.getId(); } } if (maxPack != null) {
	 * deviceService.deletePersonCardTask(machine, String.valueOf(maxPack),
	 * "3");// 将数据改为“删除状态”和“执行失败”状态 } } } } catch (NumberFormatException e) {
	 * e.printStackTrace();
	 * printHttpServletResponse(GsonUtil.toJson(taskList1));// 返回空名单，表示停止下发 }
	 * 
	 * }
	 */
	// 获取设备对应的餐次限制次数列表
	public void getMealLimitTimesList() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");

		List<DeviceConsumeSet> mealLimitTimesList = deviceService
				.getMealLimitTimesList(device_num);
		printHttpServletResponse(GsonUtil.toJson(mealLimitTimesList));
	}

	/**
	 * 读取任务名单xuewen.deng
	 */
	public void getTask() {
		List<PersonCardTask> taskList1 = new ArrayList<PersonCardTask>();
		List<PersonCardTask> taskList2 = new ArrayList<PersonCardTask>();

		List<String> onlineDeviceList = deviceService.getOnlineDevice();// 获取在线的设备名单
		// 截取<=8条名单
		try {
			taskList1 = deviceService.getSomeTask(onlineDeviceList);// 获取名单，根据创建时间排序
			if (taskList1 != null && taskList1.size() > 0) {
				int i = 0;
				for (PersonCardTask pcT : taskList1) {
					if (i < 8) {
						taskList2.add(pcT);
						i++;
					} else {
						break;
					}
				}
			}
			printHttpServletResponse(GsonUtil.toJson(taskList2));// 返回<=100条名单

		} catch (Exception e) {
			e.printStackTrace();
			printHttpServletResponse(GsonUtil.toJson(taskList2));// 返回0条名单
		}

	}

	// 删除设备设置的餐次限制次数
	public void delMealLimitTimes() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String id = request.getParameter("id");
		JsonResult result = new JsonResult();
		boolean flag = deviceService.delMealLimitTimes(id, device_num);
		if (flag) {
			result.setSuccess(true);
			result.setMsg("删除成功");
		} else {
			result.setSuccess(false);
			result.setMsg("删除失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 查询餐次信息
	public void getMealNumList() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		List<Map> list = new ArrayList<Map>();
		List<MealModel> mealList = deviceService.getMealNum(device_num);
		for (MealModel mm : mealList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", mm.getMeal_num());
			map.put("TEXT", mm.getMeal_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	// 通过设备编号查询设备名称 guofei.lian 2016-09-22
	public void getDeviceNameByNum() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		Device device = deviceService.selectDeviceByNum(device_num);
		printHttpServletResponse(GsonUtil.toJson(device));
	}

	// 新增餐次限制次数前，判断是否有可用的餐次
	public void checkMealsEqualDeviceMeal() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		boolean flag = deviceService.checkMealsEqualDeviceMeal(device_num);
		if (flag) {
			result.setMsg("");
			result.setSuccess(true);
		} else {
			result.setMsg("没有剩余餐次可用");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 新增设备设置餐次限制次数
	public void addNewMealLimitTimes() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		DeviceConsumeSet deviceConsumeSet = GsonUtil.toBean(data,
				DeviceConsumeSet.class);
		JsonResult result = new JsonResult();
		boolean flag = deviceService.addNewMealLimitTime(deviceConsumeSet);
		if (flag) {
			result.setMsg("新增成功");
			result.setSuccess(true);
		} else {
			result.setMsg("新增失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 跳转到修改餐次限制次数页面
	public String getDeviceConsumeSetById() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String device_num = request.getParameter("device_num");
		DeviceConsumeSet deviceConsumeSet = deviceService
				.getDeviceConsumeSetById(id, device_num);
		request.setAttribute("deviceConsumeSet", deviceConsumeSet);
		return "success";
	}

	// 修改餐次限制次数
	public void editMealLimitTimes() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		DeviceConsumeSet deviceConsumeSet = GsonUtil.toBean(data,
				DeviceConsumeSet.class);
		JsonResult result = new JsonResult();
		boolean flag = deviceService.modifyMealLimitTime(deviceConsumeSet);
		if (flag) {
			result.setMsg("修改成功");
			result.setSuccess(true);
		} else {
			result.setMsg("修改失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	public void getSomeTask() {

		List<PersonCardTask> taskList1 = new ArrayList<PersonCardTask>();
		List<PersonCardTask> taskList2 = new ArrayList<PersonCardTask>();
		// 获取在线的设备名单
		List<String> onlineDeviceList = deviceService.getOnlineDevice();
		// 截取<=5条名单
		try {
			taskList1 = deviceService.getSomeTask(onlineDeviceList);// 获取名单，根据创建时间排序
			if (taskList1 != null && taskList1.size() > 0) {
				int i = 0;
				for (PersonCardTask pcT : taskList1) {
					if (i < 5) {
						taskList2.add(pcT);
						i++;
					} else {
						break;
					}
				}
			}
			for (PersonCardTask task : taskList2) {// 去数据库更新任务运行状态
				deviceService.updateRunState(task.getId(), "1");
			}
			/*
			 * if (taskList2 != null && taskList2.size() > 0) {// 拼装id字符串
			 * deviceService.deletePCTask(taskList2.size());// 将发送过的数据删除 }
			 */
			printHttpServletResponse(GsonUtil.toJson(taskList2));// 返回<=5条名单

		} catch (Exception e) {
			e.printStackTrace();
			printHttpServletResponse(GsonUtil.toJson(taskList2));// 返回0条名单
		}

	}

	public void insertPersonCardTaskHistoryList() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		PersonCardTask personCardTask = GsonUtil.toBean(data,
				PersonCardTask.class);
		personCardTask.setBalance(personCardTask.getBalance().divide(
				new BigDecimal("100")));// 将消费机里拿到的余额除以100
		if (personCardTask != null) {
			if (personCardTask.getSuccess_flag() == 0) {// 如果下发成功，更新尝试次数，插入历史表，直接删除
				boolean result = deviceService.insertToNameList(personCardTask);
				if (result) {
					personCardTask
							.setTry_times(personCardTask.getTry_times() == null ? 0
									: (personCardTask.getTry_times() + 1));
					deviceService.updateTryTime(personCardTask);// 更新尝试次数
					boolean result2 = deviceService
							.insertToHistory(personCardTask);// 插入历史表
					if (result2) {
						deviceService.deletePCTask(personCardTask);// 删除任务
					}
				}
			} else {
				if (personCardTask.getTry_times() != null
						&& personCardTask.getTry_times() >= 3) {// 判断是否成功插入历史表并且尝试次数>=3
					personCardTask
							.setTry_times(personCardTask.getTry_times() == null ? 1
									: (personCardTask.getTry_times() + 1));
					deviceService.updateTryTime(personCardTask);
					boolean result2 = deviceService
							.insertToHistory(personCardTask);// 插入历史表
					if (result2) {
						deviceService.deletePCTask(personCardTask);
					}
				} else {
					personCardTask
							.setTry_times(personCardTask.getTry_times() == null ? 0
									: (personCardTask.getTry_times() + 1));
					deviceService.updateTryTime(personCardTask);
				}
			}
		}
	}

	public void compareIP() {
		HttpServletRequest request = getHttpServletRequest();
		String cronstr = request.getParameter("cron");
		int count = 0;
		if (cronstr != null) {
			Cron cron = GsonUtil.toBean(cronstr, Cron.class);
			count = deviceService.compareIP(cron);
		}
		printHttpServletResponse(count);// 返回0条名单
	}

	/*
	 * public void updateTaskRunState() { boolean r =
	 * deviceService.updateRunState(null, "0"); if (r) {
	 * printHttpServletResponse(0); } else { printHttpServletResponse(1); } }
	 */

	/**
	 * 复制设备设置参数
	 * 
	 * @author minting.he
	 * @param r
	 *            0 成功 1 失败 2 获取终端失败
	 */
	public void copyParam() {
		JsonResult result = new JsonResult();
		try {
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag"); // 0 终端设备 1 消费次数 2 已下发名单
			String device_num = request.getParameter("device_num");
			String nums = request.getParameter("nums"); // 目标设备
			if (EmptyUtil.atLeastOneIsEmpty(flag, device_num, nums)) {
				result.setSuccess(false);
				result.setMsg("复制失败，数据不合法");
			} else {
				User loginUser = (User) request.getSession().getAttribute(
						GlobalConstant.LOGIN_USER);
				if (BeanUtil.isEmpty(loginUser)) {
					result.setSuccess(false);
					result.setMsg("操作失败，请先登录");
				} else {
					String login_user = loginUser.getYhMc();
					result = deviceService.copyParam(flag, device_num, nums,
							login_user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}

	/**
	 * 删除已下发的名单
	 * 
	 * @author minting.he
	 */
	public void deleteNameList() {
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String card = request.getParameter("card");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(device_num, card)) {
			result.setSuccess(false);
			result.setMsg("删除失败，数据不合法");
		} else {
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				String[] card_nums = card.split(",");
				List<PersonCardTask> pctList = new ArrayList<PersonCardTask>();
				for (int i = 0; i < card_nums.length; i++) {
					PersonCardTask pc = new PersonCardTask();
					pc.setDevice_num(device_num);
					pc.setCard_num(card_nums[i]);
					pctList.add(pc);
				}
				if (deviceService.deleteNameList(pctList, login_user)) {
					result.setSuccess(true);
					result.setMsg("操作成功，可到“未完成任务”页面查看等待任务");
				} else {
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 新增下发名单任务
	 * 
	 * @author minting.he
	 */
	public void insertNameTask2() {
		HttpServletRequest request = getHttpServletRequest();
		String cardStr = request.getParameter("cardStr");
		String nums = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(cardStr, nums)) {
			result.setSuccess(false);
			result.setMsg("下发失败，数据不合法");
		} else {
			List<PersonCardTask> pctList = new ArrayList<PersonCardTask>();
			String[] cardList = cardStr.split(",");
			for (int i = 0; i < cardList.length; i++) {
				PersonCardTask model = new PersonCardTask();
				String card_num = cardList[i].toString();
				model.setCard_num(card_num);
				pctList.add(model);
			}
			String[] device_num = nums.split(",");
			User loginUser = (User) request.getSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if (BeanUtil.isEmpty(loginUser)) {
				result.setSuccess(false);
				result.setMsg("操作失败，请先登录");
			} else {
				String login_user = loginUser.getYhMc();
				int existSucc = 1;
				int existFail = 1;
				for (int i = 0; i < device_num.length; i++) { // 分别对多个设备下发
					if (device_num[i] != null && !"".equals(device_num[i])) {
						for (int j = 0; j < pctList.size(); j++) {
							pctList.get(j).setDevice_num(device_num[i]);
						}
						if (deviceService.insertPCTask(pctList, login_user)) {
							existSucc = 0;
						} else {
							existFail = 0;
						}
					}
				}
				if (existSucc == 0 && existFail == 1) {
					result.setSuccess(true);
					result.setMsg("操作成功，下发结果请查看已下发名单");
				} else if (existSucc == 1 && existFail == 0) {
					result.setSuccess(false);
					result.setMsg("操作失败");
				} else if (existSucc == 0 && existFail == 0) {
					result.setSuccess(false);
					result.setMsg("部分设备操作失败，下发结果请查看已下发名单");
				} else {
					result.setSuccess(false);
					result.setMsg("操作失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 获取所有commIp
	 * 
	 * @author xuewen.deng
	 */
	public void getAllCommIp() {
		List<Map> commIpMapList = deviceService.getAllCommIp();// 获取所有卡片类型
		List<Map> list = new ArrayList<Map>();
		HttpServletRequest request = getHttpServletRequest();
		for (Map commIpMap : commIpMapList) {
			Map<String, String> map = new HashMap<String, String>();
			String commIpAndPort = commIpMap.get("ip") + ":"
					+ commIpMap.get("port");
			map.put("commIpAndPort", commIpAndPort);
			map.put("commId", commIpMap.get("id") + "");
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
}
