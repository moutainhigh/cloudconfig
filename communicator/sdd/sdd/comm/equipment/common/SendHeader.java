package com.kuangchi.sdd.comm.equipment.common;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.Data;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

/**
 * 装载发送数据
 * 
 * @author yu.yao
 * 
 */

public class SendHeader extends Data{
	public static final Logger LOG = Logger.getLogger(SendHeader.class);	
	private int header;// 报头
	private int sign; // 设备标志
	private int mac; // MAC地址 3字节
	private int order; // 命令字
	private int length; // 有效数据长度
	private int crc; // 校验和=报头+设备标志+IP(A+B+C)+order+length+data
	private int orderStatus;// 命令状态
	private SendData data; // 有效数据

	public SendData getData() {
		return data;
	}

	/**
	 * 设置校验值
	 * 
	 * @return
	 */
	public int getCrcFromSum() {
		int result = 0;
		if (length > 255) {
			int l1 = (length & 0xFF00) >> 8;
			int l2 = (length & 0x00FF);
			result += l1 + l2;
		} else {
			result = getLength();
		}
		// 计算Mac地址校验和
		int m1 = (mac & 0xFF0000) >> 16;
		int m2 = (mac & 0x00FF00) >> 8;
		int m3 = (mac & 0x0000FF);
		result += header + sign + m1 + m2 + m3 + order + orderStatus
				+ getCrcFromData();
		LOG.info("发送端的校验和="+result);
		return result&0xffff;
	}

	/**
	 * 获取有效数据的校验值
	 * 
	 * @return
	 */
	private int getCrcFromData() {
		int crc = 0;
		if (data != null) {
			crc += (data.getEquipmentData() == null ? 0 : data
					.getEquipmentData().getCrcFromSum())
					+ (data.getTimeData() == null ? 0 : data.getTimeData()
							.getCrcFromSum())
					+ (data.getGateParamData() == null ? 0 : data
							.getGateParamData().getCrcFromSum())
					+ (data.getGateLimitData() == null ? 0 : data
							.getGateLimitData().getCrcFromSum())
					+ (data.getGateRecordData() == null ? 0 : data
							.getGateRecordData().getCrcFromSum())
					+ (data.getGateStatusData() == null ? 0 : data
							.getGateStatusData().getCrcFromSum())
					+ (data.getTimeBlock() == null ? 0 : data.getTimeBlock()
							.getCrcFromSum())+
							(data.getUserTimeBlock()==null?0:data.getUserTimeBlock().getCrcFromSum())
							+(data.getRespondRecord()==null?0:data.getRespondRecord().getCrcFromSum())
							+(data.getDeviceTimeBlock()==null?0:data.getDeviceTimeBlock().getCrcFromSum())
							+(data.getClearData()==null?0:data.getClearData().getCrcFromSum())+
							(data.getDoorData()==null?0:data.getDoorData().getCrcFromSum())+
							(data.getTimeGroupForbid()==null?0:data.getTimeGroupForbid().getCrcFromSum())+
							(data.getDown()==null?0:data.getDown().getCrcFromSum())+
							(data.getOver()==null?0:data.getOver().getCrcFromSum())+
							(data.getPackageDown()==null?0:data.getPackageDown().getCrcFromSum());
		}
		return crc;
	}

	public void setData(SendData data) {
		this.data = data;
	}

	public int getHeader() {
		return header;
	}

	public void setHeader(int header) {
		this.header = header;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCrc() {
		return this.crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getMac() {
		return mac;
	}

	public void setMac(int mac) {
		this.mac = mac;
	}

}
