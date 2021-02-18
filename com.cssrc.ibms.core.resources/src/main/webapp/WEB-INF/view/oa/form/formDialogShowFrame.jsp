<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>
<%@include file="/commons/include/html_doctype.html"%>
<%
	request.setAttribute("toReplace", "\"");
	request.setAttribute("replaceBy", "\\\"");
%>
<html>
<head>
	<title>显示通用对话框</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@include file="/commons/include/color.jsp"%>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<f:link href="web.css" ></f:link>
	<script type="text/javascript">
	  var __ctx='<%=request.getContextPath()%>';
	  var __jsessionId='<%=session.getId() %>';
	</script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/calendar/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/foldBox.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>  <!--panel-body高度计算-->
	<c:choose>
		<c:when test="${bpmFormDialog.style==1}">
			<link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css" />
			<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
			<script type="text/javascript">
				var json=${bpmFormDialog.displayfield};
				var isMuliti=${bpmFormDialog.issingle==0};
				var rpcrefname = '${rpcrefname}';//加入rpc远程接口参数
				
				//能够唯一标识自定义对话框上一次选中的数据字段值集合
				var dialogValue = '${dialogValue}';
				//对应的在自定义对话框中的字段名称，一般来说是ID
				var dialogKeyName = '${dialogKeyName}';
				
				var dialogTree;
				var returnField='${bpmFormDialog.resultfield}';
				var url="${ctx}/oa/form/formDialog/getTreeData.do?alias=${bpmFormDialog.alias}&rpcrefname="+rpcrefname;
				$(function(){
					var setting = {
						async:{
							enable:true,
							url:url,
							autoParam:[json.id+"=idKey",json.pid+"=pidKey",json.displayName+"=nameKey"],
							otherParam:{idKeyName:json.id,pidKeyName:json.pid,nameKeyName:json.displayName}
						},
						data: {
							key : {name: json.displayName},
							simpleData: {
								enable: true,
								idKey: json.id,
								pIdKey: json.pid
							}
						},
						check: {
							enable: isMuliti,
							chkboxType:  { "Y" : "", "N" : "p" }
						},
						callback:{onClick: zTreeOnClick,
							onCheck:zTreeOnCheck,
							beforeExpand:zTreeBeforeExpand,
							onAsyncError:zTreeOnAsyncError}
						
					};
					//一次性加载
					//var url="${ctx}/oa/flow/formDialog/getTreeData.do?alias=${bpmFormDialog.alias}";
					var initUrl = url+"&isRoot=1&${urlPara}" ;
					$.post(initUrl,function(result){
						if(result!="")
							dialogTree=$.fn.zTree.init($("#dialogTree"), setting,result);
						dialogTree.expandAll(true);
						//通过返回值来勾选对话框中的数据
						if(dialogValue!=null&&dialogValue!=""){
							var dialogValues = dialogValue.split(",");
							for(var i=0;i<dialogValues.length;i++){
								//获取所有树节点
								var nodes = dialogTree.getNodes();
								for(var t=0;t<nodes.length;t++){
									if(nodes[t][dialogKeyName]==dialogValues[i]){
										dialogTree.checkNode(nodes[t],true,false);
										window.top.__resultData__=getResult();
									}
									var nodeChildren = nodes[t].children;
									if(nodeChildren!=undefined&&nodeChildren!=null){
										for(var j=0;j<nodeChildren.length;j++){
											if(nodeChildren[j][dialogKeyName]==dialogValues[i]){
												dialogTree.checkNode(nodeChildren[j],true,false);
												window.top.__resultData__=getResult();
											}
										}
									}
								}
							}
						}
						//dialogTree.expandAll(true);
					})
				});
				function  zTreeOnClick(event, treeId, treeNode) {
					$('.ztree span.node_name').bind('click',function(){
						$('.ztree span.node_name').removeClass('active');
						$(this).addClass('active');
					})
					if(!isMuliti){
						window.top.__resultData__=getResult();
					}
					if(window.parent.treeClick){
						window.parent.treeClick(getResult());
					}
				};
				
				function zTreeOnCheck(event, treeId, treeNode) {
					if(isMuliti){
						window.top.__resultData__=getResult();
					}
				};

				function zTreeBeforeExpand(treeId,treeNode) {
					if(!treeNode.isAjaxing){
						ajaxGetNodes(treeNode,"refresh");
					}
				};
				function zTreeOnAsyncError(treeId,treeNode) {
					$.ligerDialog.error('出错了！','提示');
				};

				function ajaxGetNodes(treeNode,reloadType){
					dialogTree.reAsyncChildNodes(treeNode,reloadType,true);
				};
				
				function getResult(){
					var aryField=$.parseJSON(returnField);
					if(isMuliti){
						var aryRtn=new Array();
						var nodes = dialogTree.getCheckedNodes(true);
						if(nodes.length<1){
							return -1;	
						}
						
						for(var i=0;i<nodes.length;i++){
							var obj=new Object();
							var node=nodes[i];
							for(var j=0;j<aryField.length;j++)	{
								var field=aryField[j].field;
								obj[field]=node[field];
							}
							aryRtn.push(obj);
						}
						return aryRtn;
					}
					else{
						var nodes = dialogTree.getSelectedNodes();
						if(nodes.length<1){
							return -1;
						}
						var obj=new Object();
						var node=nodes[0];
						for(var i=0;i<aryField.length;i++)	{
							var field=aryField[i].field;
							obj[field]=node[field];
						}
						return obj;
					}
				}
				function unCheckedAll(){
					dialogTree.checkAllNodes(false);
					dialogTree.cancelSelectedNode();
				}
			</script>
		</c:when>
		<c:otherwise>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormUtil.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorInit.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
		<f:js pre="jslib/lang/common" ></f:js>
		<f:js pre="jslib/lang/js" ></f:js>
		<f:link href="form.css" ></f:link>
		<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
			<script type="text/javascript">
				var isSingle='${bpmFormDialog.issingle}';
				var isIE=!!window.ActiveXObject;
				var isIE8=isIE&&!!document.documentMode;
				function getResult(){
					var tableObj=$("#bpmFormDialogTable");
					if(isSingle=='1'){ //单选
						var obj=$("input.pk:checked",tableObj);
						if(obj.length<1){
							return -1;
						}
						var json=eval("(" + obj.next("textarea").val() +")");
						return json;
					}
					else{  //多选
						if(parent.checkBoxData.length<1){
							return -1;
						}
						return parent.checkBoxData;
					}
				}
				function fieldIDChange(obj){
					var fieldID = $(obj).val();
					if(fieldID){
						$(obj).siblings('input:text').val(fieldID);
					}
				}
				
				//全选时对每个数据进行处理
				function checkAllSub(checked){
					$("input[type='checkbox'][class='pk']").each(function(){
						var textarea=$(this).next("textarea");
						var tr = $(this).closest('tr');
						window.parent.onChecked($(this),tr,textarea);
						$(this).attr("checked", checked);
					});
				}
				//用于父页面调用，进行复选框反选，IE8下，删除单个已选元素无效
				function unChecked(obj){
					if(isIE8 || !obj) return ;
					$(obj).attr('checked',false);
				}
				function unCheckedAll(){
					$("input.pk:checked",$("#bpmFormDialogTable")).each(function(){
						$(this).attr('checked',false);
					});
				}
				
				$(function(){
					$("#chkall").click(function(){
						var state=$(this).attr("checked");
						if(state==undefined)
							checkAllSub(false);
						else
							checkAllSub(true);
					});
					
					//通过返回值来勾选对话框中的数据
					var dialogValue = '${dialogValue}';
					var dialogKeyName = '${dialogKeyName}';
					if(dialogValue!=null&&dialogValue!=""){
						var dialogValues = dialogValue.split(",");
						for(var i=0;i<dialogValues.length;i++){
							$("#bpmFormDialogTable").find("tr:not(:first)").each(function() {
								var ch=$(this).children().find("[name='rtn']");
								var textarea=$(ch).next("textarea");
								var tr = $(ch).closest('tr');
								var json = eval("(" + $(textarea).val() + ")");
								//判断json中是否包含唯一的id
								if(json[dialogKeyName]==dialogValues[i]){
									var rad=$(this).find("input[name='rtn']");
									rad.attr("checked","checked");
									window.parent.onChecked(ch,tr,textarea);
									window.top.__resultData__=getResult();
								}
							});
						}
					}
					
					
					//通过查看的已选中的数据来确认哪些数据需要勾选
					if(window.parent.checkBoxData != ""){
						var textareaArrs = window.parent.checkBoxData;
						for(var i=0;i<textareaArrs.length;i++){
							$("#bpmFormDialogTable").find("tr:not(:first)").each(function() {
								var ch=$(this).children().find("[name='rtn']");
								var textarea=$(ch).next("textarea");
								var json = eval("(" + $(textarea).val() + ")");
								//判断两个对象是否相等,方法在util.js中
								if(checkObjectSame(json,textareaArrs[i])){
									var rad=$(this).find("input[name='rtn']");
									rad.attr("checked","checked");
								}
							});
						}
					}
					
					$("body").bind("click",function(){
						window.top.__resultData__=getResult();
					});
					$('input[id="dialog"]').live('click',function(){
						var target = $(this).attr('param');
						var dialog = $(this).attr('dialog');
						var me = $(this);
						CommonDialog(dialog,function(data){
							if(Object.prototype.toString.call((data)) == '[object Array]'){
								for(var i=0,d;d=data[i++];){
									me.prev('input:text').val(d[target]);
								}
							}else{
								me.prev('input:text').val(data[target]);
							}
						});
					});
					$("#bpmFormDialogTable").unbind('click');
					$("#bpmFormDialogTable").find("tr").bind('click', function() {
						if(isSingle=='true'){
							var rad=$(this).find('input[name=rtn]');
							rad.attr("checked","checked");
						}else{
							var ch=$(this).find("[name='rtn']");
							var textarea=$(ch).next("textarea");
							var tr = $(ch).closest('tr');
							window.parent.onChecked(ch,tr,textarea);
						}
					});
					//将table的菜单栏传到父页面中
					var toolsTr = $("#bpmFormDialogTable").find("tr");
					window.parent.getToolsTr(toolsTr);
				})
			</script>
		</c:otherwise>
	</c:choose>
</head>
<body>
	<c:choose>
		<c:when test="${bpmFormDialog.style==1}">
			<div id="dialogTree" class="ztree"></div>
		</c:when>
		<c:otherwise>
			<div class="panel">
				<div class="panel-top">
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group"><a class="link search" id="btnSearch">查询</a></div>
							<c:if test="${bpmFormDialog.issingle==0}">
								
								<div class="group"><a class="link detail" id="viewSelected" onclick="parent.viewSelected()">查看已选</a></div>
							</c:if>
						</div>
					</div>
					<div class="panel-search">
						<form id="searchForm" method="post" action="showFrame.do">
							<ul class="row">
								<c:if test="${fn:length(bpmFormDialog.conditionList)>0}">
									<c:forEach items="${bpmFormDialog.conditionList}" var="col" >
										<c:if test="${col.defaultType==4}">
											<c:forEach items="${paramsMap}" var="pm">
												<c:if test="${pm.key==col.fieldName }">
													<input type="hidden" name="${col.fieldName}" value="${pm.value }" />
												</c:if>
											</c:forEach>
										</c:if>
										<c:if test="${col.fieldType=='isAfferent' and col.defaultType==5}">
											<c:forEach items="${paramsMap}" var="pm">
												<c:if test="${pm.key==col.fieldName }">
													<input type="hidden" name="${col.fieldName}" value="${pm.value }" />
												</c:if>
											</c:forEach>
										</c:if>
										<c:if test="${col.defaultType==1 or (col.defaultType==5 and col.fieldName!='' and col.fieldType!='isAfferent')}">
										<li><span class="label">${col.comment }:</span>
										<c:choose>
											<c:when test="${col.fieldType=='date'}">
												<c:choose>
													<c:when test="${col.condition=='='}">
														<input type="text" name="Q_${col.fieldName}_DL" class="date inputText"  />
													</c:when>
													<c:otherwise>
														从:<input type="text" name="Q_start${col.fieldName}_DL" class="date inputText" />
														</li>
														<li><span class="label">到:</span>
														<input type="text" name="Q_end${col.fieldName}_DG" class="date inputText" />
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${col.fieldType=='number'}">
														<input type="hidden" name="${col.fieldName}" class="inputText" disabled="true"/>
														<input type="hidden" name="${col.fieldName}ID" class="inputText" disabled="true" onchange="fieldIDChange(this)"/>
														<input type="text" name="Q_${col.fieldName}_DB"  class="inputText" />
													</c:when>
													<c:otherwise>
														<input type="hidden" name="${col.fieldName}" class="inputText" disabled="true"/>
														<input type="hidden" name="${col.fieldName}ID" class="inputText" disabled="true" onchange="fieldIDChange(this)"/>
														<input type="text" name="Q_${col.fieldName }_S"  class="inputText" />
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${col.paraCt=='0'}">
														<input type="button" value="…" id="dialog" dialog="${col.dialog}" param="${col.param}"/>
													</c:when>
													<c:when test="${col.paraCt=='4'}">
														<a href="####" class="link user" name="${col.fieldName}">用户</a>
													</c:when>
													<c:when test="${col.paraCt=='17'}">
														<a href="####" class="link role" name="${col.fieldName}">角色</a>
													</c:when>
													<c:when test="${col.paraCt=='18'}">
														<a href="####" class="link org" name="${col.fieldName}">组织</a>
													</c:when>
													<c:when test="${col.paraCt=='19'}">
														<a href="####" class="link pos" name="${col.fieldName}">岗位</a>
													</c:when>
												</c:choose>
											</c:otherwise>
										</c:choose>
										</li>
										</c:if>
									</c:forEach>
								</c:if>
							</ul>
							<input type="hidden" name="dialog_alias_" value="${bpmFormDialog.alias}">
						</form>
					</div>
				</div>
				<div class="panel-body">
						<div class='panel-table'>
						<table id="bpmFormDialogTable" cellpadding="1" class="table-grid table-list" cellspacing="1">
							<thead>
							<tr>
							<th>
								<c:if test="${bpmFormDialog.issingle==0}">
									<input type="checkbox" id="chkall"/>
								</c:if>
							</th>
							<th>序号</th>
							<c:forEach items="${bpmFormDialog.displayList}" var="field">
								<c:set var="sortMark">
									<c:if test="${sortField==field.fieldName}">
										<c:choose>
											<c:when test="${orderSeq=='ASC'}">↑</c:when>
											<c:otherwise>↓</c:otherwise>
										</c:choose>
									</c:if>
								</c:set>
								<th><a href="${baseHref}&newSortField=${field.fieldName}">${field.comment }${sortMark}</a></th>
							</c:forEach>
							</thead>
							<tbody>
							    
								<c:forEach items="${bpmFormDialog.list}" var="row" varStatus="status">
									<c:set var="clsName" ><c:choose><c:when test="${status.index %2==0 }">even</c:when><c:otherwise>odd</c:otherwise></c:choose></c:set>
									<tr class="${clsName}">
										<td>
											<c:choose>
												<c:when test="${bpmFormDialog.issingle==1}">
													<input type="radio" name="rtn" class="pk" value="${status.index}" />
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="rtn" class="pk" />
												</c:otherwise>
											</c:choose>
											<textarea style="display:none">{<c:forEach items="${bpmFormDialog.returnList}" var="field" varStatus="tempStatus"><c:choose><c:when test="${tempStatus.last}">"${field.fieldName}":"${fn:replace(row[field.fieldName],toReplace,replaceBy) }"</c:when><c:otherwise>"${field.fieldName}":"${fn:replace(row[field.fieldName],toReplace,replaceBy) }",</c:otherwise></c:choose></c:forEach>}</textarea>
										</td>
	            						<td><c:out value="${status.count}"/></td>
										<c:forEach items="${bpmFormDialog.displayList}" var="field">
											<td>${row[field.fieldName] }</td>	
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
						<c:if test="${bpmFormDialog.needpage==1 }">
						${pageHtml }
						</c:if>
					
				</div><!-- end of panel-body -->				
			</div> <!-- end of panel -->
		</c:otherwise>
	</c:choose>	
       	 
</body>
</html>
