UE.commands['groupopinionDialog'] = {
	execCommand : function(cmdName) {
		//
		me.curOpinion = this.selection.getRange().getClosedNode();
		this.ui._dialogs['groupopinionDialog'].open();
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
