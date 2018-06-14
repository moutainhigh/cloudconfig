package com.kuangchi.sdd.commConsole.gateRecord.service;

import java.util.HashMap;
import java.util.List;

public interface IGetGateRecord {
	public List<HashMap> getGateRecord(String mac,String deviceType);//获取门记录信息
}
