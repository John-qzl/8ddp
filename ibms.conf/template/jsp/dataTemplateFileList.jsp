<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ImageQtip.js" ></script>
<f:sysFile name="GONGKAI" alias="GONGKAI"></f:sysFile>
<title>文件选择</title>

<script type="text/javascript">
	  var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	  $(function() {
	    var permissionObj = $.parseJSON('${permission}');
	    var buttonRightKeys = ["attachUpload","attachDownload","attachDel","attachDownloadall",
	                        "manageDelete","managePreview","manageUpdate","manageDownload"];
	    for(var i =0;i<buttonRightKeys.length;i++){
	      var key = buttonRightKeys[i];
	      var isNotDel = permissionObj[key.toLowerCase()]==1;
	      if(!isNotDel){
	        $('[var='+key+']').parents('li').remove();
	        $('[var='+key+']').remove();
	      }
	    }
	    var canDownload = permissionObj.attachdownload == 1;
	    if(!canDownload){
	      $('a#fileName').attr("href","####");
	    }
	    
	    var jsonStr=${paramJson};
	    $("#paramJson").val(JSON.stringify(jsonStr));
	    
	  });
	  
	  /**
	   * 下载文件(多个)
	   */
	  function downloadFiles(){
	    var $arrId = $("input[type='checkbox'][class='pk']:checked");
	    var len = $arrId.length;
	    if(len == 0){
	      $.ligerDialog.warn("请选择记录！");
	    }else{
	      //获取需要下载文件Id集合
	      var fileId = '';
	      $arrId.each(function(index){
	        var obj = $(this);
	        if(index < len-1){
	          fileId += obj.val() + ",";
	        }else{
	          fileId += obj.val();
	        }
	      });
	      window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId='+fileId;
	    }
	  };
	  
	  /**
	   * 下载文件(单个)
	   */
	  function downloadFile(){
	    var $arrId = $("input[type='checkbox'][class='pk']:checked");
	    var len = $arrId.length;
	    if(len!=1){
	      $.ligerDialog.warn("请选择一条记录下载！");
	    }else{
	      var fileId=$arrId[0].value;
	      AttachMent.downloadFiles(fileId);
	    }
	    
	  };
	  
	  /**
	   * 文件上传，窗口
	   */
	  function fileUpload(fileId,upStatus){
	    var url="${ctx}/oa/system/sysFile/upload.do";
	    var isGlType=1;
	    var dataId=${dataId};
	    var tableId=${tableId};
	    var typeId=${typeId};
	    var isRoot=${isRoot};
	     var dimension=${dimension};
	    if(${fileFolder==1}&&typeId==0&&upStatus!=1){
	      if(dimension==null||dimension==undefined||dimension!=1){        
	        $.ligerDialog.warn("请选择节点再上传");
	        return;
	       }
	    }
	    if(typeId && isRoot==0){
	      url+="?typeId="+typeId+'&isGlType='+isGlType+'&dataId='+dataId+'&tableId='+tableId;
	    }else{
	      url+="?isGlType="+isGlType+'&dataId='+dataId+'&tableId='+tableId;
	    }
	    if(fileId!=null&&fileId!=undefined){
	      url+='&fileId='+fileId;
	    }
	     if(dimension!=null&&dimension!=undefined){
	        url+='&dimension='+dimension;
	     }
	     if(upStatus!=null&&upStatus!=undefined){
	      url+='&upStatus='+upStatus;
	      }
	     url=url.getNewUrl();
	     //定义前置脚本和后置脚本
	     var obj={};
	     obj.prescript=function(){return true;};//上传附件前置脚本
	     obj.afterscript=function(){return true;};//上传附件后置脚本
	
	     DialogUtil.open({
	      height:300,
	      width: 800,
	      title : '附件上传',
	      url: url, 
	      isResize: true,
	      obj:obj,
	      //自定义参数
	      sucCall:function(rtn){
	        if(rtn) location.href=location.href.getNewUrl();
	      }
	    });
	  };
	  
	  
	  /**
	   * 预览文件
	   */
	  function previewFile(fileId){
	    
	    var callback = function(data){
	      var dataObj = new com.ibms.form.ResultMessage(data);
	      var filePath = dataObj.data.filePath;
	      var fileType = dataObj.data.fileType;
	      if(filePath == 'none'){
	        $.ligerDialog.warn("文件不存在！","提示信息");
	      }else{
	        var title = '在线预览';
	        var width = $(document).width()*.9;
	        var height = $(document).height()*.9;
	        var url = __ctx + "/oa/system/sysFile/preview.do?fileId="+fileId;
	        if('doc,docx,ppt,pptx,xls,xlsx'.indexOf(fileType) != '-1'){
	          url += "&url=oa/system/sysFileOfficePreview.jsp";
	          $.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
	        }else if('pdf'.indexOf(fileType) != '-1'){
	          url += "&url=oa/system/sysFilePDFPreview.jsp";
	          $.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
	        }else if('jpg,png,gif,bmp'.indexOf(fileType) != '-1'){
	          url += "&url=oa/system/sysFileImagePreview.jsp";
	          $.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});  
	        }else if('txt'.indexOf(fileType) != '-1'){
	          url += "&url=oa/system/sysFileTextPreview.jsp";
	          $.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
	        }else if('mp4,webm,ogv'.indexOf(fileType) != '-1'){
	          url += "&url=oa/system/sysFileVideoPreview.jsp";
	          $.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
	        }else{
	          $.ligerDialog.warn("暂不支持此类型的文件在线预览！","提示信息");
	        }
	      }
	    };
	    AttachMent.isExistFile(fileId, callback);
	  };
	  
	  function delAttach(conf){
	    //获取需要下载文件Id集合
	    var fileId = '';
	    if(conf!=null&&conf.fileId!=null){
	      var fileId = conf.fileId;
	    } else {
	      var $arrId = $("input[type='checkbox'][class='pk']:checked");
	      var len = $arrId.length;
	      if(len == 0){
	        $.ligerDialog.warn("请选择记录！");
	      }else{
	        $arrId.each(function(index){
	          var obj = $(this);
	          if(index < len-1){
	            fileId += obj.val() + ",";
	          }else{
	            fileId += obj.val();
	          }
	        });
	      }
	     }
	    $.ligerDialog.confirm("确认对附件进行删除吗？",'提示信息',function(rtn) {
	      if(rtn){
	        $.ajax({
	          url : __ctx + '/oa/system/sysFile/del.do?fileId='+fileId+'&isDialog=1',
	          type : "post",
	          async : false,
	          success : function(data){
	            location.href=location.href.getNewUrl();
	          }
	          ,error : function(){
	            $.ligerDialog.warn("删除文件失败！","提示信息");
	          } 
	        })
	      }
	    });
	     
	    }  
</script>
</head>
<body >
  <div class="panel">
    <div class="panel-top">
      <div class="tbar-title">
        <span class="tbar-label">文件选择列表</span>
      </div>
      <div class="panel-toolbar">
        <div class="toolBar">
          <div class="group"><a class="link search" >查询</a></div>
          <div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
          <div class="group" var="attachUpload"><a class="link upload" onclick="fileUpload()">上传</a></div>
          <div class="group" var="attachDownload"><a class="link download" href="####" onclick="javascript:downloadFile()">下载</a></div>
          <div class="group" var="attachDel"><a class="link del" name="delFile" onclick="delAttach()">删除</a></div>
          <div var="attachDownloadall" class="group"><a class="link download" href="####" onclick="javascript:downloadFiles()">打包下载所有附件</a></div>
        </div>  
      </div>
      <div class="panel-search">
        <form id="searchForm" method="get" action="fileList.do">
          <ul class="row">
            <li><span class="label">文件名:</span><input type="text" name="Q_fileName_SL"  class="inputText"  value="${param['Q_fileName_SL']}" /></li>
            <li><span class="label">上传者:</span><input type="text" name="Q_creator_SL"  class="inputText"  value="${param['Q_creator_SL']}"/></li>
            <li><span class="label">扩展名:</span><input type="text" name="Q_ext_SL"  class="inputText"   value="${param['Q_ext_SL']}"/></li>
            <input type="hidden" id="paramJson" name="paramJson" />            
            <div class="row_date">
              <li><span class="label" style="width:25%">创建时间 从:</span><input type="text"  name="Q_begincreatetime_DL"  class="inputText date" value="${param['Q_begincreatetime_DL']}"/></li>
              <li><span class="label">至: </span><input type="text" name="Q_endcreatetime_DG" class="inputText date" value="${param['Q_endcreatetime_DG']}"/></li>
            </div>
          </ul>
        </form>
      </div>
    </div>
    <div class="panel-body">
        <c:set var="checkAll">
          <input type="checkbox" id="chkall" />
        </c:set>
      <display:table name="sysFileList" id="sysFileItem" requestURI="FileList.do" sort="external" cellpadding="1"
        cellspacing="1" export="false" class="table-grid">
        <display:column title="${checkAll}" media="html" style="width:30px;">
          <input type="checkbox" class="pk" name="fileId" value="${sysFileItem.fileId}">  
          <textarea name="fileData" style="display: none;">{fileId:'${sysFileItem.fileId}',filename:'${sysFileItem.filename}.${sysFileItem.ext}'}</textarea>
        </display:column>
        <display:column title="文件名"  sortable="true" sortName="filename">
          <a id="fileName" href="${ctx}/oa/system/sysFile/download.do?fileId=${sysFileItem.fileId}" css="link download">${sysFileItem.filename}.${sysFileItem.ext}</a>
        </display:column>
        <c:if test="${isShowSecurity }">
          <display:column title="密级">
            <c:if test="${(sysFileItem.security == null)||(sysFileItem.security eq '')}">
                <span class="green">${GONGKAI}</span>
            </c:if>
            <c:forEach var="securityChineseMap" items="${securityChineseMap}">
              <c:if test="${sysFileItem.security eq securityChineseMap.key}">
                <span class="green">${securityChineseMap.value}</span>
              </c:if>
            </c:forEach>
          </display:column>
        </c:if>
        <display:column title="创建时间" sortable="true" sortName="createtime">
              <fmt:formatDate value="${sysFileItem.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </display:column>
        <display:column property="ext" title="扩展名" sortable="true" sortName="ext" />
        <display:column property="note" title="大小" sortable="true" sortName="note" maxLength="80"></display:column>
        <display:column title="版本" sortable="true" sortName="version">
        <a href="####" onclick="javascript:AttachMent.getAttachHistory(${sysFileItem.fileId})">${sysFileItem.version}</a>
        </display:column>    
        <display:column property="creator" title="上传者"  ></display:column>
        <display:column title="归档"  >
          <c:choose>
            <c:when test="${sysFileItem.filing>0}">
              <span class="green">已归档</span>
            </c:when>
            <c:otherwise>
              <span class="red">未归档</span>
            </c:otherwise>
          </c:choose>
        </display:column>  
        <display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
          <a alias="delFile" var="manageDelete" class="link del ops_more" name="delFile" href="####" onclick="javascript:delAttach({fileId:${sysFileItem.fileId}})" css="link del">删除</a>
          <a href="####" var="managePreview" onclick="javascript:previewFile(${sysFileItem.fileId})"  class="link preview">预览</a>
          <a href="####"  var="manageUpdate" onclick="javascript:fileUpload(${sysFileItem.fileId},1)" class="link update">更新附件版本</a>
                 <c:choose>
            <c:when test="${sysFile.delFlag eq 1}"><font color="red">已删除</font></c:when>
            </c:choose>
          <a var="manageDownload" href="${ctx}/oa/system/sysFile/download.do?fileId=${sysFileItem.fileId}"  class="link download">下载</a>
        </display:column>
      </display:table>
      <ibms:paging tableId="sysFileItem"/>
    </div>
  </div>
</body>
</html>
