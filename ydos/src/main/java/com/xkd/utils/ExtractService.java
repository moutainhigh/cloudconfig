package com.xkd.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractService {

	/** 
     * @param rule 
     * @return 
     */  
    public static List<LinkTypeData> extract(Rule rule)  
    {  
  
        // 进行对rule的必要校验  
        validateRule(rule);  
  
        List<LinkTypeData> datas = new ArrayList<LinkTypeData>();  
        LinkTypeData data = null;  
        try  
        {  
            /** 
             * 解析rule 
             */  
            String url = rule.getUrl();  
            String[] params = rule.getParams();  
            String[] values = rule.getValues();  
            String resultTagName = rule.getResultTagName();  
            int type = rule.getType();  
            int requestType = rule.getRequestMoethod();  
  
            /*
             * /*
	         * .ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").timeout(5000).get()
	         *
             */
            Connection conn = Jsoup.connect(url).ignoreContentType(true);  
            // 设置查询参数  
  
            if (params != null)  
            {  
                for (int i = 0; i < params.length; i++)  
                {  
                    conn.data(params[i], values[i]);  
                }  
            }  
  
            // 设置请求类型  
            Document doc = null;  
            switch (requestType)  
            {  
            case Rule.GET:  
                doc = conn.timeout(100000).get();  
                break;  
            case Rule.POST:  
                doc = conn.timeout(100000).post();  
                break;  
            }  
  
//          //eg1:解析百度音乐 
//          Document doc = Jsoup.connect("http://list.mp3.baidu.com/top/singer/A.html").get(); 
//          Element singerListDiv = doc.getElementsByAttributeValue("class", "content").first(); 
//          Elements links = singerListDiv.getElementsByTag("a"); 
//           
//          for (Element link: links) { 
//              String linkHref = link.attr("href"); 
//              String linkText = link.text().trim(); 
//              System.out.println(linkHref);  
//          } 
//           
//           
//          //eg2:解析万年历 
//          Document doc = Jsoup.connect("http://www.nongli.com/item4/index.asp?dt=2012-03-03").get(); 
//          Element infoTable = doc.getElementsByAttributeValue("class", "table002").first(); 
//          Elements tableLineInfos = infoTable.select("tr"); 
//          for (Element lineInfo : tableLineInfos) { 
//              String lineInfoContent = lineInfo.select("td").last().text().trim(); 
//              System.out.println("jsoup is :" + lineInfoContent); 
//          } 
             
             
            //eg3:解析指定段落的内容----注意此代码中的语法：<div class="artHead"> 
//          Document doc = Jsoup.connect("http://passover.blog.51cto.com").get(); 
//          Elements divs = doc.select("div.artHead"); 
//          for (Element div: divs) { 
//              System.out.println(div.select("h3[class=artTitle]")); 
//          } 
             
            //eg4： 
//          Document doc = Jsoup.connect("http://passover.blog.51cto.com").get(); 
             
             
             
//          //eg5:查找html元素 
//            File input = new File("/tmp/input.html"); 
//            Document doc = Jsoup.parse(input, "UTF-8", "http://www.oschina.net/"); 
//            Elements links = doc.select("a[href]"); // 链接 
//            Elements pngs = doc.select("img[src$=.png]"); // 所有 png 的图片 
//            Element masthead = doc.select("div.masthead").first();// div with class=masthead 
//            Elements resultLinks = doc.select("h3.r > a"); // direct a after h3 
            
            
            
            
            
            //处理返回数据  
            Elements results = new Elements();  
//            switch (type)  
//            {  
//            case Rule.CLASS:  
//                results = doc.getElementsByClass(resultTagName);  
//                break;  
//            case Rule.ID:  
//                Element result = doc.getElementById(resultTagName);  
//                results.add(result);  
//                break;  
//            case Rule.SELECTION:  
//                results = doc.select(resultTagName);  
//                break;  
//            default:  
//                //当resultTagName为空时默认去body标签  
//                if (StringUtils.isBlank(resultTagName))  
//                {  
                    results = doc.getElementsByAttributeValue("class", "list-main-normal");  
//                }  
//            }  
  
//             <ul class="list-main-normal">
                   
            
//                Elements links = result.getElementsByTag("a");  
  
                
                for (Element link : results)  
                {  
                	
                	Elements elements = link.getElementsByTag("a");
                	
                	for(Element e : elements){
                		//必要的筛选  
                        String linkHref = e.attr("href");  
                        String linkText = e.text();
                        
                        String[] ls = new String[2];
                        
                        if(StringUtils.isNotBlank(linkText)){
                        	
                        	ls = linkText.split(" ");
                        	
                        }
                        
                        data = new LinkTypeData();  
                        data.setLinkHref(linkHref);  
                        data.setLinkText(ls[0]);  
      
                        datas.add(data);  
                	}
                }  
  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
  
        return datas;  
    }  
  
    /** 
     * 对传入的参数进行必要的校验 
     */  
    private static void validateRule(Rule rule)  
    {  
        String url = rule.getUrl();  
        if (StringUtils.isBlank(url))  
        {  
            throw new RuleException("url不能为空！");  
        }  
        if (!url.startsWith("http://"))  
        {  
            throw new RuleException("url的格式不正确！");  
        }  
  
        if (rule.getParams() != null && rule.getValues() != null)  
        {  
            if (rule.getParams().length != rule.getValues().length)  
            {  
                throw new RuleException("参数的键值对个数不匹配！");  
            }  
        }  
  
    }  
  
}
