package com.kuangchi.sdd.comm.equipment.upload.record;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.common.GateStatusData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.server.GateTimeInterface;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.commonUtil.StringUtil;
import com.kuangchi.sdd.util.excel.ExcelExportServer;
import com.kuangchi.sdd.util.network.HttpRequest;
/**
 *上传门禁状态
 * */
public class UploadStatus4Handler extends
		BaseHandler<ByteBuf, GateTimeInterface, ReceiveData> {
	public static final Logger LOG = Logger.getLogger(UploadStatus4Handler.class);	
	/**
	 * 处理报头
	 * 
	 * @param receiveBuffer
	 */
	protected String executeResponse(ByteBuf receiveBuffer) {
		ReceiveHeader receiver = new ReceiveHeader();
		int header = receiveBuffer.readUnsignedByte();// 报头aa
		receiver.setHeader(header);
		int sign = receiveBuffer.readUnsignedByte();// 设备标志04
		receiver.setSign(sign);
		int mac = receiveBuffer.readUnsignedMedium();// MAC地址
		receiver.setMac(mac);
		int order = receiveBuffer.readUnsignedByte();// 命令字
		receiver.setOrder(order);
		int length = receiveBuffer.readUnsignedShort();// 有效数据长度 2字节
		receiver.setLength(length);
		// 设置有效数据
		ByteBuf dataBuf = receiveBuffer.readBytes(31);// 取31字节有效数据
		ReceiveData data = executeData(dataBuf);
		receiver.setData(data);
		dataBuf = null;

		int orderStatus = receiveBuffer.readUnsignedByte();// 命令状态
		receiver.setOrderStatus(orderStatus);
		int checkSum = receiveBuffer.readUnsignedShort();// 校验和
		receiver.setCrc(checkSum);
		LOG.info(receiver.getCrcFromSum());

		String returnValue = GsonUtil.toJson(data.getGateStatusData());
		if (receiver.getCrcFromSum() == checkSum) {
			LOG.info("数据校验成功！");
			returnValue = GsonUtil.toJson(data.getGateStatusData());
		}
		LOG.info("客户端接收的十六进制信息:"
				+ Util.lpad(Integer.toHexString(header), 2)
				+ "|"
				+ Util.lpad(Integer.toHexString(sign), 2)
				+ "|"
				// + Integer.toHexString(macAddress)
				+ Util.lpad(Integer.toHexString(mac), 6) + "|"
				+ Util.lpad(Integer.toHexString(order), 2) + "|"
				+ Util.lpad(Integer.toHexString(length), 3) + "|"
				+ Util.lpad(Integer.toHexString(orderStatus), 2) + "|"
				+ checkSum + "|");

		int param = data.getGateStatusData().getParam();
		int lock_state = data.getGateStatusData().getLock();
		int door_state = data.getGateStatusData().getMagnetic();
		int key_state = data.getGateStatusData().getKey();
		int skid_state = data.getGateStatusData().getAnti();
		int fire_state = data.getGateStatusData().getFire();
		int infrared = data.getGateStatusData().getInfrared();
		int deviceAnti = data.getGateStatusData().getDeviceAnti();
		List<Long> events = data.getGateStatusData().getEvent();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", param);
		map.put("lock_state", lock_state);
		map.put("door_state", door_state);
		map.put("key_state", key_state);
		map.put("skid_state", skid_state);
		map.put("fire_state", fire_state);
		map.put("infrared", infrared);
		map.put("deviceAnti", deviceAnti);
		// map.put("device_mac", mac);
		map.put("events", events);

		deviceState(map, mac);

		return returnValue;

	}

	/**
	 * 获取有效数据
	 * 
	 * @param dataBuf
	 * @param bufLength
	 * @return
	 */
	protected ReceiveData executeData(ByteBuf dataBuf) {
		ReceiveData data = new ReceiveData();
		GateStatusData gateData = new GateStatusData();
		/**
		 * 读取锁的状态
		 */
		int lock = dataBuf.readUnsignedByte();
		gateData.setLock(lock);
		LOG.info("lock=" + lock);
		/**
		 * 读取门磁状态
		 */
		int magnetic = dataBuf.readUnsignedByte();
		gateData.setMagnetic(magnetic);
		LOG.info("magnetic=" + magnetic);
		/**
		 * 读取按键状态
		 */
		int key = dataBuf.readUnsignedByte();
		gateData.setKey(key);
		LOG.info("key=" + key);
		/**
		 * 读取防撬状态
		 */
		int anti = dataBuf.readUnsignedByte();
		gateData.setAnti(anti);
		LOG.info("anti=" + anti);
		/**
		 * 读取消防状态
		 */
		int fire = dataBuf.readUnsignedByte();
		gateData.setFire(fire);
		LOG.info("fire=" + fire);
		/**
		 * 红外状态
		 */
		int infrared = dataBuf.readUnsignedByte();
		gateData.setInfrared(infrared);
		LOG.info("infrared=" + infrared);
		/**
		 * 控制器防撬状态
		 */
		int deviceAnti = dataBuf.readUnsignedByte();
		gateData.setDeviceAnti(deviceAnti);
		LOG.info("deviceAnti=" + deviceAnti);
		/**
		 * 读取门的事件信息集合
		 */
		List<Long> events = new ArrayList<Long>();
		// 0号门 出入与读头事件
		long access0 = dataBuf.readUnsignedInt();
		int reader0 = dataBuf.readUnsignedByte();
		// 0号门 出入与读头事件
		long access1 = dataBuf.readUnsignedInt();
		int reader1 = dataBuf.readUnsignedByte();
		// 0号门 出入与读头事件
		long access2 = dataBuf.readUnsignedInt();
		int reader2 = dataBuf.readUnsignedByte();
		// 0号门 出入与读头事件
		long access3 = dataBuf.readUnsignedInt();
		int reader3 = dataBuf.readUnsignedByte();
		// 0-3号门报警事件
		int alarmEvent0 = dataBuf.readUnsignedByte();
		int alarmEvent1 = dataBuf.readUnsignedByte();
		int alarmEvent2 = dataBuf.readUnsignedByte();
		int alarmEvent3 = dataBuf.readUnsignedByte();

		events.add(Long.valueOf(access0));
		events.add(Long.valueOf(reader0));
		events.add(Long.valueOf(access1));
		events.add(Long.valueOf(reader1));
		events.add(Long.valueOf(access2));
		events.add(Long.valueOf(reader2));
		events.add(Long.valueOf(access3));
		events.add(Long.valueOf(reader3));
		events.add(Long.valueOf(alarmEvent0));
		events.add(Long.valueOf(alarmEvent1));
		events.add(Long.valueOf(alarmEvent2));
		events.add(Long.valueOf(alarmEvent3));
		gateData.setEvent(events);
		data.setGateStatusData(gateData);
		return data;
	}

	static Map<String, String> urlMap = new HashMap<String, String>();

	// 设备状态上报
	public void deviceState(Map<String, Object> map, int mac) {
		if (urlMap.isEmpty()) {
			urlMap = PropertiesToMap
					.propertyToMap("photoncard_interface.properties");
		}
		String deviceStateUrl = urlMap.get("url");
		String str = HttpRequest.sendPost(
				deviceStateUrl + "interface/checkHandle/updateDeviceState.do?",
				"data=" + map + "&device_mac="
						+ StringUtil.to_MJDevice_Mac(Integer.toHexString(mac)));
		LOG.info(GsonUtil.toJson(str));
	}

	@Override
	protected GateTimeInterface getDataForServer(ReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}

	// deviceState调用门禁系统增加告警事件接口方法的测试,返回结果是："{\"isSuccess\":true,\"msg\":\"设置成功\"}"
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", 1);
		map.put("lock_state", 1);
		map.put("door_state", 1);
		map.put("key_state", 1);
		map.put("skid_state", 1);
		map.put("fire_state", 1);
		map.put("infrared", 1);
		map.put("deviceAnti", 1);
		// map.put("device_mac", 123);
		List<Long> events = new ArrayList<Long>();
		events.add(4294967295L);
		events.add(254L);
		events.add(4294967295L);
		events.add(254L);
		events.add(4294967295L);
		events.add(254L);
		events.add(4294967295L);
		events.add(254L);
		events.add(128L);
		events.add(133L);
		events.add(128L);
		events.add(128L);
		map.put("events", events);
		int mac = 456;
		UploadStatus4Handler u = new UploadStatus4Handler();
		u.deviceState(map, mac);
	}
}
