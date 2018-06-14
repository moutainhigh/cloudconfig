package com.kuangchi.sdd.comm.equipment.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 姚宇
 * 
 * @param <I>
 *            承载报文的容器格式，采用netty的ByteBuf
 * @param <K>
 *            嵌入式设备返回给服务器的数据类型
 * @param <T>
 *            服务器返回给嵌入式设备的数据类型
 */
public abstract class BaseMultiPacketHandler<I, K, T extends Data> extends
		SimpleChannelInboundHandler<DatagramPacket> {
	private List<String> data;// 返回给服务器的值

	public void setData(List<String> str) {
		this.data = str;
	}

	public List<String> getData() {
		return this.data;
	}

	// 获取从嵌入式设备返回的信息
	@Override
	public void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		I receiveBuffer = (I) msg.content();
//		System.out.println("接口已连接");
		if(data == null){
			data = new ArrayList<String>();
		}
		data.add(executeResponse(receiveBuffer));
//		setData(executeResponse(receiveBuffer));
//		System.out.println("接口返回结果：=" + this.getData());
		receiveBuffer = null;
//		ctx.close();
	}

	// @Override
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		System.out.println("接口调用异常");
		ctx.close();
	}

	// 处理通讯报头
	protected abstract String executeResponse(I receiveBuffer);

	// 处理有效数据
	protected abstract T executeData(I dataBuf);

	// 配置返回给服务器的返回参数
	protected abstract K getDataForServer(T data);

}
