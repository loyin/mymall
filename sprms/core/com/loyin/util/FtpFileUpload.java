package com.jyd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;


/**
 * 使用ftp上传文件、目录
 */

public class FtpFileUpload {
	List<String> nameList = new ArrayList<String>();
	public FTPClient ftpClient = null;
	private String host;
	private String user;
	private String password;
	private int port;
	public FtpFileUpload() {
		ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(5000);// 设置连接超时时间为5s
	}

	/**
	 * 建立与ftp服务器的连接
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 * @throws IOException
	 *             建立连接失败
	 */
	public void connect(String host, int port, String user, String password)throws Exception {
		this.host=host;
		this.password=password;
		this.port=port;
		this.user=user;
		this.binary();
	}
	public void binary() throws Exception {
		ftpClient.connect(host, port);
		ftpClient.login(user, password);
		//ftpClient.bBINARY_FILE_TYPE();
	}
	/**
	 * 进入某一级目录
	 * @param relativePath
	 *            相对路径(也可以是绝对路径，但要注意写法)
	 * @throws IOException
	 *             目录不存在或权限不够
	 */
	public void cd(String relativePath) throws IOException {
		ftpClient.changeWorkingDirectory(relativePath);
	}
//	/**
//	 * 上传整个目录
//	 * 
//	 * @param directory
//	 *            目录路径
//	 * @throws IOException
//	 *             目录不存在或访问权限不够
//	 */
//	public void uploadDirectory(String directory) throws IOException {
//	/*	File file = new File(directory);
//		String name = null;// 待上传文件名
//		if (file.isDirectory()) {// 如果为目录，则按目录传
//			File[] files = file.listFiles();
//			// 在ftp服务器上创建对应目录
//			ftpClient.ascii();
//			String dir = file.getName();
//			ftpClient.sendServer("XMKD " + dir + "\r\n");
//			ftpClient.readServerResponse();
//			ftpClient.cd(dir);
//			ftpClient.binary();
//			// 循环传递目录下的所有文件与目录
//			int i = 0;
//			for (i = 0; i < files.length; i++) {
//				File tmpFile = files[i];
//
//				if (tmpFile.isDirectory())
//					uploadDirectory(tmpFile.getAbsolutePath());
//				else {
//					name = tmpFile.getName();
//					upload(directory + "/" + name);
//				}
//			}
//		} else
//			// 如果为文件，则按文件上传
//			upload(directory, file.getName());*/
//	}

	/**
	 * 上传文件
	 * 
	 * @param srcFile
	 *            源文件
	 * @throws IOException
	 */
	public boolean upload(String fileName,String newName) throws IOException {
		boolean flag = false;
		InputStream iStream = null;
		File file = new File(fileName);
		try {
			iStream = new FileInputStream(file);
			flag = ftpClient.storeFile(newName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}
	/**  
	 * 上传文件到FTP服务器，支持断点续传  
	 * @param local 本地文件名称，绝对路径  
	 * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext 按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构  
	 * @return 上传结果  
	 * @throws IOException  
	 */  
	public void uploadFile(String srcFile) throws IOException{   
		File file = new File(srcFile);  
		FileInputStream fis = new FileInputStream(file);
		try {                
		ftpClient.setBufferSize(1024);                    
		ftpClient.setControlEncoding("ISO8859-1");              
		//设置文件类型（二进制）                          
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		String filename= new String(file.getName().getBytes("GBK"),"ISO8859-1"); 
		ftpClient.storeFile(filename, fis);
		//fis.close();
		} catch (IOException e) {       
		    e.printStackTrace();        
		    throw new RuntimeException("FTP客户端出错！", e); 
		} finally {                     
		    IOUtils.closeQuietly(fis);  
		} 

	}
	/**
	 * 断开与服务器连接
	 */
	public void close() {
		try {
			ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给定文件去文件夹遍历取出文件
	 * 
	 * @param args
	 */
	public void getSubFile(String parent) {
		File parentF = new File(parent);
		if (parentF.isFile()) {
			nameList.add(parent);
			return;
		}
		String[] subFiles = parentF.list();
		for (int i = 0; i < subFiles.length; i++) {
			getSubFile(parentF.getAbsolutePath()
					+ System.getProperty("file.separator") + subFiles[i]);
		}
	}
	public List<String> getNameList() {
		return nameList;
	}
	public String getHost() {
		return host;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public int getPort() {
		return port;
	}
//	public static void main(String[] args) throws Exception {
//		FtpFileUpload ftpUpload = new FtpFileUpload();
//		try {
//			ftpUpload.connect("192.168.10.254", 21, "notes2", "notes2");
//			ftpUpload.cd("/oadata/notesdata/nccbank/data");// 打开FTP文件路径
//			//ftpUpload.ftpClient.sendServer("");
//			// ftpUpload.upload("D:\\upload\\ttt\\testftp.txt",
//			// "test1");//上传文件新命名
//			//ftpUpload.uploadDirectory("D:/runqianExcel1");
//			// ftpUpload.uploadDirectory("D:/dw/file/input/wyxt/");
//			//String str = "D:\runqianExcel1\青湖支行\月报\2009-11-30_小额支付系统运行月报_青湖支行.xls";
//			String filename="D:\\runqianExcel\\总行\\日报\\2009-12-24_南昌银行存贷款、备付金存款日报表_总行.xls";
//			String remote = "/oadata/notesdata/nccbank/";
//			ftpUpload.uploadFile(filename);
//			//System.out.println(ftpUpload.ftpClient.cwcwd(directory))
//			ftpUpload.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}