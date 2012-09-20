package net.loyin.controller.webadmin;

import com.jfinal.core.Controller;

/**
 * 基础Controller
 * 重写了render 直接转向到/WEB-INF/pages目录下的文件
 * @author 刘声凤
 *  2012-9-3 下午10:37:28
 */
public class WebAdminBaseController extends Controller {
	protected static String root;
	protected int pageSize=20;
	public String getroot(){
		return root;
	}
	@Override
	public void render(String view) {
		if(root==null)
			root=this.getRequest().getContextPath();
		this.setAttr("root",root);
		super.render("/WEB-INF/pages/webadmin/"+view);
	}
	public void add(){}
}
