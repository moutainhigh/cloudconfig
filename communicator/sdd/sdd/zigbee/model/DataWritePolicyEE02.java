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
import com.kuangchi.sdd.zigbee.util.Tool;
/**
 * 批量下载光子卡的开锁权限
 * */
public class DataWritePolicyEE02 implements DataWritePolicy  {

	@Override
	public void writeData(ChannelHandlerContext ctx, Data data)
			throws Exception {
  	     Data0xEE0x02 data0xEE0x02=(Data0xEE0x02) data;

  	    
  		ByteBuf byteBuf = Unpooled.buffer(3+data0xEE0x02.getLength()+1);
  		byteBuf.writeByte(data0xEE0x02.getHeader());
    		byteBuf.writeByte(data0xEE0x02.getCmd());
    		byteBuf.writeByte(data0xEE0x02.getLength());
    			
    		byteBuf.writeBytes(Tool.hexStringtoBytes(data0xEE0x02.getLockId()));  
  		
     		byteBuf.writeInt(Long.valueOf(data0xEE0x02.getPhotonId()).intValue());
    		
    		byteBuf.writeInt(Long.valueOf(data0xEE0x02.getStartTime()).intValue());
    		byteBuf.writeInt(Long.valueOf(data0xEE0x02.getEndTime()).intValue());
    		byteBuf.writeByte(data0xEE0x02.getTransportFlag());
          
    		
    		byte[] bytes=new byte[3+data0xEE0x02.getLength()];
    		byteBuf.getBytes(0, bytes);
    		int calCrc=CRC8.crc8(bytes, bytes.length);
    		data0xEE0x02.setCrc(calCrc);
    		byteBuf.writeByte(data0xEE0x02.getCrc());
    		
    		
    		
    		 String headerCmdLockId="ee"+data0xEE0x02.getCmd()+data0xEE0x02.getLockId();
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
