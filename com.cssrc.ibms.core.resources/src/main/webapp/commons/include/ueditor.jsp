<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/form-setting/editor_config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/jslib/ueditor2/editor_api.js"></script>

<script type="text/javascript">
	var editor = [];
	$(function() {
		$("textarea.myeditor").each(function(num) {
			var ue =new baidu.editor.ui.Editor({minFrameHeight:300,initialFrameWidth:800,lang:'zh_cn'});
			ue.render(this);
			editor.push(ue);
		});
	});
	function getEditorData(obj){
		return obj.getContent();
	}
</script>
