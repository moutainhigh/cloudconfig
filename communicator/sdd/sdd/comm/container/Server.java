package com.kuangchi.sdd.comm.container;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import com.kuangchi.sdd.comm.equipment.base.BaseHandler;
import com.kuangchi.sdd.comm.equipment.base.Manager;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.ReceiveHeader;
import com.kuangchi.sdd.comm.equipment.common.RespondRecord;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadRecordHandler;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadRecordManager;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus3Handler;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus3Manager;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus4Handler;
import com.kuangchi.sdd.comm.equipment.upload.record.UploadStatus4Manager;
//门禁控制器Server
public class Server {
	private boolean shutdown = true;
    /**
     * 启动上报记录监听服务器
     * 
     * 
     * @param server
     */
	public void startRecordUpload(Server server){
		while (shutdown) {
			try {
				listenEquipmentRecord();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * 启动上报状态的监听
	 * */
	public void startStatusUpload(Server server){
		while (shutdown) {
 			//server.listenEquipmentStatus3();
			try {
				server.listenEquipmentStatus4();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭服务器
	 */
	public void shutdown(){
		shutdown = false;
	}
	
	/**
	 * 侦听3字节卡号的门禁设备状态
	 */
	private void listenEquipmentStatus3() {
		BaseHandler handler = new UploadStatus3Handler();
		handler.setShouldClose(false);//设置netty收到消息后是否关闭ChannelContextHandler  
		DeviceInfo2 deviceInfo2=new DeviceInfo2();
		Manager manager = new UploadStatus3Manager(deviceInfo2);// 设备接口类
		// return result;
		connect(handler, manager);
	}
	/**
	 * 侦听4字节卡号的门禁设备状态
	 */
	private void listenEquipmentStatus4() {
		BaseHandler handler = new UploadStatus4Handler();
		handler.setShouldClose(false);//设置netty收到消息后是否关闭ChannelContextHandler  
		DeviceInfo2 deviceInfo2=new DeviceInfo2();
		Manager manager = new UploadStatus4Manager(deviceInfo2);// 设备接口类
		// return result;
		connect(handler, manager);
	}
	/**
	 * 侦听门禁出入记录
	 */
	private void listenEquipmentRecord() {
		BaseHandler handler = new UploadRecordHandler();
		handler.setRecordUp(true);
		handler.setShouldClose(false);//设置netty收到消息后是否关闭ChannelContextHandler  
		Manager manager = new UploadRecordManager();// 设备接口类
		connectEvent(handler, manager);
	}
	
	//监听状态上传的server
	private void connect(BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		// String result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true).handler(handler)
					.option(ChannelOption.SO_RCVBUF, 1024 * 1024*5);
			ChannelFuture  ch=b.bind(Manager.equipmentStatusPort).sync().channel()
					.closeFuture().await();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	
	//监听记录上传的server
	private void connectEvent(BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true).handler(handler)
					.option(ChannelOption.SO_RCVBUF, 1024 * 1024*5);
			b.bind(Manager.equipmentEventPort).sync().channel()
					.closeFuture().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	
	public static void main(String args[]) {
	/*	Server server = new Server();
		server.start(server);*/
	}
}
