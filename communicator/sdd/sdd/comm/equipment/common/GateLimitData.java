package com.kuangchi.sdd.comm.equipment.common;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Data;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

/**
 * 门权限对象
 * @author yu.yao
 *
 */
/**
 * 卡编号	卡有效期（开始）	卡有效期（结束）	开门密码	时段组	权限标志	删除标记	（保留）	
     3	      5	                    5	              2	          4	      2	          1	           1	字节数
 * 
 * 
 */
public class GateLimitData extends Data {
	public static final Logger LOG = Logger.getLogger(GateLimitData.class);	
	private int gateId; // 门号
	private int seq; // 连续下载的序号
	private int cardId; // 卡编号 3字节
	private int[] start; // 卡有效期开始时间 5字节
	private int[] end;   // 卡有效期结束时间 5字节
	private int password; // 开门密码 2字节
	//如0xffffffff  高两位字节，低字节保留  高位字节取0x0000--->0x0015   ,低位固定是ffff
	private long group; // 时段组 4字节
	//如0xffff  高字节保留，低字节被划分为8个bit    0,2,4,6 为分别表示0，1，2，3号门是否有时段组，1,3,5,7表示 0，1，2，3号门是否有权限
	private int limitSign; // 权限标志 2字节
	//后四位bit位，分别表示0,1,2,3号门是否有权限
	private int deleteSign;// 删除标记 1字节
	private int retain; // 保留

	/**
	 * 获取校验码
	 * 
	 * @return
	 */
	public long getCrcFromSum() {
		// 计算卡编号校验和
		int cardIdSum = 0;
		int m0,m1, m2, m3;
		m0 = (cardId & 0xFF000000) >> 24;
		m1 = (cardId & 0x00FF0000) >> 16;
		m2 = (cardId & 0x0000FF00) >> 8;
		m3 = (cardId & 0x000000FF);
		cardIdSum += (m1 + m2 + m3 + m0);
		LOG.info("cardIdSum=" + cardIdSum);
		// 计算卡有效期开始时间校验和
		int startSum = 0;
		if (start != null) {
			for (int i = 0; i < start.length; i++) {
				startSum += start[i];
				LOG.info("start["+i+"]" + start[i]);
			}
			LOG.info("startSum" + startSum);
		}
		// 计算卡有效期开始时间校验和
		int endSum = 0;
		if (end != null) {
			for (int i = 0; i < end.length; i++) {
				endSum += end[i];
			}
			LOG.info("endSum" + endSum);
		}
		// 计算开门密码校验和
		int passwordSum = 0;
		int s1, s2;
		s1 = (password & 0xFF00) >> 8;
		s2 = (password & 0x00FF);
		passwordSum += (s1 + s2);
		LOG.info("passwordSum=" + passwordSum);
		// 处理时段组校验和
		long groupSum = 0;
		long g1, g2, g3, g4;
		g1 = ((group>>>24) & 0x000000FF);
		g2 = (group & 0x00FF0000) >> 16;
		g3 = (group & 0x0000FF00) >> 8;
		g4 = (group & 0x000000FF);
		groupSum += (g1 + g2 + g3 + g4);
		LOG.info("groupSum=" + groupSum);
		// 计算权限标记校验和 2字节
		int limitSignSum = 0;
		int l1, l2;
		l1 = (limitSign & 0xFF00) >> 8;
		l2 = (limitSign & 0x00FF);
		limitSignSum += (l1 + l2);
		LOG.info(limitSign+"   limitSign=" + limitSignSum);
		// 计算删除标记校验和 2字节
		int deleteSignSum = 0;
		int d1, d2;
		//d1 = (deleteSign & 0xFF00) >> 8;
		//d2 = (deleteSign & 0x00FF);
		//deleteSignSum += (d1 + d2);
		deleteSignSum += deleteSign;
		LOG.info("deleteSignSum=" + deleteSignSum);
		long returnVal = getGateId() + getSeq()+cardIdSum+
				        startSum+endSum+passwordSum+groupSum+
				        limitSignSum+deleteSignSum+
				        getRetain();
		LOG.info("returnVal=" + returnVal);
		return returnVal;
	}

	public int getGateId() {
		return gateId;
	}

	public void setGateId(int gateId) {
		this.gateId = gateId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int[] getStart() {
		return start;
	}

	public void setStart(int[] start) {
		this.start = start;
	}

	public int[] getEnd() {
		return end;
	}

	public void setEnd(int[] end) {
		this.end = end;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public long getGroup() {
		return group;
	}

	public void setGroup(long group) {
		this.group = group;
	}

	public int getLimitSign() {
		return limitSign;
	}

	public void setLimitSign(int limitSign) {
		this.limitSign = limitSign;
	}

	public int getDeleteSign() {
		return deleteSign;
	}

	public void setDeleteSign(int deleteSign) {
		this.deleteSign = deleteSign;
	}

	public int getRetain() {
		return retain;
	}

	public void setRetain(int retain) {
		this.retain = retain;
	}
}
