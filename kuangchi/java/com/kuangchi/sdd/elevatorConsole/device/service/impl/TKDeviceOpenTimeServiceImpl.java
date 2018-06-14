package com.kuangchi.sdd.elevatorConsole.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.Time;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao.AuthorityByDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.dao.ITKDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.dao.TKDeviceOpenTimeDao;
import com.kuangchi.sdd.elevatorConsole.device.model.CommDevice;
import com.kuangchi.sdd.elevatorConsole.device.model.CommIpInfoModel;
import com.kuangchi.sdd.elevatorConsole.device.model.CommResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.HardWareParam;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.model.OpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;
import com.kuangchi.sdd.elevatorConsole.device.service.TKDeviceOpenTimeService;
import com.kuangchi.sdd.elevatorConsole.device.util.CommTimeUtil;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

@Transactional
@Service("tKDeviceOpenTimeServiceImpl")
public class TKDeviceOpenTimeServiceImpl implements TKDeviceOpenTimeService {
	@Resource(name = "tKDeviceOpenTimeDaoImpl")
	private TKDeviceOpenTimeDao tKDeviceOpenTimeDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Resource(name = "tkDeviceDao")
	private ITKDeviceDao deviceDao;

	@Autowired
	private AuthorityByDeviceDao authorityByDeviceDao;


	@Resource(name = "peopleAuthorityManagerDaoImpl")
	private PeopleAuthorityManagerDao peopleAuthorityManagerDao;
	
	@Resource(name = "tKDeviceOpenTimeServiceImpl")
	private TKDeviceOpenTimeService tKDeviceOpenTimeService;
	
	@Resource(name = "tkCommServiceImpl")
	private TkCommService tkCommService;

	@Override
	public boolean initDeviceParam(Device deviceParam, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "初始化设备信息");
		log.put("V_OP_FUNCTION", "初始化设备");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_TYPE", "业务");
		boolean result1 = false;
		boolean result2 = false;
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setAddress(deviceParam.getAddress());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		try {
			
			String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
					+ "TKDevice/tk_SystemInit.do?", "&device=" + json);
			CommResult commRes = GsonUtil.toBean(str, CommResult.class);
			if (commRes!=null && commRes.getResultCode() == 0) {
				
				for (int j = 1; j <= 8; j++) {// 重置电梯开放时区
					Map map = new HashMap();
					map.put("device_num", deviceParam.getDevice_num());
					map.put("week_day", j);
					if (!tKDeviceOpenTimeDao.resetEleOpenTime(map)) {
						result1 = false;
						log.put("V_OP_MSG", "初始化设备信息失败");
						logDao.addLog(log);
						return result1;
					} else {
						result1 = true;
					}
				}
				Map map = new HashMap();
				map.put("delete_flag", 1);
				map.put("device_num", deviceParam.getDevice_num());
				boolean resVal = tKDeviceOpenTimeService.resetHolidayTime(map,
						login_user);
				if (!resVal) {
					return false;
				}
				Map eleMap = new HashMap();
				eleMap.put("device_num", deviceParam.getDevice_num());
				eleMap.put("multi_opt_interval", 5);
				eleMap.put("single_opt_interval", 2);
				eleMap.put("visitor_opt_interval", 1);
				eleMap.put("device_up", 1);
				eleMap.put("device_down", 1);
				eleMap.put("device_open", 0);
				eleMap.put("device_close", 0);
				boolean resVal1 = tKDeviceOpenTimeService.resetEleParam(eleMap,
						login_user);
				if (!resVal1) {
					return false;
				}
				for (int j = 1; j <= 48; j++) {// 配置楼层
					Floor floor = new Floor();
					floor.setFloor_num("" + j);
					floor.setFloor_name("第" + j + "层");
					floor.setDevice_num(deviceParam.getDevice_num());
					floor.setConfiguration_floor("" + j);
					floor.setStart_time1("00:00");
					floor.setStart_time2("00:00");
					floor.setStart_time3("00:00");
					floor.setStart_time4("00:00");
					floor.setEnd_time1("00:00");
					floor.setEnd_time2("00:00");
					floor.setEnd_time3("00:00");
					floor.setEnd_time4("00:00");
					floor.setDescription("");
					if (!tKDeviceOpenTimeService.resetFloorConfig(floor)) {
						result2 = false;
						log.put("V_OP_MSG", "初始化设备信息失败");
						logDao.addLog(log);
						return result2;
					} else {
						result2 = true;
					}
				}
				
				List<TkDevAuthorityCardModel> list = tKDeviceOpenTimeService
						.getAuthorityCardByDevNum(deviceParam.getDevice_num());
				boolean result = true;
				if(list!=null && !list.isEmpty()) {
					for(int i=0;i<list.size();i++){
						Map map1 = new HashMap();
						map1.put("card_num", list.get(i).getCard_num());
						map1.put("card_type", list.get(i).getCard_type());
						map1.put("start_time", list.get(i).getBegin_valid_time());
						map1.put("end_time", list.get(i).getEnd_valid_time());
						map1.put("floor_list", list.get(i).getFloor_list());
						map1.put("action_flag", 1);
						map1.put("result_flag", 0);
						map1.put("object_num", list.get(i).getObject_num());
						map1.put("object_type", list.get(i).getObject_type());
						map1.put("floor_group_num", list.get(i).getFloor_group_num());
						map1.put("device_num", list.get(i).getDevice_num());
						map1.put("try_times", 1);
						result = tKDeviceOpenTimeDao.insertAuthorityHistory(map1);//添加到权限历史表
						if(!result){
							break;
						}
					}
				}
				boolean res = tKDeviceOpenTimeDao.deleteAuthorityBydevNum(deviceNum);//删除权限表记录
				
					/*result = tKDeviceOpenTimeService.clearAuthorityByDevNum(
							deviceParam.getDevice_num(), list, login_user);*/
				
				
				

				if (result1 && result2 && result && res) {
					log.put("V_OP_MSG", "初始化设备信息成功");
					logDao.addLog(log);
					return true;
				} else {
					log.put("V_OP_MSG", "初始化设备信息失败");
					logDao.addLog(log);
					throw new RuntimeException();// 注意写回滚注解
				}
			} else {
				log.put("V_OP_MSG", "初始化设备信息失败，连接异常");
				logDao.addLog(log);
				throw new RuntimeException();// 注意写回滚注解
				}
			/*}else{
				log.put("V_OP_MSG", "初始化设备信息失败，连接异常");
				logDao.addLog(log);
				throw new RuntimeException();// 注意写回滚注解
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "初始化设备信息失败");
			logDao.addLog(log);
			return false;
		}
		
	}

	@Override
	public TimeResultMsg getDevTime(Device deviceParam) {
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setAddress(deviceParam.getAddress());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		TimeResultMsg timeResultMsg = new TimeResultMsg();
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		/*Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
				+ "TKDevice/tk_RecvClock.do?", "&device=" + json);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		if (null != commRes) {
			if (commRes.getResultCode() == 0) {
				Time time = CommTimeUtil.TransformCommTime(commRes
						.getResultTxt());
				timeResultMsg.setTime(time);
				timeResultMsg.setResult_code("0");
			} else {
				timeResultMsg.setResult_code("1");
			}
			return timeResultMsg;
		} else {
			timeResultMsg.setResult_code("1");
			return timeResultMsg;
		}
	}

	@Override
	public CommResult setDevTime(Device deviceParam) {
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setAddress(deviceParam.getAddress());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		CommResult commRes = new CommResult();
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		try {
			/*Map<String, String> mappro = PropertiesToMap
					.propertyToMap("comm_interface.properties");*/
			String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
					+ "TKDevice/tk_SendClock.do?", "&device=" + json);
			commRes = GsonUtil.toBean(str, CommResult.class);
			if (null != commRes) {
				if (0 == commRes.getResultCode()) {
					commRes.setResultTxt("校时成功");
				} else {
					commRes.setResultTxt("校时失败");
				}
			}else {
				commRes.setResultTxt("校时失败，连接失败");
			}
		} catch (Exception e) {
			commRes.setResultTxt("校时失败");
			e.printStackTrace();
		}
		return commRes;
	}

	@Override
	public Grid<TimesGroupModel> getAllTimesGroupByDevNum() {
		Grid<TimesGroupModel> grid = new Grid<TimesGroupModel>();
		List<TimesGroupModel> list = tKDeviceOpenTimeDao
				.getAllTimesGroupByDevNumList();
		grid.setRows(list);
		if (null != list) {
			Integer count = tKDeviceOpenTimeDao.getAllTimesGroupByDevNumCount();
			grid.setTotal(count);
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public boolean updateDeviceOpenTime(OpenTimeZone openTimeZoneParam,
			Device deviceParam, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		CommDevice dev = new CommDevice();
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
	/*	Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
				+ "TKDevice/tk_SendElevatorOpenTimezone.do?", "device=" + json
				+ "&openTimeZone=" + openTimeZone);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		try {
			if (commRes!=null && commRes.getResultCode() == 0) {
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
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "设置电梯开放时区");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean motifyDeviceOpenTime(OpenTimeZone openTimeZoneParam,
			Device deviceParam, Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setAddress(deviceParam.getAddress());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		String openTimeZone = GsonUtil.toJson(openTimeZoneParam);
		map.put("device_num", deviceParam.getDevice_num());
		boolean result = false;
		/*Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceParam.getDevice_num())
				+ "TKDevice/tk_SendElevatorOpenTimezone.do?", "device=" + json
				+ "&openTimeZone=" + openTimeZone);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		try {
			if (commRes!=null && commRes.getResultCode() == 0) {
				result = tKDeviceOpenTimeDao.motifyDeviceOpenTime(map);
				if (result) {
					log.put("V_OP_TYPE", "业务");
				} else {
					log.put("V_OP_TYPE", "异常");
				}
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "电梯开放时区");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "设置电梯开放时区");
			logDao.addLog(log);
		}
		return result;

	}

	@Override
	public Integer getDeviceOpenTimeByDevNum(String device_num) {
		return tKDeviceOpenTimeDao.getDeviceOpenTimeByDevNum(device_num);
	}

	@Override
	public boolean insertDeviceOpenTime(String device_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao
					.insertDeviceOpenTime(device_num);
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
			log.put("V_OP_FUNCTION", "搜索电梯设备功能");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "初始化设置电梯开放时区");
			logDao.addLog(log);
		}

	}

	@Override
	public List<TKDeviceOpenTimeModel> checkDevTimeGroup(String device_num) {
		return tKDeviceOpenTimeDao.checkDevTimeGroup(device_num);
	}

	@Override
	public boolean deleteDevOpenTime(String device, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.deleteDevOpenTime(device);
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
			log.put("V_OP_FUNCTION", "删除电梯设备");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除电梯设备");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean copyDevOpenTime(OpenTimeZone openTimeZoneParam,
			TKDeviceOpenTimeModel tKDeviceOpenTimeModel, Device deviceParam,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
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
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		boolean result = false;
		/*Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
				+ "TKDevice/tk_SendElevatorOpenTimezone.do?", "device=" + json
				+ "&openTimeZone=" + openTimeZone);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		try {
			if (commRes!=null && commRes.getResultCode() == 0) {
				result = tKDeviceOpenTimeDao
						.copyDevOpenTime(tKDeviceOpenTimeModel);
				if (result) {
					log.put("V_OP_TYPE", "业务");
				} else {
					log.put("V_OP_TYPE", "异常");
				}
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "复制电梯开放时区");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "复制电梯开放时区");
			logDao.addLog(log);
		}
		return result;

	}

	/*
	 * try{ boolean result =
	 * tKDeviceOpenTimeDao.copyDevOpenTime(tKDeviceOpenTimeModel); if (result) {
	 * log.put("V_OP_TYPE", "业务"); } else { log.put("V_OP_TYPE", "异常"); } return
	 * result; }catch(Exception e) { e.printStackTrace(); log.put("V_OP_TYPE",
	 * "异常"); return false; } finally{ log.put("V_OP_NAME", "梯控设备管理");
	 * log.put("V_OP_FUNCTION", "复制电梯开放时区"); log.put("V_OP_ID", login_user);
	 * log.put("V_OP_MSG", "复制电梯开放时区"); logDao.addLog(log); }
	 */

	@Override
	public boolean setEleMoveParam(Device deviceParam, Map map,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setAddress(deviceParam.getAddress());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String json = GsonUtil.toJson(device);
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		
		HardWareParam hardWarePa = new HardWareParam();
		hardWarePa.setFloorRelayTime((Integer) map.get("multi_opt_interval"));
		hardWarePa.setDirectRelayTime((Integer) map.get("single_opt_interval"));
		// hardWarePa.setOpositeRelayTime((Integer)
		// map.get("visitor_opt_interval"));
		hardWarePa.setTotalFloor(48);
		String hardWareParam = GsonUtil.toJson(hardWarePa);
		boolean result = false;
		/*Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
				+ "TKDevice/tk_SendHardwareParam.do?", "device=" + json
				+ "&hardWareParam=" + hardWareParam);
		CommResult commRes = GsonUtil.toBean(str, CommResult.class);
		try {
			if (commRes!=null && commRes.getResultCode() == 0) {
				result = tKDeviceOpenTimeDao.setEleMoveParam(map);
				if (result) {
					log.put("V_OP_TYPE", "业务");
				} else {
					log.put("V_OP_TYPE", "异常");
				}
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改成功");
			logDao.addLog(log);
		}
		return result;
	}

	/*
	 * try{ boolean result = tKDeviceOpenTimeDao.setEleMoveParam(map); if
	 * (result) { log.put("V_OP_TYPE", "业务"); } else { log.put("V_OP_TYPE",
	 * "异常"); } return result; }catch(Exception e) { e.printStackTrace();
	 * log.put("V_OP_TYPE", "异常"); return false; } finally{ log.put("V_OP_NAME",
	 * "梯控设备管理"); log.put("V_OP_FUNCTION", "电梯动作参数"); log.put("V_OP_ID",
	 * login_user); log.put("V_OP_MSG", "设置电梯动作参数"); logDao.addLog(log); }
	 */

	@Override
	public Device getEleMoveParam(String device_num) {
		return tKDeviceOpenTimeDao.getEleMoveParam(device_num);
	}

	@Override
	public boolean clearAuthorityByDevNum(String device_num,
			List<TkDevAuthorityCardModel> list, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		boolean result1 = false;
		try {
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				map.put("floor_list", list.get(i).getFloor_list());
				map.put("device_ip", list.get(i).getDevice_ip());
				map.put("device_port", list.get(i).getDevice_port());
				map.put("address", list.get(i).getAddress());
				map.put("start_time", list.get(i).getBegin_valid_time());
				map.put("end_time", list.get(i).getEnd_valid_time());
				map.put("device_num", list.get(i).getDevice_num());
				map.put("card_num", list.get(i).getCard_num());
				map.put("card_type", list.get(i).getCard_type());
				map.put("object_num", list.get(i).getObject_num());
				map.put("floor_group_num", list.get(i).getFloor_group_num());
				map.put("object_type", list.get(i).getObject_type());
				map.put("action_flag", 1);
				Map delMap = new HashMap();
				delMap.put("id",list.get(i).getId()+"");
				delMap.put("state", "10");
				peopleAuthorityManagerDao.updTkAuthRecord(delMap);//修改权限表的标志位为10（待删除）
				result = tKDeviceOpenTimeDao.insertDeviceAuth(map);
				if(!result){
					break;
				}
			}
			boolean res = true;
			if(!result){
				log.put("V_OP_TYPE", "异常");
				result = false;
				return result;
			}else{
				
				while(res){
					List<TkDevAuthorityCardModel> list0 = tKDeviceOpenTimeService
							.getAuthorityCardByDevNum(device_num);
					if(list0.isEmpty()){
						res = false;
						break;
					}else{
						res = true;
					}
				}
			}
			
			if(!res){
				result = true;
			}
			
			log.put("V_OP_TYPE", "业务");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除授权数据");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改成功");
			logDao.addLog(log);
		}
	}

	@Override
	public HardWareParam getEleLevel(Device deviceParam) {
		CommDevice device = new CommDevice();
		device.setMac(deviceParam.getMac());
		device.setIp(deviceParam.getDevice_ip());
		device.setPort(deviceParam.getDevice_port());
		device.setGateway(deviceParam.getNetwork_gateway());
		device.setAddress(deviceParam.getAddress());
		device.setSerialNo(deviceParam.getDevice_sequence());
		device.setSubnet(deviceParam.getNetwork_mask());
		String deviceNum=tkCommService.getTkDevNumByMac(deviceParam.getMac());
		String json = GsonUtil.toJson(device);

		/*Map<String, String> mappro = PropertiesToMap
				.propertyToMap("comm_interface.properties");*/
		String str = HttpRequest.sendPost(tkCommService.getTkCommUrl(deviceNum)
				+ "TKDevice/tk_GetHardWareParam.do?", "device=" + json);
		HardWareParam hardWareParam = GsonUtil.toBean(str, HardWareParam.class);
		return hardWareParam;

	}

	@Override
	public boolean copyEleOpenTime(String device_num, String copy_device_num,
			String login_user) {
		List<TKDeviceOpenTimeModel> list = tKDeviceOpenTimeDao
				.checkDevTimeGroup(device_num);//被复制的开放时区
		if (null == list || list.isEmpty()) {
			return false;
		}
		String[] deviceNums = copy_device_num.split(",");
		TKDeviceOpenTimeModel tKDeviceOpenTimeModel = new TKDeviceOpenTimeModel();

		for (int i = 0; i < deviceNums.length; i++) {
			Device device = tKDeviceOpenTimeDao.getEleMoveParam(deviceNums[i]);//选中的复制设备
			for (int j = 0; j < list.size(); j++) {
				TimeZone timeZone0 = new TimeZone();
				OpenTimeZone openTimeZone = new OpenTimeZone();

				tKDeviceOpenTimeModel.setStart_time1(list.get(j)
						.getStart_time1());
				tKDeviceOpenTimeModel.setEnd_time1(list.get(j).getEnd_time1());
				String[] startTime1 = list.get(j).getStart_time1().split(":");
				String[] endTime1 = list.get(j).getEnd_time1().split(":");
				timeZone0.setStartHour(startTime1[0]);
				timeZone0.setStartMinute(startTime1[1]);
				timeZone0.setEndHour(endTime1[0]);
				timeZone0.setEndMinute(endTime1[1]);
				openTimeZone.getTimeZoneList().add(0, timeZone0);

				TimeZone timeZone1 = new TimeZone();
				tKDeviceOpenTimeModel.setStart_time2(list.get(j)
						.getStart_time2());
				tKDeviceOpenTimeModel.setEnd_time2(list.get(j).getEnd_time2());
				String[] startTime2 = list.get(j).getStart_time2().split(":");
				String[] endTime2 = list.get(j).getEnd_time2().split(":");
				timeZone1.setStartHour(startTime2[0]);
				timeZone1.setStartMinute(startTime2[1]);
				timeZone1.setEndHour(endTime2[0]);
				timeZone1.setEndMinute(endTime2[1]);
				openTimeZone.getTimeZoneList().add(1, timeZone1);

				TimeZone timeZone2 = new TimeZone();
				tKDeviceOpenTimeModel.setStart_time3(list.get(j)
						.getStart_time3());
				tKDeviceOpenTimeModel.setEnd_time3(list.get(j).getEnd_time3());
				String[] startTime3 = list.get(j).getStart_time3().split(":");
				String[] endTime3 = list.get(j).getEnd_time3().split(":");
				timeZone2.setStartHour(startTime3[0]);
				timeZone2.setStartMinute(startTime3[1]);
				timeZone2.setEndHour(endTime3[0]);
				timeZone2.setEndMinute(endTime3[1]);
				openTimeZone.getTimeZoneList().add(2, timeZone2);

				TimeZone timeZone3 = new TimeZone();
				tKDeviceOpenTimeModel.setStart_time4(list.get(j)
						.getStart_time4());
				tKDeviceOpenTimeModel.setEnd_time4(list.get(j).getEnd_time4());
				String[] startTime4 = list.get(j).getStart_time4().split(":");
				String[] endTime4 = list.get(j).getEnd_time4().split(":");
				timeZone3.setStartHour(startTime4[0]);
				timeZone3.setStartMinute(startTime4[1]);
				timeZone3.setEndHour(endTime4[0]);
				timeZone3.setEndMinute(endTime4[1]);
				openTimeZone.getTimeZoneList().add(3, timeZone3);

				TimeZone timeZone4 = new TimeZone();
				tKDeviceOpenTimeModel.setStart_time5(list.get(j)
						.getStart_time5());
				tKDeviceOpenTimeModel.setEnd_time5(list.get(j).getEnd_time5());
				String[] startTime5 = list.get(j).getStart_time5().split(":");
				String[] endTime5 = list.get(j).getEnd_time5().split(":");
				timeZone4.setStartHour(startTime5[0]);
				timeZone4.setStartMinute(startTime5[1]);
				timeZone4.setEndHour(endTime5[0]);
				timeZone4.setEndMinute(endTime5[1]);
				openTimeZone.getTimeZoneList().add(4, timeZone4);
				openTimeZone.setWeekDay(list.get(j).getWeek_day());
				tKDeviceOpenTimeModel.setWeek_day(list.get(j).getWeek_day());
				tKDeviceOpenTimeModel.setDevice_num(deviceNums[i]);
				if (!tKDeviceOpenTimeService.copyDevOpenTime(openTimeZone,
						tKDeviceOpenTimeModel, device, login_user)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<TkDevAuthorityCardModel> getAuthorityCardByDevNum(
			String device_num) {
		return tKDeviceOpenTimeDao.getAuthorityCardByDevNum(device_num);
	}

	@Override
	public boolean resetHolidayTime(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.resetHolidayTime(map);
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
			log.put("V_OP_MSG", "清除节假日开放时区");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean resetFloorTime(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.resetFloorTime(map);
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
			log.put("V_OP_MSG", "清除楼层开放时区");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean resetEleOpenTime(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			for (int j = 1; j <= 8; j++) {
				map.put("week_day", j);
				result = tKDeviceOpenTimeDao.resetEleOpenTime(map);
				if (result) {
					log.put("V_OP_TYPE", "异常");
					break;
				}
			}
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				throw new RuntimeException();// 注意写回滚注解
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			throw new RuntimeException();// 注意写回滚注解
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除电梯开放时区");
			logDao.addLog(log);
		}
		return result;
	}

	@Override
	public boolean resetFloors(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.resetFloors(map);
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
			log.put("V_OP_MSG", "清除配置楼层");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean resetFloorConfig(Floor floor) {
		return tKDeviceOpenTimeDao.resetFloorConfig(floor);
	}

	@Override
	public boolean resetEleParam(Map eleMap, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.resetEleParam(eleMap);
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
			log.put("V_OP_FUNCTION", "初始化设备");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除设备节假日");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean copyAuthorityByDevNum(String targetDeviceNum,
			List<TkDevAuthorityCardModel> list, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			// boolean result =
			// tKDeviceOpenTimeDao.clearAuthorityByDevNum(device_num);
			String floorStr1 = "";
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				List<String> floors = authorityByDeviceDao.getFloorNum2(list
						.get(i).getFloor_group_num());
				if (null == floors || floors.size() == 0) {
					floorStr1 = "";
				} else {
					for (String floorNum : floors) {
						floorStr1 += floorNum + "|";
					}
					floorStr1 = floorStr1.substring(0, floorStr1.length() - 1);
				}
				map.put("floor_list", floorStr1);
				map.put("device_ip", list.get(i).getDevice_ip());
				map.put("device_port", list.get(i).getDevice_port());
				map.put("address", list.get(i).getAddress());
				map.put("start_time", list.get(i).getBegin_valid_time());
				map.put("end_time", list.get(i).getEnd_valid_time());
				map.put("device_num", targetDeviceNum);
				map.put("card_num", list.get(i).getCard_num());
				map.put("card_type", list.get(i).getCard_type());
				map.put("object_num", list.get(i).getObject_num());
				map.put("floor_group_num", list.get(i).getFloor_group_num());
				map.put("action_flag", 0);
				result = tKDeviceOpenTimeDao.insertDeviceAuth(map);
			}
			if (null != list && !list.isEmpty()) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
				result = false;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "复制权限数据");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "复制权限数据");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean cleanConfigFloor(List<Floor> floors, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean reStr = false;
		try {
			for (Floor floor : floors) {
				reStr = deviceDao.updateDeviceFloor(floor);
				if (!reStr) {
					log.put("V_OP_TYPE", "异常");
					break;
				}
			}
			if (reStr) {
				log.put("V_OP_TYPE", "业务");
			} else {
				throw new RuntimeException();// 注意写回滚注解
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			throw new RuntimeException();// 注意写回滚注解
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除配置楼层");
			logDao.addLog(log);
		}
		return reStr;
	}

	@Override
	public boolean cleanFloorOpenArea(List<Floor> list, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			for (Floor floor : list) {
				result = deviceDao.setFloorOpenArea(floor);
				if (!result) {
					log.put("V_OP_TYPE", "异常");
					break;
				}
			}
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				throw new RuntimeException();// 注意写回滚注解
			}
		} catch (Exception e) {
			log.put("V_OP_TYPE", "异常");
			throw new RuntimeException();// 注意写回滚注解
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除楼层开放时区");
			logDao.addLog(log);
		}
		return result;
	}

	/*
	 * public boolean cleanFloorOpenArea(List<Floor> list, String login_user) {
	 * Map<String, String> log = new HashMap<String, String>(); try {
	 * if(result){ log.put("V_OP_TYPE", "业务"); return result; }else{
	 * log.put("V_OP_TYPE", "异常"); return result; } }catch (Exception e) {
	 * e.printStackTrace(); log.put("V_OP_TYPE", "异常"); return false; } finally
	 * { log.put("V_OP_NAME", "梯控设备管理"); log.put("V_OP_FUNCTION", "清除设置");
	 * log.put("V_OP_ID", login_user); log.put("V_OP_MSG", "清除楼层开放时区");
	 * logDao.addLog(log); } return false; }
	 */

	@Override
	public boolean cleanHoliday(String device_num, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		Map map = new HashMap();
		map.put("device_num", device_num);
		map.put("delete_flag", 1);
		try {
			boolean result = tKDeviceOpenTimeDao.resetHolidayTime(map);
			if (result) {
				log.put("V_OP_TYPE", "业务");
				return result;
			} else {
				log.put("V_OP_TYPE", "异常");
				throw new RuntimeException();// 回滚事物
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			throw new RuntimeException();
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除节假日");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean cleanEleMoveParam(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			boolean result = tKDeviceOpenTimeDao.setEleMoveParam(map);
			if (result) {
				log.put("V_OP_TYPE", "业务");
				return result;
			} else {
				log.put("V_OP_TYPE", "异常");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			throw new RuntimeException();
		} finally {
			log.put("V_OP_NAME", "梯控设备管理");
			log.put("V_OP_FUNCTION", "清除设置");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清除动作参数");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean cleanElevatorSet(Map map, String login_user) {
		Integer resVal = (Integer) map.get("resVal");
		Integer resVal1 = (Integer) map.get("resVal1");
		Integer resVal2 = (Integer) map.get("resVal2");
		Integer resVal3 = (Integer) map.get("resVal3");
		Integer resVal4 = (Integer) map.get("resVal4");
		Integer resVal5 = (Integer) map.get("resVal5");
		String device_num = (String) map.get("device_num");
		List<Floor> floors = (List<Floor>) map.get("floor");
		List<TkDevAuthorityCardModel> authorityList = (List<TkDevAuthorityCardModel>) map.get("authorityList");
		Map<String, String> log = new HashMap<String, String>();
		boolean reStr = false;
		
		if (0 != resVal) {
			if (1 == resVal) {
				try {
					for (Floor floor : floors) {
						reStr = deviceDao.updateDeviceFloor(floor);
						if (!reStr) {
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "清除成功，保存失败");
							break;
						}
					}
					if (reStr) {
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_MSG", "清除配置楼层成功");
						resVal = 11;
					} else {
						resVal = 12;
						throw new RuntimeException();// 注意写回滚注解
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					resVal = 12;
					throw new RuntimeException();// 注意写回滚注解
				} finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					logDao.addLog(log);
				}
			}
		}
		
		if (0 != resVal2) {
			if (1 == resVal2) {
				boolean result = false;
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
					for (Floor flo : list) {
						result = deviceDao.setFloorOpenArea(flo);
						if (!result) {
							log.put("V_OP_MSG", "清除成功，保存失败");
							log.put("V_OP_TYPE", "异常");
							break;
						}
					}
					if (result) {
						resVal2 = 11;
						log.put("V_OP_MSG", "清除楼层开放时区成功");
						log.put("V_OP_TYPE", "业务");
					} else {
						resVal2 = 12;
						throw new RuntimeException();// 注意写回滚注解
					}
				} catch (Exception e) {
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					resVal2 = 12;
					throw new RuntimeException();// 注意写回滚注解
				} finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					logDao.addLog(log);
				}
			}
		}
		
		/*boolean res = false;
		for(int i=0;i<authorityList.size();i++){
			Map mapParm = new HashMap();
			List<String> flooCons = authorityByDeviceDao.getFloorNum2(authorityList.get(i).getFloor_group_num());
			if(null==flooCons||flooCons.size()==0){
				floorStr1="";
			}else{
				for(String  floorNum:flooCons){
					floorStr1+=floorNum+"|";
				}
				floorStr1=floorStr1.substring(0, floorStr1.length()-1);
			}
			mapParm.put("floor_list", authorityList.get(i).getFloor_list());
			mapParm.put("device_ip", authorityList.get(i).getDevice_ip());
			mapParm.put("device_port", authorityList.get(i).getDevice_port());
			mapParm.put("address",authorityList.get(i).getAddress());
			mapParm.put("start_time", authorityList.get(i).getBegin_valid_time());
			mapParm.put("end_time", authorityList.get(i).getEnd_valid_time());
			mapParm.put("device_num", authorityList.get(i).getDevice_num());
			mapParm.put("card_num", authorityList.get(i).getCard_num());
			mapParm.put("card_type", authorityList.get(i).getCard_type());
			mapParm.put("object_num", authorityList.get(i).getObject_num());
			mapParm.put("floor_group_num", authorityList.get(i).getFloor_group_num());
			mapParm.put("action_flag", 1);
			Map delMap = new HashMap();
			delMap.put("id",authorityList.get(i).getId()+"");
			delMap.put("state", "10");
			peopleAuthorityManagerDao.updTkAuthRecord(delMap);//修改权限表的标志位为10（待删除）
//			res=tKDeviceOpenTimeDao.insertDeviceAuth(mapParm);*/
		
		
		if (0 != resVal4) {
			if (1 == resVal4) {
				try{
					//String floorStr1="";
					//boolean result = true;
					if(authorityList!=null && !authorityList.isEmpty()) {
						for(int i=0;i<authorityList.size();i++){
							Map map1 = new HashMap();
							map1.put("card_num", authorityList.get(i).getCard_num());
							map1.put("card_type", authorityList.get(i).getCard_type());
							map1.put("start_time", authorityList.get(i).getBegin_valid_time());
							map1.put("end_time", authorityList.get(i).getEnd_valid_time());
							map1.put("floor_list", authorityList.get(i).getFloor_list());
							map1.put("action_flag", 1);
							map1.put("result_flag", 0);
							map1.put("object_num", authorityList.get(i).getObject_num());
							map1.put("object_type", authorityList.get(i).getObject_type());
							map1.put("floor_group_num", authorityList.get(i).getFloor_group_num());
							map1.put("device_num", authorityList.get(i).getDevice_num());
							map1.put("try_times", 1);
							 tKDeviceOpenTimeDao.insertAuthorityHistory(map1);//添加到权限历史表
							/*if(!result){
								break;
							}*/
						}
					}
					boolean res = tKDeviceOpenTimeDao.deleteAuthorityBydevNum(device_num);//删除权限表记录
					if(res){
						log.put("V_OP_MSG", "清除设备权限成功");
						log.put("V_OP_TYPE", "业务");
						resVal4 = 11;
					}else{
						resVal4 = 12;
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "清除成功，保存失败");
						throw new RuntimeException();// 注意写回滚注解
					}
				}catch(Exception e) {
					e.printStackTrace();
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					resVal4 = 12;
					throw new RuntimeException();// 注意写回滚注解
					
				} finally{
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					
					logDao.addLog(log);
				}
			}
		}
		
		if(0!=resVal3){
			// 成功下发节假日到设备并且更新数据库
			if (1 == resVal3) {
				Map map0 = new HashMap();
				map0.put("device_num", device_num);
				map0.put("delete_flag", 1);
				try {
					boolean result = tKDeviceOpenTimeDao.resetHolidayTime(map0);
					if (result) {
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_MSG", "清除节假日成功");
						resVal3 = 11;
					} else {
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "清除成功，保存失败");
						resVal3 = 12;
						throw new RuntimeException();// 回滚事物
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					resVal3 = 12;
					throw new RuntimeException();
				} finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					
					logDao.addLog(log);
				}
			}
		}
		
		if(0 != resVal5){
			// 成功下发动作参数到设备并且更新数据库
			if (1 == resVal5) {
				Map map1 = new HashMap();
				map1.put("multi_opt_interval", 5);
				map1.put("single_opt_interval", 2);
				/*map.put("visitor_opt_interval", 1);*/
				map1.put("device_up", 1);
				map1.put("device_down", 1);
				map1.put("device_open", 0);
				map1.put("device_close", 0);
				map1.put("device_num", device_num);
				try {
					boolean result = tKDeviceOpenTimeDao.setEleMoveParam(map1);
					if (result) {
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_MSG", "清除动作参数成功");
						resVal5 = 11;
					} else {
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "清除成功，保存失败");
						resVal5 = 12;
						throw new RuntimeException();
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					resVal5 = 12;
					throw new RuntimeException();
				} finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					logDao.addLog(log);
				}
			}
		}
		
		if(0 != resVal1){
			if (1 == resVal1) {
				Map map2 = new HashMap();
				map2.put("device_num", device_num);
				boolean result = false;
				try {
					for (int j = 1; j <= 8; j++) {
						map2.put("week_day", j);
						result = tKDeviceOpenTimeDao.resetEleOpenTime(map2);
						if (!result) {
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "清除成功，保存失败");
							break;
						}
					}
					if (result) {
						resVal1 = 11;
						log.put("V_OP_TYPE", "业务");
						log.put("V_OP_MSG", "清除电梯开放时区成功");
					} else {
						resVal1 = 12;
						throw new RuntimeException();// 注意写回滚注解
					}
				} catch (Exception e) {
					e.printStackTrace();
					resVal1 = 12;
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "清除成功，保存失败");
					throw new RuntimeException();// 注意写回滚注解
				} finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "清除");
					log.put("V_OP_ID", login_user);
					logDao.addLog(log);
				}
			}
		}
		
		if (12 == resVal || 12 == resVal1 || 12 == resVal2||12 == resVal4
					|| 12 == resVal3 || 12 == resVal5) {
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean copyElevatorSet(Map map,String login_user) {
		Integer resVal0 = (Integer) map.get("resVal0");
		Integer resVal1 = (Integer) map.get("resVal1");
		Integer resVal2 = (Integer) map.get("resVal2");
		Integer resVal3 = (Integer) map.get("resVal3");
		Integer resVal4 = (Integer) map.get("resVal4");
		String device_num = (String) map.get("device_num");
		Device device = deviceDao.getInfoByTKDeviceNum(device_num);
		String targetNum = (String) map.get("targetNum");
		/*String[] nums = copy_device_num.split(",");*/
		Map<String, String> log = new HashMap<String, String>();
		/*
		for (int i = 0; i < nums.length; i++) {*/
			//配置楼层
			if (0 != resVal0) {
				if (1 == resVal0) {
					boolean result = false;
					try {
						List<Floor> configFloor = deviceDao.getDeviceFloor(device_num);
						List<Integer> config = new ArrayList<Integer>();
						
								for (Floor floor : configFloor) {
									floor.setDevice_num(targetNum);
									result = deviceDao.updateDeviceFloor(floor);
									if(!result){
										break;
									}
								}
						if(result){
							resVal0 = 11;
							log.put("V_OP_TYPE", "业务");
							log.put("V_OP_MSG", "复制配置楼层成功");
						}else{
							resVal0 = 12;
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "复制成功,保存失败");
							throw new RuntimeException();// 注意写回滚注解
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "复制成功,保存失败");
						throw new RuntimeException();// 注意写回滚注解
					} finally {
						log.put("V_OP_NAME", "梯控设备管理");
						log.put("V_OP_FUNCTION", "复制");
						log.put("V_OP_ID", login_user);
						logDao.addLog(log);
					}
				}
			}
			
			//楼层开放时区
			if (0 != resVal2) {
				if (1 == resVal2) {
					boolean result = false;
					try {
						List<Floor> floorOpenList = deviceDao.getFloorInfo(device_num);
							for (Floor floor : floorOpenList) {
									floor.setDevice_num(targetNum);
									result = deviceDao.setFloorOpenArea(floor);
									if(!result){
										break;
									}
							}
							if(result){
								resVal2 = 11;
								log.put("V_OP_TYPE", "业务");
								log.put("V_OP_MSG", "复制楼层开放时区成功");
							}else{
								resVal2 = 12;
								log.put("V_OP_TYPE", "异常");
								log.put("V_OP_MSG", "复制成功,保存失败");
								throw new RuntimeException();// 注意写回滚注解
							}
					} catch (Exception e) {
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "复制成功,保存失败");
						resVal2 = 12;
						throw new RuntimeException();// 注意写回滚注解
					} finally {
						log.put("V_OP_NAME", "梯控设备管理");
						log.put("V_OP_FUNCTION", "复制");
						log.put("V_OP_ID", login_user);
						logDao.addLog(log);
					}
					}
				}
			// 复制节假日
			if (0 != resVal3) {
				if (1 == resVal3) {
					boolean result = false;
					try {
						List<Holiday> list = deviceDao.getHolisByDevi(device_num);
						List<String> holidayDateList = new ArrayList<String>();
						for (int j = 0; j < list.size(); j++) {
							holidayDateList.add(j, list.get(j).getHoliday_date());
						}
								for (Holiday h : list) {
									h.setDevice_num(targetNum);
									result = deviceDao.insertHoliday(h);
									if(!result){
										break;
									}
								}
								if(result){
									resVal3 = 11;
									log.put("V_OP_TYPE", "业务");
									log.put("V_OP_MSG", "复制节假日成功");
								}else{
									resVal3 = 12;
									log.put("V_OP_TYPE", "异常");
									log.put("V_OP_MSG", "复制成功，保存失败");
									throw new RuntimeException();// 注意写回滚注解
								}
					} catch (Exception e) {
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "复制成功，保存失败");
						resVal3 = 12;
						throw new RuntimeException();// 注意写回滚注解
					} finally {
						log.put("V_OP_NAME", "梯控设备管理");
						log.put("V_OP_FUNCTION", "复制");
						log.put("V_OP_ID", login_user);
						logDao.addLog(log);
					}
				}
			}
			
			//复制电梯开放时区
			if (0 != resVal1) {
				if (1 == resVal1) {
					List<TKDeviceOpenTimeModel> list = tKDeviceOpenTimeDao
							.checkDevTimeGroup(device_num);//被复制的电梯开放时区
					if (null == list || list.isEmpty()) {
						return false;
					}
					try {
					//Device copydevice = tKDeviceOpenTimeDao.getEleMoveParam(nums[i]);//选中的复制设备
					boolean result = false;
					
					for (int j = 0; j < list.size(); j++) {
						TKDeviceOpenTimeModel tKDeviceOpenTimeModel = new TKDeviceOpenTimeModel();
						TimeZone timeZone0 = new TimeZone();
						OpenTimeZone openTimeZone = new OpenTimeZone();

						tKDeviceOpenTimeModel.setStart_time1(list.get(j)
								.getStart_time1());
						tKDeviceOpenTimeModel.setEnd_time1(list.get(j).getEnd_time1());
					

						TimeZone timeZone1 = new TimeZone();
						tKDeviceOpenTimeModel.setStart_time2(list.get(j)
								.getStart_time2());
						tKDeviceOpenTimeModel.setEnd_time2(list.get(j).getEnd_time2());
					

						TimeZone timeZone2 = new TimeZone();
						tKDeviceOpenTimeModel.setStart_time3(list.get(j)
								.getStart_time3());
						tKDeviceOpenTimeModel.setEnd_time3(list.get(j).getEnd_time3());

						TimeZone timeZone3 = new TimeZone();
						tKDeviceOpenTimeModel.setStart_time4(list.get(j)
								.getStart_time4());
						tKDeviceOpenTimeModel.setEnd_time4(list.get(j).getEnd_time4());
					

						TimeZone timeZone4 = new TimeZone();
						tKDeviceOpenTimeModel.setStart_time5(list.get(j)
								.getStart_time5());
						tKDeviceOpenTimeModel.setEnd_time5(list.get(j).getEnd_time5());
						
						tKDeviceOpenTimeModel.setWeek_day(list.get(j).getWeek_day());
						tKDeviceOpenTimeModel.setDevice_num(targetNum);
						result = tKDeviceOpenTimeDao.copyDevOpenTime(tKDeviceOpenTimeModel);
						  if(!result){
							break;
						  }
					}
					if(result){
						  resVal1 = 11;
						  log.put("V_OP_TYPE", "业务");
						  log.put("V_OP_MSG", "复制楼层开放时区成功，保存失败");
					  }else{
						  resVal1 = 12;
						  log.put("V_OP_TYPE", "异常");
						  log.put("V_OP_MSG", "复制成功，保存失败");
						  throw new RuntimeException();// 注意写回滚注解
					  }
				} catch (Exception e) {
					e.printStackTrace();
					resVal1 = 12;
					log.put("V_OP_TYPE", "异常");
					log.put("V_OP_MSG", "复制成功，保存失败");
					throw new RuntimeException();// 注意写回滚注解
				}finally {
					log.put("V_OP_NAME", "梯控设备管理");
					log.put("V_OP_FUNCTION", "复制");
					log.put("V_OP_ID", login_user);
					logDao.addLog(log);
				}
			}
		}
				
			//复制电梯动作参数
			if (0 != resVal4) {
				if (1 == resVal4) {
					boolean result = false;
					Map mapParam = new HashMap();
					mapParam.put("multi_opt_interval", device.getMulti_opt_interval());
					mapParam.put("single_opt_interval", device.getSingle_opt_interval());
					//map.put("visitor_opt_interval", visitor_opt_interval);
					mapParam.put("device_up", device.getDevice_up());
					mapParam.put("device_down", device.getDevice_down());
					mapParam.put("device_open", device.getDevice_open());
					mapParam.put("device_close", device.getDevice_close());
					mapParam.put("device_num", targetNum);
					try {
						result = tKDeviceOpenTimeDao.setEleMoveParam(mapParam);
						if(result){
							resVal4 = 11;
							log.put("V_OP_TYPE", "业务");
							log.put("V_OP_MSG", "复制电梯动作参数成功");
						}else{
							resVal4 = 12;
							log.put("V_OP_TYPE", "异常");
							log.put("V_OP_MSG", "复制成功,保存失败");
							throw new RuntimeException();// 注意写回滚注解
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.put("V_OP_TYPE", "异常");
						log.put("V_OP_MSG", "复制成功,保存失败");
						resVal4 = 12;
						throw new RuntimeException();// 注意写回滚注解
					}finally {
						log.put("V_OP_NAME", "梯控设备管理");
						log.put("V_OP_FUNCTION", "复制");
						log.put("V_OP_ID", login_user);
						
						logDao.addLog(log);
					}
				}
			}
		
			if(12==resVal4 || 12==resVal3 || 12==resVal2 || 12==resVal1 || 12==resVal0){
				return false;
			}else{
				return true;
			}
		}

	@Override
	public Grid<CommIpInfoModel> getCommIpInfo(Map map) {
		Grid<CommIpInfoModel> grid = new Grid<CommIpInfoModel>();
		List<CommIpInfoModel> list = tKDeviceOpenTimeDao.getCommIpInfoList(map);
		grid.setRows(list);
		if(null!=list){
			Integer count = tKDeviceOpenTimeDao.getCommIpInfoCount(map);
			grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
		
	}

	@Override
	public boolean addCommIpInfo(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			result = tKDeviceOpenTimeDao.addCommIpInfo(map);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
		} finally {
			log.put("V_OP_NAME", "通讯服务器Ip信息");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "新增通讯服务器Ip信息");
			logDao.addLog(log);
		}
		return result;
	}

	@Override
	public CommIpInfoModel getCommIpInfoById(String id) {
		return tKDeviceOpenTimeDao.getCommIpInfoById(id);
	}

	@Override
	public boolean updateCommIpInfo(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			result = tKDeviceOpenTimeDao.updateCommIpInfo(map);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
		} finally {
			log.put("V_OP_NAME", "通讯服务器Ip信息");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改通讯服务器Ip信息");
			logDao.addLog(log);
		}
		return result;
	}

	@Override
	public boolean delCommIpInfo(String ids, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		try {
			result = tKDeviceOpenTimeDao.delCommIpInfo(ids);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
		} finally {
			log.put("V_OP_NAME", "通讯服务器Ip信息");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除通讯服务器Ip信息");
			logDao.addLog(log);
		}
		return result;
	}
	}
