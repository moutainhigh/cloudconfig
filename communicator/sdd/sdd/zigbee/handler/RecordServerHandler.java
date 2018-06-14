package com.kuangchi.sdd.zigbee.handler;


import com.kuangchi.sdd.zigbee.model.Data0xAA0x04;



import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
/**
 *对网关发来的记录进行逻辑处理
 * */

public class RecordServerHandler extends ChannelHandlerAdapter {
	   @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    		throws Exception {  	
				 Data0xAA0x04 data0xAA0x04=(Data0xAA0x04) msg;
				 SendService.sendData0xEE0x04(data0xAA0x04, ctx);
	    }
	    
	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    		throws Exception {
	    	// TODO Auto-generated method stub
	    	super.exceptionCaught(ctx, cause);
	    }
}
