package com.xkd.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;


public class HttpRequest{

	public HttpRequest() {
		
	}

	private static HttpRequest request = null;
	
	public static HttpRequest getInstance(){
		if(request == null){
			request = new HttpRequest();
		}
		return request;
	}

	private Map<String, String> requestHeader;

	private Map<String, Object> requestParam;
	
	public static HttpPost requestPost = null;
	
	public static HttpPost getHttpPostInstance(){
		if(requestPost == null){
			requestPost = new HttpPost();
		}
		return requestPost;
	}
	
	public static HttpGet requestGet = null;
	
	public static HttpGet getHttpGettInstance(){
		if(requestGet == null){
			requestGet = new HttpGet();
		}
		return requestGet;
	}
	
	private int CONNECTION_TIMEOUT = 10000;
	
	private int SO_TIMEOUT = 100000;
	
	private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public String getData(String url) throws Exception {
		return this.getData(url, null);
	}

	public String postData(String url) throws Exception {
		return this.postData(url,null);
	}

	public String getData(String url, String characterEncoder) throws Exception {
		HttpGet request = new HttpGet(url);
		try {
			return this.requestData(request, characterEncoder);
		} catch (Exception e) {
			throw e;
		} finally {
//			request.releaseConnection();
		}

	}

	public String postData(String url, String characterEncoder) throws Exception {
		getHttpPostInstance();
		requestPost.setURI(URI.create(url));
		try {
			if (requestParam != null && !requestParam.isEmpty()) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Set<String> paramNames = requestParam.keySet();
				for (String name : paramNames) {
					String value = requestParam.get(name).toString();
					NameValuePair nameValue = new BasicNameValuePair(name, value);
					params.add(nameValue);
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,characterEncoder);
				requestPost.setEntity(entity);
			}
			return this.requestData(requestPost, characterEncoder);
		} catch (Exception e) {
			throw e;
		} finally {
//			requestPost.releaseConnection();
		}
	}
	
	public String postData(String url, String characterEncoder, String obj) throws Exception {
		getHttpPostInstance();
		requestPost.setURI(URI.create(url));
		try {
			StringEntity se = new StringEntity(obj, "UTF-8");
	        se.setContentType(CONTENT_TYPE_TEXT_JSON);
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
	        requestPost.setEntity(se);
	        
			return this.requestData(requestPost, characterEncoder);
		} catch (Exception e) {
			throw e;
		} finally {
//			requestPost.releaseConnection();
		}
	}
	
	public String requestDataByGzip(HttpUriRequest request, String characterEncoder) throws Exception {
		if (requestHeader != null && !requestHeader.isEmpty()) {
			Set<String> headerNames = requestHeader.keySet();
			for (String name : headerNames) {
				String value = requestHeader.get(name);
				request.addHeader(name, value);
			}
		}

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		HttpResponse response = httpclient.execute(request);
		if (characterEncoder != null) {
//			response.addHeader("charset", characterEncoder);
		}
		HttpEntity entity = response.getEntity();
		
		InputStream is = entity.getContent();
		byte[] bytes = new byte[1024000];
		int len = -1;
		int pos = 0;
		while ((len = is.read(bytes, pos, bytes.length - pos)) != -1) {
			pos += len;
		}
		this.detectCharset(bytes);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);  
        
        try {  
            GZIPInputStream ungzip = new GZIPInputStream(in);  
            byte[] buffer = new byte[256];  
            int n;  
            while ((n = ungzip.read(buffer)) >= 0) {  
                out.write(buffer, 0, n);  
            }  
            return out.toString(characterEncoder);  
        } catch (Exception e) {  
            System.out.println(e);  
            return "";  
        }
	}

	public BufferedImage getImage(String url) throws Exception {
		getHttpGettInstance();
		try {
			if (requestHeader != null && !requestHeader.isEmpty()) {
				Set<String> headerNames = requestHeader.keySet();
				for (String name : headerNames) {
					String value = requestHeader.get(name);
					requestGet.addHeader(name, value);
				}
			}
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(requestGet);
			HttpEntity entity = response.getEntity();
			return ImageIO.read(entity.getContent());
		} catch (Exception e) {
			throw e;
		} finally {
//			request.releaseConnection();
		}

	}

	private String requestData(HttpUriRequest request, String characterEncoder) throws Exception {
		if (requestHeader != null && !requestHeader.isEmpty()) {
			Set<String> headerNames = requestHeader.keySet();
			for (String name : headerNames) {
				String value = requestHeader.get(name);
				request.addHeader(name, value);
			}
		}

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
		HttpResponse response = httpclient.execute(request);
		if (characterEncoder != null) {
			response.addHeader("charset", characterEncoder);
		}
		HttpEntity entity = response.getEntity();
		BufferedReader br = null;
		if (characterEncoder != null) {
			br = new BufferedReader(new InputStreamReader((entity.getContent()), characterEncoder));
			StringBuilder sb = new StringBuilder();
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			return sb.toString();
		} else {
			InputStream is = entity.getContent();
			byte[] bytes = new byte[1024000];
			int len = -1;
			int pos = 0;
			while ((len = is.read(bytes, pos, bytes.length - pos)) != -1) {
				pos += len;
			}
			this.detectCharset(bytes);
			if(charset==null){
				return "";
			}
			return new String(bytes, 0, pos,charset);
		}
	}

	private String charset = null;

	private static Pattern CHARSET_PATTERN = Pattern.compile("<meta[^>]+charset[^>]+>");

	private static Pattern CHARSETS = Pattern.compile("(utf-8|utf8|gbk|gb2312)",Pattern.CASE_INSENSITIVE);
	
	private void detectCharset(byte[] content) {
		String html = new String(content);
		Matcher m = CHARSET_PATTERN.matcher(html);
		if(m.find()){
			String meta = html.substring(m.start(),m.end());
			Matcher mc = CHARSETS.matcher(meta);	
			if(mc.find()){
				charset = meta.substring(mc.start(),mc.end());
			}		
		}
	}
	
	public void setRequestParam(Map<String, Object> params) {
		this.requestParam = params;
	}

	public void setRequetHeader(Map<String, String> headers) {
		this.requestHeader = headers;
	}

	
	public void setConnectionTimeout(int timeout) {
		this.CONNECTION_TIMEOUT = timeout;
	}

	public void setSoTimeout(int timeout) {
		this.SO_TIMEOUT = timeout;
	}
}
