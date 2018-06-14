package com.kuangchi.sdd.zigbee.model;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import com.kuangchi.sdd.zigbee.handler.DataWritePolicy;
import com.kuangchi.sdd.zigbee.model.Data;
import com.kuangchi.sdd.zigbee.util.CRC8;
import com.kuangchi.sdd.zigbee.util.Tool;

public class DataWritePolicyEE0D implements DataWritePolicy {

	@Override
	public void writeData(ChannelHandlerContext ctx, Data data)
			throws Exception {
  	    Data0xEE0x0D data0xEE0x0D=(Data0xEE0x0D) data;

		ByteBuf byteBuf = Unpooled.buffer(3+data0xEE0x0D.getLength()+1);
		byteBuf.writeByte(data0xEE0x0D.getHeader());
		byteBuf.writeByte(data0xEE0x0D.getCmd());
		byteBuf.writeByte(data0xEE0x0D.getLength());
		
		byteBuf.writeBytes(Tool.hexStringtoBytes(data0xEE0x0D.getLockId()));   		
		byteBuf.writeByte(data0xEE0x0D.getStatus());
		
		byte[] bytes=new byte[3+data0xEE0x0D.getLength()];
		byteBuf.getBytes(0, bytes);
		int calCrc=CRC8.crc8(bytes, bytes.length);
		data0xEE0x0D.setCrc(calCrc);
		byteBuf.writeByte(data0xEE0x0D.getCrc());
		ctx.writeAndFlush(byteBuf); 
		Thread.sleep(1000);
        ctx.close();
      
		
	}

}
