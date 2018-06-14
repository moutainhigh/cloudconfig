package com.xkd.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.Invitation;
import com.xkd.model.InvitationPages;
import com.xkd.model.InvitationPagesElement;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.InvitationService;
import com.xkd.utils.PropertiesUtil;


@Controller
@RequestMapping("/invitation")
public class InvitationController {

	@Autowired
	InvitationService invitationService;
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:保存邀请函上传的图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveInvitationFile")
	public ResponseDbCenter saveInvitationFile(@RequestParam(value = "files", required = false) MultipartFile[] files,String token,HttpServletRequest req) throws Exception{
			
		List<Map<String, String>> listFile = new ArrayList<>();
		try {
			String userId = (String) req.getSession().getAttribute(token);
			String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + userId;
			
			for (MultipartFile file : files) {

				String fileName = file.getOriginalFilename();// 获取上传的文件名字
				String newFileName = UUID.randomUUID().toString()+"_"+fileName;

				File targetFileDir = new File(uploadPath); // 新建文件夹
				File targetFile = new File(uploadPath + "/" + newFileName); // 新建文件
				String os = System.getProperty("os.name");
				if (!targetFileDir.exists()) { // 判断文件的路径是否存在

					targetFileDir.mkdirs(); // 如果文件不存在 在目录中创建文件夹

					if(!os.toLowerCase().startsWith("win")){  
						 
						Runtime.getRuntime().exec("chmod 777 " + targetFileDir.getPath());
					} 

				}

					file.transferTo(targetFile); // 传送 失败就抛异常
					if (targetFile.exists()) {
						targetFile.setExecutable(true);// 设置可执行权限
						targetFile.setReadable(true);// 设置可读权限
						targetFile.setWritable(true);// 设置可写权限

						String saveFilename = targetFile.getPath();
						if(!os.toLowerCase().startsWith("win")){  
							Runtime.getRuntime().exec("chmod 777 " + saveFilename);
						} 
						//前台用户显示的文件名
						String httpPath = PropertiesUtil.FILE_HTTP_PATH + userId+"/"+newFileName;
						Map<String, String> fileAttr = new HashMap<>();
						fileAttr.put("imgSrc", httpPath);
						fileAttr.put("imgName", fileName);
						listFile.add(fileAttr);
						
					} else {
						return ResponseConstants.FUNC_UPLOADFILEFAIL;
					}
			}
		} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}


		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(listFile);
		
		return responseDbCenter;

	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:保存邀请函
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeInvitation")
	public ResponseDbCenter changeInvitation(String token,HttpServletRequest req,@RequestBody  String value)throws Exception {
		//try {
			
			String userId = (String) req.getSession().getAttribute(token);
			Invitation invitation = (Invitation) JSON.parseObject(value, Invitation.class);
			
			List<InvitationPages> pages =  invitation.getPages();
			
			if(null != pages){
				System.out.println("------------------------共多少页："+pages.size());
				invitation.setCreatedBy(userId);
				if(StringUtils.isBlank(invitation.getId())){
					invitation.setId(UUID.randomUUID().toString());
					invitationService.saveInvitation(invitation);
				}else{
					invitationService.editInvitation(invitation);
					invitationService.deletePagesElement(invitation.getId());
				}
				int nodeName = 1;
				for (InvitationPages invitationPages : pages) {
					List<InvitationPagesElement> elements = invitationPages.getElements();
					if(null != elements){
						invitationPages.setInvitationId(invitation.getId());
						invitationPages.setNodeName(nodeName++);
						//if(StringUtils.isBlank(invitationPages.getId())){
							invitationPages.setId(UUID.randomUUID().toString());
							invitationService.saveInvitationPages(invitationPages);
						/*}else{
							invitationService.editInvitationPages(invitationPages);
						}*/
						System.out.println("------------------------第："+(nodeName-1)+"页，元素"+elements.size());
						for (InvitationPagesElement invitationPagesElement : elements) {
							invitationPagesElement.setPagesId(invitationPages.getId());
							//if(StringUtils.isBlank(invitationPagesElement.getId())){
								invitationPagesElement.setId(UUID.randomUUID().toString());
								invitationPagesElement.setInvitationId(invitation.getId());
								invitationPagesElement.setPagesId(invitationPages.getId());
								System.out.println(invitationPagesElement.toString());
								invitationService.saveElement(invitationPagesElement);
							/*}else{
								invitationService.editElement(invitationPagesElement);
							}*/
						}
					}
				}
			}
			
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(invitation);
			return responseDbCenter;
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:根据id删除邀请函
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteInvitation")
	public ResponseDbCenter deleteInvitation(String token,HttpServletRequest req,String id) throws Exception{
		//try {
			String userId = (String) req.getSession().getAttribute(token);
			invitationService.deleteInvitation(id,userId);
			return ResponseConstants.SUCCESS; 
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:根据id查询邀请函
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getInvitationById")
	public ResponseDbCenter getInvitationById(String token,HttpServletRequest req,String id)throws Exception {
		//try {
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			System.out.println("InvitationController.getInvitationById()-----------------------"+id);
			responseDbCenter.setResModel(invitationService.getInvitationById(id));
			return responseDbCenter;
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
	
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:查询邀请函集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getInvitationList")
	public ResponseDbCenter getInvitationList(String token,HttpServletRequest req,
			 @RequestParam(required = true)int pageSize,
			 @RequestParam(required = true)int currentPage,
			 String likeName)throws Exception {
		//try {
			int  pageNo = ((currentPage)-1)*pageSize;
			Map<String, Object> map = new HashMap<>();
			map.put("pageNo", pageNo);
			map.put("pageSize", pageSize);
			map.put("likeName", likeName);
			List<Invitation> invitationList = invitationService.getInvitationList(map);
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(invitationList);
			return responseDbCenter;
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:根据id查询邀请函
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPagesTempletList")
	public ResponseDbCenter getPagesTempletList(String token,HttpServletRequest req)throws Exception {
		//try {
			List<InvitationPages> pagerList = invitationService.getPagesTempletList();
			ResponseDbCenter responseDbCenter = new ResponseDbCenter();
			responseDbCenter.setResModel(pagerList);
			return responseDbCenter;
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
	/**
	 * 
	 * @author: gaoddO
	 * @2017年3月22日 
	 * @功能描述:邀请函绑定会务
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeInvitationBindMeeting")
	public ResponseDbCenter changeInvitationBindMeeting(String token,
			HttpServletRequest req,
			 @RequestParam(required = true)String meetingId,
			 @RequestParam(required = true)String invitationId)throws Exception {
		//try {
			String userId = (String) req.getSession().getAttribute(token);
			invitationService.changeInvitationBindMeeting(meetingId,invitationId,userId);
			return ResponseConstants.SUCCESS;
		/*} catch (Exception e) {
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}*/
	}
}
