UE.commands['tablefileDialog'] = {
		execCommand : function(cmdName){
			me.curTableFile = this.selection.getRange().getClosedNode();
			this.ui._dialogs['tablefileDialog'].open();
			return true;
		},
		queryCommandState : function() {

			var el = this.selection.getRange().getClosedNode();
			if (!el) {
				return -1;
			} else if (el.tagName.toLowerCase() == 'input'
					|| el.tagName.toLowerCase() == 'textarea'
					|| el.tagName.toLowerCase() == 'select') {
				return this.highlight ? -1 : 0;
			}
			return -1;
		}
};
