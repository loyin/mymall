<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/inc/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title><s:text name="system.title"/></title>
<meta http-equiv="content-ype" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<link href="${path}/themes/default/style.css" rel="stylesheet" type="text/css" />
<link href="${path}/themes/css/core.css" rel="stylesheet" type="text/css" />
<link href="${path}/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" />
<!--[if IE]>
<link href="${path}/themes/css/ieHack.css" rel="stylesheet" type="text/css" />
<![endif]-->
<%--<link href="${path}/themes/css/login.css" rel="stylesheet" type="text/css" />--%>
<link href="${path}/themes/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var path="${path}";
</script>
<script src="${path}/javascripts/jquery-1.4.2.min.js" type="text/javascript"></script>
<script src="${path}/javascripts/jquery.cookie.js" type="text/javascript"></script>
<script src="${path}/javascripts/jquery.validate.js" type="text/javascript"></script>
<script src="${path}/javascripts/jquery.bgiframe.js" type="text/javascript"></script>
<script src="${path}/javascripts/jquery.timers-1.2.js" type="text/javascript"></script>
<script src="${path}/xheditor/xheditor-zh-cn.min.js" type="text/javascript"></script>
<script src="${path}/javascripts/json2.js" type="text/javascript"></script>
<script src="${path}/javascripts/dwz.min.js" type="text/javascript"></script>
<script src="${path}/javascripts/dwz.regional.zh.js" type="text/javascript"></script>
<script src="${path}/uploadify/scripts/swfobject.js" type="text/javascript"></script>
<script src="${path}/uploadify/scripts/jquery.uploadify.v2.1.0.js" type="text/javascript"></script>
<script src="${path}/javascripts/params.js" type="text/javascript"></script>
<script src="${path}/javascripts/public.js" type="text/javascript"></script>
<script type="text/javascript">
var loginurl="login.jsp";
var timeoutloginurl="timeoutLogin.jsp";
$(function(){
	<c:if test="${user==null ||user.id==null}">
		$("#body").load(loginurl);
	</c:if>
	<c:if test="${user!=null &&user.id!=null}">
		$("#body").load('main.jsp');
	</c:if>
	$(".progressBar,.background").hide();
});
<%--//清理浏览器内存,只对IE起效,FF不需要--%>
if ($.browser.msie){
	$('body').everyTime('5s',CollectGarbage);
<%--	window.setInterval("CollectGarbage();", 1000);--%>
}
</script>
  </head>
  <body scroll="no">
  <div id="body"></div>
<!--拖动效果-->
	<div class="resizable"></div>
<!--阴影-->
	<div class="shadow" style="width:508px; top:148px; left:296px;">
		<div class="shadow_h">
			<div class="shadow_h_l"></div>
			<div class="shadow_h_r"></div>
			<div class="shadow_h_c"></div>
		</div>
		<div class="shadow_c">
			<div class="shadow_c_l" style="height:296px;"></div>
			<div class="shadow_c_r" style="height:296px;"></div>
			<div class="shadow_c_c" style="height:296px;"></div>
		</div>
		<div class="shadow_f">
			<div class="shadow_f_l"></div>
			<div class="shadow_f_r"></div>
			<div class="shadow_f_c"></div>
		</div>
	</div>
	<!--遮盖屏幕-->
	<div id="alertBackground" class="alertBackground"></div>
	<div id="dialogBackground" class="dialogBackground"></div>
	<div id='background' class='background'></div>
	<div id='progressBar' class='progressBar'>数据加载中，请稍等...</div>
  </body>
</html>
