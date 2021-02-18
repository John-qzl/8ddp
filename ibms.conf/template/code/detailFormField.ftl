<#setting number_format="0">
<#if field.fieldType == "varchar"><#---字符串类型-->
	<#switch field.controlType>
		<#case 1><#--单行文本框-->
		<span name="editable-input"  style="display:inline-block;" isflag="tableflag">
			${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		</span>
		<#break>
		<#case 2><#--多行文本框-->
		<span name="editable-input"  style="display:inline-block;" isflag="tableflag">
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		</span>
		<#break>
		<#case 3><#--数据字典-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 4><#--人员选择器(单选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 5><#--角色选择器(多选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 6><#--组织选择器(多选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 7><#--岗位选择器(多选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 8><#--人员选择器(多选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 9><#--文件上传-->
				<div name="div_attachment_container" right="r">
					<div class="attachement"></div>
					<textarea style="display:none" controltype="attachment" name="${field.fieldName}" lablename="${field.fieldDesc}" readonly="readonly">${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}</textarea>
				</div>	
		<#break>
		<#case 10><#--富文本框ckeditor-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 11><#--下拉选项-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 26><#--密级管理-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 12><#--Office控件-->
				<input type="hidden" right="r" menuright="" class="hidden" name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="office" right="w" value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" />
		<#break>
		<#case 13><#--复选框-->
				<#list field.aryOptions?keys as optkey>
					<label><input type="checkbox" data="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" name="${field.fieldName}" value="${optkey}" validate="{<#if field.isRequired == 1>required:true</#if>}" disabled="disabled"/>${field.aryOptions[optkey]}</label>
				</#list>
		<#break>
		<#case 14><#--单选按钮-->
				<span>
				<#list field.aryOptions?keys as optkey>
					<label><input data="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" type="radio" name="${field.fieldName}" value="${optkey}" lablename="${field.fieldDesc}" validate="{<#if field.isRequired == 1>required:true</#if>}" disabled="disabled"/>${field.aryOptions[optkey]}</label>
				</#list>
				</span>
		<#break>
		<#case 15><#--日期控件-->
				&lt;fmt:formatDate value='${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}' pattern='yyyy-MM-dd'/>
		<#break>
		<#case 16><#--隐藏域-->
				
		<#break>
		<#case 17><#--角色选择器(单选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 18><#---组织选择器(单选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 19><#--岗位选择器(单选)-->
				${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
		<#break>
		<#case 20><#--流程引用-->
				<div>
					<input name="${field.fieldName}ID" type="hidden" class="hidden" lablename="${field.fieldDesc}ID" value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}">
					<input name="${field.fieldName}" type="text" lablename="${field.fieldDesc}"  value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> />
					<a href="javascript:;" class="link actInsts" atype="select" name="${field.fieldName}">选择</a>
					<a href="javascript:;" class="link reset" atype="reset" name="${field.fieldName}" >重置</a>
				</div>
		<#break>
		<#case 21><#--WebSign签章控件-->
				<input type="hidden" class="hidden" name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="webSign"  value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" />
				<div id="div_${field.fieldName?replace(":","_")}" class="webSign-div"></div>
		<#break>
		<#case 22><#--图片展示控件-->
				<div id="div_${field.fieldName?replace(":","_")}" style="width:400px;height:340px" class="pictureShow-div" > 
			        <div id="div_${field.fieldName?replace(":","_")}_container" ></div> 
			        <table id="pictureShow_${field.fieldName?replace(":","_")}_Toolbar">
			           <tr right="r">
		                 <td width="80">
		                 	<a href="javascript:;" field="${field.fieldName}" class="link selectFile" atype="uploadPicture" onclick="{PictureShowPlugin.upLoadPictureFile(this);}">上传图片</a> 
		                 </td>
		                 <td width="80">
		                 	<a href="javascript:;" field="${field.fieldName}" class="link del" atype="delPicture" onclick="{PictureShowPlugin.deletePictureFile(this);}">删除图片</a> 				              
		                 </td>
		               </tr>
		            </table>   
			    </div>
			    <input type="hidden" class="hidden"  name="${field.fieldName}" lablename="${field.fieldDesc}"  controltype="pictureShow" value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" right="r"  />
		<#break>
		<#case 23><#--关系控件 此代码感觉没有走过，dataTemplateEditData.jsp和dataTemplateDetailData.jsp 页面中走的是fieldControl.ftl中的外键显示值代码。-->
				<div id="${field.fieldName}XXXXXFKColumnShowDIV"><#--关系控件-->
					<input name="${field.fieldName}" type="hidden" class="hidden" lablename="${field.fieldDesc}" value="">
					<input name="${field.fieldName}XXXXXFKColumnShow" type="text" class="inputText" lablename="${field.fieldDesc}"  value="" 
					   validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" 
					   readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  
					   refid="${field.fieldName}"</#if> />  
					<span name="editable-input" style="display:inline-block;"><#--span这行不能少-->
						<#--自定义查询fk对话框按钮 如:dialog="{name:'ctcsbdhk',fields:[{src:'F_MC',target:'relname'},{src:'ID',target:'rel_test_idXXXXXFKColumnShow'}],
						query:[{'id':'relname','name':'F_GSBH','isMain':'true'}],rpcrefname='interfacesImplConsumerCommonService'}" -->
						<a kfname="${field.fieldName}" dialog="${field.relFormDialogStripCData}" href="javascript:;" class="extend fkselect">
						  选择
						</a>
					</span>
					<a href="javascript:;" class="link resetFKShow" atype="reset" name="${field.fieldName}">重置</a>
				</div>
		<#break>
	</#switch>
<#elseif field.fieldType == "number"><#---数字类型-->
	<#if  field.controlType == 16><#--隐藏域-->
		<input name="${field.fieldName}" type="hidden"  value="${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}">
	<#elseif field.controlType == 11><#--下拉选项-->
			<span name="editable-input"  style="display:inline-block;padding:2px;" class="selectinput">
				<select name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="select" validate='{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}'>
					<#list field.aryOptions?keys as optkey>
					<option value="${optkey}">${field.aryOptions[optkey]}</option>
					</#list>
				</select>
			</span>
	<#else><#--否则数字输入-->
		${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
	</#if>
<#elseif field.fieldType == "date"><#---日期类型-->
	<#if  field.controlType == 16><#--隐藏域-->
		
	<#else>
		&lt;fmt:formatDate value='${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}' pattern='yyyy-MM-dd'/>
	</#if>
<#else>
	<#if  field.controlType == 16><#---隐藏域-->
		
	<#elseif field.controlType == 10><#--富文本框ckeditor-->
		${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
	<#else><#--否则多文本框-->
		${r"${"}${table.variable.classVar}.${field.fieldName}${r"}"}
	</#if>
</#if>