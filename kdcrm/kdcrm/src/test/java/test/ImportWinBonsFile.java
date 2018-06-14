package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.xkd.utils.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ImportWinBonsFile {

	public static String companyName = "";
	
	public static void main(String[] args) throws Exception
	   {
		
		try {
			String httpUrl = "https://dn-openwinbons.qbox.me/1008398/Document/C79B6CDC-5EF0-0001-9894-1BA01FC013E0.doc?attname=%E7%BB%84%E7%BB%87%E6%9E%B6%E6%9E%8420160624%E6%96%B0.doc";
			String cookies = "safedog-flow-item=3BFCADFEB3B3E5AE41A2529249D9240C; Qs_lvt_168355=1501500026%2C1501634419%2C1501725507; Qs_pv_168355=3677605065913076700%2C299655171555644000%2C234927001515507260%2C3795037067380696000; _ga=GA1.2.2011300714.1501500027; _gid=GA1.2.251939586.1501725508; Hm_lvt_e0abef03671730a36ca85f9bf0e54311=1501500027,1501634419,1501725508,1501730859; Hm_lpvt_e0abef03671730a36ca85f9bf0e54311=1501731319; _dbid__=E8BF902F08FF8F49; _cmpid__=C080392F3BA2FFBF8AE48DA5DB539686; JSESSIONID=78FB6A043C8D6B59386AFF55CCBBAF7A; winbons_login_user=15814085521";
			String str_param_body = "";

			String JsonContext = ReadFile("E:\\资本军师客户.txt");
			System.out.println(JsonContext);
			JSONArray jsonArray = JSONArray.fromObject(JsonContext);
			
			BufferedWriter bufferWritter = null;
			 File file =new File("javaio-appendfile.txt");

		      //if file doesnt exists, then create it
		      if(!file.exists()){
		       file.createNewFile();
		      }

		      //true = append file
		      FileWriter fileWritter = new FileWriter(file.getName(),true);
		      bufferWritter = new BufferedWriter(fileWritter);
		      
			int size = jsonArray.size();
			for(int  i = 0; i < size; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				companyName = jsonObject.get("name")+"";
				//获取客户所有文档
//				sendPostUrl("https://crm.winbons.com/document/getList?itemid="+jsonObject.get("id"), cookies, "");
				//获取客户下所有动态情况
//				String data = sendPostUrlMessageInfo("https://crm.winbons.com/customer/loadFeeds?lastIndex=-1&CONSTANT_OWNERID=100000396081&id=100840491275", cookies, "");
				String data = sendPostUrlMessageInfo("https://crm.winbons.com/customer/loadFeeds?lastIndex=-1&CONSTANT_OWNERID=100000396081&id="+jsonObject.get("id"), cookies, "");
				bufferWritter.write(data);
			}
				
			bufferWritter.close();
         
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	      
	   }

	/**
	 * 获取文档链接
	 * @param url2
	 * @param cookies
	 * @param str_param_body
	 */
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
			String result = request.requestDataByGzip(new HttpGet(url2), "UTF-8");
			JSONObject jsonArray = JSONObject.fromObject(result);
			JSONArray data = jsonArray.getJSONArray("data");
			int size = data.size();
			for(int  i = 0; i < size; i++){

				JSONObject jsonObject = data.getJSONObject(i);
				System.out.println(companyName  + "       " + jsonObject.get("name") + "    https://dn-openwinbons.qbox.me/" + jsonObject.get("mongodbFileId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有企业动态信息
	 * @param url2
	 * @param cookies
	 * @param str_param_body
	 */
	private static String sendPostUrlMessageInfo(String url2, String cookies, String str_param_body) {
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
		String strs = null;
		try {
			
		
			request.setRequetHeader(headers);
			request.setRequestParam(params);
			String result = request.requestDataByGzip(new HttpGet(url2), "UTF-8");
			JSONObject jsonArray = JSONObject.fromObject(result);
			JSONObject objData = jsonArray.getJSONObject("data");
			JSONArray data = objData.getJSONArray("items");
			int size = data.size();
			for(int i = 0; i < size; i++){
				
				String actionValue = "";
				String contentValue = "";
				JSONObject jsonObject = data.getJSONObject(i);
				if(jsonObject.has("stream")){
					JSONObject stream = (JSONObject) jsonObject.get("stream");
					actionValue = stream.getString("actionValue");
				}
				if(jsonObject.has("content")){
					JSONObject content = (JSONObject) jsonObject.get("content");
					contentValue = content.getString("content");
				}
				String groupName = "";
				if(jsonObject.containsKey("groupName")){
					groupName = jsonObject.getString("groupName");
				}
				strs += (actionValue + "    :contentValue:    " + contentValue + "    :groupId:    " + jsonObject.getString("groupId")
				+ "    :groupName:    " + groupName
				+ "    :createBy:    " + jsonObject.getString("createBy") + "    :createDate:    " + jsonObject.getString("createDate")+"\n");
			}
			return strs;
		} catch (Exception e) {
			e.printStackTrace();
			return strs;
		}
	}
	
    
    public static String ReadFile(String Path){
    	BufferedReader reader = null;
    	String laststr = "";
    	try{
    	FileInputStream fileInputStream = new FileInputStream(Path);
    	InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
    	reader = new BufferedReader(inputStreamReader);
    	String tempString = null;
    	while((tempString = reader.readLine()) != null){
    	laststr += tempString;
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
    	return laststr;
    	}

    
}
