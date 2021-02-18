<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>系统帮助管理</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/formTableTree.js"></script>
    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/FormTableDefMenu.js"></script>
    <style type="text/css">
        .tree-title {
            overflow: hidden;
            width: 100%;
        }

        .table-a table td {
            border: 1px solid #ff35d1;
            margin-left: 15px;
            text-align: center
        }

        html, body {
            padding: 0px;
            margin: 0;
            width: 100%;
            height: 100%;
            /*overflow: hidden;*/
        }
    </style>
    <script type="text/javascript">
        $(function () {
            //ligerui Tab
            layui.use('element', function () {
                var element = layui.element;
            })
        })
    </script>
</head>
<body>
<div id="defLayout">
    <!-- 初始化数据，用于页面高度计算，误删！！ -->
    <div position="center" >
        <div class="layui-tab layui-tab-brief template1">
            <ul class="layui-tab-title">
                <li class="layui-this">发射场场景</li>
                <li class="item2">外协外包场景</li>
                <%--<li class="item3">系统管理员</li>--%>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <h1><font face="verdana">帮助视频下载</font></h1>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/0系统登录及领域选择(对应人员 所有人员).exe" h>
                        <font face="arial">0系统登录及领域选择(对应人员 所有人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/1创建型号与发次(对应角色 项目办).exe" h>
                        <font face="arial">1创建型号与发次(对应角色 项目办).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/2策划数据包结构树(对应角色 项目办).exe" h>
                        <font face="arial">2策划数据包结构树(对应角色 项目办).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/3.1创建表单模板(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">3.1创建表单模板(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/3.2复制表单模板(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">3.2复制表单模板(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/4创建工作队(对应人员 非普通节点负责人).exe" h>
                        <font face="arial"> <font face="arial">4创建工作队(对应人员 非普通节点负责人).exe</font></a>
                    </font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/5创建表单实例(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">5创建表单实例(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <%--以上是策划阶段--%>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-发射场/6表单实例下载到pad(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">6表单实例下载到pad(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-发射场/7移动终端PAD采集.mp4" h>
                        <font face="arial">7移动终端PAD采集.mp4</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/最后：查询及对比分析（对应人员 所有人员）.exe" h>
                        <font face="arial">最后：查询及对比分析（对应人员 所有人员）.exe</font></a>
                    <br/>
                    <br/>
                    <%--文档下载--%>
                    <h1><font face="verdana">文档下载</font></h1>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/文档-发射场/1数据包系统--服务端使用手册.doc" h>
                        <font face="arial">1数据包系统--服务端使用手册.doc</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/文档-发射场/2数据包系统--PAD使用手册.doc" h>
                        <font face="arial">2数据包系统--PAD使用手册.doc</font></a>
                    <br/>
                </div>
                <div class="layui-tab-item">
                    <h1><font face="verdana">帮助视频下载</font></h1>
                    <br/>
                    <h2><font face="verdana" color="red">&nbsp;所内</font></h2>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/0系统登录及领域选择(对应人员 所有人员).exe" h>
                        <font face="arial">0系统登录及领域选择(对应人员 所有人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/1创建型号与发次(对应角色 项目办).exe" h>
                        <font face="arial">1创建型号与发次(对应角色 项目办).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/2策划数据包结构树(对应角色 项目办).exe" h>
                        <font face="arial">2策划数据包结构树(对应角色 项目办).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/3.1创建表单模板(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">3.1创建表单模板(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/3.2复制表单模板(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">3.2复制表单模板(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/4创建工作队(对应人员 非普通节点负责人).exe" h>
                        <font face="arial"> <font face="arial">4创建工作队(对应人员 非普通节点负责人).exe</font></a>
                    </font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/5创建表单实例(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">5创建表单实例(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <%--以上是策划阶段--%>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-发射场/6表单实例下载到pad(对应人员 非普通节点负责人和工作队人员).exe" h>
                        <font face="arial">6表单实例下载到pad(对应人员 非普通节点负责人和工作队人员).exe</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-通用/最后：查询及对比分析（对应人员 所有人员）.exe" h>
                        <font face="arial">最后：查询及对比分析（对应人员 所有人员）.exe</font></a>
                    <br/>
                    <br/>
                    <h2><font face="verdana" color="red">&nbsp;所外</font></h2>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-外协外包/0系统登录及领域选择(对应人员 所外人员).exe" h>
                        <font face="arial">0系统登录及领域选择(对应人员 所外人员).exe</font></a>
                    <br/>
                    <br/>
                     <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-外协外包/1策划数据导入(对应人员 所外人员).exe" h>
                        <font face="arial">1策划数据导入(对应人员 所外人员).exe</font></a>
                    <br/>
                    <br/>
                     <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-外协外包/2数据包系统--所外采集(对应人员 所外人员).exe" h>
                        <font face="arial">2数据包系统--所外采集(对应人员 所外人员).exe</font></a>
                    <br/>
                    <br/>
                     <a class="t-menu v-t-m-i-02" href="${ctx}/help/视频-外协外包/3数据采集完成后导出(对应人员 所外人员).exe" h>
                        <font face="arial">3数据采集完成后导出(对应人员 所外人员).exe</font></a>
                    <br/>
                    <br/>

                    <%--文档下载--%>
                    <h1><font face="verdana">文档下载</font></h1>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/文档-外协外包/1数据包系统--所内策划--使用手册.doc" h>
                        <font face="arial">1数据包系统--所内策划--使用手册.doc</font></a>
                    <br/>
                    <br/>
                    <a class="t-menu v-t-m-i-02" href="${ctx}/help/文档-外协外包/2数据包系统--所外采集--使用手册.doc" h>
                        <font face="arial">2数据包系统--所外采集--使用手册.doc</font></a>
                    <br/>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
