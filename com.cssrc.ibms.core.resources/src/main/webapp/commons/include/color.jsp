<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="theme"%>
<theme:sysparam paramname="THEME_COLOR" alias="theme_color"></theme:sysparam>
<c:choose>
    <c:when test="${empty theme_color.theme_mainColor}">
        <c:set var="theme_mainColor" value="#3eaaf5" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_mainColor" value="${theme_color.theme_mainColor}" />
    </c:otherwise>
 </c:choose>
    <!-- 边栏 --><!--#3B485A-->
<c:choose>
    <c:when test="${empty theme_color.theme_sideColor}">
        <c:set var="theme_sideColor" value="#3B485A" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_sideColor" value="${theme_color.theme_sideColor}" />
    </c:otherwise>
</c:choose>
    <!-- 边栏背景 --><!--#293038-->
 <c:choose>
    <c:when test="${empty theme_color.theme_sideBackColor}">
        <c:set var="theme_sideBackColor" value="#ffffff" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_sideBackColor" value="${theme_color.theme_sideBackColor}" />
    </c:otherwise>
 </c:choose>
    <!-- form/table --><!--form/table-->
 <c:choose>
    <c:when test="${empty theme_color.theme_formColor}">
        <c:set var="theme_formColor" value="#3eaaf5" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_formColor" value="${theme_color.theme_formColor}" />
    </c:otherwise>
 </c:choose>
    <!-- form管理字体颜色 --><!--#0378ca-->
 <c:choose>
    <c:when test="${empty theme_color.theme_formManageColor}">
        <c:set var="theme_formManageColor" value="#0378ca" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_formManageColor" value="${theme_color.theme_formManageColor}" />
    </c:otherwise>
 </c:choose>
    <!-- form管理背景颜色 --><!--#297dbd -->
<c:choose>
    <c:when test="${empty theme_color.theme_formManageBackColor}">
        <c:set var="theme_formManageBackColor" value="#297dbd" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_formManageBackColor" value="${theme_color.theme_formManageBackColor}" />
    </c:otherwise>
 </c:choose>
    <!-- tab/btn --><!--#4092D0-->
<c:choose>
    <c:when test="${empty theme_color.theme_btnColor}">
        <c:set var="theme_btnColor" value="#4092D0" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_btnColor" value="${theme_color.theme_btnColor}" />
    </c:otherwise>
 </c:choose>
    <!-- btnBorder --><!--#48A1E4-->
<c:choose>
    <c:when test="${empty theme_color.theme_btnBorderColor}">
        <c:set var="theme_btnBorderColor" value="#48A1E4" /> 
    </c:when>
    <c:otherwise>
        <c:set var="theme_btnBorderColor" value="${theme_color.theme_btnBorderColor}" />
    </c:otherwise>   
</c:choose>

<c:choose>
    <c:when test="${empty theme_color.left_menu_bgcolor_other_hover}">
    	<c:choose>
    		<c:when test="${empty sys_layout}">
        		<c:set var="left_menu_bgcolor_other_hover" value="#878B8C" /> 
    		</c:when>
    		<c:when test="${sys_layout=='main_topMenu'}">
        		<c:set var="left_menu_bgcolor_other_hover" value="#E4F1FB" /> 
    		</c:when>
    		<c:otherwise>
        		<c:set var="left_menu_bgcolor_other_hover" value="#878B8C" /> 
    		</c:otherwise>
    	</c:choose>
    </c:when>
    <c:otherwise>
        <c:set var="left_menu_bgcolor_other_hover" value="${theme_color.left_menu_bgcolor_other_hover}" />
    </c:otherwise>
</c:choose>
<!--  
[{"name":"theme_mainColor","value":"#3eaaf5","description":"主题颜色"},{"name":"theme_sideColor","value":"#3B485A","description":"边栏"},{"name":"theme_sideBackColor","value":"#293038","description":"边栏背景"},{"name":"theme_formColor","value":"#C70019","description":"form/table"},{"name":"theme_formManageColor","value":"#0378ca","description":"form管理字体颜色"},{"name":"theme_formManageBackColor","value":"#297dbd","description":"form管理背景颜色"},{"name":"theme_btnColor","value":"#4092D0","description":"tab/btn"},{"name":"theme_btnBorderColor","value":"#48A1E4","description":"btnBorder"}]

[{"name":"theme_mainColor","value":"#C70019","description":"主题颜色"},{"name":"theme_sideColor","value":"#2E2B2B","description":"边栏"},{"name":"theme_sideBackColor","value":"#000000","description":"边栏背景"},{"name":"theme_formColor","value":"#C70019","description":"form/table"},{"name":"theme_formManageColor","value":"#E46474","description":"form管理字体颜色"},{"name":"theme_formManageBackColor","value":"#BB192E","description":"form管理背景颜色"},{"name":"theme_btnColor","value":"#A90A1E","description":"tab/btn"},{"name":"theme_btnBorderColor","value":"#C7182E","description":"btnBorder"}]

黑红个人推荐样式
#C70019；
#2E2B2B；
#000000；
#C70019；
#E46474；
#BB192E；
#A90A1E；
#C7182E；
-->





<style>
/*form管理字体颜色*/
a.ops_more {
    color: ${theme_formManageColor};
}
.tableNewHeader_con{
	color: ${theme_formManageColor};
}
th, .headRow th{
	color: ${theme_formManageColor};
}

/*form管理背景颜色*/
.ops_itemDiv li a:hover {
  background-color: ${theme_formManageBackColor};
}
.l-dialog-buttons {
	border-top: 1px solid ${theme_formManageBackColor};
}




/*主题颜色*/
.ops_btn.ops_active:before {
    color:${theme_mainColor};
}
.block-calendar-right{
	background: ${theme_mainColor};
}
.lbox-hd-h1 {
    color: ${theme_mainColor};
}
.lbox-hd-v {
    color: ${theme_mainColor};
}
.lbox-hd .down {
    color: ${theme_mainColor}
}
.lbox-hd .down a{
   color: ${theme_mainColor}
}
.btn {
    background: ${theme_mainColor};
}
.viewFramework-topbar {
	background: ${theme_mainColor};
}
.v-t-menu .base-tool-arrow b {
	border-top-color: ${theme_mainColor}
}
.block-calendar-right {
    background: ${theme_mainColor};
}
/*边栏*/
#layoutMain .l-layout-left .l-layout-header {
    background: ${theme_sideColor};
    border-bottom: 1px ${theme_sideBackColor} solid;
}
.l-layout-content>.l-tab-links{
	 background: ${theme_sideColor};
}
.sidemenu li.firstmenu {
    background-color: ${theme_sideColor};
}
/*边栏背景*/
.sidecolor {
	background: ${theme_sideBackColor}!important;
}
.sidemenu .secondmenu>li {
	background-color: ${theme_sideBackColor};
}
.sidemenu .fourmenu li {
    background-color: ${theme_sideBackColor};
}
/*form/table*/



/*tab/btn*/
.l-dialog-title {
	background-color: ${theme_btnColor};
}
.l-layout-header {
    background-color: ${theme_btnColor};
}
 #tab_bar li {
      color:${theme_btnColor};
 }
.l-layout-left .l-layout-header,.l-layout-right .l-layout-header{
    background:${theme_btnColor}
}
.l-layout-collapse-left-toggle, .l-layout-collapse-right-toggle {
    background-color: ${theme_btnColor};
}
.l-layout-collapse-left {
    background: ${theme_btnColor};
}
.l-menu {
	border: 1px solid ${theme_btnColor};
}
.l-tab-links li.l-selected {
    background: ${theme_btnColor};
}
td.formHead{
	color: ${theme_btnColor};
}
.active-li {
	background: ${theme_btnColor};
}
.ops_btn:before {
    color: ${theme_btnColor};
}
div.panel-body>span.iconfont:hover{
	color: ${theme_btnColor};
}
div.panel-body>span.iconfont.selected{
	color: ${theme_btnColor};
}
.user-defined-table>div.thead-title.checked {
	background: ${theme_btnColor};
}
ul.col-filter {
	background: ${theme_btnColor};
}
div.header + .content{
	border: 1px solid ${theme_btnColor};
}
th a.querySql-a{
	color: ${theme_btnColor};
}
a {
	color: ${theme_btnColor};
}

a.link {
	color: ${theme_btnColor};
}
a.activi {
	color: ${theme_btnColor};
}
a.tipinfo span {
    border: 1px solid ${theme_btnColor};
}
.table-top .table-top-left {
	color: ${theme_btnColor};
}
.panel-container {
	border-color: ${theme_btnColor};
}
.panel-container .l-tab-links {
	background: ${theme_btnColor};
}
.panel-container .l-tab-links .l-selected a {
    color: ${theme_btnColor};
}
.foldBox {
	border-top: 2px solid ${theme_btnColor};
}
.foldBox .title {
    color: ${theme_btnColor};
}
.foldBox .drop {
    color: ${theme_btnColor};
}
.searchShift span,.searchShift label{
	color: ${theme_btnColor};
}
.panel-toolbar a.link,#searchForm a.link {
    color: ${theme_btnColor};
    border: 1px solid ${theme_btnBorderColor};
}
.csb_btnOpe>p:first-child{
	color: ${theme_btnColor};
}
.panel-toolbar a.link:hover,#searchForm a.link:hover {
	background-color: ${theme_btnColor};
}
.panel-search .title {
    color: ${theme_btnColor};
}
.panel-complex-search .title{
	color: ${theme_btnColor};
}
.panel-search .title {
    color: ${theme_btnColor};
}
.sidemenu p:hover {
    background-color: ${theme_btnColor};
}
.mainColor{
    color: ${theme_btnColor};
}
.table-grid caption{
    color: ${theme_btnColor};
}
.table-grid th,
th.sorted a,
th.sortable a{
    background: ${theme_btnColor};
}
.tab-menu li:hover{
	color:${theme_btnColor};
}
.l-tab-links li:hover {
    background: ${theme_btnColor};
}

a.button {
	color: ${theme_btnColor};
}
a.button:hover {
	border-color: ${theme_btnColor};
}
.panel-search{
    border-top: solid 1px ${theme_btnBorderColor};
}














</style>
