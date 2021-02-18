/**
 * Author :  sgl.
 * Date : 2019/1/17.
 * Time : 17:22.
 * Description :批量导出表单实例Excel数据
 */
function batchExportExcel(){
    var selectRecords = getSelectRecordsCompleted(true,false);
    if(selectRecords === false){
        return ;
    }else{
        var url = __ctx + "/dp/form/batchExportExcel.do?recordsArr="+JSON.stringify(selectRecords);
        //后台传回前台的是response中写入文件流的形式，所以这里导出用 window.location.href的方式下载文件，ajax下载的方式稍微复杂一些。
        window.location.href = url;
    }
}

/**
 * @Description: 批量选择已完成的表单实例导出
 * @Author: shenguoliang
 * @Date: 2019/1/17 18:09
 * @methodName: getSelectRecordsCompleted
 * @Line: 21
 */
function getSelectRecordsCompleted(isAlert,isSingle){
    var idArr = [];
    var message = "";
    $('input.pk:checked').each(function(){
        var recordTr = $(this).parents('tr');
        var lineNumber = $(recordTr).children('td.number').text().trim();//表单实例所在行号
        var formStatus = $($(recordTr).children('td')[10]).attr('title');//表单实例执行状态
        //var security = $($(recordTr).children('td')[7]).text().trim();//密级
        var recordObj = {};
        if("已完成"== formStatus || "已完成待下载" ==formStatus){
            recordObj['id'] = $(this).val();
            //recordObj['security'] = security;
            idArr.push(recordObj);
        }else{
            message += lineNumber+","
        }
    });
    if("" != message){
        $.ligerDialog.warn("第【"+message.substr(0,message.length-1)+"】行的表单实例执行状态不是【已完成】或者【已完成待下载】,无法导出数据");
        return false;
    }
    if(isAlert){
        if(idArr.length==0){
            $.ligerDialog.warn("请选择数据！");
            return false;
        }
        if(idArr.length!=1&&isSingle){
            $.ligerDialog.warn("请选择一条数据！");
            return false;
        }
        return idArr;
    }else{
        return idArr;
    }
}