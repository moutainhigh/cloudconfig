package com.kuangchi.sdd.zigbee.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.context.WebApplicationContext;

import com.kuangchi.sdd.consum.handler.serverHandler.ping.CheckOnLineThread;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.PingDecoder;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.PingServerHandler;
import com.kuangchi.sdd.consum.handler.serverHandler.ping.SendPersonDownThread;
/**
 * 网关发往平台的协议服务
 * 
 * */
public class Server {
	public static WebApplicationContext webApplicationContext;

	private int port;

	public Server() {
		super();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Server(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {

									ch.pipeline().addLast(new ServerDecoder(),
											new ServerHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128)
					// (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.RCVBUF_ALLOCATOR,
							new AdaptiveRecvByteBufAllocator(64, 1024, 65536)); // (6)
			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
