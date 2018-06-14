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

import com.google.gson.JsonObject;
import com.xkd.utils.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ImportWinBonsJunshiFile {

	public static String companyName = "";
	public static String tprovince = "";
	public static String tcity = "";
	public static String tarea = "";
	public static String tel = "";
	
	public static void main(String[] args) throws Exception
	   {
		
		try {
			String cookies = "safedog-flow-item=20B48D9E5188F0F171AE3509D447DC4D; Qs_lvt_168355=1504080750%2C1504151893%2C1504258627%2C1504503476%2C1504590342; Qs_pv_168355=546818721722834560%2C146579801303317730%2C1061382752316320800%2C2048571228824012500%2C4357544477847308300; _ga=GA1.2.2044489803.1504080751; _gid=GA1.2.833974872.1504590343; Hm_lvt_e0abef03671730a36ca85f9bf0e54311=1504151893,1504258627,1504503477,1504590342; Hm_lpvt_e0abef03671730a36ca85f9bf0e54311=1504590357; _dbid__=E8BF902F08FF8F49; _cmpid__=C080392F3BA2FFBF8AE48DA5DB539686; JSESSIONID=8FB129938BF7ABF853AFD5FCEBB9E1D4; winbons_login_user=15814085521; micMarketConf=%7B%22showUrl%22%3A%22https%3A//wyx.winbons.com/%22%2C%22editorUrl%22%3A%22https%3A//crm.winbons.com/%22%2C%22qnuUploadUrl%22%3A%22https%3A//dn-openwinbons.qbox.me/%22%2C%22loginUrl%22%3A%22https%3A//pf.winbons.com/%22%2C%22qnToken%22%3A%22/qiniufile/getUploadToken%3FfileAccessType%3Dopen%22%2C%22serverUrl%22%3A%22https%3A//crm.winbons.com/%22%7D";
			String str_param_body = "";

			String JsonContext = ReadFile("E:\\工作资料\\中小\\资本军师客户.txt");
			JSONArray jsonArray = JSONArray.fromObject(JsonContext);
			
			BufferedWriter bufferWritter = null;
			 File file =new File("E:\\工作资料\\中小\\activity.csv");

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
				tprovince = jsonObject.get("tprovince")+"";
				tcity = jsonObject.get("tcity")+"";
				tarea = jsonObject.get("tarea")+"";
				tel = jsonObject.get("tel")+"";
				//获取客户所有文档
//				sendPostUrl("https://crm.winbons.com/customer/loadFeeds?id="+jsonObject.get("id")+"&CONSTANT_OWNERID=100000396081", cookies, "");
//				sendPostUrl("https://crm.winbons.com/customer/loadFeeds?id="+jsonObject.get("id")+"&CONSTANT_OWNERID=100000396081", cookies, "");
				String data = sendPostUrl("https://crm.winbons.com/customer/loadFeeds?id="+jsonObject.get("id")+"&CONSTANT_OWNERID=100000396081", cookies, "");
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
	private static String sendPostUrl(String url2, String cookies, String str_param_body) {
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
			System.out.println(result);
			JSONObject jsonArray = JSONObject.fromObject(result);
			JSONObject datas = jsonArray.getJSONObject("data");
			
			JSONArray data = datas.getJSONArray("items");
			String str = null;
			int size = data.size();
			for(int  i = 0; i < size; i++){

				String action = null;
				String actionValue = null;
				JSONObject jsonObject = data.getJSONObject(i);
				if(jsonObject.containsKey("stream")){
					JSONObject stream = jsonObject.getJSONObject("stream");
					action = stream.getString("action");
					actionValue = stream.getString("actionValue");
				}else if(jsonObject.containsKey("content")){
					
					JSONObject content = jsonObject.getJSONObject("content");
					action = "跟进";
					actionValue = content.getString("content");
				}
				String createBy = null;
				if("100000184686".equals(jsonObject.getString("createBy"))){
					createBy = "马东杰";
				}else if("100000184687".equals(jsonObject.getString("createBy"))){
					createBy = "段铖";
					
				}else if("100000202739".equals(jsonObject.getString("createBy"))){
					createBy = "刘华玉";
					
				}else{
					createBy = "--";
				}
				str += companyName  + "," + tprovince + "," + tcity + "," + tarea + "," + tel + "," + createBy + "," + jsonObject.get("createDate") + ","+ action+"\n";
//				str += companyName  + "," + ""+jsonObject.get("createBy") + "," + jsonObject.get("createDate") + ","+ action+ ","+ actionValue+"\n";
				System.out.println(str);
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
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
