<%@page pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html"%>
<html>
	<head>
		<title>选择角色</title>
		<%@include file="/commons/include/form.jsp" %>
		<f:link href="tree/zTreeStyle.css"></f:link>
	    <script type="text/javascript"	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	    <script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js" ></script>
		
 
		<script type="text/javascript">
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		
		var type ="";
		var typeVal="";
		
		var scope =eval("("+dialog.get("scope")+")"); 
		if(scope){
			type = scope.type;
			typeVal = scope.value; 
		}
		
			var isSingle="${isSingle}";
			var isGrade="${isGrade}";
			var findStr = '';
			forbidF5("Chrome");//禁止刷新页面
			$(function(){
				$("#defLayout").ligerLayout({ leftWidth: 190,rightWidth: 170,allowRightResize:false,allowLeftResize:false,allowTopResize:false,allowBottomResize:false,height: '90%',minLeftWidth:170});
				//快速查找
				$("input.quick-find").bind('keyup',function(){
					var str = $(this).val();
					if(!str)return;
					if(str==findStr)return;
					findStr = str;
					var  tbody = $("#roleList"), 	
						 firstTr = $('tr.hidden',tbody);
					$("tr",tbody).each(function(){
						var me = $(this),
							span = $('span',me),
							spanStr = span.html();
						if(!spanStr)return true;						
						if(spanStr.indexOf(findStr)>-1){
							$(this).insertAfter(firstTr);
						}
					});
				});
				initData();
				$("#roleFrame").attr("src","${ctx}/oa/system/sysRole/selector.do?isSingle=${isSingle}&isGrade=${isGrade}&type="+type+"&typeVal="+typeVal);
			});
			
			//初始化父级窗口传进来的数据
			function initData(){
				var obj = dialog.get("arrys");
				if(obj&&obj.length>0){
					for(var i=0,c;c=obj[i++];){
						var data = c.id+'#'+c.name;
						if(c.name!=undefined&&c.name!="undefined"&&c.name!=null&&c.name!=""){
							add(data);
						}
					}
				}
			};
			
			function treeClick(event, treeId, treeNode){
        		var url="${ctx}/oa/system/sysRole/selector.do?isSingle=${isSingle}&isGrade=${isGrade}&type="+type+"&typeVal="+typeVal;
        		$("#roleFrame").attr("src",url);
        	} 
			
			function selectRole(){
				var pleaseSelect= "请选择角色!";
				//单选
				if(isSingle=="true"){
					var chIds = $('#roleFrame').contents().find("input[name='roleId']:checked");
					if(chIds.length==0){
						alert("请选择");
						return;
					}
					var data=chIds.val();
					var aryRole=data.split("#");
					var obj={};
					obj.roleId=aryRole[0];
					obj.roleName=aryRole[1];
					dialog.get("sucCall")(obj);
				
				}
				//复选
				else{
					var aryRoles =$("input[name='role']", $("#roleList"));
					if(aryRoles.length==0){
						alert(pleaseSelect);
						return;
					}
					var aryId=[];
					var aryName=[];
					var json = [];
					aryRoles.each(function(){
						var data=$(this).val();
						var aryRole=data.split("#");
						aryId.push(aryRole[0]);
						aryName.push(aryRole[1]);
						json.push({id:aryRole[0],name:aryRole[1]});
					});
					var roleIds=aryId.join(",");
					var roleNames=aryName.join(",");
					
					var obj={};
					obj.roleId=roleIds;
					obj.roleName=roleNames;
					obj.roleJson = json;
					dialog.get("sucCall")(obj);
				}
				
				dialog.close();
			}
			function add(data) {
				var aryTmp=data.split("#");
				var roleId=aryTmp[0];
			
				var len= $("#role_" + roleId).length;
				if(len>0) return;
				var roleTemplate= $("#roleTemplate").val();
				
				var html=roleTemplate.replace("#roleId",roleId)
						.replace("#data",data)
						.replace("#name",aryTmp[1]);
				$("#roleList").append(html);
			};
		
			function selectMulti(obj) {
				if ($(obj).attr("checked") == "checked"){
					var data = $(obj).val();
					add(data);
				}	
			};
			
			function dellAll() {
				$("#roleList").empty();
			};
			function del(obj) {
				var tr = $(obj).closest("tr");
				$(tr).remove();
			};
			//清空角色
			function clearRole(){
				var rtn={roleId:'',roleName:''};
				dialog.get('sucCall')(rtn);
				dialog.close();
			}
	</script>
	<f:link href="from-jsp.css"></f:link>
	<style type="text/css">
		/** 将图标隔开，勿删*/
		.l-layout-right .l-layout-header-inner {
			padding-left: 22px;
		}
	</style>
	
	</head>
	<body >
		<div id="defLayout" >
			<div position="center">
          		<iframe id="roleFrame" name="roleFrame" height="100%" width="100%" frameborder="0" ></iframe>
            </div>
            <c:if test="${isSingle=='false'}" >
	            <div position="right" title="<span><a onclick='javascript:dellAll();' class='link del'>清空</a></span>" style="overflow: auto;height:95%;width:170px;">
      				<table width="345"   class="table-grid table-list"  cellpadding="1" cellspacing="1">
	      				<tbody id="roleList">
	      					<tr class="hidden"></tr>
	      				</tbody>
					</table>
			    </div> 
   	  	    </c:if>
		</div>
		<div position="bottom"  class="bottom">
	      	<a href="####" class="button"  onclick="selectRole()" style="margin-right:10px;" ><span class="icon ok"></span><span class="chosen">选择</span></a>
			<a href="####" class="button"  onclick="clearRole()"><span class="icon cancel" ></span><span class="chosen" >清空</span></a>
			<a href="####" class="button"  onclick="dialog.close()" style="margin-left:10px;" ><span class="icon cancel" ></span><span class="chosen" >取消</span></a>
		 </div>
		 <textarea id="roleTemplate" style="display: none;">
		  	 <tr id="role_#roleId">
	  			<td>
	  				<input type="hidden" name="role" value="#data"><span>#name</span>
	  			</td>
	  			<td style="width: 30px;" nowrap="nowrap"><a onclick="javascript:del(this);" class="link del" title="删除" >&nbsp;</a></td>
			  </tr>
	  	  </textarea>
	</body>
</html>