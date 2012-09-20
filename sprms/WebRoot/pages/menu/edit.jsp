<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<script type="text/javascript">
$(function (){
	$.each(menuType,function(i,row){
		$("#menu_edit_type").append("<option value='"+i+"'>"+row+"</option>");
		});
	$("#menu_edit_type option[value=${menu.type}]").attr("selected","selected");
});
</script>
<div class="page">
	<div class="pageContent">
<form name="menuForm" id="menuForm"
	action="menu_save.htm" method="post"  class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<div class="pageFormContent" layoutH="57">
	<s:if test="null != menu.parent"> 
		<s:hidden key="menu.parent.id" /> 
	</s:if>
	<s:hidden key="menu.id"/>
	<s:hidden key="menu.menuLevel"/>
	<p>
		<label>
			菜单类型:
		</label>
		<select id="menu_edit_type" name="menu.type" class="required"></select>
	</p>
	<p>
		<label>
			菜单名称
		</label>
		<input type="text" name="menu.name" value="${menu.name }" class="required">
	</p>
	<p>
		<label>
			URL
		</label>
		<input type="text" name="menu.url" value="${menu.url }">
	</p>
	<p>
		<label>
			事件
		</label>
		<input type="text" name="menu.event" value="${menu.event }">
	</p>
	<p>
		<label>
			页面编码
		</label>
		<input type="text" name="menu.safeCode" value="${menu.safeCode }">
	</p>
	<p>
		<label>
			图标
		</label>
		<input type="text" name="menu.icon" value="${menu.icon }">
	</p>
	<p>
		<label>
			排序号
		</label>
		<input type="text" name="menu.orderNum" value="${menu.orderNum }" class="required digits">
	</p>
	</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
					<li>
						<div class="button"><div class="buttonContent"><button type="Button" class="close">取消</button></div></div>
					</li>
				</ul>
			</div>
		</form>
	</div>
</div>
