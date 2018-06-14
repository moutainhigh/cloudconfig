package com.xkd.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import okhttp3.*;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.*;

public class HttpRequestUtil {
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");




    public static String sendPut(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        try {
            OkHttpClient client = new OkHttpClient();

            Set<String> formKeySet = paramMap.keySet();
            Iterator<String> formKeyIterator = formKeySet.iterator();

            FormBody.Builder formBuilder = new FormBody.Builder();
            while (formKeyIterator.hasNext()) {
                String key = formKeyIterator.next();
                if (paramMap.get(key)!=null) {
                    formBuilder = formBuilder.add(key, paramMap.get(key));
                }
            }
            FormBody formBody = formBuilder.build();




            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            if(headerMap != null && headerMap.size() > 0){
                Set<String> requestKeySet = headerMap.keySet();
                Iterator<String> requestKeyIterator = requestKeySet.iterator();
                while (requestKeyIterator.hasNext()) {
                    String key = requestKeyIterator.next();
                    if (headerMap.get(key)!=null) {
                        requestBuilder = requestBuilder.addHeader(key, headerMap.get(key));
                    }
                }
            }

            Request request = requestBuilder.put(formBody).build();


            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "-1";
            }

            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "-1";
    }





    public static String sendPutRequestBody(String url, Map<String, String> headerMap, String content ) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody= RequestBody.create(JSON, content) ;
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Set<String> requestKeySet = headerMap.keySet();
            Iterator<String> requestKeyIterator = requestKeySet.iterator();
            while (requestKeyIterator.hasNext()) {
                String key = requestKeyIterator.next();
                if (headerMap.get(key)!=null) {
                    requestBuilder = requestBuilder.addHeader(key, headerMap.get(key));
                }
            }

            Request request = requestBuilder.put(requestBody).build();


            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println(new String( response.body().bytes()));
                return "-1";
            }

            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "-1";
    }

    public static String sendGet(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        try {
            OkHttpClient client = new OkHttpClient();

            Set<String> formKeySet = paramMap.keySet();
            Iterator<String> paramKeyIterator = formKeySet.iterator();
             StringBuilder stringBuilder=new StringBuilder(url);
             stringBuilder.append("?10000=10000");

             while (paramKeyIterator.hasNext()) {
                String key = paramKeyIterator.next();
                stringBuilder.append("&").append(key).append("=").append(paramMap.get(key));
            }

            url=stringBuilder.toString();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Set<String> requestKeySet = headerMap.keySet();
            Iterator<String> requestKeyIterator = requestKeySet.iterator();
            while (requestKeyIterator.hasNext()) {
                String key = requestKeyIterator.next();
                if (headerMap.get(key)!=null) {
                    requestBuilder = requestBuilder.addHeader(key, headerMap.get(key));
                }
            }

            Request request = requestBuilder.get().build();


            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return "-1";
            }

            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "-1";
    }


    public static String sendPost(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        try {
            OkHttpClient client = new OkHttpClient();

            Set<String> formKeySet = paramMap.keySet();
            Iterator<String> formKeyIterator = formKeySet.iterator();

            FormBody.Builder formBuilder = new FormBody.Builder();
            while (formKeyIterator.hasNext()) {
                String key = formKeyIterator.next();
                if (paramMap.get(key)!=null) {
                    formBuilder = formBuilder.add(key, paramMap.get(key));
                }
            }
            FormBody formBody = formBuilder.build();






            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Set<String> requestKeySet = headerMap.keySet();
            Iterator<String> requestKeyIterator = requestKeySet.iterator();
            while (requestKeyIterator.hasNext()) {
                String key = requestKeyIterator.next();
                if (headerMap.get(key)!=null) {
                    requestBuilder = requestBuilder.addHeader(key, headerMap.get(key));
                }
            }

            Request request = requestBuilder.post(formBody).build();


            Response response = client.newCall(request).execute();
             if (!response.isSuccessful()) {
               return "-1";
            }

         return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "-1";
    }




    public static String sendPostRequestBody(String url, Map<String, String> headerMap, String content ) {
        try {
            OkHttpClient client = new OkHttpClient();

              RequestBody requestBody= RequestBody.create(JSON,content ) ;
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Set<String> requestKeySet = headerMap.keySet();
            Iterator<String> requestKeyIterator = requestKeySet.iterator();
            while (requestKeyIterator.hasNext()) {
                String key = requestKeyIterator.next();
                if (headerMap.get(key)!=null) {
                    requestBuilder = requestBuilder.addHeader(key, headerMap.get(key));
                }
            }

            Request request = requestBuilder.post(requestBody).build();


            Response response = client.newCall(request).execute();
             if (!response.isSuccessful()) {
                System.out.println(new String( response.body().bytes()));
                return "-1";
            }

            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "-1";
    }



    public static void main(String[] args) throws IOException {

//
//        Map<String,String>  map=new HashMap<String, String>();
//
//        map.put("box_id", "04B0TEST55");
//        map.put("con_type", "threshold");
//        map.put("key", "temp_ha");
//        map.put("value", "20");
//
//        Map<String,Object>  map2=new HashMap<String, Object>();
//
//        List<String> boxIdList=new ArrayList<>();
//        boxIdList.add("06DHHDL040");
//        map2.put("box_id_list",boxIdList);
//
//        List<String> userIdList=new ArrayList<>();
//        userIdList.add("123456");
//        map2.put("user_id_list", userIdList);
//        map2.put("is_ctrl", "0");
//        map2.put("receive_level", "0");
//
//        System.out.println(com.alibaba.fastjson.JSON.toJSONString(map2));
//
//
//
////        System.out.println(sendPost("http://localhost:8080/ydos/oldApi/api/thdev/cconfig/", new HashMap<String, String>(), map));
//
//        System.out.println(sendPostRequestBody("http://localhost:8080/ydos/oldApi/api/crm/BatchShareDevice/",new HashMap<>(), com.alibaba.fastjson.JSON.toJSONString(map2)));







//        Map<String,String> paramMap=new HashMap<>();
////        paramMap.put("box_id",null);
//        paramMap.put("protocol","P001-D001-B0004-S001");
//        String result= HttpRequestUtil.sendGet( "http://192.168.1.6:8001/api/crm/GetProtocolPic/", new HashMap<>(), paramMap);
//        System.out.println(result);


//        System.out.println(HttpRequestUtil);
    }
}
