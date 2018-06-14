package com.kuangchi.sdd.consumeConsole.financeStatistics.model;

import java.math.BigDecimal;

public class AccountStatistics {
	
	private Integer id;
	private Double start_account;
	private Double end_account;
	private Double recharge_inbound;//
	private Double grants_inbound;//
	private Double revoke_inbound;//
	private Double card_inbound;//
	private Double notcard_inbound;//
	private Double cardback_inbound;//
	private Double consume_outbound;//
	private Double buckle_outbound;//
	private Double refund_outbound;//
	private String create_time;
	private String begin_time;
	private String end_time;
	private String begin_t;
	private String end_t;
	
	private int start_account_c;
	private int end_account_c;
	private int recharge_c;//
	private int grants_c;//
	private int revoke_c;//
	private int card_c;//
	private int notcard_c;//
	private int cardback_c;//
	private int consume_c;//
	private int buckle_c;//
	private int refund_c;//
	
	/*BigDecimal b=new BigDecimal(recharge_inbound);  
	double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	this.recharge_inbound = f1;*/
	
	
	
	
	
	public Double getStart_account() {
		return start_account;
	}
	public int getNotcard_c() {
		return notcard_c;
	}
	public void setNotcard_c(int notcard_c) {
		this.notcard_c = notcard_c;
	}
	public Double getNotcard_inbound() {
		return notcard_inbound;
	}
	public void setNotcard_inbound(Double notcard_inbound) {
		this.notcard_inbound = notcard_inbound;
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
	public void setStart_account(Double start_account) {
		BigDecimal b=new BigDecimal(start_account);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.start_account = f1;
	}
	public Double getEnd_account() {
		return end_account;
	}
	public void setEnd_account(Double end_account) {
		BigDecimal b=new BigDecimal(end_account);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.end_account = f1;
	}
	public int getStart_account_c() {
		return start_account_c;
	}
	public void setStart_account_c(int start_account_c) {
		this.start_account_c = start_account_c;
	}
	public int getEnd_account_c() {
		return end_account_c;
	}
	public void setEnd_account_c(int end_account_c) {
		this.end_account_c = end_account_c;
	}
	
	public String getBegin_t() {
		return begin_t;
	}
	public void setBegin_t(String begin_t) {
		this.begin_t = begin_t;
	}
	public String getEnd_t() {
		return end_t;
	}
	public void setEnd_t(String end_t) {
		this.end_t = end_t;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getRecharge_c() {
		return recharge_c;
	}
	public void setRecharge_c(int recharge_c) {
		this.recharge_c = recharge_c;
	}
	public int getGrants_c() {
		return grants_c;
	}
	public void setGrants_c(int grants_c) {
		this.grants_c = grants_c;
	}
	public int getRevoke_c() {
		return revoke_c;
	}
	public void setRevoke_c(int revoke_c) {
		this.revoke_c = revoke_c;
	}
	public int getCard_c() {
		return card_c;
	}
	public void setCard_c(int card_c) {
		this.card_c = card_c;
	}
	public int getCardback_c() {
		return cardback_c;
	}
	public void setCardback_c(int cardback_c) {
		this.cardback_c = cardback_c;
	}
	public int getConsume_c() {
		return consume_c;
	}
	public void setConsume_c(int consume_c) {
		this.consume_c = consume_c;
	}
	public int getBuckle_c() {
		return buckle_c;
	}
	public void setBuckle_c(int buckle_c) {
		this.buckle_c = buckle_c;
	}
	public int getRefund_c() {
		return refund_c;
	}
	public void setRefund_c(int refund_c) {
		this.refund_c = refund_c;
	}

	public Double getRecharge_inbound() {
		return recharge_inbound;
	}
	public void setRecharge_inbound(Double recharge_inbound) {
		BigDecimal b=new BigDecimal(recharge_inbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.recharge_inbound = f1;
	}
	public Double getGrants_inbound() {
		return grants_inbound;
	}
	public void setGrants_inbound(Double grants_inbound) {
		BigDecimal b=new BigDecimal(grants_inbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.grants_inbound = f1;
	}
	public Double getRevoke_inbound() {
		return revoke_inbound;
	}
	public void setRevoke_inbound(Double revoke_inbound) {
		BigDecimal b=new BigDecimal(revoke_inbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.revoke_inbound = f1;
	}
	public Double getCard_inbound() {
		return card_inbound;
	}
	public void setCard_inbound(Double card_inbound) {
		BigDecimal b=new BigDecimal(card_inbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.card_inbound = f1;
	}
	public Double getCardback_inbound() {
		return cardback_inbound;
	}
	public void setCardback_inbound(Double cardback_inbound) {
		BigDecimal b=new BigDecimal(cardback_inbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.cardback_inbound = f1;
	}
	public Double getConsume_outbound() {
		return consume_outbound;
	}
	public void setConsume_outbound(Double consume_outbound) {
		BigDecimal b=new BigDecimal(consume_outbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.consume_outbound = f1;
	}
	public Double getBuckle_outbound() {
		return buckle_outbound;
	}
	public void setBuckle_outbound(Double buckle_outbound) {
		BigDecimal b=new BigDecimal(buckle_outbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.buckle_outbound = f1;
	}
	public Double getRefund_outbound() {
		return refund_outbound;
	}
	public void setRefund_outbound(Double refund_outbound) {
		BigDecimal b=new BigDecimal(refund_outbound);  
		Double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.refund_outbound = f1;
	}
	
	
}
