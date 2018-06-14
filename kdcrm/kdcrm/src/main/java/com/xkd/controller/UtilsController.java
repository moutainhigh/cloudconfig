package com.xkd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xkd.model.TemplateData;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.HttpRequest;
import com.xkd.utils.SmsApi;


/**
 * 蝌蚪智慧所有工具类接口集合
 * @author fangsj
 *
 */
@Controller
@RequestMapping("/utils")
public class UtilsController  extends BaseController{
	
	static final String SmsKey = "7ee46532fc4a44b27ef633093fd5b9f3";

	/**
	 * 发送短信
	 * key 密钥
	 * tel 手机号
	 * content 短信内容
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendSmsByContent")
	public ResponseDbCenter userRegSendSms(HttpServletRequest req){
		//返回数据总对象，0000表示成功，其它都表示错误
		ResponseDbCenter res = new ResponseDbCenter();
		
		String key = req.getParameter("key");
		if(!SmsKey.equals(key)){
			return ResponseConstants.PERMITTION_DENIE;
		}
		String tel = req.getParameter("tel");
		String content = req.getParameter("content");
		
		if(StringUtils.isBlank(tel)){
			return ResponseConstants.MISSING_PARAMTER_WJ;
		}
		
		String code = SmsApi.sendSmsByContent(tel, content);
		req.getSession().setAttribute("code"+tel, code);
		System.out.println("发送短信code = "+req.getSession().getAttribute("code"+tel));
	    
		return res;
	}
	@ResponseBody
	@RequestMapping("/sendMesses")
	public String sendMesses(HttpServletRequest req){
			HttpRequest request = new HttpRequest();
		
			
			
			/*XcxMessage data = new XcxMessage();
			data.setTouser("oQbL00FXR1RKQf4ZC545tTfpbQS0");
			data.setTemplate_id("naXzcOq40oUOqE3M-vNqGF0847Mu4EFPPC5K9t3flKk");
			data.setForm_id("FORMID");*/
			
			System.out.println("------------"+req.getParameter("formId"));
			Map<String, Object> param = new HashMap<>();
			param.put("touser", "oQbL00KK-NtV710GCsSPtbrT9aWc");//oQbL00OVQd2feYMDYe1xaOEoJcjw
			param.put("template_id", "naXzcOq40oUOqE3M-vNqGF0847Mu4EFPPC5K9t3flKk");
			param.put("page", "pages/index/index");
			//param.put("emphasis_keyword", "keyword1.DATA");
			param.put("form_id", req.getParameter("formId"));
			Map<String, Object> param2 = new HashMap<>();
			
			Map<String, Object> keyword1 = new HashMap<>();
			keyword1.put("value", "339208499");
			keyword1.put("color", "#173177");
			param2.put("keyword1", keyword1);
			
			Map<String, Object> keyword2 = new HashMap<>();
			keyword2.put("value", "339208499");
			keyword2.put("color", "#173177");
			param2.put("keyword2", keyword2);
			
			Map<String, Object> keyword3 = new HashMap<>();
			keyword3.put("value", "339208499");
			keyword3.put("color", "#173177");
			param2.put("keyword3", keyword3);
			
			Map<String, Object> keyword4 = new HashMap<>();
			keyword4.put("value", "339208499");
			keyword4.put("color", "#173177");
			param2.put("keyword4", "1505991393994");
			
			param.put("data", param2);
			
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx36ad2730eaeb3973&secret=45d56ee1626e06c00aefa270307fc4a2";
				//获取access_token
				String result = request.getData(url, "UTF-8");
				System.out.println("sccess_token-------------123--------------------------:"+result);
				Map<String, String> map = (Map<String, String>) JSON.parseObject(result, Object.class);
				
				String access_token = map.get("access_token");
				
				request.setRequestParam(param);
				String data = JSONObject.toJSON(param).toString();

				String url2 = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
				System.out.println(data);
				System.out.println(url2);
//				String obj = JsonObject.toJson(param).toString();
				String obj = request.postData(url2, "UTF-8", data);
				return obj;

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		return "SUCCESS";
	}
	@ResponseBody
	@RequestMapping("/pushMesses")
	public String pushMesses(){


		HttpRequest request = new HttpRequest();
		String wxToken = SysUtils.getWeixinToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+wxToken;
		Map<String, Object> pm = new HashMap<String, Object>();
		pm.put("msgtype", "text");

		Map<String, Object> text = new HashMap<String, Object>();
		text.put("content", "行程名称：啊啊啊啊啊           </br>" +
				"行程时间:2017-07-12 至 2017-08-06");
		pm.put("text", text);

		pm.put("touser", "o2DOPwUMugpWcLs9swzBX6FgEwNk");
		String data = JSONObject.toJSON(pm).toString();
		String result = "fail";
		try {
			result = request.postData(url, "UTF-8", data);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return result;
	}
	private static Map<String,Object> getkfnews() {
		Map<String,String> art1=new HashMap<>();
		art1.put("description","1");
		art1.put("ticurl","http://zz-tf.com:8899/companysLogo/20171121150023_wx-file.jpg");
		art1.put("title","丁丁测试");
		art1.put("url","http://www.baidu.com");

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,Object> news=new HashMap<>();
		list.add(art1);
		news.put("articles",list);

		Map<String,Object> kfbean=new HashMap<>();
		kfbean.put("setMsgtype","news");
		kfbean.put("touser","o2DOPwUMugpWcLs9swzBX6FgEwNk");
		kfbean.put("news",news);
		System.out.println(kfbean.toString());
		return kfbean;
	}
	//  上面的有用到一个调用接口的方法如下：
	public static String kf_news_url= "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	public static String Runkf(Map<String,Object> getkfnews, String token) throws Exception {



		String jsonnews = JSONObject.toJSON(getkfnews).toString();
		HttpRequest request = new HttpRequest();
		String obj = request.postData(kf_news_url+token, "POST", jsonnews);

		return obj;
	}


	@ResponseBody
	@RequestMapping("/pushTuwen")
	public String pushTuwen(HttpServletRequest req){
		try {
			return Runkf(getkfnews(),SysUtils.getWeixinToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getWeixinToken() {
		HttpRequest request = new HttpRequest();
		String access_token = null;
		String params = "appid=wx01994a39ee501dfe&secret=f55b370edd7df855655a3339b22522fa&grant_type=client_credential";
		String url = "https://api.weixin.qq.com/cgi-bin/token?" + params;
		try {
			System.out.println("----------------------------------------------------");
			System.out.println(url);
			String result = request.getData(url, "UTF-8");
			JsonParser parse =new JsonParser();  //创建json解析器
			JsonObject json = (JsonObject) parse.parse(result);  //创建jsonObject对象
			access_token = json.get("access_token").getAsString();
			System.out.println(access_token);
		} catch (Exception e) {
			System.out.println(e);
		}
		return access_token;
	}
	@ResponseBody
	@RequestMapping("/pushTemp")
	public String pushTemp(HttpServletRequest req){
		try {
			String json =  TemplateData.New()
				.setTouser("oom0Aj6Lh2O8m9VRg_HZ19G7Frgk")
				.setTemplate_id("xC3jqb0Ek-g6xRP7o6ZaU4ffMhhDgSSVvfuOvZCvfrE")
				.setTopcolor("#743A3A")
				.setUrl("www.baidu.com")
				.add("first","标题","#743A3A")
					.add("keyword1","内容1","#743A3A")
				.add("keyword2","内容2","#743A3A")
				.add("keyword3","内容3","#743A3A")
				.add("keyword4","内容4","#743A3A")
				.add("remake","详情","#743A3A")
					.build();
			HttpRequest request = new HttpRequest();
			String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getWeixinToken();
			System.out.println(url);
			System.out.println(json);
			String obj = request.postData(url, "UTF-8", json);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
}
