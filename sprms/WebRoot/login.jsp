<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<%
pageContext.setAttribute("now",new Date());
pageContext.setAttribute("serverUrl","http://"+request.getServerName()+":"+request.getServerPort());
%>
<%--<link href="login.css" rel="stylesheet" type="text/css" />--%>
<style>
/**圆角开始*/
h3,h2{ margin:0; padding:0;}
.box{ margin-bottom:10px; font-size:12px;margin-left:20px;}
.box .hd,.box .rc-tp,.box .rc-tp span,.box .rc-bt,.box .rc-bt span{background:url(box.png) no-repeat;}
.box .rc-tp,.box .rc-bt{position:relative;display:block;height:4px;}
.box .rc-tp span,.box .rc-bt span{float:right;width:4px;height:4px;}
.box .rc-tp{margin-bottom:-4px;background-position:-3px 0;}
.box .rc-bt{margin-top:-4px;background-position:-3px -3px;}
.box .rc-bt span{background-position:0 -3px;}
.box .hd{height:21px;border:1px solid #D1D1D1;border-bottom:none;background-color:#fbfbfb;background-position:0 -8px;background-repeat:repeat-x;}
.box .hd h3{font-size:22px;font-weight:bold;line-height:23px; *line-height:21px; color:#404040;padding-left:10px;}
.box .bd{padding:5px 10px 5px 10px;border:1px solid #D1D1D1; line-height:21px;}
.box .rc-tp,.box .rc-bt{overflow:hidden;}
/**圆角结束*/
div.unit1 {margin:0; padding:5px 0;}
.login_input { width:150px;border:1px #D1D1D1 solid;}
#username{background:url('themes/images/user.png') right 0 no-repeat;}
#password{background:url('themes/images/pwd.png') right 0 no-repeat;}
.code { float:left; width:60px;border:1px #D1D1D1 solid;}
#loginFormDiv,#linkDiv{width:200px;padding:10px;margin-top: 10px;}
div.unit1 label{float:left;width:50px; padding:0 5px; line-height:21px;text-align:left;}
#roadPlanDiv,#tunnelPlanDiv{float:left;margin-left:70px;margin-top:20px;width:300px;}
#linkDiv li{margin-top:3px;}
.box li{margin-top:2px;font-size:14px;width:100%;}
.box table{width:100%;}
.box table tr{margin-top:2px;}
.box table td{padding-top:3px;}
.sub{ display:block; width:75px; height:30px; border:none; background:url(themes/default/images/login_sub.png) no-repeat; cursor:pointer;float:right;margin-right:10px;}
#loginTable td{text-align:left;margin-top:1px;vertical-align:middle;padding:5px;}
</style>
<script>var uupth='${serverUrl}${path }/';</script>
	<script src="login.js"></script>
		<div id="header">
			<div class="headerNav">
			<div class="logo"></div>
			</div>
			<div id="userinfoBar" class="accountInfo">
				<div style="float:left;padding-left:20px;"><fmt:formatDate value="${now}" pattern="yyyy年 M月d日  EEEE"/></div>
				<div class="logoutdiv" style="float:right;margin-right:30px;"></div>
			</div>
		</div>
	<div style="float: left;">
		<div class="box"  style="width:250px;"> 
        <span class="rc-tp">
            <span></span>
        </span>
<%--        <div class="hd" style="text-align:left;">--%>
<%--            <h3>登录</h3>--%>
<%--        </div>--%>
        <div class="bd"  style="height:142px;">
		<form id="loginForm" onsubmit="return loginSubmit();">
			<table id="loginTable">
			<tr>
			<td width="100">用户名</td>
			<td><input type="text" size="20" class="login_input"name="username" id="username" /></td>
			</tr>
			<tr>
			<td>密码</td>
			<td><input type="password" size="20" class="login_input"name="password" id="password" /></td>
			</tr>
			<tr>
			<td>验证码</td>
			<td><input class="code" id="code" name="code" type="text" size="4"/>
				<span style="float:left;margin-left:5px;"><img id="codeImg" onclick="this.src='${path}/VC.jpg?id='+Math.random()*10"
						title="点击更新验证码" width="75" height="24" />
				</span></td>
			</tr>
			<tr>
			<td></td>
			<td><button class="sub" type="submit"></button></td>
			</tr>
			</table>
		</form>
        </div>
        <span class="rc-bt">
            <span></span>
        </span>
    </div>
		<div class="box" style="width:250px;float:left;"> 
        <span class="rc-tp">
            <span></span>
        </span>
        <div class="bd">
				<div id="linkDiv">
		<ul>
			<li><a href="http://www.bjlzj.gov.cn/" target="_blank"><img src="themes/link/link1.png"/></a></li>
			<li><a href="http://www.bjwatergroup.com.cn/" target="_blank"><img src="themes/link/link2.png"/></a></li>
			<li><a href="http://www.bjgas.com/" target="_blank"><img src="themes/link/link3.png"/></a></li>
			<li><a href="http://www.bdc.cn" target="_blank"><img src="themes/link/link4.png"/></a></li>
			<li><a href="http://www.bdhg.com.cn/" target="_blank"><img src="themes/link/link5.png"/></a></li>
			<li><a href="http://www.bj.sgcc.com.cn" target="_blank"><img src="themes/link/link6.png"/></a></li>
			<li><a href="http://www.bjtelecom.com.cn/" target="_blank"><img src="themes/link/link7.png"/></a></li>
		</ul>
		</div>
        </div>
        <span class="rc-bt">
            <span></span>
        </span>
    </div>
		</div>
	<div style="float:left;">
	<div class="box" style="width:500px;"> 
        <span class="rc-tp">
            <span></span>
        </span>
        <div class="hd" style="text-align:left;height:30px;padding-top:10px;">
            <h3>道路大修项目信息</h3>
        </div>
        <div class="bd" id="roadplanlist" style="text-align:left;">
        	<table width="100%"><tbody></tbody></table>
        </div>
        <span class="rc-bt">
            <span></span>
        </span>
    </div>
	<div class="box" style="width:500px;"> 
        <span class="rc-tp">
            <span></span>
        </span>
        <div class="hd" style="text-align:left;height:30px;padding-top:10px;">
            <h3>管线更新改造项目信息</h3>
        </div>
        <div class="bd" id="tunnelplanlist" style="text-align:left;">
        <table width="100%"><tbody></tbody></table>
        </div>
        <span class="rc-bt">
            <span></span>
        </span>
    </div>
    </div>
    <div style="float:left;margin-left: 20px;">
    	 <div class="box" style="width:400px;margin-left:0px;"> 
        <span class="rc-tp">
            <span></span>
        </span>
        <div class="hd" style="text-align:left;height:30px;padding-top:10px;">
            <h3>通知</h3>
        </div>
        <div class="bd" id="noticelist" style="text-align:left;">
        <table width="100%"><tbody></tbody></table>
        </div>
        <span class="rc-bt">
            <span></span>
        </span>
    </div>
    </div>
<div id="footer">版权所有北京市市政市容管理委员会</div>