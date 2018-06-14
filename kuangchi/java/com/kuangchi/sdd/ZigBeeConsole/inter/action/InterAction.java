package com.kuangchi.sdd.ZigBeeConsole.inter.action;




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.service.AuthorityManagerService;
import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.device.service.IZBdeviceService;
import com.kuangchi.sdd.ZigBeeConsole.inter.model.LightKeyUser;
import com.kuangchi.sdd.ZigBeeConsole.inter.service.IInterService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 光子锁对外接口 - action
 * @author yuman.gao
 */
@Controller("ZigBeeInterAction")
public class InterAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "ZigBeeInterServiceImpl")
	private IInterService interService;
	
	@Resource(name = "authorityManagerServiceImpl")
	private AuthorityManagerService authorityManagerService;
	
	@Resource(name = "zBdeviceServiceImpl")
	private IZBdeviceService deviceService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 更新设备电量接口
	 * @author yuman.gao
	 */
	public void updateElectricity(){
		
		HttpServletRequest request = getHttpServletRequest();

		String electricity = request.getParameter("bateryBalance");
//		String room_num = request.getParameter("roomNo");
		String device_id = request.getParameter("lockId");
		
		// 更新电量
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("electricity", electricity);
		param.put("device_id", device_id);
		boolean updateResult = interService.updateElectricity(param);
		
		// 新增电压报警记录
		Map<String, Object> recordParam = new HashMap<String, Object>();
		recordParam.put("device_id", device_id);
		recordParam.put("open_type", 5);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String swipe_date = sdf.format(new Date());
		recordParam.put("swipe_date", swipe_date);
		boolean addResult = interService.addRecord(recordParam, null);
		
		
		// 反馈上报结果
		Map gateInfo = authorityManagerService.getZigbeeIpMap(device_id);
		String gateway_ip = gateInfo.get("gateway").toString(); //网关
		String gateway_port = gateInfo.get("gateway_port").toString();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("gateway_ip", gateway_ip);
		result.put("gateway_port", gateway_port);
		result.put("lockId", device_id);
		if(updateResult){
			result.put("status", "0");
		} else {
			result.put("status", "1");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 记录上报接口
	 * @author yuman.gao
	 */
	public void recordReport(){
		
		HttpServletRequest request = getHttpServletRequest();
//		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String photonId = request.getParameter("photonId");
		String device_id = request.getParameter("lockId");
		String open_type = request.getParameter("openType");
		String swipe_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String staff_name = null;
		if("2".equals(open_type)){  //如果为密码开门，则不存光ID和员工姓名
			photonId = null;
		} else {
			staff_name = interService.getStaffNamebyStaffNum(photonId);
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("light_id", photonId);
		param.put("staff_name", staff_name);
		param.put("device_id", device_id);
		param.put("open_type", open_type);
		param.put("swipe_date", swipe_date);
		
		boolean addResult = interService.addRecord(param, null);
		
		// 可能当前上位机状态为休眠，需将设备状态改为在线
		ZBdeviceModel device = new ZBdeviceModel();
		device.setDevice_state("0");
		device.setDevice_id(device_id);
		deviceService.setDevStateByID(device);
				
		// 调用第三方接口
		
		
		
		// 反馈上报结果
		Map gateInfo = authorityManagerService.getZigbeeIpMap(device_id);
		String gateway_ip = gateInfo.get("gateway").toString(); //网关
		String gateway_port = gateInfo.get("gateway_port").toString();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("gateway_ip", gateway_ip);
		result.put("gateway_port", gateway_port);
		result.put("lockId", device_id);
		if(addResult){
			result.put("status", "0");
		} else {
			result.put("status", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	// 上报键盘锁定信息
	public void illegalRecordReport(){
		
		HttpServletRequest request = getHttpServletRequest();
//		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String device_id = request.getParameter("lockId");
		String swipe_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String open_type = request.getParameter("openType");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_id", device_id);
		param.put("open_type", open_type);
		param.put("swipe_date", swipe_date);
		
		boolean addResult = interService.addRecord(param, null);
		
		// 如果设备进入休眠状态，将设备状态改为休眠
		if("3".equals(open_type)){
			ZBdeviceModel device = new ZBdeviceModel();
			device.setDevice_state("2");
			device.setDevice_id(device_id);
			deviceService.setDevStateByID(device);
		}
		
		
		// 反馈上报结果
		Map gateInfo = authorityManagerService.getZigbeeIpMap(device_id);
		String gateway_ip = gateInfo.get("gateway").toString(); //网关
		String gateway_port = gateInfo.get("gateway_port").toString();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("gateway_ip", gateway_ip);
		result.put("gateway_port", gateway_port);
		result.put("lockId", device_id);
		if(addResult){
			result.put("status", "0");
		} else {
			result.put("status", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	// 键盘密码验证
	public void pasVaild(){
		
		HttpServletRequest request = getHttpServletRequest();
//			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String device_id = request.getParameter("lockId");
		String password = request.getParameter("password");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_id", device_id);
		
		boolean addResult = false;
//		boolean addResult = interService.addRecord(param, null);
		
		Map gateInfo = authorityManagerService.getZigbeeIpMap(device_id);
		String gateway_ip = gateInfo.get("gateway").toString(); //网关
		String gateway_port = gateInfo.get("gateway_port").toString();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("gateway_ip", gateway_ip);
		result.put("gateway_port", gateway_port);
		result.put("lockId", device_id);
		if(addResult){
			result.put("status", "0");
		} else {
			result.put("status", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
		
		
		
	/**
	 * 同步员工信息
	 * @author yuman.gao
	 */
	public void synchronizedStaffInfo(){
		JsonResult result = new JsonResult();
		try {
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag");
			String data = request.getParameter("data");
			
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				LightKeyUser user = GsonUtil.toBean(data, LightKeyUser.class);
				// 防止卡号为空导致出错
				if(user.getCard_id() == null){
					return;
				}
				
				map.put("remote_staff_id", user.getUserid());
				map.put("staff_mobile", user.getUser_phone());
				map.put("staff_password", user.getUser_pwd());
				map.put("staff_name", user.getRealname());
				map.put("staff_img", user.getPhoto());
				map.put("staff_num", UUID.randomUUID().toString());
				map.put("staff_hiredate", user.getReg_time());
				map.put("staff_remark", user.getCompanyid());
				map.put("staff_dept", "2");
				map.put("card_num", user.getCard_id());
//				map.put("create_user", loginUser.getYhMc());
				map.put("staff_no", authorityManagerService.getStaffNo());
				
				Calendar cal = Calendar.getInstance();  // 卡片默认有效期为20年
		        cal.add(Calendar.YEAR, 20);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        String date = df.format(cal.getTime());
		        map.put("card_validity", date);
		        
			} catch (Exception e) {
				map.put("remote_staff_id", data);
			}
			
			boolean setResult = false;
			if("update".equals(flag)){
				setResult = interService.updateUserByLightKey(map);
			} else if ("add".equals(flag)){
				setResult = interService.addUserByLightKey(map);
			} else if("freeze".equals(flag)){
				setResult = interService.freeUserByLightKey(map);
			} else if ("unFreeze".equals(flag)) {
				setResult = interService.unfreeUserByLightKey(map);
			} 
			if(setResult){
				result.setSuccess(true);
				result.setMsg("同步成功");
			} else {
				result.setSuccess(false);
				result.setMsg("同步失败");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	
	
	
}
