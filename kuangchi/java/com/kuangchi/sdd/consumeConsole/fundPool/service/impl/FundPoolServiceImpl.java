package com.kuangchi.sdd.consumeConsole.fundPool.service.impl;



import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.fund.dao.IFundDao;
import com.kuangchi.sdd.consumeConsole.fund.model.FundModel;
import com.kuangchi.sdd.consumeConsole.fundPool.dao.IFundPoolDao;
import com.kuangchi.sdd.consumeConsole.fundPool.model.FundPoolModel;
import com.kuangchi.sdd.consumeConsole.fundPool.service.IFundPoolService;

@Transactional
@Service("fundPoolServiceImpl")
public class FundPoolServiceImpl implements IFundPoolService{
	
	@Resource(name = "fundPoolDaoImpl")
	private IFundPoolDao fundPoolDao;
	
	@Resource(name = "fundDaoImpl")
	private IFundDao fundDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<FundPoolModel> getFundPoolByParamPage(Map<String, Object> map) {
		return fundPoolDao.getFundPoolByParamPage(map);
	}

	@Override
	public List<FundPoolModel> getFundPoolByParam(Map<String, Object> map) {
		return fundPoolDao.getFundPoolByParam(map);
	}

	@Override
	public Integer getFundPoolByParamCount(Map<String, Object> map) {
		return fundPoolDao.getFundPoolByParamCount(map);
	}

	@Override
	public boolean addFundPool(FundPoolModel fundPoolModel, String loginUserName) {
		
		boolean result =  fundPoolDao.addFundPool(fundPoolModel);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "机构资金管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "新增机构资金池");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean freezeFundPool(String ids, String loginUserName) {
		
		boolean result = fundPoolDao.freezeFundPool(ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "机构资金管理");
		log.put("V_OP_FUNCTION", "更新");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "冻结机构资金池");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public boolean unfreezeFundPool(String ids, String loginUserName) {
		
		boolean result = fundPoolDao.unfreezeFundPool(ids);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "机构资金管理");
		log.put("V_OP_FUNCTION", "更新");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "解冻机构资金池");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return result;
	}

	@Override
	public List<FundPoolModel> getFundPoolByNum(String organiztion_num) {
		return fundPoolDao.getFundPoolByNum(organiztion_num);
	}
	
	@Override
	public List<FundPoolModel> getFundPoolById(String id) {
		return fundPoolDao.getFundPoolById(id);
	}

	@Override
	public boolean recharge(FundPoolModel fundPoolModel, String loginUserName) {
		
		// 查询原有的资金信息
		String organiztion_num = fundPoolModel.getOrganiztion_num();
		String organiztion_name = fundPoolModel.getOrganiztion_name();
		List<FundPoolModel> fundPoolList = fundPoolDao.getFundPoolByNum(organiztion_num);
		Double inbound = fundPoolList.get(0).getInbound();
		Double outbound = fundPoolList.get(0).getOutbound();
		Double total = fundPoolList.get(0).getTotal();
		
		// 计算充值金额，重新统计资金，更新资金池
		Double recharge_sum = fundPoolModel.getRecharge_sum();
		inbound = inbound + recharge_sum;
		Double left_total = inbound - outbound;
		total = inbound;
		Double rate = left_total/total;
		DecimalFormat df = new DecimalFormat(".0000"); // 资金百分比保存到小数点后四位
		Double percent = Double.parseDouble(df.format(rate));
		
		fundPoolModel.setInbound(inbound);
		fundPoolModel.setOutbound(outbound);
		fundPoolModel.setPercent(percent);
		fundPoolModel.setTotal(total);
		fundPoolModel.setLeft_total(left_total);
		
		boolean rechargeResult = fundPoolDao.updateFundPool(fundPoolModel);
		
		// 新增资金流水表记录
		FundModel fundModel = new FundModel();
		fundModel.setAmount(recharge_sum);
		fundModel.setOrganiztion_num(organiztion_num);
		fundModel.setOrganiztion_name(organiztion_name);
		fundModel.setOp_id(loginUserName);
		fundModel.setOp_type("2");
		fundModel.setBalance(left_total);
		fundModel.setReason(fundPoolModel.getRecharge_reason());
		
		boolean addResult = fundDao.addFund(fundModel);
		
		// 更新日志
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "机构资金管理");
		log.put("V_OP_FUNCTION", "更新");
		log.put("V_OP_ID", loginUserName);
		log.put("V_OP_MSG", "机构资金池注资");
		if(rechargeResult && addResult){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		
		return rechargeResult && addResult;
		
	}
	
}
