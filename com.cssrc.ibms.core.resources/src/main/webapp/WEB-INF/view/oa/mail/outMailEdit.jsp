<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<%@include file="/commons/include/form.jsp" %>
<f:sysparam paramname="uploadType" alias="uploadType"></f:sysparam>
<html>
<head>
	<title>邮件</title>
    <f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="from-jsp.css"></f:link>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=outMail"></script>
	<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/form-setting/editor_config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_api.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/ibms/oa/mail/MailDef.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<f:link href="jquery/plugins/attach.css" ></f:link>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.attach.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js" ></script>
	<script type="text/javascript">
	var menu
	var uploadType = '${uploadType}';	
	$(function() {
			loadTree("initTree");
			layout();
			var mailUeditor = new baidu.editor.ui.Editor({minFrameHeight:300,initialFrameWidth:'100%',lang:'zh_cn'});
			mailUeditor.render("content"); 
			$("a.run").click(function() {
				var obj=$(this);
				var addressName = $("#receiverAddresses").val();
				$('#content').val(mailUeditor.getContent());
				if(addressName == ""){
					$("#warn").show();
					setTimeout(function(){$("#warn").hide()}, 3000);
					return;
				}
				submit(2,obj);
			});
			$('#dataFormDraft').click(function() {
				var obj=$(this);
				var addressName = $("#receiverAddresses").val();
				$('#content').val(mailUeditor.getContent());
				if(addressName == ""){
					$("#warn").show();
					setTimeout(function(){$("#warn").hide()}, 3000);
					return;
				}
				submit(3,obj);
			});
			getMenu()
			$("input[address='true']").click(changeClass);
			
			$("#sortingByTimes").click(function() {
				loadTree("sortingByTimes");
			});
			$("#sortingByLasttime").click(function() {
				loadTree("sortingByLasttime");
			});
			
		});
		
		
		
		function submit(action,obj){
			if(obj.hasClass('disabled')) return false;
			var rtn=$("#outMailForm").valid();
			if(!rtn) return;
			$.ligerDialog.waitting("正在发送,请您耐心等待...");
			$('#types').val(action);
			$('#isReply').val(0);
			$('#outMailForm').get(0).submit();
		}

        //重 置
        function reset(obj) {
			$('#receiverAddresses').val('');
			$('#ccAddresses').val('');
			$('#bcCAddresses').val('');
			$('#title').val('');
			$('#fileIds').val('');
			$('#fileIdsShow').html('');
		}
        
		//布局
		function layout(){
			$("#layout").ligerLayout( {
				leftWidth : 230,
				onHeightChanged: heightChanged
			});
			//取得layout的高度
	        var height = $(".l-layout-center").height();
	        $("#linkmanTree").height(height-60);
		};
		
		//布局大小改变的时候通知tab，面板改变大小
	    function heightChanged(options){
	     	$("#linkmanTree").height(options.middleHeight - 60);
	    };
	    
		//树
		var linkmanTree;
		
		//加载树
		function loadTree(condition){
			var setting = {
				data: {
					key : {
						name: "linkAddress"
					},
					simpleData: {
						enable: true,
						idKey: "linkId"
					}
				},
				view:{
					showIcon:false
				},
				callback:{
					onClick: zTreeOnLeftClick,
					onRightClick : zTreeOnRightClick
				}
			};
			$.post("${ctx}/oa/mail/outMailLinkman/getOutMailLinkmanData.do",{condition:condition},function(result){
				linkmanTree= $.fn.zTree.init($("#linkmanTree"), setting, result);}
			);
		};
		
		//右击
		function zTreeOnRightClick(e, treeId, treeNode) {
			linkmanTree.selectNode(treeNode);
			menu.hide();
			if (treeNode.isRoot == 1) {//根节点时，把删除和编辑隐藏掉
				menu_root.show({
					top : e.pageY,
					left : e.pageX
				});
			} else {
				justifyRightClickPosition(e);
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
			}
		};
		
		//右键菜单
		function getMenu() {
			menu = $.ligerMenu({
				top : 100,
				left : 100,
				width : 100,
				items:<f:menu>
					[ 
					 {
						text : '删除',
						click : 'delNode',
						alias:'delOrg'
					}
					]
					</f:menu>       
			});
		}
		function delNode() {
			var treeNode=getSelectNode();
			var params = "id=" + treeNode.linkId;
			$.post("${ctx}/oa/mail/outMailLinkman/del.do", params, function(data) {
				var json =JSON.parse(data);
				if(json.result=='1'){
					linkmanTree.removeNode(treeNode);
					$.ligerDialog.success(json.message);
				}else{
					$.ligerDialog.warn(json.message);
				}
			});
		};
		function getSelectNode(){
			linkmanTree = $.fn.zTree.getZTreeObj("linkmanTree");
			var nodes = linkmanTree.getSelectedNodes();
			var treeNode = nodes[0];
			return treeNode;
		}
		//左击
		function zTreeOnLeftClick(event, treeId, treeNode){
			var txtAddress=$("input.focus[address='true']");
			if(txtAddress.length==0){
				 $.ligerDialog.warn("请选择要填入的地址","提示信息");
				 return;
			}
			var address=treeNode.linkAddress;
			address=address.substring(address.indexOf('(')+1,address.indexOf(')'));
			var thistemp=txtAddress.val();
			if(thistemp==''){
				txtAddress.val(address);
			}else{
				var arrtemp=thistemp.split(',');
				for(var i=0;i<arrtemp.length;i++){
					if(arrtemp[i]==address){
						txtAddress.val(thistemp); break;
					}else{
						txtAddress.val(thistemp+","+address);
					}
				}
			}
		};
		
		//改变样式
		function changeClass() {
			var obj=$(this);
			var aryAddress=$("input[address='true']");
			aryAddress.attr('class', 'inputText');
			obj.attr('class', "focus");
		};
		
		/*选择联系人*/
		function selectLinkMan(object){
			linkManDialog({callback:function (data){
				var arryName =[];
				var arryAddress = [];
				var addressObj=$(object).parent().children("input[type='text']");
				var nameObj=$(object).parent().children("input[type='hidden']");
				
				var beforeAddresses= addressObj.val();/*已经填写的地址*/
				var beforeNames=nameObj.val();
			
				for(var i = 0 ; i < data.length ; i++){
					var temddata = data[i].value.split("#");
					if(beforeAddresses.indexOf(temddata[2]) > -1){
						continue;
					}
					arryName.push(temddata[1]);
					arryAddress.push(temddata[2]);
				}
				var obj={name:arryName.join(","),address:arryAddress.join(",")};
				if(obj.address ==""){
					return;
				}
				if(beforeAddresses != ""){
					nameObj.val(beforeNames+','+obj.name);
					addressObj.val(beforeAddresses+','+obj.address);
				}
				else{
					nameObj.val(obj.name);
					addressObj.val(obj.address);
				}
			}});
		}
		/*联系人对话框*/
		function linkManDialog(conf){
			if(!conf) conf={};
			var url=__ctx + "/oa/oa/oaLinkman/selector.do";
			var dialog = null;
			dialog = DialogUtil.open({
				passConf : {dialog:dialog},
				url:url,
				title : "联系人",
				width : 600,
				height : 400,
				modal : true,
				resizable : true,
				buttons:[{
					text:'确定',
					onclick:function(){
						    var arrylinkman = dialog.jiframe.contents().find("input[name='linkmandata'][checked]");
							if(conf.callback){
								conf.callback(arrylinkman);
								dialog.close();
							}else{
								dialog.close();
							}
					}
				},{
					text:'取消',
					onclick: function (item,dialog) { dialog.close(); }
				}]
			});
		}
	</script>
</head>
<body>  
	<div id="layout">
		<div position="right" title="最近联系人">
		    <div class="tree-toolbar" id="pToolbar">
				<div class="toolBar"
					style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">
					<div class="group" >
						<a class="link reload" id="sortingByTimes" >次数</a>
					</div>
					<div class="group" >
						<a class="link reload" id="sortingByLasttime" >最后发送</a>
					</div>
				</div>
			</div>
			<div id="linkmanTree" class="ztree">
			</div>
		</div>
		<div id="editor" position="center"  style="overflow: auto;">
			<div position="center" style="height:100%;">
				<div class="panel">
					<div class="panel-top">
						<div class="tbar-title">
							<span class="tbar-label">新建邮件</span>
						</div>
						<div class="panel-toolbar">
							<div class="toolBar">
								<div class="group"><f:a alias="sendMail" css="link run" showNoRight="false">发送</f:a></div>
								
								<div class="group"><f:a alias="saveOutmail" css="link save" showNoRight="false" id="dataFormDraft">保存草稿</f:a></div>
								
							    <div class="group"><a class="link undo" id="dataFormRest" onclick="reset(this)">重置</a></div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<form id="outMailForm" method="post" action="send.do" >
							<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<th width="15%">发件人:<span class="required">*</span></th>
									<td>
								    <select id="senderAddresses"  name="senderAddresses" style="width: 35%;">
										<c:forEach items="${outMailUserSetingList}" var="mail">
										    <option value="${mail.mailAddress}" address="${mail.id}" <c:if test="${mail.isDefault==1}">selected='selected'</c:if>>${mail.mailAddress}</option>
									    </c:forEach>
									</select>
									</td>
								</tr>
								<tr  >
									<th width="15%">收件人:<span class="required">*</span></th>
									<td height="45" >
									    <input type="text" address="true"  id="receiverAddresses" name="receiverAddresses"  value="${outMail.receiverAddresses}"   class="inputText" style="width: 80%;"/>
										<input type="hidden"  id="receiverNames" name="receiverNames" value=""  class="inputText" style="width: 80%;"/>
									 	<a class="link search"  onclick="selectLinkMan(this);"><span ></span>选择</a>&nbsp;&nbsp;<span id="warn" style="color:red;" class="hidden">*收件人必填</span>
									<br>(注：可以直接单击最近联系人列表，也可手动输入，多个收件人以","号分割如两个收件人：aaa@163.com,bbb@163.com)
									</td>
								</tr>
								<tr>
									<th width="15%">抄送人: </th>
									<td>
									   <input type="text" address="true"  id="ccAddresses" name="ccAddresses" value="${outMail.ccAddresses}"  class="inputText"  style="width: 80%;"/>
									   <input type="hidden"  id="ccAddrName" name="ccAddrName" value=""  class="inputText" style="width: 80%;"/>
									 	<a class="link search"  onclick="selectLinkMan(this);"><span ></span>选择</a>
									</td>
								</tr>
								<tr >
									<th width="15%">暗送人: </th>
									<td>
									    <input type="text" address="true"  id="bcCAddresses" name="bcCAddresses" value="${outMail.bcCAddresses}"  class="inputText"  style="width: 80%;"/>
									    <input type="hidden"  id="bcCAddrName" name="bcCAddrName" value=""  class="inputText" style="width: 80%;"/>
									 	<a class="link search"  onclick="selectLinkMan(this);"><span ></span>选择</a>
									</td>
								</tr>
								<tr>
									<th width="15%">主题: </th>
									<td><input type="text" id="title" name="title"  value="${outMail.title}"  class="inputText"  style="width: 80%;"/></td>
								</tr>
								<tr>
									<th width="15%">附件: </th>
									<td>
										<input type="text" name="fileIds" id="fileIds" class="attach" attachType="1" value="${outMail.fileIds}" />
									</td>
								</tr>
								<tr>
								    <th width="15%">发送内容: </th>
									<td>
									<textarea  name="content" id="content" >${outMail.content}</textarea>
									</td>
								</tr>
							</table>
						
							<input type="hidden" name="mailId" value="${outMail.mailId}" />
							<input name="types" id="types" type="hidden" value="${mail.mailAddress}"/>
							<input type="hidden" id="isReply" name="isReply" value="${mail.isReply}"/>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
