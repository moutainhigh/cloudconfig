package com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.dao;

import java.util.Map;

public interface AllSynchronizeDao {
	public Map getMacAndType(String deviceNum);
	public void addSynAuthority(Map map);
	public void delSynAuthority(Map map);
}
