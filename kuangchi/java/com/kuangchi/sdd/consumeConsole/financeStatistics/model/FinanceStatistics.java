package com.kuangchi.sdd.consumeConsole.financeStatistics.model;

import java.math.BigDecimal;

public class FinanceStatistics {
	
	private Integer id;
	private String detail_flow_no;//明细流水号
	private String account_num;//账号
	private String time;//时间
	private String staff_num;//员工编号
	private String staff_no;//员工工号
	private double inbound;//收入金额
	private double outbound;//支出金额
	private double balance;//余额
	private String staff_name;//员工姓名
	private String dept_no;//部门编号
	private String dept_num;//部门代码
	private String dept_name;//部门名称
	private String type;//收支类型 0 充值 1 消费 2 补助 3 补扣 4 撤销消费 5 退款 6 转入 7 转出
	private String deal_reason;//交易原因
	private String count_flag;//账户标志
	private String begin_time;//开始时间
	private String end_time;//结束时间
	
	
	
	private double inbound_a;//充值总额
	private double inbound_b;//补助总额
	private double inbound_c;//撤销消费总额
	private double inbound_d;//转入总额
	private double outbound_a;//消费总额
	private double outbound_b;//补扣总额
	private double outbound_c;//退款总额
	private double outbound_d;//转出总额
	
	
	private int inbound_Num;//收入总次数
	private double inbound_Money;//收入总金额
	
	private int outbound_Num;//收入总次数
	private double outbound_Money;//收入总金额
	
	
	private String card_num;//卡号
	private String card_pledge;//卡片押金
	private String create_time;//创建时间
	private String state_dm;//卡片状态码
	private double account_balance;//余额
	private int delete_flag;//删除标志位
	
	private double card_pledge_money;//卡片押金总额
	private int card_pledge_count;//卡片押金次数
	
	private double notcard_pledge_money;//未发卡片押金总额
	private int notcard_pledge_count;//未发卡片押金次数
	
	private double refund_pledge_money;//退款押金总额
	private int refund_pledge_count;//退款押金次数
	
	private double people_accountMoney;//账户总额
	private double begin_accountMoney;//期初余额
	private double end_accountMoney;//期末余额
	private int begin_accountCount;//期初次数
	private int end_accountCount;//期末次数
	
	private String beginTimes;//时间-隐藏域
	private String endTimes;//结束时间+24小时
	
	private int count00;//未绑定
	private int count10;//未发卡
	private int count20;//正常
	private int count30;//报损
	private int count40;//挂失
	private int count50;//解挂
	private int count60;//退卡
	private int count100;//发卡不成功
	
	
	
	
	public double getNotcard_pledge_money() {
		return notcard_pledge_money;
	}
	public void setNotcard_pledge_money(double notcard_pledge_money) {
		this.notcard_pledge_money = notcard_pledge_money;
	}
	public int getNotcard_pledge_count() {
		return notcard_pledge_count;
	}
	public void setNotcard_pledge_count(int notcard_pledge_count) {
		this.notcard_pledge_count = notcard_pledge_count;
	}
	public int getCount00() {
		return count00;
	}
	public void setCount00(int count00) {
		this.count00 = count00;
	}
	public int getCount10() {
		return count10;
	}
	public void setCount10(int count10) {
		this.count10 = count10;
	}
	public int getCount20() {
		return count20;
	}
	public void setCount20(int count20) {
		this.count20 = count20;
	}
	public int getCount30() {
		return count30;
	}
	public void setCount30(int count30) {
		this.count30 = count30;
	}
	public int getCount40() {
		return count40;
	}
	public void setCount40(int count40) {
		this.count40 = count40;
	}
	public int getCount50() {
		return count50;
	}
	public void setCount50(int count50) {
		this.count50 = count50;
	}
	public int getCount60() {
		return count60;
	}
	public void setCount60(int count60) {
		this.count60 = count60;
	}
	public int getCount100() {
		return count100;
	}
	public void setCount100(int count100) {
		this.count100 = count100;
	}

	public String getBeginTimes() {
		return beginTimes;
	}
	public void setBeginTimes(String beginTimes) {
		this.beginTimes = beginTimes;
	}
	
	public double getBegin_accountMoney() {
		return begin_accountMoney;
	}
	public void setBegin_accountMoney(double begin_accountMoney) {
		BigDecimal b=new BigDecimal(begin_accountMoney);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.begin_accountMoney = f1;
	}
	public double getEnd_accountMoney() {
		return end_accountMoney;
	}
	public void setEnd_accountMoney(double end_accountMoney) {
		BigDecimal b=new BigDecimal(end_accountMoney);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.end_accountMoney = f1;
	}
	public int getBegin_accountCount() {
		return begin_accountCount;
	}
	public void setBegin_accountCount(int begin_accountCount) {
		this.begin_accountCount = begin_accountCount;
	}
	public int getEnd_accountCount() {
		return end_accountCount;
	}
	public void setEnd_accountCount(int end_accountCount) {
		this.end_accountCount = end_accountCount;
	}
	public double getPeople_accountMoney() {
		return people_accountMoney;
	}
	public void setPeople_accountMoney(double people_accountMoney) {
		this.people_accountMoney = people_accountMoney;
	}
	public double getCard_pledge_money() {
		return card_pledge_money;
	}
	public void setCard_pledge_money(double card_pledge_money) {
		this.card_pledge_money = card_pledge_money;
	}
	public int getCard_pledge_count() {
		return card_pledge_count;
	}
	public void setCard_pledge_count(int card_pledge_count) {
		this.card_pledge_count = card_pledge_count;
	}
	public double getRefund_pledge_money() {
		return refund_pledge_money;
	}
	public void setRefund_pledge_money(double refund_pledge_money) {
		this.refund_pledge_money = refund_pledge_money;
	}
	public int getRefund_pledge_count() {
		return refund_pledge_count;
	}
	public void setRefund_pledge_count(int refund_pledge_count) {
		this.refund_pledge_count = refund_pledge_count;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	
	public String getCard_pledge() {
		return card_pledge;
	}
	public void setCard_pledge(String card_pledge) {
		this.card_pledge = card_pledge;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getState_dm() {
		return state_dm;
	}
	public void setState_dm(String state_dm) {
		this.state_dm = state_dm;
	}
	public double getAccount_balance() {
		return account_balance;
	}
	public void setAccount_balance(double account_balance) {
		BigDecimal b=new BigDecimal(account_balance);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.account_balance = f1;
	}
	public int getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}
	public int getInbound_Num() {
		return inbound_Num;
	}
	public void setInbound_Num(int inbound_Num) {
		this.inbound_Num = inbound_Num;
	}
	public double getInbound_Money() {
		return inbound_Money;
	}
	public void setInbound_Money(double inbound_Money) {
		BigDecimal b=new BigDecimal(inbound_Money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_Money = f1;
	}
	public int getOutbound_Num() {
		return outbound_Num;
	}
	public void setOutbound_Num(int outbound_Num) {
		this.outbound_Num = outbound_Num;
	}
	public double getOutbound_Money() {
		return outbound_Money;
	}
	public void setOutbound_Money(double outbound_Money) {
		BigDecimal b=new BigDecimal(outbound_Money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_Money = f1;
	}
	private int inbound_a_c;//充值总额次数
	private int inbound_b_c;//补助总额次数
	private int inbound_c_c;//撤销消费总额次数
	private int inbound_d_c;//转入总额次数
	private int outbound_a_c;//消费总额次数
	private int outbound_b_c;//补扣总额次数
	private int outbound_c_c;//退款总额次数
	private int outbound_d_c;//转出总额次数
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDetail_flow_no() {
		return detail_flow_no;
	}
	public void setDetail_flow_no(String detail_flow_no) {
		this.detail_flow_no = detail_flow_no;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public double getInbound() {
		return inbound;
	}
	public void setInbound(double inbound) {
		BigDecimal b=new BigDecimal(inbound);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound = f1;
	}
	public double getOutbound() {
		return outbound;
	}
	public void setOutbound(double outbound) {
		BigDecimal b=new BigDecimal(outbound);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound = f1;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeal_reason() {
		return deal_reason;
	}
	public void setDeal_reason(String deal_reason) {
		this.deal_reason = deal_reason;
	}
	public String getCount_flag() {
		return count_flag;
	}
	public void setCount_flag(String count_flag) {
		this.count_flag = count_flag;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public double getInbound_a() {
		return inbound_a;
	}
	public void setInbound_a(double inbound_a) {
		BigDecimal b=new BigDecimal(inbound_a);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_a = f1;
	}
	public double getInbound_b() {
		return inbound_b;
	}
	public void setInbound_b(double inbound_b) {
		BigDecimal b=new BigDecimal(inbound_b);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_b = f1;
	}
	public double getInbound_c() {
		return inbound_c;
	}
	public void setInbound_c(double inbound_c) {
		BigDecimal b=new BigDecimal(inbound_c);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_c = f1;
	}
	public double getInbound_d() {
		return inbound_d;
	}
	public void setInbound_d(double inbound_d) {
		BigDecimal b=new BigDecimal(inbound_d);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_d = f1;
	}
	public double getOutbound_a() {
		return outbound_a;
	}
	public void setOutbound_a(double outbound_a) {
		BigDecimal b=new BigDecimal(outbound_a);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_a = f1;
	}
	public double getOutbound_b() {
		return outbound_b;
	}
	public void setOutbound_b(double outbound_b) {
		BigDecimal b=new BigDecimal(outbound_b);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_b = f1;
	}
	public double getOutbound_c() {
		return outbound_c;
	}
	public void setOutbound_c(double outbound_c) {
		BigDecimal b=new BigDecimal(outbound_c);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_c = f1;
	}
	public double getOutbound_d() {
		return outbound_d;
	}
	public void setOutbound_d(double outbound_d) {
		BigDecimal b=new BigDecimal(outbound_d);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_d = f1;
	}
	public int getInbound_a_c() {
		return inbound_a_c;
	}
	public void setInbound_a_c(int inbound_a_c) {
		this.inbound_a_c = inbound_a_c;
	}
	public int getInbound_b_c() {
		return inbound_b_c;
	}
	public void setInbound_b_c(int inbound_b_c) {
		this.inbound_b_c = inbound_b_c;
	}
	public int getInbound_c_c() {
		return inbound_c_c;
	}
	public void setInbound_c_c(int inbound_c_c) {
		this.inbound_c_c = inbound_c_c;
	}
	public int getInbound_d_c() {
		return inbound_d_c;
	}
	public void setInbound_d_c(int inbound_d_c) {
		this.inbound_d_c = inbound_d_c;
	}
	public int getOutbound_a_c() {
		return outbound_a_c;
	}
	public void setOutbound_a_c(int outbound_a_c) {
		this.outbound_a_c = outbound_a_c;
	}
	public int getOutbound_b_c() {
		return outbound_b_c;
	}
	public void setOutbound_b_c(int outbound_b_c) {
		this.outbound_b_c = outbound_b_c;
	}
	public int getOutbound_c_c() {
		return outbound_c_c;
	}
	public void setOutbound_c_c(int outbound_c_c) {
		this.outbound_c_c = outbound_c_c;
	}
	public int getOutbound_d_c() {
		return outbound_d_c;
	}
	public void setOutbound_d_c(int outbound_d_c) {
		this.outbound_d_c = outbound_d_c;
	}
	public String getEndTimes() {
		return endTimes;
	}
	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}
	
	
	
}
