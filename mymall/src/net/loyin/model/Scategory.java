package net.loyin.model;

import net.loyin.jFinal.anatation.TableBind;

import com.jfinal.plugin.activerecord.Model;

/***
 * 商铺类别
 * @author 龙影
 *  2012-9-4 上午10:39:04
 */
@TableBind(name="gcategory")
@SuppressWarnings("serial")
public class Scategory extends Model<Scategory> {
	public static final Scategory dao=new Scategory();
}
