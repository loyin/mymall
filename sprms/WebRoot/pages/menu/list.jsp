<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>
<script type="text/javascript">
$(function (){
	$.each(menuType,function(i,row){
		$("#menu_type").append("<option value='"+i+"'>"+row+"</option>");
		});
	$("#menu_table tr td:nth-child(4)").each(function(r,td){
		var num=(new Number($(td).text()));
		$(td).text(menuType[num]);
	});
	initQueryForm();
});
</script>
<div class="page">
<form action="menu.htm" method="post" id="pagerForm" onsubmit="return navTabSearch(this)">
		<input type="hidden" name="pageNum" value="${page.pageNo}" />
		<input type="hidden" name="numPerPage" value="${page.pageSize}" />
	<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<li>
						<label>
							菜单类型
						</label>
						<select name="menu.type" id="menu_type" val="${menu.type}">
							<option value="">全部</option>
						</select>
					</li>
					<li>
						<label>
							菜单名称
						</label>
						<input type="text" name="menu.name" val="${menu.name}"/>
					</li>
				</ul>
				<div class="subBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">查询</button>
								</div>
							</div>
						</li>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="reset">重置</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="pageContent">
			<div class="panelBar">
				<ul class="toolBar">
					<li><a href="#"class="add" url="menu_edit.htm" title="添加菜单" height="300" width="550" target="dialog" ><span>添加</span></a></li>
				</ul>
			</div>
			<table cellpadding="0" cellspacing="0" id="menu_table" class="table" layouth="138">
			<thead>
				<tr>
					<th>序号</th>
					<th>菜单名称</th>
					<th>页面编码</th>
					<th>类型</th>
					<th>URL</th>
					<th>事件</th>
					<th>排序号</th>
					<th>图标</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.data}" var="item" varStatus="statu">
					<tr>
						<td>${statu.index+1+page.start }</td>
						<td>${item.name }</td>
						<td>${item.safeCode }</td>
						<td>${item.type }</td>
						<td>${item.url}</td>
						<td>${item.event}</td>
						<td>${item.orderNum}</td>
						<td>${item.icon}</td>
						<td>
							<a href="#"class="editBtn" url="menu_edit.htm?menu.parent.id=${item.id}&menu.menuLevel=${item.menuLevel+1}" height="300" width="550" target="dialog" title="新增&lt;${item.name }&gt;子菜单">新增子菜单</a>
							<a href="#"class="editBtn" url="menu_edit.htm?menu.id=${item.id}" height="300" width="550" target="dialog" title="编辑&lt;${item.name }&gt;菜单">编辑</a>
							<a href="#"class="delBtn" url="menu_delete.htm?menu.id=${item.id }" target="navTabTodo" title="确定要删除吗?">删除</a>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
	</div>
</form>
<%@include file="/inc/page.jsp"%>
</div>