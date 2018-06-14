
package com.kuangchi.sdd.baseConsole.doorinfo.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.restlet.Application;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.cardstate.model.CardState;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.baseConsole.doorinfo.service.IDoorInfoService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.BeanUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.network.HttpRequest;
@Controller("DoorinfoAction")
public class DoorinfoAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DoorinfoAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "DoorInfoServiceImpl")
	private IDoorInfoService doorInfoService;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	private DoorInfoModel model;
	public DoorinfoAction(){
		model=new DoorInfoModel();}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到门信息主页面
	public String toMyDoorinfo(){
		return "success";
	}
	
	//查询全部信息
	public void getAlldoorinfos(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		DoorInfoModel doorinfo=GsonUtil.toBean(data,DoorInfoModel.class);
		if(doorinfo.getDoor_num()!=null){
			doorinfo.setDoor_num(doorinfo.getDoor_num().trim());
		}else if(doorinfo.getDevice_name()!=null){
			doorinfo.setDevice_num(doorinfo.getDevice_name().trim());
		}else if(doorinfo.getDoor_name()!=null){
			doorinfo.setDoor_name(doorinfo.getDoor_name().trim());
		}
		Grid allCard=doorInfoService.selectAllDoorinfos(doorinfo, page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	
	}
	//查询所有能用的设备编号新增
		public void getDeviceNumAdd(){
			List<DoorInfoModel> doorNum=doorInfoService.selectDeviceNumAdd();
			List<Map> list=new ArrayList<Map>();  
			for (DoorInfoModel doorInfoModel : doorNum) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("TEXT",doorInfoModel.getDevice_name());
		           map.put("ID",doorInfoModel.getDevice_num());
		           list.add(map);
			}
	        printHttpServletResponse(GsonUtil.toJson(list));
		}
	
	
	//新增门信息
	public void getInsertDoorInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String data= request.getParameter("data");
		DoorInfoModel doorModel=GsonUtil.toBean(data,DoorInfoModel.class);
		if("4".equals(doorModel.getOpen_pattern())){
			doorModel.setFirst_open(1);//是首卡开门
		}else{
			doorModel.setFirst_open(0);;//否首卡开门
		}
		if(doorModel.getDoor_name()!=null){
			doorModel.setDoor_name(doorModel.getDoor_name().trim());
		}
		String device_num=doorModel.getDevice_num();
		
		String door_name=doorModel.getDoor_name();
		String description=doorModel.getDescription();
		String old_device_num=doorModel.getOld_device_num();//没有修改之前的设备编号
		String old_door_num=doorModel.getOld_door_num();//没有修改之前的门编号
		
		DeviceInfo deviceInfo=doorInfoService.queryMacByDeviceNum(device_num);//查询设备的mac地址
		String mac=deviceInfo.getDevice_mac();
		String local_ip=deviceInfo.getLocal_ip_address();
		String device_type=deviceInfo.getDevice_type();
		
		// 后台判断，不能为空
				if (EmptyUtil.atLeastOneIsEmpty(device_num, door_name)) {
					JsonResult result = new JsonResult();
					result.setMsg("输入的参数不能为空");
					result.setSuccess(false);
					printHttpServletResponse(GsonUtil.toJson(result));
					return;
				}
		// 获得登陆用户的名字
			User loginUser = (User) getHttpServletRequest().getSession()
					.getAttribute(GlobalConstant.LOGIN_USER);
			doorModel.setCreate_user(loginUser.getYhMc());
			JsonResult result = new JsonResult();
			if ("add".equals(flag)) {
				String min=doorInfoService.querydevicemin(device_num);
				if(min!=null){
					doorModel.setDoor_num(min);
					doorModel.setDevice_num(device_num);
					doorModel.setValidity_flag("0");
					doorModel.setOpen_delay(doorModel.getOpen_delay());
					doorModel.setOpen_overtime(doorModel.getOpen_overtime());
					String open_delay=doorModel.getOpen_delay();//开门延时
					String open_overtime=doorModel.getOpen_overtime();//开门超时
					String door_num_min=min;//新增已有的被删除的门的编号
					
					//新增门信息之前调用接口
					/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
					//（1）调用设置门工作参数接口
					String setGateWorkParamUrl=mjCommService.getMjCommUrl(device_num)+"gateWorkParam/setGateWorkParam.do";
					String setGateWorkParamStr=HttpRequest.sendPost(setGateWorkParamUrl, "mac="+mac+"&gateId="+door_num_min+"&open_delay="+open_delay+"&open_overtime="+open_overtime+"&device_type="+device_type+"&local_ip_addr="+local_ip);
					Map setParamResult=GsonUtil.toBean(setGateWorkParamStr, HashMap.class);
					
					if(setParamResult!=null){
						if("0".equals(setParamResult.get("result_code"))){
							Integer num=doorInfoService.updateInsertDoor(doorModel);
							if(num==1){
					  			result.setMsg("添加成功");
					  			result.setSuccess(true);
							}else{
					  			result.setMsg("添加失败");
					  			result.setSuccess(false);
							}
						}else{
							result.setMsg("添加失败");
				  			result.setSuccess(false);
						}
					}else {
						result.setMsg("添加失败，连接异常");
			  			result.setSuccess(false);
					}
					
				}else{
					String Num=String.valueOf((Integer.valueOf(doorInfoService.getDoorID(device_num))+1));//设备数量
					doorModel.setDoor_num(Num);
					doorModel.setValidity_flag("0");
					doorModel.setOpen_delay(doorModel.getOpen_delay());
					doorModel.setOpen_overtime(doorModel.getOpen_overtime());
					String open_delay=doorModel.getOpen_delay();//开门延时
					String open_overtime=doorModel.getOpen_overtime();//开门超时
					String door_num_add=Num;//新增时使用的门编号
				
					//新增门信息之前调用接口
					/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
					//（1）调用设置门工作参数接口
					String setGateWorkParamUrl=mjCommService.getMjCommUrl(device_num)+"gateWorkParam/setGateWorkParam.do";
					String setGateWorkParamStr=HttpRequest.sendPost(setGateWorkParamUrl, "mac="+mac+"&gateId="+door_num_add+"&open_delay="+open_delay+"&open_overtime="+open_overtime+"&device_type="+device_type+"&local_ip_addr="+local_ip);
					Map setParamResult=GsonUtil.toBean(setGateWorkParamStr, HashMap.class);
					
					Boolean obj=true;
					if(setParamResult!=null){
						if("0".equals(setParamResult.get("result_code"))){    
							obj=doorInfoService.insertDoorinfo(doorModel);	
							if(true==obj){
					  			result.setMsg("添加成功");
					  			result.setSuccess(true);
							}else{
					  			result.setMsg("添加失败");
					  			result.setSuccess(false);
							}
						}else{
							result.setMsg("添加失败");
							result.setSuccess(false);
						}
					}else {
						result.setMsg("添加失败，连接异常");
						result.setSuccess(false);
					}
				}
				printHttpServletResponse(GsonUtil.toJson(result));
			}else if ("edit".equals(flag)){
				doorModel.setDoor_num(door_name);
				doorModel.setDescription(description);
				doorModel.setOpen_delay(doorModel.getOpen_delay());
				doorModel.setOpen_overtime(doorModel.getOpen_overtime());
				String open_delay=doorModel.getOpen_delay();//开门延时
				String open_overtime=doorModel.getOpen_overtime();//开门超时
				String door_num_edit=old_door_num;//修改时使用的门编号
				Integer use_super_password=doorModel.getUse_super_password();//是否启用超级密码开门
				String super_password=doorModel.getSuper_password();//超级开门密码
				Integer use_force_password=doorModel.getUse_force_password();//是否启用胁迫密码
				String force_password=doorModel.getForce_password();//胁迫密码
				String relock_password=doorModel.getRelock_password();//重锁密码
				String unlock_password=doorModel.getUnlock_password();//解锁密码
				String police_password=doorModel.getPolice_password();//报警密码
				String open_pattern=doorModel.getOpen_pattern();//功能模式
				String check_open_pattern=doorModel.getCheck_open_pattern();//验证开门模式
				String work_pattern=doorModel.getWork_pattern();//工作模式
				Integer multi_open_number=doorModel.getMulti_open_number();//多卡开门数量
				String multi_open_card_num=doorModel.getMulti_open_card_num();//多卡开门卡号
				
				//修改门信息之前调用接口
				/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				//（1）调用设置门工作参数接口
				String setGateWorkParamUrl=mjCommService.getMjCommUrl(device_num)+"gateWorkParam/setGateWorkParam.do";
				String setGateWorkParamStr=HttpRequest.sendPost(setGateWorkParamUrl, "mac="+mac+"&device_type="+device_type
						+"&gateId="+door_num_edit+"&use_super_password="+use_super_password
						+"&super_password="+super_password+"&use_force_password="+use_force_password
						+"&force_password="+force_password+"&relock_password="+relock_password
						+"&unlock_password="+unlock_password+"&police_password="+police_password
						+"&open_pattern="+open_pattern+"&check_open_pattern="+check_open_pattern
						+"&work_pattern="+work_pattern+"&multi_open_number="+multi_open_number
						+"&multi_open_card_num="+(multi_open_card_num==null?"":multi_open_card_num)
						+"&open_delay="+open_delay+"&open_overtime="+open_overtime);
				Map setParamResult=GsonUtil.toBean(setGateWorkParamStr, HashMap.class);
				//调用设置首卡开门接口
				/*String setFirstOpenUrl=mjCommService.getMjCommUrl(device_num)+"firstAction/setFirst.do";
				String setFirstOpenStr=HttpRequest.sendPost(setFirstOpenUrl, "mac="+mac+"&device_type="+device_type+"&gateId="+door_num_edit+"&first="+first_open);
				Map firstOpenResult=GsonUtil.toBean(setFirstOpenStr, HashMap.class);*/
				
				Boolean obj=true;
				
				if(setParamResult!=null){
					if("0".equals(setParamResult.get("result_code"))){    
						obj=doorInfoService.updateDoorNameAndDes(doorModel);
						if(true==obj){
							result.setMsg("修改成功");
					        result.setSuccess(true);
						}else{
							result.setMsg("修改失败");
					        result.setSuccess(false);
						}
					}else{
						result.setMsg("修改失败");
				        result.setSuccess(false);
					}
				}else {
					result.setMsg("修改失败，连接异常");
			        result.setSuccess(false);
				}
				
		        printHttpServletResponse(GsonUtil.toJson(result));
	}
}	
	
	//删除门信息
	public void getdeletedoorinfos(){
		HttpServletRequest request = getHttpServletRequest();
		String door_device_num=request.getParameter("data_ids");
		String[] sourceStrArray = door_device_num.split(",");
        for (int i = 0; i < sourceStrArray.length; i=i+2) {
        	model.setDoor_num(sourceStrArray[i]);
        	model.setDevice_num(sourceStrArray[i+1]);
        	Integer del=doorInfoService.deleteDoorinfo(model);
		if(del==0){
			JsonResult result = new JsonResult();
		    result.setSuccess(false);
		    printHttpServletResponse(GsonUtil.toJson(result));
		}else{
    		Integer delDoor=doorInfoService.deleteDoorPeopleauthorityService(model);
    		JsonResult result = new JsonResult();
		    result.setSuccess(true);
		    printHttpServletResponse(GsonUtil.toJson(result));
		}
      }
	}
	
	// 分发页面
		public String toOperatePage() {
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag");
			if ("view".equals(flag) ) {
				Integer door_id=Integer.valueOf(request.getParameter("door_id"));
				List<DoorInfoModel> door=doorInfoService.selectDoorinfoById(door_id);
				
				String device_num=door.get(0).getDevice_num();
				DeviceInfo deviceInfo=doorInfoService.queryMacByDeviceNum(device_num);//查询设备的mac地址
				String mac=deviceInfo.getDevice_mac();
				String device_type=deviceInfo.getDevice_type();//sign
				String door_num=door.get(0).getDoor_num();//gateId
				//跳转到修改门信息页面前，调用服务器读取门工作参数接口
				/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				//（1）调用设置门工作参数接口
				String getGateWorkParamUrl=mjCommService.getMjCommUrl(device_num)+"gateWorkParam/getGateWorkParam.do";
				String getGateWorkParamStr=HttpRequest.sendPost(getGateWorkParamUrl, "mac="+mac+"&gateId="+door_num+"&device_type="+device_type);
				Map getParamResult=GsonUtil.toBean(getGateWorkParamStr, HashMap.class);
				if(BeanUtil.isEmpty(getParamResult)){
					for (DoorInfoModel doorInfoModel : door) {
						request.setAttribute("doors", doorInfoModel);
					}
					return "view";
				}else {
					if("0".equals(getParamResult.get("result_code"))){
						String gateWorkParam=(String) getParamResult.get("gateWorkParam");
						Map gateWorkParamStr=GsonUtil.toBean(gateWorkParam, HashMap.class);
						door.get(0).setForce_password((String) gateWorkParamStr.get("forcePassword"));
						door.get(0).setRelock_password((String) gateWorkParamStr.get("relockPassword"));
						door.get(0).setOpen_overtime(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("openOvertime"), 16))));
						door.get(0).setUse_super_password(Integer.parseInt((String)gateWorkParamStr.get("useSuperPassword")));
						door.get(0).setCheck_open_pattern((String) gateWorkParamStr.get("checkOpenPattern"));
						door.get(0).setOpen_delay(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("openDelay"), 16))/10));
						door.get(0).setWork_pattern(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("workPattern"), 16))));
						door.get(0).setSuper_password((String) gateWorkParamStr.get("superPassword"));
						door.get(0).setUse_force_password(Integer.parseInt((String)gateWorkParamStr.get("useForcePassword")));
						door.get(0).setPolice_password((String) gateWorkParamStr.get("policePassword"));
						door.get(0).setUnlock_password((String) gateWorkParamStr.get("unlockPassword"));
						door.get(0).setOpen_pattern((String) gateWorkParamStr.get("openPattern"));
						door.get(0).setMulti_open_number(Integer.parseInt((String)gateWorkParamStr.get("multiOpenNumber")));
						String multiOpenCardnum=(String) gateWorkParamStr.get("multiOpenCard");
						if(null!=multiOpenCardnum&&!"".equals(multiOpenCardnum)){
							String[] multiOpenCardnumArr=multiOpenCardnum.split(",");
							long[] multi_open_cardnum=new long[multiOpenCardnumArr.length];
							for(int i=0;i<multiOpenCardnumArr.length;i++){
								multi_open_cardnum[i]=Long.parseLong(multiOpenCardnumArr[i], 16);//将16进制的卡号转换成10进制
							}
							String multi_Open_CardNum="";
							for(int j=0;j<multi_open_cardnum.length;j++){
								multi_Open_CardNum=multi_Open_CardNum+multi_open_cardnum[j]+",";//将卡号拼成字符串
							}
							if(multi_Open_CardNum.length()>=1){
								multi_Open_CardNum=multi_Open_CardNum.substring(0, multi_Open_CardNum.length()-1);//去除末尾逗号
							}
							door.get(0).setMulti_open_card_num(multi_Open_CardNum);
						}else{
							door.get(0).setMulti_open_card_num("");
						}
					
						for (DoorInfoModel doorInfoModel : door) {
							request.setAttribute("doors", doorInfoModel);
						}
						return "view";
					}else{
						for (DoorInfoModel doorInfoModel : door) {
							request.setAttribute("doors", doorInfoModel);
						}
						return "view";//读取门工作参数失败时，返回error
					}
				}
			}else if("edit".equals(flag)){
				Integer door_id=Integer.valueOf(request.getParameter("door_id"));
				List<DoorInfoModel> door=doorInfoService.selectDoorinfoById(door_id);
				
				String device_num=door.get(0).getDevice_num();
				DeviceInfo deviceInfo=doorInfoService.queryMacByDeviceNum(device_num);//查询设备的mac地址
				String mac=deviceInfo.getDevice_mac();
				String device_type=deviceInfo.getDevice_type();//sign
				String door_num=door.get(0).getDoor_num();//gateId
				//跳转到修改门信息页面前，调用服务器读取门工作参数接口
				/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				//（1）调用设置门工作参数接口
				String getGateWorkParamUrl=mjCommService.getMjCommUrl(device_num)+"gateWorkParam/getGateWorkParam.do";
				String getGateWorkParamStr=HttpRequest.sendPost(getGateWorkParamUrl, "mac="+mac+"&gateId="+door_num+"&device_type="+device_type);
				Map getParamResult=GsonUtil.toBean(getGateWorkParamStr, HashMap.class);
				if(BeanUtil.isEmpty(getParamResult)){
					for (DoorInfoModel doorInfoModel : door) {
						request.setAttribute("doors", doorInfoModel);
					}
					return "edit";
				}else {
					if("0".equals(getParamResult.get("result_code"))){
						String gateWorkParam=(String) getParamResult.get("gateWorkParam");
						Map gateWorkParamStr=GsonUtil.toBean(gateWorkParam, HashMap.class);
						door.get(0).setForce_password((String) gateWorkParamStr.get("forcePassword"));
						door.get(0).setRelock_password((String) gateWorkParamStr.get("relockPassword"));
						door.get(0).setOpen_overtime(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("openOvertime"), 16))));
						door.get(0).setUse_super_password(Integer.parseInt((String)gateWorkParamStr.get("useSuperPassword")));
						door.get(0).setCheck_open_pattern((String) gateWorkParamStr.get("checkOpenPattern"));
						door.get(0).setOpen_delay(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("openDelay"), 16))/10));
						door.get(0).setWork_pattern(String.valueOf((Integer.parseInt((String) gateWorkParamStr.get("workPattern"), 16))));
						door.get(0).setSuper_password((String) gateWorkParamStr.get("superPassword"));
						door.get(0).setUse_force_password(Integer.parseInt((String)gateWorkParamStr.get("useForcePassword")));
						door.get(0).setPolice_password((String) gateWorkParamStr.get("policePassword"));
						door.get(0).setUnlock_password((String) gateWorkParamStr.get("unlockPassword"));
						door.get(0).setOpen_pattern((String) gateWorkParamStr.get("openPattern"));
						door.get(0).setMulti_open_number(Integer.parseInt((String)gateWorkParamStr.get("multiOpenNumber")));
						String multiOpenCardnum=(String) gateWorkParamStr.get("multiOpenCard");
						if(null!=multiOpenCardnum&&!"".equals(multiOpenCardnum)){
							String[] multiOpenCardnumArr=multiOpenCardnum.split(",");
							long[] multi_open_cardnum=new long[multiOpenCardnumArr.length];
							for(int i=0;i<multiOpenCardnumArr.length;i++){
								multi_open_cardnum[i]=Long.parseLong(multiOpenCardnumArr[i], 16);//将16进制的卡号转换成10进制
							}
							String multi_Open_CardNum="";
							for(int j=0;j<multi_open_cardnum.length;j++){
								multi_Open_CardNum=multi_Open_CardNum+multi_open_cardnum[j]+",";//将卡号拼成字符串
							}
							if(multi_Open_CardNum.length()>=1){
								multi_Open_CardNum=multi_Open_CardNum.substring(0, multi_Open_CardNum.length()-1);//去除末尾逗号
							}
							door.get(0).setMulti_open_card_num(multi_Open_CardNum);
						}else{
							door.get(0).setMulti_open_card_num("");
						}
						
						for (DoorInfoModel doorInfoModel : door) {
							request.setAttribute("doors", doorInfoModel);
						}
						return "edit";
					}else{
						for (DoorInfoModel doorInfoModel : door) {
							request.setAttribute("doors", doorInfoModel);
						}
						return "edit";//读取门工作参数失败时，读取一卡通数据库数据
					}
				}
				
			}	
			else {
				return "success";
			}
		}	
		
	//设置/取消首卡开门(已合并到修改门参数的方法里)
		public void updateFirstOpen(){
			HttpServletRequest request=getHttpServletRequest();
			String flag=request.getParameter("flag");
			/*String door_num=request.getParameter("door_num");//门编号
			String device_num=request.getParameter("device_num");*/
			String door_device_num=request.getParameter("data_ids");
			JsonResult result = new JsonResult();
  			String[] sourceStrArray = door_device_num.split(",");
  			 for (int i = 0; i < sourceStrArray.length; i=i+2) {
			DeviceInfo deviceInfo=doorInfoService.queryMacByDeviceNum(sourceStrArray[i+1]);//查询设备的mac地址
			String mac=deviceInfo.getDevice_mac();//设备mac值
			String device_type=deviceInfo.getDevice_type();//设备类型
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		    String login_user = loginUser.getYhMc();
			if("set".equals(flag)){
				Integer first_open=1;
				/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				String firstOpenUrl=mjCommService.getMjCommUrl(sourceStrArray[i+1])+"firstAction/setFirst.do";
				String firstOpenStr=HttpRequest.sendPost(firstOpenUrl, "mac="+mac+"&device_type="+device_type+"&gateId="+sourceStrArray[i]+"&first="+first_open);
				Map firstOpenResult=GsonUtil.toBean(firstOpenStr, HashMap.class);
				if(firstOpenResult!=null){
					if("0".equals(firstOpenResult.get("result_code"))){
						
						Integer num=doorInfoService.updateDoorinfo(first_open,sourceStrArray[i+1],sourceStrArray[i],login_user);
						if(num==1){
				  			result.setMsg("设置成功");
				  			result.setSuccess(true);
						}else{
				  			result.setMsg("设置失败");
				  			result.setSuccess(false);
						}
					}else{
						result.setMsg("设置失败");
						result.setSuccess(false);
					}
				}else {
					result.setMsg("设置失败，连接异常");
					result.setSuccess(false);
				}
				
			}else if("cancel".equals(flag)){
				Integer first_open=0;
				/*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				String firstOpenUrl=mjCommService.getMjCommUrl(sourceStrArray[i+1])+"firstAction/setFirst.do";
				String firstOpenStr=HttpRequest.sendPost(firstOpenUrl, "mac="+mac+"&device_type="+device_type+"&gateId="+sourceStrArray[i]+"&first="+first_open);
				Map firstOpenResult=GsonUtil.toBean(firstOpenStr, HashMap.class);
				if(firstOpenResult!=null){
					if("0".equals(firstOpenResult.get("result_code"))){
						Integer num=doorInfoService.updateDoorinfo(first_open,sourceStrArray[i+1],sourceStrArray[i],login_user);
						if(num==1){
				  			result.setMsg("取消成功");
				  			result.setSuccess(true);
						}else{
				  			result.setMsg("取消失败");
				  			result.setSuccess(false);
						}
					}else{
						result.setMsg("取消失败");
						result.setSuccess(false);
					}
				}else {
					result.setMsg("取消失败，连接异常");
					result.setSuccess(false);
				}
			}
  		 }
  			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
		  //设置/取消屏蔽门
  		public void updatehaddenflag(){
  			HttpServletRequest request=getHttpServletRequest();
  			String flag=request.getParameter("flag");
  			/*String door_num=request.getParameter("door_num");//门编号
  			String device_num=request.getParameter("device_num");*/
  			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
  		    String login_user = loginUser.getYhMc();
  			String door_device_num=request.getParameter("data_ids");
  			String[] sourceStrArray = door_device_num.split(",");
  	        
  			JsonResult result = new JsonResult();
  			Integer num=0;
  			if("set".equals(flag)){
  				Integer hadden_flag=1;
  				  for (int i = 0; i < sourceStrArray.length; i=i+2) {
  		        	 num=doorInfoService.updatehaddenflag(hadden_flag, sourceStrArray[i+1], sourceStrArray[i],login_user);
  		        }
  					if(num==1){
  			  			result.setMsg("设置成功");
  			  			result.setSuccess(true);
  					}else{
  			  			result.setMsg("设置失败");
  			  			result.setSuccess(false);
  					}
  			}else if("cancel".equals(flag)){
  				Integer hadden_flag=0;
  				 for (int i = 0; i < sourceStrArray.length; i=i+2) {
  		        	 num=doorInfoService.updatehaddenflag(hadden_flag, sourceStrArray[i+1], sourceStrArray[i],login_user);
  		        }
  					if(num==1){
  			  			result.setMsg("取消成功");
  			  			result.setSuccess(true);
  					}else{
  			  			result.setMsg("取消失败");
  			  			result.setSuccess(false);
  					}
  				}
  			printHttpServletResponse(GsonUtil.toJson(result));
  			}
		
		
		/**
		 * 根据门号和设备号查询门信息
		 * @author minting.he
		 */
		public void selectDoorInfo(){
			try{
				HttpServletRequest request = getHttpServletRequest();
				String doors = request.getParameter("doors");
				String devices = request.getParameter("devices");
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				String[] door = doors.split(",");
				String[] device = devices.split(",");
				Map<String, Object> paramMap = new HashMap<String, Object>();
				for(int i=0; i<door.length; i++ ){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("door_num", door[i]);
					map.put("device_num", device[i]);
					list.add(map);
				}
				paramMap.put("list", list);
				List<DoorInfoModel> resultList = doorInfoService.selectDoorInfo(paramMap);
				printHttpServletResponse(GsonUtil.toJson(resultList));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/**
		 * 修改门信息多卡开门卡号时，查找卡号
		 * guofei.lian
		 * 2016-07-16
		 * */
		public void getCardsInfo(){
			HttpServletRequest request=getHttpServletRequest();
			String data=request.getParameter("data");
			Map map=GsonUtil.toBean(data,HashMap.class);
			Integer rows = Integer.parseInt(request.getParameter("rows"));
		    Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		    map.put("rows", rows);
		    map.put("skip", skips);
		    Grid<Map> grid=doorInfoService.getCardsInfoDynamic(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		
		/**
		 * 修改门信息时显示根据多卡开门卡号查询员工信息
		 * guofei.lian
		 * 2016-09-29
		 * */
		public void getStaffInfoByMultiCardnum(){
			HttpServletRequest request=getHttpServletRequest();
			String multi_open_card_num=request.getParameter("multi_open_card_num");
			List<Map> staffInfoByMulCardnum=doorInfoService.getStaffInfoByMultiCardnum(multi_open_card_num);
			printHttpServletResponse(GsonUtil.toJson(staffInfoByMulCardnum));
		}
		
		/**
		 * 修改监控门
		 * @author minting.he
		 */
		public void updateMonitor(){
			HttpServletRequest request=getHttpServletRequest();
			JsonResult result = new JsonResult();
			String device_yes = request.getParameter("device_yes");
			String door_yes = request.getParameter("door_yes");
			String device_no = request.getParameter("device_no");
			String door_no = request.getParameter("door_no");
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			if((EmptyUtil.isEmpty(device_yes)||EmptyUtil.isEmpty(door_yes)) && (EmptyUtil.isEmpty(device_no)||EmptyUtil.isEmpty(door_no)) ){
				result.setSuccess(false);
				result.setMsg("修改失败，数据不正确");
			}else {
				if(BeanUtil.isEmpty(user)){
					result.setSuccess(false);
					result.setMsg("修改失败，请先登录");
				}else {
					String login_user = user.getYhMc();
					boolean r = doorInfoService.updateMonitor(device_yes, door_yes, device_no, door_no, login_user);
					if(r){
						result.setSuccess(true);
						result.setMsg("修改成功");
					}else {
						result.setSuccess(false);
						result.setMsg("修改失败");
					}
				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
		/**
		 * 获取监控的树
		 * @author minting.he
		 */
		public void getMonitorTree(){
			Tree deviceGroupTree = doorInfoService.getMonitorTree();
			printTree(deviceGroupTree);
			StringBuilder builder=new StringBuilder();
			builder.append("[");
			builder.append(new Gson().toJson(deviceGroupTree));
			builder.append("]");
			printHttpServletResponse(builder.toString());
		}
		
		public void printTree(Tree tree){
			LOG.info(tree.getId()+"=="+tree.getText());
			List<Tree> childrenList=tree.getChildren();
			for(Tree treeNode:childrenList){
				printTree(treeNode);
			}
		}
		
		/**
		 * 获取已经勾选监控门的树
		 * @author minting.he
		 */
		public void getCheckedTree(){
			Tree deviceGroupTree = doorInfoService.getCheckedTree();
			printTree(deviceGroupTree);
			StringBuilder builder=new StringBuilder();
			builder.append("[");
			builder.append(new Gson().toJson(deviceGroupTree));
			builder.append("]");
			printHttpServletResponse(builder.toString());
		}
		
		/**
		 * 没监控门 分页
		 * @author minting.he
		 */
		public void getNoMonitorParam() {
		    HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("data");
			Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			map.put("skip", (page - 1) * rows);
			map.put("rows", rows);
			Grid<Map<String, Object>> data = doorInfoService.getNoMonitorParam(map);
			printHttpServletResponse(GsonUtil.toJson(data));
		}

		/**
		 * 监控门 
		 * @author minting.he
		 */
		public void getMonitor() {
		/*    HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("data");
			Map<String, Object> map = GsonUtil.toBean(beanObject, HashMap.class);*/
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> data = doorInfoService.getMonitor(map);
			printHttpServletResponse(GsonUtil.toJson(data));
		}
		
		/**
		 * 更新监控门
		 * @author minting.he
		 */
		public void updateMonitorParam(){
			HttpServletRequest request=getHttpServletRequest();
			JsonResult result = new JsonResult();
			String device_yes = request.getParameter("device_yes");
			String door_yes = request.getParameter("door_yes");
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			if(BeanUtil.isEmpty(user)){
				result.setSuccess(false);
				result.setMsg("修改失败，请先登录");
			}else {
				String login_user = user.getYhMc();
				boolean r = doorInfoService.updateMonitorParam(device_yes, door_yes, login_user);
				if(r){
					result.setSuccess(true);
					result.setMsg("修改成功");
				}else {
					result.setSuccess(false);
					result.setMsg("修改失败");
				}
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
}
