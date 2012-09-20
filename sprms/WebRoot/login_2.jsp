<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<%
pageContext.setAttribute("now",new Date());
%>
<style>
div.unit1 {margin:0; padding:5px 0;}
#loginFormDiv,#linkDiv{width:200px;padding:5px;height:150px; padding:10px;}
#loginFormDiv label{float:left; width:50px; padding:0 5px; line-height:21px;}
#linkDiv li{margin-top:3px;}
#leftside,#container{background-color:#ffffff;}
.sub{ display:block; width:75px; height:30px; border:none; background:url(${path}/themes/default/images/login_sub.png) no-repeat; cursor:pointer;float:right;margin-right:10px;}
</style>
	<script src="login.js"></script>
	<div id="layout">
		<div id="header">
			<div class="headerNav">
			<div class="logo"></div>
			</div>
			<div id="userinfoBar" class="accountInfo">
				<div style="float:left;"><fmt:formatDate value="${now}" pattern="yyyy年 M月d日  EEEE"/></div>
				<div class="logoutdiv" style="float:right;margin-right:30px;"></div>
			</div>
		</div>
	<div id="leftside">
		<div id="loginFormDiv">
		<form id="loginForm" onsubmit="return loginSubmit();">
			<div class="unit1">
				<label>
					用户名：
				</label>
				<input type="text" size="20" class="login_input"name="username" id="username" />
			</div>
			<div class="unit1">
				<label>
					密码：
				</label>
				<input type="password" size="20" class="login_input"name="password" id="password" />
			</div>
			<div class="unit1">
				<label>
					验证码：
				</label>
				<input class="code" id="code" name="code" type="text" size="4"/>
				<span><img id="codeImg" onclick="this.src='${path}/VC.jpg?id='+Math.random()*10"
						alt="点击更新验证码" width="75" height="24" />
				</span>
			</div>
			<div class="unit1"><button class="sub" type="submit"></button></div>
		</form>
		</div>
		<div id="linkDiv">
		<ul>
			<li><a href="#"><img src="themes/link/link1.png"/></a></li>
			<li><a href="#"><img src="themes/link/link2.png"/></a></li>
			<li><a href="#"><img src="themes/link/link3.png"/></a></li>
			<li><a href="#"><img src="themes/link/link4.png"/></a></li>
			<li><a href="#"><img src="themes/link/link5.png"/></a></li>
			<li><a href="#"><img src="themes/link/link6.png"/></a></li>
			<li><a href="#"><img src="themes/link/link7.png"/></a></li>
		</ul>
		</div>
		</div>
	<div id="container">
	</div>
	</div>
<div id="footer">版权所有：北京市市政市容管理委员会</div>