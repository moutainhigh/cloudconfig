package com.xkd.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.UserAttendMeetingMapper;
import com.xkd.model.UserAttendMeeting;
import com.xkd.utils.PropertiesUtil;

@Service
public class UserAttendMeetingService {

	@Autowired
	private UserAttendMeetingMapper userAttendMeetingMapper;
	
	public UserAttendMeeting selectUserAttendMeetingByCompanyId(String companyId){
		
		UserAttendMeeting userAttendMeeting = userAttendMeetingMapper.selectUserAttendMeetingByCompanyId(companyId);
		
		return userAttendMeeting;
	}

	public Integer getTotoalByMeetingId(String meetingId, String content, String companyAdviserStr, String companyDirectorStr, String mgroupStr, String userTypeStr) {
		
		Integer total = userAttendMeetingMapper.getTotoalByMeetingId(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);
		
		return total;
	}

	public Integer getAttendedByMeetingId(String meetingId,String content) {
		
		Integer Attended = userAttendMeetingMapper.getAttendedByMeetingId(meetingId,content);
		
		return Attended;
	}

	public List<UserAttendMeeting> selectUserInfoByMeetingId(String meetingId, String notGroupStr, int currentPageInt, int pageSizeInt) {
		
		List<UserAttendMeeting> userAttendMeetings = userAttendMeetingMapper.selectUserInfoByMeetingId(meetingId,notGroupStr,currentPageInt,pageSizeInt);
		
		return userAttendMeetings;
	}

	public Integer updateMeetingUserContentById(UserAttendMeeting userAttendMeeting) {
		
		Integer num = userAttendMeetingMapper.updateMeetingUserContentById(userAttendMeeting);
		
		return num;
	}
	
	public Integer deleteMeetingUserByUserId(String userId){
		
		Integer num = userAttendMeetingMapper.deleteMeetingUserByUserId(userId);
		
		return num;
	}

	
	
	public Integer deleteMeetingUserByUserIdAndCompanyId(String userId,String companyId ){
		return userAttendMeetingMapper.deleteMeetingUserByUserIdAndCompanyId(userId, companyId);
	}
	
	public List<UserAttendMeeting> selectMeetingUserByParam(String meetingId, String companyIds, int currentPageInt, int pageSizeInt) {
		
		List<UserAttendMeeting> userAttendMeetings = userAttendMeetingMapper.selectMeetingUserByParam(meetingId,companyIds,currentPageInt,pageSizeInt);
		
		return userAttendMeetings;
	}

	public Integer saveUserMeeting(UserAttendMeeting userAttendMeeting) {
		
		Integer num = userAttendMeetingMapper.saveUserMeeting(userAttendMeeting);
		
		return num;
	}

	public List<UserAttendMeeting> selectMeetingUserById(String meetingId) {
		
		List<UserAttendMeeting> meetings = userAttendMeetingMapper.selectMeetingUserById(meetingId);
		
		return meetings;
	}

	public Integer getNotGroupCount(String meetingId, String notGroupStr) {
		
		Integer num = userAttendMeetingMapper.getNotGroupCount(meetingId,notGroupStr);
		
		return num;
	}

	
	public Integer getTotoalByMeetingIdAttend(String meetingId, String content, String companyAdviserStr, String companyDirectorStr, String mgroupStr, String userTypeStr) {
		
		Integer total = userAttendMeetingMapper.getTotoalByMeetingIdAttend(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);
		
		return total;
	}

	public List<HashMap<String, Object>> selectUserInfoMapByMeetingId(Map<String,Object> paramMap) {
			
		List<HashMap<String, Object>> maps = userAttendMeetingMapper.selectUserInfoMapByMeetingId(paramMap);
		
		return maps;
	}

	public Integer updateUserStarsById(String id, String star) {
		
		Integer num = userAttendMeetingMapper.updateUserStarsById(id,star);
		
		return num;
	}

	public Integer deleteMeetingUserByIds(String idString) {
		
		Integer num = userAttendMeetingMapper.deleteMeetingUserByIds(idString);
		
		return num;
	}

	public List<Map<String, Object>> selectExcluedCompanyUserMapsByMeetingId(Map<String,Object>  paramMap) {
		
		List<Map<String, Object>> maps = userAttendMeetingMapper.selectExcluedCompanyUserMapsByMeetingId(paramMap);
		
		return maps;
	}

	public Integer selectTotalExcluedCompanyUserMapsByMeetingId(Map<String,Object>  paramMap) {
		
		Integer num = userAttendMeetingMapper.selectTotalExcluedCompanyUserMapsByMeetingId(paramMap);
		
		return num;
	}

	public List<Map<String, Object>> selectUsersHotelByMeetingId(String userId,String meetingId, String content, int currentPageInt,int pageSizeInt) {
			
		
		List<Map<String, Object>> maps = userAttendMeetingMapper.selectUsersHotelByMeetingId(userId,meetingId,content,currentPageInt,pageSizeInt);
		
		return maps;
	}

	public Integer selectTotalUsersHotelByMeetingId(String meetingId, String content) {
		
		Integer num = userAttendMeetingMapper.selectTotalUsersHotelByMeetingId(meetingId,content);
		
		return num;
	}

	public List<Map<String, Object>> selectMeetingProjectsMapsByMeetingId(Map<String,Object> paramMap) {
		
		List<Map<String, Object>> maps = userAttendMeetingMapper.selectMeetingProjectsMapsByMeetingId(paramMap);
		
		return maps;
	}

	public Integer selectTotalMeetingProjectsMapsByMeetingId(Map<String,Object> paramMap) {
			
		
		Integer num = userAttendMeetingMapper.selectTotalMeetingProjectsMapsByMeetingId(paramMap);
				
		return num;
	}

	public List<Map<String, Object>> selectMeetingUserMapsByCompanyId(String companyId, String meetingId) {
		
		List<Map<String, Object>> maps = userAttendMeetingMapper.selectMeetingUserMapsByCompanyId(companyId,meetingId);
		
		return maps;
	}
	

	public List<UserAttendMeeting> selectMeetingUserByUserIdAndMeetingId(String meetingId, String userId) {
		
		List<UserAttendMeeting> userAttendMeetings = userAttendMeetingMapper.selectMeetingUserByUserIdAndMeetingId(meetingId,userId);
		
		return userAttendMeetings;
	}

	public Integer updateMeetingUserStatusById(String id, String ustatus, String status) {
		
		Integer num = userAttendMeetingMapper.updateMeetingUserStatusById(id,ustatus,status);
				
		return num;
		
	}

	public Integer updateMeetingUserStatus(String id, String status) {
		
		Integer num = userAttendMeetingMapper.updateMeetingUserStatus(id,status);
		
		return num;
	}

	public List<UserAttendMeeting> selectMeetingUserByIds(String idsString) {
		
		List<UserAttendMeeting> userAttendMeetings = userAttendMeetingMapper.selectMeetingUserByIds(idsString);
		
		return userAttendMeetings;
	}

	public Integer deleteMeetingUserAndStatus(String id, String status) {
		
		Integer num = userAttendMeetingMapper.deleteMeetingUserAndStatus(id,status);
		
		return num;
	}

	public Integer getTotoalByMeetingIdNeedCheck(String meetingId, String content, String companyAdviserStr, String companyDirectorStr, String mgroupStr, String userTypeStr) {
		
		Integer total = userAttendMeetingMapper.getTotoalByMeetingIdNeedCheck(meetingId,content,companyAdviserStr,companyDirectorStr,mgroupStr,userTypeStr);
		
		return total;
	}

	public Integer updateMeetingUserNeedByIdByInvitation(Map<String, Object> umap) {
		
		Integer total = userAttendMeetingMapper.updateMeetingUserNeedByIdByInvitation(umap);
		
		return total;
	}

	public List<HashMap<String, Object>> selectAdviserDirectorMaps(String meetingId) {
		
		List<HashMap<String, Object>> maps = userAttendMeetingMapper.selectAdviserDirectorMaps(meetingId);
		
		return maps;
	}

	public Integer deleteMeetingUserByUserIds(List<String> userIdList) {
		
		Integer total = userAttendMeetingMapper.deleteMeetingUserByUserIds(userIdList);
		
		return total;
	
	}

	//导出excel
		public String userHotelWriteExcel(List<Map<String, Object>> list,String userId) {

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("用户订房信息");
		XSSFCellStyle style_title = wb.createCellStyle();
		Font fontHeader=wb.createFont();
		fontHeader.setFontHeightInPoints((short)12);
		fontHeader.setBold(true);
		//字体名称
		fontHeader.setFontName("宋体");
		style_title.setFont(fontHeader);
		style_title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style_title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("index","序号");
		map.put("uname","客户");
		map.put("mobile","电话");
		map.put("companyName","行企业名称");
		map.put("hotelName","酒店名称");
		map.put("room","房型");
		map.put("startTime","入住日期");
		map.put("endTime","退房日期");
		map.put("dates","住宿天数");

		XSSFRow row = sheet.createRow((int) 0);
		row.setHeightInPoints(30);
		XSSFCell cell = null;
		int cell_0 = 0;
		for (String key : map.keySet()) {
			cell = row.createCell(cell_0);
			cell.setCellValue(map.get(key));
			cell.setCellStyle(style_title);
			cell_0 ++;
		}
		if(null != list && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i+1);
				cell_0 = 0;

				for (String key : map.keySet()) {
					cell = row.createCell(cell_0);
					if(key.equals("index")){
						cell.setCellValue((i+1));
					}else{
						Object value = list.get(i).get(key);
						if(null == value){
							cell.setCellValue("--");
						}else{
							cell.setCellValue(value+"");
						}
					}
					cell.setCellStyle(style);
					cell_0 ++;
				}
			}
		}

		String path =PropertiesUtil.FILE_UPLOAD_PATH+userId+"/客户订房信息.xlsx";
		FileOutputStream outputStream = null;
		try {
			// //给文件夹设置读写修改操作
			File  dir = new File(PropertiesUtil.FILE_UPLOAD_PATH+userId);
			String os = System.getProperty("os.name");
			if (!dir.exists()) {
				dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别
				if(!os.toLowerCase().startsWith("win")){
					Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
				}
			}

			outputStream = new FileOutputStream(path);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

			//给文件设置读写修改操作
			File targetFile = new File(path);

			if(targetFile.exists()){

				targetFile.setExecutable(true);//设置可执行权限
				targetFile.setReadable(true);//设置可读权限
				targetFile.setWritable(true);//设置可写权限
				String saveFilename = targetFile.getPath();
				if(!os.toLowerCase().startsWith("win")){

					Runtime.getRuntime().exec("chmod 777 " + saveFilename);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return null;
		}

		return PropertiesUtil.FILE_HTTP_PATH+userId+"/客户订房信息.xlsx";
	}

    public List<Map<String,Object>> selectMeetingUsersByIds(String ids) {
		return userAttendMeetingMapper.selectMeetingUsersByIds(ids);
    }

	public List<HashMap<String,Object>> selectProjectAdvisersMeetingId(String meetingId) {
		return userAttendMeetingMapper.selectProjectAdvisersMeetingId(meetingId);
	}

	public Integer updateUserMeeting(UserAttendMeeting userAttendMeeting) {
		return userAttendMeetingMapper.updateUserMeeting(userAttendMeeting);
	}
}
