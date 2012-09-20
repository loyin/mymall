package net.loyin.model;

import net.loyin.jFinal.anatation.TableBind;

import com.jfinal.plugin.activerecord.Model;

/***
 * 文章类别
 * @author 刘声凤
 *  2012-9-4 上午10:39:04
 */
@TableBind(name="acategory")
@SuppressWarnings("serial")
public class Acategory extends Model<Acategory> {
	public static final Acategory dao=new Acategory();
}
