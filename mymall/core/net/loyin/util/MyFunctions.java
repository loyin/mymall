package net.loyin.util;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.loyin.util.safe.CipherUtil;

public class MyFunctions {
	/**
	 * 判断为空时做的字符串链接处理
	 * 
	 * @param os  需要处理的str
	 * @param ds  默认str
	 * @param fs  前缀str
	 * @param cs  链接在后的str
	 * @return
	 */
	public static String doNull(String os, String ds,String fs, String cs) {
		if (os == null || "".equals(os)) {
			if (ds != null && !"".equals(ds))
				return ds + (cs == null ? "" : cs);
			else {
				return null;
			}
		} else {
			return ((fs!=null&&!"".equals(fs)?fs:""))+os + (cs == null ? "" : cs);
		}
	}
	/**加密*/
	public static String encryptData(String source){
		try{
		return CipherUtil.encryptData(source,CipherUtil.key);
		}catch(Exception e){
			return null;
		}
	}
	/**解密*/
	public static String decryptData(String source){
		try{
		return CipherUtil.decryptData(source,CipherUtil.key);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 去掉html代码
	 * @param htmlstr
	 * @return
	 */
	public static String quHtml(String htmlstr){
		Pattern patt=Pattern.compile("<[^>]+>([^<]*)</[^>]+>");  
		if(htmlstr==null)
			return null;
        Matcher m=patt.matcher(htmlstr);  
        while(m.find()){  
        	htmlstr=htmlstr.replaceFirst("<[^>]+>([^<]*)</[^>]+>", m.group(1).toString());  
        }  
		return htmlstr;
	}
	public static String[] filelist(String dir){
		File file=new File(dir);
		if(file.exists()){
			return file.list();
		}else{
			return null;
		}
	}
}
