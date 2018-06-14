package com.kuangchi.sdd.baseConsole.device.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DescriptionPicModel;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.ContinueFTP;
/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-6-8 下午16:32:05
 * @功能描述: 设备分布背景图Action
 */
@Scope("prototype")
@Controller("descriptionPicturesAction")
public class DescriptionPicturesAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	public Object getModel() {
		return null;
	}
	
	@Resource(name="deviceService")
	DeviceService deviceService; 
	
	private File image; //上传的文件
	private String imageFileName; //文件名称
	private String imageContentType; //文件类型
	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	/**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-8 下午15:32:05
  	 * @功能描述: 查询设备分布背景图(分页)
  	 */
	public void getDescriptionPictures(){
		String flag = getHttpServletRequest().getParameter("flag");
		String description = getHttpServletRequest().getParameter("description");
		String device_group_num = getHttpServletRequest().getParameter("device_group_num");
		String page = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");
		List<DescriptionPicModel> dpList = deviceService.getDescriptionPictures(flag, description, device_group_num, page, size);
		Integer dpCount = deviceService.getDescriptionPicturesCount(flag, description, device_group_num);
		Grid<DescriptionPicModel> dpGrid = new Grid<DescriptionPicModel>();
		dpGrid.setRows(dpList);
		dpGrid.setTotal(dpCount);
		printHttpServletResponse(GsonUtil.toJson(dpGrid));
	}
	
	 /**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 删除设备分布背景图
  	 */
	public void deleteDescriptionPicturesByIds(){
		JsonResult result = new JsonResult();
		String ids = getHttpServletRequest().getParameter("ids");
		boolean deleteResult = deviceService.deleteDescriptionPicturesByIds(ids);
		if(deleteResult){
			result.setSuccess(true);
			result.setMsg("删除成功");
		}else{
			result.setMsg("删除失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	 /**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 新增设备分布背景图
  	 */
	public void addDescriptionPicture(){
		JsonResult result = new JsonResult();
		String flag = getHttpServletRequest().getParameter("flag");
		String description = getHttpServletRequest().getParameter("description");
		String pic_path = getHttpServletRequest().getParameter("pic_path");
		String device_group_num = getHttpServletRequest().getParameter("device_group_num");
		
		boolean addResult = deviceService.addDescriptionPicture(flag, description, pic_path, device_group_num);
		if(addResult){
			result.setSuccess(true);
			result.setMsg("添加成功");
		}else{
			result.setSuccess(false);
			result.setMsg("添加失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 查询设备分布背景图(不分页)
  	 */
	public void getDescriptionPicturesNoPage(){
		String description = getHttpServletRequest().getParameter("description");
		String id = getHttpServletRequest().getParameter("id");
		String device_group_num = getHttpServletRequest().getParameter("device_group_num");
		
		List<DescriptionPicModel> dpList = deviceService.getDescriptionPicturesNoPage(Integer.valueOf(id), description, device_group_num);
		printHttpServletResponse(GsonUtil.toJson(dpList));
	}
	
	/**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 修改设备分布背景图
  	 */
	public void updateDescriptionPicture(){
		JsonResult result = new JsonResult();
		String description = getHttpServletRequest().getParameter("description");
		String pic_path = getHttpServletRequest().getParameter("pic_path");
		String id = getHttpServletRequest().getParameter("id");
		String device_group_num = getHttpServletRequest().getParameter("device_group_num");
		
		boolean updateResult = deviceService.updateDescriptionPicture(description, pic_path, device_group_num, Integer.valueOf(id));
		if(updateResult){
			result.setSuccess(true);
			result.setMsg("更新成功");
		}else{
			result.setSuccess(false);
			result.setMsg("更新失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	 /**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 上传设备分布背景图
  	 */
	public String uploadDescriptionPicture(){
		ContinueFTP continueFTP=new ContinueFTP();
		String ftpPropertiesPath=getHttpServletRequest().getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"properties"+File.separator+"ftp.properties");
		com.kuangchi.sdd.util.file.FTP ftp=com.kuangchi.sdd.util.file.FTPUtil.getFtp(ftpPropertiesPath);	
		String dateString=  DateUtil.getDateString(new Date(),"yyyyMMdd");
	    String dateTimeString=DateUtil.getDateString(new Date(),"yyyyMMddHHmmss");
	    String fileName=dateTimeString+imageFileName; 
	    fileName = fileName.replaceAll(" ", "");  
	    if(fileName.lastIndexOf(".")!=-1){	//文件没有后缀
	    	String suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		    if("bmp".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix)){	//格式错误
		    	String imgPath=dateString+"/"+fileName;
				try {
					continueFTP.connect(ftp.getHost(), ftp.getPort(), ftp.getUserName(), ftp.getPassword());
					continueFTP.upload(image,imgPath );
					getHttpServletRequest().setAttribute("message", "上传成功"); 
					getHttpServletRequest().setAttribute("imgPath",imgPath );
				} catch (IOException e) {
					e.printStackTrace();
					getHttpServletRequest().setAttribute("message", "上传失败"); 
				}
		    }else {
		    	getHttpServletRequest().setAttribute("message", "格式错误，上传失败"); 
		    }
	    } else {
	    	getHttpServletRequest().setAttribute("message", "格式错误，上传失败"); 
	    }
		return SUCCESS;
	}
	
	/**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-7-15  下午15:32:05
  	 * @功能描述: 查询设备组
  	 */
	public void getDiviceGroupToPic(){
		HttpServletRequest request=getHttpServletRequest();	
		String flag = request.getParameter("flag");
		List<DeviceGroupModel> dgList = deviceService.getDiviceGroupToPic(flag);
		printHttpServletResponse(GsonUtil.toJson(dgList));
	}
	
	/**
  	 * @创建人　: 陈凯颖
  	 * @创建时间: 2016-6-12  上午10:32:05
  	 * @功能描述: 显示设备分布背景图
  	 */
	public void showDescriptionPicture(){

   	 HttpServletRequest request=getHttpServletRequest();	
   	 HttpServletResponse response=getHttpServletResponse();
   	 String imgPath = "";
	 try {
		String a=request.getParameter("imgPath");
		imgPath = new String(request.getParameter("imgPath").getBytes("iso-8859-1"), "UTF-8");
		imgPath = a;
	 } catch (UnsupportedEncodingException e1) {
		e1.printStackTrace();
	 }
   	 ContinueFTP continueFTP=new ContinueFTP();
   	 java.io.InputStream is=null;
   	 OutputStream out=null;
   	 try {
   		 
    	  String ftpPropertiesPath=request.getSession().getServletContext().getRealPath("/WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"properties"+File.separator+"ftp.properties");
    	  com.kuangchi.sdd.util.file.FTP ftp=com.kuangchi.sdd.util.file.FTPUtil.getFtp(ftpPropertiesPath);		
   		 
	      continueFTP.connect(ftp.getHost(), ftp.getPort(), ftp.getUserName(), ftp.getPassword());
	      if (null!=imgPath&&!imgPath.trim().equals("")&&!"undefined".equals(imgPath)) {
		  is=continueFTP.download(imgPath);
	      }else{
	    	 //如果没有上传头像，则给一个默认头像
	    	String defaultImg=request.getSession().getServletContext().getRealPath("/businessConsole"+File.separator+"images"+File.separator+"defaultImg.jpg");
	    	File file=new File(defaultImg)  ;
	    	is=new FileInputStream(file);
	      }
		  out=response.getOutputStream();
		  byte[] buffer=new byte[1024];
		  while (is.read(buffer)!=-1) {
		    out.write(buffer);			
		  }
		out.flush();
		continueFTP.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null!=is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			if (null!=out) {
			    try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}
}
