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
function open(a){
	var $ur=$(a);
	$.pdialog.open($ur.attr("url"), 'sfd', '查看');
}
function initADialog(){
	$("a[target=dialog]").each(function(){
		var attr=$(this).attr("initADailog");
		if(attr==null||attr==undefined||attr==false){
		$(this).click(function(event){
		var $this=$(this);
		var title=$this.attr("title")||$this.text();
		var rel=$this.attr("rel")||"_blank";
		var options={};
		var w=$this.attr("width");
		var h=$this.attr("height");
		if(w)options.width=w;
		if(h)options.height=h;
		options.max=$this.attr("max");
		options.mask=$this.attr("mask");
		var url=unescape($this.attr("url"));
		if(!url.isFinishedTm()){
		alertMsg.error($this.attr("warn")||DWZ.msg("alertSelectMsg"));
		return false;}
		$.pdialog.open(url,rel,title,options);
		return false;});
		$(this).attr("initADailog",true);
		}
	});
}
$(function(){
	initflash();
	$("#codeImg").attr("src", "VC.jpg?id=" + Math.random() * 10);
	$.ajax({url:'roadPlan_showTop.htm',type:'POST',dataType:'json',success:function(data){
		var html="";
		if(data.length>0){
			$.each(data,function(i,r){
			html+="<tr><td><a url='roadPlan_show.htm?roadPlanEdit.id="+r.id+"'height='300' width='550' target='dialog'>"+r.projectName+"</a></td><td width='100px;'>"+r.datetime+"</td><tr>";
			});
		}
		$("#roadplanlist").find("tbody").empty().append(html);
		initADialog();
	}});
	$.ajax({url:'tunnelPlan_showTop.htm',type:'POST',dataType:'json',success:function(data){
		var html="";
		if(data.length>0){
			$.each(data,function(i,r){
			html+="<tr><td><a url='tunnelPlan_show.htm?tunnelPlanEdit.id="+r.id+"' height='300' width='550' target='dialog'>"+r.projectName+"</a></td><td width='100px;'>"+r.datetime+"</td><tr>";
			});
		}
		$("#tunnelplanlist").find("tbody").empty().append(html);
		initADialog();
	}});
	$.ajax({url:'notification_showTop.htm',type:'POST',dataType:'json',success:function(data){
		var html="";
		if(data.length>0){
			$.each(data,function(i,r){
			html+="<tr><td><a url='notification_show.htm?noticeEdit.id="+r.id+"' height='400' width='550' target='dialog'>"+r.title+"</a></td><td width='100px;'>"+r.datetime+"</td><tr>";
			});
		}
		$("#noticelist").find("tbody").empty().append(html);
		initADialog();
	}});
	//initflash();
});
function loginSubmit() {
	var username = $("#username").val();
	var password = $("#password").val();
	var code = $("#code").val();
	var msg = "";
	if (username == "") {
		msg = "用户名不能为空！";
	} else if (password == "") {
		msg = "密码不能为空！";
	} else if (code == "") {
		msg = "验证码不能为空！";
	} else {
		$.ajax( {
			url : 'user_login.htm',
			data : $("#loginForm").serialize(),
			dataType : 'json',
			type : 'POST',
			cache : false,
			success : function(data) {
				if (data.statusCode == "100") {
					$.pdialog.closeCurrent();
					$("#body").load('main.jsp');
				} else {
					$("#codeImg").attr("src", "VC.jpg?id=" + Math.random() * 10);
					$("#password").val("");
					 $("#code").val("");
					alertMsg.error(data.message);
				}
			},
			error : function() {
				alertMsg.error("登录异常！");
			},
			complete : function() {}
		});
	}
	if (msg != "") {
		alertMsg.error(msg);
	}
	return false;
}