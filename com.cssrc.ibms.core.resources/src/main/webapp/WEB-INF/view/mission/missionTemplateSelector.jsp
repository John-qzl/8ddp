<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
    <title>任务模板选择器</title>
    <%@include file="/commons/include/form.jsp"%>
    <base target="_self" />
    <f:link href="Aqua/css/ligerui-all.css"></f:link>
    <%--<link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css"
          type="text/css" />--%>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <script type="text/javascript"
            src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
    <script type="text/javascript">
        /*KILLDIALOG*/
        var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)

        var xhId = '${xhId}';
        var isSingle = true;// 设置为单选
        // 树展开层数
        var expandDepth = 2;
        $(function() {
            debugger;
            loadTree();
            $("a.save").click(categorySingleSelect);
        });

        var check;
        if (isSingle) {
            check = {
                chkStyle : "radio",
                enable : true,
                radioType : "all"
            };
        } else {
            check = {
                chkboxType : {
                    "Y" : "ps",
                    "N" : "ps"
                },
                enable : true
            };
        }
        // 通用模板结构树
        var categoryTemplateTree;
        // 加载通用模板结构树
        function loadTree() {

            var setting = {
                data : {
                    key : {
                        name : "name",
                        title : "name"
                    },
                    simpleData : {
                        enable : true,
                        idKey : "id",
                        pIdKey : "parentId",
                        rootPId : "-1"
                    }
                },
                view : {
                    selectedMulti : false
                },
                check : check

            };
            //一次性加载

            var url = "${ctx}/template/manage/getMissionTemplate.do?xhId="+xhId;

            $.post(url, function(result) {
                if (result == "[]") {
                    $.ligerDialog.warn("没有通用模板,不能复制！");
                    dialog.close();
                } else {
                    categoryTemplateTree = $.fn.zTree.init($("#categoryTemplateTree"),
                        setting, eval(result));
                    var toolTree = $.fn.zTree.getZTreeObj("categoryTemplateTree");
                    var treeNodes = toolTree.transformToArray(toolTree.getNodes());
                    for (var i = 0; i < treeNodes.length; i++) {
                        var element = treeNodes[i];
                        if (element.type == 'root') {
                            element.icon = 'xiaohuojian';
                        } else if (element.type == 'folder') {
                            element.icon = 'file';
                        } else {
                            element.icon = 'yulanbiaodan';
                        }
                        toolTree.updateNode(element);
                    }
                    if (expandDepth != 0) {
                        categoryTemplateTree.expandAll(true);
                        var nodes = categoryTemplateTree.getNodesByFilter(function(
                            node) {
                            return (node.level < expandDepth);
                        });
                        if (nodes.length > 0) {
                            for (var i = 0; i < nodes.length; i++) {
                                categoryTemplateTree
                                    .expandNode(nodes[i], true, false);
                            }
                        }
                    } else
                        categoryTemplateTree.expandAll(true);
                }
            })
        };

        // 批量复制通用模板
        function categorySingleSelect() {
            var categoryTemplateTree = $.fn.zTree.getZTreeObj("categoryTemplateTree");
            var nodes = categoryTemplateTree.getCheckedNodes(true);
            var ids = "";
            var names = "";
            $.each(nodes, function(i, n) {
                if (n.type != "form") {
                    return true;
                }
                ids += n.id + ",";
                names += n.name + ",";
            });

            ids = ids.substring(0, ids.length - 1);
            names = names.substring(0, names.length - 1);
            var acceptancePlanId=$.getParameter("acceptancePlanId");
    		$.ajax({
    			  url: __ctx+"/dataPackage/tree/ptree/checkTempRepeat.do",
    			  async:false,
    			  data:{
    			    	acceptancePlanId:acceptancePlanId,
    			    	tempName:names,
    			    	tempId:ids
    			    },
    			    success:function(data){
    			    	if("1"==data){
    			    		$.ligerDialog.confirm('当前选择模板已下发，是否重复下发?','提示信息',function(rtn) {
    			    			if(rtn) {
    			    				if(dialog.options.sucCall){
    					    			dialog.options.sucCall({ids:ids,names:names});
    					    		}
    					    		dialog.close();
    			    			}
    			    		})
    					}
    			    	else{
    			    		if(dialog.options.sucCall){
    			    			dialog.options.sucCall({ids:ids,names:names});
    			    		}
    			    		dialog.close();
    			    	}
    			    }
    		});
        };
    </script>
    <f:link href="from-jsp.css"></f:link>
</head>
<body>
<div class="panel">
    <div class="panel-top">
        <div class="panel-toolbar">
            <div class="toolBar">
                <div class="group">
                    <a class="link save" id="btnConfirm">确定</a>
                </div>

                <div class="group">
                    <a class="link del" onclick="javasrcipt:dialog.close()">关闭</a>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="panel-detail">
            <div id="categoryTemplateTree" class="ztree"></div>
        </div>
    </div>
</div>
</body>
</html>

