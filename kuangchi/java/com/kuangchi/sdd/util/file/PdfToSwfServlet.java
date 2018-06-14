package com.kuangchi.sdd.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PdfToSwfServlet extends HttpServlet {

	private static final long serialVersionUID = -1899574260157767421L;
	
	private static final  String SWFTools_HOME = "F:\\chucun\\tool\\work\\swf\\pdf2swf.exe";

	private File filePath;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		File sourceFile = new File(filePath, "Paper.pdf");

		String destFileName = UUID.randomUUID().toString() + "Paper.swf";

		File destFile = new File(filePath, destFileName);

		int convertResult = pdf2SWF(sourceFile, destFile);

		PrintWriter out = response.getWriter();
		String result = "";
		if (convertResult == 1) {
			result = "{\"success\":true}";

		} else {
			result = "{\"success\":true,\"url\":\"" + destFileName + "\"}";
		}
		out.write(result);
		out.flush();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
	
		filePath = new File(config.getServletContext().getRealPath("upload"));
		
		if(!filePath.exists()){
			filePath.mkdirs();
		}
	}
	
	
	

	
	
	public static int pdf2SWF(File sourceFile, File destFile) {

		// 目标路径不存在则建立目标路径
		if (!destFile.getParentFile().exists())
			destFile.getParentFile().mkdirs();

		// 源文件不存在则返回 -1

		if (!sourceFile.exists())
			return -1;

		try {
			// 调用pdf2swf命令进行转换swfextract -i - sourceFilePath.pdf -o
			// destFilePath.swf
			String command = SWFTools_HOME + "  -i " + sourceFile + " -o "
					+ destFile;
			Process pro = Runtime.getRuntime().exec(command);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(pro.getInputStream(),"utf-8"));
			while (bufferedReader.readLine() != null) {

			}
			pro.waitFor();
			return pro.exitValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return 1;
	} 
	
}
