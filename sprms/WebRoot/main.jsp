<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<%
pageContext.setAttribute("now",new Date());
%>
<script type="text/javascript" src="main.js"></script>
	<div id="layout">
		<div id="header">
			<div class="headerNav">
			<div class="logo"></div>
			</div>
			<div id="userinfoBar" class="accountInfo">
				<div style="float:left;"><fmt:formatDate value="${now}" pattern="yyyy年 M月d日  EEEE"/> 当前用户：<font color="red" >${user.name }</font>&nbsp;
				<c:catch>
					<c:if test="${user.role.id!=1}">
						所属部门：${user.enterprise.name }
					</c:if>
				</c:catch>
				</div>
				<div class="logoutdiv" style="float:right;margin-right:30px;">
				   <label><a href="#" url="changepwd.html" target="dialog" width="350" height="250"><img src="themes/images/isnot.png" style="vertical-align:text-top"/>修改密码</a></label>
				   <label onclick="logOut();"><img src="themes/images/cancel_1.png" style="vertical-align:text-top"/>退出</label>
				</div>
			</div>
		</div>
		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>主菜单</h2><div>收缩</div></div>
				<div class="accordion" fillSpace="sideBar"></div>
				<div class="accordion" fillSpace="sideBar">
	<c:forEach items="${menus}" var="menu0">
		<c:if test="${menu0.parent==null||menu0.menuLevel==0}">
		<div class="accordionHeader">
			<h2><span>Folder</span>${menu0.name }</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<c:forEach items="${menus}" var="menu1">
				<c:if test="${menu1.parent.id==menu0.id}">
				<li>
					<c:if test="${!empty menu1.url&&menu1.type==0}">
						<a url="${menu1.url }" target="navTab" rel="${menu1.safeCode }">${menu1.name }</a>
					</c:if>
					<c:if test="${empty menu1.url&&menu1.type==0}">
						<a>${menu1.name }</a>
						<ul>
							<c:forEach items="${menus}" var="menu2">
									<c:if test="${menu2.type==0 &&(menu2.parent.id==menu1.id)}">
									<li>
									<a url="${menu2.url }" target="navTab" rel="${menu2.safeCode }">${menu2.name }</a>
									</li>
									</c:if>
							</c:forEach>
						</ul>
					</c:if>
					</li>
				</c:if>
				</c:forEach>
			</ul>
		</div>
		</c:if>
	</c:forEach>
</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="desktop" style="display:none;" class="main"><a href="javascript:void(0)"><span><span class="home_icon">桌面</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li style="display:none;"><a href="javascript:void(0)">桌面</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent">
					<div></div>
				</div>
			</div>
		</div>

		<div id="taskbar" style="left:0px; display:none;z-index:1000;">
			<div class="taskbarContent">
				<ul></ul>
			</div>
			<div class="taskbarLeft taskbarLeftDisabled" style="display:none;">taskbarLeft</div>
			<div class="taskbarRight" style="display:none;">taskbarRight</div>
		</div>
		<div id="splitBar"></div>
		<div id="splitBarProxy"></div>
	</div>
	<div id="footer">版权所有：北京市市政市容管理委员会</div>
	<%--showselectoption 是共用的--%>
<div id="showSelectOption" class="showSelectOption">
<!--（中间层）脱离标准流，绝对定位，拥有一个自动的宽高，不够的话，可以自己再设，_filter:alpha(opacity=0);去除iframe颜色，ie6 中 iframe层级永高于select -->
<iframe style="position:absolute; top:0px; left:0px; border-style:none;z-index:-1;opacity=0;_filter:alpha(opacity=0);"></iframe>
<div>
	<ul></ul>
</div>
	<span class="selectdivclose" title="关闭">&nbsp;</span>
</div>
