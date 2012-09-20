package net.loyin.controller.webadmin;

import java.util.HashMap;
import java.util.Map;

import net.loyin.jFinal.anatation.RouteBind;
import net.loyin.model.Manager;
import net.loyin.util.safe.MD5;

import com.google.gson.Gson;

/**
 * 后台管理
 * 
 * @author 刘声凤 2012-9-4 下午4:51:36
 */
@RouteBind(path = "/webadmin")
public class WebadminController extends WebAdminBaseController {
	
	public void index() {
		Manager m=this.getSessionAttr("manager");
		if(m==null||m.getInt("id")==0){
			this.render("index.html");
		}else{
			this.render("main.html");
		}
	}
	public void loginDialog(){
		this.render("loginDialog.html");
	}
	public void login(){
		String username=this.getPara("username");
		String pwd=this.getPara("pwd");
		String code=this.getPara("code");
		String check= this.getSessionAttr("check");
		Map<String,Object> json=new HashMap<String,Object>();
		Gson gson=new Gson();
		if(username==null||"".equals(username.trim())||pwd==null||"".equals(pwd)||code==null||"".equals(code)){
			json.put("stat",200);
			json.put("msg", "信息填写不全！");
		}else
		if(check==null||"".equals(check)){
			json.put("stat",200);
			json.put("msg", "验证码超时");
		}else if(!check.equals(code)){
			json.put("stat",200);
			json.put("msg", "验证码错误");
		}else{
			pwd=MD5.getMD5ofStr(pwd);
			Manager m= Manager.dao.findFirst("select * from manager where userno=? and pwd=?", new Object[]{username,pwd});
			if(m!=null&&m.getInt("id")!=0){
				this.setSessionAttr("manager", m);
				json.put("stat",100);
			}else{
				json.put("stat",200);
				json.put("msg", "用户名或密码错误");
			}
		}
		renderText(gson.toJson(json));
	}
	public void logout(){
		this.removeSessionAttr("manager");
		this.redirect(this.getroot()+"/webadmin");
	}
}
