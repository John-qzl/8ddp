$(function () {
    //给含有任务结束的tr添加背景色
    $("tr").each(function () {
        //是否任务结束标识符
        var ifAlreadyDoneFlag=false;
        $(this).find('td').each(function () {
            var title=$(this).attr("title");
            if (title=="任务结束"){
                ifAlreadyDoneFlag=true;
            }
        })
        //如果当前tr有一个td存放的是任务结束
        //  则本tr变灰色
        if (ifAlreadyDoneFlag){
            $(this).attr("style","background-color:#EEEEEE");
        }else {
            //如果没有任务结束,则tr变白色
            $(this).attr("style","background-color:#f5f9ff");
        }
    })
});