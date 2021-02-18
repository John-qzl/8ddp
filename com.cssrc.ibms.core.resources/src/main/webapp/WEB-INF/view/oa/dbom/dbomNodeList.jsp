<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/include/html_doctype.html"%>

<html>
<head>
<title>DBom数据显示</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/DBomTree.js"></script>

<script type="text/javascript">
	
		//当前点击的节点
		var curNode = '';
		//数据过滤条件
		var filter = new Array();
		//当前节点 所有URL
		var curNodeTabUrl = new Array();

		//DBom树构建
		var dbomTree = new DBomTree('${dbomCode}', 'dbomTree', {
			onClick : onClick
		});
		
		/**
		 * 初始化DBom树
		 */
		$(function(){
		    $('#dbomLayout').ligerLayout({leftWidth:210, height:'100%', allowLeftResize:true});
			dbomTree.loadDBomTree();
		});
		
		/**
		 * 展开收起 
		 */
		function treeExpandAll(type){
			dbomTree.treeExpandAll(type);
		};
		
		/**
		 * 树节点单击事件
		 */
		function onClick(treeNode){
			if(treeNode.tempUrl != ''){
				curNode = treeNode;
				//清空URL
				curNodeTabUrl.splice(0,curNodeTabUrl.length);
				//重置ul标签
				resetUl(treeNode.code);
				var ul = document.getElementById('dbomTab');
				// 点击节点只加载第一个tab 页的数据
				reloadDataByTree(0);
			}
		};
		
		/**
		 * 点击节点时候，获取过滤条件，直至树的根节点
		 */
		function getAllFilter(filterArray, treeNode){
			var parentId = treeNode.parentId;
			if(!parentId||parentId == '${dbomCode}'){
				//当节点的parentId为dbom tree code，表示已经递归到根节点的第一个节点
				return filterArray;
			}else{
				filterArray.push({
					code : treeNode.code,
					text : treeNode.text
				});
				getAllFilter(filterArray, treeNode.getParentNode());
			}
			
		};
		
		/**
		 * 重置ul标签
		 */
		function resetUl(code){
			$.ajax({
				url : __ctx + '/oa/system/dbomNode/getTab.do',
				data : {code : code},
				async: false,
				type : 'post',
				success : function(result){
					var tabs = result.tabs;
					var ul = document.getElementById('dbomTab');
					var length = ul.children.length;
					//删除li标签
					for(var index=0; index<length; index++){
						ul.removeChild(ul.children[0]);
					}
					for(var index=0; index<tabs.length; index++){
						//加载tab 页时是按照 tab url 的index 来获取 curNodeTabUrl中的具体url
						curNodeTabUrl.push(tabs[index]);
						//获取tab 页显示的标题。这个需要配置在 dbom 树的节点中
						var tabName=getQueryString(tabs[index],"__title__")
						//添加一个tab页
						addLi(ul, tabName, tabName, index);
					}
				}
			});
		};

		/**
		 * 获取URL中的参数
		 */
		function getQueryString(url,name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var  query=url.split("?")[1];
			var r = query.match(reg);
			if (r != null){
				return unescape(r[2]);
			}else{
				return null;
			}
		}
		/**
		 * 重置li标签
		 */
		function addLi(ul, tableName, tableDesc, index) {
			//添加li标签
			var li = document.createElement('li');
			var a = document.createElement('a');
			//为tab页的a标签添加 click 事件， 事件参数为 url 在curNodeTabUrl 数组中的index
			a.href = 'javascript:reloadDataByTree("' + index + '")';
			a.innerHTML = tableDesc;
			a.id = 'a_' + tableName;
			if (index == 0) {
				li.className = "active-li";
			} else {
				li.className = "unactive-li";
			}
			li.id = tableName;
			li.appendChild(a);
			ul.appendChild(li);
		};

		/**
		 * 点击tab 页 加载数据  urlIndex 为 url 在curNodeTabUrl 数组中的index
		 */
		function reloadDataByTree(urlIndex) {
			if (curNode == '') {
				$.ligerDialog.warn('请先点击某个数据节点！', '提示信息');
				return;
			}
			//通过 curNodeTabUrl[urlIndex] 直接从curNodeTabUrl 中获取 要加载的url
			var url=__ctx + curNodeTabUrl[urlIndex];
			var pNode=curNode.getParentNode();
			//重新加载数据
			if(pNode){
				$.ajax({
					url : __ctx + '/oa/system/dbomNode/getURLParams.do',
					data : {
						code : curNode.code,
						value: curNode.id,
						urlIndex:urlIndex
					},
					type : 'post',
					success : function(result){
						//修改标签样式
						$('#dbomTab').children(".active-li").attr('class', 'unactive-li');
						var curtab=$('#dbomTab').children()[urlIndex];
						$(curtab).attr('class', 'active-li');
						if(result.url&&result.url!=""){
							$('#dbomFrame').attr('src', "${ctx}"+result.url);
						}
					}
					
				});
				
			}else {
				$('#dbomFrame').attr('src',url);
			}
		};
	</script>

</head>

<body>

	<div id="dbomLayout" class="panel-top" style="height: 80%">
		<div position="left" title="${dbomName}" style="overflow: auto; float: left; width: 100%; height: 96%">
			<div class="tree-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link reload" id="treeFresh" href="javascript:dbomTree.loadDBomTree();"></a>
					</div>
					
					<div class="group">
						<a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)"></a>
					</div>
					
					<div class="group">
						<a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)"></a>
					</div>
				</div>
			</div>
			<ul id="dbomTree" class="ztree" style="height: 94%"></ul>
		</div>
		<div position="center">
			<ul id="dbomTab">
			</ul>
			<div style="height: 100%;margin-top: 10px;">
				<iframe id="dbomFrame" height="96%" width="100%" frameborder="0" src=""></iframe>
			</div>
		</div>
	</div>

</body>
</html>
