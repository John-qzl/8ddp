
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>系统组织管理</title>
	<%@include file="/commons/include/get.jsp"%>
	<script type="text/javascript">
	forbidF5("Chrome");//禁止刷新页面
	var isSingle="${isSingle}";
		//树列表的收展
		function treeClick(obj) {
	
			var clazz = $(obj).attr("class");
			var id = $(obj).parents("tr").attr("id");
	
			if (clazz == "tree-list-minus") {
				toggleChild(id, "hide");
			} else if (clazz == "tree-list-plus") {
				toggleChild(id, "show");
			}
	
			//置换加减号
			$(obj).toggleClass("tree-list-minus");
			$(obj).toggleClass("tree-list-plus");
		};
	
		//子结点收展
		function toggleChild(parentId, type) {
			var child = $("tr[parentId='" + parentId + "']");
			$.each(child, function(i, c) {
				if (type == "hide") {
					$(c).hide();
				} else if (type == "show") {
					$(c).find("a[name='tree_a']").removeClass("tree-list-plus");
					$(c).find("a[name='tree_a']").addClass("tree-list-minus");
					$(c).show();
				}
	
				var id = $(c).attr("id");
				toggleChild(id, type);
			});
	
		};
	       function clickOrg(obj){
				
				if($(obj).attr("type")=="radio"){
					return;
				}
				var orgId = $(obj).val();
		
				var isChecked=false;
				if(obj.checked){
					isChecked=true;
				}else{
					isChecked=false;
				}
			}
			 
			 $(function(){
				 $("#sysOrgItem>tbody").find("tr").bind('click', function() {
					 if(isSingle=='true'){
						var rad=$(this).find('input[name=orgId]:radio');
						rad.attr("checked","checked");
					}else{
						var ch=$(this).find(":checkbox");
						window.parent.selectMulti(ch);
					}
				});	
				 
				 $(".pk").parent().parent().click(function(){
					 var pks = $(this).find(".pk");
					 if(pks.length>0){
						 clickOrg(pks.get(0));
					 }
				}); 
				 $("#chkall").bind("click",function(){
				        var checkAll=false;
						if($(this).attr("checked")){
							checkAll=true;	
						}
						var checkboxs=$(":checkbox",$("#sysOrgItem>tbody"));
						checkboxs.each(function(){
							if(checkAll){
								window.parent.selectMulti(this);
							}
						});
				 });
			 });
	
	//单选情况下选中目标便返回信息并关闭页面
	function singleCheck(){
		window.parent.selectOrg();
	}
	</script>
<style>
div.panel-top{
	border-top: 5px solid #f5f5f5;
	background: #fff;
}
.panel-search .title,.panel-search .drop{
	background: #fff;
}
a.button{
	border: 1px solid #347EFE;
	color: #347EFE;
    background: #fff;
}
a.button:hover{
	background: #347EFE;
	color: #fff;
}
.label{
	margin-right: 5px;
	color: #333;
    font-weight: bold;
}
input.inputText {
	border: 1px solid #D9D9D9;
	color: #666;
}
span.red{
	right: 0px;
    top: 0px;
}
.table-grid .even,.table-grid .odd{
	background-color: #fff;
}
</style>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-search" moreHeight="30">
				<form action="selector.do?" id="searchForm" method="POST" target="orgFrame">
					<div class="row">
						<input type="hidden" name="isSingle" value="${isSingle }">
						<span class="label">组织名:</span> 
						<input type="hidden" name="typeVal" id="typeVal" value="${typeVal }"/> 
						<input type="hidden" name="type" id="type"  value="${type }"/>
						<input type="hidden" name="orgId" id="orgId" /> 
						<input type="hidden" name="demId" id="demId" /> 
						<input type="text" id="orgName" name="orgName" 
						class="inputText" value="${param['orgName']}"/> &nbsp; 
						<a href="javascript:;" onclick="$('#searchForm').submit()" class='button'><span>查询</span></a>
					</div>
				</form>
		    </div>
	    </div>
	    <div class="panel-body">
		    <c:if test="${isSingle==false}">
				<c:set var="checkAll">
					<input type="checkbox" id="chkall" name="chkall" />
				</c:set>
			</c:if>
			<display:table name="sysOrgList" id="sysOrgItem" requestURI="selector.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<c:choose>
						<c:when test="${isSingle==false}">
							<input type="checkbox" class="pk" name="orgId" value="${sysOrgItem.orgId}#${sysOrgItem.orgName}">
						</c:when>
						<c:otherwise>
							<input type="radio" class="pk" name="orgId" onclick="singleCheck()" value="${sysOrgItem.orgId}#${sysOrgItem.orgName}">
						</c:otherwise>
					</c:choose>
					<input type="hidden" name="orgName" value="${sysOrgItem.orgName}">
				</display:column>
				<display:column property="orgName" title="名称" sortable="true" sortName="orgName" ></display:column>
				<display:column property="orgPathname" title="所在路径"  sortable="true" sortName="orgPathname" maxLength="250" ></display:column>
			</display:table> 
			<ibms:paging tableId="sysOrgItem" showExplain="true"/>
		</div>
	</div>
</body>
</html>