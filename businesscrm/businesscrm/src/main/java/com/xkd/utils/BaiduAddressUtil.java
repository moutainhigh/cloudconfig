package com.xkd.utils;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Map;

 import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.model.AddressData;
import com.xkd.model.LngLatData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 创建人：巫建辉
 * 创建时间：2017-11-23
 * 功能描述  通过调用百度地图接口将地址解析为经纬度，省市区等
 */
public class BaiduAddressUtil {
	Logger log= LoggerFactory.getLogger(BaiduAddressUtil.class);

	public static String sendGet(String url, String host, String cookie) {
		try {

			
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					  .setSocketTimeout(10000)
					  .setConnectTimeout(10000)
					  .setConnectionRequestTimeout(10000)
					  .setStaleConnectionCheckEnabled(true)
					  .build();
			CloseableHttpClient httpclient = HttpClients.createDefault();
			 
 
			URL theUrl = new URL(url);
			URI uri = new URI(theUrl.getProtocol(), theUrl.getHost(), theUrl.getPath(), theUrl.getQuery(), null);

			HttpGet httpGet = new HttpGet(uri);
			httpGet.setConfig(defaultRequestConfig);
			httpGet.addHeader("Cookie", cookie);
			httpGet.setHeader("content-type", "application/json");
			//httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			String result = EntityUtils.toString(response.getEntity());
			response.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}


	/**
	 * 将地址解析为经纬度
	 * @param address 地址 如深圳莲花山
	 * @return
	 */
	public static LngLatData  parseAddressToLngLat(String address){
		String AK=PropertiesUtil.BAIDUMAP_AK;
		 String result= sendGet("http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak="+AK,"",null);	 
			// 将其解析为一个大Map
			Map<String, Object> map = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
			});
			if (0==(Integer)map.get("status")) {
				 LngLatData lngLatData=new LngLatData();
				 BigDecimal lng=(BigDecimal)((Map)((Map)map.get("result")).get("location")).get("lng");
				 BigDecimal lat=(BigDecimal)((Map)((Map)map.get("result")).get("location")).get("lat");
				 lngLatData.setLongitude(lng.toString());
				 lngLatData.setLatitude(lat.toString());
				 return lngLatData;
			}
		 return null;
	}

	/**
	 * 将经纬度解析为格式化的地址
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @return
	 */
	public static AddressData parseLngLatToAddress(String longitude,String latitude){
		 String AK=PropertiesUtil.BAIDUMAP_AK;
		 String result= sendGet("http://api.map.baidu.com/geocoder/v2/?location="+latitude+","+longitude+"&output=json&pois=1&ak="+AK,"",null);
			// 将其解析为一个大Map
			Map<String, Object> map = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
			});
			
			if (0==(Integer)map.get("status")) {
				AddressData addressData=new AddressData();
				Map<String,String> addressComponent=(Map)((Map)map.get("result")).get("addressComponent");
				addressData.setCountry(addressComponent.get("country"));
			    addressData.setCity(addressComponent.get("city"));
			    addressData.setProvince(addressComponent.get("province"));
			    addressData.setCounty(addressComponent.get("district"));
			    return addressData;
			}
			return null;
			
		 
 	}


	/**
	 * 将地址直接转化为格式化地址 如:深圳莲花山 解析为 中国 广东省 深圳市 莲花山
	 * @param address
	 * @return
	 */
	public static  AddressData parseAddressToCountryProvinceCityCounty(String address){
		LngLatData lngLatData=parseAddressToLngLat(address) ;
		if(lngLatData != null){
			return parseLngLatToAddress(lngLatData.getLongitude(), lngLatData.getLatitude());
		}else{
			return null;
		}

	}
	
 	
	public static void main(String[] args) throws Exception {
		System.out.println(JSON.toJSON(parseAddressToCountryProvinceCityCounty("广西")));

	 
	}
}
