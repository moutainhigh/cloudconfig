package com.kuangchi.sdd.zigbee.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import com.kuangchi.sdd.zigbee.handler.DataWritePolicy;
import com.kuangchi.sdd.zigbee.pool.ObjectCountDownLatchPool;
import com.kuangchi.sdd.zigbee.pool.ObjectSephamorePool;
import com.kuangchi.sdd.zigbee.pool.ResultPool;
import com.kuangchi.sdd.zigbee.util.CRC8;
/**
 * 上报锁ID + 房间号
 * */
public class DataWritePolicyEE01 implements DataWritePolicy {

	@Override
	public void writeData(ChannelHandlerContext ctx, Data data) throws Exception {
		
        Data0xEE0x01 data0xEE0x01=(Data0xEE0x01) data;
        ByteBuf byteBuf = Unpooled.buffer(3 + data0xEE0x01.getLength() + 1);
        byteBuf.writeByte(data0xEE0x01.getHeader());
        byteBuf.writeByte(data0xEE0x01.getCmd());
        byteBuf.writeByte(data0xEE0x01.getLength());
        
        
        byte[] bytes=new byte[3+data0xEE0x01.getLength()];
        byteBuf.getBytes(0, bytes);
        int calCrc= CRC8.crc8(bytes, bytes.length);
        data0xEE0x01.setCrc(calCrc);
        byteBuf.writeByte(data0xEE0x01.getCrc());
        
        
        
		String headerCmdLockId="ee"+data0xEE0x01.getCmd();  //某一个设备某一个命令的标志位
			try {
				ResultPool.clear(headerCmdLockId);//先清除某一个设备某一个命令的历史结果
				CountDownLatch countDownLatch=new CountDownLatch(1);//创建一个闭锁，用来等待服务端结果返回
		        ObjectCountDownLatchPool.saveCountDownLatchPool(headerCmdLockId, countDownLatch);
		        ctx.writeAndFlush(byteBuf);
		        countDownLatch.await(timeout, TimeUnit.SECONDS); //最多等待3秒钟。
		        ObjectCountDownLatchPool.release(headerCmdLockId);  
			} catch (Exception e) { 
				 e.printStackTrace();
			}finally{
				  ctx.close();
			} 
	}
    
}
