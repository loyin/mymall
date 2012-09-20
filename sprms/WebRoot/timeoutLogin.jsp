<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<script>
$(function(){
	$("#codeImg").attr("src","${path}/VC.jpg?id="+Math.random()*10);
	$(".loginBtn").click(function (){
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
						$.pdialog.closeCurrent();
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
<div class="page">
	<div class="pageContent">
	<form method="post" id="loginForm" class="pageForm required-validate" onsubmit="return false;">
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>用户名：</label>
					<input type="text" size="20" class="login_input" name="username" id="username" />
					</div>
					<div class="unit">
						<label>密码：</label>
						<input type="password" size="20" class="login_input" name="password" id="password"/>
					</div>
					<div class="unit">
						<label>验证码：</label>
						<input class="code" id="code" name="code" type="text" size="4"value=""/>
						<span><img id="codeImg" onclick="this.src='${path}/VC.jpg?id='+Math.random()*10" alt="点击更新验证码" width="75" height="24" /></span>
					</div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" class="loginBtn">登录</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
</div>

