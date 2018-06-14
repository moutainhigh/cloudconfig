package com.kuangchi.sdd.consumeConsole.good.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.good.dao.IGoodDao;
import com.kuangchi.sdd.consumeConsole.good.model.Discount;
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.good.model.Vendor;
import com.kuangchi.sdd.consumeConsole.good.service.IGoodService;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;

@Service("goodServiceImpl")
public class GoodServiceImpl implements IGoodService {
	@Resource(name = "goodDaoImpl")
	private IGoodDao goodDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<GoodType> getGoodType() {
		return goodDao.getGoodType();
	}

	@Override
	public Grid getGoodInfoByParam(Map<String, String> map) {
		List<Good> goodList=goodDao.getGoodInfoByParam(map);
		Integer count=goodDao.getGoodInfoCount(map);
		Date date=new Date();
		SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for (Good good : goodList) {
				try {
					if(good.getEnd_date()==null||"".equals(good.getEnd_date())){
						good.setDiscount_state("2");
					}else{
						Date endDate=matter.parse(good.getEnd_date());
						if(date.getTime()<endDate.getTime()){
							good.setDiscount_state("0");
						}else{
							good.setDiscount_state("1");
						}
					}
					
				} catch (Exception e) {
				}
			}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(goodList);
		return grid;
	}

	@Override
	public List<Vendor> getVendornum() {
		return goodDao.getVendornum();
	}

	@Override
	public List<Discount> getDiscountnum() {
		return goodDao.getDiscountnum();
	}

	@Override
	public boolean insertNewGood(Good good,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品信息维护");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=goodDao.insertNewGood(good);
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
	public Good selectGoodByNum(String good_num) {
		return this.goodDao.selectGoodByNum(good_num);
	}

	@Override
	public boolean modifyGood(Good good,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品信息维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=goodDao.modifyGood(good);
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
	public boolean delGoods(String num,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品信息维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=goodDao.delGoods(num);
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

	@Override
	public Good selectGoodInfoByNum(String good_num) {
		return this.goodDao.selectGoodInfoByNum(good_num);
	}

	@Override
	public List<Good> getAllGood() {
		return this.goodDao.getAllGood();
	}

	public Good getGoodByNum(String good_num){
		return this.goodDao.getGoodByNum(good_num);
	}

	@Override
	public Integer selectDeviceByGoodNum(String good_num) {
		return goodDao.selectDeviceByGoodNum(good_num);
	}

	@Override
	public void insertPriceHistory(PriceHistoryModel priceHistoryModel) {
		goodDao.insertPriceHistory(priceHistoryModel);
	}

	@Override
	public Grid getPriceHistoryList(Map<String, Object> map) {
		List<PriceHistoryModel> priceHistoryList=goodDao.getPriceHistoryList(map);
		Integer count=goodDao.getPriceHistoryCount(map);
		
		Grid grid=new Grid();
		grid.setRows(priceHistoryList);
		grid.setTotal(count);
		return grid;
	}

	@Override
	public Discount selectAvailableTimeByNum(String discount_num) {
		return goodDao.selectAvailableTimeByNum(discount_num);
	}
	
	@Override
	public List<Map<String, Object>> getGoodByVendor(String vendor_num) throws Exception{
		try{
			return goodDao.getGoodByVendor(vendor_num);
		}catch(Exception e){
			throw e;
		}
	}
}
