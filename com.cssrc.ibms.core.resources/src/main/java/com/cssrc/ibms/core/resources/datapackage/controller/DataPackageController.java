package com.cssrc.ibms.core.resources.datapackage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateRequestService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.ISolrService;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.model.CheckDataPackageInfo;
import com.cssrc.ibms.core.resources.datapackage.model.CheckInstant;
import com.cssrc.ibms.core.resources.datapackage.model.CheckPackage;
import com.cssrc.ibms.core.resources.datapackage.model.CheckWorkTeam;
import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.datapackage.service.DataPackageService;
import com.cssrc.ibms.core.resources.datapackage.service.WorkTeamService;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.service.ClientExportService;
import com.cssrc.ibms.core.resources.io.service.ClientImportService;
import com.cssrc.ibms.core.resources.io.service.ServerExportService;
import com.cssrc.ibms.core.resources.io.service.ServerImportService;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.ioOld2New.service.ImportFromOldSystemService;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import com.fr.report.core.A.a;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/dataPackage/tree/ptree/")
public class DataPackageController extends BaseController {
	@Resource
	private DataPackageService dpService;
	@Resource
	private DataTemplateService dataTemplateService;
	@Resource
	private ClientExportService clientExportService;
	@Resource
	private ClientImportService clientImportService;
	@Resource
	private ServerExportService serverExportService;
	@Resource
	private ServerImportService serverImportService;	
	@Resource
	private WorkTeamService workTeamService;
	@Resource
	private ImportFromOldSystemService importFromOldSystemService;
	@Resource
	private ProjectService projectService;
	@Resource
	ISolrService solrService;
	@Resource
	private ProductService productService;
	@Resource
    private FormService formService;
	@Resource
	IOSignDefDao iOSignDefDao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private SysUserService SysUserService;
	@Resource
	private AcceptancePlanService AcceptancePlanService;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private WorkBoardDao workBoardDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;


	@RequestMapping({ "getTree" })
	public void getTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String projectId = RequestUtil.getString(request, "projectId");		
		String treeInfo = dpService.getPackageTree(projectId);
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(treeInfo);
	}
	
	@RequestMapping({ "delAllData" })
	public void delAllData(HttpServletRequest request, HttpServletResponse response) {
		dpService.delAllData();
	}
	
	@RequestMapping({ "getTreeData" })
	@Action(description = "数据包结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 数据包结构树
		List<String> dataPackageTree = new ArrayList<String>();

		String rootUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000560353\"";
		String dataPackageUrl = "\"/dataPackage/tree/dataPackage/main.do?\"";

		// 获取发次ID，型号id，节点名称
		String projectId = request.getParameter("id");
		String productId = request.getParameter("pid");
		String rootName = request.getParameter("nodeName");

		// 添加根节点
		Long rootId = 0L;
		Long rootParentId = -1L;
		String rootNode = "{id:" + rootId + ", parentId:" + rootParentId + ", Name:\"" + rootName
				+ "\" , Type: \"root\", ProductId :\"" + productId + "\",ProjectId : \"" + projectId + "\", tempUrl:"
				+ rootUrl + ", target : \"dataPackageFrame\",open:true}";
		dataPackageTree.add(rootNode);

		List<Map<String, Object>> dataPackageInfo = dpService.queryDataPackageInfo(projectId, productId);
		dataPackageTree = dpService.addTreeNode(dataPackageTree, dataPackageInfo, dataPackageUrl);

		// 利用Json插件将Array转换成Json格式
		response.getWriter().print(JSONArray.fromObject(dataPackageTree).toString());
	}
	
	@RequestMapping({ "getProductTree" })
	@Action(description = "产品结构维度树")
	public void getProductTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 获取型号id，节点名称
		String productId = request.getParameter("id");
		String rootName = java.net.URLDecoder.decode(request.getParameter("nodeName"), "UTF-8"); // 解码
		
		// 数据包结构树
		List<String> dataPackageTree = new ArrayList<String>();
		
		// 型号信息
		String rootUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000480036\"";
		String dataPackageUrl = "\"/dataPackage/tree/dataPackageShow/main.do?\"";

		// 添加根节点
		Long rootId = 0L;
		Long rootParentId = -1L;
		String rootNode = "{id:" + rootId + ", parentId:" + rootParentId + ", Name:\"" + rootName
				+ "\" , Type: \"root\" , tempUrl:"
				+ rootUrl + ", target : \"dataPackageFrame\",open:true}";
		dataPackageTree.add(rootNode);
		
		// 获取某型号下一级普通分类节点(PARENTID=0)
		List<Map<String, Object>> allNormalNode = dpService.queryPackageNormalNode(productId);
		// 有普通节点-并集-相同合并
		if (allNormalNode.size() > 0) {
			dataPackageTree = dpService.unionNormalNode(allNormalNode, dataPackageTree, dataPackageUrl, productId);
		}
				
		// 无普通节点
		List<Map<String, Object>> specNode = dpService.queryPackageSpecNode(productId);
		dataPackageTree = dpService.addTreeNode(dataPackageTree, specNode, dataPackageUrl);

		// 利用Json插件将Array转换成Json格式
		response.getWriter().print(JSONArray.fromObject(dataPackageTree).toString());
	}
	
	@RequestMapping({ "getProjectTree" })
	@Action(description = "发次维度树")
	public void getProjectTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 数据包结构树
		List<String> dataPackageTree = new ArrayList<String>();
		// 型号信息
		String rootUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000480036\"";
		String dataPackageUrl = "\"/dataPackage/tree/dataPackage/main.do?\"";

		// 获取型号id，节点名称
		String productId = request.getParameter("id");
		String rootName = java.net.URLDecoder.decode(request.getParameter("nodeName"), "UTF-8"); // 解码

		// 添加根节点
		Long rootId = 0L;
		Long rootParentId = -1L;
		String rootNode = "{id:" + rootId + ", parentId:" + rootParentId + ", Name:\"" + rootName
				+ "\" , Type: \"root\" , tempUrl:"
				+ rootUrl + ", target : \"dataPackageFrame\",open:true}";
		dataPackageTree.add(rootNode);
		
		// 获取某型号下所有发次
		List<Map<String, Object>> projectInfo = projectService.queryProjectNodeById(productId);
		if (projectInfo.size() > 0) {
			for (int i = 0; i < projectInfo.size(); i++) {
				String projectId = CommonTools.Obj2String(projectInfo.get(i).get("ID"));
				String parentId = "0";
				String projectName = CommonTools.Obj2String(projectInfo.get(i).get("F_FCMC"));
				String projectType = "fcType";
				String projectNode = "{id:" + projectId + ", parentId:" + parentId + ", Name:\"" + projectName
						+ "\" , Type: \"" + projectType + "\" , tempUrl:" + dataPackageUrl
						+ ", target : \"dataPackageFrame\",open:true}";
				dataPackageTree.add(projectNode);
				
				// 获取某发次下数据包节点
				List<Map<String, Object>> dataPackageInfo = dpService.queryDataPackageInfo(projectId, productId);
				if (dataPackageInfo.size() > 0) {
					for (int j = 0; j < dataPackageInfo.size(); j++) {
						String nodeId = CommonTools.Obj2String(dataPackageInfo.get(j).get("ID"));
						String nodePid = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_PARENTID"));
						if ("0".equals(nodePid)) {
							nodePid = projectId;
						}
						String nodeName = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_JDMC"));
						String nodeType = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_JDLX"));
						String nodeProductId = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_SSXH"));
						String nodeProjectId = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_SSFC"));
						//追加发次信息（非普通节点）
						if (!nodeType.equals(IOConstans.SIMPLE_PACKAGE_NODE)) {
							String nodeProjectName = dpService.getProjectNameById(nodeProjectId);
							if (nodeProjectName == "false") {
								continue;
							}
							nodeName += "," + nodeProjectName;
						}
						
						String currentNode = "{id:" + nodeId + ", parentId:" + nodePid + ", Name:\"" + nodeName
								+ "\" , Type: \"" + nodeType + "\" , ProductId :\"" + nodeProductId + "\",ProjectId : \""
								+ nodeProjectId + "\", tempUrl:" + dataPackageUrl
								+ ", target : \"dataPackageFrame\",open:true}";
						dataPackageTree.add(currentNode);
					}
				}
			}
		}
		// 利用Json插件将Array转换成Json格式
		response.getWriter().print(JSONArray.fromObject(dataPackageTree).toString());
	}
	
	@RequestMapping("editData")
	@Action(description = "编辑业务数据模板数据", detail = "编辑业务数据模板数据")
	public ModelAndView editData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Long displayId = RequestUtil.getLong(request, "__displayId__");
		String pk = RequestUtil.getString(request, "__pk__");
		// DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
		String dbomFKName = RequestUtil.getString(request, "__dbomFKName__");
		// DBom管理-dbom使用：子表新增表单时，存储外键字段值
		String dbomFKValue = RequestUtil.getString(request, "__dbomFKValue__");

		// xuyajun : 从型号发次树传递的型号id和发次id用于数据包结构树的过滤展示
		String productId = RequestUtil.getString(request, "productId");
		String projectId = RequestUtil.getString(request, "projectId");
		String UserId = RequestUtil.getString(request, "userId");
		String UserName = RequestUtil.getString(request, "userName");
		// 用于同层排序自动赋值
	//	String tcpx = RequestUtil.getString(request, "tcpx");

		// 返回参数
		Long tableId = 0L;
		Long formKey = 0L;
		String tableName = "";
		String pkField = "";
		FormDef bpmFormDef = null;
		ISysBusEvent sysBusEvent = null;
		String ctxPath = request.getContextPath();
		String returnUrl = RequestUtil.getPrePage(request);
		boolean hasPk = StringUtil.isNotEmpty(pk);
		String headHtml = "";

		// 判断是否有RPC远程过程调用服务
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		Map<String, Object> dataMap = new HashMap<String, Object>();

		Long userId = UserContextUtil.getCurrentUserId();
		if (StringUtil.isNotEmpty(rpcrefname)) {
			// 采用IOC方式，根据RPC远程过程调用服务调用数据
			CommonService commonService = (CommonService) AppUtil.getBean(rpcrefname);
			try {
				dataMap = commonService.editData(displayId, pk, UserContextUtil.getCurrentUserId(),
						request.getContextPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 当不是RPC远程过程调用服务 或者 远程过程调用超时失败，从本地调用
			dataMap = dataTemplateService.editData(displayId, pk, userId, ctxPath);
		}

		tableId = (Long) dataMap.get("tableId");
		pkField = (String) dataMap.get("pkField");
		formKey = (Long) dataMap.get("formKey");
		tableName = (String) dataMap.get("tableName");
		bpmFormDef = (FormDef) dataMap.get("bpmFormDef");
		sysBusEvent = (ISysBusEvent) dataMap.get("sysBusEvent");
		headHtml = (String) dataMap.get("headhtml");
		ModelAndView mv = IDataTemplateRequestService.getCustomEditData(request, getAutoView());
		return mv.addObject("bpmFormDef", bpmFormDef).addObject("id", pk).addObject("pkField", pkField)
				.addObject("tableId", tableId).addObject("tableName", tableName).addObject("returnUrl", returnUrl)
				.addObject("hasPk", hasPk).addObject("alias", formKey).addObject("sysBusEvent", sysBusEvent)
				.addObject("dbomFKName", dbomFKName).addObject("dbomFKValue", dbomFKValue)
				.addObject("displayId", displayId).addObject("isBackData", dataMap.get("isBackData"))
				.addObject("headHtml", headHtml).addObject("formKey", formKey).addObject("productId", productId)
				.addObject("projectId", projectId).addObject("userId", UserId).addObject("userName", UserName);
	}

	/**
	 * 明细数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detailData")
	@Action(description = "查看业务数据模板明细数据", detail = "查看业务数据模板明细数据")
	public ModelAndView detailData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long id = RequestUtil.getLong(request, "__displayId__");
		String pk = RequestUtil.getString(request, "__pk__");
		// DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
		String dbomFKName = RequestUtil.getString(request, "__dbomFKName__");
		// DBom管理-dbom使用：子表新增表单时，存储外键字段值
		String dbomFKValue = RequestUtil.getString(request, "__dbomFKValue__");

		// 判断是否有rpc远程接口 &rpcrefname="interfacesImplConsumerCommonService"
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);

		String contextPath = AppUtil.getContextPath();
		String form = "";
		String headhtml = "";
		Long curUserId = UserContextUtil.getCurrentUserId();
		Long formKey = null;
		if (StringUtil.isNotEmpty(rpcrefname)) {
			// 采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getBean(rpcrefname);
			// 获取业务数据表单详细
			form = commonService.getFormData(id, pk, curUserId, contextPath);
		} else {
			// 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(IFormHandlerService._displayId_, id);
			param.put(IFormHandlerService._userId_, curUserId);
			param.put(IFormHandlerService._businessKey_, pk);
			param.put(IFormHandlerService._contextPath_, contextPath);
			param.put(IFormHandlerService._highLight_, false);
			param.put(IFormHandlerService._isCopyFlow_, false);

			Map<String, Object> map = dataTemplateService.getForm(param);
			form = CommonTools.Obj2String(map.get("form"));
			headhtml = CommonTools.Obj2String(map.get("headhtml"));
			formKey = map.get("formKey") == null ? 0L : (Long) map.get("formKey");
		}

		return getAutoView().addObject("form", form).addObject("pk", pk).addObject("headHtml", headhtml)
				.addObject("dbomFKName", dbomFKName).addObject("dbomFKValue", dbomFKValue).addObject("ctx", contextPath)
				.addObject("formKey", formKey);
	}

	@RequestMapping({ "del" })
	@ResponseBody
	@Action(description = "删除数据包结构树节点")
	public void delData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		// 获取节点id
		String id = RequestUtil.getString(request, "id");

		try {
			//列表记录及其关联表记录
			
			int count = dpService.deleteData(id);
			if (count == -1) {
				message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail"));
			} else if(count == -2){
				message = new ResultMessage(ResultMessage.Fail, "所选节点为普通分类节点；节点下含有工作队信息或数据包信息，无法进行删除！");
			}else {
				message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
			}
		} catch (Exception e) {
			message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ":" + e.getMessage());
		}
		response.getWriter().print(message);
	}
	
	 /**
     * 删除数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteData")
    @Action(description = "删除业务数据",execOrder = ActionExecOrder.BEFORE, 
    detail = "删除业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,__pk__)}", 
    exectype = SysAuditExecType.DELETE_TYPE,
    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public void deleteData(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        Long id = RequestUtil.getLong(request, "__displayId__");
        String pk = RequestUtil.getString(request, "__pk__");
        boolean noDirect = RequestUtil.getBoolean(request, "noDirect", false);
     
        try
        {
            // 判断是否有RPC远程过程调用服务
            String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
            if (StringUtil.isNotEmpty(rpcrefname))
            {
                // 采用IOC方式，根据RPC远程过程调用服务调用数据
                CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
                commonService.deleteData(id, pk);
            }
            else
            {
            	//数据包删除，其实例录入信息删除
				//数据包详细信息表
				//W_DATAPACKAGEINFO ---> W_TB_INSTANT -----> W_CK_RESULT\W_CONDI_RES\W_SIGNRESULT签署结果
                dpService.deleteResult(pk);

                //删除型号关联信息
                dpService.deleteDataByXHId(pk);
                //删除发次关联
                //1.数据包节点
                //2.数据包节点关联的表单实例，检查结果等
                dpService.deleteDataByFCId(pk);
                // 删除列表记录
                dataTemplateService.deleteData(id, pk);
                // 删除该记录绑定的附件文件
                dataTemplateService.delFileOfData(pk);
                
            }
            //by songchen 根据id 删除索引
            solrService.deleteSqlDataIndex(pk);
            message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
        }
        catch (Exception ex)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ":" + ex.getMessage());
			ex.printStackTrace();
        }
        addMessage(message, request);
        if(!noDirect) {
        	response.sendRedirect(preUrl);
        }
    }
    
	@RequestMapping({ "formTemplateDialog" })
	@Action(description = "表单模板选择器")
	public ModelAndView formTemplateDialog(HttpServletRequest request, HttpServletResponse response) {
		String projectId = RequestUtil.getString(request, "projectId");
		Long isSingle = RequestUtil.getLong(request, "isSingle", 0);
		ModelAndView mv = new ModelAndView("/dp/formTemplateDialog.jsp");
		return mv.addObject("projectId", projectId).addObject("isSingle",
				isSingle);
	}
	
	/**
	 * Description : 得到当前用户
	 * Author : XYF
	 * Date : 2018年9月14日下午1:50:27
	 * Return : ModelAndView
	 */
	@RequestMapping({ "getUser" })
	@ResponseBody
	public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Long curUserId = UserContextUtil.getCurrentUserId();
		List<Map<String,Object>> User = dpService.selectUserById(curUserId);
		response.getWriter().print(JSONArray.fromObject(User).toString());
	}
	
	/**
	 * Description : 用于判断当前用户是否是该节点负责人
	 * Author : XYF
	 * Date : 2018年9月14日下午4:28:14
	 * Return : String
	 */
	@RequestMapping({ "checkUser" })
	@ResponseBody
	public String checkUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String userId = RequestUtil.getString(request, "userId");
		String result = "0";
		Long curUserId = UserContextUtil.getCurrentUserId();
		String UserId = String.valueOf(curUserId);
		if(userId.equals(UserId)){
			result ="1";
		}
		return result;
	}
	
	/**
	 * Description : 根据数据包节点ID查询该节点负责人ID
	 * Author : XYF
	 * Date : 2018年9月15日上午10:50:29
	 * Return : String
	 */
	@RequestMapping({ "getFzeIdById" })
	@ResponseBody
	public String getFzeIdById(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Long Id = RequestUtil.getLong(request, "packageId");
		String result ="";
		CheckPackage Package = dpService.selectById(Id);
		result = Package.getF_FZRID();
		return result;
	}

	/**
	 * @param buttonName
	 *            :要校验的按钮名称
	 * @param sssjb
	 *            ： 所属数据包
	 * @param ids
	 *            ： 要校验的记录主键（123,2342,...）
	 * @param canOperaterZt
	 *            ：可以操作的执行状态
	 */
	@RequestMapping({ "getButtonRight" })
	public void getButtonRight(HttpServletRequest request, HttpServletResponse response) {
		try {
			String buttonName = RequestUtil.getString(request, "buttonName");
			String ids = RequestUtil.getString(request, "ids");
			Long sssjb = RequestUtil.getLong(request, "sssjb");
			String[] canOperaterZt = RequestUtil.getStringAryByStr(request, "canOperaterZt");

			JSONObject rtn = dpService.getButtonRight(buttonName, sssjb, ids, canOperaterZt);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param buttonName
	 *            :要校验的按钮名称
	 * @param sssjb
	 *            ： 所属数据包
	 * @param ids
	 *            ： 要校验的记录主键（123,2342,...）
	 */
	@RequestMapping({ "getTeamButtonRight" })
	public void getTeamButtonRight(HttpServletRequest request, HttpServletResponse response) {
		try {
			String buttonName = RequestUtil.getString(request, "buttonName");
			String ids = RequestUtil.getString(request, "ids");
			Long sssjb = RequestUtil.getLong(request, "sssjb");

			JSONObject rtn = workTeamService.getButtonRight(buttonName, sssjb, ids);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(rtn.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导入数据包结构树节点信息 selectNode : 所要导入的节点, mbId : 所选导入模板的主键
	 * sourceFcId : 被复制的发次表单
	 */
	@RequestMapping({ "importDataPackageTreeInfo" })
	public void importDataPackageTreeInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			Long sourceId = RequestUtil.getLong(request, "sourceId");
			Long sourceFcId = RequestUtil.getLong(request, "sourceFcId");
			Long targetId = RequestUtil.getLong(request, "targetId");
			Long targetFcId = RequestUtil.getLong(request, "targetFcId");
			Long targetXhId = RequestUtil.getLong(request, "targetXhId");
			String targetFcName = RequestUtil.getString(request, "targetFcName") ;

			dpService.importTreeInfo(sourceId, sourceFcId, targetId, targetFcId, targetXhId, targetFcName);
			message = new ResultMessage(ResultMessage.Success, "true");
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	@RequestMapping({ "clientExportPackages" })
	public void clientExportPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			Long fcId = RequestUtil.getLong(request, "fcId");
			String nodeIds = RequestUtil.getString(request, "nodeIds");
			String filePath = clientExportService.exportPackage(fcId,nodeIds);
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	@RequestMapping({ "clientImportPackages" })
	public void clientImportPackages(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			String log = clientImportService.importPakage(file);
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", log);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}


/*	*//**
	 * @Author  shenguoliang
	 * @Description: 服务器导出(根据数据包节点的选择导出)
	 * @Params [request, response]
	 * @Date 2018/6/8 10:20
	 * @Return void
	 *//*
	@RequestMapping({ "serverExportPackages" })
	public void serverExportPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
			ResultMessage message = null;
		try {
			Long fcId = RequestUtil.getLong(request, "fcId");
			String nodeIds = RequestUtil.getString(request, "nodeIds");

			String filePath = serverExportService.exportPackage(fcId, nodeIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}*/
	
/*	@RequestMapping({ "serverExportPackages" }) fuyong
	public void serverExportPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
			ResultMessage message = null;
		try {
			Long fcId = RequestUtil.getLong(request, "fcId");
			String nodeIds = RequestUtil.getString(request, "nodeIds");

			String filePath = serverExportService.exportPackage(fcId, nodeIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}*/

	/**
	 * 产品验收的表单下发导出
	 * note by zmz 20200826
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "serverExportPackages" })
	public void serverExportPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
			ResultMessage message = null;
		try {

			String datapackIds = RequestUtil.getString(request, "datapackId");

			String filePath = serverExportService.exportPackagedata(datapackIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 靶场实验的表单下发导出
	 * note by zmz 20200826
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "serverExportRangeTestPackages" })
	public void serverExportRangeTestPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {

			String datapackIds = RequestUtil.getString(request, "datapackId");


			String filePath = serverExportService.exportRangeTestPackagedata(datapackIds);


			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * @Deprecated 武器所检和靶场下发都走的是同一模板:serverExportRangeTestPackages这个方法
	 * 武器所检的表单下发导出
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "serverExportWeaponCheckPackages" })
	public void serverExportWeaponCheckPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {

			String datapackIds = RequestUtil.getString(request, "datapackId");

			String filePath = serverExportService.exportWeaponCheckPackagedata(datapackIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 靶场试验下发的表单导入
	 * 把zip的文件和表信息同步到本地
	 * 曾经是挂在表单下发页面有个导入按钮的	20200907 改为 全部型号 - 型号列表 - 数据导入 的按钮事件 by zmz
	 * by zmz
	 * 20200821
	 * @param request
	 * @param response
	 */
	@RequestMapping({"serverImportRangeTestPackages"})
	public void serverImportRangeTestPackages(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultMessage message=null;
		MultipartFile multipartFile=request.getFile("file");
		message=serverImportService.importRangeTestPackagedata(multipartFile);
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 在pad回传签章分配签章页面 __displayId__=10000028880051 点击导出按钮
	 * 把签章的图片和PADHCQZB和SIGNMODEL表的信息打包
	 * 因为pad回传签章的时候,会自动分配一次签章到系统内已有用户
	 * 所以sign_model表也要同步
	 * @Date 20200820
	 * @author zmz
	 * @param request
	 * @param response
	 */
	@RequestMapping({"exportPADSignModelToZip"})
	public void exportPADSignModelToZip(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultMessage message = null;
		try {

			String signModelIds = RequestUtil.getString(request, "signModelIds");
			String filePath = serverExportService.exportPADSignModelToZip(signModelIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 在pad回传签章分配签章页面 __displayId__=10000028880051 点击导入按钮
	 * 把zip的文件和表信息同步到本地
	 * by zmz
	 * 20200821
	 * @param request
	 * @param response
	 */
	@RequestMapping({"importSignModelInfoFromZip"})
	public void importSignModelInfoFromZip(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		ResultMessage message=null;
		MultipartFile multipartFile=request.getFile("file");
		message=serverImportService.importSignModelInfoFromZip(multipartFile);
		writeResultMessage(response.getWriter(), message);
	}

	//靶场试验 - 全部任务 - 数据导出 - 导出按钮
	@RequestMapping({ "ExportRangeTestPackages" })
	public void ExportRangeTestPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {

			String missionId = RequestUtil.getString(request, "missionId");

			String filePath = serverExportService.ExportRangeTestPackages(missionId);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}



	//摇摆机导出(产品批次的数据导出 displayId__=10000025850008
	@RequestMapping({ "transferExportPackages" })
	public void transferExportPackages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {

			String acceptancePlanIds = RequestUtil.getString(request, "acceptancePlanIds");

			String filePath = serverExportService.exportTransferdata(acceptancePlanIds);

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	
	//摇摆机导入
	@RequestMapping({ "cornerServerImportPackages" })
	public void cornerServerImportPackages(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			message = serverImportService.cornerImportPakage(file);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 靶场试验 全部任务 数据导入 rangeTestPlanDataImport.jsp 数据导入接口
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "rangeTestPlanDataImport" })
	public void rangeTestPlanDataImport(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			message = serverImportService.rangeTestPlanDataImport(file);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	//服务器导入
	@RequestMapping({ "serverImportInstance" })
	public void serverImportInstance(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			message = serverImportService.serverImportInstance(file);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 中转机-->服务器通用导入方法(已废弃
	 * 会分流到各个领域
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "commonImportDataEntrance" })
	public void commonImportDataEntrance(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			message = serverImportService.commonImportDataEntrance(file);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}


	//用户信息导入
		@RequestMapping({ "userOrgImport" })
		public void userOrgImport(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
			ResultMessage message = null;
			try {
				MultipartFile file = request.getFile("file");
				message = serverImportService.userOrgImport(file);
			} catch (Exception e) {
				e.printStackTrace();
				message = new ResultMessage(ResultMessage.Fail, e.getMessage());
			}
			writeResultMessage(response.getWriter(), message);
		}
	/**
	 * @Author  shenguoliang
	 * @Description: 服务器导入(根据数据包节点的选择导入)
	 * @Params [request, response]
	 * @Date 2018/6/9 14:54
	 * @Return void
	 */
/*	@RequestMapping({ "serverImportPackages" })
	public void serverImportPackages(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			String fcId = request.getParameter("fcId") ;
			String xhId = request.getParameter("xhId") ;

			message = serverImportService.importPakage(file, fcId, xhId);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}*/
	
	//服务器导入
	@RequestMapping({ "serverImportPackages" })
	public void serverImportPackages(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			MultipartFile file = request.getFile("file");
			String fcId = request.getParameter("fcId") ;
			String xhId = request.getParameter("xhId") ;

			message = serverImportService.importPakage(file, fcId, xhId);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	/**
	 * 数据包系统迁移导入ByFZH
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "importPackagesFromOldSystem" })
	public void importPackagesFromOldSystem(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		String fcId = RequestUtil.getString(request, "fcId");
		try {
			MultipartFile file = request.getFile("file");
			String log = importFromOldSystemService.importPakage(file,fcId);
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", log);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}


	/**
	 * @Author  shenguoliang
	 * @Description: 根据发次Id获取发次名称
	 * @Params [request, response]
	 * @Date 2018/5/21 20:04
	 * @Return void
	 */
	@RequestMapping({ "getProjectNameById" })
	public void getProjectNameById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		String typeId = RequestUtil.getString(request, "typeId");

		try {
			String typeName = dpService.getProjectNameById(typeId);
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("typeName", typeName);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		}
		writeResultMessage(response.getWriter(), message);
	}
	
	//编辑模版
	@RequestMapping({ "eduitTemplate" })
	public void eduitTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			String Id = request.getParameter("Id") ;

			dpService.deleteTemplate(Id);
			message = new ResultMessage(ResultMessage.Success, "true");
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	//删除模版
	@RequestMapping({ "deleteTemplate" })
	public void deleteTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			String Id = request.getParameter("Id") ;

			dpService.deleteTemplate(Id);
			message = new ResultMessage(ResultMessage.Success, "true");
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	//数据包结构树节点上移
	@RequestMapping("setUp")
	@ResponseBody
	public void setUp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResultMessage message = null;
		try{
			String Id = request.getParameter("Id") ;
			//获取所属发次ID
			String fcId = request.getParameter("fcId");
			//获取前一个同层排序号
			String prId=dpService.selectParentIdById(Id);
			List<Map<String,Object>> list = dpService.selectNullTcpx(prId);;//定义一个list寻找tcpx值为空的记录
			
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap= list.get(i) ;
					String ID = resMap.get("ID").toString();
					String tcpx=dpService.selectTcpxByParentId(prId);
					dpService.updateTcpxById(ID,tcpx);
				}
			}
			String tcpx = dpService.selectTcpxById(Id);
			
			String upTcpx = dpService.selectUptcpxBytcpx(tcpx, prId, fcId);
			String upId = "";
			//根据同层排序获取ID
			List<Map<String,Object>> packageList = dpService.selectIdByTcpx(upTcpx, prId, fcId);

			if(packageList.size() > 0 && packageList != null){
				upId = CommonTools.Obj2String(packageList.get(0).get("ID"));
			}
			if(!"".equals(upId)){
				String t = upTcpx;
				dpService.updateTcpxById(upId,tcpx);
				dpService.updateTcpxById(Id,t);
				message = new ResultMessage(ResultMessage.Success, "true");

			}

		}catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, "已经到最顶层");
		}
		
		writeResultMessage(response.getWriter(), message);
	
	}
	
	
	//数据包结构树节点下移
	@RequestMapping("setDown")
	@ResponseBody
	public void setDown(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResultMessage message = null;
		try{
			String Id = request.getParameter("Id") ;
			//获取所属发次ID
			String fcId = request.getParameter("fcId");
			//获取前一个同层排序号
			String prId=dpService.selectParentIdById(Id);
            List<Map<String,Object>> list = dpService.selectNullTcpx(prId);;//定义一个list寻找tcpx值为空的记录
			
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap= list.get(i) ;
					String ID = resMap.get("ID").toString();
					String tcpx=dpService.selectTcpxByParentId(prId);
					dpService.updateTcpxById(ID,tcpx);
				}
			}
			String tcpx = dpService.selectTcpxById(Id);
			
			String downTcpx = dpService.selectDowntcpxBytcpx(tcpx, prId, fcId);
			String downId = "";
			//根据同层排序获取ID
			List<Map<String,Object>> packageList = dpService.selectIdByTcpx(downTcpx, prId, fcId);

			if(packageList.size() > 0 && packageList != null){
				downId = CommonTools.Obj2String(packageList.get(0).get("ID"));
			}
			if(!"".equals(downId)){
				String t = downTcpx;
				dpService.updateTcpxById(downId,tcpx);
				dpService.updateTcpxById(Id,t);
				message = new ResultMessage(ResultMessage.Success, "true");

			}
			
		}catch(Exception e){
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, "已经到最顶层");
		}
		writeResultMessage(response.getWriter(), message);
	
	
	}
	//数据包结构树节点移动（跨父节点移动）
	@RequestMapping({ "treeMove" })
	public void treeMove(HttpServletRequest request, HttpServletResponse response) throws Exception {
			ResultMessage message = null;
		try {
			String Ids = RequestUtil.getString(request, "Id");
			String nodeId = RequestUtil.getString(request, "toId");
			String fcId = RequestUtil.getString(request, "fcId");
			if("0".equals(nodeId)){
				String[] ids = Ids.split(",");
				
                for(int i=0;i<ids.length;i++){
                	String name=productService.selectNameByfcId(fcId);
    				String tcpx = dpService.selectTcpxByParentId(nodeId);
					String Id = ids[i];
					dpService.updateParentIdById(Id,nodeId);
					dpService.updateParentNameByParentId(nodeId,name);
					dpService.updateTcpxById(Id, tcpx);
				}
				
			}else{
//				String filePath = serverExportService.exportPackage2(Id, nodeId);
				String[] ids = Ids.split(",");
				for(int i=0;i<ids.length;i++){
					String name=dpService.selectNameById(nodeId);
		 			String tcpx = dpService.selectTcpxByParentId(nodeId);
		 			String Id = ids[i];
					dpService.updateParentIdById(Id,nodeId);
					dpService.updateParentNameByParentId(nodeId,name);
					dpService.updateTcpxById(Id, tcpx);
				}
				

				message = new ResultMessage(ResultMessage.Success, "true");
			//	message.addData("filePath", filePath);
			}

		
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	
	/**
	 * Description : 数据包结构树非普通节点复制功能
	 * Author : XYF
	 * Date : 2018年8月16日下午2:22:30
	 * Return : void
	 */
	@RequestMapping({ "copyNodes" })
	public void copyNodes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {
			String Ids = RequestUtil.getString(request, "Ids");
			String nodeId = RequestUtil.getString(request, "toId");
			String fcId = RequestUtil.getString(request, "fcId");
			String parentName = "";
			if("0".equals(nodeId)){
				List<Map<String,Object>> parent = dpService.selectProjectById(fcId);
				if(parent.size()>0){
					Object ParentName = parent.get(0).get("FCMC");
					parentName = String.valueOf(ParentName);
				}
				
			}else{ 
				parentName = dpService.selectNameById(nodeId);
			}
			String[] ids = Ids.split(",");
			String Packages="";
            for(int i=0;i<ids.length;i++){
            	String tcpx = dpService.selectTcpxByParentId(nodeId);
            	Long Tcpx = Long.parseLong(tcpx);
            	String Id = ids[i];
            	Long mid = Long.parseLong(Id);
            	CheckPackage checkPackage = dpService.selectById(mid);
            	Long mpk = UniqueIdUtil.genId();
            	String Mpk = Long.toString(mpk);
            	checkPackage.setID(mpk);
            	checkPackage.setF_PARENTID(nodeId);
            	checkPackage.setF_PARENTNAME(parentName);
            	checkPackage.setF_TCPX(Tcpx);
            	dpService.addPackage(checkPackage);
            	
            	//复制工作队
                List<CheckWorkTeam> checkWorkTeams = dpService.selectWorkTeamById(Id);
                for(int n=0;n<checkWorkTeams.size();n++){
                	CheckWorkTeam checkWorkTeam = checkWorkTeams.get(n);
                	Long teamId = UniqueIdUtil.genId();
                	checkWorkTeam.setID(teamId);
                	checkWorkTeam.setF_SSSJB(Mpk);
                	dpService.addWorkTeam(checkWorkTeam);
                }
            	
            	List<CheckDataPackageInfo> infos = dpService.selectDataPackageInfoById(Id);
                for (int j = 0; j < infos.size(); j++) {
                	CheckDataPackageInfo info = infos.get(j);
                	String ssmb = info.getF_SSMB();
                    Long Ssmb = Long.parseLong(ssmb);
                    Long infpk = UniqueIdUtil.genId();
                    Long inspk = UniqueIdUtil.genId();
                    String Inspk = Long.toString(inspk);
                    info.setID(infpk);
                    info.setF_SSSJB(Mpk);
                    info.setF_SSMB(Inspk);
                    dpService.addDataPackageInfo(info);
                    
                    //增加表单实例
                    CheckInstant checkInstant = dpService.selectInstantById(Ssmb);
                    checkInstant.setID(inspk);
                    dpService.addInstant(checkInstant);
                    
                    String formId = checkInstant.getF_TABLE_TEMP_ID();
                    List<Map<String, Object>> checkitems = formService
                            .getCheckItemById(formId);
                    String content = checkInstant.getF_CONTENT().toString();
                    dpService.createcheckresult(checkitems, inspk, content,Ssmb);//复制检查项结果并把content替换
                    
                    //复制检查条件结果
                    List<CkConditionResult> ckConditionResults = dpService.selectConditionResultById(ssmb);
                    for(int m=0;m<ckConditionResults.size();m++){
                    	CkConditionResult ckConditionResult = ckConditionResults.get(m);
                    	Long crId = UniqueIdUtil.genId();
                    	ckConditionResult.setID(crId);
                    	ckConditionResult.setInstantID(inspk);
                    	dpService.addConditionResult(ckConditionResult);
                    }
                     //复制签署结果
                    List<SignResult> signResults = dpService.selectSignResultById(ssmb);
                    for(int c=0;c<signResults.size();c++){
                    	SignResult signResult = signResults.get(c);
                    	Long srId = UniqueIdUtil.genId();
                    	signResult.setID(srId);
                    	signResult.setInstantID(inspk);
                    	dpService.addSignResult(signResult);
                    }
                    }
                }
            	
            	
            	/*Map<String, Object> resMap= packages.get(0) ;
            	for(Object value : resMap.values()){
            		Packages +=value+",";
            	}
            	String[] list = Packages.split(",");
            	Long mpk = UniqueIdUtil.genId();
            	String Mpk=Long.toString(mpk);
            	list[0]=Mpk;
            	String field = "";
            	for(int j=0;j<list.length;j++){
            		if(j==0){
            			field="('"+list[j]+"'";
            		}else if(j==list.length-1){
            			field+=",'"+list[j]+"')";
            		}else{
            			field+=",'"+list[j]+"'";
            		}
            	}
            	dpService.insertIntoPackage(field);*/
            
			
		}catch(Exception e){
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, "已经到最顶层");
		}
		
	}
	/**
	 * Description : 查找数据包结构树中一个节点的所有上级节点
	 * Author : XYF
	 * Date : 2018年8月15日下午4:32:49
	 * Return : String
	 */
	@RequestMapping({ "selectParentId" })
	@ResponseBody
	public List<String> selectParentId(HttpServletRequest request, HttpServletResponse response) {
		String Id = request.getParameter("Id") ;
		String parentIds = "";
		if("0".equals(Id)){
			parentIds = "-1";
		}else{
			parentIds = dpService.selectParentId(Id);
		}
		
		String[] ParentIds = parentIds.split(",");
		List<String> list = new ArrayList<String>();
		for(int j=0;j<ParentIds.length;j++){
			list.add(ParentIds[j]) ;
		}
		
		return list;
	}
	//新增节点同层排序自动赋值
		@RequestMapping("editTcpx")
		@ResponseBody
		public void editTcpx(HttpServletRequest request) {
			String Id = request.getParameter("Id") ;
			String prId = dpService.selectParentIdById(Id);
			
			String tcpx = dpService.selectTcpxByParentId(prId);
			dpService.updateTcpxById(Id,tcpx);
			
			
		}
		
		/**
		 * Description : 删除发次前判断该发次下是否存在数据包信息
		 * Author : XYF
		 * Date : 2018年9月8日下午3:53:17
		 * Return : void
		 */
		@RequestMapping("projectDelCheck")
		@ResponseBody
		public String projectDelCheck(HttpServletRequest request) {
			String Ids = request.getParameter("Ids") ;
			String[] list = Ids.split(",");
			String result = "0";
			for(int i=0;i<list.length;i++){
				String Id = list[i];
				List<Map<String,Object>> datapackage = dpService.selectPackageByFcId(Id);
				if(datapackage.size()>0){
					List<Map<String,Object>> project =  dpService.selectProjectById(Id);
					if(project.size()>0){
						Object Result=project.get(0).get("F_FCMC");
						result = String.valueOf(Result);
					}
					break;
				}
			}
			return result;
			
		}
		
		/**
		 * Description : 删除型号前判断该型号下是否存在发次信息
		 * Author : XYF
		 * Date : 2018年9月10日下午2:17:56
		 * Return : String
		 */
		@RequestMapping("productDelCheck")
		@ResponseBody
		public String productDelCheck(HttpServletRequest request) {
			String Ids = request.getParameter("Ids") ;
			String[] list = Ids.split(",");
			String result = "0";
			for(int i=0;i<list.length;i++){
				String Id = list[i];
				List<Map<String,Object>> project = dpService.selectprojectByXhId(Id);
				if(project.size()>0){
					List<Map<String,Object>> product =  dpService.selectProductById(Id);
					if(product.size()>0){
						Object Result=product.get(0).get("F_XHMC");
						result = String.valueOf(Result);
					}
					break;
				}
			}
			return result;
			
		}


	/**
	 * @Author  shenguoliang
	 * @Description: 修改当前发次节点下的节点的父节点名称
	 * @Params [request, response]
	 * @Date 2018/5/21 20:04
	 * @Return void
	 */
	@RequestMapping({ "doChangeParentTypeName" })
	public void doChangeParentTypeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		String typeId = RequestUtil.getString(request, "typeId");

		try {
			String typeName = dpService.getProjectNameById(typeId);

			dpService.doChangeParentTypeName(typeId,typeName) ;

			message = new ResultMessage(ResultMessage.Success, "true");
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		}
		writeResultMessage(response.getWriter(), message);
	}
	/**
	 * @Author  fuyong
	 * @Description: 表单实例在被创建的时候如果是产品报告就把策划中的小组成员带过来
	 * @Params [request, response]
	 * @Return void
	 */
	@RequestMapping({ "acceptanceAddSign" })
	public void acceptanceAddSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String Id=RequestUtil.getString(request, "ID");
		String templateName=RequestUtil.getString(request,"templateName");
		String acceptancePlanId=RequestUtil.getString(request, "acceptancePlanId");
		workBoardDao.updatework(acceptancePlanId,"表单已下发","PAD数据采集");
		if(templateName.indexOf("验收报告")>=0) {
			List<Map<String,Object>> list=acceptanceGroupDao.getDataByAcceptancePanId(acceptancePlanId);
			iOSignDefDao.deleteByTempId(Id);
			if(list!=null&&list.size()!=0) {
				int i=1;
				for (Map<String, Object> map : list) {
					SignDef signDef=new SignDef();
					String signDefId=String.valueOf(UniqueIdUtil.genId());
					signDef.setId(signDefId);
					signDef.setTable_temp_id(Id);
					String str=map.get("F_ZW").toString();
					if(str.equals("承制方")) {
						signDef.setName(map.get("F_ZW").toString());
					}
					else {
						if(map.get("F_XM")==null) {
							signDef.setName(map.get("F_ZW").toString()+"("+" "+")");
						}
						else {
							signDef.setName(map.get("F_ZW").toString()+"("+map.get("F_XM").toString()+")");
						}
						
					}
					signDef.setOrder(String.valueOf(i));
					iOSignDefDao.insert(signDef);
					i++;
				}
				SignDef signDef=new SignDef();
				String signDefId=String.valueOf(UniqueIdUtil.genId());
				signDef.setId(signDefId);
				signDef.setTable_temp_id(Id);
				signDef.setName("承制方");
				signDef.setOrder(String.valueOf(i+1));
				iOSignDefDao.insert(signDef);
				
				//付勇定制，下发验收报告表单添加两个空出来的签署
				signDef=new SignDef();
				signDefId=String.valueOf(UniqueIdUtil.genId());
				signDef.setId(signDefId);
				signDef.setTable_temp_id(Id);
				signDef.setName("组员(签署人员1)");
				signDef.setOrder(String.valueOf(i+1));
				iOSignDefDao.insert(signDef);
				signDef.setId(String.valueOf(UniqueIdUtil.genId()));
				signDef.setName("组员(签署人员2)");
				signDef.setOrder(String.valueOf(i+1));
				iOSignDefDao.insert(signDef);
			}
		}
		
	}
	@RequestMapping("getOrgByUserId")
	@ResponseBody
	public SysOrg getOrgByUserId(HttpServletRequest request, HttpServletResponse response) {
		String userId=RequestUtil.getString(request, "userId");
		if(userId!=null&&!"".equals(userId)) {
			List<SysOrg> list=sysOrgService.getOrgsByUserId(Long.valueOf(userId));
			if(list.size()!=0) {
				return list.get(0);
			}
		}
		return null;
	}
	//批次下发按钮权限控制
	@RequestMapping("GetUserButtonRole")
	@ResponseBody
	public Boolean GetUserButtonRole(HttpServletRequest request, HttpServletResponse response) {
		ISysUser user=UserContextUtil.getCurrentUser();
		SysUser sysUser=SysUserService.getById(user.getUserId());
		Boolean check=false;
		List<SysRole> sysRoleList=sysUser.getUserRoleAuthorities();  //获取当前登陆人角色信息
		for (SysRole sysRole : sysRoleList) {
			if("sjgly".equals(sysRole.getAlias())){  //如果所级管理员
				check=true;
				return check;
			}
		}
		String acceptancePlanId=RequestUtil.getString(request, "acceptancePlanId");
		Map<String,Object> acceptanceMap=AcceptancePlanService.getPlansById(acceptancePlanId);
		String groupLeader=(String) acceptanceMap.get("F_YSZZID"); 
		if(groupLeader.equals(String.valueOf(sysUser.getUserId()))) {  //获取策划的验收组长和当前登陆人进行判断
			check=true;
		}
		return check;	
	}
	//判断当前是否已下发验收报告
	@RequestMapping("checkReport")
	@ResponseBody
	public JSONObject checkReport(HttpServletRequest request, HttpServletResponse response) {
		Boolean check=false;
		JSONObject jsonObject=new JSONObject();
		String ids=RequestUtil.getString(request,"ids");
		String acceptancePlanId=RequestUtil.getString(request,"acceptancePlanId");
		List<Map<String,Object>> dataPackageList=dataPackageDao.getByIds(ids);
		
		for (Map<String, Object> map : dataPackageList) {
			if(map.get("F_SJMC").toString().indexOf("报告")>=0) {
				check=true;
			}
		}
		if(!check) {
			List<DataPackage> dataPackages=dataPackageDao.getByPlanId(acceptancePlanId);
			if(dataPackages!=null) {
				for (DataPackage dataPackage : dataPackages) {
					if(dataPackage.getSjmc().indexOf("报告")>=0&&!dataPackage.getZxzt().equals("未开始")) {
						check=true;
					}
				}
			}
		}
		if(check) {
			List<Map<String, Object>> list=new ArrayList<>();
			 list=acceptanceReportDao.getAcceptanceReport(acceptancePlanId);
			if(list.size()!=0&&CommonTools.Obj2String(list.get(0).get("F_SPZT")).equals("审批通过")) {
				check=false;
			}
		}
		jsonObject.put("check", check);
		jsonObject.put("ids", ids);
		return jsonObject;
		
	}
	@RequestMapping({ "ExuserInfo" })
	public void ExuserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		try {


			String filePath = serverExportService.ExuserInfo();

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	//判断当前表单是否下发重复
		@RequestMapping("checkTempRepeat")
		@ResponseBody
		public String checkTempRepeat(HttpServletRequest request, HttpServletResponse response) {
			String acceptancePlanId=request.getParameter("acceptancePlanId");
			String tempName=request.getParameter("tempName");
			String tempId=request.getParameter("tempId");
			List<DataPackage> list=dataPackageDao.getByPlanId(acceptancePlanId);
			for (DataPackage dataPackage : list) {
				if(dataPackage.getTemplateId().equals(tempId)) {
					return "1";
				}
				if(tempName.indexOf("验收报告")>=0) {
					if(dataPackage.getTemplateName().indexOf("验收报告")>=0) {
						return "1";
					}
				}
			}
			return "0";
		}
}

