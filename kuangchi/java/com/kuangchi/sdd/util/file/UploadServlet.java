package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 739410093043225599L;
	
	private  File uploadFileFolder;
	
	private static DiskFileItemFactory factory;
	
	private ServletFileUpload upload;

	

	@Override
	public void init(ServletConfig config) throws ServletException {
		
		ServletContext context = config.getServletContext();
		uploadFileFolder = new File(context.getRealPath("upload"));
		
		if(!uploadFileFolder.exists()){
			uploadFileFolder.mkdirs();
		}
		
		FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
		
		factory = new DiskFileItemFactory();
		factory.setRepository(uploadFileFolder);
		factory.setFileCleaningTracker(fileCleaningTracker);
		upload = new ServletFileUpload(factory);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if(isMultipart){
			ContinueFTP ftp  = null;
			try {
				List<FileItem> items = upload.parseRequest(request);
				
				
				ftp =  new ContinueFTP();
				
				ftp.connect("127.0.0.1", 21, "admin", "admin");
				
				for(FileItem tmp: items){
					if(!tmp.isFormField()){
						File tmpFile = new File(uploadFileFolder, UUID.randomUUID().toString()+ "-" + tmp.getName());
						
					
						tmp.write(tmpFile);
	
						UploadStatus uploadStatus = ftp.upload(tmpFile, "/upload/"+tmpFile.getName());
						
						if(uploadStatus== UploadStatus.Upload_New_File_Success){
							//上传成功删除
							tmpFile.delete();
							
						}
						
						
					
						
						
						
					}
				}
			} catch (FileUploadException e) {
				
				e.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
			}finally{
				if(null != ftp){
					ftp.disconnect();
				}
			}
		}
	}

}
