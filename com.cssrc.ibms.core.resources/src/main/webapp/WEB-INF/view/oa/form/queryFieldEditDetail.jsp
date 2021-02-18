<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>设置字段信息 </title>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="reminder.css" ></f:link>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	$(function(){
		init();
	});
	function init() {
		//处理下拉框按钮内容的上下删操作
		$("#option-table>tbody").delegate('tr.editable-tr','mouseover mouseleave', function(e) {
	 		 if (e.type == 'mouseover') {
	            $(this).addClass('hover');
	        } else {
	            $(this).removeClass('hover');
	        } 
	    });
	    //删除选项
	    $("#option-table>tbody").delegate('a.del','click',function(e){
	        stopBubble(e);
	       /* if(confirm('确认删除？')){ */
	        var me = $(this),           
            tr = me.parents('tr.editable-tr');
	        tr.remove();
	       /* } */
	    });
	    //上移选项
	    $("#option-table>tbody").delegate('a.moveup','click',function(e){
	      	stopBubble(e);
	      	move($(this),true);
	    });
	    //下移选项
	    $("#option-table>tbody").delegate('a.movedown','click',function(e){
	      	 stopBubble(e);
	      	 move($(this),false);
	    });
	    //新增选项
	    $("a.add").click(function() {
	        var data = {
	            key: "",
	            value: ""
	        }, newTr = addColumn(data);
	
	        var tbody = $('#option-table>tbody');
	        tbody.append(newTr);
	    });
	  	//初始化，所有key-value值
	    $("a.setting").click(function() {
	    	var tableName = $('#option-table input[name=tableName]').val().trim();
	    	var fieldName ="${sysQueryField.name}";
	    	if(tableName==""){
	    		$.ligerDialog.warn("表名不能为空！");
	    		return;
	    	}
	    	var newTr;
	    	$.ajax({
	    		url : __ctx + '/oa/form/formTable/getOpinonData.do',
	    		data : {tableName:tableName,fieldName:fieldName},
	    		async: false,
	    		type : 'post',
	    		success : function(result){
	    			if(result){
	    				var options = $.parseJSON(result);
	    				var newTr;
	    				var tbody = $('#option-table>tbody');
	    				for(var i=0;i<options.length;i++){
	    					newTr = addColumn(options[i]);
	    					tbody.append(newTr);
	    				}
	    			}else{
	    				$.ligerDialog.warn("没有取到数据！");
	    	    		return;
	    			}
	    		}
	    	});	
	    });
	    //初始化下拉框
	     if('${sysQueryField.controlType}'==11){
		    initOptControlContent();
	     }
	    //初始化number的format格式化
	    if("${sysQueryField.type}"=="number"){
		    initNumFormat();
	    }
	};
	
	//控件类型change触发事件
	function hdlControlType(){
		//选中控件类型
		var val=$("#controlType").val();
		//处理显示的逻辑
		if(val==3){
			$("#trDict").show();
			$("#trOption").hide();
		}else if(val==11){
			$("#trOption").show();
			$("#trDict").hide();
		}else{
			$("#trOption").hide();
			$("#trDict").hide();
		}
	}
	
	//数字类型选择货币后要显示货币类型事件
	function coinChange(){
		var selected = $("#numberFormatCoin").attr("checked");
		
		if(selected=='checked'){
			$("#numberFormatCoinType").attr("style","");
		}else{
			$("#numberFormatCoinType").attr("style","display:none;");
		}
	}
	
    /**
     * 上下移动
     * @param {} obj 移动的对象
     * @param {} flag 上移 true,下移 false
     */
     function move(obj,flag){         
    	   var  trObj = obj.parents('tr.editable-tr');
    	if(flag){
    		var prevObj=trObj.prev();
    		if(prevObj.length>0){
    			trObj.insertBefore(prevObj);	
    		}
    	}else{
    		var nextObj=trObj.next();
    		if(nextObj.length>0){
    			trObj.insertAfter(nextObj);
    		}
    	}
    }
     
    /**
     * 添加选项
     * @param  {[json]} data [数据]
     * @return {[type]}      [description]
     */
    function addColumn(data){
      var hiddenTable = $("#hiddenTable"),
          tmpTr = $("tr.editable-tr",hiddenTable),
          newTr = tmpTr.clone();
      if( typeof(data.key)!= undefined && data.key!=null && data.key!=''){
    	  $("input[name='optionKey']",newTr).val(data.key);
    	  $("input.long",newTr).each(function(){
    		  var opinionValue = data.value;
    		  var me = $(this).val(''+opinionValue),
    		      name = me.attr("name");
    	      for(var i=0,c;c=opinionValue[i++];){
    	          if(c.lantype==name){
    	              me.val(c.lanres);
    	          }
    	      }
    	  });
      }
      return newTr;
    };
    
    /**
     * 终止事件冒泡
     * @param  {[type]} e [description]
     * @return {[type]}   [description]
     */
    function stopBubble(e) {
        if (e && e.stopPropagation) e.stopPropagation();
        else window.event.cancelBubble = true;
    };
    
    //获取格式化内容
    function getFormat(){
    	var str="";
    	if("${sysQueryField.type}"=="DATE"){
			str=getDateFormat();
    	}
    	if("${sysQueryField.type}"=="NUMBER"){
			str=getNumFormat();
    	}
		return str;
    }
    
    //返回日期格式化信息
    function getDateFormat(){
    	return $("#dateFormat").val();
    }
    
  //返回number格式化信息
    function getNumFormat(){
    	obj = {};
    	obj.nft=$("#numberFormatThousands").attr("checked");//千分位
    	obj.nfc=$("#numberFormatCoin").attr("checked");//是否货币显示
    	if(obj.nfc=='checked'){//货币类型
    		obj.nfct=$("#numberFormatCoinType").val();
    	}
    	obj.nfz=$("#numberFormatZsw").val();//整数位
    	obj.nfx=$("#numberFormatXsw").val();//小数位
    	
    	return JSON2.stringify(obj);
    }
  
    //初始化number格式化的内容
    function initNumFormat(){
    	 var nf = '${sysQueryField.format}';
    	 if(nf==null||nf=="") return;
    	 var json = JSON.parse(nf);
    	 
    	 var nft=json.nft;
    	 if(nft!=undefined){
    		 $("#numberFormatThousands").attr("checked","checked")
    	 }
    	 
    	 var nfc=json.nfc;
    	 if(nfc!=undefined){
    		 $("#numberFormatCoin").attr("checked","checked");
    		 $("#numberFormatCoinType").attr("style","");
    		 $("#numberFormatCoinType").attr("value",json.nfct);
    	 }
    	 
    	 var nfz=json.nfz;
    	 if(nfz!=undefined){
    		 $("#numberFormatZsw").attr("value",nfz);
    	 }
    	 
    	 var nfx=json.nfx;
    	 if(nfx!=undefined){
    		 $("#numberFormatXsw").attr("value",nfx);
    	 }
    }
  
  //获取控件类型
  function getControlType(){
	  return $("#controlType").val();
  }
  
  //获取控件内容
  function getControlContent(){
	  var str="";
	  var ct = getControlType();
	  if(ct==3){//数据字典
		  str=getDictControlContent();
	  }else if(ct==11){
		  str=getOptControlContent();
	  }
	  return str;
  }
  
  //获取数据字典控件内容，得到数据字典的id
  function getDictControlContent(){
	  return $("#dictControlContent").val();
  }
  
  //获取下拉框控件内容
  function getOptControlContent(){
	  var optKeys = document.getElementsByName("optionKey");
	  var optVals = document.getElementsByName("optionValue");
	  
	  var json = [];
	  for(var i=0;i<optKeys.length-1;i++){
		  var obj={};
		  obj.key=optKeys[i].value;
		  obj.val=optVals[i].value;
		  json.push(obj);
	  }
	  return JSON2.stringify(json);
  }
  
  //初始化下拉框控件内容
  function initOptControlContent(){
	  var occ = '${sysQueryField.controlContent}';
	  if(occ==null||occ=="") return;
	  var jsons = JSON.parse(occ);
	  for(var i = 0;i<jsons.length;i++){
		  var json = jsons[i];
          var data = {
              key: json.key,
              value: json.val
          }, newTr = addColumn(data);
          var tbody = $('#option-table>tbody');
          tbody.append(newTr);
	  }
  }
  
  //异步提交表单
  function customFormSubmit(){
	  //表单内容
	  var content={
		  id:${sysQueryField.id},
		  format:getFormat(),
		  controlType:getControlType(),
		  controlContent:getControlContent()
	  };
	  var form = $('<form method="post" action="saveDetail.do"></form>');
	  var input = $("<input type='hidden' name='jsonStr'/>");
	  var jsonStr=JSON2.stringify(content);
	  input.val(jsonStr);
	  form.append(input);

	  //异步表单参数
	  var options = {
		  dataType: 'json',//表单返回内容格式
    	  success: function(data) {//成功提交表单的调用参数
    		  if(data.result=="1"){
    			  var rtn=new Object();
    			  rtn.ct=$("#controlType").val();
    			  dialog.get("succCall")(rtn);
    			  dialog.close();
    		  }else{
    			  alert(data.message);
    		  }
  	  	  }
	  };
	  
	  form.ajaxForm(options); //设置参数
	  form.submit();//提交表单
  }
  
</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
				<div class="tbar-title">
						修改字段信息 
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="####" onclick="customFormSubmit();">保存</a>
						</div>
						<div class="group">
							<a class="link del" href="####" onclick="dialog.close();">关闭</a>
						</div>
					</div>
				</div>
		</div>
		<div class="panel-body">
	<form id="frmFields" action="">
		<div class="panel-detail">
			<input type="hidden" id="fieldId" name="fieldId"/>
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width=100>字段描述:</th>
					<td>${sysQueryField.fieldDesc}</td>
					<th width=100>字段名称:</th>
					<td>${sysQueryField.name}</td>
				</tr>
				<tr>
					<th width=100>字段类型:</th>
					<td colspan="3">
						${sysQueryField.type}
					</td>
				</tr>
				
				<!-- 目前就日期和number需要格式化  考虑到性能问题，暂时不需要格式化-->
				<c:if test="${sysQueryField.type=='DATE'||sysQueryField.type=='NUMBER'}">
					<tr >
						<th width=100>格式化:</th>
						<td colspan="3">
							<c:if test="${sysQueryField.type=='DATE'}">
								<select id="dateFormat" name="dateFormat">
									<option value="yyyy" <c:if test="${sysQueryField.format=='yyyy'}">selected="selected"</c:if>>yyyy</option>
									<option value="yyyy-MM" <c:if test="${sysQueryField.format=='yyyy-MM'}">selected="selected"</c:if>>yyyy-MM</option>
									<option value="yyyy-MM-dd" <c:if test="${sysQueryField.format=='yyyy-MM-dd'}">selected="selected"</c:if>>yyyy-MM-dd</option>
									<option value="yyyy-MM-dd HH:mm:ss" <c:if test="${sysQueryField.format=='yyyy-MM-dd HH:mm:ss'}">selected="selected"</c:if>>yyyy-MM-dd HH:mm:ss</option>
									<option value="yyyy-MM-dd HH:mm:00" <c:if test="${sysQueryField.format=='yyyy-MM-dd HH:mm:00'}">selected="selected"</c:if>>yyyy-MM-dd HH:mm:00</option>
									<option value="HH:mm:ss" <c:if test="${sysQueryField.format=='HH:mm:ss'}">selected="selected"</c:if>>HH:mm:ss</option>
								</select>
							</c:if>
							<c:if test="${sysQueryField.type=='NUMBER'}">
								<label>千分位:</label><input class="plat-input-sql" id="numberFormatThousands" name="numberFormatThousands" type="checkbox"/>
								<label>货币:</label><input class="plat-input-sql" id="numberFormatCoin" name="numberFormatCoin" type="checkbox" onchange="coinChange();"/>
								<select id="numberFormatCoinType" name="numberFormatCoinType" style="display:none;">
									<option value="rmb">￥人民币</option>
									<option value="dollar">$美金</option>
								</select>
								<label>整数位:</label><input class="plat-input-sql" id="numberFormatZsw" name="numberFormatZsw" type="text" size="10"/>
								<label>小数位:</label><input class="plat-input-sql" id="numberFormatXsw" name="numberFormatXsw" type="text" size="10"/>
							</c:if>
						</td>
					</tr>
				</c:if> 
				
				<tr id="trControlType" >
					<th width=100>控件类型:</th>
					<td colspan="4">
						<select id="controlType" name="controlType" onchange="hdlControlType();">							
							<!-- 只有日期类型才能选这个 -->
							<c:if test="${sysQueryField.type=='DATE'}">
								<option value ="15" <c:if test="${sysQueryField.controlType==15}">selected="selected"</c:if>>日期控件</option>
							</c:if>
							<c:if test="${sysQueryField.type=='NUMBER'}">
								<option value ="25" <c:if test="${sysQueryField.controlType==25}">selected="selected"</c:if>>数字范围控件</option>
							</c:if>
							<option value ="1" <c:if test="${sysQueryField.controlType==1}">selected="selected"</c:if>>单行文本框</option>
							<option value ="3" <c:if test="${sysQueryField.controlType==3}">selected="selected"</c:if>>数据字典</option>
							<option value ="11" <c:if test="${sysQueryField.controlType==11}">selected="selected"</c:if>>下拉选项</option>
							<option value ="4" <c:if test="${sysQueryField.controlType==4}">selected="selected"</c:if>>人员选择器(单选)</option>
							<option value ="17" <c:if test="${sysQueryField.controlType==17}">selected="selected"</c:if>>角色选择器(单选)</option>
							<option value ="18" <c:if test="${sysQueryField.controlType==18}">selected="selected"</c:if>>组织选择器(单选)</option>
							<option value ="19" <c:if test="${sysQueryField.controlType==19}">selected="selected"</c:if>>岗位选择器(单选)</option>
							
							<option value ="8" <c:if test="${sysQueryField.controlType==8}">selected="selected"</c:if>>人员选择器(多选)</option>
							<option value ="5" <c:if test="${sysQueryField.controlType==5}">selected="selected"</c:if>>角色选择器(多选)</option>
							<option value ="6" <c:if test="${sysQueryField.controlType==6}">selected="selected"</c:if>>组织选择器(多选)</option>
							<option value ="7" <c:if test="${sysQueryField.controlType==7}">selected="selected"</c:if>>岗位选择器(多选)</option>
						</select>
					</td>
				</tr>
				<tr id="trDict" <c:if test="${sysQueryField.controlType!=3}">style="display:none;"</c:if>>
					<th width=100>数据字典类型:</th>
					<td colspan="3">
						<select id="dictControlContent" name="dictControlContent">
							<c:forEach items="${globalTypes}" var="globalType">
								<option value ="${globalType.typeId}" <c:if test="${sysQueryField.controlContent}==${globalType.typeId}">selected="selected"</c:if>>${globalType.typeName }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr id="trOption" <c:if test="${sysQueryField.controlType!=11}">style="display:none;"</c:if>>
					<th width=100>下拉选项:</th>
					<td colspan="3">
						<div id="panel" class="s">			
							<table id="option-table">
								<thead>
									<tr>
										<td colspan="1">
											表名：<input class="plat-input-sql" name="tableName" type="text" value=""/>
											字段名：${sysQueryField.name}
										</td>
										<td colspan="1">
											<a href="####" class="link setting">初始化</a>
											<a href="####" class="link add">添加</a>
										</td>
									</tr>
									<!-- <tr>
										<td colspan="2">
											<a href="####" class="link add">添加</a>
										</td>
									</tr> -->
								</thead>
								<tbody>
								</tbody>
							</table>
							<div class="hidden">
								<table id="hiddenTable">
									<tbody>
										<tr class="editable-tr">
					                        <td>
					                            <div class="editable-left-div">
					                                 <label>值: <input name="optionKey" type="text" class="plat-input-sql"/></label>
	              	 									 <label style="margin:0 0 0 10px;">选项: <input class="plat-input-sql" name="optionValue" class="long" type="text"/></label>
					                            </div>
					                        </td>
					                        <td>
					                            <div class="editable-right-div">
					                                <a href="####" class="link moveup" title="上移" tabindex="-1">&nbsp;</a>
					                                <a href="####" class="link movedown" title="下移"  tabindex="-1">&nbsp;</a>
					                                <a href="####" class="link del"  title="移除" tabindex="-1">&nbsp;</a>
					                            </div>
					                        </td>
				                    	</tr>
									</tbody>
								</table>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>		
	</form>
	</div>
</div>
</body>
</html>