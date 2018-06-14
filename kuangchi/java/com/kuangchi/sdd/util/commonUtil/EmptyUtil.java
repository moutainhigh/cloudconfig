package com.kuangchi.sdd.util.commonUtil;

public class EmptyUtil {

	public static boolean isEmpty(Object obj)
	{
		if(null==obj||obj.equals(""))
		{
			return true;
		}else
		{
			return false;
		}
	}
	//判断至少有一个是空的
	public static boolean atLeastOneIsEmpty(String...objs)
	{
		for(String obj:objs)
		{
			if(obj==null||obj.equals(""))
			{
				return true;
			}
		}
		return false;
		
	}
}
