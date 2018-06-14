package com.kuangchi.sdd.singleLogin.dao;

import java.util.Map;

public interface SingleLoginDao {
	//读取CS考勤系统的单点登录标志
     Map getLoginSerialNum(String localLoginSerialNum);
}
