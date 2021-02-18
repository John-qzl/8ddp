var _mm;
var VoteContent = function(options) {
	var _common_model = "<textarea style=\"resize:none;width:100%; min-height:57px\"></textarea>";
	var _vote = $.extend({
		spyjModel : [],
		opinion : "",
		mcode : ""
	}, options || {});
	var self = this;
	var um = null;
	// 初始化审批意见模板
	this.init = function() {
		if (_vote.spyjModels && _vote.spyjModels.length > 0) {
			self.initVoteModelHtml();
		} else {
			// 默认审批意见模板是简单的textarea
			$("#spyj_voteContent").html($(_common_model).val(_vote.opinion));
		}

	}

	// 富文本编辑模式，火狐下checkbox兼容性有问题
	this.initEdit = function() {
		var URL = __ctx + "/jslib/ueditor2/";
		var reviseURL = __ctx + "/";
		window.UEDITOR_CONFIG = {
			UEDITOR_HOME_URL : URL,
			wordCount : false,
			elementPathEnabled : false,
			toolbars : []
		}
		um = new baidu.editor.ui.Editor({
			initialFrameWidth : 1000,
			initialFrameHeight : 50
		});
		um.render("spyj_voteContent");
		um.addListener('ready', function() {
			self.initVoteModelHtml();
		});
	}
	// 选择常用语设置中的审批模板
	this.initVoteModelHtml = function() {
		// 切换成文本框模式
		var showTip = function() {
			var tip = "点击切换按钮切换成文本框模式<br/>"
					+ "<input type='button' value='切换' onclick='replaceIuput($(this));'>"
					+ "<input type='button' value='关闭' onclick='closeTip(0);'>";
			$("#spyj_voteContent").ligerTip({
				content : tip,
				width : 180,
				x : 800,
				y : 300
			});

		}
		$(_vote.spyjModels).each(
				function(i, _m) {
					
					$("#voteContent_code").val(_vote.mcode);
					if (_vote.mcode == "" && _m.default_ == 1) {
						// um.execCommand("inserthtml",_m.expression)
						// um.setContent(_m.expression);
						$("#spyj_voteContent").html(_m.expression);
						$("#spyj_voteContent").bind({
							mouseenter : showTip,
							mouseleave : closeTip
						});
						return false;
					} else if (_vote.mcode == _m.code) {
						// um.execCommand("inserthtml",_m.expression)
						$("#spyj_voteContent").html(_m.expression);
						$("#spyj_voteContent").bind({
							mouseenter : showTip,
							mouseleave : closeTip
						});
						return false;
					} else {
						// um.execCommand("inserthtml",_vote.opinion)
						$("#spyj_voteContent").html(
								$(_common_model).val(_vote.opinion));
						$("#spyj_voteContent").unbind("mouseenter").unbind("mouseleave");
					}
				});
	}
	// 设置审批意见内容，将整个模板作为内容
	this.setVoteContent = function() {
		if (_vote.spyjModels && _vote.spyjModels.length > 0) {
			self.setVoteCheckBox();
			self.setInputSelect();
			self.setRadioBox();
			self.setInputText();
			self.setTextarea();
			$("#voteContent").val($("#spyj_voteContent").html())
		} else {
			$("#voteContent").val(
					$("#spyj_voteContent").children(":first").val())
		}
	}

	// 获取审批意见模板
	this.getVoteContent = function() {
		self.setVoteContent();
		return $("#voteContent").val();
	}

	// 设置审批意见模板checkbox选中状态
	this.setVoteCheckBox = function() {
		$("#spyj_voteContent").find(":checkbox").each(function(i, ck) {
			if ($(ck).attr("checked")) {
				$(ck).attr("checked", "checked");
			} else {
				$(ck).removeAttr("checked");
			}
		})
	}
	// 设置审批意见模板select控件选中的值
	this.setInputSelect = function() {
		$("#spyj_voteContent").find("select").each(function(i, ck) {
			$(ck).attr("value", $(ck).val());
			$(ck).find("option").each(function(j, op) {
				if ($(op).val() == $(ck).val()) {
					$(op).attr("selected", "selected");
				}
			})
		})
	}

	// 设置审批意见模板RadioBox控件选中的值
	this.setRadioBox = function() {
		$("#spyj_voteContent").find(":radio").each(function(i, ck) {
			if ($(ck).attr("checked")) {
				$(ck).attr("checked", "checked");
			} else {
				$(ck).removeAttr("checked");
			}
		})
	}
	// 设置审批意见模板inputText控件选中的值
	this.setInputText = function() {
		$("#spyj_voteContent").find("input:text").each(function(i, t) {
			$(t).attr("value", $(t).val());
		})
	}
	// 设置审批意见模板textarea控件选中的值
	this.setTextarea = function() {
		$("#spyj_voteContent").find("textarea").each(function(i, t) {
			$(t).html($(t).val());
		})
	}
	// 判断是否已经填写审批意见
	this.hasVote = function() {
		var content = self.getVoteContent();
		var length = $("#voteContent").length;
		return length == 0 || (content && content.trim() != '')
	}
}

// 选择常用语
function addComment(spyjModel) {
	if (spyjModel) {
		var objContent = document.getElementById("voteContent");
		var selItem = $('#selTaskAppItem').val();
		jQuery.insertText(objContent, selItem);
	}
}
function replaceIuput(obj, _vote) {
	var _common_model = "<textarea rows=\"2\" cols=\"78\" maxlength=\"512\"></textarea>";
	_mm = $("#spyj_voteContent").html();
	$("#spyj_voteContent").html(_common_model);
	$("#spyj_voteContent").ligerHideTip();

	var changeTip = function() {
		var tip = "点击切换按钮切换成模板模式<br/>"
				+ "<input type='button' value='切换' onclick='replaceM($(this),_mm);'>"
				+ "<input type='button' value='关闭' onclick='closeTip(0);'>";
		showTip(tip);
	}
	$("#spyj_voteContent").unbind('mouseenter').unbind('mouseleave');
	$("#spyj_voteContent").bind({
		mouseenter : changeTip,
		mouseleave : closeTip
	});
	// $(obj).parent().parent().remove();
}

function replaceM(obj, _vote) {
	$("#spyj_voteContent").html(_mm);
	$("#spyj_voteContent").ligerHideTip();
	var changeTip = function() {
		var tip = "点击切换按钮切换成文本框模式<br/>"
				+ "<input type='button' value='切换' onclick='replaceIuput($(this));'>"
				+ "<input type='button' value='关闭' onclick='closeTip(0);'>";
		showTip(tip);
	}
	$("#spyj_voteContent").unbind('mouseenter').unbind('mouseleave');
	$("#spyj_voteContent").bind({
		mouseenter : changeTip,
		mouseleave : closeTip
	});
	// $(obj).parent().parent().remove();
}
function showTip(tip){
	$("#spyj_voteContent").ligerTip({
		content : tip,
		width : 180,
		x : 800,
		y : 300
	});

}
function closeTip(time){
	if(time){
		//setTimeout('$("#spyj_voteContent").ligerHideTip()',5000);
	}else{
		//$(obj).parent().parent().remove();
		setTimeout('$("#spyj_voteContent").ligerHideTip()',time);
	}
}
