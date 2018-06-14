package com.kuangchi.sdd.zigbee.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.kuangchi.sdd.zigbee.model.Data;
import com.kuangchi.sdd.zigbee.pool.DataWritePolicyFactory;

public class ClientEE {
         protected String remoteIp;
         protected int port;
         Data data;
         
		public ClientEE(String remoteIp, int port,Data data) {
			super();
			this.remoteIp = remoteIp;
			this.port = port;
			this.data=data;
		}
           
		public void run() throws Exception {
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            Bootstrap b = new Bootstrap(); // (1)
	            b.group(workerGroup); // (2)
	            b.channel(NioSocketChannel.class); // (3)
	            b.option(ChannelOption.SO_REUSEADDR, true); // (4)
	            b.handler(new ChannelInitializer<SocketChannel>() {
	                DataWritePolicy dataWritePolicy=DataWritePolicyFactory.getDataWritePolicy(data);
	                @Override
	                public void initChannel(SocketChannel ch) throws Exception {
	                    ch.pipeline().addLast(new ClientHandlerEE(dataWritePolicy,data));
	                }
	            });
	            // Start the client.
	            ChannelFuture f = b.connect(remoteIp, port).sync(); // (5)

	            // Wait until the connection is closed.
	            f.channel().closeFuture().sync();
	            
	            
	            
	            System.out.println("连接关闭");
	            
	            
	            
	            
	        } finally {
	            workerGroup.shutdownGracefully();
	        }
	    }
}
