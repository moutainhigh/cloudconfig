package com.kuangchi.sdd.consumeConsole.goodType.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.goodType.dao.IGoodTypeDao;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.goodType.model.PriceHistoryModel;
import com.kuangchi.sdd.consumeConsole.goodType.service.IGoodTypeService;
@Service("goodTypeServiceImpl")
public class GoodTypeServiceImpl implements IGoodTypeService {
	@Resource(name="goodTypeDaoImpl")
	private IGoodTypeDao goodTypeDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public Grid getGoodtypeInfoByParam(Map<String, String> map) {
		List<GoodType> goodTypeList= goodTypeDao.getGoodtypeInfoByParam(map);
		Integer count=goodTypeDao.getGoodTypeInfoCount(map);
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for(GoodType goodType:goodTypeList){
			try{
				if(goodType.getEnd_date()==null||"".equals(goodType.getEnd_date())){
					goodType.setDiscount_state("2");
				}else{
					Date endDate=format.parse(goodType.getEnd_date());
					if(date.getTime()<endDate.getTime()){
						goodType.setDiscount_state("0");
					}else{
						goodType.setDiscount_state("1");
					}
				}
				
			}catch(Exception e){}
		}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(goodTypeList);
		return grid;
	}

	@Override
	public GoodType selectTypeByNum(String type_num) {
		return this.goodTypeDao.selectTypeByNum(type_num);
	}

	@Override
	public boolean insertNewGoodtype(GoodType goodType,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品类别维护");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=goodTypeDao.insertNewGoodype(goodType);
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
	public boolean modifyGood(GoodType goodType,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品类别维护");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID",create_user);
		try{
			boolean flag=goodTypeDao.modifyGoodType(goodType);
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
	public boolean delGoodtypes(String num,String create_user) {
		Map<String,String> log=new HashMap<String,String>();
		log.put("V_OP_NAME", "商品类别维护");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		try{
			boolean flag=goodTypeDao.delGoodtypes(num);
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
	public Integer selectGoodByNum(String num) {
		return this.goodTypeDao.selectGoodByNum(num);
	}

	@Override
	public List<GoodType> getAllGoodType() {
		return goodTypeDao.getAllGoodType();
	}

	@Override
	public GoodType getGoodTypeByNum(String type_num) {
		return goodTypeDao.getGoodTypeByNum(type_num);
	}

	@Override
	public Integer selectDeviceByTypeNum(String typeNum) {
		return goodTypeDao.selectDeviceByTypeNum(typeNum);
	}

	@Override
	public void insertNewPriceHistory(PriceHistoryModel priceHistoryModel) {
		goodTypeDao.insertNewPriceHistory(priceHistoryModel);
		
	}

	@Override
	public Grid getPriceHistoryList(Map<String, Object> map) {
		List<PriceHistoryModel> priceHistoryList=goodTypeDao.getPriceHistoryList(map);
		Integer count=goodTypeDao.getPriceHistoryCount(map);
		
		Grid grid=new Grid();
		grid.setRows(priceHistoryList);
		grid.setTotal(count);
		return grid;
	}

	@Override
	public String validPrice(String type_num) {
		return goodTypeDao.validPrice(type_num);
	}
}
