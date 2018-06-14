package com.kuangchi.sdd.consumeConsole.device.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceDoorBean;
import com.kuangchi.sdd.baseConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.cron.dao.ICronDao;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.consumeConsole.device.dao.IDeviceDao;
import com.kuangchi.sdd.consumeConsole.device.model.DataDown;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceConsumeSet;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGood;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.consumeConsole.device.model.ParameterDownResponse;
import com.kuangchi.sdd.consumeConsole.device.model.ParameterUpResponse;
import com.kuangchi.sdd.consumeConsole.device.model.PersonCardTask;
import com.kuangchi.sdd.consumeConsole.device.model.ResultMsg;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.xfComm.service.XfCommService;
import com.kuangchi.sdd.elevatorConsole.tkComm.dao.TkCommDao;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

/**
 * 设备信息维护Service
 * 
 * @author minting.he
 * 
 */
@Transactional
@Service("cDeviceService")
public class CDeviceServiceImpl implements IDeviceService {
	@Resource(name = "tkCommDaoImpl")
	private TkCommDao tkCommDao;
	@Resource(name = "cDeviceDao")
	private IDeviceDao deviceDao;
	@Resource(name = "cronDaoImpl")
	ICronDao cronDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "deviceGroupService")
	private DeviceGroupService deviceGroupService;
	@Resource(name = "xfCommServiceImpl")
	private XfCommService xfCommService;
	@Resource(name = "cDeviceService")
	IDeviceService deviceService;

	@Override
	public Grid<Device> getDeviceByParam(Device device) {
		try {
			Grid<Device> grid = new Grid<Device>();
			List<Device> resultList = deviceDao.getDeviceByParam(device);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(deviceDao.getDeviceByParamCount(device));
			} else {
				grid.setTotal(0);
			}
			return grid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Device selDeviceById(String id) {
		try {
			return deviceDao.selDeviceById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer validNum(Device device) {
		try {
			return deviceDao.validNum(device);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean insertDevice(Device device, String is_type,
			String good_or_type_num, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			Map<String, String> commIpMap = new HashMap<String, String>();
			commIpMap.put("ipAddress", device.getCommIp());
			commIpMap.put("commPort", device.getCommSocket());
			commIpMap.put("description", "");

			try {// 此处加个try，catch，保证即使通讯服务器ip没添加成功，设备也能添加成功
				if (device.getCommIp() != null
						&& device.getCommSocket() != null) {
					if (xfCommService.getXfCommIpCountByIp(commIpMap)) {// 判断是否已经存在此通讯服务器IP
						String commIpId = xfCommService
								.getXfCommIpIdByMap(commIpMap);// 根据ip和端口获取记录id
						device.setCommId(commIpId);
					} else {
						String commIpId = xfCommService.getXfCommIpMaxId();// 获取当前表中最大的id
						xfCommService.addXfCommIp(commIpMap, create_user);// 新增commIp
						Integer commId = 1;
						if (commIpId != null) {
							commId = Integer.valueOf(commIpId) + 1;
						}
						device.setCommId("" + commId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean r1 = deviceDao.insertDevice(device);
			boolean r2 = true;
			if (!"2".equals(is_type)) { // 商品或商品类别，关联
				DeviceGood deviceGood = new DeviceGood();
				deviceGood.setDevice_num(device.getDevice_num());
				deviceGood.setIs_type(is_type);
				deviceGood.setGood_or_type_num(good_or_type_num);
				r2 = deviceDao.insertDeviceGood(deviceGood);
			}
			boolean result = false;
			if (r1 && r2) {
				log.put("V_OP_TYPE", "业务");
				result = true;
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "新增消费设备信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean updateDevice(Device device, String is_type,
			String good_or_type_num, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean r1 = deviceDao.updateDevice(device);
			boolean r2 = true;
			if (!"2".equals(is_type)) { // 商品或商品类别，关联
				DeviceGood deviceGood = new DeviceGood();
				deviceGood.setDevice_num(device.getDevice_num());
				deviceGood.setIs_type(is_type);
				deviceGood.setGood_or_type_num(good_or_type_num);
				DeviceGood dg = deviceDao
						.selByDeviceNum(device.getDevice_num());
				if (BeanUtil.isEmpty(dg)) { // 本来没有就插入，有就更新
					r2 = deviceDao.insertDeviceGood(deviceGood);
				} else {
					r2 = deviceDao.updateDeviceGood(deviceGood);
				}
			} else { // 零钞消费，解绑
				r2 = deviceDao.deleteDeviceGood(device.getDevice_num());
			}
			boolean result = false;
			if (r1 && r2) {
				log.put("V_OP_TYPE", "业务");
				result = true;
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改消费设备信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteDevice(String device_num, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean r1 = deviceDao.deleteDevice(device_num);
			boolean r2 = deviceDao.deleteDeviceGood(device_num);
			boolean result = false;
			if (r1 && r2) {
				log.put("V_OP_TYPE", "业务");
				result = true;
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除消费设备信息");
			logDao.addLog(log);
		}
	}

	@Override
	public DeviceGood selByDeviceNum(String device_num) {
		try {
			return deviceDao.selByDeviceNum(device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer typeNumIsExist(String device_type_num) {
		try {
			return deviceDao.typeNumIsExist(device_type_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean selXFDeviceInGroup(String device_group_num,
			String create_user) {
		try {
			device_group_num = "'" + device_group_num + "'";
			List<String> devices = deviceDao
					.selXFDeviceInGroup(device_group_num);
			for (int i = 0; i < devices.size(); i++) {
				devices.set(i, "'" + devices.get(i) + "'");
			}
			String deviceStr = devices.toString().substring(1,
					devices.toString().length() - 1);
			if (!"".equals(deviceStr)) {
				changeDeviceGroup("0", deviceStr, create_user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 初始化树
	 * 
	 * @param tree
	 * @param deviceDoorBeanList
	 */
	void InitTree(Tree tree, List<DeviceDoorBean> deviceDoorBeanList) {
		try {
			for (DeviceDoorBean deviceDoorBean : deviceDoorBeanList) {
				if (deviceDoorBean.getParentNum() != null
						&& !"".equals(deviceDoorBean.getParentNum())
						&& tree.getId().equals(deviceDoorBean.getParentNum())) {
					Tree treeNode = new Tree();
					treeNode.setId(deviceDoorBean.getNum());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("isDevice", deviceDoorBean.getType());
					if (deviceDoorBean.getType() == 2) {
						map.put("deviceNumAndDoorNum",
								deviceDoorBean.getParentNum() + ""
										+ deviceDoorBean.getNum());
					}
					treeNode.setAttributes(map);
					treeNode.setText(deviceDoorBean.getName());
					treeNode.setState("open");
					if (deviceDoorBean.getType() == 0) {
						treeNode.setIconCls("icon-deviceGroup_v1");
					} else if (deviceDoorBean.getType() == 1) {
						treeNode.setIconCls("icon-device_v1");
					} else {
						treeNode.setIconCls("icon-door");
					}
					treeNode.setPid(tree.getId());
					tree.getChildren().add(treeNode);
					InitTree(treeNode, deviceDoorBeanList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tree deviceAndGroupTree() {
		try {
			List<DeviceGroup> getXFDeviceGroup = deviceDao.getXFDeviceGroup();
			List<Device> allDevice = deviceDao.getAllXFDevice();
			List<DeviceDoorBean> deviceGroupNodes = new ArrayList<DeviceDoorBean>();
			// 遍历设备组
			for (DeviceGroup dg : getXFDeviceGroup) {
				DeviceDoorBean deviceDoorBean = new DeviceDoorBean();
				deviceDoorBean.setName(dg.getGroup_name());
				deviceDoorBean.setNum(dg.getGroup_num());
				deviceDoorBean.setParentNum(dg.getParent_group_num());
				deviceDoorBean.setType(0);
				deviceGroupNodes.add(deviceDoorBean);
			}
			// 遍历设备 父节点和标志
			for (Device device : allDevice) {
				DeviceDoorBean deviceDoorBean = new DeviceDoorBean();
				deviceDoorBean.setName(device.getDevice_name());
				deviceDoorBean.setNum(device.getDevice_num());
				deviceDoorBean.setParentNum(device.getDevice_group_num());
				deviceDoorBean.setType(1);
				deviceGroupNodes.add(deviceDoorBean);
			}
			Tree tree = new Tree();
			for (DeviceDoorBean deviceDoorBean : deviceGroupNodes) {
				if (null == deviceDoorBean.getParentNum()
						&& !"".equals(deviceDoorBean.getParentNum())) {
					tree.setId(deviceDoorBean.getNum());
					tree.setText(deviceDoorBean.getName());
					tree.setState("open");
					tree.setIconCls("icon-enterprise");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("isDevice", deviceDoorBean.getType());
					tree.setAttributes(map);
				}
			}
			InitTree(tree, deviceGroupNodes);
			return tree;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Tree onlyGroupTree() {
		try {
			List<DeviceGroup> getXFDeviceGroup = deviceDao.getXFDeviceGroup();
			List<DeviceDoorBean> deviceGroupNodes = new ArrayList<DeviceDoorBean>();
			// 遍历设备组
			for (DeviceGroup dg : getXFDeviceGroup) {
				DeviceDoorBean deviceDoorBean = new DeviceDoorBean();
				deviceDoorBean.setName(dg.getGroup_name());
				deviceDoorBean.setNum(dg.getGroup_num());
				deviceDoorBean.setParentNum(dg.getParent_group_num());
				deviceDoorBean.setType(0);
				deviceGroupNodes.add(deviceDoorBean);
			}
			Tree tree = new Tree();
			for (DeviceDoorBean deviceDoorBean : deviceGroupNodes) {
				if (null == deviceDoorBean.getParentNum()
						&& !"".equals(deviceDoorBean.getParentNum())) {
					tree.setId(deviceDoorBean.getNum());
					tree.setText(deviceDoorBean.getName());
					tree.setState("open");
					tree.setIconCls("icon-enterprise");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("isDevice", deviceDoorBean.getType());
					tree.setAttributes(map);
				}
			}
			InitTree(tree, deviceGroupNodes);
			return tree;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public DeviceGroup getXFGroupInfoByNum(String group_num) {
		try {
			return deviceDao.getXFGroupInfoByNum(group_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateXFDeviceGroup(DeviceGroup deviceGroup,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.updateXFDeviceGroup(deviceGroup);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备组信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean insertXFDeviceGroup(DeviceGroup deviceGroup,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.insertXFDeviceGroup(deviceGroup);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增设备组信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteXFDeviceGroup(String group_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean r1 = true; // deviceGroupService.selDeviceInGroup(group_num,
								// login_user); //把设备组下的门禁设备放进未分配组
			boolean result = false;
			if (r1) {
				result = deviceDao.deleteXFDeviceGroup(group_num);
				if (result) {
					log.put("V_OP_TYPE", "业务");
				} else {
					log.put("V_OP_TYPE", "异常");
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除设备组");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean changeDeviceGroup(String device_group_num,
			String device_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.changeDeviceGroup(device_group_num,
					device_num);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备的设备组");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean changeParentGroup(String parent_group_num, String group_num,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.changeParentGroup(parent_group_num,
					group_num);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备组的父设备组");
			logDao.addLog(log);
		}
	}

	@Override
	public Grid<Device> getDeviceByTree(Device device) {
		try {
			Grid<Device> grid = new Grid<Device>();
			List<Device> resultList = deviceDao.getDeviceByTree(device);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(deviceDao.getDeviceByTreeCount(device));
			} else {
				grid.setTotal(0);
			}
			return grid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Grid<Map> getNameByDevice(Map map) {
		try {
			Grid<Map> grid = new Grid<Map>();
			List<Map> resultList = deviceDao.getNameByDevice(map);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(deviceDao.getNameByDeviceCount(map));
			} else {
				grid.setTotal(0);
			}
			return grid;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void insertNameTask(String device_num, String card_num,
			String validity_flag, String trigger_flag) {
		try {
			Map<String, Object> map = deviceDao.getStaffByCard(card_num);
			BigDecimal balance = new BigDecimal(0);
			BigDecimal b = deviceDao.getAccountBalance(device_num,
					(String) map.get("staff_num"));
			if (!EmptyUtil.isEmpty(b)) {
				if (b.compareTo(new BigDecimal(0)) >= 0) { // 0、1
					balance = b;
				}
			}
			int triggerFlag = Integer.valueOf(trigger_flag);
			PersonCardTask pcTask = new PersonCardTask();
			pcTask.setStaff_id(map.get("staff_id").toString());
			pcTask.setStaff_no(map.get("staff_no").toString());
			pcTask.setStaff_num(map.get("staff_num").toString());
			pcTask.setStaff_name(map.get("staff_name").toString());
			pcTask.setDevice_num(device_num);
			pcTask.setCard_num(card_num);
			pcTask.setBalance(balance);
			pcTask.setFlag(validity_flag);
			pcTask.setTrigger_flag(triggerFlag);
			if (triggerFlag == 1 || triggerFlag == 4 || triggerFlag == 5) { // 消费、撤销、退款
				pcTask.setPriority("3");
			} else if (triggerFlag == 0 || triggerFlag == 2 || triggerFlag == 3) { // 充值、补助、补扣
				pcTask.setPriority("2");
			} else {
				pcTask.setPriority("1");
			}
			Integer count = deviceDao.isExistCardTask(device_num, card_num);
			if (count >= 1) { // 名单已存在相同则更新，否则新增
				deviceDao.updateNameTask(pcTask);
			} else {
				UUID task_id = UUID.randomUUID();
				pcTask.setTask_id(task_id.toString());
				deviceDao.insertNameTask(pcTask);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean insertPCTask(List<PersonCardTask> pctList, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			for (PersonCardTask pcTask : pctList) {
				insertNameTask(pcTask.getDevice_num(), pcTask.getCard_num(),
						"0", "8");
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增下发名单");
			logDao.addLog(log);
		}
	}

	@Override
	public List<String> getAllCardNum() {
		try {
			return deviceDao.getAllCardNum();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<DeviceConsumeSet> getMealLimitTimesList(String device_num) {
		try {
			List<DeviceConsumeSet> mealLimitTimesList = deviceDao
					.getMealLimitTimesList(device_num);

			return mealLimitTimesList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean delMealLimitTimes(String id, String device_num) {
		try {
			return deviceDao.delMealLimitTimes(id, device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<MealModel> getMealNum(String device_num) {
		try {
			return deviceDao.getMealNum(device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addNewMealLimitTime(DeviceConsumeSet deviceConsumeSet) {
		try {
			return deviceDao.addNewMealLimitTime(deviceConsumeSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public DeviceConsumeSet getDeviceConsumeSetById(String id, String device_num) {
		try {
			return deviceDao.getDeviceConsumeSetById(id, device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean modifyMealLimitTime(DeviceConsumeSet deviceConsumeSet) {
		try {
			return deviceDao.modifyMealLimitTime(deviceConsumeSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean checkMealsEqualDeviceMeal(String device_num) {
		try {
			int meals = deviceDao.selectMealCount();
			int deviceMeals = deviceDao.selectDeviceMealCount(device_num);
			if (meals == deviceMeals) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ResultMsg getParamByMac(String DevNum, String req) {
		ResultMsg result = null;
		try {
			Map<String, String> devCommIp = deviceService
					.getCommIPByDevNum(DevNum);
			// List<Map> commIpList = xfCommService.getXfCommIp();
			// for (Map commIpMap : commIpList) {

			String commUrl = "http://" + devCommIp.get("ip") + ":"
					+ devCommIp.get("port")
					+ "/comm/CDevice/getParamByDevNum.do";
			String str = HttpRequest.sendPost(commUrl, "DevNum=" + DevNum
					+ "&req=" + req);
			if (!("".equals(str)) && str != null && !("null".equals(str))) {
				ResultMsg result2 = null;// 暂存返回结果
				result2 = GsonUtil.toBean(str, ResultMsg.class);
				if ("0".equals(result2.getResult_code())) {
					result = result2;
					if (result2.getParameterUpResponse() != null) {
						result2.getParameterUpResponse()
								.getDataUp()
								.setPassword(
										Integer.valueOf((Integer
												.toHexString(result2
														.getParameterUpResponse()
														.getDataUp()
														.getPassword()))));
					}
					// break;
				}
			}
			// }
		} catch (Exception e) {
			result = new ResultMsg();
			result.setResult_code("1");
			result.setResult_msg("设备不在线或处于忙碌状态");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ResultMsg setTerminalParam(ParameterDownResponse paramDown) {
		/*
		 * String url =
		 * PropertiesToMap.propertyToMap("comm_interface.properties")
		 * .get("comm_url") + "CDevice/setTerminalParameter.do";
		 */
		ResultMsg result = null;
		Map<String, String> devCommIp = deviceService.getCommIPByDevNum(""
				+ paramDown.getMachine());
		// List<Map> commIpList = xfCommService.getXfCommIp();
		// for (Map commIpMap : commIpList) {
		/*
		 * Map<String, String> map = PropertiesToMap
		 * .propertyToMap("comm_interface.properties"); String sendPersonDownUrl
		 * = map.get("comm_url") + "CDevice/SendPersonDownT.do?"; String
		 * sendPersonDownUrlStr = HttpRequest.sendPost( sendPersonDownUrl,
		 * "flag=1");
		 */
		String commUrl = "http://" + devCommIp.get("ip") + ":"
				+ devCommIp.get("port")
				+ "/comm/CDevice/setTerminalParameter.do";

		String str = HttpRequest.sendPost(commUrl,
				"paramDown=" + GsonUtil.toJson(paramDown));

		if (!("".equals(str)) && str != null && !("null".equals(str))) {
			ResultMsg result2 = null;// 暂存返回结果
			result2 = GsonUtil.toBean(str, ResultMsg.class);
			if ("0".equals(result2.getResult_code())) {
				result = result2;
				// break;
			} else {
				result = new ResultMsg();
				result.setResult_code("1");
				result.setResult_msg("设置消费终端参数失败");
			}
		}
		// }
		return result;
	}

	/*
	 * @Override public List<PersonCardTask> getPersonCardTask(String machine) {
	 * 
	 * List<PersonCardTask> resultList; try { resultList =
	 * deviceDao.getPersonCardTask(machine); return resultList; } catch
	 * (Exception e) { resultList = new ArrayList<PersonCardTask>();
	 * e.printStackTrace(); return resultList; }
	 * 
	 * }
	 */
	/*
	 * @Override public boolean deletePersonCardTask(String machine, String
	 * pack, String returnVal) { Map<String, String> log = new HashMap<String,
	 * String>();
	 * 
	 * boolean result = false; log.put("V_OP_NAME", "下发名单");
	 * log.put("V_OP_FUNCTION", "删除"); // log.put("V_OP_ID", login_user);
	 * log.put("V_OP_MSG", "删除名单"); try { result =
	 * deviceDao.deletePersonCardTask(machine, pack, returnVal); if (result &&
	 * "1".equals(returnVal)) { // 判断下发成功的名单是否已存在,若存在则更新,若不存在则插入
	 * List<PersonCardTask> personCardTask = deviceDao .getSuccNameList(machine,
	 * pack); for (PersonCardTask per : personCardTask) { if
	 * (deviceDao.valName(per.getDevice_num(), per.getCard_num()) > 0) {//
	 * 判断此设备的此卡号是否已存在 deviceDao.updateName(per.getDevice_num(),
	 * per.getCard_num());// 修改名单的更新时间 } else {
	 * deviceDao.insertToNameList(per.getCard_num(), per.getDevice_num(),
	 * per.getCreate_time());// 将下发成功的名单插入kc_xf_name_list表中 } } } if
	 * (deviceDao.insertToHistory(machine)) {// 如果复制成功
	 * deviceDao.deletePCT(machine);// 删除原任务表的内容 } if (result) {
	 * log.put("V_OP_TYPE", "业务"); } else { log.put("V_OP_TYPE", "异常"); } }
	 * catch (Exception e) { log.put("V_OP_TYPE", "异常"); e.printStackTrace(); }
	 * logDao.addLog(log); return result; }
	 */

	/*
	 * @Override public List<ResultMsg> issueName(String device_num) { String[]
	 * deviceNums = device_num.split(","); String issueurl =
	 * PropertiesToMap.propertyToMap(
	 * "comm_interface.properties").get("comm_url") +
	 * "CDevice/applyPersonCard.do"; List<ResultMsg> resultList = new
	 * ArrayList<ResultMsg>(); ResultMsg result = null; String[] str = new
	 * String[deviceNums.length]; for (int i = 0; i < deviceNums.length; i++) {
	 * 
	 * try { str[i] = HttpRequest.sendPost(issueurl, "machine=" +
	 * deviceNums[i]);
	 * 
	 * if (str[i] == null || "".equals(str[i])) { result = new ResultMsg();
	 * result.setResult_code("1"); result.setResult_msg(deviceNums[i]);
	 * deviceDao.markFail(deviceNums[i]);// 将此设备的所有任务删除 if
	 * (deviceDao.insertToHistory(deviceNums[i])) {// 如果复制成功
	 * deviceDao.deletePCT(deviceNums[i]);// 删除原任务表的内容 } } else { result =
	 * GsonUtil.toBean(str[i], ResultMsg.class); if (result != null &&
	 * "1".equals(result.getResult_code())) {
	 * deviceDao.markFail(deviceNums[i]);// 将此设备的所有任务删除 if
	 * (deviceDao.insertToHistory(deviceNums[i])) {// 如果复制成功
	 * deviceDao.deletePCT(deviceNums[i]);// 删除原任务表的内容 } }
	 * 
	 * } resultList.add(result); } catch (Exception e) { result = new
	 * ResultMsg(); result.setResult_code("1");
	 * result.setResult_msg(deviceNums[i]); resultList.add(result);
	 * e.printStackTrace(); } }
	 * 
	 * return resultList; }
	 */
	/*
	 * @Override public boolean updateTaskState(String idStr, String pack) {
	 * 
	 * try { deviceDao.updateTaskState(idStr, pack); return true; } catch
	 * (Exception e) { e.printStackTrace(); return false; } }
	 */

	/*
	 * @Override public Integer getMaxPack(String machine) { return
	 * deviceDao.getMaxPack(machine); }
	 * 
	 * @Override public Integer getTry_times(Integer maxPack, String machine) {
	 * return deviceDao.getTry_times(maxPack, machine); }
	 * 
	 * @Override public List<PersonCardTask> getRunningTask(String machine) {
	 * List<PersonCardTask> resultList; try { resultList =
	 * deviceDao.getRunningTask(machine); return resultList; } catch (Exception
	 * e) { resultList = new ArrayList<PersonCardTask>(); e.printStackTrace();
	 * return resultList; }
	 * 
	 * }
	 * 
	 * @Override public boolean updateTaskTrytimes(String idStr, String
	 * tryTimes) { try { deviceDao.updateTaskTrytimes(idStr, tryTimes); return
	 * true; } catch (Exception e) { e.printStackTrace(); return false; } }
	 */

	@Override
	public List<PersonCardTask> getSomeTask(List<String> onlineDevice) {
		List<PersonCardTask> resultList;
		// 将设备编号数组拼装成字符串
		String devStr = "";
		if (onlineDevice.size() > 0) {// 拼装设备编号字符串
			boolean flag = true;
			for (String dev_num : onlineDevice) {
				if (flag) {
					devStr = dev_num;
					flag = false;
				} else {
					devStr = devStr + "," + dev_num;
				}
			}
		}
		try {
			if ("".equals(devStr)) {
				devStr = "''";
			}
			resultList = deviceDao.getSomeTask(devStr);
			if (resultList != null && resultList.size() > 0) {
				BigDecimal b = new BigDecimal(Double.valueOf(100.00));
				for (PersonCardTask p : resultList) {
					p.setBalance(p.getBalance().multiply(b));
				}
			}
			return resultList;
		} catch (Exception e) {
			resultList = new ArrayList<PersonCardTask>();
			e.printStackTrace();
			return resultList;
		}
	}

	@Override
	public boolean deletePCTask(PersonCardTask personCardTask) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.deletePCTask(personCardTask.getId());
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "下发名单任务");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_MSG", "删除名单");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean insertToNameList(PersonCardTask personCardTask) {

		try {
			String flag = personCardTask.getFlag();
			if ("0".equals(flag)) { // 下发名单
				if (deviceDao.valName(personCardTask.getDevice_num(),
						personCardTask.getCard_num()) > 0) {// 判断此设备的此卡号是否已存在
					deviceDao.updateName(personCardTask.getDevice_num(),
							personCardTask.getCard_num());// 修改名单的更新时间
				} else {
					deviceDao.insertToNameList(personCardTask.getCard_num(),
							personCardTask.getDevice_num(),
							personCardTask.getCreate_time());// 将下发成功的名单插入kc_xf_name_list表中
				}
			} else { // 删除名单
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("device_num", personCardTask.getDevice_num());
				map.put("card_num", personCardTask.getCard_num());
				deviceDao.delNameList(map);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean insertToHistory(PersonCardTask personCardTask) {
		try {
			deviceDao.insertToHistory(personCardTask);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setHistorySuccFlag(PersonCardTask personCardTask) {
		try {
			deviceDao.setHistorySuccFlag(personCardTask.getSuccess_flag(),
					personCardTask.getId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> getOnlineDevice() {
		List<String> resultList;
		// 将设备编号数组拼装成字符串

		try {
			resultList = deviceDao.getOnlineDevice();
			return resultList;
		} catch (Exception e) {
			resultList = new ArrayList<String>();
			e.printStackTrace();
			return resultList;
		}
	}

	@Override
	public boolean updateTryTime(PersonCardTask personCardTask) {
		try {
			deviceDao.updateTryTime(personCardTask);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateRunState(String id, String state) {
		try {
			deviceDao.updateRunState(id, state);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Integer compareIP(Cron cron) {
		try {
			return deviceDao.compareIP(cron);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Device selectDeviceByNum(String device_num) {
		return deviceDao.selectDeviceByNum(device_num);
	}

	@Override
	public JsonResult copyParam(String flag, String device_num, String nums,
			String login_user) throws Exception {
		Map<String, String> log = new HashMap<String, String>();
		JsonResult result = new JsonResult();
		ResultMsg msg = null;
		ParameterDownResponse deviceParam = null;
		List<DeviceConsumeSet> mealLimitTimesList = null;
		List<Map> newList = null;
		try {
			String[] flags = flag.split(",");
			String[] goal_device = nums.split(",");
			String success_name = "";
			String fail_name = "";
			
			Map<String, String> devCommIp = deviceService.getCommIPByDevNum(device_num);
			String devCommUrl = "http://" + devCommIp.get("ip") + ":"
					+ devCommIp.get("port")
					+ "/comm/CDevice/getParamByDevNum.do";
			
			for (int i = 0; i < flags.length; i++) { // 如果复制有终端设置，先判断是否能获取成功
				if ("0".equals(flags[i])) {
					String str = HttpRequest.sendPost(devCommUrl, "DevNum="
							+ device_num + "&req=" + 1);
					msg = GsonUtil.toBean(str, ResultMsg.class);
					if(msg!=null){
						if ("0".equals(msg.getResult_code())) {
							str = HttpRequest.sendPost(devCommUrl, "DevNum=" + device_num
									+ "&req=" + 2);
							msg = GsonUtil.toBean(str, ResultMsg.class);
							deviceParam = convertParam(msg); // 封装参数model
							if (deviceParam == null
									|| deviceParam.getDataDown() == null) {
								result.setSuccess(false);
								result.setMsg("复制失败，读取设备终端设置失败");
							}
						} else {
							result.setSuccess(false);
							result.setMsg("复制失败，读取设备终端设置失败");
						}
					}else {
						result.setSuccess(false);
						result.setMsg("复制失败，读取设备终端设置失败");
					}
				} else if ("1".equals(flags[i])) {
					mealLimitTimesList = deviceDao
							.getMealLimitTimesList(device_num);
				} else if ("2".equals(flags[i])) {
					Map<String, Object> deviceMap = new HashMap<String, Object>();
					deviceMap.put("device_num", device_num);
					newList = deviceDao.getAllNameList(deviceMap);
				}
			}

			// 开始复制
			for (int j = 0; j < goal_device.length; j++) {
				int f = 0; // 标志是否已失败，0：成功，1：失败
				String num = goal_device[j];
				String device_name = deviceDao.getDeviceName(num);
				
				for (int i = 0; i < flags.length; i++) {
					if ("0".equals(flags[i])) { // 终端设置
						deviceParam.setMachine(Integer.valueOf(num));
						Map<String, String> goalCommIp = deviceService.getCommIPByDevNum(num);
						String goalCommUrl = "http://" + goalCommIp.get("ip") + ":"
								+ goalCommIp.get("port")
								+ "/comm/CDevice/setTerminalParameter.do";
						String str = HttpRequest.sendPost(goalCommUrl, "paramDown="
								+ GsonUtil.toJson(deviceParam));
						ResultMsg r = GsonUtil.toBean(str, ResultMsg.class);
						if(r!=null){
							if ("1".equals(r.getResult_code())) {
								if (f == 0) {
									fail_name = fail_name + device_name + "、";
									f = 1;
								}
							}
						}else {
							if(f == 0) {
								fail_name = fail_name + device_name + "、";
								f = 1;
							}
						}
					} else if ("1".equals(flags[i])) { // 消费次数
						boolean r1 = deviceDao.delAllMeal(num);
						if (r1) {
							for (int x = 0; x < mealLimitTimesList.size(); x++) {
								DeviceConsumeSet c1 = mealLimitTimesList.get(x);
								DeviceConsumeSet c2 = new DeviceConsumeSet();
								c2.setDevice_num(num);
								c2.setMeal_num(c1.getMeal_name());
								c2.setTimes_limit(c1.getTimes_limit());
								boolean r2 = deviceDao.addNewMealLimitTime(c2);
								if (!r2) {
									if (f == 0) {
										fail_name = fail_name + device_name
												+ "、";
										f = 1;
									}
								}
							}
						} else {
							if (f == 0) {
								fail_name = fail_name + device_name + "、";
								f = 1;
							}
						}
					} else if ("2".equals(flags[i])) { // 已下发名单
						Map<String, Object> deviceMap = new HashMap<String, Object>();
						deviceMap.put("device_num", device_num);
						deviceMap.put("device_num", num);
						List<Map> oldList = deviceDao.getAllNameList(deviceMap);
						// 将以前的删除
						for (int x = 0; x < oldList.size(); x++) {
							Map<String, Object> nameMap = oldList.get(x);
							if (nameMap != null && nameMap.size() != 0) {
								PersonCardTask pcTask = new PersonCardTask();
								pcTask.setStaff_id(nameMap.get("staff_id")
										.toString());
								pcTask.setStaff_no(nameMap.get("staff_no")
										.toString());
								pcTask.setStaff_num(nameMap.get("staff_num")
										.toString());
								pcTask.setStaff_name(nameMap.get("staff_name")
										.toString());
								pcTask.setDevice_num(num);
								pcTask.setCard_num(nameMap.get("card_num")
										.toString());
								BigDecimal balance = new BigDecimal(0);
								BigDecimal b = deviceDao.getAccountBalance(num,
										nameMap.get("card_num").toString());
								if (!EmptyUtil.isEmpty(b)) {
									balance = b;
								}
								pcTask.setBalance(balance);
								pcTask.setFlag("1");
								pcTask.setTrigger_flag(9);
								pcTask.setPriority("1");
								Integer count = deviceDao.isExistCardTask(num,
										nameMap.get("card_num").toString());
								boolean r1 = false;
								if (count >= 1) { // 名单已存在相同则更新，否则新增
									r1 = deviceDao.updateNameTask(pcTask);
								} else {
									UUID task_id = UUID.randomUUID();
									pcTask.setTask_id(task_id.toString());
									r1 = deviceDao.insertNameTask(pcTask);
								}
								if (!r1) {
									if (f == 0) {
										fail_name = fail_name + device_name
												+ "、";
										f = 1;
									}
								}
							}
						}
						// 再下发新的
						for (int x = 0; x < newList.size(); x++) {
							Map<String, Object> nameMap = newList.get(x);
							if (nameMap != null && nameMap.size() != 0) {
								PersonCardTask pcTask = new PersonCardTask();
								pcTask.setStaff_id(nameMap.get("staff_id")
										.toString());
								pcTask.setStaff_no(nameMap.get("staff_no")
										.toString());
								pcTask.setStaff_num(nameMap.get("staff_num")
										.toString());
								pcTask.setStaff_name(nameMap.get("staff_name")
										.toString());
								pcTask.setDevice_num(num);
								pcTask.setCard_num(nameMap.get("card_num")
										.toString());
								BigDecimal balance = new BigDecimal(0);
								BigDecimal b = deviceDao.getAccountBalance(num,
										nameMap.get("card_num").toString());
								if (!EmptyUtil.isEmpty(b)) {
									balance = b;
								}
								pcTask.setBalance(balance);
								pcTask.setFlag("0");
								pcTask.setTrigger_flag(8);
								pcTask.setPriority("1");
								Integer count = deviceDao.isExistCardTask(num,
										nameMap.get("card_num").toString());
								boolean r1 = false;
								if (count >= 1) { // 名单已存在相同则更新，否则新增
									r1 = deviceDao.updateNameTask(pcTask);
								} else {
									UUID task_id = UUID.randomUUID();
									pcTask.setTask_id(task_id.toString());
									r1 = deviceDao.insertNameTask(pcTask);
								}
								if (!r1) {
									if (f == 0) {
										fail_name = fail_name + device_name
												+ "、";
										f = 1;
									}
								}
							}
						}
					}
				}
				if (f == 0) {
					success_name = success_name + device_name + "、";
				}
			}
			log.put("V_OP_TYPE", "业务");
			if ("".equals(fail_name)) { // 没有失败
				success_name = success_name.substring(0,
						success_name.length() - 1);
				result.setSuccess(true);
				result.setMsg(success_name + "," + fail_name);
			} else if ("".equals(success_name)) { // 没有成功
				fail_name = fail_name.substring(0, fail_name.length() - 1);
				result.setSuccess(true);
				result.setMsg(success_name + "," + fail_name);
			} else { // 部分成功
				success_name = success_name.substring(0,
						success_name.length() - 1);
				fail_name = fail_name.substring(0, fail_name.length() - 1);
				result.setSuccess(true);
				result.setMsg(success_name + "," + fail_name);
			}
			return result;
		} catch (Exception e) {
			log.put("V_OP_TYPE", "异常");
			throw e;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_MSG", "复制设备参数");
			log.put("V_OP_ID", login_user);
			logDao.addLog(log);
		}
	}

	/**
	 * 设：down，读：up
	 * 
	 * @throws Exception
	 */
	@Override
	public ParameterDownResponse convertParam(ResultMsg msg) throws Exception {
		ParameterDownResponse downParam = new ParameterDownResponse();
		try {
			ParameterUpResponse upParam = msg.getParameterUpResponse();
			if (upParam != null) {
				DataDown data = new DataDown();

				data.setLimit(upParam.getDataUp().getLimit());
				data.setMoney(upParam.getDataUp().getMoney());
				data.setPassword(upParam.getDataUp().getPassword());
				data.setConfirm(upParam.getDataUp().getConfirm());
				data.setMultiUse(upParam.getDataUp().getMultiUse());
				data.setTimeLimit(upParam.getDataUp().getTimeLimit());
				data.setOnOffLineWay(upParam.getDataUp().getOnOffLineWay());
				data.setMeal1(upParam.getDataUp().getMeal1());
				data.setMeal2(upParam.getDataUp().getMeal2());
				data.setMeal3(upParam.getDataUp().getMeal3());
				data.setMeal4(upParam.getDataUp().getMeal4());
				data.setTimeLimitMoney(upParam.getDataUp().getTimeLimitMoney());
				data.setMeal1Mode(upParam.getDataUp().getMeal1Mode());
				data.setMeal2Mode(upParam.getDataUp().getMeal2Mode());
				data.setMeal3Mode(upParam.getDataUp().getMeal3Mode());
				data.setMeal4Mode(upParam.getDataUp().getMeal4Mode());
				data.setGroupsuport(upParam.getDataUp().getGroupsuport());
				data.setGoodNumMoney(upParam.getDataUp().getGoodNumMoney());

				downParam.setDataDown(data);
			}
			return downParam;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean deleteNameList(List<PersonCardTask> pctList,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			for (PersonCardTask pcTask : pctList) {
				insertNameTask(pcTask.getDevice_num(), pcTask.getCard_num(),
						"1", "9");
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "消费设备管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除下发名单");
			logDao.addLog(log);
		}
	}

	@Override
	public List<String> cardIfExistList(String card_num) {
		try {
			return deviceDao.cardIfExistList(card_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAllDevNum() {
		return deviceDao.getAllDevNum();
	}

	@Override
	public List<String> getNameListByDevice(String deviceNum) {
		return deviceDao.getNameListByDevice(deviceNum);
	}

	@Override
	public Map<String, String> getCommIPByDevNum(String deviceNum) {
		return deviceDao.getCommIPByDevNum(deviceNum);
	}

	@Override
	public List<Map> getAllCommIp() {
		return xfCommService.getXfCommIp();
	}

}
