package com.kuangchi.sdd.visitorConsole.connCs.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.kuangchi.sdd.util.file.ContinueFTP;
import com.kuangchi.sdd.visitorConsole.connCs.service.ConnCsService;

@Controller("connCsAction")
public class ConnCsAction extends BaseActionSupport{

	@Autowired
	ConnCsService connCsService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 *发卡/补发卡 
	 *by gengji.yang
	 */
	public void makeCard(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
		Map resultMap=new HashMap();
		if(connCsService.makeCard(m)){
			resultMap.put("result", "0");
			resultMap.put("msg", "发卡/补发卡 成功");
		}else{
			resultMap.put("result", "1");
			resultMap.put("msg", "发卡/补发卡 失败");
		}
	printHttpServletResponse(new Gson().toJson(resultMap));
		//暂存在这里的测试代码，方便测试
//  		Map m=new HashMap();
//  		m.put("cardNum","456");
//		m.put("mVisitorNum","924742f1-e2e6-4396-9b8d-100c617274c0");
//		m.put("groupNum","1ad59c3d-b6de-11e6-9b81-0050569f7086");
//		m.put("visitCardType","0");
	}
	
	/**
	 *发卡/补发卡 ,用于与光钥匙对接
	 *by gengji.yang
	 */
	public void makeCard_phone(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
		Map resultMap=new HashMap();
		if(connCsService.makeCard_phone(m)){
			resultMap.put("result", "0");
			resultMap.put("msg", "发卡/补发卡 成功");
		}else{
			resultMap.put("result", "1");
			resultMap.put("msg", "发卡/补发卡 失败");
		}
	printHttpServletResponse(new Gson().toJson(resultMap));
		//暂存在这里的测试代码，方便测试
//  		Map m=new HashMap();
//  		m.put("cardNum","456");
//		m.put("mVisitorNum","924742f1-e2e6-4396-9b8d-100c617274c0");
//		m.put("groupNum","1ad59c3d-b6de-11e6-9b81-0050569f7086");
//		m.put("visitCardType","0");
	}
	
	/**
	 * 卡解挂
	 * by gengji.yang
	 */
	public void makeCardBack(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Map resultMap=new HashMap();
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
//	暂存在这里的测试代码，方便测试	
//		Map m=new HashMap();
//  		m.put("cardNum","456");
//		m.put("mVisitorNum","d88e583d-5d4c-4be2-9c99-90309fb442b8");
		try{
			connCsService.makeCardBack(m);
			resultMap.put("result", "0");
			resultMap.put("msg", "卡解挂 成功");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("result", "1");
			resultMap.put("msg", "卡解挂 失败");
		}
	printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 卡挂失
	 * 并删除权限
	 * by gengji.yang
	 */
	public void makeCardLost(){
		HttpServletRequest request=getHttpServletRequest();
		Map resultMap=new HashMap();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
//	暂存在这里的测试代码，方便测试
//		Map m=new HashMap();
//  		m.put("cardNum","456");
//		m.put("mVisitorNum","924742f1-e2e6-4396-9b8d-100c617274c0");
		
		if(connCsService.makeCardLost(m)){
			resultMap.put("result", "0");
			resultMap.put("msg", "卡挂失 成功");
		}else{
			resultMap.put("result", "1");
			resultMap.put("msg", "卡挂失 失败");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 获取卡号
	 * by gengji.yang
	 */
	public void getNewCardNum(){
		HttpServletRequest request=getHttpServletRequest();
		Map resultMap=new HashMap();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
//		Map m=new HashMap();
//		m.put("mVisitorNum", "1");
//		m.put("visitCardType", "0");
		String cardNum=connCsService.getNewCardNum(m);
		resultMap.put("cardNum",cardNum);
		if(cardNum==null){
			resultMap.put("flag", "1");
			resultMap.put("msg", "获取卡号失败，卡号已满");
		}else{
			resultMap.put("flag", "0");
			resultMap.put("msg", "获取卡号成功");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 超时后的离开登记
  	 * lostFlag:卡挂失标记位，0：挂失 1：不挂失
	 * blackFlag:加入黑名单标记位 0：加入 1：不加入
	 * by gengji.yang
	 */
	public void overTimeLeave(){
		HttpServletRequest request=getHttpServletRequest();
		Map resultMap=new HashMap();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
//		Map m=new HashMap();
//		m.put("mVisitorNum", "1");
//		m.put("cardNum", "56572");
//		m.put("lostFlag", "0");
//		m.put("blackFlag", "0");
		boolean flag=connCsService.overTimeLeave(m);
		if(flag==true){
			resultMap.put("flag", "0");
		}else{
			resultMap.put("flag", "1");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 返回请求url的固定前缀
	 * eg：http://192.168.10.51:8090/photoncard/interface/
	 * by gengji.yang
	 */
	public void getUrl(){
		HttpServletRequest request=getHttpServletRequest();
		Map resultMap=new HashMap();
		try {
			InetAddress addr=InetAddress.getLocalHost();
			String url="http://"+addr.getHostAddress()+":"+request.getLocalPort()+request.getContextPath()+"/interface/";
			resultMap.put("url", url);
			resultMap.put("flag", "0");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			resultMap.put("flag", "1");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 预约的访客到访登记后，更改状态为 正在访问
	 * by gengji.yang
	 */
	public void makeVisitorVisiting(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("mVisitorNum");
		Map m=new HashMap();
		m.put("mVisitorNum", data);
		connCsService.makeVisitorVisiting(m);
	}
	
	/**
	 * 回收卡片
	 * 卡号可以再次使用，删除卡权限
	 * by gengji.yang
	 */
	public void recycleCard(){
		HttpServletRequest request=getHttpServletRequest();
		Map resultMap=new HashMap();
		String data=request.getParameter("cardNum");
		Map m=new HashMap();
		m.put("cardNum", data);
//		Map m=new HashMap();
//		m.put("cardNum","32779");
		try{
			connCsService.recycleCard(m);
			resultMap.put("result", "0");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("result", "1");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 上传图片
	 * by gengji.yang
	 * 
	 */
	public void uploadImg() {
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map m=gson.fromJson(data, HashMap.class);
		String fileName=m.get("fileName").toString();
		String imgStr=m.get("imgStr").toString();
		Map resultMap =new HashMap();
		ContinueFTP continueFTP = new ContinueFTP();
		String ftpPropertiesPath = request
				.getSession()
				.getServletContext()
				.getRealPath(
						"/" + "WEB-INF" + File.separator
								+ "classes" + File.separator + "conf"
								+ File.separator + "properties"
								+ File.separator + "ftp.properties");
		com.kuangchi.sdd.util.file.FTP ftp = com.kuangchi.sdd.util.file.FTPUtil
				.getFtp(ftpPropertiesPath);
		OutputStream out=null;
		if(null!=imgStr&&!"".equals(imgStr)&&null!=fileName&&!"".equals(fileName)){
			try {
				File file=new File(fileName);
				if(file!=null){
		            PrintStream ps = new PrintStream(new FileOutputStream(file),true,"UTF-8");
		            ps.println(imgStr);// 往文件里写入字符串
				/*	out=new FileOutputStream(file);
					out.w;
					out.flush();
					out.close();*/
					continueFTP.connect(ftp.getHost(), ftp.getPort(),
							ftp.getUserName(), ftp.getPassword());
					continueFTP.upload(file, fileName);
					resultMap.put("result","0");
				}else{
					resultMap.put("result","1");
				}
			} catch (IOException e) {
				e.printStackTrace();
				resultMap.put("result","1");
			}finally{
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			resultMap.put("result","1");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}

	/**
	 * 获取图片
	 * by gengji.yang
	 */
	public void getImg() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		Map resultMap = new HashMap();
		String fileName = request.getParameter("fileName");

		ContinueFTP continueFTP = new ContinueFTP();
		java.io.InputStream is = null;
		try {

			String ftpPropertiesPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/" + "WEB-INF" + File.separator
									+ "classes" + File.separator + "conf"
									+ File.separator + "properties"
									+ File.separator + "ftp.properties");
			com.kuangchi.sdd.util.file.FTP ftp = com.kuangchi.sdd.util.file.FTPUtil
					.getFtp(ftpPropertiesPath);
			boolean getConnection=true;
            try {
    			continueFTP.connect(ftp.getHost(), ftp.getPort(),
    					ftp.getUserName(), ftp.getPassword());
			} catch (Exception e) {
				 getConnection=false;
			}
            Integer flag=null;
			if (getConnection&&null != fileName && !fileName.trim().equals("")
					&& !"undefined".equals(fileName)) {
				is = continueFTP.download(fileName);
				flag=0;
			} else {
				flag=1;
		/*		// 如果没有上传头像，则给一个默认头像
				String defaultImg = request
						.getSession()
						.getServletContext()
						.getRealPath(
								File.separator + "businessConsole"
										+ File.separator + "images"
										+ File.separator + "defaultImg.jpg");
				File file = new File(defaultImg);
				is = new FileInputStream(file);*/
			}
			if(flag==0){
				String isStr=IOUtils.toString(is);
				String returnStr=isStr.substring(0,isStr.indexOf("]")+1);
				resultMap.put("result", "0");
				resultMap.put("imgStr", returnStr);
			}else{
				resultMap.put("result", "1");
			}
			continueFTP.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "1");
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	/**
	 * 返回服务器时间
	 * by gengji.yang
	 */
	public void getServcieTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		String timeStr=sdf.format(now);
		printHttpServletResponse(new Gson().toJson(timeStr));
	}
	
	/**
	 * 获取小区号
	 * by gengji.yang
	 */
	public void getAreaNum(){
		Map<String, String> map = PropertiesToMap.propertyToMap("damon_thread_setting.properties");
		Map resultMap=new HashMap();
		resultMap.put("areaNum",map.get("areaNum"));
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
		
	/**
	 * 发布版本号和版本发布时间
	 * by gengji.yang
	 */
	public void getSystemVersionAndTime(){
		Map<String, String> map = PropertiesToMap.propertyToMap("damon_thread_setting.properties");
		Map resultMap=new HashMap();
		resultMap.put("systemVersion",map.get("systemVersion"));
		resultMap.put("publicTime",map.get("publicTime"));
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	

}
