package net.loyin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.loyin.controller.web.WebBaseController;
import net.loyin.jFinal.anatation.RouteBind;

import org.apache.commons.fileupload.util.Streams;

import com.jfinal.upload.UploadFile;

@RouteBind(path = "/file")
public class FileController extends WebBaseController {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
	private static String uploadroot = "/upload/";

	/** 单个文件上传 */
	@SuppressWarnings("deprecation")
	public void up() {
		String errmsg ="", savefilename="";
		UploadFile upfile = this.getFile();
		File file = upfile.getFile();
		String filedataFileName = upfile.getOriginalFileName();
		String realPath = this.getRequest().getRealPath("/");
		savefilename = uploadroot+ sf.format(new Date())+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
		if (file != null) {
			try {
				if (root == null)
					root = this.getRequest().getContextPath();
				Streams.copy(new FileInputStream(file),new BufferedOutputStream(new FileOutputStream(realPath + savefilename)), true);
			} catch (Exception e) {
			} finally {
				file.delete();//将已经原始上传文件删除
			}
		}else{
			errmsg="未上传文件";
		}
		this.renderText("{'err':'" + errmsg + "','msg':'" + root + savefilename+ "'}");
	}
	
	/** 文件下载 */
	public void down() {
		
	}
}
