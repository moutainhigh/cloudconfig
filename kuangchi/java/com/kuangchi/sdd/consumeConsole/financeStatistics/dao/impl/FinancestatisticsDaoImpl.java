package com.kuangchi.sdd.consumeConsole.financeStatistics.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.financeStatistics.dao.IFinancestatisticsDao;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.AccountStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.dao.IIncomestatisticsDao;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;

@Repository("financestatisticsDaoImpl")
public class FinancestatisticsDaoImpl extends BaseDaoImpl<FinanceStatistics> implements IFinancestatisticsDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	//根据充值类型查询充值信息 
	@Override
	public List<FinanceStatistics> selectRecharge(FinanceStatistics finance) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", finance.getBegin_time());
		mapState.put("end_time", finance.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectRecharge", mapState);
	}
	
	//查询所有的卡片押金
	@Override
	public List<FinanceStatistics> selectCarddeposit(FinanceStatistics card) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", card.getBegin_time());
		mapState.put("end_time", card.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectCarddeposit", mapState);
	}
	
	//根据退款类型查询退款信息
	@Override
	public FinanceStatistics selectAccountInfoMoney(FinanceStatistics finance) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", finance.getBegin_time());
		mapState.put("end_time", finance.getEndTimes());
		return (FinanceStatistics) this.getSqlMapClientTemplate().queryForObject("selectAccountInfoMoney", mapState);
	}
	

	//查询账户信息表的期初余额
	@Override
	public List<FinanceStatistics> selectAccountInfoBegin(FinanceStatistics card) {
		
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", card.getBegin_time());
		mapState.put("end_time", card.getBeginTimes());
		return this.getSqlMapClientTemplate().queryForList("selectAccountInfoBegin", mapState);
	}

	//查询账户信息表的期末余额
	@Override
	public List<FinanceStatistics> selectAccountInfoEnd(FinanceStatistics card) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", card.getBegin_time());
		mapState.put("end_time", card.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectAccountInfoEnd", mapState);
	}

	@Override
	public Boolean addAccountStatic(AccountStatistics account) {
		// TODO Auto-generated method stub
		return insert("addAccountStatic",account);
	}

	@Override
	public List<AccountStatistics> selectAccountStatic(
			AccountStatistics account, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("begin_time", account.getBegin_time());
		mapState.put("end_time", account.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectAccountStatic", mapState);
	}

	@Override
	public Integer selectAccountStaticCount(AccountStatistics account) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", account.getBegin_time());
		mapState.put("end_time", account.getEnd_time());
		return queryCount("selectAccountStaticCount", mapState);
	}

	@Override
	public List<FinanceStatistics> selectCardrefundDeposit(
			FinanceStatistics finance) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", finance.getBegin_time());
		mapState.put("end_time", finance.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectCardrefundDeposit", mapState);
	}

	@Override
	public List<FinanceStatistics> selectCardStateNum(FinanceStatistics finance) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", finance.getBegin_time());
		mapState.put("end_time", finance.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectCardStateNum", mapState);
	}

	@Override
	public List<FinanceStatistics> selectRefundCardState(
			FinanceStatistics finance) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("begin_time", finance.getBegin_time());
		mapState.put("end_time", finance.getEndTimes());
		return this.getSqlMapClientTemplate().queryForList("selectRefundCardState", mapState);
	}

}
