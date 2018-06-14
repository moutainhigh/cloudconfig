package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;

import com.xkd.utils.HttpRequest;

public class ImportWinBonsData {

	public static void main(String[] args) throws Exception
	   {
		
		try {
			String httpUrl = "https://crm.winbons.com/document/getList?itemid=100840446766";
			String cookies = "Qs_lvt_168355=1501500026; Qs_pv_168355=3677605065913076700; JSESSIONID=531BD20CE1147B293C4525C5624CF55A; Hm_lvt_e0abef03671730a36ca85f9bf0e54311=1501500027; Hm_lpvt_e0abef03671730a36ca85f9bf0e54311=1501571574; _ga=GA1.2.2011300714.1501500027; _gid=GA1.2.2102018385.1501500027; _gat=1; _dbid__=E8BF902F08FF8F49; _cmpid__=C080392F3BA2FFBF8AE48DA5DB539686; winbons_login_user=15814085521";
			String str_param_body = "";

			sendPostUrl(httpUrl, cookies, str_param_body);
         
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
//			String result = request.getData(url2, "UTF-8");
			String result = request.requestDataByGzip(new HttpGet(url2), "UTF-8");
			System.out.println("result -------  "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String cookieVal=""  ;
}
