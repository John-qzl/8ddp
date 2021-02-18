//导入产品验收数据
function dataImportForCPYS(){
    var url = __ctx + '/io/serverDataImport.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "数据包导入",
        sucCall: function (rtn) {
            location.reload();
        }
    });
}

//导入武器及靶场数据
function dataImportForWQSJ(){
    var url = __ctx + '/io/rangeTestPlanDataImport.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "数据包导入",
        sucCall: function (rtn) {
            parent.location.reload();
        }
    });
}