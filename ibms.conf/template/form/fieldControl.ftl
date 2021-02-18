<#setting number_format="0">
<#macro input field>
		<#if field.fieldType == "varchar"><#---字符串类型-->
			<#switch field.controlType>
				<#case 1><#--单行文本框-->
				<span name="editable-input" class="input-content input-pattern" isflag="tableflag">
					<input name="${field.fieldName}" isUnique="${field.isUnique}" type="text" lablename="${field.fieldDesc}" class="inputText input-dhwbk" value="" validate="{maxlength:${field.charLen}<#if field.isRequired == 1>,required:true</#if><#if field.validRule != "">,${field.validRule}:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
					 isflag="tableflag" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>/>
				</span>
				<#break>
				<#case 2><#--多行文本框-->
				<span name="editable-input" style="display:inline-block;" isflag="tableflag">
					<textarea name="${field.fieldName}" lablename="${field.fieldDesc}" class="l-textarea textarea-dhwbk" rows="5" cols="40" validate="{maxlength:${field.charLen}<#if field.isRequired == 1>,required:true</#if><#if field.validRule != "">,${field.validRule}:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
					 isflag="tableflag" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>></textarea>
				</span>
				<#break>
				<#case 3><#--数据字典-->
						<input lablename="${field.fieldDesc}" class="dicComboTree input-dataDictionary" nodeKey="${field.dictType}" value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" name="${field.fieldName}" 
						height="200" width="170" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>/>
				<#break>
				<#case 4><#--人员选择器(单选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="user input-userPicker" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if>  showCurUser="<#if (field.getPropertyMap().showCurUser==null)>0<#else>${field.getPropertyMap().showCurUser}</#if>" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>/>
				<#break>
				<#case 5><#--角色选择器(多选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="roles input-rolePickers" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 6><#--组织选择器(多选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="orgs input-orgsPickers" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 7><#--岗位选择器(多选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="positions input-posPickers" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 8><#--人员选择器(多选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="users input-userPickers" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 9><#--文件上传-->
						<div  name="div_attachment_container" displayType="${field.getPropertyMap().fileDisplay}" >
							<div  class="attachement" ></div>
							<textarea style="display:none" controltype="attachment" name="${field.fieldName}" lablename="${field.fieldDesc}"></textarea>
							<a href="javascript:;" name="${field.fieldName}" field="${field.fieldName}" class="link selectFile" atype="select"  onclick="{<#if field.getPropertyMap().isDirectUpLoad==1>AttachMent.directUpLoadFile(this);<#else>AttachMent.addFile(this);</#if>}" validate="{<#if field.isRequired == 1>required:true</#if>}">选择</a>
						</div>	
				<#break>
				<#case 10><#--富文本框ckeditor-->
						<span name="editable-input"  style="display:inline-block;" >
						<textarea class="ckeditor"  name="${field.fieldName}" lablename="${field.fieldDesc}" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.validRule != "">,${field.validRule}:true</#if>}" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>></textarea>
						</span>
				<#break>
				<#case 11>
				<#case 28><#--下拉选项-->
					<span name="editable-input" class="selectinput input-content select-pullOpt">
						<select name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="select" validate='{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}' <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
							<#list field.aryOptions?keys as optkey>
							<option value="${optkey}">${field.aryOptions[optkey]}</option>
							</#list>
						</select>
					</span>
				<#break>
				<#case 26><#--密级管理-->
					<span name="editable-input" class="selectinput input-content">
						<select class="select-secManager" name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="select" validate='{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}' <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
							<#list field.aryOptions?keys as optkey>
							<option value="${optkey}">${field.aryOptions[optkey]}</option>
							</#list>
						</select>
					</span>
				<#break>				
				<#case 12><#--Office控件-->
						<input type="hidden" class="hidden input-office" name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="office"  value="" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 13><#--复选框-->
						<#list field.aryOptions?keys as optkey>
							<label><input type="checkbox" class="input-checkbox" name="${field.fieldName}" value="${optkey}" validate="{<#if field.isRequired == 1>required:true</#if>}" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>/>${field.aryOptions[optkey]}</label>
						</#list>
				<#break>
				<#case 14><#--单选按钮-->
						<#list field.aryOptions?keys as optkey>
							<label><input type="radio" class="input-radio" name="${field.fieldName}" value="${optkey}" lablename="${field.fieldDesc}" validate="{<#if field.isRequired == 1>required:true</#if>}" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>/>${field.aryOptions[optkey]}</label>
						</#list>
				<#break>
				<#case 15><#--日期控件-->
						<input name="${field.fieldName}" type="text" isUnique="${field.isUnique}" class="Wdate input-dateControl" lablename="${field.fieldDesc}" displayDate="<#if (field.getPropertyMap().displayDate==null)>0<#else>${field.getPropertyMap().displayDate}</#if>"
						 dateFmt="<#if (field.getPropertyMap().format==null)>yyyy-MM-dd<#else>${field.getPropertyMap().format}</#if>"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
				<#break>
				<#case 16><#--隐藏域-->
						<input name="${field.fieldName}" type="hidden" lablename="${field.fieldDesc}"  value="" validate="{<#if field.isRequired == 1>required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}">
				<#break>
				<#case 17><#--角色选择器(单选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="role" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 18><#---组织选择器(单选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="org input-orgsPicker" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly"  <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> showCurOrg="<#if (field.getPropertyMap().showCurOrg==null)>0<#else>${field.getPropertyMap().showCurOrg}</#if>" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 19><#--岗位选择器(单选)-->
						<input name="${field.fieldName}" type="text" ctlType="selector" class="position input-posPicker" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}"
						 readonly="readonly"  <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> showCurPos="<#if (field.getPropertyMap().showCurPos==null)>0<#else>${field.getPropertyMap().showCurPos}</#if>" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if> />
				<#break>
				<#case 20><#--流程引用-->
						<div>
							<input name="${field.fieldName}ID" type="hidden" class="hidden" lablename="${field.fieldDesc}ID" value="">
							<input name="${field.fieldName}" class="input-process" type="text" lablename="${field.fieldDesc}"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  refid="${field.fieldName}ID"</#if> />
							<a href="javascript:;" class="link actInsts" atype="select" name="${field.fieldName}">选择</a>
							<a href="javascript:;" class="link reset" atype="reset" name="${field.fieldName}" >重置</a>
						</div>
				<#break>
				<#case 21><#--WebSign签章控件-->
						<input type="hidden" class="hidden" name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="webSign"  value="" />
						<div id="div_${field.fieldName?replace(":","_")}" class="webSign-div"></div>
				<#break>
				<#case 22><#--图片展示控件-->
						<div id="div_${field.fieldName?replace(":","_")}" style="width:400px;height:340px" class="pictureShow-div" > 
					        <div id="div_${field.fieldName?replace(":","_")}_container" ></div> 
					        <table id="pictureShow_${field.fieldName?replace(":","_")}_Toolbar">
					           <tr>
				                 <td width="80">
				                 	<a href="javascript:;" name="${field.fieldName}" field="${field.fieldName}" class="link selectFile" atype="uploadPicture" onclick="{PictureShowPlugin.upLoadPictureFile(this);}">上传图片</a> 
				                 </td>
				                 <td width="80">
				                 	<a href="javascript:;" name="${field.fieldName}" field="${field.fieldName}" class="link del" atype="delPicture" onclick="{PictureShowPlugin.deletePictureFile(this);}">删除图片</a> 				              
				                 </td>
				               </tr>
				            </table>   
					    </div>
					    <input type="hidden" class="hidden"  name="${field.fieldName}" lablename="${field.fieldDesc}"  controltype="pictureShow" value="" right="w" />
				<#break>
				<#case 23>
				<div id="${field.fieldName}XXXXXFKColumnShowDIV"><#--关系控件-->
					<input name="${field.fieldName}" type="hidden" class="hidden" lablename="${field.fieldDesc}" value="">
					<#list field.relField as relf>
						<#if relf.isTableField>
							<input name="${relf.fieldName}"  type="text" class="inputText" value="" lablename="${field.fieldDesc}" value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" 
							   readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  
							   refid="${field.fieldName}"</#if> />  
							<span name="editable-input" style="display:inline-block;"><#--span这行不能少-->
								<a kfname="${field.fieldName}" name="${field.fieldName}" dialog="${field.relFormDialogStripCData}" href="javascript:;" class="extend fkselect">选择</a>
							</span>
							<span name="editable-input" style="display:inline-block;">
								<a href="javascript:;" class="link resetFKShow" atype="reset" name="${field.fieldName}">重置</a>
							</span>
						</#if>
						<#if !relf.isTableField>
							<input name="${field.fieldName}XXXXXFKColumnShow" type="text" class="inputText" lablename="${field.fieldDesc}"  value="" 
							   validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" 
							   readonly="readonly" <#if 1==field.isReference>linktype="${field.controlType}"  
							   refid="${field.fieldName}"</#if> />  
								<span name="editable-input" style="display:inline-block;"><#--span这行不能少-->
								<#--自定义查询fk对话框按钮 如:dialog="{name:'ctcsbdhk',fields:[{src:'F_MC',target:'relname'},{src:'ID',target:'rel_test_idXXXXXFKColumnShow'}],
								query:[{'id':'relname','name':'F_GSBH','isMain':'true'}],rpcrefname='interfacesImplConsumerCommonService'}" -->
								<a kfname="${field.fieldName}" name="${field.fieldName}" dialog="${field.relFormDialogStripCData}" href="javascript:;" class="extend fkselect">
								  选择
								</a>
							</span>
							<a href="javascript:;" class="link resetFKShow" atype="reset" name="${field.fieldName}">重置</a>
						</#if>
					</#list>
					
				</div>
				<#break>
				<#case 24><#--流程状态-->
				<span name="editable-input"  style="display:inline-block;padding:2px;" class="selectinput select-processState">
						<select name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="select" validate='{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}' <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
							<#list field.aryOptions?keys as optkey>
							<option value="${optkey}">${field.aryOptions[optkey]}</option>
							</#list>
						</select>
				</span>
				<#break>
			</#switch>
		<#elseif field.fieldType == "number"><#---数字类型-->
			<#if  field.controlType == 16><#--隐藏域-->
				<input name="${field.fieldName}" type="hidden"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}">
			<#elseif field.controlType == 11><#--下拉选项-->
					<span name="editable-input"  style="display:inline-block;padding:2px;" class="selectinput select-pullOpt">
						<select name="${field.fieldName}" lablename="${field.fieldDesc}" controltype="select" validate='{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}'>
						<#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>
							<#list field.aryOptions?keys as optkey>
							<option value="${optkey}">${field.aryOptions[optkey]}</option>
							</#list>
						</select>
					</span>
			<#else><#--否则数字输入-->
				<input class="input-number" name="${field.fieldName}" type="text" value="" showType=${field.ctlProperty}  validate="{number:true,maxIntLen:${field.intLen},maxDecimalLen:${field.decimalLen}<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" 
				<#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
			</#if>
		<#elseif field.fieldType == "date"><#---日期类型-->
			<#if  field.controlType == 16><#--隐藏域-->
				<input name="${field.fieldName}" type="hidden" class="hidden" displayDate="<#if (field.getPropertyMap().displayDate==null)>0<#else>${field.getPropertyMap().displayDate}</#if>" 
				 dateFmt="<#if (field.getPropertyMap().format==null)>yyyy-MM-dd<#else>${field.getPropertyMap().format}</#if>" value="" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
			<#else>
				<input name="${field.fieldName}" type="text" isUnique="${field.isUnique}" class="Wdate" displayDate="<#if (field.getPropertyMap().displayDate==null)>0<#else>${field.getPropertyMap().displayDate}</#if>" 
				 dateFmt="<#if (field.getPropertyMap().format==null)>yyyy-MM-dd<#else>${field.getPropertyMap().format}</#if>"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}" 
				 <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>>
			</#if>
		<#else>
			<#if  field.controlType == 16><#---隐藏域-->
				<input name="${field.fieldName}" type="hidden"  value="" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.isWebSign == 1>,isWebSign:true</#if>}">
			<#elseif field.controlType == 10><#--富文本框ckeditor-->
				<textarea class="ckeditor"  name="${field.fieldName}" validate="{empty:false<#if field.isRequired == 1>,required:true<#else>,required:false</#if><#if field.validRule != "">,${field.validRule}:true</#if>}" <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>></textarea>
			<#else><#--否则多文本框-->
				<textarea  name="${field.fieldName}" validate="{empty:false<#if field.isRequired == 1>,required:true</#if><#if field.validRule != ""><#if field.isRequired == 1>,</#if>${field.validRule}:true</#if>}"<#if field.isWebSign == 1>,isWebSign:true</#if> <#if (field.getPropertyMap().displayHoverTitle == 1)>title="${field.getPropertyMap().hoverTitle}"</#if>></textarea>
			</#if>
		</#if>

</#macro>