package net.loyin.model;

import net.loyin.jFinal.anatation.TableBind;

import com.jfinal.plugin.activerecord.Model;

/***
 * 管理员
 * @author 刘声凤
 *  2012-9-4 上午10:39:04
 */
@TableBind(name="manager")
@SuppressWarnings("serial")
public class Manager extends Model<Manager> {
	public static final Manager dao=new Manager();
}
