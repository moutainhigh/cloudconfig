package com.kuangchi.sdd.consumeConsole.device.model;

public class DataDown {
	int money; // 定额金额 3 字节
	int limit; // 机器限额 3
	int confirm;// 定额操作方式：是否按确认键完成消费 1
	int multiUse; // 定额操作方式：同一张卡连续多次刷卡，是否提示确认 1
	int password;// 消费机操作密码 3
	int[] meal1 = new int[4];// 餐次1起止时间 4
	int[] meal2 = new int[4];// 餐次2起止时间 4
	int[] meal3 = new int[4];// 餐次3起止时间 4
	int[] meal4 = new int[4];// 餐次4起止时间 4
	int groupsuport;// 终端支持的卡分组 1
	int onOffLineWay;// 脱机、联机方式（ID机型） 1
	int[] goodNumMoney = new int[10]; // 菜号金额 30
										// 在菜号模式下，0-9每个按键对应的菜号金额，共10个按键，每个金额占3个字节，共30个字节
	int timeLimit;// 时段定额功能 1

	int[] timeLimitMoney = new int[4];// 4个餐次时段的定额值
										// 4个餐次时段的定额值，每个时段定额值为3个字节，4个时段共12个字节
	int[] retain = new int[180];// 预留，全部填00

	int meal1Mode = 0;// 餐次1的模式
	int meal2Mode = 0;// 餐次2的模式
	int meal3Mode = 0;// 餐次3的模式
	int meal4Mode = 0;// 餐次4的模式

	public int getMoney() {
		return money;

	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	public int getMultiUse() {
		return multiUse;
	}

	public void setMultiUse(int multiUse) {
		this.multiUse = multiUse;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public int[] getMeal1() {
		return meal1;
	}

	public void setMeal1(int[] meal1) {
		this.meal1 = meal1;
	}

	public int[] getMeal2() {
		return meal2;
	}

	public void setMeal2(int[] meal2) {
		this.meal2 = meal2;
	}

	public int[] getMeal3() {
		return meal3;
	}

	public void setMeal3(int[] meal3) {
		this.meal3 = meal3;
	}

	public int[] getMeal4() {
		return meal4;
	}

	public void setMeal4(int[] meal4) {
		this.meal4 = meal4;
	}

	public int getGroupsuport() {
		return groupsuport;
	}

	public void setGroupsuport(int groupsuport) {
		this.groupsuport = groupsuport;
	}

	public int getOnOffLineWay() {
		return onOffLineWay;
	}

	public void setOnOffLineWay(int onOffLineWay) {
		this.onOffLineWay = onOffLineWay;
	}

	public int[] getGoodNumMoney() {
		return goodNumMoney;
	}

	public void setGoodNumMoney(int[] goodNumMoney) {
		this.goodNumMoney = goodNumMoney;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int[] getTimeLimitMoney() {
		return timeLimitMoney;
	}

	public void setTimeLimitMoney(int[] timeLimitMoney) {
		this.timeLimitMoney = timeLimitMoney;
	}

	public int[] getRetain() {
		return retain;
	}

	public void setRetain(int[] retain) {
		this.retain = retain;
	}

	public int getMeal1Mode() {
		return meal1Mode;
	}

	public void setMeal1Mode(int meal1Mode) {
		this.meal1Mode = meal1Mode;
	}

	public int getMeal2Mode() {
		return meal2Mode;
	}

	public void setMeal2Mode(int meal2Mode) {
		this.meal2Mode = meal2Mode;
	}

	public int getMeal3Mode() {
		return meal3Mode;
	}

	public void setMeal3Mode(int meal3Mode) {
		this.meal3Mode = meal3Mode;
	}

	public int getMeal4Mode() {
		return meal4Mode;
	}

	public void setMeal4Mode(int meal4Mode) {
		this.meal4Mode = meal4Mode;
	}

}
