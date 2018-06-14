package com.kuangchi.sdd.interfaceConsole.cardSender.model;

/**
 * 卡片操作模型
 * 
 * @author xuewen.deng
 * @功能描述: 卡片操作model，供发卡软件用
 */

public class CardOperaSyncModel {
	String id;// 发卡软件端id
	String bsId;// bs端人卡绑定id
	String cardNo;// 卡号
	String personId; // B/S人员UUID
	String cardType;// 卡类别【0】光子卡，【1】IC卡，【2】光钥匙。
	String cardStatus;// 卡状态【20】正常，【40】挂失，【352】冻结。
	String endDate; // 卡片失效时间
	String errorCode; // 错误编码

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getBsId() {
		return bsId;
	}

	public void setBsId(String bsId) {
		this.bsId = bsId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
