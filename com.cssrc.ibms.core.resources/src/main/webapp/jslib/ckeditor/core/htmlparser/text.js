(function(){CKEDITOR.htmlParser.text=function(a){this.value=a;this._={isBlockLike:false};};CKEDITOR.htmlParser.text.prototype={type:CKEDITOR.NODE_TEXT,writeHtml:function(b,a){var c=this.value;if(a&&!(c=a.onText(c,this))){return;}b.text(c);}};})();