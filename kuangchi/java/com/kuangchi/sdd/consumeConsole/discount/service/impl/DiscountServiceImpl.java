package com.kuangchi.sdd.consumeConsole.discount.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.discount.dao.IDiscountDao;
import com.kuangchi.sdd.consumeConsole.discount.model.Discount;
import com.kuangchi.sdd.consumeConsole.discount.service.IDiscountService;
@Transactional
@Service("DiscountServiceImpl")
public class DiscountServiceImpl  implements IDiscountService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "DiscountDaoImpl")
	private IDiscountDao discountDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	
	/**
	 * 新增折扣信息
	 */
	@Override
	public Boolean insertDiscount(Discount discount_info) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "折扣信息维护");
        log.put("V_OP_FUNCTION", "新增");
        log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=discountDao.insertDiscount(discount_info);
    		if(obj==true){
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
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return false;
        }
	}

	/**
	 * 修改折扣信息
	 */
	@Override
	public Boolean updateDiscount(Discount discount_info) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "折扣信息维护");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=discountDao.updateDiscount(discount_info);
    		if(obj==true){
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
	
	/**
	 * 查询所有折扣信息
	 */
	@Override
	public Grid selectAllDiscounts(Discount discount_info, String page,
			String size) {
		Integer count=discountDao.getAllDiscountCount(discount_info);
		List<Discount> discount=discountDao.selectAllDiscounts(discount_info, page, size);
		Date date=new Date();
		SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			for (Discount discount2 : discount) {
				try {
					discount2.setDiscount_rate(discount2.getDiscount_rate()*100);
					Date startDate=matter.parse(discount2.getEnd_date());
					if(date.getTime()<startDate.getTime()){
						discount2.setDiscount_state(0);
					}else{
						discount2.setDiscount_state(1);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(discount);
		return grid;
		
	}
	
	/**
	 * 根据id查询折扣信息
	 */
	@Override
	public List<Discount> selectDiscountById(Integer id) {
		List<Discount> discount=discountDao.selectDiscountById(id);
		return discount;
	}
	
	/**
	 * 删除折扣信息
	 */
	@Override
	public Integer deleteDiscount(String id,String create_user) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "折扣信息维护");
        log.put("V_OP_FUNCTION", "删除");
        log.put("V_OP_ID", create_user);
        try{
        	Integer obj=discountDao.deleteDiscount(id);
    		if(obj==1){
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除成功");
    	        logDao.addLog(log);
    	    return 1;    
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "删除失败");
    			logDao.addLog(log);
    			return 0;
    		}
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "删除异常");
    		logDao.addLog(log);
    		return 0;
        }
	}
	
	/**
	 * 根据折扣代码查询折扣信息
	 */
	@Override
	public List<Discount> selectDiscountByNum(Discount discount_info) {
		List<Discount> discount=discountDao.selectDiscountByNum(discount_info);
		return discount;
	}

	@Override
	public Integer selectGoodByNumCount(String discount_num) {
		Integer count=discountDao.selectGoodByNumCount(discount_num);
		return count;
	}

	@Override
	public Integer selectGoodTypeByNumCount(String discount_num) {
		Integer count=discountDao.selectGoodTypeByNumCount(discount_num);
		return count;
	}

	
	
}
