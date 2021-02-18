<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <title>在线设计器</title>
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
    	//console.log(window.parent.frames);
	   // var dialog = window;// 调用页面的dialog对象(ligerui对象)
	   var dialog = null;
		if (frameElement) {
			dialog = frameElement.dialog;
		}
    	function submitFormHtml(){
    		var htmlres=UE.getEditor('editor').getContent();
    		var id=$("#id").val();
    		var url="${ctx}/dp/form/modifyFormTemplate.do";
    		var params={htmlres:htmlres,id:id};
    		$.post(url,params,function(data){
    			console.log(data);
    			if(data.success=="true"){
    				$.ligerDialog.confirm('数据保存成功，是否关闭该页面',function (rtn){
							if(rtn){
								/* window.top.document.getElementById('10000000410001')
								.contentWindow.document.getElementById('form_table_frame')
								.contentWindow.document.getElementById('tableFrame')
								.contentWindow.location.reload(true) */
								//window.location.reload(true);
								dialog.close();
							}
						});
    			}else{
    				$.ligerDialog.error(data.msg,"保存失败");
    				UE.getEditor('editor').setContent(data.html, false);
    			}
    		});
    	}
        function help() {
            window.top.$.ligerDialog.open({
                url:'',
                width:300,
                height:800,
                left:0,
                top:0
            })
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
	<form id="htmlform"action="${ctx}/dp/form/modifyFormTemplate.do" method="post" style="height:auto;">
		<input type="hidden" id="htmlres" name="htmlres"/>

    <div class="panel-toolbar" style="background:none;">
      <div class="toolBar">
        <div class="group"><a class="link edit" href="####" onclick="submitFormHtml()">提交</a></div>
        <div class="group"><a class="link copyTo" href="####" onclick="help()">帮助</a></div>
      </div>
    </div>

		<input type="hidden"  id="id" name="id" value="${id}"/>

	</form>
	<script type="text/javascript">

    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	/*
	 'subscript':'下标','superscript':'上标', 'selectall':'全选', 'print':'打印', 'preview':'预览',
         'removeformat':'清除格式', 'insertrow':'前插入行', 'insertcol':'前插入列', 'mergeright':'右合并单元格', 'mergedown':'下合并单元格',
        'deleterow':'删除行', 'deletecol':'删除列', 'splittorows':'拆分成行','splittocols':'拆分成列', 'splittocells':'完全拆分单元格',
        'mergecells':'合并多个单元格', 'deletetable':'删除表格', 'cleardoc':'清空文档','insertparagraphbeforetable':"表格前插入行",
        'fontfamily':'字体', 'fontsize':'字号', 'paragraph':'段落格式', 'edittable':'表格属性','edittd':'单元格属性',
         'spechars':'特殊字符', 'searchreplace':'查询替换',  'justifyleft':'居左对齐', 'justifyright':'居右对齐', 'justifycenter':'居中对齐',
        'justifyjustify':'两端对齐', 'forecolor':'字体颜色', 'backcolor':'背景色', 'fullscreen':'全屏',  'imagecenter':'居中',
        'lineheight':'行间距','edittip' :'编辑提示','customstyle':'自定义标题', 'autotypeset':'自动排版',
        'inserttable':'插入表格','drafts': '从草稿箱加载','test': '输入框','checkbox': '确认框'
	*/
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
			'forecolor', 'input','checkbox','photo','|','format','help'//,'autotypeset'

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
