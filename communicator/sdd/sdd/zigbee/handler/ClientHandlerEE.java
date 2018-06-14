package com.kuangchi.sdd.zigbee.handler;



import org.apache.log4j.Logger;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.zigbee.model.Data;



import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandlerEE extends ChannelHandlerAdapter {
	public static final Logger LOG = Logger.getLogger(ClientHandlerEE.class);
	  DataWritePolicy dataWritePolicy;
	  
	  Data data;


     

	public ClientHandlerEE(DataWritePolicy dataWritePolicy, Data data) {
		super();
		this.dataWritePolicy = dataWritePolicy;
		this.data = data;
	}





	@Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	          dataWritePolicy.writeData(ctx, data);
	    }

	    
	    
	    
	    
	    @Override
	    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	    	LOG.info("------------------关闭ClientEE客户端连接---------------------------");
	    }
	    
	   
	    
	    @Override
	        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	            cause.printStackTrace();
	            ctx.close();
	        }
}
