
/*
	placeholder 函数
*/
function placeholder(){
	
	var isPlaceholderSupport = (function(){return 'placeholder' in document.createElement('input')})();

	if(isPlaceholderSupport) {
		return false;
	}
	var oForms = document.forms;
	if(oForms.length == 0) {
		return false
	}
	
	$.each(oForms, function(i, oForm){
		var oIptGroup = oForm.getElementsByTagName('input');
		$.each(oIptGroup, function(i, obj){
			if(this.type && (this.type.toLowerCase() == 'text' || this.type.toLowerCase() == 'tel' || this.type.toLowerCase() == 'email' || this.type.toLowerCase() == 'password')) {
				
				var sPlaceholderTxt = this.getAttribute('placeholder') || false;
				var oLabel = null;
				
				if(sPlaceholderTxt != false) {
					
					oLabel = document.createElement('label');
					oLabel.className = 'fm-ipt-mask';
					oLabel.innerHTML = sPlaceholderTxt;
					this.parentNode.appendChild(oLabel);
				
					if(this.value.replace(/^[\s]+|[\s]+|$/g, '').length > 0) {
						oLabel.style.display = 'none';
					} else {
						oLabel.style.display = 'block';
					}
					
					$(oLabel).click('click', function(){
						obj.focus();
					});
					
					$(this).focus(function(){
						oLabel.style.display = 'none';
					});
					$(this).blur(function(){
						if(this.value.replace(/^[\s]+|[\s]+|$/g, '').length < 1) {
							this.value = '';
							oLabel.style.display = 'block';
						}
					});
					return;
				}
			}
		});
	});
}




function radioCheckbox(range){
	
		var oBody = range || document.body,
			aInputs = oBody.getElementsByTagName('input'),
			iLen = aInputs.length,
			oIpt = null;
		 
			while (oIpt = aInputs[--iLen]){
				if(oIpt.type == 'radio' || oIpt.type == 'checkbox') {
					var sName = oIpt.name || '',
						oParentNode = oIpt.parentNode;
					
					oParentNode.className += ' ' + oIpt.type + 'Label';
					
					var div = document.createElement('div');
					div.style.cssText = 'height:0px;line-height:0px;overflow:hidden';
				
					var oSpan = document.createElement('span');
					oSpan.className = oIpt.type + ' fl xye_'+ sName;
					
					oIpt.checked ? 
						oSpan.className += ' checked':
						oSpan.className = oSpan.className.replace(/\s?checked/g, '');
			
					
					oParentNode.insertBefore(oSpan, oParentNode.children[0]);
					oParentNode.appendChild(div);
					div.appendChild(oIpt);
					
					
					
					
					fnEvent(oParentNode, oSpan, oIpt, sName);
				}
			}
		
		function fnEvent(oP, oSpan, oIpt, sName){
			var oP = oP,
				oIpt = oIpt,
				type = oIpt.type,
				oNow = oSpan,
				sName = sName || '';
			
			$(oIpt).bind('change', function(event){
				var oEvent = event || window.event;
				try{
					oEvent.stopPropagation && oEvent.stopPropagation();
					oEvent.cancelBubble && (oEvent.cancelBubble = true);
				} catch(e){}
			
				if(type == 'checkbox') {
					this.checked ? 
						oNow.className += ' checked':
						oNow.className = oNow.className.replace(/\s?checked/g, '');
				
				}else if(type == 'radio') {
					
					var aTempArr =  fnEvent['rd_' + oIpt.name] = fnEvent['rd_' + oIpt.name] || byCn('span', 'xye_' + sName, document.body);
				
					for (var i = 0, l = aTempArr.length; i < l; i++) {
						if(aTempArr[i].className.substr(0, aTempArr[i].className.indexOf(' checked'))) {
							aTempArr[i].className = aTempArr[i].className.replace(/\s?checked/g, '');
						}
					}
					
					oIpt.checked && (oNow.className.indexOf(' checked') == -1) && (oNow.className += ' checked');
				
				}
			});


			oP.onclick = function(event){
				var event = event || window.event;
				event.stopPropagation && event.stopPropagation();
				event.cancelBubble && (event.cancelBubble = true);
				$(oIpt).trigger("change");
				
			}
		}


		
		
		function byCn(tName, cName, oParent) {
			var tName = tName || '*',
				oParent = oParent || document,
				cName = cName || !0,
				result = [];
			
			return cName && oParent.getElementsByClassName ? oParent.getElementsByClassName(cName) :(function(){
			
				var reClass = new RegExp("(^| )" + cName + "( |$)");
				var aElem = oParent.getElementsByTagName(tName);
				
				for (var i = 0; i < aElem.length; i++) reClass.test(aElem[i].className) && result.push(aElem[i]);
				return result;
			})();
		}

	
	
}



function niceSelect(){

/*
	select 美化
*/


	$('.ui-select-box').remove();

	
	var oSelect = document.getElementsByTagName('select');
	
	for(var l = oSelect.length -1; l>=0; l--) {
		oSelect[l].style.display = 'none';
		//oSelect[l].parentNode.style.width = "auto";
		var iLabel = document.createElement('label');
		var option = oSelect[l].children;
		var disabled = oSelect[l].disabled;
	
		
		iLabel.className = "ui-select-box";
		iLabel.innerHTML = [
			'<a class="chosen-single '+ (disabled ? 'ui-select-disabled' : '') +' '+  oSelect[l].className + '">' + option[oSelect[l].selectedIndex].innerHTML + '&nbsp;<i class="fm-ui-arrow"><em></em></i></a>',
			'<ol  >' + oSelect[l].innerHTML.replace(/option/ig, 'li') + '</ol>',
			'<i class="ui-select-arrow"></i>'
		].join('');
		oSelect[l].parentNode.appendChild(iLabel);

		
		for(var i=0; i < option.length; i++){
			
			(function(index, l){
				var oA = iLabel.children[0];
				var oUl = iLabel.children[1];
				var oI = iLabel.children[2];
				if(disabled) {
					return
				}
				oA.onclick = function(ev){
					var oEvent = ev || event;
					stopPropagation(oEvent);
					closeUl();
					$(this).parents('.pr').css('zIndex', 500);
					/* 结局一个不知什么原因的bug */
					setTimeout(function(){
						oUl.style.display = 'block'; 
						oUl.className = 'ui-pop-fadein';
					}, 10)
				
				}
				oUl.children[i].onclick = function(ev){
					var oEvent = ev || event;
					stopPropagation(oEvent);
					oA.innerHTML = this.innerHTML + '&nbsp;<i class="fm-ui-arrow"><em></em></i>';
					oUl.style.display = 'none';
					oUl.className = '';
					oSelect[l].selectedIndex = index;
					oSelect[l].onchange && oSelect[l].onchange();
					if(!(typeof $ == 'undefined') && $.isFunction($)) {
						$(oSelect[l]).trigger("change");
					}
				}
				
			})(i, l);
		} 
	}

	var oUl = $('.ui-select-box');

	function closeUl (){
		
		 for(var l = oUl.length -1; l>=0; l--){
			oUl[l].children[1].style.display = 'none';
			oUl.className = '';
		 }

		$(oUl).parents('.pr').css('zIndex', 0);
	}

	function stopPropagation(oEvent) {
		oEvent.stopPropagation ? oEvent.stopPropagation() : (oEvent.cancelBubble = true)
	}
	
	$(document).click(closeUl);
}