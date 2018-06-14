package com.kuangchi.sdd.websocket;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
public class WebSocketServer {
	public static final Logger LOG = Logger.getLogger(WebSocketServer.class);
     public void run(int port){
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
             ServerBootstrap b = new ServerBootstrap();
             b.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     //HttpServerCodec将请求和应答消息编码或解码为HTTP消息
                     //通常接收到的http是一个片段，如果想要完整接受一次请求所有数据，我们需要绑定HttpObjectAggregator
                     //然后就可以收到一个FullHttpRequest完整的请求信息了
                     //ChunkedWriteHandler 向客户端发送HTML5文件，主要用于支持浏览器和服务器进行WebSocket通信
                     //WebSocketServerHandler自定义Handler
                     ch.pipeline().addLast("http-codec", new HttpServerCodec())
                                  .addLast("aggregator", new HttpObjectAggregator(65536)) //定义缓冲大小
                                  .addLast("http-chunked", new ChunkedWriteHandler())
                                  .addLast("handler", new WebSocketServerHandler());
                 }
             });
             
             ChannelFuture f = b.bind(port).sync();
             LOG.info("start...");
             f.channel().closeFuture().sync();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }
     
     public static void main(String[] args) {
         new WebSocketServer().run(7777);
     }
 }
