package com.kuangchi.sdd.util.commonUtil;


/**
 * @author ccdt
 *
 */
public class BeanUtil {
	
	/**
	 * 判断对象是否为空
	 * @param obj
	 * @return 为空是返回true 反之false
	 */
	public static boolean isEmpty(Object obj){
		if(null == obj){
			return true;
		}else{
			return "".equals(obj.toString()) ? true:false;
		}
		
	}
	/**
	 * 判断对象是否不为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}
	
	/**
	 * 俩者是否相等
	 * @param obj
	 * @param target
	 * @return
	 */
	public static boolean isEqual(Object obj,Object target){
		if(isEmpty(obj)){
			return false;
		}else{
			if(obj == target){
				return true;
			}else if(obj.toString().equals(target.toString())){
				return true;
			}else{
				return false;
			}
		}
	}
	

	
}
