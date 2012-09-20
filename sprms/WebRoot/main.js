function initflash(){
	DWZ.init("dwz.frag.xml", {
	//	loginUrl:timeoutloginurl, loginTitle:"登录",	// 弹出登录对话框
		loginUrl:path,	// 跳到登录页面--%>
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"themes"});
		}
	});
}
$(function(){
	initflash();
});

//清理浏览器内存,只对IE起效,FF不需要
/*if ($.browser.msie) {
	window.setInterval("CollectGarbage();", 10000);
}*/
function logOut(){
	try{
	if($.pdialog!=undefined)
	$.pdialog.remove();//closeCurrent();
	}catch(e){}
	alertMsg.confirm("确认要退出系统？", {
        okCall: function(){
		$.ajax({url:'user_logOut.htm',dataType:"json",success:function (data){
			if(data.statusCode=="100"){
		 	//	$("#body").load(loginurl);
				window.location=path;
			}else{
				alertMsg.error(data.msg);
			}
		},error:function(){alertMsg.error("退出异常！");}});
        },cancelCall:function(){}
       }
	);
	/*
	$.exfpopup.msgbox("确认要退出系统？","退出系统",3,function(){
		$.ajax({url:'user_logOut.htm',dataType:"json",success:function (data){
			if(data.statusCode=="100"){
		 		$("#body").load('login.jsp');
			}else{
				alertMsg.error(data.msg);
			}
		},error:function(){$.exfpopup.msgbox("退出异常！");}});
	},function(){});*/
}