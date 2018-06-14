package com.kuangchi.sdd.elevatorConsole.device.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.baseConsole.device.model.Time;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

public class CommTimeUtil {
			public static Time TransformCommTime(String result){
				final int LENGTH = 2;
				Time time = null;
				try {
						time = new Time();
						String f = "%0" + LENGTH + "d";
						StringBuffer sbuffer = new StringBuffer();
						sbuffer.append(result.substring(0,4));
						sbuffer.append("-");
						sbuffer.append(String.format(f,
								Integer.parseInt(result.substring(4,6))));
						sbuffer.append("-");
						sbuffer.append(String.format(f,
								Integer.parseInt(result.substring(6,8))));
						sbuffer.append(" ");
						sbuffer.append(String.format(f,
								Integer.parseInt(result.substring(8,10))));
						sbuffer.append(":");
						sbuffer.append(String.format(f,
								Integer.parseInt(result.substring(10,12))));
						sbuffer.append(":");
						sbuffer.append(String.format(f,
								Integer.parseInt(result.substring(12,14))));
						time.setDate(sbuffer.toString());
						time.setDayOfWeek(result.substring(14,16));

				} catch (Exception e) {
					e.printStackTrace();
				}

			return time;
			}
}
