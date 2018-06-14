package com.kuangchi.sdd.commConsole.gateWorkParam.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.gateWorkParam.model.Result_Msg;
import com.kuangchi.sdd.commConsole.gateWorkParam.service.IGateWorkParam;

@Controller("gateWorkParamAction")
public class GateWorkParamAction extends BaseActionSupport{
	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="gateWorkParamServiceImpl")
	private IGateWorkParam gateWorkParamService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public void getGateWorkParam(){
		String mac=getHttpServletRequest().getParameter("mac");
		String sign=getHttpServletRequest().getParameter("device_type");
		String gateId=getHttpServletRequest().getParameter("gateId");
		String gateWorkParam=gateWorkParamService.getGateWorkParam(sign,mac, gateId);
		Result_Msg rm=new Result_Msg();
		if(null!=gateWorkParam && !"null".equals(gateWorkParam)){
			rm.setResult_code("0");
			rm.setResult_msg("获取门工作参数成功");
			rm.setGateWorkParam(gateWorkParam);
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("获取门工作参数失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
	public void setGateWorkParam(){
		String mac=getHttpServletRequest().getParameter("mac");
		String sign=getHttpServletRequest().getParameter("device_type");
		String gateId=getHttpServletRequest().getParameter("gateId");
		String openDelay=getHttpServletRequest().getParameter("open_delay");
		String openOvertime=getHttpServletRequest().getParameter("open_overtime");
		String useSuperPassword=getHttpServletRequest().getParameter("use_super_password");
		String superPassword=getHttpServletRequest().getParameter("super_password");
		String useForcePassword=getHttpServletRequest().getParameter("use_force_password");
		String forcePassword=getHttpServletRequest().getParameter("force_password");
		String relockPassword=getHttpServletRequest().getParameter("relock_password");
		String unlockPassword=getHttpServletRequest().getParameter("unlock_password");
		String policePassword=getHttpServletRequest().getParameter("police_password");
		String openPattern=getHttpServletRequest().getParameter("open_pattern");
		String checkOpenPattern=getHttpServletRequest().getParameter("check_open_pattern");
		String workPattern=getHttpServletRequest().getParameter("work_pattern");
		String multiOpenNumber=getHttpServletRequest().getParameter("multi_open_number");
		String[] multiOpenCardNum=null;
        if(null!=getHttpServletRequest().getParameter("multi_open_card_num")&&!"".equals(getHttpServletRequest().getParameter("multi_open_card_num"))){
        	multiOpenCardNum=getHttpServletRequest().getParameter("multi_open_card_num").split(",");
	     }
 		
		
		GateParamData gateWorkParamModel=new GateParamData();
		gateWorkParamModel.setGateId(Integer.parseInt(gateId)-1);
		int open_delay=(int)(Double.valueOf(openDelay).intValue());
		//以100ms为单位
		if(open_delay>6553){//限制开门延时的值在2字节内
			open_delay=6553;
		}
		gateWorkParamModel.setOpenDelay(open_delay*10);//开门延时
		int open_Overtime=(int)(Double.valueOf(openOvertime).intValue());
		if(open_Overtime>255){//限制开门超时的值在1字节内
			open_Overtime=255;
		}
		gateWorkParamModel.setOpenOvertime(open_Overtime);//开门超时
		int use_super_password=Integer.parseInt(useSuperPassword);
		long super_password=Long.parseLong(superPassword);
		int use_force_password=Integer.parseInt(useForcePassword);
		int force_password=Integer.parseInt(forcePassword);
		int relock_password=Integer.parseInt(relockPassword);
		int unlock_password=Integer.parseInt(unlockPassword);
		int police_password=Integer.parseInt(policePassword);
		int open_pattern=Integer.parseInt(openPattern);
		int check_open_pattern=Integer.parseInt(checkOpenPattern);
		int work_pattern=Integer.parseInt(workPattern);
		int multi_open_number=Integer.parseInt(multiOpenNumber);
		long[] multi_open_card_num=null;
		if(null!=multiOpenCardNum){
			multi_open_card_num=new long[multiOpenCardNum.length];
			for(int i=0;i<multiOpenCardNum.length;i++){
				multi_open_card_num[i]=Long.parseLong(multiOpenCardNum[i]);
			}
		}
		
		
		gateWorkParamModel.setUseSuperPassword(use_super_password);//是否启用超级开门密码
		gateWorkParamModel.setSuperPassword(super_password);//超级开门密码
		gateWorkParamModel.setUseForcePassword(use_force_password);//是否启用胁迫密码
		gateWorkParamModel.setForcePassword(force_password);//胁迫密码
		gateWorkParamModel.setRelockPassword(relock_password);//重锁密码
		gateWorkParamModel.setUnlockPassword(unlock_password);//解锁密码
		gateWorkParamModel.setPolicePassword(police_password);//报警密码
		gateWorkParamModel.setOpenPattern(open_pattern);//功能模式
		gateWorkParamModel.setCheckOpenPattern(check_open_pattern);//验证开门模式
		gateWorkParamModel.setWorkPattern(work_pattern);//工作模式
		gateWorkParamModel.setMultiOpenNumber(multi_open_number);//多卡开门数量
		gateWorkParamModel.setMultiOpenCard(multi_open_card_num);//多卡开门卡号
		
		
		int i=gateWorkParamService.setGateWorkParam(mac,sign, gateWorkParamModel);
		Result_Msg rm=new Result_Msg();
		if(i==0){
			rm.setResult_code("0");
			rm.setResult_msg("设置门的工作参数成功");
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("设置门的工作参数失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

}
