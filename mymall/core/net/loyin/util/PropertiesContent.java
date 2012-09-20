package net.loyin.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import net.loyin.util.safe.CipherUtil;

import org.apache.log4j.Logger;

/**
 * 配置文件config.properties所有配置
 * 
 * @author 刘声凤 2012-6-21 下午2:05:06
 */
public class PropertiesContent {
	public static Logger log = Logger.getLogger(PropertiesContent.class);
	public static Map<String, String> config = new HashMap<String, String>();
	public static Properties properties=new Properties();
	static {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		Enumeration<String> cfgs = rb.getKeys();
		while (cfgs.hasMoreElements()) {
			String key = cfgs.nextElement();
			String val=rb.getString(key);
			if (key.contains("mail.password")==true) {
				try {
					config.put(key, CipherUtil.decryptData(val));
				} catch (Exception e) {
					log.error("对参数解密异常",e);
				}
			}else{
				config.put(key,val);
			}
		}
		 properties.putAll(config);
	}
}
