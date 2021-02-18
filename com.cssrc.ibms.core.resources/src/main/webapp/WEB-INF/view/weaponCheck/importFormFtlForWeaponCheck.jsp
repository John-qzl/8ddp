<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
    <%@include file="/commons/include/form.jsp" %>
    <title>
        表单下发导入
    </title>
    <f:js pre="jslib/lang/view/oa/form" ></f:js>
    <script type="text/javascript">
        window.name="签章导入";
        var dialog = frameElement.dialog;
        $(function(){
            valid(showResponse);
            $("#btnSave").click(function(){
                var path = $('#file').val();
                var extName = path.substring(path.length-4, path.length);
                if(extName=='.zip'||extName=='.rar'){
                    $.ligerDialog.waitting("正在导入，请稍候...");
                    $("#importForm").submit();
                }else{
                    $.ligerDialog.warn("文件格式不符合要求，只支持zip或者rar文件");
                }
            });
        });

        function showResponse(responseText){
            $.ligerDialog.closeWaitting();
            var obj=new com.ibms.form.ResultMessage(responseText);
            if(obj.isSuccess()){//成功
                $.ligerDialog.success("导入成功!", function(){
                    dialog.get('sucCall')();
                    dialog.close();
                }, obj.getMessage());
            }else{//失败
                $.ligerDialog.err("错误提示","",obj.getMessage());
            }
        }

        function valid(showResponse){
            debugger;
            var options={success:showResponse,url:__ctx+'/dataPackage/tree/ptree/serverImportRangeTestPackages.do?'};
            __valid=$("#importForm").validate({
                rules: {},
                messages: {},
                submitHandler:function(form){
                    $(form).ajaxSubmit(options);
                },
                success: function(label) {}
            });
        }
    </script>
</head>
<body>
<div class="panel-top">
    <div class="tbar-title">
        <span class="tbar-label">表单下发导入</span>
    </div>
    <div class="panel-toolbar">
        <div class="toolBar">
            <div class="group"><a class="link import" id="btnSave">导入</a></div>

            <div class="group"><a class="link del" onclick="dialog.get('sucCall')();dialog.close()">取消</a></div>

        </div>
    </div>
</div>
<div class="panel-body">
    <form id="importForm"  name="importForm" method="post" target="win"  enctype="multipart/form-data">
        <div class="row">
            <input type="hidden" id="xhId" name="xhId">
            <input type="hidden" id="fcId" name="fcId">
            <table id="tableid" class="table-detail" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <th width="22%">选择文件：</th>
                    <td width="78%"><input type="file"  size="4000" name="file" id="file"/></td>
                </tr>
            </table>

        </div>
    </form>
</div>
<div class="panel-body">
    导入说明：<br/>
    <span name="client" style="display:none">
	 	导入的包将更新对应的表单及文件信息<br/>
	 </span>
</div>
<!-- end of panel-body -->
</body>
</html>