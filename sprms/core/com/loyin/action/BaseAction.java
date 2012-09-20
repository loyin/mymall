package com.jyd.action;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;

import com.jyd.model.OperatLog;
import com.jyd.model.User;
import com.jyd.service.OperatLogService;
import com.jyd.util.Page;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 基本Action类
 * 
 * @author loyin
 */
@SuppressWarnings("serial")
public abstract class BaseAction extends ActionSupport implements ApplicationAware,
 SessionAware,
 ServletRequestAware, ServletResponseAware, Preparable
 //,ApplicationContextAware
{
	private static final Logger log = Logger.getLogger(BaseAction.class);
	/** action的后缀 是struts.properties文件定义的struts.action.extension */
	public static String postfix="action";
	/**HttpSession*/
	protected HttpSession session;
	protected Map<String, Object> sessionMap;
//	protected int pageSize = 20;
//	protected int pageNo = 1;
	protected int pageNum=1;
	protected int numPerPage=20;
	protected Page page;
	protected String printJsonStr;
	public Long id;
	protected Map<String,Object> jsonMap=new HashMap<String,Object>();
	@Resource
	protected OperatLogService operatLogService;
	protected User user;
	static {
		try {
			postfix = ResourceBundle.getBundle("struts").getString("struts.action.extension");
		} catch (Exception ex) {
			log.error(ex);
		}
	}
	/**检查登录是否超时*/
	public boolean checkLoginTimeOut(){
		if(user==null||user.id==null){
			jsonMap.put("statusCode",301);
			jsonMap.put("message","登录超时！请重新登录！");
			this.outPrint(this.toJsonString(jsonMap));
			return true;
		}
		return false;
	}
	public abstract  String execute() throws Exception ;
	protected Map<String, Object> application = null;

	protected HttpServletRequest request = null;

	protected HttpServletResponse response = null;

	/**
	 * 需要跳转的页面
	 */
	protected String webpage = null;
	/**
	 * 需要跳转的action路径名
	 */
	protected String action = null;
	/** 跳转到action */
	public static final String ACTION = "action";// ${action} 直接跳转到另一action

	/**
	 * 返回登录界面
	 */
	public static final  String LONGINPAGE = "login";
	/**返回录入页面*/
	public static final String INPUT="input";
	public static final String web="web";
	/**录入页面路径*/
	public String inputpage=null;
	/**
	 * 返回首页
	 */
	public static final String INDEX = "index";
	/**
	 * 是接口Preparable实现方法 是用来验证的。
	 */
	public void prepare() throws Exception {
		this.user=(User)session.getAttribute("user");
	}
	/**插入操作日期*/
	public void insertOperatLog(String menuName,String method,Long userId){
		operatLogService.save(new OperatLog(request.getLocalAddr(),menuName,method,userId));
	}
	public void setRequest(HttpServletRequest request) {
		if(session==null)
		session=request.getSession();
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@SuppressWarnings("unchecked")
	public void setApplication(Map application) {
		this.application = application;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 打印html等页面显示的作用这些都要写xml或html的完整代码
	 * 
	 * @param String
	 *            msg
	 */
	public void outPrint(String msg) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("pragma","no-cache");
			response.setHeader("cache-control","no-cache");
			response.setHeader("content-type","text/html;charset=utf-8");
			response.setHeader("expires","0");
			request.setCharacterEncoding("utf-8");
			response.getWriter().print(msg);
		} catch (Exception ex) {

		}
	}

	/**
	 * 获得Json形式的数据
	 * 
	 * @param Map
	 *            &lt;String,Object&gt; map
	 * @return String
	 */
	public String toJsonString(Map<String, Object> map) {
		JSONObject obj = new JSONObject(map);
		log.info(obj.toString());
		return obj.toString();
	}

//	public int getPageSize() {
//		return this.pageSize;
//	}
//
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}
//
//	public int getPageNo() {
//		return pageNo;
//	}
//
//	public void setPageNo(int pageNo) {
//		this.pageNo = pageNo;
//	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setServletRequest(HttpServletRequest request) {
		if(session==null)
		session=request.getSession();
		this.request=request;
	}

	public static String getPostfix() {
		return postfix.replace(".", "");
	}

	@SuppressWarnings("unchecked")
	public void setSession(Map arg0) {
		sessionMap=arg0;
	}

	public String getPrintJsonStr() {
		return printJsonStr;
	}

	public void setPrintJsonStr(String printJsonStr) {
		this.printJsonStr = printJsonStr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
//		this.pageNo=pageNum;
		this.pageNum = pageNum;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage1) {
//		this.pageSize=numPerPage;
		this.numPerPage = numPerPage1;
		/*Cookie[] cookies=request.getCookies();
		for(Cookie ck:cookies){
			if("numPerPage".equals(ck.getName())){
				String pageNumstr=ck.getValue();
				if(pageNumstr!=null&&!"".equals(pageNumstr)){
					int t=Integer.parseInt(pageNumstr);
					if(t!=numPerPage){
						numPerPage=t;
					}else{
						Cookie ck1=new Cookie("numPerPage",numPerPage+"");
						response.addCookie(ck1);
					}
				}
				break;
			}
		}*/
		
	}
	
}
