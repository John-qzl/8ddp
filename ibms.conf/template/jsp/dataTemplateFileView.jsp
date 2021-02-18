<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>

<title>附件管理</title>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:gtype name="CAT_FILE" alias="CAT_FILE"></f:gtype>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalMenu.js"></script>

<script type="text/javascript">

  var objJson=${paramJson};
  var permission =$.parseJSON('${permission}');
  //获取文件分类树
  var catKey="${CAT_FILE}";
  var url="${ctx}/oa/system/globalType/getPersonType.do" ;
  var fileMenu=new FileMenu();
  var curMenu;
  var conf={url:url,onClick:treeClick,onRightClick:zTreeOnRightClick,expandByDepth:1,dataId:objJson.dataId,
      dimension:objJson.dimension,dimensionKey:objJson.dimensionKey};
  var curDimensionKey = conf.dimensionKey;
  if(objJson.nodekey!=null||objJson.nodekey!=undefined){
    var globalType=new GlobalType(objJson.nodekey,"glTypeTree",conf);
  }else{
    var globalType=new GlobalType(catKey,"glTypeTree",conf);
  }   
  $(function() {
    $("#defLayout").ligerLayout({
      leftWidth : 195,
      height : '100%',
      allowLeftResize :false,
      bottomHeight:40
    });
    //树初始化
    globalType.loadGlobalTree();
    
    //这边加参数判断是否显示文件夹树
    if(permission.filefolderview==0||$.isEmpty(permission.filefolderview)){
      $("#fileTreePanel").hide();
      $("#defLayout").ligerLayout().setLeftCollapse(true);
    }
    var url = "${ctx}/oa/form/dataTemplate/fileList.do?paramJson="+JSON.stringify(objJson);
    $("#fileFrame").attr("src", url);
    if($.isEmpty(objJson.dimension)||objJson.dimension!=1){
        $("#dimensionChange").hide();
      }
  });
  
  //展开收起
  function treeExpandAll(type) {
    globalType.treeExpandAll(type);
  };
  //树左击事件
  function treeClick(treeNode) {
    var isRoot=treeNode.isRoot;
    //isRoot为1说明是根节点，0为子节点
    if(isRoot==undefined||isRoot==""||isRoot==null){
      isRoot=0;
    }
    var typeId=treeNode.typeId;
    var url = "${ctx}/oa/form/dataTemplate/fileList.do?typeId=" + typeId +"&isRoot="+isRoot+"&paramJson="+JSON.stringify(objJson);
    $("#fileFrame").attr("src", url);
  }
  
  /**
   * 树右击事件
   */
  function zTreeOnRightClick(event, treeId, treeNode) {
    
    if (treeNode) {
      globalType.currentNode=treeNode;
      globalType.glTypeTree.selectNode(treeNode);
      curMenu=fileMenu.getMenu(treeNode, handler,permission);
      //查看菜单权限
      if(permission.filefolderview){
        curMenu.show({ top: event.pageY, left: event.pageX });
      }
      
    }
  };
 //加载维度树 
 function loadDimensionTree(dimensionKey){ 
  if(globalType&&globalType.conf){
     globalType.conf.dimensionKey = dimensionKey;
   } 
     objJson.dimensionKey = dimensionKey;
     var conf2={onClick:treeClick,onRightClick:zTreeOnRightClick,expandByDepth:1,dataId:objJson.dataId,
           dimension:objJson.dimension,dimensionKey:dimensionKey};
     conf2.url = "${ctx}/oa/system/globalType/getPersonType.do";
     var globalType2=new GlobalType(catKey,"glTypeTree",conf2);
     globalType2.loadGlobalTree();
 }
 //维度切换
 function changeDimension(){
     var url=__ctx  +"/oa/system/globalType/getDimensionInfo.do?";
     url += "dataId="+objJson.dataId;
     url += "&catKey="+catKey;
     url += "&curDimensionKey="+curDimensionKey;
     url=url.getNewUrl();
     DialogUtil.open({
      height:250,
      width: 300,
      title : '选择维度',
      url: url, 
      isResize: true,
      sucCall:function(rtn){
       if(rtn.dimensionKey){
         loadDimensionTree(rtn.dimensionKey);
         curDimensionKey = rtn.dimensionKey;
       }
      }
    });
  }
   function hiddenMenu(){
    if(curMenu){
      curMenu.hide();
    }
   }
     //权限按钮处理
     function handler(item){
       hiddenMenu();
       var key=item.key;
       switch(key){
         case "add":
           globalType.openGlobalTypeDlg(true,true);
           break;
         case "edit":
           globalType.openGlobalTypeDlg(false);
           break;
         case "del":
           //系统文件夹不可删除
          globalType.delFileNode(${userId});
           break;
         case "download":
           //系统文件夹不可删除
           globalType.download();
           break;
       }
     }
  //选取所有
   function selectAll(obj){
    var state = $(obj).attr("checked");
    if(state == undefined) {
      checkAll(false);    
    } else {    
      checkAll(true);
    }
  };
   
</script>
<style type="text/css">
.l-layout-right{left:528px;}
.l-layout-left, .l-layout-center, .l-layout-right { height:90%;}
.l-accordion-content { height:324px;}
.l-accordion-content .ztree { height:285px;}
.quick-find {width:35px;}  
</style>
</head>
<body>
  <div id="defLayout" style="height:100%;">
     <div id="fileTreePanel"position="left" title="附件分类" style="overflow: auto;float:left;width:100%;height:100%;">
        <div class="tree-toolbar">
          <span class="toolBar">
            <div class="group"><a class="link reload" id="treeFresh" href="javascript:void(0);" onclick="globalType.loadGlobalTree();return false;" title="刷新" ></a></div>
            
            <div class="group"><a class="link expand" id="treeExpandAll" href="javascript:void(0);" onclick="treeExpandAll(true);return false;" title="展开"></a></div>
            
            <div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:void(0);" onclick="treeExpandAll(false);return false;" title="收起" ></a></div>  
            
            <div class="group"><a class="link resetPwd" id="dimensionChange" href="javascript:void(0);" onclick="changeDimension()" title="维度切换" ></a></div>  
          </span>
        </div>
        <ul id="glTypeTree" class="ztree"></ul>
        </div>
    
    <div position="center" style="height:100%;">
    <div class="l-layout-header">附件列表</div>      
      <iframe id="fileFrame" name="fileFrame"  style="height:92%;"  width="100%" frameborder="0"></iframe>
    </div>
  </div>
</body>
</html>