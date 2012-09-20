package net.loyin.model;

import net.loyin.jFinal.anatation.TableBind;

import com.jfinal.plugin.activerecord.Model;

/***
 * 用户会员包含管理员
 * @author 刘声凤
 *  2012-9-4 上午10:39:04
 */
@TableBind(name="member")
@SuppressWarnings("serial")
public class Member extends Model<Member> {
	public static final Member dao=new Member();
}
