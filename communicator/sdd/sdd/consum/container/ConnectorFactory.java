package com.kuangchi.sdd.consum.container;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public  class ConnectorFactory {

                public static final Map<String, ChannelHandlerContext> connections=new HashMap<String, ChannelHandlerContext>();
                
                //上一次心跳包时间
                public static  Map<String, Date> channelHandlerContextTimeMap=new HashMap<String, Date>();
                
                //上操作时间
                public static  Map<String, Date> lastOperateTimeMap=new HashMap<String, Date>();
                
                public synchronized static ChannelHandlerContext getConnection(String machine){
                	return connections.get(machine);
                }
                
                public synchronized static void  registerConnection(String key,ChannelHandlerContext ctx){
                	
                	connections.put(key, ctx);
                	channelHandlerContextTimeMap.put(key, new Date());
                	
                }
                
                
                 
}
