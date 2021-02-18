<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <title>模板表单设计器</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
      <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-all.css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/web.css">


     <f:js pre="jslib/lang/common" ></f:js>
    <script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
    <f:js pre="jslib/lang/js" ></f:js>
    <script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>


	<script type="text/javascript" charset="utf-8" src="${ctx}/editor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/editor/ueditor.all.js"> </script>

<%@include file="/commons/include/get.jsp" %>
  <f:link href="tree/zTreeStyle.css"></f:link>

    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="${ctx}/editor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" charset="utf-8">
	$(function(){
		if(!!"${msg.success}"){
			if("${msg.success}"=='false'){
				$.ligerDialog.error("${msg.info}","校验失败");
			}
			
		}
	})
</script>
<script type="text/javascript" charset="utf-8">
	
    var dialog = window;// 调用页面的dialog对象(ligerui对象)
    if (frameElement) {
        dialog = frameElement.dialog;
    }

	//初始化时的html或者暂存后的html
	var originalHtml='${html}';
	//检查两个字符串是否相同
	function checkHtmlCommon(original,current){
		if(!original&&!current){//原始html为空 && 当前为空
			return true;
		}
		if(!!original&&!!current){//original不为空&&current不为空
			if(original==current){
				return true;
			}
		}
		return false;
	}
	//暂存数据
	//async true:异步    ;  false:同步
	function temporaryStorage(async,handleFunction){
		var originalHtml=UE.getEditor('editor').getContent();
		var id=$("#MID").val();
		//保存到服务器
		$.ajax({
			type:"post",
			data:{"html":originalHtml,"id":id},
			url: "${ctx}/dp/form/temporaryStorage.do",
			async:async,
			success:function(data){
				if(data.success){
					$.ligerDialog.success(data.info,"提示",function(opt){
						if(opt){
							handleFunction();
						}
					});
				}else{
					$.ligerDialog.error(data.info);
				}
			}
		})

	}
	//关闭页面
	function closeDesigner(){
		var currentHtml=UE.getEditor('editor').getContent();
		if(checkHtmlCommon(originalHtml,currentHtml)){
			closeDialog();
		}else{
			$.ligerDialog.confirm('数据已修改，是否暂存？',function (rtn){
				var close=true;
				if(rtn){
					//执行暂存操作
					temporaryStorage(false,closeDialog);
				}else{
					closeDialog();
				}
			});
		}

	}
	//关闭弹框
	function closeDialog(){
        dialog.close();
	}

   	// 下一步
   	function preview(){
   		UE.getEditor('editor').execCommand('format');
   		var htmlres=UE.getEditor('editor').getContent();
   		$('#content').val(htmlres);
   		$('#htmlform').attr('action','${ctx}/template/manage/validateTemplateHtml.do');
   		$('#htmlform').submit();
   	}
   	
   	//跳转到模板基础信息页
    function backbasicinfo() {
		var currentHtml=UE.getEditor('editor').getContent();
		$('#content').val(currentHtml);
		if(!checkHtmlCommon(originalHtml,currentHtml)){
			$.ligerDialog.confirm('数据已修改，是否暂存？',function (rtn){
				if(rtn){
					//执行暂存操作
					var back=temporaryStorage(false,backBasicInfo);
				}else{
					backBasicInfo();
				}
			});
		}else{
			backBasicInfo();
		}

	}
	function backBasicInfo(){
   		$('#htmlform').attr('action','${ctx}/dp/form/backBasicInfo.do');
		$('#htmlform').submit();
   	}
    </script>
<style>
	h2 {
		color:#067cd8;
		    font-size: 18px;
	}
	input.btn {
		background:url(${ctx}/dpImg/submit.png) no-repeat;
		width:62px;height:21px;
		border: none;
		margin: 5px 10px;
	}
  .panel-toolbar{
    position: fixed;
    bottom: 0;
    width: 100%;
    z-index: 10000;
  }
</style>
</head>
<body>
	 <h2>在线表单设计器</h2>
    <script id="editor" type="text/plain" style="width:1024px;height:500px;">${html}</script>
	<form id="htmlform"action="" method="post" >
		<input type="hidden" id="htmlres" name="htmlres"/>
			<div class="panel-toolbar">
				<div class="group"><a href="####" class="link" onClick="temporaryStorage(true)">暂存</a></div>
				<div class="group"><a href="####" class="link" onClick="preview()">下一步</a></div>
				<div class="group"><a href="####" class="link" onClick="backbasicinfo()">返回模板基础信息</a></div>
				<div class="group"><a href="####" class="link" onClick="closeDesigner()">关闭</a></div>
			</div>
		<input type="hidden"  id="id" name="id" value="${id }"/>
		<input type="hidden"  id="content" name="content" value=""/>
		<input type="hidden" id="name" name="name" value="${name }"/>
		<input type="hidden" id="sign" name="sign" value="${sign }"/>
		<input type="hidden" id="status" name="status" value="${status }"/>
		<input type="hidden" id="attention" name="attention" value="${attention }"/>
		<input type="hidden" id="moduleId" name="moduleId" value="${moduleId }"/>
		<input type="hidden" id="folderId" name="folderId" value="${folderId }" />
		<input type="hidden" id="MID" name="MID" value="${MID}" />
		<input type="hidden" id="type" name="type" value="${type}" />
		<input type="hidden" id="fcName" name="fcName" value="${fcName}" />
	</form>
	<script type="text/javascript">

	var ue = UE.getEditor('editor', {
		toolbars: [
			['fullscreen', 'source', 'undo', 'redo',

			//表格相关
			 'inserttable','insertrow', 'insertcol', 'mergeright', 'mergedown','deleterow', 'deletecol', 'splittorows','splittocols', 'splittocells',
			'mergecells', 'deletetable', 'insertparagraphbeforetable',

			'spechars', 'searchreplace',
			//字体相关
			'fontfamily', 'fontsize', 'paragraph', 'justifyleft', 'justifyright', 'justifycenter',
			'justifyjustify',

			'subscript','superscript', 'selectall', 'removeformat',
			'print', 'preview','cleardoc',
			//自定义功能
			'forecolor', 'input','checkbox','photo','|','format','help','tablefront','tabletitle','tabletail','macros','markupactualval','markuprequireval'//,'autotypeset'

			]
		],
		autoHeightEnabled: true,
		autoFloatEnabled: true
	});

    function isFocus(e){
        alert(UE.getEditor('editor').isFocus());
        UE.dom.domUtils.preventDefault(e)
    }
    function setblur(e){
        UE.getEditor('editor').blur();
        UE.dom.domUtils.preventDefault(e)
    }
    function insertHtml() {
        var value = prompt('插入html代码', '');
        UE.getEditor('editor').execCommand('insertHtml', value)
    }
    function createEditor() {
        enableBtn();
        UE.getEditor('editor');
    }
    function getAllHtml() {
        alert(UE.getEditor('editor').getAllHtml())
    }
    function getContent() {
        var arr = [];
        arr.push("使用editor.getContent()方法可以获得编辑器的内容");
        arr.push("内容为：");
        arr.push(UE.getEditor('editor').getContent());
        alert(arr.join("\n"));
    }
    function getPlainTxt() {
        var arr = [];
        arr.push("使用editor.getPlainTxt()方法可以获得编辑器的带格式的纯文本内容");
        arr.push("内容为：");
        arr.push(UE.getEditor('editor').getPlainTxt());
        alert(arr.join('\n'))
    }
    function setContent(isAppendTo) {
        var arr = [];
        arr.push("使用editor.setContent('欢迎使用ueditor')方法可以设置编辑器的内容");
        UE.getEditor('editor').setContent('欢迎使用ueditor', isAppendTo);
        alert(arr.join("\n"));
    }
    function setDisabled() {
        UE.getEditor('editor').setDisabled('fullscreen');
        disableBtn("enable");
    }

    function setEnabled() {
        UE.getEditor('editor').setEnabled();
        enableBtn();
    }

    function getText() {
        //当你点击按钮时编辑区域已经失去了焦点，如果直接用getText将不会得到内容，所以要在选回来，然后取得内容
        var range = UE.getEditor('editor').selection.getRange();
        range.select();
        var txt = UE.getEditor('editor').selection.getText();
        alert(txt)
    }

    function getContentTxt() {
        var arr = [];
        arr.push("使用editor.getContentTxt()方法可以获得编辑器的纯文本内容");
        arr.push("编辑器的纯文本内容为：");
        arr.push(UE.getEditor('editor').getContentTxt());
        alert(arr.join("\n"));
    }
    function hasContent() {
        var arr = [];
        arr.push("使用editor.hasContents()方法判断编辑器里是否有内容");
        arr.push("判断结果为：");
        arr.push(UE.getEditor('editor').hasContents());
        alert(arr.join("\n"));
    }
    function setFocus() {
        UE.getEditor('editor').focus();
    }
    function deleteEditor() {
        disableBtn();
        UE.getEditor('editor').destroy();
    }
    function disableBtn(str) {
        var div = document.getElementById('btns');
        var btns = UE.dom.domUtils.getElementsByTagName(div, "button");
        for (var i = 0, btn; btn = btns[i++];) {
            if (btn.id == str) {
                UE.dom.domUtils.removeAttributes(btn, ["disabled"]);
            } else {
                btn.setAttribute("disabled", "true");
            }
        }
    }
    function enableBtn() {
        var div = document.getElementById('btns');
        var btns = UE.dom.domUtils.getElementsByTagName(div, "button");
        for (var i = 0, btn; btn = btns[i++];) {
            UE.dom.domUtils.removeAttributes(btn, ["disabled"]);
        }
    }

    function getLocalData () {
        alert(UE.getEditor('editor').execCommand( "getlocaldata" ));
    }

    function clearLocalData () {
        UE.getEditor('editor').execCommand( "clearlocaldata" );
        alert("已清空草稿箱")
    }
</script>
</body>
</html>
