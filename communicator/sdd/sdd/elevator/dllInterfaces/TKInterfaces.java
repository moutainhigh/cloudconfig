package com.kuangchi.sdd.elevator.dllInterfaces;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;
/**
 * 
 * 梯控调用DLL的接口类
 * 
 * 
 * **/
public interface TKInterfaces  extends Library {
//	返 回 值: 通信线程数 通信线程数 通信线程数 ,返回 0表示所有通信 表示所有通信 表示所有通信 线程已关闭 线程已关闭 线程已关闭 线程已关闭 线程已关闭 ,其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在其他表示当前有通 信线程在信;
//	函数功能 : 获取通信线程计数 获取通信线程计数 获取通信线程计数 获取通信线程计数 ;
//	函数 参: 无;
//	备 注: 无;
	   public   Integer KKTK_GetCommThreadCount();
//	   返 回 值: 无;
//	   函数功能: 停止所有通信线程;
//	   函数参数: 无;	      
	   public void KKTK_StopAllComm();
	   
//	   返 回 值: 参见返回代码;
//	   函数功能: 获取通信结果;
//	   函数参数: 
//	   　参数1> nCommID 为通信时dll返回的通信ID;
//	   　参数2> strResult  返回的结果字符串;
//	   备    注: 无;
	   public  Integer KKTK_GetCommResult(Integer nCommID,byte[] strResult);
	   
	   
//	   返 回 值:参见返回代码, 一般返回CommID;
//	   函数功能:更新控制器密码;
//	   函数参数:
//	   　参数1> strControlName 为控制器名称;
//	   　参数2> strNewPassword控制器密码;
//	   备    注: 无;	   
	   public Integer KKTK_UpdatePassword(String strControlName ,String strNewPassword);  
	   
	   
//	   返 回 值: 参见返回代码, 一般返回CommID;
//	   函数功能: 初始化控制器;
//	   函数参数: 
//	     参数1> strControlName 为控制器名称;
//	   备    注:初始化控制器后恢复出场设置;  
	   public Integer  KKTK_SystemInit(String  strControlName);
	   
	   
//	   返 回 值: 参见返回代码,一般返回CommID;
//	   函数功能: 发送时钟;
//	   函数参数: 
//	   　　参数1> strControlName 为控制器名称;
//	   备    注: 无;
	   public Integer KKTK_SendClock(String strControlName );
	   
	
//	   返 回 值: 参见返回代码，一般返回CommID;
//	   函数功能:  发送电梯开放时区;
//	   函数参数: 
//	   　　参数1> strControlName->控制器名称；
//	       参数2> strEtoz->开放时区时间;
//	   备    注:
//	   　　参数2举例如下:
//	   　　"05|02|00|00|23|59|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|00|"
//	   　　05表示周五，其他取值说明:
//	   　　   1-7表示星期一到星期天,
//	   　　　　8-表示节假日
//	   　　　　
//	   　　02表示时段个数, 时段个数<=5
//	   　　
//	   后面依次为5个时段(时段格式参考楼层开放时区)
	   public Integer KKTK_SendElevatorOpenTimezone(String strControlName,String strEtoz);
	   
	  
//	   返 回 值: 参见返回代码,一般返回CommID;
//	   函数功能: 发送发送硬件参数;
//	   函数参数: 
//	   　参数1> strControlName 为控制器名称;
//	   　参数2> strHardwareParam 为硬件参数；
//	   备    注: 
//	   　　参数2举例如下:
//	   　　　"3|7|8|30|0|0|20|0|0|0|0|"
//	   　　3表示层控继电器动作时间,单位为秒;7表示直达继电器动作时间,单位为秒;8->表示对讲继电器动作时间，单位为分钟，默认1分钟;30(16进制)->表示楼层总数(48层);其后三个字节 0, 0, 20 表示报警剩余次数;后边4个0依次表示上行、下行、开门、关门信号电平;
//	     其中报警剩余次数可忽略,给默认值即可. 
	   public Integer KKTK_SendHardwareParam(String strControlName,String strHardwareParam);
	
	   
//	   返 回 值: 参见返回代码,一般返回CommID;
//	   函数功能: 发送配置表;
//	   函数参数: 
//	   　参数1>  strControlName->控制器名;
//	   　参数2>  nType->发送类型;
//	   　参数3>  strCtb ->所有楼层;
//	   备    注:
//	   　1> 参数2详细说明: 0-表示配置楼层表;1->表示配置对讲开放楼层表;
//	   　2> 参数3详细说明: 
//	   　"48|47|46|45|44|43|42|41|40|39|38|37|36|35|34|33|32|31|30|29|28|27|26|25|24|23|22|21|20|19|18|17|16|15|14|13|12|11|10|09|08|07|06|05|04|03|02|01|"
//	   代表48层楼, 注意: 低楼层在前.
	  public Integer KKTK_SendCongfigTable(String strControlName,Integer nType,String strCtb);
	   
	  
//	  返 回 值: 参见返回代码,一般返回CommID;
//	  函数功能: 发送节假日表;
//	  函数参数: 
//	  　参数1> strControlName->控制器名;
//	  　参数2> strHtb ->节假日;
//	  备    注: 
//	  　1> 参数2详细说明: 
//	  　"48|47|46|45|44|43|42|41|40|39|38|37|36|35|34|33|32|31|30|29|28|27|26|25|24|23|22|21|20|19|18|17|16|15|14|13|12|11|10|9|8|7|6|5|4|3|"
//	  　每个字节的每一位代表一天，计46*8->368天;位置1表示当天为节假日;
//	  注意: 从第一个字节第一个bit开始算起.
	  public Integer  KKTK_SendHolidayTable(String strControlName,String strHtb );
	  
	  
//	  返 回 值:参见返回代码;
//	  函数功能: 发送楼层开放时区;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	    参数2> strFotz ->楼层开放时区时间;
//	  备    注: 
//	  　参数2详细说明:
//	  　　“06|00|00|23|59|00|00|00|00|00|00|23|59|00|00|00|00|”
//	  　　 06表示楼层，剩余表示四个时段:
//	  　　 00:00 ->23:59
//	  　　 00:00 ->00:00
//	  　　 00:00 ->23:59
//	     00:00 ->00:00
	  public Integer KKTK_SendFloorOpenTimezone(String strControlName,String strFotz);
	  
	  
	  
	  
	  
//	  返 回 值:参见返回代码;
//	  函数功能: 发送黑白名单;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	    参数2> ntype ->发送类型;
//	  　参数3> 发送芯片号;
//	  备    注: 
//	  　1> 参数2详细说明: 0-发送黑名单,1-发送白名单;
//	  　2> 参数3举例如下:
//	  　"1|2|2842939630|2837002542|"
//	  1表示下发模式;2表示黑名单条数,2条则后面跟两个芯片号(芯片号为十进制格式字符串形式);	  
	  public Integer KKTK_SendBlacklist(String strControlName,Integer ntype, String strBlacklist,boolean bCmd );//
	  
	  
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收时钟;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  备    注: 无;
	  public Integer KKTK_RecvClock(String strControlName);
	  
	  
	
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收参数;
//	  函数参数: 
//	  　参数1>  strControlName->控制器名称;
//	  　参数2>  nType >接收类型;
//	  备    注: 
//	  　1> 参数2取值详细说明: 
//	  　    0x34->报警剩余次数.
//	  　    0x35->电梯上行下行参数.
//	  　    0x36->楼层总数.
//	  　    0x37->层控继电器动作时间,
//	  　    0x38->直达继电器动作时间,
//	  　    0x39->对讲继电器动作时间;
//	  　
//	  　    当参数2取值为0x35时，返回值表示形式:    0|0|0|0|
//	  　    含义依次为: 上行信号,下行信号,开门信号，关门信号
//	  　
//	  　    当参数2取值为0x34时，返回值表示形式: 12
//	      参数2取值为0x35时比较特殊，其余与参数2取值为0x34时类似.
	  public Integer KKTK_RecvParam(String strControlName, Integer nType);
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收记录;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  　参数2> recvBuf事件描述,格式如下.
//	  　
//	  备    注: 每次读取一条记录;
	  public Integer KKTK_RecvEvent(String strControlName,byte[] recvBuf );
	  
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收控制器版本;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  备    注: 无;
	  public Integer KKTK_RecvVer(String strControlName);
	  
	  
	  
	  
	  
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 发送控制板通信参数;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  　参数2> strComm ->通信参数;
//	  备    注:
//	  　1> 参数2详细说明:
//	  　"1||192.168.3.12|6000|255.255.255.0|192.168.3.251|030400005182|"
//	  1表示485地址, 192.168.3.12表示IP，6000为端口号，255.255.255.0表示子网掩码，192.168.3.251代表网关，030400005182表示序列号;
	  public Integer KKTK_SendControlCommParam(String strControlName,String strComm);//
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收控制板通信参数;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  备    注: 无;
	  public Integer KKTK_RecvControlCommParam(String strControlName);
	  
	  
	  
	  
//	  返 回 值:参见返回代码;
//	  函数功能: 接收控制板配置表
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  　参数2> nType ->接收类型;
//	  备    注: 
//	  1>  参数2详细说明: 0-读取配置楼层, 1-读取对讲开放楼层配置;
	  public Integer KKTK_RecvConfigTbale(String strControlName,Integer nType);
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能: 接收控制板状态;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  备    注: 无;
	  public Integer KKTK_RecvStatus(String strControlName);  //
	  
	  
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能:发送添加授权;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  　参数2> strAddParam 说明:
//	  　示例: 
//	  　　1|0|1|1|卡号|密码|卡类型|000|000|000|000|000|000|00|00|150304150808|
//	  　1| 包计数. 默认给1.
//	  　0| 是否需要下发清除指令 : 0-下发清除指令，1-不下发清除指令，默认给0即可.
//	  　1|  : 0-表示覆盖下载(在下载授权之前，先清除授权，再发送新授权数据),1-表示追加下载.
//	  　1|  : 人员数量, 根据实际发送人员数量.
//	  　卡号| : 人员卡号
//	  　     卡号要求:如果配置读头为26，则发送26 10位格式，如果配置为34，发送34 10位格式.
//	  　
//	  　密码| : 人员密码
//	  　卡类型| : 2-用户卡,3-接待卡,4-计次卡
//	  　001|002|003|004|005|006| : 楼层权限.
//	  　   上边的楼层权限表示含义如下:
//	  　   001 :表示使用一层权限.
//	  　   002: 表示使用10.
//	  　   003: 表示使用 17,18.
//	  　   004: 表示使用 25.
//	  　   005: 表示使用33,34.
//	  　   006: 表示使用 42,43
//	  　   楼层权限使用bit表示，每一个字节表示8个楼层，共计48层.
//	  　
//	  　00|00| : 余额次数(默认即可). 
//	  　150304150808| : 人员有效期.
//	  备    注: 无;
	  public Integer KKTK_SendAddID(String strControlName,String strAddParam);
	  
	  
//	  返 回 值: 参见返回代码;
//	  函数功能:发送删除授权;
//	  函数参数: 
//	  　参数1> strControlName->控制器名称;
//	  　参数2> strDelParam ->
//	  　       示例1:
//	  　       1|1|卡号1|
//	  　       1| 表示删除指定卡号
//	  　       1| 表示人员数量
//	  　       卡号1| 要删除的卡号
//	  　
//	  　       示例2:
//	  　　　　　0|0|
//	  　　　　　0| 表示删除所有
//	  　　　　　0| 删除所有时人员数量给0.
//	  　　　　　
//	  　　　　　示例3:
//	  　　　　　1|2|卡号1|卡号2
//	  　　　　　1| 表示删除指定的卡号
//	  　　　　　2| 表示删除两个人员
//	  　　　　　卡号1|卡号2| 待删除的两个人员
//	  备    注: 无;
	  public Integer KKTK_SendDelID(String strControlName,String strDelParam);
	  
//	  返回值: 参见返回码.
//	  功能: 设置电平(上行，下行，开门，关门).
//	  参数: 参数1: strControlName->控制器名称.
//	        参数2: level 电平.
//	         0|0|0|0| 
//	        0-表示低电平有效,1-表示高电平有效.
//	  备注:
	  public Integer KKTK_SendEletricLevel(String strControlName,String level );
	  
	  
	  
//	  返回值: 参见返回码.
//	  功能: 读取电平电平(上行，下行，开门，关门).
//	  参数: 参数1: strControlName->控制器名称.
//	  备注:
	  public Integer KKTK_RecvElectricLevel(String strControlName);
	  
	  

//	   返回值:参见返回码.
//	   功能: 搜索电梯设备.
//	   参数: szSearchResult(out)->搜索结果.  
//
//	   备注:
//	       分配空间>=1024.
	   public Integer KKTK_SearchControls(byte[] szSearchResult,int len); //
	   
	   
	
	  
	  
	 //=======================================================================  
//	   返回值 :参见返回码 .
//	   功能 : 查看动态库内部版本 查看动态库内部版本 .
//	   参数 :szVersionszVersionszVersionszVersionszVersionszVersionszVersion szVersion(out)(out)(out) ->版本号 .
//	   备注 :
//	   大于 10 个字节 .
	   public  Integer KKTK_Get_DLLVersion(byte[] version);
	   
//	   返回值: 参见返回码.
//	   功能: 修改控制器通信参数.
//	   示例:
//	   "2|4|192.168.3.178|6000|255.255.255.0|192.168.3.251|12.02.21.00.08.19|”
//	   2 表示旧的设备地址.
//	   4 表示新的设备地址.
//	   192.168.3.178: 设备IP.
//	   6000: 设备端口.
//	   255.255.255.0 : 掩码.
//	   192.168.3.251: 网关.
//	   12.02.21.00.08.19: MAC地址.(来源于搜索)
	   public Integer KKTK_UpdateControl(String szCtrlParam);
	   
	   
	   
	   
//	功能: 绑定网卡.
//	备注: 目前只支持IPV4.
	   public Integer KKTK_SetSearchIP(String szIp);
	   

}
