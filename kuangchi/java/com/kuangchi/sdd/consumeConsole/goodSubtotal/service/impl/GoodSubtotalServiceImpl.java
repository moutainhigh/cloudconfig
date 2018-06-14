package com.kuangchi.sdd.consumeConsole.goodSubtotal.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.dao.IGoodSubtotalDao;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.model.GoodSubtotal;
import com.kuangchi.sdd.consumeConsole.goodSubtotal.service.IGoodSubtotalService;

/**
 * 商品清算ServiceImpl
 * @author minting.he
 *
 */
@Transactional
@Service("goodSubtotalService")
public class GoodSubtotalServiceImpl implements IGoodSubtotalService {

	@Resource(name = "goodSubtotalDao")
	private IGoodSubtotalDao goodSubtotalDao;

	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid<GoodSubtotal> getGoodSubtotalByParam(String vendor_name, String start_date, String end_date, Integer skip, Integer rows) {
		try{
			Grid<GoodSubtotal> grid = new Grid<GoodSubtotal>();
			List<GoodSubtotal> resultList = goodSubtotalDao.getSubtotalByParam(vendor_name, start_date, end_date, skip, rows);
			grid.setRows(resultList);
			if(null != resultList){
				grid.setTotal(goodSubtotalDao.getSubtotalByParamCount(vendor_name, start_date, end_date));
			}else{
				grid.setTotal(0);
			}
			return grid;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<GoodSubtotal> exportAllGoodSubtotal() {
		try{
			return goodSubtotalDao.exportAllGoodSubtotal();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Map> getVendorSubInfo(){
		try{
			return goodSubtotalDao.getVendorSubInfo();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void vendorSub(String vendor_num, String previous_time, Date date){
		try{
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
			String now_time = dateFormat1.format(date); 
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vendor_num", vendor_num);
			map.put("previous_time", previous_time);
			map.put("now_time", now_time);
			List<Map> consumeList = goodSubtotalDao.getConsumeByVendor(map);	//商户的正常消费记录
			if(consumeList!=null && consumeList.size()!=0){	
				int count=0;	//交易笔数
				BigDecimal fee_balance = new BigDecimal(0);	//实际金额（折前）
				BigDecimal real_balance = new BigDecimal(0);	//实付金额（折后）
				for(Map<String, Object> consumeMap : consumeList){
					BigDecimal outbound = new BigDecimal(consumeMap.get("outbound").toString());
					if(consumeMap.get("good_price")!=null){	//商品模式
						BigDecimal good_price = new BigDecimal(consumeMap.get("good_price").toString());
						fee_balance = fee_balance.add(good_price);
						real_balance = real_balance.add(outbound);
						++count;
					}else if(consumeMap.get("type_good_price")!=null){	//商品类型模式
						BigDecimal good_type_price = new BigDecimal(consumeMap.get("type_good_price").toString());
						fee_balance = fee_balance.add(good_type_price);
						real_balance = real_balance.add(outbound);
						++count;
					}else {			//零钞模式
						fee_balance = fee_balance.add(outbound);
						real_balance = real_balance.add(outbound);
						++count;
					}
				}
				BigDecimal relief_balance = fee_balance.subtract(real_balance);	//减免金额
				BigDecimal discount = real_balance.divide(fee_balance,2);
				String sub_date = dateFormat2.format(date); 
				
				GoodSubtotal total = new GoodSubtotal();
				total.setVendor_num(vendor_num.toString());
				total.setSub_date(sub_date);
				total.setTrading_volume(String.valueOf(count));
				total.setDiscount_balance(discount.toString());
				total.setReal_balance(real_balance);
				total.setFee_balance(fee_balance);
				total.setRelief_balance(relief_balance);
				goodSubtotalDao.insertVendorSub(total);		//新增商户清算统计
			}
			goodSubtotalDao.updateVendorPreTime(map);	//更新商户清算时间
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean manualSubVendor(String login_user){
		Map<String, String> log = new HashMap<String, String>();
		try{
			List<Map> vendorList = getVendorSubInfo();	//所有商户信息
			if(vendorList!=null && vendorList.size()!=0){	
				Date d2 = new Date(); 
				for(Map<String, Object> infoMap : vendorList){	
					String vendor_num =  infoMap.get("vendor_num").toString();
					String previous_time = infoMap.get("previous_time").toString();
					vendorSub(vendor_num, previous_time, d2);
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		}finally{
			log.put("V_OP_NAME", "商户信息维护");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "商户清算");
			logDao.addLog(log);
		}
	}

}
