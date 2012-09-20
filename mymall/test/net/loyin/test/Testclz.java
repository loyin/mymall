package net.loyin.test;
import net.loyin.util.safe.MD5;


public class Testclz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		URL url=Testclz.class.getResource("/");
//		System.out.println(url.toString());
		System.out.println(MD5.getMD5ofStr("123456").toLowerCase());
	}

}
