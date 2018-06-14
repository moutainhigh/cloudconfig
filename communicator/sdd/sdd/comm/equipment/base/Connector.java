package com.kuangchi.sdd.comm.equipment.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;

public abstract class Connector<T extends Data> {
    private T data;
    private SendHeader sendHeader;
    private DeviceInfo2 deviceInfo;
	
   

	public DeviceInfo2 getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo2 deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * 配置基础发送参数（除有效数据）
	 * @return 
	 * @return
	 */
	public void setHeader(SendHeader pkg){
		this.sendHeader = pkg;
	}
	
	protected SendHeader getHeader(){
		return this.sendHeader;
	}
    /**
     * 配置有效数据
     * @param info
     */
	public void setData(T info) {
		this.data = info;
	}
	protected T getData(){
    	return (T)data;
    } 
	/**
	 * 与控制器进行通讯的基础方法
	 * @param handler 返回信息处理类
	 * @param manager 发送参数处理类
	 * @return
	 */
	public String connect (BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		String result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true)
					.handler(handler);
			Channel ch = b.bind(manager.getLocalHostIP(), manager.getLocalPort()).sync().channel();
			// 向网段内的所有机器广播UDP消息
			ch.writeAndFlush(
					new DatagramPacket(manager.sendSetMachineParameter(getData(),getHeader()),
							new InetSocketAddress(manager.getBroadcastIP(), manager.getBroadcastPort())))
					.sync();
			
			
			ch.closeFuture().await(500);

			if(handler.getData() != null){
				result = handler.getData();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		return result;
	}
	
	
	
	
	
	/**
	 * 获取时间的方法
	 * @param handler 返回信息处理类
	 * @param manager 发送参数处理类
	 * @return
	 */
	public String connectGetTime (BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		String result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true)
					.handler(handler);
			Channel ch = b.bind(manager.getLocalHostIP(), manager.getLocalPort()).sync().channel();
			// 向网段内的所有机器广播UDP消息
			ch.writeAndFlush(
					new DatagramPacket(manager.sendSetMachineParameter(getData(),getHeader()),
							new InetSocketAddress(manager.getBroadcastIP(), manager.getBroadcastPort())))
					.sync();
			
			
			ch.closeFuture().await(200);

			if(handler.getData() != null){
				result = handler.getData();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		return result;
	}
	
	
	
	/**
	 * 与控制器进行通讯的基础方法
	 * @param handler 返回信息处理类
	 * @param manager 发送参数处理类
	 * @return
	 */
	public List<String> connectSearch (BaseMultiPacketHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		List<String> result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true)
					.handler(handler);
			Channel ch = b.bind(manager.localHostIP, manager.localPort).sync().channel();
			// 向网段内的所有机器广播UDP消息
			ch.writeAndFlush(
					new DatagramPacket(manager.sendSetMachineParameter(getData(),getHeader()),
							new InetSocketAddress(manager.getBroadcastIP(), manager.broadcastPort)))
					.sync();
			ch.closeFuture().await(1000);
			 
			if(handler.getData() != null){
				result = handler.getData();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			
		}

		return result;
	}
	
	
	
	
	/**
	 * 获取控制器的事件通道信息
	 * @param handler 返回信息处理类
	 * @param manager 发送参数处理类
	 * @return
	 */
	public String event (BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		String result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true)
					.handler(handler);
			Channel ch = b.bind(manager.getLocalHostIP(), manager.getLocalPort()).sync().channel();
			// 向网段内的所有机器广播UDP消息
			ch.writeAndFlush(
					new DatagramPacket(manager.sendSetMachineParameter(getData(),getHeader()),
							new InetSocketAddress(manager.getEquipmentIP(), manager.getEquipmentEventPort())))
					.sync();
			ch.closeFuture().await(2000);
			
			if(handler.getData() != null){
				result = handler.getData();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		return result;
	}
	
	
	public void connectEvent(BaseHandler handler,Manager manager){
		EventLoopGroup group = new NioEventLoopGroup();
		// String result = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_REUSEADDR, true).handler(handler);
			Channel ch = b.bind(manager.getLocalHostIP(), manager.getLocalPort()).sync().channel();
			// 向网段内的所有机器广播UDP消息
						ch.writeAndFlush(
								new DatagramPacket(manager.sendSetMachineParameter(getData(),getHeader()),
										new InetSocketAddress(manager.getEquipmentIP(), manager.getEquipmentRecordPort())))
								.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	
    public abstract String run () throws Exception; 
}
