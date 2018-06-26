package com.kuangchi.sdd.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * 支持断点续传的FTP实用类

 * @version 0.1 实现基本断点上传下载
 * @version 0.2 实现上传下载进度汇报
 * @version 0.3 实现中文目录创建及中文文件创建，添加对于中文的支持
 */
public class ContinueFTP {
	
	public FTPClient ftpClient = new FTPClient();
	
	public ContinueFTP(){
		//设置将过程中使用到的命令输出到控制台
	//	this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}
	
	public static final Logger LOG = Logger.getLogger(ContinueFTP.class);
	
	/**
	 * 连接到FTP服务器
	 * @param hostname 主机名
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public  boolean connect(String hostname, int port, String username,
			String password) throws IOException {
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}
	
	/**
	 * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public DownloadStatus download(String remote,String local) throws IOException{
		//设置被动模式
		ftpClient.enterLocalPassiveMode();
		//设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		DownloadStatus result;
		
		//检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"),"iso-8859-1"));
		if(files.length != 1){
			LOG.info("远程文件不存在");
			return DownloadStatus.Remote_File_Noexist;
		}
		
		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		//本地存在文件，进行断点下载
		if(f.exists()){
			long localSize = f.length();
			//判断本地文件大小是否大于远程文件大小
			if(localSize >= lRemoteSize){
				LOG.info("本地文件大于远程文件，下载中止");
				return DownloadStatus.Local_Bigger_Remote;
			}
			
			//进行断点续传，并记录状态
			FileOutputStream out = new FileOutputStream(f,true);
			ftpClient.setRestartOffset(localSize);
			InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"),"iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize /100;
			long process=localSize /step;
			int c;
			while((c = in.read(bytes))!= -1){
				out.write(bytes,0,c);
				localSize+=c;
				long nowProcess = localSize /step;
				if(nowProcess > process){
					process = nowProcess;
					if(process % 10 == 0)
						LOG.info("下载进度："+process);
					//TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean isDo = ftpClient.completePendingCommand();
			if(isDo){
				result = DownloadStatus.Download_From_Break_Success;
			}else {
				result = DownloadStatus.Download_From_Break_Failed;
			}
		}else {
			OutputStream out = new FileOutputStream(f);
			InputStream in= ftpClient.retrieveFileStream(new String(remote.getBytes("GBK"),"iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize /100;
			long process=0;
			long localSize = 0L;
			int c;
			while((c = in.read(bytes))!= -1){
				out.write(bytes, 0, c);
				localSize+=c;
				long nowProcess = localSize /step;
				if(nowProcess > process){
					process = nowProcess;
					if(process % 10 == 0)
						LOG.info("下载进度："+process);
					//TODO 更新文件下载进度,值存放在process变量中
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if(upNewStatus){
				result = DownloadStatus.Download_New_Success;
			}else {
				result = DownloadStatus.Download_New_Failed;
			}
		}
		return result;
	}
	
	/**
	 * 上传文件到FTP服务器，支持断点续传
	 * @param local 本地文件名称，绝对路径
	 * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
	 * @return 上传结果
	 * @throws IOException
	 */
	public UploadStatus upload(String local,String remote) throws IOException{
		//设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		//设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		UploadStatus result;
		//对远程目录的处理
		String remoteFileName = remote;
		if(remote.contains("/")){
			remoteFileName = remote.substring(remote.lastIndexOf("/")+1);
			//创建服务器远程目录结构，创建失败直接返回
			if(CreateDirecroty(remote, ftpClient)==UploadStatus.Create_Directory_Fail){
				return UploadStatus.Create_Directory_Fail;
			}
		}
		
		//检查远程是否存在文件
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"),"iso-8859-1"));
		if(files.length == 1){
			long remoteSize = files[0].getSize();
			File f = new File(local);
			long localSize = f.length();
			if(remoteSize==localSize){
				return UploadStatus.File_Exits;
			}else if(remoteSize > localSize){
				return UploadStatus.Remote_Bigger_Local;
			}
			
			//尝试移动文件内读取指针,实现断点续传
			result = uploadFile(remoteFileName, f, ftpClient, remoteSize);
			
			//如果断点续传没有成功，则删除服务器上文件，重新上传
			if(result == UploadStatus.Upload_From_Break_Failed){
				if(!ftpClient.deleteFile(remoteFileName)){
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, f, ftpClient, 0);
			}
		}else {
			result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
		}
		return result;
	}
	/**
	 * 断开与远程服务器的连接
	 * @throws IOException
	 */
	public void disconnect() throws IOException{
		if(ftpClient.isConnected()){
			ftpClient.disconnect();
		}
	}
	
	/**
	 * 递归创建远程服务器目录
	 * @param remote 远程服务器文件绝对路径
	 * @param ftpClient FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public UploadStatus CreateDirecroty(String remote,FTPClient ftpClient) throws IOException{
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = remote.substring(0,remote.lastIndexOf("/")+1);
		if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"),"iso-8859-1"))){
			//如果远程目录不存在，则递归创建远程服务器目录
			int start=0;
			int end = 0;
			if(directory.startsWith("/")){
				start = 1;
			}else{
				start = 0;
			}
			end = directory.indexOf("/",start);
			while(true){
				String subDirectory = new String(remote.substring(start,end).getBytes("GBK"),"iso-8859-1");
				if(!ftpClient.changeWorkingDirectory(subDirectory)){
					if(ftpClient.makeDirectory(subDirectory)){
						ftpClient.changeWorkingDirectory(subDirectory);
					}else {
						LOG.info("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}
				
				start = end + 1;
				end = directory.indexOf("/",start);
				
				//检查所有目录是否创建完毕
				if(end <= start){
					break;
				}
			}
		}
		return  status;
	}
	
	/**
	 * 上传文件到服务器,新上传和断点续传
	 * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
	 * @param localFile 本地文件File句柄，绝对路径
	 * @param processStep 需要显示的处理进度步进值
	 * @param ftpClient FTPClient引用
	 * @return
	 * @throws IOException
	 */
	public UploadStatus uploadFile(String remoteFile,File localFile,FTPClient ftpClient,long remoteSize) throws IOException{
		UploadStatus status;
		//显示进度的上传
		long step = localFile.length() / 100;
		long process = 0;
		long localreadbytes = 0L;
		RandomAccessFile raf = new RandomAccessFile(localFile,"r");
		OutputStream out = ftpClient.appendFileStream(new String(remoteFile.getBytes("GBK"),"iso-8859-1"));
		//断点续传
		if(remoteSize>0){
			ftpClient.setRestartOffset(remoteSize);
			process = remoteSize /step;
			raf.seek(remoteSize);
			localreadbytes = remoteSize;
		}
		byte[] bytes = new byte[1024];
		int c;
		while((c = raf.read(bytes))!= -1){
			out.write(bytes,0,c);
			localreadbytes+=c;
			if(localreadbytes / step != process){
				process = localreadbytes / step;
				LOG.info("上传进度:" + process);
				//TODO 汇报上传状态
			}
		}
		out.flush();
		out.close();
		raf.close();
		
		boolean result =ftpClient.completePendingCommand();
		if(remoteSize > 0){
			status = result?UploadStatus.Upload_From_Break_Success:UploadStatus.Upload_From_Break_Failed;
		}else {
			status = result?UploadStatus.Upload_New_File_Success:UploadStatus.Upload_New_File_Failed;
		}
		return status;
	}
	
	public static void main(String[] args) {
		ContinueFTP myFtp = new ContinueFTP();
		try {
			myFtp.connect("192.168.153.151", 21, "root", "root");
//			myFtp.ftpClient.makeDirectory(new String("歌曲".getBytes("GBK"),"iso-8859-1"));
//			myFtp.ftpClient.changeWorkingDirectory(new String("歌曲".getBytes("GBK"),"iso-8859-1"));
//			myFtp.ftpClient.makeDirectory(new String("爱你等于爱自己".getBytes("GBK"),"iso-8859-1"));
//			System.out.println(myFtp.upload("F:\\chucun\\tool\\common\\KMS8.exe", "/file/KMS8.exe"));
			System.out.println(myFtp.upload("D:\\background.jpg", "/var/ftp/pub/background.jpg"));
//			System.out.println(myFtp.download("/usr/local/received/zlib-1.2.7.tar.gz", "D://aaaaa.tar.gz"));
			myFtp.disconnect();
		} catch (IOException e) {
			LOG.info("连接FTP出错："+e.getMessage());
		}
	}
	
	
	
	public UploadStatus upload(File  file,String remote) throws IOException{
		//设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		//设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		UploadStatus result;
		//对远程目录的处理
		String remoteFileName = remote;
		if(remote.contains("/")){
			remoteFileName = remote.substring(remote.lastIndexOf("/")+1);
			//创建服务器远程目录结构，创建失败直接返回
			if(CreateDirecroty(remote, ftpClient)==UploadStatus.Create_Directory_Fail){
				return UploadStatus.Create_Directory_Fail;
			}
		}
		
		//检查远程是否存在文件
		FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"),"iso-8859-1"));
		if(files.length == 1){
			long remoteSize = files[0].getSize();
		
			long localSize = file.length();
			if(remoteSize==localSize){
				return UploadStatus.File_Exits;
			}else if(remoteSize > localSize){
				return UploadStatus.Remote_Bigger_Local;
			}
			
			//尝试移动文件内读取指针,实现断点续传
			result = uploadFile(remoteFileName, file, ftpClient, remoteSize);
			
			//如果断点续传没有成功，则删除服务器上文件，重新上传
			if(result == UploadStatus.Upload_From_Break_Failed){
				if(!ftpClient.deleteFile(remoteFileName)){
					return UploadStatus.Delete_Remote_Faild;
				}
				result = uploadFile(remoteFileName, file, ftpClient, 0);
			}
		}else {
			result = uploadFile(remoteFileName,file, ftpClient, 0);
		}
		return result;
	}

	public   InputStream  download(String remote) throws Exception {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(
				remote.getBytes("GBK"), "iso-8859-1"));
		if (files.length != 1) {
			throw new Exception("文件不存在");
		}

		File tmpFile = new File(System.getProperty("java.io.tmpdir"),UUID.randomUUID().toString());
		
		
		OutputStream out = new FileOutputStream(tmpFile);
		InputStream in = ftpClient.retrieveFileStream(new String(remote
				.getBytes("GBK"), "iso-8859-1"));
		byte[] buffer = new byte[1024];

		int c;
		while ((c = in.read(buffer)) != -1) {
			out.write(buffer, 0, c);

		}
		in.close();
		out.close();

		boolean downloadStatus = ftpClient.completePendingCommand();
		if (!downloadStatus)
			throw new Exception("下载失败");

		InputStream inputStream = new FileInputStream(tmpFile);
		
		return inputStream;

	}
}