package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuangchi.sdd.util.commonUtil.QRcodeUtil;

public class QcodeServlet extends HttpServlet {

	
	private static final long serialVersionUID = -7782092088300528948L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			 {
		String codeContent = req.getParameter("content");
		
		File file =QRcodeUtil.makeQRcode(codeContent);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/png charset=UTF-8");
		
		FileInputStream in = null;
		ServletOutputStream out = null; 
		try {
			in = new FileInputStream(file);
			out = resp.getOutputStream();
			InToOut(in, out);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			
			file.delete();
		
		}
		
		
		
		
		
		
		
	}
	
	private void InToOut(InputStream in,OutputStream out) throws IOException{
		byte[] buffer = new byte[1024];
		int readTotal;
		
		while((readTotal = in.read(buffer)) > 0){
			out.write(buffer, 0, readTotal);
		}
		
		out.flush();
	}
	
	
	

}
