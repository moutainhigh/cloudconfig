package com.kuangchi.sdd.consumeConsole.vendorType.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.vendorType.dao.IVendorTypeDao;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
import com.kuangchi.sdd.consumeConsole.vendorType.service.IVendorTypeService;
@Service("vendorTypeServiceImpl")
public class VendorTypeServiceImpl implements IVendorTypeService {
	@Resource(name="vendorTypeDaoImpl")
	private IVendorTypeDao vendorTypeDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid getVendorTypeInfoByParam(Map<String, String> map) {
		List<VendorType> vendorTypeList=vendorTypeDao.getVendorTypeInfoByParam(map);
		Integer count=vendorTypeDao.getVendorTypeInfoCount(map);
		Grid grid=new Grid();
		grid.setRows(vendorTypeList);
		grid.setTotal(count);
		return grid;
	}
	@Override
	public VendorType selectVendortypeByNum(String vendor_type_num) {
		return vendorTypeDao.selectVendortypeByNum(vendor_type_num);
	}
	@Override
	public boolean insertNewVendortype(VendorType vendorType,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户类型维护");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorTypeDao.insertNewVendortype(vendorType);
			if(flag){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "新增成功");
				logDao.addLog(log);
				return true;
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "新增失败");
				logDao.addLog(log);
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "新增失败");
			logDao.addLog(log);
			return false;
		}
	}
	@Override
	public boolean updateVendorType(VendorType vendorType,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户类型维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorTypeDao.updateVendorType(vendorType);
			if(flag){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "修改成功");
				logDao.addLog(log);
				return true;
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "修改失败");
				logDao.addLog(log);
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "修改失败");
			logDao.addLog(log);
			return false;
		}
	}
	@Override
	public Integer selectVendorByNum(String vendor_type_num) {
		return vendorTypeDao.selectVendorByNum(vendor_type_num);
	}
	@Override
	public boolean delVendorType(String num,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户类型维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorTypeDao.delVendorType(num);
			if(flag){
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "删除成功");
				logDao.addLog(log);
				return true;
			}else{
				log.put("V_OP_TYPE", "业务");
				log.put("V_OP_MSG", "删除失败");
				logDao.addLog(log);
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			log.put("V_OP_MSG", "删除失败");
			logDao.addLog(log);
			return false;
		}
	}

}
