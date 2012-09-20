<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<%
pageContext.setAttribute("now",new Date());
%>
<style>
div.unit1 {margin:0; padding:5px 0;}
#loginFormDiv label{float:left; width:50px; padding:0 5px; line-height:21px;}
.sub { display:block; width:75px; height:30px; border:none; background:url(${path}/themes/default/images/login_sub.png) no-repeat; cursor:pointer;float:right;margin-right:10px;}
</style>
	<script>
	$(function (){
			DWZ.init("dwz.frag.xml", {
				debug:false,	// 调试模式 【true|false】
				callback:function(){
					initEnv();
					$("#themeList").theme({themeBase:"themes"});
				}
			});
			$("#codeImg").attr("src","${path}/VC.jpg?id="+Math.random()*10);
	});
	function loginSubmit(){
		var username=$("#username").val();
		var password=$("#password").val();
		var code=$("#code").val();
		var msg="";
		if(username==""){
			msg="用户名不能为空！";
		}else if(password==""){
			msg="密码不能为空！";
		}else if(code==""){
			msg="验证码不能为空！";
		}else{
				$.ajax({url:'user_login.htm',
					data:$("#loginForm").serialize(),
					dataType:'json',
					type:'POST',
					cache: false,
					success:function (data){
					if(data.statusCode=="100"){
				 		$("#body").load('main.jsp');
					}else{
						$("#codeImg").attr("src","${path}/VC.jpg?id="+Math.random()*10);
						alertMsg.error(data.message);
					}},error:function(){alertMsg.error("登录异常！");},
					complete:function(){
						
						}});
			}
		if(msg!=""){
			alertMsg.error(msg);
		}
		return false;
		}
	</script>
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
		</div>
	</div>
<div id="footer">版权所有：北京市市政市容管理委员会</div>