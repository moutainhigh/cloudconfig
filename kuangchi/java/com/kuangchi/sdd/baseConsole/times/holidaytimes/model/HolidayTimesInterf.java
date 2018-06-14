package com.kuangchi.sdd.baseConsole.times.holidaytimes.model;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-25下午1:56:53
 * @功能描述:实现服务器接口传参model
 * @参数描述:
 */
public class HolidayTimesInterf {
	//传递的日期model
	private String holiday_dateInter;//日期
	private String day_of_weekInter;//星期
	
	private String result_code;	//返回结果类型
	
	
	public String getResult_code() {
		return result_code;
	}


	public void setResult_type(String result_code) {
		this.result_code = result_code;
	}


	public String getHoliday_dateInter() {
		return holiday_dateInter;
	}


	public void setHoliday_dateInter(String holiday_dateInter) {
		this.holiday_dateInter = holiday_dateInter;
	}


	public String getDay_of_weekInter() {
		return day_of_weekInter;
	}


	public void setDay_of_weekInter(String day_of_weekInter) {
		/*switch (Integer.valueOf(day_of_weekInter)){
		 case 0: day_of_weekInter="节假日";break;
		 case 1: day_of_weekInter="星期一"; break;
		 case 2: day_of_weekInter="星期二"; break;
		 case 3: day_of_weekInter="星期三"; break;
		 case 4: day_of_weekInter="星期四"; break;
		 case 5: day_of_weekInter="星期五"; break;
		 case 6: day_of_weekInter="星期六"; break;
		 case 7: day_of_weekInter="星期日"; break;
		 }*/
		this.day_of_weekInter = day_of_weekInter;
	}
	
}
