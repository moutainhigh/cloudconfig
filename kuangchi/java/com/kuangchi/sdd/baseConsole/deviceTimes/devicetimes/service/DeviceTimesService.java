package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.TimeData;
import com.sun.mail.iap.ConnectionException;


/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-10 下午5:12:55
 * @功能描述: 时段管理模块-业务类
 */
public interface DeviceTimesService {
	
    
	boolean addDeviceTimes(DeviceTimes times, String loginUser);// 新增时段信息 
	
	boolean copeAddDeviceTimes(DeviceTimes times, String loginUser);// 复制时新增时段信息
	
	// 修改时段信息
	boolean modifyTimes(DeviceTimes times, String loginUser);
    
    // 根据条件查询时段信息 (分页)
    List<DeviceTimes> getTimesByParamPage(DeviceTimes times,int page, int size);
   
    // 根据条件查询时段信息总数 
    int getTimesByParamCount(DeviceTimes times);
    
    // 时段下发
    void issuedTime(String deviceNums) throws Exception;
    
    /**根据编号查询设备mac和块号，有数据库追加*/
    List<DeviceTimes> getDeviceMacByNums(String deviceNums);
    
    /**根据设备编号查询时段表数据除了时段编号为0*/
    List<DeviceTimes> getDeviceTimesByDeviceNum(String deviceNums);//保存时段
    
    boolean getDeviceTimesByDeviceNums(String deviceNums,
    		String device)throws ConnectionException,RuntimeException,Exception;//保存并下发时段
    /** 复制或之前先删除时段信息*/
   	boolean deleteDeviceTimesByDeviceNum(String deviceNums);
   	
   	public List<DeviceInfo> getDeviceByNums(String device_num);//查询设备信息
   	/**判断服务器是否连接或设备是否在线  */ 
   	public String isNormalState(String deviceNums);
   	
   	/**
   	 * 删除时段
   	 * @author minting.he
   	 * @param ids
   	 * @param times_num
   	 * @param device_num
   	 * @param login_user
   	 * @return
   	 */
   	public boolean delTimesById(String ids, String device_num, String login_user);
   	
   	/**
   	 * 根据编号查询设备mac和块号，无数据库追加
   	 * @author minting.he
   	 * @param deviceNums
   	 * @return
   	 */
   	public List<DeviceTimes> getDeviceMacByNums2(String deviceNums);
   	
   	/**
   	 * 删除前判断时段是否在使用
   	 * @author minting.he
   	 * @param map
   	 * @return
   	 */
   	public Integer ifUsedTimes(Map<String, Object> map);
   	
}
	