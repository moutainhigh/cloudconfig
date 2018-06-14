package com.xkd.controller;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Controller
@RequestMapping("/file")
public class PictureController  extends BaseController{
	

	
	
    private static final String UPLOAD_DIRECTORY = "crm_fileUpload";
    
	@ResponseBody
	@RequestMapping(value="/uploadPicture",method = { RequestMethod.POST })  
	public String uploadPicture(@RequestParam(value = "imgFile", required = false) MultipartFile[] imgFile , HttpServletRequest request) {  
		  
		
		String token  = request.getParameter("token");
		
		if(StringUtils.isBlank(token)){
			//富文本编辑器的格式，要这样返回
			return "{\"error\":1,\"message\":\"token令牌不能为空\",\"url\":\"\"}";
		}
		
		String userId = null;
		
		try {
			
			userId = request.getSession().getAttribute(token).toString();
			
		} catch (Exception e) {
			
			System.out.println(e);
			return "{\"error\":2,\"message\":\"根据token令牌获取用户ID失败\",\"url\":\"\"}";
		}
		
        if(StringUtils.isBlank(userId)){
        	
        	return "{\"error\":3,\"message\":\"redis中的用户ID不能为空\",\"url\":\"\"}";
        }
		
//		  String uploadPath = "/usr/local" + File.separator + UPLOAD_DIRECTORY + File.separator + userId ;
		  String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + userId ;
		
//		  String path = "E:/";//上传的目录  简单点 这里是E盘根目录  根据自己的需求改  想把上传的文件放在哪里  路径就写到哪里  
		  List<String> paths = new ArrayList<>();
		  
		  for(MultipartFile file : imgFile){
			  
			  String fileName = file.getOriginalFilename();//获取上传的文件名字 日后可以根据文件名做相应的需改 例如自定义文件名 分析文件后缀名等等  
			  
			  Date date = new Date();
			  
			  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			  
			  String dateString = simpleDateFormat.format(date);
			  
			  Random random = new Random();
			  int code = random.nextInt(8999) + 1000;
			  
			  fileName = fileName.substring(fileName.lastIndexOf("."),fileName.length());
			  
			  String newFileName = dateString+""+code+fileName;
			  
			  File dir = new File(uploadPath);
			  
			  File targetFile = new File(uploadPath , newFileName);  //新建文件  
			  
			  try {
				  
				  	String os = System.getProperty("os.name"); 
				  
					if (!dir.exists()) {
						  
						dir.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别  
						
						if(!os.toLowerCase().startsWith("win")){  
							 
							Runtime.getRuntime().exec("chmod 777 " + dir.getPath());
						}
					}  
				  
				    file.transferTo(targetFile);
				    
				    targetFile.setExecutable(true);//设置可执行权限
				    targetFile.setReadable(true);//设置可读权限
					targetFile.setWritable(true);//设置可写权限
					
					String saveFilename = targetFile.getPath();
					
					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + saveFilename);
					}
				    
				    
				    String newPath = PropertiesUtil.FILE_HTTP_PATH+userId+"/"+newFileName;
				    
				    paths.add(newPath);
		    
			  } catch (Exception e) {
				  
				e.printStackTrace();  
				return "{\"error\":4,\"message\":\"服务器异常\",\"url\":\"\"}";
			  }  
		  }
		  
		  return "{\"error\":0,\"message\":\"SUCCESS\",\"url\":\""+paths.get(0)+"\"}";
		  
		}
    
	/**
	 * 
	 * @author: xiaoz
	 * @2017年6月20日 
	 * @功能描述:图片另存为
	 * @param files
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/savePicture",method = { RequestMethod.POST })  
	public ResponseDbCenter upload(@RequestParam(value = "files", required = false) MultipartFile[] files , HttpServletRequest request) {  
		  
		  String uploadPath = "F:" + File.separator + UPLOAD_DIRECTORY;
//		  String path = "E:/";//上传的目录  简单点 这里是E盘根目录  根据自己的需求改  想把上传的文件放在哪里  路径就写到哪里  
		  List<String> paths = new ArrayList<>();
		  
		  for(MultipartFile file : files){
			  
			  String fileName = file.getOriginalFilename();//获取上传的文件名字 日后可以根据文件名做相应的需改 例如自定义文件名 分析文件后缀名等等  
			  String newFileName = System.currentTimeMillis()+"_"+fileName;
			  
			  File targetFile = new File(uploadPath , newFileName);  //新建文件  
			  
			  if (!targetFile.exists()) {    //判断文件的路径是否存在  
				  
			    targetFile.mkdirs();  //如果文件不存在  在目录中创建文件夹   这里要注意mkdir()和mkdirs()的区别  
			  }  
			  // 保存  
			  try {  
				  
			    file.transferTo(targetFile); //传送  失败就抛异常  
			    String newPath = "http://172.116.101.62:8888/"+newFileName;
			    paths.add(newPath);
			    
			  } catch (Exception e) {  
				  
				e.printStackTrace();  
				return ResponseConstants.FUNC_SERVERERROR;
			  }  
		  }
		  
		  
		  
		  ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		  responseDbCenter.setResModel(paths);
		  
		  return responseDbCenter;
		  
		}  
	
}
