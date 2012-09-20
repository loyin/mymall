package net.loyin.model;

import net.loyin.jFinal.anatation.TableBind;

import com.jfinal.plugin.activerecord.Model;

/***
 * 商品类别
 * @author 刘声凤
 *  2012-9-4 上午10:39:04
 */
@TableBind(name="gcategory")
@SuppressWarnings("serial")
public class Gcategory extends Model<Gcategory> {
	public static final Gcategory dao=new Gcategory();
}
