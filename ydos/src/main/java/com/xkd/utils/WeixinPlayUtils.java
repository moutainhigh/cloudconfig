package com.xkd.utils;



import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weixin")
public class WeixinPlayUtils {
	
	
	public final static String APPID =PropertiesUtil.APPID;
	public final static String SECRET =PropertiesUtil.APPSECRET;
	public static String MCH_ID = PropertiesUtil.MCH_ID;
	public static String API_KEY = PropertiesUtil.API_KEY;
	public static String NOTIFY_URL = PropertiesUtil.NOTIFYURL;//支付成功回调接口
	
	
	public static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//下单接口
	public static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";//查询下单接口
	public static String refund_url = "https://api.mch.weixin.qq.com/pay/refund";
	public final static String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	public final static String GRANT_TYPE = "authorization_code";
	
	
	
	//传入
	//body商品名称
	//total_fee 商品金额
	//openId 下单用户的openId
	//返回  
	//mhtOrderNo 订单号
	//此返回数据需全部返回前端
	public static SortedMap<Object, Object> weixinUserOrder(Map<String, Object> map) throws Exception{

		String nonceStr = getNonceStr();//随机数
		String out_trade_no = getOrderNoStr();//订单号
		
		//微信下单数据
		SortedMap<Object, Object> orderInfo = new TreeMap<Object,Object>(); 
		orderInfo.put("appid", APPID);
		orderInfo.put("mch_id", MCH_ID);
		orderInfo.put("nonce_str", nonceStr);
		orderInfo.put("body", map.get("body"));
		orderInfo.put("out_trade_no", out_trade_no);
		orderInfo.put("total_fee",  map.get("total_fee"));
		orderInfo.put("spbill_create_ip", "120.78.216.64");
		orderInfo.put("notify_url", NOTIFY_URL);
		orderInfo.put("trade_type", "JSAPI");
		orderInfo.put("openid",map.get("openId"));
		orderInfo.put("sign", createSign("UTF-8",orderInfo));
		
		//微信下单数据 封装成标准格式
		String resultXML = getRequestXml(orderInfo);
		HttpRequest request = new HttpRequest();
		String result = request.postData(UNIFIED_ORDER_URL, "UTF-8", resultXML);

		//下单成功获取prepayId返回前台掉起微信密码框
		String prepayId = result.substring(result.indexOf("<prepay_id><![CDATA[")+20, result.indexOf("]]></prepay_id>"));
		
		//封装数据返回前台掉起密码框，并将下单数据保存到数据库
        SortedMap<Object, Object> csOrder = new TreeMap<Object,Object>();
		csOrder.put("package", "prepay_id="+prepayId);
		csOrder.put("timeStamp", getTimeStamp());
		csOrder.put("signType", "MD5");
		csOrder.put("nonceStr",nonceStr);
		csOrder.put("appId", APPID);
		csOrder.put("sign", createSign("UTF-8",csOrder));
		csOrder.put("mhtOrderNo", out_trade_no);//订单号
		
		return csOrder;
		
	}
	//生成随机数字
	public static String getNonceStr() {
	  Random random = new Random();
	  return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }
	//时间戳
	public static String getTimeStamp() {
	    return System.currentTimeMillis() / 1000 + "";
	  }
	//签名，数据必须是SortedMap类型
	public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v) 
            		&& !"sign".equals(k) && !"key".equals(k)) {
            	sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }
	//生成订单号
    public static String getOrderNoStr() {

		Random random = new Random();
		String rannmber = random.nextInt(8999) + 1000 +"";
		return System.currentTimeMillis()+rannmber;
	}
    //将数据封装成xml格式
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = entry.getValue()+"";
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
  //将数据封装成xml格式
    public static String getRequestXml2(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = entry.getValue()+"";
            sb.append("<"+k+">"+v+"</"+k+">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
    //out_trade_no 订单号
    //total_fee 订单总价
    //refund_fee 退款金额
    //transaction_id 支付订单号
    //refund_desc 退款说明
    public static void refund(Map<String, Object> map) {
    	String nonceStr = getNonceStr();//随机数
		String out_refund_no = getOrderNoStr();//退款 订单号
		//微信下单数据
		SortedMap<Object, Object> orderInfo = new TreeMap<Object,Object>(); 
		orderInfo.put("appid", APPID);
		orderInfo.put("mch_id", MCH_ID);
		orderInfo.put("nonce_str", nonceStr);
		orderInfo.put("out_trade_no", map.get("out_trade_no"));
		orderInfo.put("out_refund_no", out_refund_no);
		orderInfo.put("total_fee", map.get("total_fee"));
		orderInfo.put("refund_fee", map.get("refund_fee"));
		orderInfo.put("refund_desc", map.get("refund_desc"));
		orderInfo.put("transaction_id", map.get("transaction_id"));
		
		orderInfo.put("sign", createSign("UTF-8",orderInfo));
		
		//微信下单数据 封装成标准格式
		String resultXML = getRequestXml2(orderInfo);
		System.out.println(resultXML);
		String result = null;
		try {
			ClientCustomSSL.refund(resultXML);
		} catch (Exception e) {
			System.out.println("有异常");
			e.printStackTrace();
		}
		System.out.println(result);
	}

    
    
}
