package com.xkd.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xkd.mapper.PaymentMapper;
import com.xkd.model.Payment;

@Service
public class PaymentService {

	
	@Autowired
	private PaymentMapper paymentMapper;
	
	public List<Payment> selectPaymentByCompanyId(String companyId){
		
		List<Payment> payments = paymentMapper.selectPaymentByCompanyId(companyId);
		
		return payments;
	}
	
	
	public List<Payment> selectPaymentByCompanyIdOrderByPaymentDate(String companyId){
		
		List<Payment> payments = paymentMapper.selectPaymentByCompanyIdOrderByPaymentDate(companyId);
		
		return payments;
	}
	
	public Payment selectPaymentByUserId(String userId){
		
		Payment payment = paymentMapper.selectPaymentByUserId(userId);
		
		return payment;
	}
	
	public Integer updatePaymentBySql(String sql) {
		
		Integer res = paymentMapper.updatePaymentBySql(sql);
		
		return res;
	}

	/**
	 * 
	 * @author: xiaoz
	 * @2017年4月27日 
	 * @功能描述:根据会议id，用户id，企业id改变缴费信息表
	 * @param pm
	 */
	public Integer updatePaymentInfoById(Payment pm) {
		
		Integer res = paymentMapper.updatePaymentInfoById(pm);
		
		return res;
		
	}

	public Integer insertPayment(Payment payment) {
		
		Integer res = paymentMapper.insertPayment(payment);
		
		return res;
	}


	public Payment selectPaymentById(String id) {
		
		Payment payment = paymentMapper.selectPaymentById(id);
		
		return payment;
	}


	public String selectSumMoneyByCompanyId(String companyId) {
		
		String sumMoney = paymentMapper.selectSumMoneyByCompanyId(companyId);
		
		return sumMoney;
	}
	
	public int deletePaymentById(@Param("id") String id){
		return paymentMapper.deletePaymentById(id);
	}

	
}
