<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ib" uri="http://www.ibms.cn/formFun"%>
<%@taglib prefix="ui" uri="http://www.ibms.cn/formUI"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻公告编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/form-setting/editor_config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_api.js"></script>
</head>
<body>
	<ib:toolbar toolbarId="toolbar1" pkId="${insNews.newId}"/>	
	<div id="p1" class="form-outer">
		<form id="form1" method="post">
			<div style="padding: 5px;">
				<input id="pkId" name="newId" class="mini-hidden" value="${insNews.newId}" />
				<table class="table-detail" cellspacing="1" cellpadding="0">
					<caption>新闻公告基本信息</caption>
					<tr>
						<th>标题 <span class="star">*</span> ：
						</th>
						<td>
						<input name="subject" value="${insNews.subject}" class="mini-textbox" vtype="maxLength:120" style="width: 80%" required="true" emptyText="请输入标题" />
						</td>
						<th>作者 *：</th>
						<td>
						<input name="author" value="${insNews.author}" class="mini-textbox" vtype="maxLength:50" style="width: 80%" required="true" />
						</td>
					</tr>
					<tr>
						<th>标签 <span class="star">*</span> ：
						</th>
						<td>
						<input name="tag" value="${insNews.tag}" class="mini-textbox" vtype="maxLength:100" style="width: 80%" required="true"/>
						</td>
						<th>关键字 *：</th>
						<td>
						<input name="keywords" value="${insNews.keywords}" class="mini-textbox" vtype="maxLength:50" style="width: 80%" required="true"/>
						</td>
					</tr>
					<tr>
						<th>是否为图片信息 ：</th>
						<td>
						<input type="checkbox" name="isImg" value="YES" <c:if test="${insNews.isImg=='YES'}">checked="checked"</c:if> onclick="changeIsImg(this)" />
						</td>
						<th>状态<span class="star">*</span> ：
						</th>
						<td>
						<ui:miniRadioList name="status" value="${insNews.status}" id="status" data="[{id:'Draft',text:'草稿'},{id:'Issued',text:'发布'}]" required="true" emptyText="请输入状态" />
						</td>
					</tr>
					<tr id="imgRow" <c:if test="${empty insNews.isImg || insNews.isImg=='NO'}">style="display:none"</c:if>>
						<th>标题图片：</th>
						<td colspan="3"><input name="imgFileId" value="${insNews.imgFileId}" class="mini-hidden" vtype="maxLength:64" /> 
						<img src="${ctx}/oa/system/sysFile/imageView.do?thumb=true&fileId=${insNews.imgFileId}" class="upload-file" /></td>
					</tr>
					<tr id="comment">
						<th>是否允许评论 *：</th>
						<td><ui:radioBoolean name="allowCmt" value="${insNews.allowCmt}" required="true" /></td>
					</tr>
					<tr>
						<td colspan="4">
						<textarea  name="content" id="content" >${insNews.content}</textarea>
						</td>
					</tr>

				</table>
			</div>
		</form>
	</div>
	<ib:formScript formId="form1" baseUrl="oa/portal/insNews" entityName="com.cssrc.ibms.index.model.InsNews" />
	<script type="text/javascript">
	var newsUeditor = null;
	$(function(){
		newsUeditor = new baidu.editor.ui.Editor({minFrameHeight:300,initialFrameWidth:'100%',lang:'zh_cn'});
		newsUeditor.render("content"); 
		 $(".upload-file").on('click',function(){
			 var img=this;
			_UserImageDlg(true,
				function(imgs){
					if(imgs.length==0) return;
					$(img).attr('src','${ctx}/sys/core/file/imageView.do?thumb=true&fileId='+imgs[0].fileId);
					$(img).siblings('input[type="hidden"]').val(imgs[0].fileId);
					
				}
			);
		 });
	});
	
	//覆盖表单提交方法
	function selfSaveData() {
	    var url = "${ctx}/oa/portal/insNews/save.do";
	  	var content = $('#content').val(newsUeditor.getContent());

	  	 var form = new mini.Form("form1");
		 form.validate();
		 if (!form.isValid()) {
	            return;
	        }
		 var formData=$("#form1").serializeArray();
	  	
	    _SubmitJson({
	        url: url,
	        method: 'POST',
	        data: formData,
	        success: function(result) {
	        	CloseWindow('ok');
	        }
	    });
	}
	
	function changeIsImg(ck){
		if(ck.checked){
			$("#imgRow").css("display","");
		}else{
			$("#imgRow").css("display","none");
		}
	}
	</script>
	<ib:formScript formId="form1" baseUrl="oa/portal/insNews" entityName="com.cssrc.ibms.index.model.InsNews"/>
</body>
</html>