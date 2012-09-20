package net.loyin.interceptor;

import net.loyin.model.Manager;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
/**
 * 管理用户后台登录状态及权限验证拦截器
 * @author 刘声凤
 *  2012-9-6 下午8:32:53
 */
public class ManagerInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		Controller ctrl=ai.getController();
		Manager manager= (Manager)ctrl.getSessionAttr("manager");
		if(manager==null||manager.getInt("id")==0){
			String ckey=ai.getControllerKey();
			if(ckey.contains("webadmin")){
				ctrl.redirect(ctrl.getRequest().getContextPath()+"/webadmin");
			}else
			ctrl.renderText("{\"statusCode\":301}");
		}else
		ai.invoke();//注意 一定要执行此方法
	}
}