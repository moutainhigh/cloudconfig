package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuangchi.sdd.util.excel.ExcelTemplateFactory;

//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1288656820113456151L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String wjDm = request.getParameter("wjDm");

		String type = request.getParameter("type");
		String template = request.getParameter("template");

		InputStream inputStream = null;
		ServletOutputStream outputStream = null;
		String showName = null;
		ContinueFTP ftp = null;
		
		File file =null;
		try {
			if (type == null) {
				response.sendError(404);
				return;
			} else if (type.equals("ftp")) {

				if (null == wjDm) {
					response.sendError(404);
					return;
				} else {

					ftp = new ContinueFTP();

					ftp.connect("127.0.0.1", 21, "admin", "admin");

					inputStream = ftp.download("/upload/" + wjDm);

					showName = wjDm;
				}
			} else if (type.equals("template")) {
				
				if (null == template) {
					response.sendError(404);
					return;
				} else {
					file = ExcelTemplateFactory.createTemplate(template);
					inputStream = new FileInputStream(file);
					
					showName = "模板.xls";
				}

			}

			outputStream = response.getOutputStream();

			response.setCharacterEncoding("UTF-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("application/x-download charset=UTF-8");

			setContentDisposition(request, response, showName);

			byte[] buffer = new byte[1024];
			int total;

			while ((total = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, total);

				outputStream.flush();
			}

			outputStream.flush();

		} catch (Exception e) {
			response.sendError(500);
			e.printStackTrace();
		} finally {

			if (null != outputStream) {
				outputStream.close();

			}

			if (null != inputStream) {
				inputStream.close();
			}

			if (null != ftp) {
				ftp.disconnect();
			}
			
			if(file != null){
				file.delete();
			}

		}

	}

	private void setContentDisposition(HttpServletRequest request,
			HttpServletResponse response, String showName)
			throws UnsupportedEncodingException {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String contentDisposition = "";
		if (userAgent != null) {
			if (userAgent.indexOf("msie") >= 0) {
				contentDisposition = "attachment; filename="
						+ URLEncoder.encode(showName, "UTF-8").replace("+",
								"%20");

			} else if (userAgent.indexOf("mozilla") >= 0) {
				contentDisposition = "attachment; filename*=UTF-8''"
						+ URLEncoder.encode(showName, "UTF-8").replace("+",
								"%20");

			} else if (userAgent.indexOf("applewebkit") >= 0) {

				// contentDisposition = "attachment; filename=" +
				// MimeUtility.encodeText(showName, "UTF8", "B");

				contentDisposition = "attachment; filename="
						+ URLEncoder.encode(showName, "UTF-8").replace("+",
								"%20");
			} else if (userAgent.indexOf("safari") >= 0) {
				contentDisposition = "attachment; filename="
						+ new String(showName.getBytes("UTF-8"), "ISO8859-1");

			} else if (userAgent.indexOf("opera") >= 0) {
				contentDisposition = "attachment; filename*=UTF-8''"
						+ URLEncoder.encode(showName, "UTF-8").replace("+",
								"%20");

			} else {
				contentDisposition = "attachment; filename*=UTF-8''"
						+ URLEncoder.encode(showName, "UTF-8").replace("+",
								"%20");

			}
		}

		response.setHeader("Content-Disposition", contentDisposition);
	}

}
