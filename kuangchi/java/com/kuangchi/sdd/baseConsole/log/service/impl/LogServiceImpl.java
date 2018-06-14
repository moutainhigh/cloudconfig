package com.kuangchi.sdd.baseConsole.log.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.kuangchi.sdd.baseConsole.log.model.Log;
import com.kuangchi.sdd.baseConsole.log.service.LogService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EasyUiTable;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
@Service("LogServiceImpl")
public class LogServiceImpl implements LogService{
	
	@Resource(name ="LogDaoImpl")
	private  LogDao LogDao;
	
	@Override
	public void addLog(Map<String, String> parameter) {
		// TODO Auto-generated method stub
		LogDao.addLog(parameter);
	}
	
	@Override
	public String getLogList(Log log, int rows, int page) {
		Map<String,Object> map=new HashMap();
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		map.put("op_type", log.getOp_type());
		map.put("op_function", log.getOp_function());
		map.put("startDate", log.getStartDate());
		map.put("endDate", log.getEndDate());
		
		EasyUiTable easyUiTable = new EasyUiTable();
		easyUiTable.setTotal(LogDao.getListRecords(map));
		easyUiTable.setRows(LogDao.getLogListByParams(map));
		return new Gson().toJson(easyUiTable);
	}

	@Override
	public void backupLog(String absolutePath) {
		String fileName="日志"+DateUtil.getDateString(new Date(), "yyyyMMddHHmmss")+".xls";
		FileOutputStream fos=null;
		 List<Map<String, Object>> list=LogDao.getAllLogList();
		 List<String> columns=LogDao.getLogTableColumns();
		try{
			 String[] colTitles =new String[columns.size()];	
			 String[] cols =new String[columns.size()];	
			for(int i=0;i<columns.size();i++){
				colTitles[i]=columns.get(i);
				cols[i]=columns.get(i);
			}
		fos=new FileOutputStream(absolutePath+fileName);
        Workbook workbook = ExcelUtilSpecial.exportExcel("日志",colTitles, cols, list);
        workbook.write(fos);
        fos.flush();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=fos){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        for(Map map:list){
        	LogDao.deleteLogById(map);
        }  
		
	}

	@Override
	public List<Log> getOp_type() {
		return LogDao.getOp_type() ;
	}

	@Override
	public List<Log> getOp_function() {
		return LogDao.getOp_function();
	}

	@Override
	public List<Log> ExportLog(Log log) {
		return LogDao.exportLogList(log);
	}

	@Override
	public List<Log> getLogInterval(Map map){
		try{
			return LogDao.getLogInterval(map);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean delLogInterval(Map map, String login_user){
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = LogDao.delLogInterval(map);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "日志管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除备份日志");
			LogDao.addLog(log);
		}
	}
	
}
