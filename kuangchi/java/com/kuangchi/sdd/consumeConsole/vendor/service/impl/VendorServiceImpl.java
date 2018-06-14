package com.kuangchi.sdd.consumeConsole.vendor.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.consumeConsole.vendor.dao.IVendorDao;
import com.kuangchi.sdd.consumeConsole.vendor.service.IVendorService;
import com.kuangchi.sdd.consumeConsole.vendorType.model.VendorType;
@Service("vendorServiceImpl")
public class VendorServiceImpl implements IVendorService {
	@Resource(name = "vendorDaoImpl")
	private IVendorDao vendorDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public List<VendorType> getVendorType() {
		return vendorDao.getVendorType();
	}

	@Override
	public Grid getVendorInfoByParam(Vendor vendor, String page, String rows) {
		List<Vendor> vendorList=vendorDao.getVendorInfoByParam(vendor,page,rows);
		Integer count=vendorDao.getVendorInfoCount(vendor);
		Grid grid=new Grid();
		grid.setRows(vendorList);
		grid.setTotal(count);
		return grid;
	}

	@Override
	public List<Vendor> getAllVendor() {
		return vendorDao.getAllVendor();
	}

	@Override
	public Vendor selectVendorByNum(String vendor_num) {
		return vendorDao.selectVendorByNum(vendor_num);
	}

	@Override
	public boolean insertNewVendor(Vendor vendor,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户信息维护");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorDao.insertNewVendor(vendor);
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
	public boolean updateVendor(Vendor vendor,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户信息维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorDao.updateVendor(vendor);
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
	public Vendor selectVendorInfoByNum(String vendor_num) {
		return vendorDao.selectVendorInfoByNum(vendor_num);
	}

	@Override
	public Integer selectDeviceByVendorNum(String vendor_num) {
		return vendorDao.selectDeviceByVendorNum(vendor_num);
	}

	@Override
	public boolean delVendor(String vendor_num,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商户信息维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=vendorDao.delVendor(vendor_num);
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
