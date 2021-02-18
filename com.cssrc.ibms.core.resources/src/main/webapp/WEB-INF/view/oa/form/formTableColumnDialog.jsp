<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title><c:choose> <c:when test="${isAdd==1}">添加自定义列</c:when><c:otherwise>修改自定义列</c:otherwise> </c:choose> </title>

<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<f:link href="tree/zTreeStyle.css"></f:link>
<f:link href="reminder.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/loadjscss.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FormDialogDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/EditTable.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FieldsManage.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/serialNumber.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/EditTableExt.js"></script>


<script type="text/javascript">
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
$(function(){
	//值来源。该方法写在EditTable.js中
	handValueFrom();
	//字段类型修改。 该方法写在EditTable.js中
	handFieldType();
	//处理控件类型。 该方法写在EditTable.js中
	handControlType();
	//
	init();
	//处理条件字段isQuery。该方法写在EditTable.js中
	handConditionClick();
	
	//处理货币coin。该方法写在EditTable.js中
	handCoinClick();
	
});

var __isFieldAdd__=true;

function init() {
	var conf = dialog.get('conf');
    //设置字段管理,主要是为了验证字段是否存在。
    TableRow.setFieldManage(conf.fieldManage);
    //在EditTable.js 的validateField 方法中有用到。
    __isFieldAdd__ = conf.isAdd;

    var valid = validateField();
    //添加字段
    if (__isFieldAdd__) {

        $("#dataFormSave").html("添加");
        initAdd(conf.isExternal);
        if(typeof TableEXT=="function"){
        	TableEXT.columnInit(conf);
        }
    }
    //更新字段
    else {
        var allowEditColName = conf.allowEditColName;
        $("#dataFormSave").html("保存");
        //根据字段设置页面控件状态。initControlByField方法在EditTable.js中.
        initControlByField(conf.field, allowEditColName,conf.isExternal);
        var identityName=$("#identityName").val()
        if(!identityName)
        initIdentity();
        if(typeof TableEXT=="function"){
        	TableEXT.columnInit(conf);
        }
    }
    $("#dataFormSave").click(function() {
        InitMirror.save();//方法在InitMirror.js中，保存编辑器内容到原来的textarea.
        var field = getField();//获取字段.方法在EditTable.js中，可以按F3进入查看
        if (!valid.form()) return false;
        if (__isFieldAdd__) {
            var rtn = conf.callBack.call(this, field);
            if (!rtn) return;
            //$.ligerDialog.success('添加字段属性成功,请点击业务表定义上的保存按钮','提示信息', function() {
            //	resetField();
            //});
             //重置字段
            resetField();
        } else {
            conf.callBack.call(this, field);
            $.ligerDialog.success('修改字段属性成功,请点击业务表定义上的保存按钮','提示信息', function() {
            	 dialog.close();
            });
        }
    });

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
       if(confirm('确认删除？')){
	        var me = $(this),           
	            tr = me.parents('tr.editable-tr');
	        tr.remove();
       }
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
        }, newTr = addColumn1(data);

        var tbody = $('#option-table>tbody');
        tbody.append(newTr);
    });
};

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

function selectScript() {
    ScriptDialog({
        callback: function(script) {
            InitMirror.editor.insertCode(script);
        }
    });
}

//关联关系列选择自定义对话框
function selectFormDialog() {

	var relation=InitMirror.editor.getCode();
	relation=$.trim(relation);
	FormDialogDialog({
		allFileds:TableRow.fieldManage,
		curentFiled:getField(),
		relation:relation,
        callback: function(data) {
        	InitMirror.editor.setCode(data)
        }
    });
}

//根据字段描述生成字段名并生成悬停提示信息
function autoGetKey(inputObj){
	var url=__ctx + '/oa/form/formTable/getFieldKey.do' ;
	var subject=$(inputObj).val();
	if($.trim(subject).length<1) return;
	$.ajax({
		  url: url,
		  async:false,
		  type:'POST',
		  data: ({subject : subject}),
		  success: function(data){
			  var json=eval('('+data+')');
				if(json.result==1 && $.trim($('#fieldName').val()).length<1    ){			
					$('#fieldName').val(json.message);
					$('#hoverTitleValue').val("请填写字段"+json.message+"的信息");
					var valid=validateField();
					valid.form();
				
				}
		  }
	});
}

//弹出页面选择流水号
function ChooseIdentity(){
	var callBack=function(identityAlias,identityName){
		$("#identityName").val(identityName);
		$("#identityAlias").val(identityAlias);
	}
	IdentityList(callBack);
}

//初始化流水号，根据别名取名称
function initIdentity(){
	var identityAlias=$("#identityAlias").val();
	if(!identityAlias)return;
	var url=__ctx+'/oa/system/serialnumber/getByAlias.do'
	$.ajax({
		url:url,
		async:false,
		type:'POST',
		data: ({alias: identityAlias}),
		success: function(data){
		var json=eval('('+data+')');
			if(json.result==1 ){	
		 		$("#identityName").val(json.message);
		 	}
		}
	});
}

//是否显示悬停输入框
function isOpenInput(){
	var isHoverTitle = $("#isHoverTitle").is(':checked');
	if(isHoverTitle){
		$("#hoverTitleValue").show();
	}else{
		$("#hoverTitleValue").hide();
	}
}

</script>
</head>
<body>
<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
					<div class="tbar-title">
						<c:choose> <c:when test="${isAdd==1}">添加自定义列</c:when><c:otherwise>修改自定义列</c:otherwise> </c:choose>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<a class="link save" id="dataFormSave" href="####">添加</a>
							</div>
							<div class="group">
								<a class="link del" href="####" onclick="dialog.close();">关闭</a>
							</div>
							<div class="group">
								 <a href="####" class="tipinfo"><span>
								 "选项:"<br/>
								 1.“是否流程变量”: 选中后，在流程处理中可以用作变量，比如分支条件、调整规则设置，设置审批人员、消息任务、标题时可以当做条件变量；<br/>
								 2.“是否支持Web印章验证”: 勾选后可以将此字段盖章，盖章的目的为了验证此字段有没有修改过。部分字段类型不支持，不支持的字段类型有：office类型，复选框类型，单选，超链接。<br/>
								 3.“唯一约束”: 用于业务表导入数据时候，判定是新增还是修改记录。可以设定多个进行组合约束。对表单数据录入不起唯一约束。
								 </span></a>
							</div>
						</div>
					</div>
			</div>
		</div>
	<form id="frmFields" action="">
		<div class="panel-detail">
			<input type="hidden" id="fieldId" name="fieldId"/>
			<input type="hidden" id="curIsMain" value="${isMain}" />
			<input type="hidden" id="isAdd" value="${isAdd}" />
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width=100>字段描述:</td>
					<td><input type="text" id="fieldDesc" name="fieldDesc" value="" class="inputText"   onblur="autoGetKey(this)"/></td>
					
					<th width=100>字段名称:</td>
					<td><input type="text" id="fieldName" maxlength="20" name="fieldName" value="" class="inputText"/></td>
					
				</tr>
				<tr>
					<th width=100>字段类型:</td>
					<td>
						<select id="fieldType" name="fieldType">
							<option value="varchar">文字</option>
							<option value="number">数字</option>
							<option value="date">日期</option>
							<option value="clob">大文本</option>
						</select>
						<span id="spanDateFormat" style="display:none">
							<br>
							取当前时间：<input type="checkbox" id="isCurrentDate" name="isCurrentDate" value="1">
							<br>
							<select id="selDateFormat" >
								<option value="yyyy-MM-dd">yyyy-MM-dd</option>
								<option value="yyyy-MM">yyyy-MM</option>
								<option value="yyyy-MM-dd HH:mm:ss">yyyy-MM-dd HH:mm:ss</option>
								<option value="yyyy-MM-dd HH:mm:00">yyyy-MM-dd HH:mm:00</option>
								<option value="HH:mm:ss">HH:mm:ss</option>
								<option value="yyyy">yyyy</option>
							</select>
						</span>
						<span id="hoverTitle">
							<br>
							是否显示悬停提示信息：<input type="checkbox" id="isHoverTitle" name="isHoverTitle" onclick="isOpenInput()">
							<br>
							<input id="hoverTitleValue" type="text" style="display:none;" class="inputText"></input>
						</span>
					</td>
					
					<th width=100>长度:</td>
					<td>
						<span id="spanCharLen">
							<input type="text" id="charLen" name="charLen" value="100" class="inputText" style="width:40px" />
						</span>
						
						<span id="showComdify" style="display: none">
							<label>千分位:</label>
							<input id="isShowComdify" name="isShowComdify" type="checkbox"  value="1"/>
						</span>
						<span id="spanCoin" style="display: none">
							<label>货币:</label>
							<input id="coin" name="coin" type="checkbox" value="1"/>
						</span>
						<span id="spanCoinType" style="display: none">
							<select id="CoinType" name="CoinType">
								<option value="￥">￥人民币</option>
								<option value="$">$美元</option>
							</select>
						</span>
				
						<span id="spanIntLen" style="display: none">
							<label>整数位:</label>
							<input type="text" id="intLen" name="intLen" value="18" class="inputText" size="4" style="width:40px"/>
						</span>
						
					
						<span id="spanDecimalLen" style="display: none">
							<label>小数位:</label>
							<input type="text" id="decimalLen" name="decimalLen" value="0" class="inputText" style="width:20px" />
						</span>
					</td>
				</tr>
			
				<tr>
					<th width=100>选项:</td>
					<td colspan="5">
						<span><input id="isRequired" name="isRequired" type="checkbox" value="1"/>&nbsp;必填&nbsp;</span>
						<!-- 
						<c:if test="${isMain==1}">
						<span><input id="isList" name="isList" type="checkbox" checked="checked" value="1"/>&nbsp;显示到列表&nbsp;</span>
						<span><input id="isQuery" name="isQuery" type="checkbox" value="1"/>&nbsp;作为查询条件&nbsp;</span>
						</c:if>
						 -->
						<span><input id="isFlowVar" name="isFlowVar" type="checkbox" value="1"/>&nbsp;是否流程变量&nbsp;</span>
						<span><input id="isPkShow" name="isPkShow" type="checkbox" value="1"/>&nbsp;是否主键显示值&nbsp;</span>
						<span id="showIsReference" style="display:none;"><input id="isReference" name="isReference" type="checkbox" checked="checked" value="1"/>&nbsp;作为超连接&nbsp;</span>
						<span><input id="isWebSign" name="isWebSign" type="checkbox" value="1"/>&nbsp;是否支持Web印章验证&nbsp;</span>
						<span><input id="isUnique" name="isUnique" type="checkbox" value="1"/>&nbsp;唯一约束&nbsp;</span>
					</td>
				</tr>
<!-- 				<tr id="trCondition"  >
					<th width=100>查询条件:</th>
					<td colspan="3">
						<table style="width:100%">
							<tr>
								<th>条件</th>
								<td >
									<select id="selCondition">
									</select>
								</td>
								<th>值来源</th>
								<td>
									<select id="selValueFrom" onchange="changeSelValFrom(this)">
										<option value="1">表单输入</option>
										<option value="2">固定值</option>
										<option value="3">脚本</option>
									</select>
								</td>
							</tr>
							<tr id="trSelValue">
								<th>值</th>
								<td colspan="3">
									<div id="selValue">
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr> -->
				<tr>
					<th width=100 >值来源:</td>
					<td colspan="3">
						<select id="valueFrom" name="valueFrom">
							<option value="0">表单输入</option>
							<option value="1">脚本运算(显示)</option>
							<option value="2">脚本运算(不显示)</option>
							<option value="3">流水号</option>
						</select>
						<span name="showidentity" id="showidentity">
							<input type="hidden" value="" name="identityAlias" id="identityAlias">
							<input type="text" name="identityName" id="identityName" value="" readonly="readonly"> 
							<input type="button" name="identitybut" id="identitybut" onclick="ChooseIdentity()" value="选择">
							<input type="checkbox" name="isShowidentity" id="isShowidentity" >是否显示在启动流程中
						</span>
					</td>
				</tr>
				<tr id="trControlType" >
					<th width=100>控件类型:</td>
					<td colspan="3"><select id="controlType" name="controlType"></select>
					<span style="display:none;" ><input type="checkbox" name="addDropDownDefault" id="addDropDownDefault" >是否添加下拉选项默认值</span>
					<span id="radioShow" style="display:none;color: red;">若该表为子表或者关联表，请慎用单选框，否则会出现数据丢失的情况，改用下拉框。</span>
					<span id="securityShow" style="display:none;color: red;">密级管理分为：绝密、机密、秘密与公开四类，会以下拉框的形式供用户选择。</span></td>
				</tr>
				<tr id="trRelTableType" >
					<th width=100>关系类型:</th>
					<td >
						<select id="relTableType" name="relTableType">
							<option value="2">多对一</option>
							<option value="0">一对一</option>
							<!-- <option value="1">一对多</option> -->
						</select>
					</td>
					<td nowrap="true">
					rel关联表<br/>记录删除类型:<!-- rel关联表记录删除类型，1-直接删除，0-取消关联。 -->
						<select id="relDelType" name="relDelType">
							<option value="0">取消关联</option>
							<option value="1">直接删除</option>
							
						</select>
					</td>
					<td nowrap="true">
					主表记录删除，rel<br/>关联表记录删除类型:<!-- 主表记录删除，rel关联表记录删除类型，1-直接删除，0-取消关联。 -->
						<select id="relDelLMType" name="relDelLMType">
							<option value="0">取消关联</option>
							<option value="1">直接删除</option>
						</select>
					</td>
				</tr>
				<tr>
				
				<tr id="trRelTableId" >
				
					<th width=100>选择关联关系表:</th>
					<td >
						<select id="relTableId" name="relTableId" size="18" style="width:350px;height: 100%;">
							<c:forEach items="${bpmFormTableList}" var="formTable">
								<option value="${formTable.tableId}">${formTable.tableDesc}(${formTable.tableName})</option>
							</c:forEach>
						</select>
					</td>
					<th width=100>关联的自定义对话框信息:</th>
					<td >
						<a href="javascript:;" onclick="selectFormDialog()" id="formDialogScript" class="link var" title="选择自定义对话框">选择自定义对话框</a>
						<textarea id="relFormDialog" codemirror="true" mirrorheight="320px" name="relFormDialog" rows="10" cols="80"></textarea>
					</td>
				</tr>
				<tr id="trDict" >
					<th width=100>数据字典类型:</td>
					<td colspan="3">
						<input id="dictTypeName" class="catComBo" catKey="DIC" valueField="dictType" isNodeKey="true" name="dictTypeName" height="270" width="200"/>
					</td>
				</tr>
				<tr id="trRule" >
					<th width=100>验证规则:</td>
					<td colspan="3">
						<select id="validRule" name="validRule">
							<option value="">无</option>
							<c:forEach items="${validRuleList}" var="rule">
								<option value="${rule.name}">${rule.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr id="trEncrypt" >
					<th width=100>加密算法:</td>
					<td colspan="3">
						<select id="encrypt" name="encrypt">
							<option value="">无</option>
							<c:forEach items="${encryptList}" var="encrypt">
								<option value="${encrypt}">${encrypt}</option>
							</c:forEach>
						</select>
					</td>
				</tr>				
				<tr id="trScript" >
					<th width=100>脚本:</td>
					<td colspan="3">
						<a href="####" onclick="selectScript()" id="btnScript" class="link var" title="常用脚本">常用脚本</a>
						<br/>脚本中要使用到其他字段参与运算， 请使用“[字段名]”方式引用。<br />
						<textarea id="script" codemirror="true" name="script" rows="6" cols="70"></textarea>
					</td>
				</tr>
				<tr id="trScriptID" >
					<th width=100>ID脚本:</th>
					<td colspan="3">
						<a href="####" onclick="selectScript()" id="btnScriptID" class="link var" title="常用脚本">常用脚本</a>
						<br/>脚本中要使用到其他字段参与运算， 请使用“[字段名]”方式引用。<br />
						<textarea id="scriptID" codemirror="true" mirrorheight="80px" name="scriptID" rows="4" cols="70"></textarea>
					</td>
				</tr>
				<tr id="trOption" >
					<th width=100>下拉选项:</td>
					<td colspan="3">
					<div id="panel" class="s">			
							<table id="option-table">
								<thead>
									<tr>
										<td colspan="2">
											<a href="####" class="link add">添加</a>
										</td>
									</tr>
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
               	 									 <label style="margin:0 0 0 0px;">选项: <input name="optionValue" class="long plat-input-sql" type="text"/></label>
               	 									 <label id="statusIsShow" style="margin:0 0 0 0px;">状态:
               	 									 		<select id="optionStatus" name="optionStatus" style="width:50px;height:21px;">
																<option value="1">显示</option>
																<option value="0">隐藏</option>
															</select>
               	 									 </label>
					                            </div>
					                        </td>
					                        <td>
					                            <div class="editable-right-div">
					                                <a href="####" class="link moveup" title="上移" tabindex="-1"></a>
					                                <a href="####" class="link movedown" title="下移"  tabindex="-1"></a>
					                                <a href="####" class="link del"  title="移除" tabindex="-1"></a>
					                            </div>
					                        </td>
					                    </tr>
									</tbody>
								</table>
							</div>
						</div>
					</td>
				</tr>
				<!-- <tr id="formUserTr" style="display:none;">
					<th width=100>
						是否表单用户:
					</td>
					<td colspan="3">
						<label><input id="ifFormUser" name="ifFormUser" type="checkbox" /><span id="formUserSpan"></span></label>
					</td>
				</tr> -->
				<tr id="trUpLoad" style="display:none;">
					<th width=120>
						直接上传文件:
					</th>
					<td colspan="3">
						<label><input id="isDirectUpLoad" name="isDirectUpLoad" type="checkbox" /></label>
					</th>
				</tr>
				<tr id="showCurUserTr" style="display:none;">
					<th width=120>
						显示当前用户:
					</th>
					<td colspan="3">
						<label><input id="showCurUser" name="showCurUser" type="checkbox" /></label>
					</th>
				</tr>
				<tr id="showCurOrgTr" style="display:none;">
					<th width=120>
						显示当前组织:
					</th>
					<td colspan="3">
						<label><input id="showCurOrg" name="showCurOrg" type="checkbox" /></label>
					</td>
				</tr>
				<tr id="showCurPosTr" style="display:none;">
					<th width=120>
						显示当前岗位:
					</th>
					<td colspan="3">
						<label><input id="showCurPos" name="showCurPos" type="checkbox" /></label>
					</td>
				</tr>
			</table>
		</div>		
	</form>
</div>
</body>
</html>