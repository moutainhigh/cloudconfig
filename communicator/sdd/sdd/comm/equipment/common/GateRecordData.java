package com.kuangchi.sdd.comm.equipment.common;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Data;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

/**
 * 记录门禁系统的头指针和记录信息
 * 
 * @author yu.yao 
 * 数据样例： 0xMMMMMMMMnnnn： 门禁记录头指针和门禁记录数。 　　 
 * 0xGGGGGGGGnnnn：巡更记录头指针和巡更记录数。 　　 
 * 0xUUUUUUUUnnnn： 用户记录头指针和用户记录数。 　　 
 * 0xAAAAAAAA：设备当前门禁权限个数。 　　 
 * 0xBB： 设备当前巡更权限个数。
 */
public class GateRecordData extends Data {
	public static final Logger LOG = Logger.getLogger(GateRecordData.class);	
	private long gatePoint;// 门禁记录头指针  即总记录数，包括已经上报的和未上报的
	private int gateRecord;// 门禁记录数 ,未上报的记录数
	private long patrolPoint;// 巡更记录头指针 即总记录数，包括已经上报的和未上报的
	private int patrolRecord;// 巡更记录数,未上报的记录数
	private long userPoint;// 用户记录头指针 即总记录数，包括已经上报的和未上报的
	private int userRecord;// 用户记录数  ,未上报的记录数
	private long gateLimitNum;// 门禁权限记录数
	private int patrolLimitNum;// 巡更权限记录数
    private int recordType;//记录类型
	public long getCrcFromSum() {
		// 计算门禁记录头指针校验和
		long gatePointSum = 0;
		long g1, g2, g3, g4;
		g1 = (gatePoint & 0xFF000000) >> 24;
		g2 = (gatePoint & 0x00FF0000) >> 16;
		g3 = (gatePoint & 0x0000FF00) >> 8;
		g4 = (gatePoint & 0x000000FF);
		gatePointSum += (g1 + g2 + g3 + g4);
		LOG.info("gatePointSum=" + gatePointSum);

		// 计算门禁记录数校验和
		int gateRecordSum = 0;
		int gr1, gr2;
		gr1 = (gateRecord & 0xFF00) >> 8;
		gr2 = (gateRecord & 0x00FF);
		gateRecordSum += (gr1 + gr2);
		LOG.info("gateRecordSum=" + gateRecordSum);


		// 计算巡更记录头指针校验和
		long patrolPointSum = 0;
		long ph1, ph2, ph3, ph4;
		ph1 = (patrolPoint & 0xFF000000) >> 24;
		ph2 = (patrolPoint & 0x00FF0000) >> 16;
		ph3 = (patrolPoint & 0x0000FF00) >> 8;
		ph4 = (patrolPoint & 0x000000FF);
		patrolPointSum += (ph1 + ph2 + ph3 + ph4);
		LOG.info("patrolPointSum=" + patrolPointSum);

		// 计算巡更记录数校验和
		int patrolRecordSum = 0;
		int pr1, pr2;
		pr1 = (patrolRecord & 0xFF00) >> 8;
		pr2 = (patrolRecord & 0x00FF);
		patrolRecordSum += (pr1 + pr2);
		LOG.info("patrolRecordSum=" + patrolRecordSum);

		// 计算用户记录头指针校验和
		long userPointSum = 0;
		long uh1, uh2, uh3, uh4;
		uh1 = (userPoint & 0xFF000000) >> 24;
		uh2 = (userPoint & 0x00FF0000) >> 16;
		uh3 = (userPoint & 0x0000FF00) >> 8;
		uh4 = (userPoint & 0x000000FF);
		userPointSum += (uh1 + uh2 + uh3 + uh4);
		LOG.info("userPointSum=" + userPointSum);

		// 计算用户记录数校验和
		int userRecordSum = 0;
		int ur1, ur2;
		ur1 = (userRecord & 0xFF00) >> 8;
		ur2 = (userRecord & 0x00FF);
		userRecordSum += (ur1 + ur2);
		LOG.info("userRecordSum=" + userRecordSum);
 
		//计算四位保留位
		long gateLimitNumSum = 0;
		long gn1, gn2,gn3,gn4;
		gn1 = (gateLimitNum & 0xFF000000) >> 24;
		gn2 = (gateLimitNum & 0x00FF0000) >> 16;
		gn3 = (gateLimitNum & 0x0000FF00) >> 8;
		gn4 = (gateLimitNum & 0x000000FF);
		gateLimitNumSum += (gn1 + gn2+gn3+gn4);
		LOG.info("gateLimitNumSum=" + gateLimitNumSum);
		
		//计算一位保留位
		int patrolLimitNumSum = 0;
		int pln1;
		pln1 = (patrolLimitNum & 0x00FF);
		patrolLimitNumSum += pln1;
		LOG.info("patrolLimitNumSum=" + patrolLimitNumSum);
		
		long result = gatePointSum+gateRecordSum+patrolPointSum+patrolRecordSum+
				     userPointSum+userRecordSum+gateLimitNumSum+patrolLimitNumSum
				     ;
		return result;
	}

	public long getGatePoint() {
		return gatePoint;
	}

	public void setGatePoint(long gatePoint) {
		this.gatePoint = gatePoint;
	}

	public int getGateRecord() {
		return gateRecord;
	}

	public void setGateRecord(int gateRecord) {
		this.gateRecord = gateRecord;
	}

	public long getPatrolPoint() {
		return patrolPoint;
	}

	public void setPatrolPoint(long patrolPoint) {
		this.patrolPoint = patrolPoint;
	}

	public int getPatrolRecord() {
		return patrolRecord;
	}

	public void setPatrolRecord(int patrolRecord) {
		this.patrolRecord = patrolRecord;
	}

	public long getGateLimitNum() {
		return gateLimitNum;
	}

	public void setGateLimitNum(long gateLimitNum) {
		this.gateLimitNum = gateLimitNum;
	}

	public int getPatrolLimitNum() {
		return patrolLimitNum;
	}

	public void setPatrolLimitNum(int patrolLimitNum) {
		this.patrolLimitNum = patrolLimitNum;
	}

	public long getUserPoint() {
		return userPoint;
	}

	public void setUserPoint(long userPoint) {
		this.userPoint = userPoint;
	}

	public int getUserRecord() {
		return userRecord;
	}

	public void setUserRecord(int userRecord) {
		this.userRecord = userRecord;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}
    
}
