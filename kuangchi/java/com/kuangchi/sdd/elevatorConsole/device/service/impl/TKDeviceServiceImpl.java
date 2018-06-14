package com.kuangchi.sdd.elevatorConsole.device.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceDoorBean;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao.AuthorityByDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.dao.ITKDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.dao.TKDeviceOpenTimeDao;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.CommFloorConfig;
import com.kuangchi.sdd.elevatorConsole.device.model.CommResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.FloorOpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.HardWareParam;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.model.OpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

/**
 * 设备信息维护Service
 * 
 * @author
 * 
 */
@Transactional
@Service("tkDeviceService")
public class TKDeviceServiceImpl implements ITKDeviceService {

	@Resource(name = "tkDeviceDao")
	private ITKDeviceDao deviceDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Autowired
	private AuthorityByDeviceDao authorityByDeviceDao;

	@Resource(name = "tKDeviceOpenTimeDaoImpl")
	private TKDeviceOpenTimeDao tKDeviceOpenTimeDao;
	
	@Resource(name = "tkCommServiceImpl")
	private TkCommService tkCommService;

	@Override
	public Grid<Device> getTKDeviceListParam(Device device) {
		try {
			Grid<Device> grid = new Grid<Device>();
			List<Device> resultList = deviceDao.getTKDeviceListParam(device);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(deviceDao.getTKDeviceListParamCount(device));
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
	public Tree deviceAndGroupTree() {
		try {
			List<DeviceGroup> getXFDeviceGroup = deviceDao.getTKDeviceGroup();
			List<Device> allDevice = deviceDao.getAllTKDevice();
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
	public Grid<Device> getTKDeviceByTree(Device device) {
		try {
			Grid<Device> grid = new Grid<Device>();
			List<Device> resultList = deviceDao.getTKDeviceByTree(device);
			grid.setRows(resultList);
			if (null != resultList) {
				grid.setTotal(deviceDao.getTKDeviceByTreeCount(device));
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
	public boolean changeTKDeviceGroup(String device_group_num,
			String device_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.changeTKDeviceGroup(device_group_num,
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
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备的设备组");
			logDao.addLog(log);
		}
	}

	@Override
	public Integer seekTKDevice(List<Device> devices, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		Integer count = 0;
		try {
			for (Device device : devices) {
				device.setDelete_flag("0,1");
				Integer exist = deviceDao.ifExistTKDeviMac(device);
				if (0 == exist) { // 不存在则新增
					device.setDelete_flag("0");
					UUID uuid = UUID.randomUUID();
					device.setDevice_num(uuid.toString());
					deviceDao.insertTKDevice(device);
					for (int i = 1; i < 49; i++) { // 默认楼层48
						Floor floor = new Floor();
						floor.setDevice_num(device.getDevice_num());
						floor.setFloor_num(String.valueOf(i));
						floor.setFloor_name("第" + i + "层");
						floor.setConfiguration_floor(String.valueOf(i));
						floor.setStart_time1("00:00");
						floor.setStart_time2("00:00");
						floor.setStart_time3("00:00");
						floor.setStart_time4("00:00");
						floor.setEnd_time1("00:00");
						floor.setEnd_time2("00:00");
						floor.setEnd_time3("00:00");
						floor.setEnd_time4("00:00");
						deviceDao.insertDefaultFloor(floor);
					}
					tKDeviceOpenTimeDao.insertDeviceOpenTime(device
							.getDevice_num());// 默认时段
					count = +1;
				} else { // 存在
					device.setDelete_flag("0");
					Integer ifDel = deviceDao.ifExistTKDeviMac(device);
					if (ifDel == 0) { // 已删除则更新
						device.setDevice_group_num("0");
						deviceDao.updateTKDevice(device);
						deviceDao.resetPosition(device.getMac());
						count = +1;
						/*
						 * for(int i=1; i<49; i++){ //默认楼层48 Floor floor = new
						 * Floor(); floor.setDevice_num(device.getDevice_num());
						 * floor.setFloor_num(String.valueOf(i));
						 * floor.setFloor_name("第"+i+"层");
						 * floor.setConfiguration_floor(String.valueOf(i));
						 * floor.setStart_time1(""); floor.setStart_time2("");
						 * floor.setStart_time3(""); floor.setStart_time4("");
						 * floor.setEnd_time1(""); floor.setEnd_time2("");
						 * floor.setEnd_time3(""); floor.setEnd_time4("");
						 * deviceDao.insertDefaultFloor(floor); }
						 * tKDeviceOpenTimeDao
						 * .insertDeviceOpenTime(device.getDevice_num());// 默认时段
						 * tKDeviceOpenTimeDao
						 * .initDeviceParam(device.getDevice_num()); //默认动作参数
						 */
					}
				}
			}
			log.put("V_OP_TYPE", "业务");
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return count;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "搜索设备");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteTKDevice(String device_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = deviceDao.deleteTKDevice(device_num);
			/*
			 * deviceDao.delDeviceFloor(device_num); //删除对应楼层
			 * deviceDao.delDeviceHoliday(device_num); //删除对应节假日
			 * tKDeviceOpenTimeDao.deleteDevOpenTime(device_num); //删除对应时段
			 */
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
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除设备信息");
			logDao.addLog(log);
		}
	}

	@Override
	public Device getInfoByTKDeviceNum(String device_num) {
		try {
			return deviceDao.getInfoByTKDeviceNum(device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer ifExistTKDeviName(Map<String, Object> map) {
		try {
			return deviceDao.ifExistTKDeviName(map);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public boolean updateTKDevice(Device device, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean r = deviceDao.updateTKDevice(device);
			if (r) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean updateTKDevcieState(Device device) {
		try {
			return deviceDao.updateTKDeviceState(device);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Floor> getDeviceFloor(String device_num) {
		try {
			LinkedTreeMap commFloor = commReadFloor(device_num);
			if (commFloor != null) { // 连接成功
				List<Floor> floors = deviceDao.getDeviceFloor(device_num);
				List<Double> configs = (List<Double>) commFloor
						.get("floorList");
				for (int i = 0; i < configs.size(); i++) {
					double c = configs.get(i);
					floors.get(i).setConfiguration_floor(
							String.valueOf((int) c));
				}
				return floors;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Floor> getFloorName(String device_num) {
		try {
			return deviceDao.getFloorName(device_num);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public boolean updateDeviceFloorComm(List<Floor> floors,String login_user) {
		try {
			List<Integer> config = new ArrayList<Integer>();
			for (int i = 0; i < floors.size(); i++) {
				config.add(i,
						Integer.valueOf(floors.get(i).getConfiguration_floor()));
			}
			String device_num = floors.get(0).getDevice_num();
			if (commConfigFloor(device_num, config)) {
				return true;
				}else{
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}
	}
	
	@Override	
	public boolean updateDeviceFloor(List<Floor> floors, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String device_num = floors.get(0).getDevice_num();
			List<Floor> configFloor = deviceDao.getDeviceFloor(device_num);
			List<Integer> config = new ArrayList<Integer>();
			int i=0;
			for(int j=0; j<configFloor.size(); j++){
				for( ; i<configFloor.size(); ) {
					if(i<floors.size()){
						if(configFloor.get(j).getFloor_num().equals(floors.get(i).getFloor_num())){
							config.add(j, Integer.valueOf(floors.get(i).getConfiguration_floor()));
							i++;
						}else {
							config.add(j, Integer.valueOf(configFloor.get(j).getConfiguration_floor()));
						}
					}else {
						config.add(j, Integer.valueOf(configFloor.get(j).getConfiguration_floor()));
					}
					break;
				}
			}
			
			if (commConfigFloor(device_num, config)) { // 连接接口成功
				for (Floor floor : floors) {
					boolean result = deviceDao.updateDeviceFloor(floor);
					if(!result){
						return false;
					}
				}
				log.put("V_OP_TYPE", "业务");
				return true;
			} else {
				log.put("V_OP_TYPE", "异常");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改配置楼层");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean setFloorOpenArea(List<Floor> floors, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String device_num = floors.get(0).getDevice_num();
			for (Floor floor : floors) {
				if (commFloorOpen(device_num, floor)) { // 连接接口成功
					deviceDao.setFloorOpenArea(floor);
				} else {
					log.put("V_OP_TYPE", "异常");
					return false;
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "设置楼层开放时区");
			logDao.addLog(log);
		}
	}

	@Override
	public List<Floor> getFloorInfo(String device_num) {
		try {
			List<Floor> floors = new ArrayList<Floor>();
			LinkedTreeMap commFloor = commReadFloor(device_num);
			if (commFloor != null) { // 连接成功
				floors = deviceDao.getFloorInfo(device_num);
				List<Double> configs = (List<Double>) commFloor
						.get("floorList");
				for (int i = 0; i < configs.size(); i++) {
					double c = configs.get(i);
					floors.get(i).setConfiguration_floor(
							String.valueOf((int) c));
				}
				
				//不显示不启用的楼层
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("device_num", device_num);
				map.put("enabled_flag", "1");
				List<String> list = deviceDao.getIfEnabledFloor(map);
				int j=0;
				for(int i=0; i<list.size(); i++){
					for( ; j<floors.size(); j++){
						if(list.get(i).equals(floors.get(j).getFloor_num())){
							floors.remove(j);
							break;
						}
					}
				}
			}
			return floors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean copyConfiFloor(String device_num, String copy_device_num) {
		boolean res = false;
	
			//String[] nums = copy_device_num.split(",");
			List<Floor> configFloor = deviceDao.getDeviceFloor(device_num);
			List<Integer> config = new ArrayList<Integer>();
			for (int j = 0; j < configFloor.size(); j++) {
				config.add(j, Integer.valueOf(configFloor.get(j)
						.getConfiguration_floor()));
			}
			/*for (int i = 0; i < nums.length; i++) {*/
				if (!commConfigFloor(copy_device_num, config)) { // 连接成功
					res = false;
				}else{
					res = true;
				}
		
			return res;
	}
	/*public boolean copyConfiFloor(String device_num, String copy_device_num,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String[] nums = copy_device_num.split(",");
			List<Floor> configFloor = deviceDao.getDeviceFloor(device_num);
			List<Integer> config = new ArrayList<Integer>();
			for (int j = 0; j < configFloor.size(); j++) {
				config.add(j, Integer.valueOf(configFloor.get(j)
						.getConfiguration_floor()));
			}
			for (int i = 0; i < nums.length; i++) {
				if (commConfigFloor(nums[i], config)) { // 连接成功
					for (Floor floor : configFloor) {
						floor.setDevice_num(nums[i]);
						deviceDao.updateDeviceFloor(floor);
					}
				} else {
					log.put("V_OP_TYPE", "异常");
					return false;
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "复制配置楼层");
			logDao.addLog(log);
		}
	}*/

	@Override
	public boolean copyFloorOpenArea(String device_num, String copy_device_num) {
		boolean result = false;
			//String[] nums = copy_device_num.split(",");
			List<Floor> floorOpenList = deviceDao.getFloorInfo(device_num);
			/*for (int i = 0; i < nums.length; i++) {*/
				for (Floor floor : floorOpenList) {
					if (!commFloorOpen(copy_device_num, floor)) { // 连接成功
						break;
					} else {
						result = true;
					}
				}
		
			return result;
	}
	/*public boolean copyFloorOpenArea(String device_num, String copy_device_num,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String[] nums = copy_device_num.split(",");
			List<Floor> floorOpenList = deviceDao.getFloorInfo(device_num);
			for (int i = 0; i < nums.length; i++) {
				for (Floor floor : floorOpenList) {
					if (commFloorOpen(nums[i], floor)) { // 连接成功
						floor.setDevice_num(nums[i]);
						deviceDao.setFloorOpenArea(floor);
					} else {
						log.put("V_OP_TYPE", "异常");
						return false;
					}
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "复制楼层开放时区");
			logDao.addLog(log);
		}
	}*/

	@Override
	public Grid<Holiday> getHolidayByDevice(Map<String, Object> map) {
		try {
			Grid<Holiday> grid = new Grid<Holiday>();
			List<Holiday> holiday = deviceDao.getHolidayByDevice(map);
			grid.setRows(holiday);
			if (null != holiday) {
				grid.setTotal(deviceDao.getHolidayByDeviceCount(map));
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
	public boolean issuedHoliday(String device_num, List<Holiday> holidayList,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			List<String> holiList = new ArrayList<String>();
			for (Holiday h : holidayList) {
				holiList.add(h.getHoliday_date());
			}

			// List<Holiday> holiListSended =
			// deviceDao.getSendHoliday(device_num);
			List<Holiday> holiListSended = deviceDao.getHolisByDevi(device_num);
			for (Holiday hSended : holiListSended) {// 已经下发过的加进来重新下发
				holiList.add(hSended.getHoliday_date());
			}

			if (commHoliday(device_num, holiList)) { // 连接成功
				// deviceDao.delDeviceHoli("'" + device_num + "'");

				/*
				 * for (Holiday holiday : holidayList) { //
				 * deviceDao.insertHoliday(holiday);
				 * deviceDao.updateHoliSend_State(holiday, 0); }
				 */
				log.put("V_OP_TYPE", "业务");
				return true;
			} else {
				log.put("V_OP_TYPE", "异常");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "下发节假日");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean copyHoliday(String device_num, String copy_device_num) {
			boolean result = false;
			/*String[] nums = copy_device_num.split(",");*/
			List<Holiday> list = deviceDao.getHolisByDevi(device_num);
			List<String> holidayDateList = new ArrayList<String>();
			for (int j = 0; j < list.size(); j++) {
				holidayDateList.add(j, list.get(j).getHoliday_date());
			}
			/*for (int i = 0; i < nums.length; i++) {*/
				if (commHoliday(copy_device_num, holidayDateList)) { // 连接成功
					result = true;
				}
			
			return result;
	}
	/*public boolean copyHoliday(String device_num, String copy_device_num,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String[] nums = copy_device_num.split(",");
			List<Holiday> list = deviceDao.getHolisByDevi(device_num);
			List<String> holidayDateList = new ArrayList<String>();
			for (int j = 0; j < list.size(); j++) {
				holidayDateList.add(j, list.get(j).getHoliday_date());
			}
			for (int i = 0; i < nums.length; i++) {
				if (commHoliday(nums[i], holidayDateList)) { // 连接成功
					for (Holiday h : list) {
						h.setDevice_num(nums[i]);
						deviceDao.insertHoliday(h);
					}
				} else {
					log.put("V_OP_TYPE", "异常");
					return false;
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "复制节假日");
			logDao.addLog(log);
		}
	}*/

	@Override
	public boolean updateAllState(String online_state) {
		try {
			return deviceDao.updateAllState(online_state);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Device> getAllTKDeviceInfo() {
		try {
			return deviceDao.getAllTKDeviceInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Holiday> getHolisByDevice(String device_num) {
		try {
			List<Holiday> list1 = new ArrayList<Holiday>();
			List<Holiday> list0 = deviceDao.getHolisByDevi(device_num);

			Date day = new Date();// 获取当前日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
			String vali_date = df.format(day);

			Date date = null;
			try {
				date = df.parse(vali_date);// 格式化当前日期
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long sysTime = date.getTime();// 获取当前日期的毫秒数
			for (Holiday holi : list0) {
				if (sysTime > df.parse(holi.getHoliday_date()).getTime()) {
					holi.setValid_date("1");
				} else {
					holi.setValid_date("0");
				}
				list1.add(holi);
			}

			return list1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LinkedTreeMap commReadFloor(String device_num) {
		try {
			// 设备
			Device device = deviceDao.getInfoByTKDeviceNum(device_num);
			CommDevice commDevice = new CommDevice();
			commDevice.setMac(device.getMac());
			commDevice.setIp(device.getDevice_ip());
			commDevice.setPort(device.getDevice_port());
			commDevice.setGateway(device.getNetwork_gateway());
			commDevice.setSerialNo(device.getDevice_sequence());
			commDevice.setSubnet(device.getNetwork_mask());
			commDevice.setAddress(device.getAddress());
			String jsonDevice = GsonUtil.toJson(commDevice);
			// 连接
			String url = tkCommService.getTkCommUrl(device_num);
			String message = HttpRequest
					.sendPost(url + "TKDevice/tk_RecvConfigTable.do?",
							"device=" + jsonDevice);
			Gson gson = new Gson();
			LinkedTreeMap commFloor = gson.fromJson(message,
					LinkedTreeMap.class);
			return commFloor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean commConfigFloor(String device_num, List<Integer> config) {
		try {
			// 设备
			Device device = deviceDao.getInfoByTKDeviceNum(device_num);
			CommDevice commDevice = new CommDevice();
			commDevice.setMac(device.getMac());
			commDevice.setIp(device.getDevice_ip());
			commDevice.setPort(device.getDevice_port());
			commDevice.setGateway(device.getNetwork_gateway());
			commDevice.setSerialNo(device.getDevice_sequence());
			commDevice.setSubnet(device.getNetwork_mask());
			commDevice.setAddress(device.getAddress());
			String jsonDevice = GsonUtil.toJson(commDevice);
			// 配置楼层
			CommFloorConfig commConfig = new CommFloorConfig();
			commConfig.setType(0);
			commConfig.setFloorList(config);
			String jsonConfig = GsonUtil.toJson(commConfig);
			String url =tkCommService.getTkCommUrl(device_num);
			String message = HttpRequest.sendPost(url
					+ "TKDevice/tk_SendCongfigTable.do?", "device="
					+ jsonDevice + "&config=" + jsonConfig);
			CommResult commRes = GsonUtil.toBean(message, CommResult.class);
			if (commRes!=null && commRes.getResultCode() == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean commFloorOpen(String device_num, Floor floor) {
		try {
			// 设备
			Device device = deviceDao.getInfoByTKDeviceNum(device_num);
			CommDevice commDevice = new CommDevice();
			commDevice.setMac(device.getMac());
			commDevice.setIp(device.getDevice_ip());
			commDevice.setPort(device.getDevice_port());
			commDevice.setGateway(device.getNetwork_gateway());
			commDevice.setSerialNo(device.getDevice_sequence());
			commDevice.setSubnet(device.getNetwork_mask());
			commDevice.setAddress(device.getAddress());
			String jsonDevice = GsonUtil.toJson(commDevice);
			// 楼层开放时区
			FloorOpenTimeZone commFloorOpen = new FloorOpenTimeZone();
			commFloorOpen.setFloor(Integer.valueOf(floor.getFloor_num()));
			List<TimeZone> timeList = new ArrayList<TimeZone>();
			TimeZone t1 = new TimeZone();
			String[] str1 = floor.getStart_time1().split(":");
			t1.setStartHour(str1[0]);
			t1.setStartMinute(str1[1]);
			String[] str2 = floor.getEnd_time1().split(":");
			t1.setEndHour(str2[0]);
			t1.setEndMinute(str2[1]);
			timeList.add(0, t1);
			TimeZone t2 = new TimeZone();
			String[] str3 = floor.getStart_time2().split(":");
			t2.setStartHour(str3[0]);
			t2.setStartMinute(str3[1]);
			String[] str4 = floor.getEnd_time2().split(":");
			t2.setEndHour(str4[0]);
			t2.setEndMinute(str4[1]);
			timeList.add(1, t2);
			TimeZone t3 = new TimeZone();
			String[] str5 = floor.getStart_time3().split(":");
			t3.setStartHour(str5[0]);
			t3.setStartMinute(str5[1]);
			String[] str6 = floor.getEnd_time3().split(":");
			t3.setEndHour(str6[0]);
			t3.setEndMinute(str6[1]);
			timeList.add(2, t3);
			TimeZone t4 = new TimeZone();
			String[] str7 = floor.getStart_time4().split(":");
			t4.setStartHour(str7[0]);
			t4.setStartMinute(str7[1]);
			String[] str8 = floor.getEnd_time4().split(":");
			t4.setEndHour(str8[0]);
			t4.setEndMinute(str8[1]);
			timeList.add(3, t4);
			commFloorOpen.setTimeList(timeList);
			String jsonFloor = GsonUtil.toJson(commFloorOpen);
			String url = tkCommService.getTkCommUrl(device_num);
			String message = HttpRequest.sendPost(url
					+ "TKDevice/tk_SendFloorOpenTimezone.do?", "device="
					+ jsonDevice + "&floorOpenTimeZone=" + jsonFloor);
			CommResult commRes = GsonUtil.toBean(message, CommResult.class);
			if (commRes!=null && commRes.getResultCode() == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean commHoliday(String device_num, List<String> holidayDateList) {
		try {
			// 设备
			Device device = deviceDao.getInfoByTKDeviceNum(device_num);
			CommDevice commDevice = new CommDevice();
			commDevice.setMac(device.getMac());
			commDevice.setIp(device.getDevice_ip());
			commDevice.setPort(device.getDevice_port());
			commDevice.setGateway(device.getNetwork_gateway());
			commDevice.setSerialNo(device.getDevice_sequence());
			commDevice.setSubnet(device.getNetwork_mask());
			commDevice.setAddress(device.getAddress());
			String jsonDevice = GsonUtil.toJson(commDevice);
			// 节假日
			String jsonHoliday = GsonUtil.toJson(holidayDateList);
			String url = tkCommService.getTkCommUrl(device_num);
			String message = HttpRequest.sendPost(url
					+ "TKDevice/tk_SendHolidayTable.do?", "device="
					+ jsonDevice + "&holidayDateList=" + jsonHoliday);
			CommResult commRes = GsonUtil.toBean(message, CommResult.class);
			if (commRes!=null && commRes.getResultCode() == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean clearHoliday(String device_num, List holiList,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			if (commHoliday(device_num, holiList)) {
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}
	}
	/*public boolean clearHoliday(String device_num, List holiList,
			String login_user) {
		boolean result = false;
		Map<String, String> log = new HashMap<String, String>();
		try {
			if (commHoliday(device_num, holiList)) {
				Map map = new HashMap();
				map.put("device_num", device_num);
				map.put("delete_flag", 1);
				result = tKDeviceOpenTimeDao.resetHolidayTime(map);
			}
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
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除节假日");
			logDao.addLog(log);
		}

	}*/

	@Override
	public boolean cleanFloorOpenArea(Floor floor, String login_user) {
		try {
			String device_num = floor.getDevice_num();
			if (commFloorOpen(device_num, floor)) { // 连接接口成功
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			}
		}
	/*public boolean cleanFloorOpenArea(Floor floor, String login_user) {
		try {
			String device_num = floor.getDevice_num();
			if (commFloorOpen(device_num, floor)) { // 连接接口成功
				return deviceDao.setFloorOpenArea(floor);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/

	@Override
	public boolean issuedHoliForModify(String device_num, String holiday0,
			List<Holiday> holidayList, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			List<String> holiList = new ArrayList<String>();
			for (Holiday h : holidayList) {
				holiList.add(h.getHoliday_date());
			}

			// List<Holiday> holiListSended =
			// deviceDao.getSendHoliday(device_num);
			List<Holiday> holiListSended = deviceDao.getHolisByDevi(device_num);
			for (Holiday hSended : holiListSended) {// 已经下发过的加进来重新下发
				if (!(hSended.getHoliday_date().equals(holiday0))) {
					holiList.add(hSended.getHoliday_date());
				}
			}

			if (commHoliday(device_num, holiList)) { // 连接成功
				// deviceDao.delDeviceHoli("'" + device_num + "'");

				/*
				 * for (Holiday holiday : holidayList) { //
				 * deviceDao.insertHoliday(holiday);
				 * deviceDao.updateHoliSend_State(holiday, 0); }
				 */
				log.put("V_OP_TYPE", "业务");
				return true;
			} else {
				log.put("V_OP_TYPE", "异常");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "下发节假日");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean updateDeviceOpenTime(OpenTimeZone openTimeZoneParam,
			Device deviceParam) {
		CommDevice dev= new CommDevice();
		dev.setMac(deviceParam.getMac());
		dev.setIp(deviceParam.getDevice_ip());
		dev.setPort(deviceParam.getDevice_port());
		dev.setGateway(deviceParam.getNetwork_gateway());
		dev.setAddress(deviceParam.getAddress());
		dev.setSerialNo(deviceParam.getDevice_sequence());
		dev.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(dev);
		String openTimeZone = GsonUtil.toJson(openTimeZoneParam);
		
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		
		Map<String, String> mappro =
				  PropertiesToMap.propertyToMap("comm_interface.properties");
		   String str=HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)+"TKDevice/tk_SendElevatorOpenTimezone.do?"
				  ,"device="+json+"&openTimeZone="+openTimeZone); 
		   CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		try {
			 if(commRes!=null && commRes.getResultCode()==0){
					return true;
			 }else{
				 return false;
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean clearAuthorityByDevNum(String device_num,List<TkDevAuthorityCardModel> list,String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try{
			//boolean result = tKDeviceOpenTimeDao.clearAuthorityByDevNum(device_num);
			String floorStr1="";
			for(int i=0;i<list.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				List<String> floors = authorityByDeviceDao.getFloorNum2(list.get(i).getFloor_group_num());
				if(null==floors||floors.size()==0){
					floorStr1="";
				}else{
					for(String  floorNum:floors){
						floorStr1+=floorNum+"|";
					}
					floorStr1=floorStr1.substring(0, floorStr1.length()-1);
				}
				map.put("floor_list", floorStr1);
				map.put("device_ip", list.get(i).getDevice_ip());
				map.put("device_port", list.get(i).getDevice_port());
				map.put("address",list.get(i).getAddress());
				map.put("start_time", list.get(i).getBegin_valid_time());
				map.put("end_time", list.get(i).getEnd_valid_time());
				map.put("device_num", list.get(i).getDevice_num());
				map.put("card_num", list.get(i).getCard_num());
				map.put("card_type", list.get(i).getCard_type());
				map.put("object_num", list.get(i).getObject_num());
				map.put("floor_group_num", list.get(i).getFloor_group_num());
				map.put("action_flag", 1);
				result=tKDeviceOpenTimeDao.insertDeviceAuth(map);
			}
			if (null!=list&&!list.isEmpty()) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
				result = false;
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除授权数据");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除授权数据");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean setEleMoveParam(Device deviceParam, Map<String, Integer> map) {
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setAddress(deviceParam.getAddress());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		
		HardWareParam hardWarePa = new HardWareParam();
		hardWarePa.setFloorRelayTime((Integer) map.get("multi_opt_interval"));
		hardWarePa.setDirectRelayTime((Integer) map.get("single_opt_interval"));
		hardWarePa.setUp((Integer) map.get("device_up"));
		hardWarePa.setDown((Integer) map.get("device_down"));
		hardWarePa.setOpen((Integer) map.get("device_open"));
		hardWarePa.setClose((Integer) map.get("device_close"));
		
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		
		/*hardWarePa.setOpositeRelayTime((Integer) map.get("visitor_opt_interval"));*/
		hardWarePa.setTotalFloor(48);
		String hardWareParam = GsonUtil.toJson(hardWarePa);
			   /*Map<String, String> mappro =
					  PropertiesToMap.propertyToMap("comm_interface.properties");*/
			   String str=HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)+"TKDevice/tk_SendHardwareParam.do?"
					  ,"device="+json+"&hardWareParam="+hardWareParam); 
			   CommResult commRes = GsonUtil.toBean(str, CommResult.class);
			try {
			   if(commRes!=null && commRes.getResultCode()==0){
					return true;
			   }else{
				   return false;
			   }
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
	}

	@Override
	public boolean copyEleMoveParam(Map<String, Object> deviceMap,String copy_device_num) {
		boolean result = false;
		/*String[] nums = copy_device_num.split(",");*/
		//for (int i = 0; i < nums.length; i++) {
			Device copyDevice = deviceDao.getInfoByTKDeviceNum(copy_device_num);	
			CommDevice device = new CommDevice();
			device.setMac(copyDevice.getMac());
			device.setIp(copyDevice.getDevice_ip());
			device.setPort(copyDevice.getDevice_port());
			device.setGateway(copyDevice.getNetwork_gateway());
			device.setAddress(copyDevice.getAddress());
			device.setSerialNo(copyDevice.getDevice_sequence());
			device.setSubnet(copyDevice.getNetwork_mask());
			String json = GsonUtil.toJson(device);
			
			HardWareParam hardWarePa = new HardWareParam();
			hardWarePa.setFloorRelayTime(Integer.parseInt((String) deviceMap.get("multi_opt_interval")));
			hardWarePa.setDirectRelayTime(Integer.parseInt((String)deviceMap.get("single_opt_interval")));
			hardWarePa.setUp(Integer.parseInt((String)deviceMap.get("device_up")));
			hardWarePa.setDown(Integer.parseInt((String) deviceMap.get("device_down")));
			hardWarePa.setOpen(Integer.parseInt((String) deviceMap.get("device_open")));
			hardWarePa.setClose(Integer.parseInt((String)deviceMap.get("device_close")));
			hardWarePa.setTotalFloor(48);
			String hardWareParam = GsonUtil.toJson(hardWarePa);
			 /*  Map<String, String> mappro =
					  PropertiesToMap.propertyToMap("comm_interface.properties");*/
			   String str=HttpRequest.sendPost(tkCommService.getTkCommUrl(copy_device_num)+"TKDevice/tk_SendHardwareParam.do?"
					  ,"device="+json+"&hardWareParam="+hardWareParam); 
			   CommResult commRes = GsonUtil.toBean(str, CommResult.class);
			try {
			   if(commRes!=null && commRes.getResultCode()==0){
					result = true;
			   }else{
				   result = false;
			   }
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		return result;
	}

	@Override
	public boolean copyEleOpenTime(String device_num, String copy_device_num) {

		List<TKDeviceOpenTimeModel> list = tKDeviceOpenTimeDao
				.checkDevTimeGroup(device_num);//被复制的电梯开放时区
		if (null == list || list.isEmpty()) {
			return false;
		}
		/*String[] deviceNums = copy_device_num.split(",");*/
		boolean  res = false;
		//for (int i = 0; i < deviceNums.length; i++) {
			Device device = tKDeviceOpenTimeDao.getEleMoveParam(copy_device_num);//选中的复制设备
			for (int j = 0; j < list.size(); j++) {
				TimeZone timeZone0 = new TimeZone();
				OpenTimeZone openTimeZone = new OpenTimeZone();

				/*TKDeviceOpenTimeModel tKDeviceOpenTimeModel = new TKDeviceOpenTimeModel();
				tKDeviceOpenTimeModel.setStart_time1(list.get(j)
						.getStart_time1());
				tKDeviceOpenTimeModel.setEnd_time1(list.get(j).getEnd_time1());*/
				String[] startTime1 = list.get(j).getStart_time1().split(":");
				String[] endTime1 = list.get(j).getEnd_time1().split(":");
				timeZone0.setStartHour(startTime1[0]);
				timeZone0.setStartMinute(startTime1[1]);
				timeZone0.setEndHour(endTime1[0]);
				timeZone0.setEndMinute(endTime1[1]);
				openTimeZone.getTimeZoneList().add(0, timeZone0);

				TimeZone timeZone1 = new TimeZone();
			/*	tKDeviceOpenTimeModel.setStart_time2(list.get(j)
						.getStart_time2());
				tKDeviceOpenTimeModel.setEnd_time2(list.get(j).getEnd_time2());*/
				String[] startTime2 = list.get(j).getStart_time2().split(":");
				String[] endTime2 = list.get(j).getEnd_time2().split(":");
				timeZone1.setStartHour(startTime2[0]);
				timeZone1.setStartMinute(startTime2[1]);
				timeZone1.setEndHour(endTime2[0]);
				timeZone1.setEndMinute(endTime2[1]);
				openTimeZone.getTimeZoneList().add(1, timeZone1);

				TimeZone timeZone2 = new TimeZone();
				/*tKDeviceOpenTimeModel.setStart_time3(list.get(j)
						.getStart_time3());
				tKDeviceOpenTimeModel.setEnd_time3(list.get(j).getEnd_time3());*/
				String[] startTime3 = list.get(j).getStart_time3().split(":");
				String[] endTime3 = list.get(j).getEnd_time3().split(":");
				timeZone2.setStartHour(startTime3[0]);
				timeZone2.setStartMinute(startTime3[1]);
				timeZone2.setEndHour(endTime3[0]);
				timeZone2.setEndMinute(endTime3[1]);
				openTimeZone.getTimeZoneList().add(2, timeZone2);

				TimeZone timeZone3 = new TimeZone();
				/*tKDeviceOpenTimeModel.setStart_time4(list.get(j)
						.getStart_time4());
				tKDeviceOpenTimeModel.setEnd_time4(list.get(j).getEnd_time4());*/
				String[] startTime4 = list.get(j).getStart_time4().split(":");
				String[] endTime4 = list.get(j).getEnd_time4().split(":");
				timeZone3.setStartHour(startTime4[0]);
				timeZone3.setStartMinute(startTime4[1]);
				timeZone3.setEndHour(endTime4[0]);
				timeZone3.setEndMinute(endTime4[1]);
				openTimeZone.getTimeZoneList().add(3, timeZone3);

				TimeZone timeZone4 = new TimeZone();
				/*tKDeviceOpenTimeModel.setStart_time5(list.get(j)
						.getStart_time5());
				tKDeviceOpenTimeModel.setEnd_time5(list.get(j).getEnd_time5());*/
				String[] startTime5 = list.get(j).getStart_time5().split(":");
				String[] endTime5 = list.get(j).getEnd_time5().split(":");
				timeZone4.setStartHour(startTime5[0]);
				timeZone4.setStartMinute(startTime5[1]);
				timeZone4.setEndHour(endTime5[0]);
				timeZone4.setEndMinute(endTime5[1]);
				openTimeZone.getTimeZoneList().add(4, timeZone4);
				openTimeZone.setWeekDay(list.get(j).getWeek_day());
				/*tKDeviceOpenTimeModel.setWeek_day(list.get(j).getWeek_day());
				tKDeviceOpenTimeModel.setDevice_num(deviceNums[i]);*/
				
				/*res = copyDevOpenTime(openTimeZone,
						tKDeviceOpenTimeModel, device);*/

				res = copyDevOpenTime(openTimeZone,device);
				if(!res){
					break;
				}else{
					res = true;
				}
			}
		return res;
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-15 下午4:44:08
	 * @功能描述: 复制电梯开放时区调通信服务器 
	 * @参数描述:
	 */
	public boolean copyDevOpenTime(OpenTimeZone openTimeZoneParam,
			Device deviceParam) {
		
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setAddress(deviceParam.getAddress());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String openTimeZone = GsonUtil.toJson(openTimeZoneParam);
		String json = GsonUtil.toJson(device);
		boolean result = false;
		Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceParam.getDevice_num())
				+ "TKDevice/tk_SendElevatorOpenTimezone.do?", "device=" + json
				+ "&openTimeZone=" + openTimeZone);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
			if (commRes!=null && commRes.getResultCode() == 0) {
				result = true;
			} else {
				result = false;
			}
		return result;
	}
	
	@Override
	public List<Map<String, Object>> getFlagFloor(String device_num){
		try{
			return deviceDao.getFlagFloor(device_num);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean setIfEnabledFloor(List<Map<String, Object>> list, String login_user) throws Exception{
		Map<String, String> log = new HashMap<String, String>();
		try {
			for(Map<String, Object> map : list){
				boolean result = deviceDao.setIfEnabledFloor(map);
				if(!result){
					return false;
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e){
			log.put("V_OP_TYPE", "异常");
			throw e;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "设置启用楼层");
			logDao.addLog(log);
		}
	}
	
	@Override
	public List<String> getIfEnabledFloor(Map<String, Object> map){
		try{
			return deviceDao.getIfEnabledFloor(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
}
