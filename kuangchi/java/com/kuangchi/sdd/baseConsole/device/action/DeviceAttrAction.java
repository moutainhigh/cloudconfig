package com.kuangchi.sdd.baseConsole.device.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
@Scope("prototype")
@Controller("deviceAttrAction")
public class DeviceAttrAction extends BaseActionSupport{
	@Autowired
	DeviceService deviceService;  
	private DeviceAttriModel model;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	@Override
	public Object getModel() {
		return model;
	}
	public DeviceAttrAction(){
		model=new DeviceAttriModel();
	}
	//打开页面显示所有的记录
	public void  getAllDeviceAttrs(){
		Grid<DeviceAttriModel> grid=null;
		 HttpServletRequest request = getHttpServletRequest();
		 String beanObject = getHttpServletRequest().getParameter("data");
		 DeviceAttriModel bean = GsonUtil.toBean(beanObject, DeviceAttriModel.class);	//将数据转化为javabean
		 Integer page=Integer.parseInt(request.getParameter("page"));
		 Integer rows=Integer.parseInt(request.getParameter("rows"));
		 Integer skip=(page-1)*rows;
		 grid=deviceService.searchDeviceAttribute(bean.getDeviceName(), bean.getDeviceMac(), skip, rows);
	     printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//多条件查询
	public void queryDeviceAttrs(){
		 HttpServletRequest request = getHttpServletRequest();
		 String deviceNum = getHttpServletRequest().getParameter("deviceNum");
		 DeviceAttriModel deviceAttriModel=deviceService.getDeviceAttributeByDeviceNum(deviceNum);
	     printHttpServletResponse(GsonUtil.toJson(deviceAttriModel));
	}
	
	/**
	 * 私有的工具方法，将二进制字符串按权运算转换成10进制字符串
	 * @param binStr 111
	 * @return 7
	 */
	private static String tranBinStrToDeciStr(String binStr){
		char[] c=binStr.toCharArray();
		String rt="";
		for(int i=binStr.length()-1;i>=0;i--){
			rt+=c[i];
		}
		Double sum=0.0;
		for(int i=0;i<binStr.length();i++){
			sum+=Double.parseDouble(Character.valueOf(rt.charAt(i)).toString())*Math.pow(2, i);
		}
		return (int)sum.doubleValue()+"";
	}
	
	//修改
	public void modifyDeviceAttrs(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result =new JsonResult();
		DeviceAttriModel deviceAttriBean=new DeviceAttriModel();
		try{  	 
			BeanUtils.copyProperties(model, deviceAttriBean);
			 User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			 Map deviceMap=deviceService.getMacByDeviceNum(deviceAttriBean.getDeviceNum());
			  String mac=(String)deviceMap.get("deviceMac");
//			  String deviceType=(String)deviceMap.get("deviceType");
			  String useFireFighting=deviceAttriBean.getFireFlag();
			  String deviceType=(String)deviceMap.get("deviceType");
			  StringBuilder build=new StringBuilder();
			  
			  String deviceNum=mjCommService.getTkDevNumByMac(mac);
			  
			  build.append(deviceAttriBean.getFourLockControlFlag());
			  build.append(deviceAttriBean.getThreeLockControlFlag());
			  build.append(deviceAttriBean.getTwoLockControlFlag());
			  build.append(deviceAttriBean.getOneLockControlFlag());
			  build.append(deviceAttriBean.getTwoOutControlFlag());
			  build.append(deviceAttriBean.getOneOutControlFlag());
			  
			  
			  String inOutParam=tranBinStrToDeciStr(build.toString());
			  
			 /* Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
			  String fireUrl=mjCommService.getMjCommUrl(deviceNum)+"fireAction/setFire.do?";
			  String setInOutUrl=mjCommService.getMjCommUrl(deviceNum)+"doubleInOut/setDoubleInOut.do?";
			  //TODO   调用通讯服务器接口
			  String  fireRespStr=HttpRequest.sendPost(fireUrl, "useFireFighting="+useFireFighting+"&mac="+mac+"&sign="+deviceType);
			  String  setInOutRespStr=HttpRequest.sendPost(setInOutUrl, "inOut="+inOutParam+"&mac="+mac+"&deviceType="+deviceType);
			  
			  Map fireMap=GsonUtil.toBean(fireRespStr,HashMap.class);
			  Map inOutMap=GsonUtil.toBean(setInOutRespStr,HashMap.class);
			  
			  if(fireMap!=null && inOutMap!=null){
				  if("0".equals(fireMap.get("result_code")) && "0".equals(inOutMap.get("result_code")) ){
					  deviceService.setDeviceAttribute(deviceAttriBean.getDeviceNum(), deviceAttriBean.getHeaderCardFlag(), deviceAttriBean.getOneOutControlFlag(), deviceAttriBean.getTwoOutControlFlag(), deviceAttriBean.getOneLockControlFlag(), deviceAttriBean.getTwoLockControlFlag(), deviceAttriBean.getThreeLockControlFlag(), deviceAttriBean.getFourLockControlFlag(), deviceAttriBean.getDelayOpenDoorTime(), deviceAttriBean.getFireFlag(),loginUser.getYhMc());
				  	  result.setSuccess(true);
				  }else{
					  result.setSuccess(false);
				  }
			  }else{
				  	 result.setSuccess(false);
			  }
			  printHttpServletResponse(GsonUtil.toJson(result));
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		  result.setMsg("设置失败");
	              result.setSuccess(false);		
	              printHttpServletResponse(GsonUtil.toJson(result));
	    	}
	    }
	
	private class ResultMsg {
		private String result_code;
		private String result_msg;
		private List<Map> map;
		public List<Map> getMap() {
			return map;
		}
		public void setMap(List<Map> map) {
			this.map = map;
		}
		public String getResult_code() {
			return result_code;
		}
		public void setResult_code(String result_code) {
			this.result_code = result_code;
		}
		public String getResult_msg() {
			return result_msg;
		}
		public void setResult_msg(String result_msg) {
			this.result_msg = result_msg;
		}
	}
	
	
	
	
	/**
	 * 获取设备记录数
	 * by gengji.yang
	 */
	public void getDeviceRecords(){
		 HttpServletRequest request = getHttpServletRequest();
		 JsonResult result=new JsonResult();
		 try{
			 String deviceMac = getHttpServletRequest().getParameter("deviceMac");
			/* Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
			
			 String deviceNum=mjCommService.getTkDevNumByMac(deviceMac);
			 Map deviceMap=deviceService.getMacByDeviceNum(deviceNum);
			 String url=mjCommService.getMjCommUrl(deviceNum)+"gateRecord/getGateRecord.do?";
			
			 String  recordStr=HttpRequest.sendPost(url, "mac="+deviceMac+"&deviceType="+deviceMap.get("deviceType"));
			 Map recordMap=GsonUtil.toBean(recordStr,HashMap.class);// gateRecordList=[{gatePoint=8000000.0, gateRecord=0.0, patrolPoint=0.0, patrolRecord=0.0, userPoint=0.0, userRecord=4400.0, gateLimitNum=0.0, patrolLimitNum=0.0, recordType=0.0}]}
			 if(recordMap!=null){// gateRecord=0.0 patrolRecord=0.0 userRecord=4400.0 gateLimitNum=0.0, patrolLimitNum=0.0
				 List<Map>list=(List<Map>) recordMap.get("gateRecordList");
				 deviceService.updateDeviceRecord(deviceMac, list);
				 if(list.size()>0){
					 Map m=new HashMap();
					 m.put("gateRecord",Integer.parseInt((String)list.get(0).get("gateRecord"),16));
					 m.put("gatePoint",Integer.parseInt((String)list.get(0).get("gatePoint"),16));
					 m.put("userRecord",Integer.parseInt((String)list.get(0).get("userRecord"),16));
					 m.put("userPoint",Integer.parseInt((String)list.get(0).get("userPoint"),16));
					 printHttpServletResponse(GsonUtil.toJson(m));
				 }else{
					 result.setSuccess(false);
					 printHttpServletResponse(GsonUtil.toJson(result));
				 }
			 }else{
				 result.setSuccess(false);
				 printHttpServletResponse(GsonUtil.toJson(result));
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 result.setSuccess(false);
			 printHttpServletResponse(GsonUtil.toJson(result));
		 }
	}
	
}



