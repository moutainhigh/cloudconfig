package com.xkd.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Payment;

public interface PaymentMapper {

	List<Payment> selectPaymentByCompanyId(@Param("companyId") String companyId);
	
	List<Payment> selectPaymentByCompanyIdOrderByPaymentDate(@Param("companyId") String companyId);

	Payment selectPaymentByUserId(@Param("userId") String userId);

	Integer updatePaymentBySql(@Param("sql") String sql);

	Integer updatePaymentInfoById(Payment pm);

	Integer insertPayment(Payment payment);

	Payment selectPaymentById(String id);

	String selectSumMoneyByCompanyId(@Param("companyId") String companyId);
	
	public int deletePaymentById(@Param("id") String id);
	
}
