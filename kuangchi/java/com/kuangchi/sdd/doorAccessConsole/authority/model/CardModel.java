package com.kuangchi.sdd.doorAccessConsole.authority.model;

public class CardModel {
		private String cardNum;
		private String cardType;
		private String cardName;
		private String createTime;
		private String stateDm;
		private String stateName;
		private String description;
		public String getCardNum() {
			return cardNum;
		}
		public void setCardNum(String cardNum) {
			this.cardNum = cardNum;
		}
		public String getCardType() {
			return cardType;
		}
		public void setCardType(String cardType) {
			this.cardType = cardType;
		}
		public String getCardName() {
			return cardNum+"("+cardType+")";
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getStateDm() {
			return stateDm;
		}
		public void setStateDm(String stateDm) {
			this.stateDm = stateDm;
		}
		public String getStateName() {
			return stateName;
		}
		public void setStateName(String stateName) {
			this.stateName = stateName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}


		
		
}
