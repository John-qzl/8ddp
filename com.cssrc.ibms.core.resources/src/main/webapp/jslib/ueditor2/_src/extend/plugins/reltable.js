/**
 * 关系表操作
 */
UE.plugins['reltable'] = function() {
	var me = this,
		keys = domUtils.keys,
		addClass = domUtils.addClass,
		removeClass = domUtils.removeClass;

	var tempDiv = null,
		relTableDiv = null;

	/**
	 * 剪切关系表
	 */
	me.commands['cutreltable'] = {
		queryCommandState: function(){
			if(this.highlight ){return -1;}
			var start = me.selection.getStart(),
				div = domUtils.findParentByTagName(start,'div',true);
			if(!div)return -1;
			if(div.className=='relTableToolBar')
				div = domUtils.findParentByTagName(div,'div',false);
			if(div.getAttribute("type")!='reltable')return -1;
			tempDiv = div;
			return 0;
		},
		execCommand: function(cmdName, opt){
			if(!tempDiv)return;
			relTableDiv = tempDiv;
			domUtils.remove(relTableDiv);
		}
	};

	/**
	 * 粘贴关系表
	 */
	me.commands['pastereltable'] = {
		queryCommandState: function() {
			if(this.highlight||!relTableDiv){return -1;}
			return 0;
		},
		execCommand: function(cmdName, opt) {
			var start = me.selection.getStart();
			if(!start||!relTableDiv)return;
			if(start.tagName=='TD'){
				start.appendChild(relTableDiv);
			}
			else{
				utils.insertAfter(relTableDiv, start);
			}
			relTableDiv = null;
		}
	};
};