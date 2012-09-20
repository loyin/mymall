package net.loyin.controller.webadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.loyin.interceptor.ManagerInterceptor;
import net.loyin.jFinal.anatation.RouteBind;
import net.loyin.model.Manager;
import net.loyin.util.safe.MD5;

import com.google.gson.Gson;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;

/**
 * 网站管理用户
 * 
 * @author 刘声凤 2012-9-6 下午9:06:42
 */
@RouteBind(path = "/ht/manager")
@Before({ ManagerInterceptor.class })
public class ManagerController extends WebAdminBaseController {
	public static String ak = "manager/";
	private static String navTabId="manager";
	public void index() {
		StringBuffer whee=new StringBuffer(" where 1=1 ");
		List<Object> param=new ArrayList<Object>();
		String username=this.getPara("username");
		if(username!=null&&!"".equals(username.trim())){
			whee.append(" and username like ?");
			param.add("%"+username+"%");
		}
		String userno=this.getPara("userno");
		if(userno!=null&&!"".equals(userno.trim())){
			whee.append(" and userno like ?");
			param.add("%"+userno+"%");
		}
		this.setAttr("username", username);
		this.setAttr("userno", userno);
		this.setAttr("page", Db.paginate(this.getParaToInt("pageNum", 1),
				this.getParaToInt("numPerPage", 20),
				"select id,userno 账号,username 姓名 ",
				" from  manager "+whee.toString()+" order by id",param.toArray()));
		render(ak + "index.html");
	}
	private static final String chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	/**
	 * 产生随机数
	 * @return
	 */
	private char[] getCode(int length) {
		char[] rands = new char[length];
		for (int i = 0; i < length; i++) {
			int rand = (int) (Math.random() * chars.length());
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}

	public void add() {
		String pwd="";//随机密码
		Manager pojo = new Manager();
		Long id = this.getParaToLong(0, 0L);
		if (id != null && id != 0) {
			pojo = Manager.dao.findById(id);
		}else{
			//生成随机密码
			pwd=new String(this.getCode(6));
		}
		setAttr("pojo", pojo);
		setAttr("pwd", pwd);
		setAttr("pwd2", MD5.getMD5ofStr(pwd));
		render(ak + "add.html");
	}

	public void save() {
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		try{
			getModel(Manager.class).save();
			jsonMap.put("statusCode", 200);
			jsonMap.put("message", "保存成功！");
			jsonMap.put("navTabId", navTabId);
		}catch(Exception e){
			jsonMap.put("statusCode", 300);
			jsonMap.put("message", "保存异常！");
		}
		Gson gson=new Gson();
		this.renderText(gson.toJson(jsonMap));
	}
	public void update() {
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		try{
			getModel(Manager.class).update();
			jsonMap.put("statusCode", 200);
			jsonMap.put("message", "保存成功！");
			jsonMap.put("navTabId", navTabId);
		}catch(Exception e){
			jsonMap.put("statusCode", 300);
			jsonMap.put("message", "保存异常！");
		}
		Gson gson=new Gson();
		this.renderText(gson.toJson(jsonMap));
	}
	public void del() {
		Manager.dao.deleteById(this.getParaToLong(0, 0L));
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		jsonMap.put("statusCode", 200);
		jsonMap.put("message", "删除成功！");
		jsonMap.put("navTabId", navTabId);
		Gson gson=new Gson();
		this.renderText(gson.toJson(jsonMap));
	}
	public void resetPwd(){
		String pwd=new String(this.getCode(6));//随机密码
		Long id=this.getParaToLong(0,0L);
		if(id!=0L){
			Manager.dao.set("id",id).set("pwd", MD5.getMD5ofStr(pwd)).update();
		}
		Map<String,Object> jsonMap=new HashMap<String,Object>();
		jsonMap.put("statusCode", 200);
		jsonMap.put("message", "重置密码成功！密码为<br><h3><b style='color:red;'>"+pwd+"</b></h3><br>请牢记！");
		jsonMap.put("navTabId", navTabId);
		Gson gson=new Gson();
		this.renderText(gson.toJson(jsonMap));
	}
	public void role(){
		render(ak+"role.html");
	}
}
