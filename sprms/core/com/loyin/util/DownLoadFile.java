package com.jyd.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;

public class DownLoadFile{
	/**
	 * 根据文件名称下载文件
	 * @param fileName
	 * @param response
	 */
	public static void downloadFile(String fileName,HttpServletResponse response){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "utf-8"));
			fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			fos = response.getOutputStream();
			bos = new BufferedOutputStream(fos);

			int bytesRead = 0;
			byte[] buffer = new byte[100 * 1024];
			while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				bos.flush();
				
			}
		} catch (IOException e) {
			// response.setContentType("text/html");
			response.reset();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				System.err.print(e);
			}
		}
	}

	public void downloadFile(File tempFile,String fileName, HttpServletResponse response) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;
		try {
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "utf-8"));
			fis = new FileInputStream(tempFile);
			bis = new BufferedInputStream(fis);
			fos = response.getOutputStream();
			bos = new BufferedOutputStream(fos);

			int bytesRead = 0;
			byte[] buffer = new byte[100 * 1024];
			while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				bos.flush();
			}
		} catch (IOException e) {
			// response.setContentType("text/html");
			response.reset();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				System.err.print(e);
			}
		}
	}
}