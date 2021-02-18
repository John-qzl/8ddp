/*
    desc:IbmsSign签章
    author:zxg
    date:2016-12-14
 */

IbmsWebSign = function(options) {
	{
		var self = this;
		this.init=true;
		this.signArray = [];
		this.signResult = [];
		this.singIdS = [];
		var _sign = $.extend({
			signArea : '',
			addBtn : '',
			width : 80,
			height : 78,
			offset : 10, // 边界值
			curUser : -1,
			callBack : null
		}, options || {});
	}
	if (_sign.signArea == '') {
		$.ligerDialog.warn("签名域不能为空", "提示信息");
		return;
	}
	if (_sign.addBtn == '') {
		//$.ligerDialog.warn("签名按钮不能为空", "提示信息");
		//return;
	}

	var _parent = $("#" + _sign.signArea).addClass('zsign');
	var pposition = _parent.position();
	if(!pposition){
		self.init=false;
		return;
	}
	var range = {
		minX : pposition.left + _sign.offset,
		minY : pposition.top + _sign.offset,
		maxX : _parent.width(), // 扣去2个padding=8px以及2个边框1px
		maxY : _parent.height()
	};
	var _html = "<div class='sign' style='height:"
			+ _sign.height
			+ "px;width:"
			+ _sign.width
			+ "px;top:"
			+ range.minY
			+ "px;left:"
			+ range.minX
			+ "px'><img style='width:"
			+ _sign.width
			+"px;height:"
			+ _sign.height
			+"px' src='' draggable='false'/><button class='btn ok' >确定</button><button class='btn del' >删除</button></div>";

	/*
	 * 保存现有的sign
	 */
	this.saveSigns = function() {
		var sign=self.signArray.length;
		if(sign==undefined){
			return;
		}
		for (i = 0; i < self.signArray.length; i++) {
			var data = self.signArray[i];
			if (data.status == 0) {
				saveSign(i, data);
			}
		}
		return self.signResult;
	}
	/*
	 * load sign
	 */
	this.loadSign = function(signId) {
		var signIds = signId.split(",");
		for (var i = 0; i < signIds.length; i++) {
			if(signIds[i]&&signIds[i]!=""){
				var signHtml=self.creatSign(signIds[i])
				var sign = $(signHtml).appendTo(_parent);
			}
		}
	}
	/*
	 * 从服务器读取sign数据
	 */
	this.creatSign = function(signId) {
		var mill = (parseInt(Math.random() * 10000)).toString();
		var url = __ctx + '/oa/system/sign/getItem.do?id='+signId+"&mill="+mill;
		$.ajax({
			url : url,
			type : "post",
			async : false,
			success : function(result) {
				var json = eval('(' + result + ')');
				var signPosition=eval('(' + json.position + ')');
				var signImg=self.creatEml(json.image,signPosition)
				var sign = $(signImg).appendTo(_parent);
			},
			error : function() {
				$.ligerDialog.warn("连接超时，请联系系统管理员！", "提示信息");
			}
		})
	}
	/*
	 * 创建sign element
	 */
	this.creatEml=function(image,signPosition){
		//当前印章域坐标
		var areaTop=pposition.top;
		var areaLeft=pposition.left;
		/*if(signPosition.areaTop!=0){
			areaTop=areaTop/signPosition.areaTop;
		}else{
			areaTop=1
		}*/
		/*if(signPosition.areaLeft!=0){
			areaLeft=areaLeft/signPosition.areaLeft;
		}else{
			areaLeft=1;
		}*/

		//印章坐标
		var top=signPosition.top+(areaTop-signPosition.areaTop);
		var left=signPosition.left+(areaLeft-signPosition.areaLeft);
		var html = "<div class='sign ok' style='height:"
			+ _sign.height
			+ "px;width:"
			+ _sign.width
			+ "px;top:"
			+ top
			+ "px;left:"
			+ left
			+ "px'><img class='imgok' style='width:"
			+ _sign.width
			+"px;height:"
			+ _sign.height
			+"px' src='data:image/gif;base64,"+image+"'/></div>";
		return html;
	}
	/*
	 * 获取当前存在的sign
	 * 
	 */
	this.getSign = function() {
		var signs = [];
		for (i = 0; i < self.signArray.length; i++) {
			var data = self.signArray[i];
			if (data.status == 1) {
				signs.push(data);
			}
		}
		return signs;
	}
	/*
	 * 获取当前存在的sign ids
	 */
	this.getSignIds = function() {
		var ids = "";
		for (var i = 0; i < self.signResult.length; i++) {
			ids += self.signResult[i] + ",";
		}
		if (ids != "") {
			ids = ids.substring(0, ids.length - 1);
		}
		return ids;
	}
	/*
	 * 弹出选择印章窗口
	 */
	var _add = $(_sign.addBtn).click(
			function(e) {
				_add.attr('disabled', 'disabled');
				var dialogWidth = 900;
				var dialogHeight = 700;
				conf = {
					dialogWidth : dialogWidth,
					dialogHeight : dialogHeight,
					help : 0,
					isSingle : true,
					status : 0,
					scroll : 0,
					center : 1
				};
				var winArgs = "dialogWidth=" + conf.dialogWidth
						+ "px;dialogHeight=" + conf.dialogHeight + "px;help="
						+ conf.help + ";status=" + conf.status + ";scroll="
						+ conf.scroll + ";center=" + conf.center;
				if (!conf.isSingle) {
					conf.isSingle = false;
				}

				var url = __ctx + '/oa/system/sign/getSign.do?userId='
						+ _sign.curUser + '&isSingle=' + conf.isSingle;
				url = url.getNewUrl();
				DialogUtil.open({
					height : conf.dialogHeight,
					width : conf.dialogWidth,
					title : '添加印章模型',
					url : url,
					userId : _sign.curUser,
					isResize : true,
					succcall : function(rtn) {
						var sign = $(_html).appendTo(_parent);
						$(sign).find("img").attr("src", rtn.img);
						$('.ok', sign).click(function() {
							addSign(sign, rtn);
						});
						$('.del', sign).click(function() {
							// 取消盖章
							sign.remove();
							_add.removeAttr('disabled');
						});
						// 绑定移动事件
						sign.on('mousedown', function(e) {
							moveEvent(e, sign);
						});
					}
				});

			});

	/*
	 * 确定盖章
	 */
	var addSign = function(sign, rtn) {
		// 确定盖章
		sign.addClass('ok').off('mousedown').find('.btn').remove();
		sign.find('img').addClass('imgok');
		_add.removeAttr('disabled');
		var data = {
			signId : rtn.signId,
			status : 0,
			img : rtn.img,
			top : parseInt(sign.css('top')),
			left : parseInt(sign.css('left'))
		}
		self.signArray.push(data);
	};

	/*
	 * 绑定移动事件
	 */
	var moveEvent = function(e, sign) {
		sign.data('x', e.clientX);
		sign.data('y', e.clientY);
		var position = sign.position();
		$(document).on('mousemove', function(e1) {
			var x = e1.clientX - sign.data('x') + position.left;
			var y = e1.clientY - sign.data('y') + position.top;
			x = x < range.minX ? range.minX : x;
			x = x > range.maxX ? range.maxX : x;
			y = y < range.minY ? range.minY : y;
			y = y > range.maxY ? range.maxY : y;
			sign.css({
				left : x,
				top : y
			});
		}).on('mouseup', function() {
			$(this).off('mousemove').off('mouseup');
		});
	};

	/*
	 * 保存sign到服务器中
	 */
	var saveSign = function(i, data) {
		// 计算position百分比
		/*
		 * 客户端宽高
		 */
		data.width = $(window).width();
		data.height = $(window).height();
		/*
		 * 印章域坐标
		 */
		data.areaTop = pposition.top;
		data.areaLeft = pposition.left;
		//当前执行任务ID
		data.taskId = "${param.taskId}";
		//印章表单数据
		var signData = new Object();
		$('input,select,textarea').each(function(i, item) {
			if ($(item).attr("name")) {
				signData[$(item).attr("name")] = $(item).val()
			}
		})
		data["signData"] = signData;
		//保存印章
		var mill = (parseInt(Math.random() * 10000)).toString();
		var url = __ctx + "/oa/system/sign/saveItem.do?mill=" + mill;
		$.ajax({
			url : url,
			type : "post",
			async : false,
			data : data,
			success : function(result) {
				var json = eval('(' + result + ')');
				self.signResult.push(json.sign_id);
				self.signArray[i].status = 1;
			},
			error : function() {
				$.ligerDialog.warn("连接超时，请联系系统管理员！", "提示信息");
			}
		})

	};
};