<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<link href="${path}/themes/css/login.css" rel="stylesheet" type="text/css" />
	<script>
	document.title="<s:text name="system.title"/>";
	$(function (){
			DWZ.init("dwz.frag.xml", {
				debug:false,	// 调试模式 【true|false】
				callback:function(){
					initEnv();
					$("#themeList").theme({themeBase:"themes"});
				}
			});
			$("#codeImg").attr("src","${path}/VC.jpg?id="+Math.random()*10);
		$(".sub").click(function (){
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
			});
	});
	</script>
	<div id="login">
		<div id="login_header">
			<h1 class="login_logo">
			</h1>
			<div class="login_headerContent">
				<div class="navList">
					<ul>
					</ul>
				</div>
				<h2 class="login_title">
<%--				<img src="${path}/themes/default/images/login_title.png" />--%>
				</h2>
			</div>
		</div>
		<div id="login_content">
			<div class="loginForm">
				<form id="loginForm">
					<p>
						<label>用户名：</label>
						<input type="text" size="20" class="login_input" value="" name="username" id="username" />
					</p>
					<p>
						<label>密码：</label>
						<input type="password" size="20" class="login_input" value="" name="password" id="password"/>
					</p>
					<p>
						<label>验证码：</label>
						<input class="code" id="code" name="code" type="text" size="4"value=""/>
						<span><img id="codeImg" onclick="this.src='${path}/VC.jpg?id='+Math.random()*10" alt="点击更新验证码" width="75" height="24" /></span>
					</p>
				</form>
				<div class="login_bar">
						<button class="sub"></button>
					</div>
			</div>
			<div class="login_banner"><img src="${path}/themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">
					
				</ul>
				<div class="login_inner">
				</div>
			</div>
		</div>
		<div id="login_footer">
			版权所有：北京市市政市容管理委员
		</div>
	</div>
