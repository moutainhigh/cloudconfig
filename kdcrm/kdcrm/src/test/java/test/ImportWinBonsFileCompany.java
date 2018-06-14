package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.xkd.utils.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ImportWinBonsFileCompany {

	public static String companyName = "";
	
	public static void main(String[] args) throws Exception
	   {
		
		try {
			String httpUrl = "https://www.tianyancha.com/company/companyholder.html?id=500604074&ps=10&_=1501586098263";
			String cookies = "TYCID=74e40f4075d011e7becf8f4cafed53bd; uccid=3f642086811a324c8e8c72466214fae2; ssuid=8474978384; tyc-user-info=%257B%2522token%2522%253A%2522eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzEyODg4MzU5OCIsImlhdCI6MTUwMTQ5MjQ0NiwiZXhwIjoxNTE3MDQ0NDQ2fQ.VGhJ7yPa2Rr8CFMSFt37MAWmuMh8zDbSHxRaK1cp-CfRRBTMaUXuZlupT5Vwb9zPOiQ_0oNHH2LE_xghCq_NQg%2522%252C%2522integrity%2522%253A%25220%2525%2522%252C%2522state%2522%253A%25220%2522%252C%2522vnum%2522%253A%25220%2522%252C%2522onum%2522%253A%25220%2522%252C%2522mobile%2522%253A%252213128883598%2522%257D; auth_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzEyODg4MzU5OCIsImlhdCI6MTUwMTQ5MjQ0NiwiZXhwIjoxNTE3MDQ0NDQ2fQ.VGhJ7yPa2Rr8CFMSFt37MAWmuMh8zDbSHxRaK1cp-CfRRBTMaUXuZlupT5Vwb9zPOiQ_0oNHH2LE_xghCq_NQg; aliyungf_tc=AQAAAGvGi1F/cgEAUzsR2p4yAfpPxy0+; csrfToken=JFIrpu6CrVRZU8D99dAFEYaX; OA=l/Z5NdGIHeZEngrVvNGx/LvOekaCSB5DLDXU8zPlv15R8XjS9W8XmwumHusqepPR; _csrf=X2zY19/xgo1pKpAFwkTk4Q==; _csrf_bk=b16e2472314576e40164ada7f6e3b616; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1501492375,1501586083; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1501586098";
			String str_param_body = "";

			BufferedReader reader = null;
	    	String laststr = "";
	    	try{
	    	FileInputStream fileInputStream = new FileInputStream("E:\\工作资料\\中小\\文档数据.txt");
	    	InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
	    	reader = new BufferedReader(inputStreamReader);
	    	String tempString = null;
	    	while((tempString = reader.readLine()) != null){
	    		String[] strs = tempString.split("    ");
	    		System.out.println(strs[2]+"?attname="+strs[1].trim());
//	    		sendPostUrl(strs[2]+"?attname="+strs[1].trim(), cookies, "");
//		    	laststr += tempString;
//		    	return;
	    	}
	    	reader.close();
	    	}catch(IOException e){
	    	e.printStackTrace();
	    	}finally{
	    	if(reader != null){
	    	try {
	    	reader.close();
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
	    	}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	      
	   }

	private static void sendPostUrl(String url2, String cookies, String str_param_body) {
    	HttpRequest request = new HttpRequest();
    	Map<String, String> headers = new HashMap<>();
    	headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
    	headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    	headers.put("curpage", "1");
    	headers.put("pageSize", "500");
    	headers.put("selectType", "0");
    	headers.put("token", "bdb5a265-fb07-4fd0-916d-53d27af09f7c");
    	headers.put("CONSTANT_OWNERID", "100000396081");
    	headers.put("Cookie", cookies);
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
    	params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    	params.put("curpage", "1");
    	params.put("pageSize", "500");
    	params.put("selectType", "0");
    	params.put("token", "bdb5a265-fb07-4fd0-916d-53d27af09f7c");
    	params.put("CONSTANT_OWNERID", "100000396081");
    	params.put("Cookie", cookies);
		try {
			request.setRequetHeader(headers);
			request.setRequestParam(params);
			String result = request.getData(url2, "UTF-8");
			JSONObject jsonArray = JSONObject.fromObject(result);
			JSONArray data = jsonArray.getJSONArray("data");
			int size = data.size();
			for(int  i = 0; i < size; i++){
				JSONObject jsonObject = data.getJSONObject(i);
				System.out.println(companyName  + "       " + jsonObject.get("name") + "    https://dn-openwinbons.qbox.me/" + jsonObject.get("mongodbFileId"));
				
//				sendPostUrl("https://crm.winbons.com/document/getList?itemid="+jsonObject.get("name"), cookies, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
    
}
