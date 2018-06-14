package com.kuangchi.sdd.consum.handler.serverHandler.ping;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.consum.bean.CardNumPack;
import com.kuangchi.sdd.consum.bean.ConsumeRecordPack;
import com.kuangchi.sdd.consum.bean.ParameterSetResponse;
import com.kuangchi.sdd.consum.bean.ParameterUpResponse;
import com.kuangchi.sdd.consum.bean.PersonCardPackUp;
import com.kuangchi.sdd.consum.bean.PingUp;
import com.kuangchi.sdd.consum.bean.SinglePersonCardUp;
import com.kuangchi.sdd.consum.util.Util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
/**
 * 消费协议解析类,注意要解决TCP并包和粘包问题
 * 
 * */
public class PingDecoder extends ByteToMessageDecoder {
          
	 private static final Logger LOG = Logger.getLogger(PingDecoder.class);
     
	
	 private static char[] HexCode = {'0', '1', '2', '3', '4', '5', '6', '7',  
         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};  
	 public static String byte2HexString(byte b) {  
	        StringBuffer buffer = new StringBuffer();  
	        buffer.append(HexCode[(b >>> 4) & 0x0f]);  
	        buffer.append(HexCode[b & 0x0f]);  
	        return buffer.toString();  
	    }  
	 
	 
	 public static void printHex(byte b){
		
			System.out.print(byte2HexString(b));
	
		
	 }
	
	
	
	//ByteBuf in=Unpooled.buffer(65535);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {

		if (in.readableBytes() == 0) {
			return;
		}

//		LOG.debug("本次上报字节" + buf.readableBytes());
//		System.out.print("上报的字节为：");
//		for (int i = 0; i < buf.readableBytes(); i++) {
//			printHex(buf.getByte(i));
//		}
//		LOG.debug();
		
		
		//写到缓存中
//		in.writeBytes(buf);
		
		while (in.readableBytes() > 0) {
			
			int header=in.getByte(0);
			
			
			LOG.debug("--------------解析开始----------------");
			LOG.debug("缓冲字节剩余数:" + in.readableBytes());
			LOG.debug("剩余的字节为：");
			for (int i = 0; i < in.readableBytes(); i++) {
				printHex(in.getByte(i));
			}
			LOG.debug("");
			
			LOG.debug("第一个字节header为:" + header);
			if (header == 0x03) { // 终端心跳上传（5个字节）:
				if (in.readableBytes() < 5) {
					return; // (3)
				}

				byte[] bytes = new byte[5];
				in.getBytes(0, bytes);

				int crcCal = Util.getXor(3, bytes);
				int receiveCRC = (bytes[3]) & 0xff;

				PingUp pingUp = BeanUtil.generatePingUp(in);
				if (receiveCRC == crcCal) {
						out.add(pingUp); // (4)
				}
				LOG.debug("读取03 ");
			} else if (header == 0x2d || header == 0x01) { // 卡号包（终端发往软件，9个字节）
				if (in.readableBytes() < 9) {
					return; // (3)
				}
				byte[] bytes = new byte[9];
				in.getBytes(0, bytes);

				if (header == 0x2d) {
					int crcCal = Util.getXor(7, bytes);
					int receiveCRC = (bytes[7]) & 0xff;	
					CardNumPack cardNumPack = BeanUtil.generateCardNumPack(in);
					if (receiveCRC == crcCal) {
						out.add(cardNumPack); // (4)
					}
					LOG.debug("读取2d   01 ");

				} else {
					if (in.readableBytes() < 9) {
						return;
					}
					 byte[] bys=new byte[9];
					 in.readBytes(bys);//业务待实现
				}

			} else if (header == 0x02) { // 记录包（终端发往软件，40个字节）
				if (in.readableBytes() < 26) {
					return; // (3)
				}
				byte[] bytes = new byte[26];
				in.getBytes(0, bytes);

				int crcCal = Util.getXor(24, bytes);
				int receiveCRC = bytes[24] & 0xff;
				
				ConsumeRecordPack consumeRecordPack = BeanUtil
						.generateConsumeRecordPack(in);
				if (receiveCRC == crcCal) {
						out.add(consumeRecordPack); // (4)
				}
				LOG.debug("读取02");

			} else if (header == 0x0a) { // 读取参数设置应答包（终端发往软件，125字节）

				if (in.readableBytes() < 125) {
					return; // (3)
				}
				byte[] bytes = new byte[125];
				in.getBytes(0, bytes);

				//int crcCal = Util.getCrc16(123, bytes);   //这个地方获取上来的参数没有校验和统一为0000,这个可能设备上报时本身就没有计算校验和
				int crcCal =0x00;

				// 读取校验和 2字节
				int crcL = bytes[123];// 低位
				int crcH = bytes[124];// 高位
				int receiveCRC = ((crcH & 0xff) << 8) | (crcL & 0xff);
					
				ParameterUpResponse parameterUpResponse = BeanUtil
						.generateParameterUpResponse(in);
				if (receiveCRC == crcCal) {
					out.add(parameterUpResponse); // (4)
				}
				
				
				/**
				 * 有一点就是一定要注意的，当消费机在消费或下发名单时，我们去获取设备参数时，获取的字节数会出现错位,所以消费机设置参数只能在空闲时操作 
				 * 
				 * 
				 * **/
				
//				if (receiveCRC == crcCal) {
//					ParameterUpResponse parameterUpResponse = BeanUtil
//							.generateParameterUpResponse(in);
//					out.add(parameterUpResponse); // (4)
//				}
//				
//				else{//说明上报失败   在设备忙的时候，获取设备参数时设备就会上报错误的包，这个地方硬件可能有问题，一旦得到了错误的包，就要马上把它清掉
//					byte[] discardBytes=new byte[37];
//					in.readBytes(discardBytes);
//					LOG.debug("清除设备参数异常数据数为:" + discardBytes.length);
//					System.out.print("清除设备参数异常数的字节为：");
//					for (int i = 0; i < discardBytes.length; i++) {
//						printHex(discardBytes[i]);
//					}
//				}
				LOG.debug("读取0a ");

			} else if (header == 0x09) { // 参数设置后的返回包
				if (in.readableBytes() < 3) {
					return; // (3)
				}
				byte[] bytes = new byte[3];
				in.getBytes(0, bytes);

				ParameterSetResponse parameterSetResponse = BeanUtil
						.generateParameterSetResponse(in);
                
				out.add(parameterSetResponse); // (4) 

				LOG.debug("读取09 ");

			} else if (header == 0x04) { // 申请名单包（上行，7个字节）
				if (in.readableBytes() < 7) {
					return; // (3)
				}
				byte[] bytes = new byte[7];
				in.getBytes(0, bytes);

				int crcCal = Util.getXor(5, bytes);
				int receiveCRC = bytes[5] & 0xff;

				PersonCardPackUp personCardPackUp = BeanUtil
						.generatePersonCardUp(in);
				if (receiveCRC == crcCal) {
					out.add(personCardPackUp); // (4)
			     }
				LOG.debug("读取04 ");

			} else if (header == 0x06) {
				if (in.readableBytes() < 37) {
					return;
				}
				byte[] bytes = new byte[37];
				in.getBytes(0, bytes);
				byte[] calBytes = new byte[36];
				calBytes[0] = 0x07;
				for (int i = 0; i < 35; i++) {
					calBytes[i + 1] = bytes[i];
				}
				int crcCal = Util.getCrc16(36, calBytes);

				// 读取校验和 2字节
				int crcL = bytes[35];// 低位
				int crcH = bytes[36];// 高位
				int receiveCRC = ((crcH & 0xff) << 8) | (crcL & 0xff);
				
					 
				
				SinglePersonCardUp singlePersonCardUp = BeanUtil
						.generateSinglePersonCardUp(in);
				if (receiveCRC == crcCal) {
					out.add(singlePersonCardUp);
				}
				LOG.debug("读取06 ");
			}

			else if (header == 0x61) {// ====重连接标识
				if (in.readableBytes() < 12) {
					return;
				}
				LOG.debug("重连接标志： at+reconn=1"); //at+reconn=1 为重连接标识，这个也会当作字节流报上来，所以要处理

				byte[] bytes = new byte[12];
				in.getBytes(0, bytes);
				int calCRC = Util.getCrc16(12, bytes);
//				if (calCRC == 27106) { // 重连接字节是固定的字符"at+reconn=1"
//					return;
//				}
				byte[] bs=new byte[12];
				in.readBytes(bs);
				LOG.debug("读取重连接：  at+reconn=1 ");
			}else if(header==-1){
				LOG.debug("清除FF异常数据："+in.readByte());	//这个地方会不定时上报一个FF字节，遇到这个字节应该把它单做解析
			}else { // 如果不是需要的命令头,直接清掉byteBuf，说明字节流已经错乱，让其自动恢复到正确的字节流顺序
				byte[] bytes=new byte[in.readableBytes()];
				in.readBytes(bytes);
				LOG.debug("清除异常数据数为:" + bytes.length);
				System.out.print("清除异常数的字节为：");
				for (int i = 0; i < bytes.length; i++) {
					printHex(bytes[i]);
				}
			}
             //丢弃掉已经读的字节
			in.discardReadBytes();
            LOG.debug("--------------解析结束----------------");
		}
	}

}
