package com.kuangchi.sdd.commConsole.gateRecord.action;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.gateRecord.model.Result_Msg;
import com.kuangchi.sdd.commConsole.gateRecord.service.IGetGateRecord;

@Controller("getGateRecordAction")
public class GetGateRecordAction extends BaseActionSupport {
	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="getGateRecordServiceImpl")
	private IGetGateRecord getGateRecordService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void getGateRecord(){
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		List<HashMap> gateRecordList=getGateRecordService.getGateRecord(mac,deviceType);
		Result_Msg rm=new Result_Msg();
		if(gateRecordList!=null){//[null]
			rm.setResult_code("0");
			rm.setResult_msg("获取门记录信息成功");
			rm.setGateRecordList(gateRecordList);
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("获取门记录信息失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

}
