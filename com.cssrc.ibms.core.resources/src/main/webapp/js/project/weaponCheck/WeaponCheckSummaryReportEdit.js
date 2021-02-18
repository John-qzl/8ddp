/**
 * 武器所检总结表单的自动填充js
 */

$(function () {


    //预置属性值
    var type = $.getParameter("type");
    var missionId = $.getParameter("missionId");
  /*  $("#originFile").hide();*/
    //只有在发起人审批及之后的节点才需要展示待办结果
    if(typeof(taskDefinitionKey) != "undefined"){
    	   if (taskDefinitionKey == "UserTask3" || taskDefinitionKey == "UserTask5" || taskDefinitionKey == "UserTask6") {
    	        //$(".needToDoRes").removeAttr("style");
               changeInputToTextPlus();
    	    }
    }
 

    // 如果当前是新增页面或者流程启动页面就去 找对应策划的数据回填
    if (type == "add" || type == "startAdd") {
        //发起节点和第一个节点把添加待办的按钮放出来
        $(".relTableToolBar").removeAttr("style");

        $("input[name='m:bcsybgb:ssch']").attr("value", missionId);
        $("input[name='m:bcsybgb:spzt']").attr("value", "审批中");
        // $("#hasNeedToDo").attr("checked", "true");
        //   $("#originFile").hide();//隐藏试验依据文件的原生控件

        var html = "";
        //从策划报告表提取数据
        $.ajax({
            url: __ctx + "/rangeTest/mission/plan/getRangeTestPlanById.do?missionId=" + missionId,
            async: false,
            success: function (data) {
                if (data != null && data != "") {
                    //拉取信息

                    $("input[name='m:bcsybgb:csdw']").attr("value", data.F_SYDW);
                    //    $("input[name='m:bcsybgb:syjd']").attr("value", data.F_SYJD);
                    $("input[name='m:bcsybgb:csrq']").attr("value", data.F_KSSJ);//试验开始时间
                    $("input[name='m:bcsybgb:jssyrq']").attr("value", data.F_JSSJ);//试验结束时间
                    $("input[name='m:bcsybgb:sydd']").attr("value", data.F_SJDD);//试验地点
                    $("input[name='m:bcsybgb:wqxtbh']").attr("value", data.F_WQXTBH);//武器系统编号
                    $("input[name='m:bcsybgb:ddbh']").attr("value", data.F_DDBH);//导弹编号
                    $("input[name='m:bcsybgb:bcsybh']").attr("value", data.F_CHBGBBH);
                    $("input[name='m:bcsybgb:ssch']").attr("value", data.ID);
                    $("input[name='m:bcsybgb:ssxh']").attr("value", data.F_XHDH);
                    $("input[name='m:bcsybgb:ssxhid']").attr("value", data.F_XHID);
                    $("input[name='m:bcsybgb:syjd']").attr("value", data.F_SYRWMC); //试验任务名称
                    $("input[name='m:bcsybgb:syzz']").attr("value", data.F_SYZZ);
                    $("input[name='m:bcsybgb:syzzID']").attr("value", data.F_SYZZID); //试验任务名称
                    $("#xhID").attr("value", data.F_XHID);
//填充总结报告表编号
                    /*
                    var summaryReportCode = data.F_CHBGBBH.replace("WQSJ", "SJQR");
                    $("input[name='m:bcsybgb:bgbbh']").attr("value", summaryReportCode);
                    */
                    $("input[name='m:bcsybgb:bgbbh']").attr("value", data.F_CHBGBBH);
                    var userNames = "";
                    var userIds = "";
                    for (var i = 0; i < data.acceptanceGroup.length; i++) {
                        if (data.acceptanceGroup[i].zw == "组员") {
                            userNames += data.acceptanceGroup[i].xm + ",";
                            userIds += data.acceptanceGroup[i].xmId + ",";
                        }

                    }
                    if (userNames.length > 0) {
                        userNames = userNames.substring(0, userNames.length - 1);
                        userIds = userIds.substring(0, userIds.length - 1);
                    }
                    $("input[name='m:bcsybgb:zyqr']").attr("value", userNames);
                    $("input[name='m:bcsybgb:zyqrID']").attr("value", userIds);
                    //填充总结报告表编号
                    /*       var summaryReportCode=data.F_CHBGBBH.replace("BCSY","SJQR");
                           $("input[name='m:bcsybgb:bgbbh']").attr("value",summaryReportCode);*/
                    //试验报告回传数据(pad同步的时候存进来的)
                    if (data.F_SYBGHCSJ) {
                        var json = JSON.parse(data.F_SYBGHCSJ);
                        $("textarea[name='m:bcsybgb:syzfxdzywt']").text(json.problem);
                        $("textarea[name='m:bcsybgb:qtsm']").text(json.instructions);
                    } else {
                        //回传的报告没有内容
                        $("textarea[name='m:bcsybgb:syzfxdzywt']").text("无");
                        $("textarea[name='m:bcsybgb:qtsm']").text("无");
                    }
                    //如果试验依据文件存在,就处理文件链接之类的,
                    if (data.F_SYYJWJ != null) {
                        var file = JSON.parse(data.F_SYYJWJ);
                        //先测当前数组长度
                        var fileNumber = file.length;

                        //存储原生文件上传控件的值
                        var fileJson = "";
                        //根据数组长度直接循环下面的代码
                        var html='<div class="attachement">';
                        for (var i = 0; i < fileNumber; i++) {
                            var fileId = file[i].id;
                            $.ajax({
                            	url: __ctx+"/oa/system/sysFile/getFile.do?fileId="+fileId,
                                async: false,
                                success: function (fileData) {
                                	html+='<span class="attachement-span">';
			    			    	html+='<span fileid="'+fileData.fileId+'" name="attach" file="'+fileData.fileId+','+fileData.filename+'.'+fileData.ext+'">';
			    			    	html+='<a class="attachment" target="_blank" path="/dp/oa/system/sysFile/file_'+ fileData.fileId+'.do" onclick="AttachMent.handleClickItem(this)" title="'+fileData.filename+'">';
			    			    	html+=fileData.filename+'.'+fileData.ext+'</a></span><a href="javascript:;" onclick="AttachMent.download(this);" title="下载" class="link download"></a>&nbsp;</span>';
                                }
                            });
                            //多文件时每个文件换行
                            html+="<br/>";
                        }
                        html+="</div>";
                        $("#syyjwj").prepend(html);
                        //隐藏上传文件按钮
                        $(".selectFile").hide();
    					 $("textarea[name='m:bcsybgb:syyjwj']").attr("value",data.F_SYYJWJ);
                    }
                }
            }
        })
    } else {

        //移除自定义控件
        $("#fileJson").remove();
        $(".tableNew.clearfix.item2").remove();
        //隐藏待办删除那妞
        $(".deleteThisNeedToDo").hide();
        //如果不是发起审批节点
       // $(".needToDoRes").removeAttr("style");
        changeInputToTextPlus();
        //除了第一个节点不允许点击待办
        if(typeof(taskDefinitionKey) != "undefined"){
        	if (taskDefinitionKey != "UserTask1") {
                $("input[name='m:bcsybgb:sfydb']").attr("disabled", "disabled");
                //隐藏添加待办选项
                $(".relTableToolBar").hide();
                //隐藏删除待办
                $(".deleteThisNeedToDo").hide();
            }else {
                //发起节点和第一个节点把添加待办的按钮放出来
                $(".relTableToolBar").removeAttr("style");
            }
        } 
    }

})

//填充依据文件的值
function addBasisFile(tempFileJson, fileJson) {
    if (fileJson == "" || fileJson == null) {
        //第一个文件
        fileJson = tempFileJson;
    } else {
        //第n个文件
        fileJson = fileJson + "," + tempFileJson;
    }
    return fileJson;
}

//实验数据页面的超链接
function displayType(conf) {
    if ($.isEmpty(conf && conf.type && conf.scope)) {
        return;
    }
    var me = $(conf.scope || this);
    var url = me.attr("action");
    var params = url.getArgs();
    var _height = window.top.document.documentElement.clientHeight;
    var displayId = params.__displayId__;
    var permissionUrl = __ctx + "/oa/form/dataTemplate/getManagePermission.do";
    permissionUrl += "?displayId=" + displayId;
    $.getJSON(permissionUrl, function (result) {
        if (result.success) {
            if (true) {
                openLinkDialog({scope: conf.scope, width: 500, height: 400, isFull: false});
            } else {
                $.ligerDialog.warn("没有明细访问权限！", '提示信息');
            }
        } else {
            $.ligerDialog.error("getManagePermission请求出错！", '提示信息');
        }
    });

    function openLinkDialog(conf) {
        conf = conf || {};
        var keyName = conf.keyName;
        if (keyName) {
            try {
                var rtn = getPreScript(keyName);
                if (rtn != null && rtn == false) {
                    return;
                }
            } catch (e) {
                console.error(e);
                alert("前置脚本执行出错！");
            }
        }
        var contentWidth = window.top.document.documentElement.clientWidth,
            contentHeight = window.top.document.documentElement.clientHeight;
        conf.width > contentWidth ? conf.width = contentwidth : null;
        conf.height > contentHeight ? conf.height = contentHeight : null;
        var obj = conf.scope || this;
        width = conf.width || 1000,
            height = conf.height || 760,
            isFull = conf.isFull || false,
            isStart = conf.isStart || false,
            title = conf.title || '';
        if (isFull) {
            height = contentHeight;
            width = contentWidth;
        }
        if (title == '') {
            title = '提示';
        }
        if (isStart) {
            $.ligerDialog.warn("请先绑定流程！");
            return;
        }
        var url = $(obj).attr("action");
        var optClass = $(obj).attr("class");
        //  url=url.getNewUrl();
        var missionId = $.getParameter("missionId");
        if (missionId == "" || missionId == null) {
            missionId = $("input[name='m:bcsybgb:ssch']").val();
            if (missionId==null||missionId==""){
                missionId = $("#sschNumber").html();
                missionId=missionId.substring(1);
            }
        }
        url = __ctx + "/oa/form/dataTemplate/preview.do?__displayId__=10000022570015&__pk__=" + missionId
//            http://192.168.8.127:8080/dp10000029750040&rand=1599811914798
        DialogUtil.open({
            height: height,
            width: width,
            url: url,
            showMax: false,                             //是否显示最大化按钮
            showToggle: false,                         //窗口收缩折叠
            title: title,
            showMin: false,
            sucCall: function (rtn) {
                if (keyName) {
                    try {
                        getAfterScript(keyName);
                    } catch (e) {
                        console.error(e);
                        alert("后置脚本执行出错");
                    }
                }
                //rtn作为回调对象，可进行定制和扩展
                if (!(rtn == undefined || rtn == null || rtn == '')) {
                    //自动刷新有树的页面情况
                    //setTimeout(function(){
                    //	parent.isRefresh=true;
                    //	parent.reFresh();
                    //},0);
                    if (window.parent.document.getElementById("treeFresh")) {
                        window.parent.document.getElementById("treeFresh").click();
                    }
                    //刷新原来的当前的页面信息
                    locationPrarentPage();
                    //如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
                    location.href = location.href.getNewUrl();
                }
            }
        });
    }
}

//查看试验组员的页面的超链接
function displayTeam(conf) {

    if ($.isEmpty(conf && conf.type && conf.scope)) {
        return;
    }
    var me = $(conf.scope || this);
    var url = me.attr("action");
    var params = url.getArgs();
    var _height = window.top.document.documentElement.clientHeight;
    var displayId = params.__displayId__;
    var permissionUrl = __ctx + "/oa/form/dataTemplate/getManagePermission.do";
    permissionUrl += "?displayId=" + displayId;
    $.getJSON(permissionUrl, function (result) {
        if (result.success) {
            if (true) {
                openLinkDialog({scope: conf.scope, width: 500, height: 400, isFull: false});
            } else {
                $.ligerDialog.warn("没有明细访问权限！", '提示信息');
            }
        } else {
            $.ligerDialog.error("getManagePermission请求出错！", '提示信息');
        }
    });

    function openLinkDialog(conf) {
        conf = conf || {};
        var keyName = conf.keyName;
        if (keyName) {
            try {
                var rtn = getPreScript(keyName);
                if (rtn != null && rtn == false) {
                    return;
                }
            } catch (e) {
                console.error(e);
                alert("前置脚本执行出错！");
            }
        }
        var contentWidth = window.top.document.documentElement.clientWidth,
            contentHeight = window.top.document.documentElement.clientHeight;
        conf.width > contentWidth ? conf.width = contentwidth : null;
        conf.height > contentHeight ? conf.height = contentHeight : null;
        var obj = conf.scope || this;
        width = conf.width || 1000,
            height = conf.height || 760,
            isFull = conf.isFull || false,
            isStart = conf.isStart || false,
            title = conf.title || '';
        if (isFull) {
            height = contentHeight;
            width = contentWidth;
        }
        if (title == '') {
            title = '提示';
        }
        if (isStart) {
            $.ligerDialog.warn("请先绑定流程！");
            return;
        }
        var url = $(obj).attr("action");
        var optClass = $(obj).attr("class");
        //  url=url.getNewUrl();
        var missionId = $.getParameter("missionId");
        if (missionId==""||missionId==null){
            //先看看有没有input框
            missionId=$("input[name='m:bcsybgb:ssch']").val();
        }
        if (missionId == "" || missionId == null) {
            missionId = $("#sschNumber").html();
            missionId=missionId.substring(1);
        }
        url = __ctx + "/oa/form/dataTemplate/preview.do?__displayId__=10000022570015&__pk__=" + missionId
//            http://192.168.8.127:8080/dp10000029750040&rand=1599811914798
        //获取所有的displayId
        var teamInfoDisplayId;
        $.ajax({
            url: __ctx + "/oa/form/dataTemplate/getDisplayIdByFormAliases.do",
            data: {'allFormAliases': "zysjzsbd"},
            dataType: "json",
            async: false,
            success: function (result) {
                teamInfoDisplayId = result[0].zysjzsbd;
            },
            error: function () {
                console.log("ajax请求失败");
            }
        })
        var testTeamUrl = __ctx + "/oa/form/dataTemplate/preview.do?__displayId__=" + teamInfoDisplayId + "&__dbomSql__=F_SSBCCH=" + missionId;

        DialogUtil.open({
            height: 700,
            width: 1000,
            url: testTeamUrl,
            showMax: false,                             //是否显示最大化按钮
            showToggle: false,                         //窗口收缩折叠
            title: title,
            showMin: false,
            sucCall: function (rtn) {
                if (keyName) {
                    try {
                        getAfterScript(keyName);
                    } catch (e) {
                        console.error(e);
                        alert("后置脚本执行出错");
                    }
                }
                //rtn作为回调对象，可进行定制和扩展
                if (!(rtn == undefined || rtn == null || rtn == '')) {
                    //自动刷新有树的页面情况
                    //setTimeout(function(){
                    //	parent.isRefresh=true;
                    //	parent.reFresh();
                    //},0);
                    if (window.parent.document.getElementById("treeFresh")) {
                        window.parent.document.getElementById("treeFresh").click();
                    }
                    //刷新原来的当前的页面信息
                    locationPrarentPage();
                    //如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
                    location.href = location.href.getNewUrl();
                }
            }
        });
    }
}

// 是否有待办项
function showNeedToDo() {

    var wdcp = $("input:radio[name='m:bcsybgb:sfydb']:checked").val();
    if (wdcp == '1') {
        // 显示
        //  var needToDoManageHtml="<div type=\"reltable\" tablename=\"bcsjqrdbb\" id=\"needToDo\" right=\"w\" show=\"true\"><div class=\"relTableToolBar l-tab-links\"><a class=\"link add\" href=\"javascript:;\" onclick=\"return false;\" right=\"w\">添加</a></div><div formtype=\"edit\" class=\"block\" style=\"display: none;\"></div></div>"
        // $("table").after(needToDoManageHtml);
        $("#needToDo").removeAttr("style");
    } else {
        // 不显示
        // $("#needToDo").remove();
        $("#needToDo").attr("style", "display:none");
    }
}

function removeThisTable(e) {
    var delADom = $(e);
    var listTableDom = delADom.parents(".block");
    listTableDom.remove();
}

//提取待办结果的框显示在原来的位置并去掉隐藏
function changeInputToTextPlus() {
    //定位待办结果
    $(".needToDoRes").each(function () {
        //去掉隐藏效果
        $(this).removeAttr("style");
        //定位完成时间
        changeInputToText($(this).find("input[name='r:bcsjqrdbb:sjwcsj']"))
        changeInputToText($(this).find("input[name='r:bcsjqrdbb:bhqk']"))
    })
}
function changeInputToText(e) {
    var inputDom=$(e);
    var inputValue=inputDom.val();
    var inputDomParentsTd=inputDom.parents('td');
    inputDomParentsTd.empty();
    inputDomParentsTd.append(inputValue);
}