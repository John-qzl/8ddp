/**
 * 靶场试验总结表单的自动填充js
 */

$(function(){
    //预置属性值
    var type=$.getParameter("type");
    // 如果当前是新增页面或者流程启动页面就去 找对应策划的数据回填
    if(type=="add"||type=="startAdd"){
        /**
         * 组员已经知道要复用产品验收的表了
         * 但是明天要给用户验收,到今天下午组员的字段还没有确定下来
         * 所以暂时还是用靶场的组员
         * 靶场这边只要组员,不要组员签署
         * 后端返回的信息里也没有组员的签署信息
         * by zmz 20200912
         */

        //取组员信息并填充到表单里
        $.ajax({
            url: __ctx+"/rangeTest/mission/plan/getRangeTestPlanGroup.do?missionId="+missionId,
            async:false,
            //这是是手动生成组员信息
            success:function(data){
                for(var i=0;i<data.length;i++){
                    var detailedData=data[i];
                    html+='<tr class="listRow" formtype="edit"> ';
                    html+='<td tname="r:r:cpyszb:zw"><span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
                    html+='<input name="r:cpyszb:zw"  lablename="职务" controltype="select" validate="{empty:false}" readonly=""   value='+'"'+detailedData.F_ZW+'"'+'  class="inputText">';
                    html+='</span></td>';
                    html+='<td tname="r:r:cpyszb:xm">';
                    html+='<input name="r:cpyszb:xmID" type="hidden" lablename="姓名ID" class="hidden" value='+'"'+detailedData.F_XMID+'"'+'>';
                    html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
                    html+='<input name="r:cpyszb:xm"  lablename="姓名" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_XM+'"'+'  class="inputText  ">';
                    html+='</span></td>';

                    html+='<td tname="r:r:cpyszb:dw">';
                    html+='<input name="r:cpyszb:dwID" type="hidden" lablename="单位ID" class="hidden" value='+'"'+detailedData.F_DWID+'"'+'>';
                    html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
                    html+='<input name="r:cpyszb:dw"  lablename="单位" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_DW+'"'+'  class="inputText">';
                    html+='</span></td>';

                    html+='<td tname="r:r:cpyszb:fzxm">';
                    html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
                    html+='<input name="r:cpyszb:fzxm"  lablename="负责项目" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_FZXM+'"'+'  class="inputText">';
                    html+='</span></td>';
                    html+='<input type="hidden" name="r:cpyszb:id" value="">';
                    html+='</tr>';
                }
                $("#context").append(html);
            }
        });

    }
})

//实验数据页面的超链接
function displayType(conf){
    if($.isEmpty(conf&&conf.type&&conf.scope)){
        return;
    }
    var me = $(conf.scope||this);
    var url = me.attr("action");
    var params = url.getArgs();
    var _height=window.top.document.documentElement.clientHeight;
    var displayId = params.__displayId__;
    var permissionUrl = __ctx+"/oa/form/dataTemplate/getManagePermission.do";
    permissionUrl+="?displayId="+displayId;
    $.getJSON(permissionUrl,function(result){
        if(result.success){
            if(true){
                openLinkDialog({scope:conf.scope,width:500,height:400,isFull:false});
            }else{
                $.ligerDialog.warn("没有明细访问权限！",'提示信息');
            }
        }else{
            $.ligerDialog.error("getManagePermission请求出错！",'提示信息');
        }
    });
    function openLinkDialog(conf){
        conf = conf || {};
        var keyName = conf.keyName;
        if(keyName){
            try{
                var rtn = getPreScript(keyName);
                if(rtn!=null&&rtn==false){
                    return;
                }
            }catch(e){
                console.error(e);
                alert("前置脚本执行出错！");
            }
        }
        var	contentWidth = window.top.document.documentElement.clientWidth,
            contentHeight = window.top.document.documentElement.clientHeight;
        conf.width>contentWidth?conf.width = contentwidth:null;
        conf.height>contentHeight?conf.height = contentHeight:null;
        var obj =conf.scope||this;
        width= conf.width||1000,
            height=conf.height||760,
            isFull =conf.isFull||false,
            isStart = conf.isStart||false,
            title = conf.title||'';
        if(isFull){
            height=contentHeight;
            width=contentWidth;
        }
        if(title == ''){
            title = '提示';
        }
        if(isStart){
            $.ligerDialog.warn("请先绑定流程！");
            return;
        }
        var url=$(obj).attr("action");
        var optClass=$(obj).attr("class");
        //  url=url.getNewUrl();
        var missionId=$.getParameter("missionId");
        if (missionId==""||missionId==null){
            missionId=$("input[name='m:bcsybgb:ssch']").val();
        }
        url=__ctx+"/oa/form/dataTemplate/preview.do?__displayId__=10000022570015&__pk__="+missionId
//            http://192.168.8.127:8080/dp10000029750040&rand=1599811914798
        DialogUtil.open({
            height:height,
            width: width,
            url: url,
            showMax: false,                             //是否显示最大化按钮
            showToggle: false,                         //窗口收缩折叠
            title: title,
            showMin: false,
            sucCall:function(rtn){
                if(keyName){
                    try{
                        getAfterScript(keyName);
                    }catch(e){
                        console.error(e);
                        alert("后置脚本执行出错");
                    }
                }
                //rtn作为回调对象，可进行定制和扩展
                if(!(rtn == undefined || rtn == null || rtn == '')){
                    //自动刷新有树的页面情况
                    //setTimeout(function(){
                    //	parent.isRefresh=true;
                    //	parent.reFresh();
                    //},0);
                    if(window.parent.document.getElementById("treeFresh")) {
                        window.parent.document.getElementById("treeFresh").click();
                    }
                    //刷新原来的当前的页面信息
                    locationPrarentPage();
                    //如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
                    location.href=location.href.getNewUrl();
                }
            }
        });
    }
}