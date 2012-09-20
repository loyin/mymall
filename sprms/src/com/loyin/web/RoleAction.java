package com.jyd.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jyd.action.BaseAction;
import com.jyd.model.Role;
import com.jyd.service.RoleService;
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction {
	private static final long serialVersionUID = -4193254366558048301L;
	private static final Logger log = Logger.getLogger(RoleAction.class);
	@Resource
	private RoleService roleService;
	private Role roleEdit;
	private static String navTabId="page9";
	@Override
	public String execute() throws Exception {
		Map<String,Object> filter=new HashMap<String,Object>();
		if(roleEdit!=null&&roleEdit.getName()!=null){
			filter.put("name_like", "%"+roleEdit.getName()+"%");
		}
		filter.put("delFlag", false);
		filter.put("name_order","asc");
		this.page=this.roleService.pageByFilter(filter, this.pageNum, this.numPerPage);
		this.webpage="list";
		return "web";
	}
	public String edit(){
		if(roleEdit!=null&&roleEdit.id!=null&&roleEdit.id!=0){
			roleEdit=roleService.get(roleEdit.id);
		}
		this.webpage="edit";
		return "web";
	}
	public String save(){
		try{
			if(checkLoginTimeOut()){
				return null;
			}
			Role role= new Role();
		if(roleEdit!=null&&roleEdit.id!=null&&roleEdit.id!=0){
			role=this.roleService.get(roleEdit.id);
		}
		
		role.setName(roleEdit.getName());
		role.setRemark(roleEdit.getRemark());
		roleService.save(role);
		jsonMap.put("statusCode",200);
		jsonMap.put("message","保存角色成功！");
		jsonMap.put("navTabId",navTabId);
//		jsonMap.put("callbackType","forward");
//		jsonMap.put("forward","");
		this.insertOperatLog("角色管理", "保存", user.id);
		}catch(Exception e){
			log.error("保存角色异常",e);
			jsonMap.put("statusCode",300);
			jsonMap.put("message","保存角色异常！");
		}
		this.outPrint(this.toJsonString(jsonMap));
		return null;
	}
	public String delete(){
		try{
			if(checkLoginTimeOut()){
				return null;
			}
		roleService.delFlag(roleEdit.id);
		jsonMap.put("statusCode",200);
		jsonMap.put("message","删除角色成功！");
		jsonMap.put("navTabId",navTabId);
//		jsonMap.put("callbackType","forward");
//		jsonMap.put("forward","");
		this.insertOperatLog("角色管理", "删除", user.id);
		}catch(Exception e){
			log.error("删除角色异常",e);
			jsonMap.put("statusCode",300);
			jsonMap.put("message","删除角色异常！");
		}
		this.outPrint(this.toJsonString(jsonMap));
		return null;
	}
	public Role getRoleEdit() {
		return roleEdit;
	}
	public void setRoleEdit(Role roleEdit) {
		this.roleEdit = roleEdit;
	}

}
