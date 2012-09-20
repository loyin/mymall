<%@ CODEPAGE=65001 %>
<!--#include file="../serverscript/asp/ubb2html.asp"-->
<%
'此程序为UBB模式下的服务端显示测试程序
Response.Charset="UTF-8"
dim sHtml
sHtml=ubb2html(request("elm1"))'Server.HTMLEncode()
sHtml=showCode(sHtml)
sHtml=showFlv(sHtml)
%><script language="javascript" runat="server">
function showCode(sHtml)
{
	sHtml=sHtml.replace(/\[code\s*(?:=\s*((?:(?!")[\s\S])+?)(?:"[\s\S]*?)?)?\]([\s\S]*?)\[\/code\]/ig,function(all,t,c){//code特殊处理
		t=t.toLowerCase();
		if(!t)t='plain';
		c=c.replace(/[<>]/g,function(c){return {'<':'&lt;','>':'&gt;'}[c];});
		return '<pre name="code" class="'+t+'">'+c+'</pre>';
	});
	return sHtml;
}
function showFlv(sHtml)
{
	sHtml=sHtml.replace(/\[flv\s*(?:=\s*(\d+)\s*,\s*(\d+)\s*)?\]\s*(((?!")[\s\S])+?)(?:"[\s\S]*?)?\s*\[\/flv\]/ig,function(all,w,h,url){
		if(!w)w=480;if(!h)h=400;
		return '<embed type="application/x-shockwave-flash" src="mediaplayer/player.swf" wmode="transparent" allowscriptaccess="always" allowfullscreen="true" quality="high" bgcolor="#ffffff" width="'+w+'" height="'+h+'" flashvars="file='+url+'" />';
	});
	return sHtml;
}
</script><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>UBB文章显示测试页</title>
<style type="text/css">
body{margin:5px;border:2px solid #ccc;padding:5px;}
</style>
<link type="text/css" rel="stylesheet" href="syntaxhighlighter/SyntaxHighlighter.css"/>
<script type="text/javascript" src="syntaxhighlighter/shCore.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushXml.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushJScript.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushCss.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushPhp.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushCSharp.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushCpp.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushJava.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushPython.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushRuby.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushVb.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushDelphi.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushSql.js"></script>
<script type="text/javascript" src="syntaxhighlighter/shBrushPlain.js"></script>
<script type="text/javascript">
window.onload=function(){dp.SyntaxHighlighter.HighlightAll('code');}
</script>
<body>
	<%=sHtml%>
</body>
</html>