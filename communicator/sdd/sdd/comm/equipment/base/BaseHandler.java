package com.kuangchi.sdd.comm.equipment.base;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import com.kuangchi.sdd.comm.container.Service;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.ReceiveData;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.RespondRecord;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadRecordHandler;
/**
 * 
 * @author 姚宇
 *
 * @param <I> 承载报文的容器格式，采用netty的ByteBuf 
 * @param <K> 嵌入式设备返回给服务器的数据类型
 * @param <T> 服务器返回给嵌入式设备的数据类型
 */
public abstract class BaseHandler<I,K,T extends Data> extends SimpleChannelInboundHandler<DatagramPacket>{
	private boolean shouldClose=true;
	
	private boolean isRecordUp=false;
	private String data;//返回给服务器的值
	public void setData(String str){
		this.data=str;
	}
	public String getData(){
		return this.data;
	}
	
	
	public boolean isShouldClose() {
		return shouldClose;
	}
	public void setShouldClose(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}
	
	
	
	
	public boolean isRecordUp() {
		return isRecordUp;
	}
	public void setRecordUp(boolean isRecordUp) {
		this.isRecordUp = isRecordUp;
	}
	//获取从嵌入式设备返回的信息
	@Override
	public void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		I receiveBuffer = (I)msg.content();
//		System.out.println("接口已连接");
		setData(executeResponse(receiveBuffer));
//		System.out.println("接口返回结果：="+this.getData());
		receiveBuffer = null;
		if(shouldClose){
			ctx.close();	
		}
		if(isRecordUp){  //如果是打卡记录上报
		try {
					//返回结果
					UploadRecordHandler recordHandler=(UploadRecordHandler) this;
					ReceiveHeader receiveHeader=recordHandler.getReceiveHeader();
			        SendHeader pkg = new SendHeader();
					pkg.setHeader(0x55);
					pkg.setSign(receiveHeader.getSign());
					
					pkg.setMac(receiveHeader.getMac());
					pkg.setOrder(0x40);
					pkg.setLength(0x0005);
					pkg.setOrderStatus(0x00);
					
					RespondRecord data = new RespondRecord();
					data.setRecordType(receiveHeader.getData().getCardRecord().getRecordType());
					data.setRecordId(Long.valueOf(receiveHeader.getData().getCardRecord().getRecordId()).intValue());
					
					SendData sendData = new SendData();
					sendData.setRespondRecord(data);
					pkg.setData(sendData);
					pkg.setCrc(pkg.getCrcFromSum());
					GetDevInfoService getDevInfoService=new GetDevInfoService();
					DeviceInfo2 deviceInfo22=getDevInfoService.getManager(Integer.toHexString(receiveHeader.getMac()));
					String result = Service.service("respond_record", pkg,deviceInfo22);
					} catch (Exception e) {
						e.printStackTrace();
					}
		}
	}
	
	
	
	
	
	
	
	
	
	
	//处理通讯报头
	protected abstract String executeResponse(I receiveBuffer);
	//处理有效数据
	protected abstract T executeData(I dataBuf);
	//配置返回给服务器的返回参数
	protected abstract K getDataForServer(T data);
	
	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		 ctx.close();
		super.disconnect(ctx, promise);
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		super.exceptionCaught(ctx, cause);

	}
}
