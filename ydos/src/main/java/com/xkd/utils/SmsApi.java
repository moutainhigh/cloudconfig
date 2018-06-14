package com.xkd.utils;

import com.xkd.service.ObjectNewsService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;

/**
 * 发送短信
 */
public class SmsApi {

	private static final String NAME = "18682218202";
	private static final String PWD = "C088F853BAB3B73C23C6C6D65BA1";
//	private static final String PWD = "A49EA30CC139079B32AE4E373A34";

	/**
	 * 手机发送验证码
	 * @param moblie
	 * @return 6位数的验证码
	 */
	public static String sendSms(String moblie) {

		Random random = new Random();
		int code = random.nextInt(8999) + 1000;
		//发送内容
		String sign="";

		// 创建StringBuffer对象用来操作字符串
		//StringBuffer sb = new StringBuffer("http://sms.1xinxi.cn:8881/asmx/smsservice.aspx?");
		StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name="+NAME);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd="+PWD);

		// 向StringBuffer追加手机号码
		sb.append("&mobile="+moblie);

		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 向StringBuffer追加消息内容转URL标准码
		try {
			sb.append("&content="+URLEncoder.encode("【资本之道】您申请的验证码为："+code+"，该验证码5分钟内有效，请注意保密。","UTF-8"));
			//加签名
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));

			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			System.out.println(sb.toString());
			URL url = new URL(sb.toString());

			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = SmsApi.convertStreamToString(is);
			System.out.println(returnStr);
			if(returnStr.indexOf("提交成功") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println(returnStr + " 手机号：" + moblie + " 验证码： "+ code);
				return code+"";
			}else{
				return "0000";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return null;
	}


	/**
	 * 手机发送验证码
	 * @param moblie
	 * @return 6位数的验证码
	 */
	public static String sendSmsInTicket(String moblie) {

		Random random = new Random();
		int code = random.nextInt(8999) + 1000;
		//发送内容
		String sign="";

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name="+NAME);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd="+PWD);

		// 向StringBuffer追加手机号码
		sb.append("&mobile="+moblie);

		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 向StringBuffer追加消息内容转URL标准码
		try {
			sb.append("&content="+URLEncoder.encode("验证码："+code+"（请勿泄露），若非本人操作，请忽略【蝌蚪智慧】","UTF-8"));

			//加签名
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));

			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			URL url = new URL(sb.toString());

			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = SmsApi.convertStreamToString(is);
			if(returnStr.indexOf("提交成功") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println(returnStr + " 手机号：" + moblie + " 验证码： "+ code);
				return code+"";
			}else{
				return "0000";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 移动端票务购票发送短信提醒
	 * @param moblie
	 * @return 6位数的验证码
	 */
	public static String sendSmsInTicketAttendMeeting(String adviserMobile,String moblie,String userName,String meetingName) {

		//发送内容
		String sign="";

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name="+NAME);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd="+PWD);

		// 向StringBuffer追加手机号码
		sb.append("&mobile="+adviserMobile);

		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 向StringBuffer追加消息内容转URL标准码
		try {
			sb.append("&content="+URLEncoder.encode("手机号为"+moblie+"的"+userName+"客户，通过crm系统参加"+meetingName+"活动，请知晓【蝌蚪智慧】","UTF-8"));

			//加签名
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));

			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			URL url = new URL(sb.toString());

			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = SmsApi.convertStreamToString(is);
			if(returnStr.indexOf("提交成功") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println("手机号为"+moblie+"的"+userName+"客户，通过crm系统参加"+meetingName+"活动，请知晓【蝌蚪智慧】");
				return "send message success";
			}else{
				return "0000";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 發送行程通知
	 * @param moblie
	 * @return 6位数的验证码
	 */
	public static String sendSms(String moblie,String toContent) {

		Random random = new Random();
		int code = random.nextInt(8999) + 1000;
		//发送内容
		String sign="";
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name="+NAME);

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd="+PWD);

		// 向StringBuffer追加手机号码
		sb.append("&mobile="+moblie);
		
		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 向StringBuffer追加消息内容转URL标准码
		try {
			sb.append("&content="+URLEncoder.encode(toContent,"UTF-8"));
			//加签名
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
			
			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			URL url = new URL(sb.toString());
	
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
	
			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = SmsApi.convertStreamToString(is);
			if(returnStr.indexOf("提交成功") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println(returnStr + " 手机号：" + moblie );
				return code+"";
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e);
		}
		return null;
	}
	

	/**
	 * 發送行程通知
	 * @param moblie	手机号
	 * @param toContent 内容
	 * @return 6位数的验证码
	 */
	public static String sendSmsByContent(String moblie,String toContent) {
		
		//发送内容
		String sign="";
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");
		
		// 向StringBuffer追加用户名
		sb.append("name="+NAME);
		
		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd="+PWD);
		
		// 向StringBuffer追加手机号码
		sb.append("&mobile="+moblie);
		
		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");
		
		// 向StringBuffer追加消息内容转URL标准码
		try {
			sb.append("&content="+URLEncoder.encode(toContent,"UTF-8"));
			//加签名
			sb.append("&sign="+URLEncoder.encode(sign,"UTF-8"));
			
			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			URL url = new URL(sb.toString());
			
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
			
			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = SmsApi.convertStreamToString(is);
			if(returnStr.indexOf("提交成功") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				System.out.println(returnStr + " 手机号：" + moblie );
				return "";
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static void main(String[] args) {
		
//		System.out.println(sendSms("18316449039"));
//		System.out.println(sendSms("13128883598"));
//		System.out.println(sendSms("15889421186"));

		System.out.println(("aa bb").replace(" ",""));

		//String a = sendSmsInTicketAttendMeeting("15889421186","15889421186","xiaoz11","测试资本之道");
		//System.out.println(a);

		/*String s = sendSmsInTicket("15889421186");
		System.out.println(s);*/
		
	}
	
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            System.out.println(e);    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               System.out.println(e);    
            }    
        }    
        return sb1.toString();    
    }

}