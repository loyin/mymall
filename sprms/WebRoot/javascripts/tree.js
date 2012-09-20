function getE(id) {
	return document.getElementById(id);
}
function updatestring(str1, str2, clear) {
	str2 = '_' + str2 + '_';
	return clear ? str1.replace(str2, '') : (str1.indexOf(str2) == -1 ? str1 + str2 : str1);
}
function checkChild(pnode){
	var nodes=document.getElementsByName(pnode.name);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].getAttribute("pid")==pnode.id){
			nodes[i].checked=pnode.checked;	
			checkChild(nodes[i]);
		}
	}
}
function checkParent(pnode){
	var nodes=document.getElementsByName(pnode.name);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].id==pnode.getAttribute("pid")){
			nodes[i].checked=pnode.checked;	
			checkParent(nodes[i]);
			checkChild2(nodes[i])
			break;
		}
	}
}
function checkChild2(pnode){
	var nodes=document.getElementsByName(pnode.name);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].getAttribute("pid")==pnode.id){
			if(nodes[i].checked==true)
			{
				pnode.checked=true;	
				break;
			}
				checkChild2(nodes[i]);
		}
	}
}
var icon = new Object();
icon.root		= IMGDIR + '/tree_root.gif';
icon.folder		= IMGDIR + '/tree_folder.gif';
icon.folderOpen		= IMGDIR + '/tree_folderopen.gif';
icon.file		= IMGDIR + '/tree_file.gif';
icon.empty		= IMGDIR + '/tree_empty.gif';
icon.line		= IMGDIR + '/tree_line.gif';
icon.lineMiddle		= IMGDIR + '/tree_linemiddle.gif';
icon.lineBottom		= IMGDIR + '/tree_linebottom.gif';
icon.plus		= IMGDIR + '/tree_plus.gif';
icon.plusMiddle		= IMGDIR + '/tree_plusmiddle.gif';
icon.plusBottom		= IMGDIR + '/tree_plusbottom.gif';
icon.minus		= IMGDIR + '/tree_minus.gif';
icon.minusMiddle	= IMGDIR + '/tree_minusmiddle.gif';
icon.minusBottom	= IMGDIR + '/tree_minusbottom.gif';

function treeNode(id, pid, name,img, url,doevent, open,check) {
	//public
	var obj = new Object();
	obj.id = id;
	obj.pid = pid;
	obj.name = name;
	obj.img=img;
	obj.url = url;
	obj.open = open;
	//private
	obj._isOpen = open;
	obj._lastChildId = 0;
	obj._pid = 0;
	obj.doevent=doevent;
	obj.check=check==null?true:check;
	return obj;
}
/**treeName :树的名称
 checkType：是复选还是单选。值有：'radio'\'chechbox'
 namevar1:控件的name值
 checkParent:true当选择子节点时选择父节点 默认是false*/
function dzTree(treeName,checkType1,namevar1,checkParent,checkChild){
	var checkType=checkType1==null?"":checkType1;
	var namevar=namevar1==null?"":namevar1;
	
	this.nodes = new Array();
	if(checkParent){
		this.checkParent=true;
	}else{
		this.checkParent=false;
	}
	this.checkChild=true;
	if(checkChild==false){
		this.checkChild=false;
	}
	this.pushNodes = new Array();
	this.addNode = function(id, pid, name,img, url,doevent, open,check) {
		var theNode = new treeNode(id, pid, name,img, url,doevent, open,check);
		this.pushNodes.push(id);
		if(!this.nodes[pid]) {
			this.nodes[pid] = new Array();
		}
		this.nodes[pid]._lastChildId = id;

		for(k in this.nodes) {
			if(this.nodes[k].pid == id) {
				theNode._lastChildId = this.nodes[k].id;
			}
		}
		this.nodes[id] = theNode;
	}
	this.addNodes=function (nodes){
		this.pushNodes = new Array();
		for(var j=0;j<nodes.length;j++){
			var check=nodes[j].check==null?true:nodes[j].check;
			this.addNode(nodes[j].id, nodes[j].pid, nodes[j].name,nodes[j].img, nodes[j].url,nodes[j].doevent, nodes[j].open,check);
		}
	}
	this.show = function() {
		var s = '<div class="tree">';
		s += this.createTree(this.nodes[0]);
		s += '</div>';
		document.write(s);
	}
	this.getHtml=function(){
		var s = '<div class="tree">';
		s += this.createTree(this.nodes[0]);
		s += '</div>';
		return s;
	}
	this.setHtml=function (divId){
		var s = '<div class="tree">';
		s += this.createTree(this.nodes[0]);
		s += '</div>';
		getE(divId).innerHTML=s;
	}
	this.clear=function(div){
		this.nodes = new Array();
		getE(div).innerHTML="";
	}
	this.createTree = function(node, padding) {
		padding = padding ? padding : '';
		if(node.pid == -1){
			var icon1 = '';
		} else {
			//' + treeName + '.switchDisplay(
			var icon1 = '<img src="' + this.getIcon1(node) + '"  onclick="switchDisplay(\''+treeName+'\',\'' + node.id + '\')"  id="'+treeName+'_icon1_' + node.id + '" style="cursor: pointer;">';
		}
		var icon2 = '<img src="' + this.getIcon2(node) + '"  onclick="switchDisplay(\''+treeName+'\',\'' + node.id + '\')"  id="'+treeName+'_icon2_' + node.id + '" style="cursor: pointer;" width="16" height="16">';
		var s = '<div class="node" id="'+treeName+'_node_' + node.id + '" ondblclick="switchDisplay(\''+treeName+'\',\'' + node.id + '\')" >' + padding + icon1 + icon2 + this.getName(node) + '</div>';
		s += '<div class="nodes" id="'+treeName+'_nodes_' + node.id + '" style="display:' + (node._isOpen ? '' : 'none') + '">';
		for(k in this.pushNodes) {
			var id = this.pushNodes[k];
			var theNode = this.nodes[id];
			if(theNode.pid == node.id) {
				if(node.id == 0){
					var thePadding = '';
				} else {
					var thePadding = padding + (node.id == this.nodes[node.pid]._lastChildId  ? '<img src="' + icon.empty + '">' : '<img src="' + icon.line + '">');
				}
				if(!theNode._lastChildId) {
					var icon1 = '<img src="' + this.getIcon1(theNode) + '"' + ' id="'+treeName+'_icon1_' + theNode.id + '">';
					var icon2 = '<img src="' + this.getIcon2(theNode) + '" id="'+treeName+'_icon2_' + theNode.id + '" width="16" height="16">';
					s += '<div class="node" id="'+treeName+'_node_' + theNode.id + '">' + thePadding + icon1 + icon2 + this.getName(theNode) + '</div>';
				} else {
					s += this.createTree(theNode, thePadding);
				}
			}
		}
		s += '</div>';
		return s;
	}

	this.getIcon1 = function(theNode) {
		var parentNode = this.nodes[theNode.pid];
		var src = '';
		if(theNode._lastChildId) {
			if(theNode._isOpen) {
				if(theNode.id == 0) {
					return icon.minus;
				}
				if(theNode.id == parentNode._lastChildId) {
					src = icon.minusBottom;
				} else {
					src = icon.minusMiddle;
				}
			} else {
				if(theNode.id == 0) {
					return icon.plus;
				}
				if(theNode.id == parentNode._lastChildId) {
					src = icon.plusBottom;
				} else {
					src = icon.plusMiddle;
				}
			}
		} else {
			if(theNode.id == parentNode._lastChildId) {
				src = icon.lineBottom;
			} else {
				src = icon.lineMiddle;
			}
		}
		return src;
	}

	this.getIcon2 = function(theNode) {
		if(theNode.img!=''){
			return theNode.img;
		}
		var src = '';
		if(theNode.id == 0 ) {
			return icon.root;
		}
		if(theNode._lastChildId) {
			if(theNode._isOpen) {
				src = icon.folderOpen;
			} else {
				src = icon.folder;
			}
		} else {
			src = icon.file;
		}
		return src;
	}
	this.getName = function(theNode) {
			if(checkType=='checkbox'&&theNode.pid!='-1'){//复选
				if(theNode.check==false)
					return '<span class="treeNodeSpan" style="cursor:pointer;" onclick="setTreeNodeBg(this);'+theNode.doevent+'">'+theNode.name+'</span>';
				else
				return '<span class="treeNodeSpan" style="cursor:pointer;" onclick="setTreeNodeBg(this);"><label title="'+theNode.name+'" for="'+treeName+'_'+theNode.id+'"><input type="checkbox"  onclick="'+(this.checkChild==true?'checkChild(this);':';')+theNode.doevent+';'+(this.checkParent?'checkParent(this);':';')+'" pid="'+treeName+'_'+theNode.pid+'" value="'+theNode.id+'" url= "'+theNode.url+'" name="'+namevar+'" id="'+treeName+'_'+theNode.id+'">'+theNode.name+'</label></span>';
			}else if(checkType=='radio'&&theNode.pid!='-1'){//单选
				if(theNode.check==false)
					return '<span class="treeNodeSpan" style="cursor:pointer;" onclick="setTreeNodeBg(this);'+theNode.doevent+'" title="'+theNode.name+'">'+theNode.name+'</span>';
				else
				return '<span class="treeNodeSpan" style="cursor:pointer;" onclick="setTreeNodeBg(this);" title="'+theNode.name+'"><label for="'+treeName+'_'+theNode.id+'"><input onclick="'+theNode.doevent+'" type="radio" value="'+theNode.id+'" name="'+namevar+'" id="'+treeName+'_'+theNode.id+'">'+theNode.name+'</label></span>';
			}
			else{
				if(theNode.url!=null&&theNode.url!="") {
				//显示连接
					return '<a class="treeNodeSpan"  href="'+theNode.url+'" target="' + theNode.target + '" title="'+theNode.name+'"> '+theNode.name+'</a>';
				}else{
					return "<span class='treeNodeSpan'  style='cursor:pointer;' onclick='"+theNode.doevent+"' title='"+theNode.name+"'>"+theNode.name+"</span>";
				}
			}
	}
	this.expandAll=function (){
		eval('var theTree = ' + treeName);
		for(var i=0;i<this.nodes.length;i++){
			var id;
			var theNode = theTree.nodes[i];
			if(theNode ==null||theNode ==' '||theNode.id ==null||theNode.id ==' ' )
			{continue;
			}else{
				id=theNode.id;
			}
			theNode._isOpen = true;
				var obj1=getE(treeName+'_nodes_' + id);
				if(obj1!=null)
				obj1.style.display = '';
				var obj2=getE(treeName+'_icon1_' + id);
				if(obj2!=null)
				obj2.src = theTree.getIcon1(theNode);
				var obj3=getE(treeName+'_icon2_' + id);
				if(obj3!=null)
				obj3.src = theTree.getIcon2(theNode);
		}
	}
}
function switchDisplay(treeName,nodeId) {
	eval('var theTree = ' + treeName);
	var theNode = theTree.nodes[nodeId];
	if(getE(treeName+'_nodes_' + nodeId).style.display == 'none') {
		theNode._isOpen = true;
			getE(treeName+'_nodes_' + nodeId).style.display = '';
		try{
			getE(treeName+'_icon1_' + nodeId).src = theTree.getIcon1(theNode);
		}catch(e){}
		try{
			getE(treeName+'_icon2_' + nodeId).src = theTree.getIcon2(theNode);
		}catch(e){}
	} else {
		theNode._isOpen = false;
		getE(treeName+'_nodes_' + nodeId).style.display = 'none';
		try{
			getE(treeName+'_icon1_' + nodeId).src =  theTree.getIcon1(theNode);
		}catch(e){}
		try{
			getE(treeName+'_icon2_' + nodeId).src = theTree.getIcon2(theNode);
		}catch(e){}
	}
}
function setTreeNodeBg(node){
	$("span.treeNodebg").attr("class","treenode");
	node.className="treeNodebg";
}