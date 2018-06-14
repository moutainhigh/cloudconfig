package com.kuangchi.sdd.commConsole.gateWorkParam.service;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.common.GateParamData;

public interface IGateWorkParam {
	public String getGateWorkParam(String sign,String mac,String gateId) ;//获取门的工作参数
	
	public int setGateWorkParam(String mac,String sign,GateParamData gateParamData);//设置门的工作参数
}
