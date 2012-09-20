String.prototype.format=function(){
	var args=arguments;
	return this.replace(/\{(\d+)\}/g,function (m,i){return args[i];});
};
String.prototype.format1=function(args){
	return this.replace(/\{(\d+)\}/g,function (m,i){
		return args[i];});
};
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
};
jQuery.fn.extend({
	//tab页
	tab:function(){
	var $tabthis=this;
	var $list=$(this).find(".tab_title>li");
	$list.addClass("nomorl");
	var $tabcontent=$(this).find(".tab_content");
	if($tabcontent==undefined||$tabcontent==null||$tabcontent.length<=0){
		$(this).append('<div class="tab_content"></div>');;
	}
	var $contentlist=$(this).find(".tab_content>div");
	if($contentlist==undefined||$contentlist.length<=0){
		var $newtabcontent=$(".tab_content");
		for(var i=0;i<$list.length;i++){
			$newtabcontent.append('<div></div>');
		}
	}else if($list.length<$list.length){
		for(var i=$list.length-1;i<$list.length;i++){
			$newtabcontent.append('<div></div>');
		}
	}
	
	$list.click(function (){
		$($tabthis).find(".tab_show").removeClass("tab_show").addClass("nomorl");
		$(this).addClass("tab_show");
		$($tabthis).find(".tab_content>div").hide();
		var $tabcontent=$($tabthis).find(".tab_content div:nth-child("+($list.index($(this))+1)+")");
		var isclicked=$(this).attr("isclicked");
		if(isclicked!=true){
			var url=$(this).attr("url");
			if(url!=undefined&&url!=''){
				$.ajax({url:url,success:function(html){$tabcontent.html(html);}});
			}
		}
		$tabcontent.show();
		$(this).attr("isclicked",true);
	});
	$(this).find(".tab_title>li:first").click();
	},
		// 分组合并table内容相同的单元格
		/**使用方法
		 *第一列合并
		 * $("#grouptable tr td:nth-child(1)").mergeRowCell();
		 * */
		mergeRowCell : function() {
			var rowObj = $(this)[0];// 记录需要合并的第一个单元格，会将其他重复的单元格删除
			var cfcount = 1;//重复数量
			var prevRowSpan=0;
			var prevRowSpantemp=0;
			var ttcount=0;
			this.each(function(i, row) {
				if($(rowObj).index()>0){
					var $rowPerv=$(rowObj).prev();
					if($rowPerv!=undefined){
						prevRowSpan=$rowPerv.attr("rowspan");
						ttcount=prevRowSpan;
						prevRowSpantemp=prevRowSpan;
					}
				}
				if (i > 0){
						if ($(row).text()== $(rowObj).text()){
							if(prevRowSpan!=cfcount&&ttcount<= prevRowSpan){
								cfcount++;
								$(row).remove();
								$(rowObj).attr("rowspan", cfcount);
							}else{
								cfcount = 1;
								rowObj = row;
							}
						} else {
							cfcount = 1;
							rowObj = row;
							if($(rowObj).index()>0){
								var $rowPerv=$(rowObj).prev();
								if($rowPerv!=undefined){
									prevRowSpan=$rowPerv.attr("rowspan");
									ttcount=prevRowSpan;
									prevRowSpantemp=prevRowSpan;
								}
							}
						}
				}
				ttcount--;
		});
		},
		//针对多值传递过来可以使用这样的方式valuefrom:'#id1,#id2,#id3',url:'xx.do?s={0}&b={1}&c={2}&传递输入框的值paramname=#key' key为 输入框的值s
		ajaxselect:function(opt){
			var option={valuefrom:''//接收某些值的对象id
						,valueto:''//选中后值接收对象
						,url:''//ajax访问的url 不带参数
						,data:''//url后面接的参数如data:'&p1={0}&p2={1}&p3={2}&keyname='
						,selectoption:''//显示option的div
							};
				option=opt;
			
			var $ajaxselect=$(this);
			var $selectDiv=$(option.selectoption);
			$(".selectdivclose").click(function(){$selectDiv.hide();});
			
			$ajaxselect.keyup(
			function (){
				$(document).ajaxStart(function(){
					var ajaxbg=$("#background,#progressBar");
					ajaxbg.hide();
					$(".progressBar,.background").hide();
				});
			//	$selectDiv.find("li").remove();
				var $this=$(this);
				var valueto=option.valueto;
				$(valueto).val("");
				if($this.val()==""){
					$selectDiv.hide();
					$selectDiv.find("li").remove();
				}
				var $ul=$selectDiv.find("ul");
				$ul.empty();
				var datastr="";
				//构建ajax传递的url及参数值
				if(option.valuefrom!=""){
				var valuefrom=option.valuefrom;
				var paramestr=new Array();
				var vas=valuefrom.split(",");
				$.each(vas,function(i,v){
					paramestr.push($(v).val());
					});
				//格式化数据到ajaxurl中
				datastr=option.data.format1(paramestr)+$this.val();
				}else{
					datastr=option.data+$this.val();
				}
				if($this.val()!="")
				$.ajax({url:option.url,data:datastr,dataType:'json',type:'POST',success:function(data){
					$selectDiv.hide();
					$ul.empty();
					if($(data).length<=0){
						$(valueto).val("");
					}else{
						//装载数据
						$.each(data,function(i,d){
							$ul.append("<li valueto='"+valueto+"' val='"+d.id+"'>"+d.name+"</li>");
						});	
						$selectDiv.show();$selectDiv.css("display","block");
					}
				},error:function(e){},cache:false,complete:function(){
					var $valuetoli=$selectDiv.find("li[valueto]");
					$valuetoli.click(function(){
						var $this=$(this);
						var $sto=$($this.attr("valueto"));
						$sto.val($this.attr("val"));
						$ajaxselect.val($this.text());
						$selectDiv.hide();
						var $ul=$selectDiv.find("ul");
						$ul.empty();
					});
					$valuetoli.hover(function(){
						$(this).removeClass("out").addClass("over");
						},function(){
							$(this).removeClass("over").addClass("out");
						});
					}
				});		
			}
			);
			$ajaxselect.focus(function(){
				$selectDiv.hide();
				$selectDiv.css({top:($ajaxselect.offset().top+$ajaxselect.height()+4)+"px",left:$ajaxselect.offset().left+"px"});
				var $ul=$selectDiv.find("ul");
				$ul.empty();
				$(document).ajaxStart(function(){});
			});
			$ajaxselect.blur(function(){
					var $ajaxbg=$("#background,#progressBar");
					$ajaxbg.hide();
					$(document).ajaxStart(function(){
						$ajaxbg.show();}).ajaxStop(function(){
							$ajaxbg.hide();});
			});
		}
	});
$.ajaxSetup ({
	cache: false
	});
  document.onkeydown = function() {
	if (event.keyCode == 116 // 屏蔽F5键 //
		//	|| event.keyCode == 122// 屏蔽F11键
	) {
		event.keyCode = 0;
		event.cancelBubble = true;
		return false;
	}
	if ((event.ctrlKey && event.keyCode == 82)) {// 屏蔽Ctrl+R
		event.keyCode = 0;
		event.cancelBubble = true;
		return false;
	}
	if ((event.ctrlKey && event.keyCode == 78)) {// 屏蔽Ctrl+N
		event.keyCode = 0;
		event.cancelBubble = true;
		return false;
	}
	if ((event.keyCode == 8)
			&& (event.srcElement.type != "text"
					&& event.srcElement.type != "textarea" && event.srcElement.type != "password")) {// 屏蔽BackSpace键
		event.keyCode = 0;
		event.cancelBubble = true;
		return false;
	}
};
//格式化控制
function format(number) {
	number = number.toString();
	if (number.indexOf(".") != -1) {//数值中有"."
		var numberArr = number.split(".");//将数值的整数部分与小数部分分开
		return formatInt(numberArr[0]) + "." + formatDouble(numberArr[1]);
	} else {//数值中无"."
		return formatInt(number);
	}
}

//格式化数字(整数)
function formatInt(number) {
	var len = Math.floor(number.length / 3);//计算需要几个","
	var remainder = number.length % 3;//计算第一个","前面有几位
	if (len < 1) {//长度不需要加","
		return number;
	}
	var array = number.split("");//将数字转化成数组
	var resultArray = new Array();//声明结果数组

	var j = 0;
	if (remainder != 0) {
		 var temp = "";
		for ( var i = 0; i < remainder; i++) {
			temp = temp + array[i];
		}
		resultArray[j] = temp;
		j = 1;
	}

	for ( var m = 0; m < len; m++) {
		resultArray[j] = "" + array[m * 3 + remainder]
				+ array[m * 3 + remainder + 1]
				+ array[m * 3 + remainder + 2];
		j++;
	}
	return (resultArray.join());
}

//格式化数字(小数)
function formatDouble(number) {
	var len = Math.floor(number.length / 3);//计算需要几个","
	var remainder = number.length % 3;//计算最后一个","后面有几位
	if (len < 1) {//长度不需要加","
		return number;
	}
	var array = number.split("");//将数字转化成数组
	var resultArray = new Array();//声明结果数组

	var j = 0;

	for (j = 0; j < len; j++) {
		resultArray[j] = "" + array[j * 3] + array[j * 3 + 1]
				+ array[j * 3 + 2];
	}

	if (remainder != 0) {
		resultArray[j] = "";
		for ( var i = 0; i < remainder; i++) {
			resultArray[j] += array[j * 3 + i];
		}
	}

	return (resultArray.join());
}

//利用Number对象的toLocaleString方法格式化
function format2(number) {
	return number.toLocaleString();
}
function initQueryForm(){
	$("select[val]").each(function(i){
		var $this=$(this);
		$this.find("option").attr("selected","");
		var val=$this.attr("val");
		if(val!=""){
			$this.find("option[value='"+val+"']").attr("selected","selected");
		}else{
			$("option ",$this).eq(0).attr("selected","selected");
		}
		$this.change();
		});
	$(":input[val]").each(function(i){
		var $this=$(this);
		var val=$this.attr("val");
		if(val!="")
		$this.val($this.attr("val"));
		});
	$(".searchBar [type='submit']").click(function(){
		$("[name='pageNum']").val(1);
	});
	$("textarea[val]").each(function(i){
		var $this=$(this);
		var val=$this.attr("val");
		if(val!=""){
			$this.html(val);
		}
		});
	$(".searchContent :text").each(function(i,item){
		$(item).css("width","75px");
	});
}