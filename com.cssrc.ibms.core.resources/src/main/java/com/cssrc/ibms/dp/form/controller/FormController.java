package com.cssrc.ibms.dp.form.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.dp.template.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.form.dao.DataPackageInfoDao;
import com.cssrc.ibms.dp.form.service.CheckFormService;
import com.cssrc.ibms.dp.form.service.DataPackageInfoService;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.form.service.FormValidateService;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SolrService;
import com.cssrc.ibms.system.service.SysFileService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;

/**
 * 表单处理
 *
 * @author sc
 * @date 2017年11月16日上午9:42:23
 */
@Controller
@RequestMapping("/dp/form/")
public class FormController extends BaseController {
    @Resource
    private FormService formService;
    @Resource
    private CheckFormService checkFormService;
    @Resource
    private FormValidateService formValidateService;
    @Resource
	private ProductService productService;	
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private SolrService solrService;
    @Resource
    private ProjectService projectService;
    @Resource
    private DataPackageInfoService dataPackageInfoService;
    @Resource
    private DataPackageInfoDao dataPackageInfoDao;
    @Resource
    private ProductCategoryBatchDao productCategoryBatchDao;
    @Resource
    private IOTableTempDao ioTableTempDao;
    @Resource
    private TemplateService templateService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleManageService moduleManageService;

    private String saveType = PropertyUtil.getSaveType();
    // 附件保存路径
    private String attachPath = AppUtil.getAttachPath();

    /**
     * 表单模板页到预览页前，进行html校验
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("validateFormTemplate")
    //@ResponseBody
    public ModelAndView validateFormTemplate(HttpServletRequest request,
                                             HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        String html = RequestUtil.getString(request, "content");
        String type = RequestUtil.getString(request, "type");
        String MID = RequestUtil.getString(request, "MID");
        // Map<String,
        // Object>msg=formService.check(html,id,name,sign,status,attention,pid,fcid,MID);
        Map<String, Object> msg = formValidateService.validate(html, type, MID);
        // System.out.println(msg.get("html"));

        mv.addObject("id", RequestUtil.getString(request, "id"));// 检查表编号
        mv.addObject("name", RequestUtil.getString(request, "name"));
        mv.addObject("attention", RequestUtil.getString(request, "attention"));
        mv.addObject("pid", RequestUtil.getString(request, "pid"));
        mv.addObject("fcid", RequestUtil.getString(request, "fcid"));
        mv.addObject("fcName", RequestUtil.getString(request, "fcName"));
        mv.addObject("type", type);
        mv.addObject("MID", MID);// 模板编号
        mv.addObject("html", msg.get("html"));
        mv.addObject("msg", msg);

        String sign = RequestUtil.getString(request, "sign");
        String status = RequestUtil.getString(request, "status");


        //校验未通过，直接跳至设计器页，并给出提示信息
        if (msg.get("success").toString().equals("false")) {
            mv.setViewName("/oa/editor/designer.jsp");
            mv.addObject("sign", sign);
            mv.addObject("status", status);
            return mv;
        }

        //跳至预览页
        mv.setViewName("/oa/editor/temppreview.jsp");
        String _status[] = status.split("/");
        String _signs[] = sign.split("/");
        mv.addObject("signs", _signs);
        mv.addObject("status", _status);

        return mv;
    }

    @RequestMapping("saveFormTemplate")
    @ResponseBody
    public Map<String, Object> saveFormTemplate(HttpServletRequest request,
                                                HttpServletResponse response) {
        String html = RequestUtil.getString(request, "htmlres");
        String MID = RequestUtil.getString(request, "MID");
        String type = RequestUtil.getString(request, "type");
        int index1 = html.indexOf("<table");
        int index2 = html.lastIndexOf("</table>");
        html = html.substring(index1, index2 + 8);
        System.out.println(html);
        // Map<String,
        // Object>msg=formService.check(html,id,name,sign,status,attention,pid,fcid,MID);
        Map<String, Object> msg = formService.save(html, MID, type);
        // System.out.println(msg.get("html"));
        return msg;
    }
    
    /**
     * Description : 保存表单模版的创建人和创建时间
     * Author : XYF
     * Date : 2018年10月18日上午11:15:36
     * Return : void
     */
    @RequestMapping("saveCreate")
    @ResponseBody
    public void saveCreate(HttpServletRequest request,HttpServletResponse response) {
        String MID = RequestUtil.getString(request, "MID");
        Long curUserId = UserContextUtil.getCurrentUserId();
        String userId = curUserId.toString();
        String time = TimeUtil.getCurrentTime();
        time = "to_date('"+time+"','yyyy-mm-dd,hh24:mi:ss')";
        formService.saveCreate(MID,userId,time);
    }
    /**
     * Description : 编辑表单模版时验证当前用户是否是该表单模版的创建人或所在型号的项目办管理人员
     * Author : XYF
     * Date : 2018年10月18日下午2:32:17
     * Return : String
     */
    @RequestMapping("checkUser")
    @ResponseBody
    public String checkUser(HttpServletRequest request,HttpServletResponse response){
    	String ID = RequestUtil.getString(request, "ID");
    	String result = "0";
    	Long curUserId = UserContextUtil.getCurrentUserId();
        String userId = curUserId.toString();
        List<Map<String,Object>> temps = formService.selectTableTempById(ID);
        if(temps.size()>0){
        	 
        }
        if(temps.size()>0){
        	Object createPeople = temps.get(0).get("F_CREATE_ID");
    		String CreatePeople = String.valueOf(createPeople);
    		if("".equals(CreatePeople)||"null".equals(CreatePeople)||userId.equals(CreatePeople)){
    			result = "1";
    		}else{
    			//验证当前用户是否是所属型号的项目办管理人员
    			Object FcId = temps.get(0).get("F_PROJECT_ID");
    			String fcId = String.valueOf(FcId);
    			List<Map<String,Object>> project = productService.selectProjectById(fcId);
    			if(project.size()>0){
    				Object ProductId = project.get(0).get("F_SSXH");
    				String productId = String.valueOf(ProductId);
    				List<Map<String,Object>> list = productService.getProjectOffById(productId);
    				if(list.size()>0){
    					for(int i=0;i<list.size();i++){
    						Object People = list.get(i).get("F_OFFICEPEOPLEID");
    						String people = String.valueOf(People);
    						String[] Person = people.split(",");
    						for(int j=0;j<Person.length;j++){
    							String person = Person[j];
    							if(person.equals(userId)){
    								result="1";
    								return result;
    							}
    						}
    					}
    				}
    			}
    		}
        }
        
    	return result;
    }

    /**
     * 跳转到新增模板页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addFormTemplate")
    public ModelAndView addFormTemplate(HttpServletRequest request,
                                        HttpServletResponse response) {
        String pid = RequestUtil.getString(request, "pid");
        String fcid = RequestUtil.getString(request, "fcid");
        ModelAndView mv = new ModelAndView("/oa/editor/checkform.jsp");
        mv.addObject("pid", pid);
        mv.addObject("fcid", fcid);
        return mv;
    }

    /**
     * 跳转到编辑模板页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("editFormTemplate")
    public ModelAndView editFormTemplate(HttpServletRequest request,
                                         HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");
        String fcName = RequestUtil.getString(request, "fcName");
        Map<String, Object> param = new HashMap<String, Object>();
        //表单模板id
        param.put("id", id);

        Map<String, Object> tabletemp = formService.getTableTempById(param);
        List<Map<String, Object>> signs = formService.getSignById(id);
        List<Map<String, Object>> checkCondition = formService.getCheckConditionById(id);

        ModelAndView mv = new ModelAndView("/oa/editor/checkFormEdit.jsp");
        mv.addObject("id", id);
        mv.addObject("tabletemp", tabletemp);
        mv.addObject("MID", tabletemp.get("ID"));
        mv.addObject("sign", signs);
        mv.addObject("status", checkCondition);
        mv.addObject("fcName", fcName);
        return mv;
    }

    /**
     * 校验模板编号是否唯一
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("checkFormid")
    @ResponseBody
    public Map<String, Object> checkFormid(HttpServletRequest request,
                                           HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");
        String mid = RequestUtil.getString(request, "MID");
        Map<String, Object> msg = formService.check_ID(id, mid);
        return msg;
    }

    @RequestMapping("checkFormName")
    @ResponseBody
    public Map<String, Object> checkFormName(HttpServletRequest request,
                                             HttpServletResponse response) {
        String name = RequestUtil.getString(request, "name");
        Map<String, Object> message = new HashMap<>();
        //检验 是否包含空格之类的特殊字符
        if (name.contains("\t")) {
            message.put("success", "false");
            message.put("msg", "检查表名称中包含制表符");
        } else {
            message.put("success", "true");
            message.put("msg", "检查表名称可以使用");
        }

        return message;
    }

    /**
     * 保存表单模板基本信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("checkForm")
    public ModelAndView checkForm(HttpServletRequest request,
                                  HttpServletResponse response) {
        String id = request.getParameter("id");// 模板编号
        String mid = request.getParameter("MID");// 模板主键
        String fcName = request.getParameter("fcName");// 模板编号
        String name = request.getParameter("name");
        String sign[] = request.getParameterValues("sign");
        String status[] = request.getParameterValues("status");
        String attention = request.getParameter("attention");
        String pid = RequestUtil.getString(request, "pid");
        String fcid = RequestUtil.getString(request, "fcid");
        String type = RequestUtil.getString(request, "type");
        Map<String, Object> message;
        ModelAndView mv = new ModelAndView();
        if (StringUtil.isEmpty(mid)) {
            message = formService.saveBasicInfo(id, name, sign, status,
                    attention, pid, fcid, type, IOConstans.TABLE_TEMP_UNCOMPLETE);
        } else {
            message = formService.updateBasicInfo(mid, id, name, sign, status,
                    attention, type);
            Map<String, Object> param = new HashMap<String, Object>();
            //表单模板id
            param.put("id", mid);
            Map<String, Object> tabletemp = formService.getTableTempById(param);
            String html = (String) tabletemp.get("F_CONTENTS");
            if (html != null) {
                int index1 = html.indexOf("<table");
                int index2 = html.lastIndexOf("</table>");
                if (index1 != -1 && index2 != -1) {
                    try {
                        html = html.substring(index1, index2 + 8);
                    } catch (Exception e) {
                        html = "";
                    }
                }
            }
            mv.addObject("html", html);
        }

        mv.addObject("id", id);// 检查表编号
        mv.addObject("name", name);
        mv.addObject("fcName", fcName);
        mv.addObject("attention", attention);
        mv.addObject("pid", pid);
        mv.addObject("fcid", fcid);
        mv.addObject("type", type);
        mv.addObject("MID", message.get("ID"));// 模板编号
        mv.addObject("message", message);

        if (message.get("success").equals("true")) {
            mv.addObject("sign", arrayToString(sign));
            mv.addObject("status", arrayToString(status));
            mv.setViewName("/oa/editor/designer.jsp");
        } else {
            mv.addObject("sign", sign);
            mv.addObject("status", status);
            mv.setViewName("/oa/editor/checkform.jsp");
        }
        return mv;
    }

    /**
     * 跳转到预览页
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("previewTableTemp")
    public ModelAndView previewTableTemp(HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        String html = RequestUtil.getString(request, "content");
        String id = RequestUtil.getString(request, "id");
        String name = RequestUtil.getString(request, "name");
        String sign = RequestUtil.getString(request, "sign");
        String status = RequestUtil.getString(request, "status");
        String attention = RequestUtil.getString(request, "attention");
        String pid = RequestUtil.getString(request, "pid");
        String fcid = RequestUtil.getString(request, "fcid");
        String MID = RequestUtil.getString(request, "MID");
        String Status[] = status.split("/");
        String Sign[] = sign.split("/");
        ModelAndView mv = new ModelAndView("/oa/editor/temppreview.jsp");
        mv.addObject("id", id);// 检查表编号
        mv.addObject("name", name);
        mv.addObject("Signs", Sign);
        mv.addObject("Status", Status);
        mv.addObject("attention", attention);
        mv.addObject("html", html);
        mv.addObject("pid", pid);
        mv.addObject("fcid", fcid);
        mv.addObject("MID", MID);// 模板编号
        return mv;
    }

    /**
     * 从预览页，跳转到表单模板设计器页
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("backTableTemp")
    public ModelAndView backTableTemp(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String html = RequestUtil.getString(request, "content");
        String id = RequestUtil.getString(request, "id");
        String name = RequestUtil.getString(request, "name");
        String sign[] = request.getParameterValues("signs");
        String status[] = request.getParameterValues("status");
        String attention = request.getParameter("attention");
        String pid = RequestUtil.getString(request, "pid");
        String fcid = RequestUtil.getString(request, "fcid");
        String MID = RequestUtil.getString(request, "MID");
        String type = RequestUtil.getString(request, "type");
        String Sign = "";
        for (int i = 0; i < sign.length; i++) {
            Sign += sign[i] + "/";
        }
        Sign = Sign.substring(0, Sign.length() - 1);
        String Status = "";
        for (int i = 0; i < status.length; i++) {
            Status += status[i] + "/";
        }
        Status = Status.substring(0, Status.length() - 1);
        ModelAndView mv = new ModelAndView("/oa/editor/designer.jsp");
        mv.addObject("id", id);// 检查表编号
        mv.addObject("name", name);
        mv.addObject("sign", Sign);
        mv.addObject("status", Status);
        mv.addObject("attention", attention);
        mv.addObject("pid", pid);
        mv.addObject("fcid", fcid);
        mv.addObject("MID", MID);// 模板编号
        mv.addObject("html", html);
        mv.addObject("type", type);
        return mv;
    }

    /**
     * 表单模板设计器页 暂存表单数据
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("temporaryStorage")
    @ResponseBody
    public Map temporaryStorage(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        String html = RequestUtil.getString(request, "html");
        Map msg = checkFormService.temporaryStorage(id, html);
        return msg;
    }

    /**
     * 从模板设计器页 跳转到模板基础信息页
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("backBasicInfo")
    public ModelAndView backBasicInfo(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        String name = RequestUtil.getString(request, "name");
        String sign = RequestUtil.getString(request, "sign");
        String status = RequestUtil.getString(request, "status");
        String attention = request.getParameter("attention");
        String html = RequestUtil.getString(request, "content");
        String type = RequestUtil.getString(request, "type");
        String MID = RequestUtil.getString(request, "MID");
        String Status[] = status.split("/");
        String Sign[] = sign.split("/");
        ModelAndView mv = new ModelAndView("/oa/editor/checkform.jsp");
        mv.addObject("id", id);// 检查表编号
        mv.addObject("MID", MID);// 检查表ID
        mv.addObject("formTempId", MID);// 检查表ID
        mv.addObject("name", name);
        mv.addObject("type", type);
        mv.addObject("sign", Sign);
        mv.addObject("status", Status);
        mv.addObject("attention", attention);
        mv.addObject("html", html);
        return mv;
    }

    @RequestMapping("manage")
    public ModelAndView manage(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        String fcId = RequestUtil.getString(request, "fcId");
        String fcName = RequestUtil.getString(request, "fcName");

        ModelAndView mv = getAutoView();
        mv.addObject("fcId", fcId);
        mv.addObject("fcName", fcName);
        return mv;
    }

    /**
     * 获取产品验收的某个产品的表单模板建模的树节点
     * 或 靶场实验的某个型号的表单模板建模的树节点
     * 靶场实验复用此代码的时候未改变代码内部逻辑，只增加了注释
     * renote by zmz 20200824
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getTreeData")
    @ResponseBody
    public void getTreeData(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        List<String> tree = new ArrayList<String>();
        /**
         * 这里获取的projectId是要去W_TABLE_TEMP的F_PROJECT_ID里找对应的信息
         * 值来源说明：
         *      当是产品验收时，此id为W_CPLBPCB的id
         *      当是靶场实验时，此id为型号id
         */
        String projectId = RequestUtil.getString(request, "projectId"); //W_CPLBPCB的id
        /**
         * 这里获取的projectName是表单建模的根文件夹名，由前端传来的数据指定
         *      当前端未传此数据时，去W_CPLBPCB表里查projectId对应的信息
         * 此值不参与逻辑
         */
        String projectName = RequestUtil.getString(request, "projectName"); //产品名称+代号
        //是否是选择模板弹出树
        String flag = RequestUtil.getString(request, "flag");

        String rootUrl = "\"\"";
        if (projectName.equals("")) {
//            Project p = projectService.getById(Long.valueOf(projectId));
//            if (p != null) {
//                projectName = p.getFcmc();
//            }
        	Map<String, Object> productBatch = productCategoryBatchDao.getById(projectId);
        	projectName = CommonTools.Obj2String(productBatch.get("F_CPMC"));
        }
        // 添加根节点
        String rootId = "1";
        String rootParentId = "-1";
        String rootNode = "{id:" + rootId + ", parentId:" + rootParentId
                + ", name:\"" + projectName + "\" , type:\"root\", tempUrl:"
                + rootUrl + ", target:\"formFrame\",open:true}";
        tree.add(rootNode);

        // 添加表单模板
        String formUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720028&__pk__=";
        // if(StringUtil.isNotEmpty(folderIds.toString())){
        // folderIds.deleteCharAt(folderIds.length()-1);
        List<Map<String, Object>> forms = new ArrayList<Map<String, Object>>();
        if (flag.equals("true")) {
            forms = formService.getFormsByProjectID(projectId, true);
        } else {
            forms = formService.getFormsByProjectID(projectId);
        }
        for (Map<String, Object> form : forms) {
            String id = CommonTools.Obj2String(form.get("ID"));
            String name = CommonTools.Obj2String(form.get("F_NAME"));
            String parentId = CommonTools.Obj2String(form.get("F_TEMP_FILE_ID"));
            String status = CommonTools.Obj2String(form.get("F_STATUS"));
            if (StringUtil.isEmpty(parentId)) {
                parentId = rootId;
            }
            String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
                    + name + "\" , type:\"form\", status:\"" + status + "\" , tempUrl:"
                    + (formUrl + id + "\"")
                    + ", target :\"formFrame\",open:true}";
            tree.add(node);
        }
        // }
        // 添加文件夹
        List<Map<String, Object>> folders = formService
                .allFormFolder(projectId);
        // StringBuffer folderIds=new StringBuffer();
        String folderUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720049&__pk__=";
        for (int i = 0; i < folders.size(); i++) {
            Map<String, Object> folder = folders.get(i);
            String id = CommonTools.Obj2String(folder.get("ID"));
            // folderIds.append(id+",");
            String parentId = CommonTools.Obj2String(folder
                    .get("F_TEMP_FILE_ID"));
            if (StringUtil.isEmpty(parentId)) {
                parentId = rootId;
            }
            String name = CommonTools.Obj2String(folder.get("F_NAME"));
            String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
                    + name + "\" , type:\"folder\", tempUrl:"
                    + (folderUrl + id + "\"")
                    + ", target :\"formFrame\",open:true}";
            tree.add(node);
        }
        // 利用Json插件将Array转换成Json格式
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONArray.fromObject(tree).toString());

    }

    @RequestMapping("showTable")
    public ModelAndView showTable(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        Map<String, Object> res = formService.getTableTempById(param);
        Object contents = res.get("F_CONTENTS");
        String html = "";
        if (contents != null)
            html = contents.toString();
        return getAutoView().addObject("id", id).addObject("html", html);
    }

    // 修改检查表先判断是否存在表单实例
    @RequestMapping("existInstance")
    @ResponseBody
    public Map<String, Object> existInstance(HttpServletRequest request,
                                             HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");
        Map<String, Object> msg = formService.ifExistIns(id);
        return msg;
    }

    // 修改检查表
    @RequestMapping("modifyTable")
    public ModelAndView modifyTable(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        Map<String, Object> res = formService.getTableTempById(param);
        Object contents = res.get("F_CONTENTS");
        String html = "";
        if (contents != null) {
            html = contents.toString();
            int index1 = html.indexOf("<table");
            int index2 = html.lastIndexOf("</table>");
            html = html.substring(index1, index2 + 8);
        }
        ModelAndView mv = new ModelAndView("/oa/editor/modifydesigner.jsp");
        mv.addObject("id", id);
        mv.addObject("html", html);
        return mv;
    }

	/*// 修改检查表
    @RequestMapping("modifyFormTemplate")
	@ResponseBody
	public Map<String, Object> modifyFormTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String html = RequestUtil.getString(request, "htmlres");
		String id = RequestUtil.getString(request, "id");
		Map<String, Object> msg = formService.modifycheck(html, id);
		return msg;
	}*/

    // 删除检查表模板
    @RequestMapping("deleteFormTemplate")
    @ResponseBody
    public Map<String, Object> deleteFormTemplate(HttpServletRequest request,
                                                  HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");
        Map<String, Object> msg = formService.deletecheck(id);
        return msg;
    }

    @RequestMapping("getInsId")
    @ResponseBody
    public Map<String, Object> getInsId(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> msg;
        Long insId = 0L;
        try {
            // 表单模板ID
            String formId = RequestUtil.getString(request, "formId");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", formId);
            Map<String, Object> ins = formService.getTableTempById(param);
            if (ins == null) {// ins为null，说明实例已经生成，已经不是模板了
                /*msg = new HashMap<String, Object>();
                msg.put("success", true);
                msg.put("insId", formId);*/
            	String tempId = formService.getTableTempIdById(formId);
            	Map<String, Object> Param = new HashMap<String, Object>();
            	Param.put("id", tempId);
            	ins = formService.getTableTempById(Param);
            }
       //     } else {
                // 表格实例ID
                insId = UniqueIdUtil.genId();
                ins.put("insId", insId);
                // 生成表格实例
                msg = formService.insertTableIns(ins);
                msg.put("insId", insId);
                // 生成检查条件结果
                List<Map<String, Object>> checkconditions = formService
                        .getCheckConditionById(formId);
                formService.createckcondition(checkconditions, insId);
                // 生成签署结果
                List<Map<String, Object>> signs = formService
                        .getSignById(formId);
                formService.createsignresult(signs, insId);
                // 生成检查结果、且替换掉表格实例内的content
                List<Map<String, Object>> checkitems = formService
                        .getCheckItemById(formId);
                String content = ins.get("F_CONTENTS").toString();
                formService.createcheckresult(checkitems, insId, content);
          //  }
        } catch (Exception e) {
            msg = new HashMap<String, Object>();
            msg.put("success", false);
            msg.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }

    
 // 批量删除检查表模板
 		@RequestMapping("templateListDelete")
 		@ResponseBody
 		public void templateListDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
 	        ResultMessage message = null;
 	        List<String> templateList = new ArrayList<>();
 	        try {
 	            String ids = RequestUtil.getString(request, "ID");
 	        //    Long sssjb = RequestUtil.getLong(request, "sssjb");

 	            templateList = formService.templateListDel( ids);
 	            message = new ResultMessage(ResultMessage.Success, "true");
 	            message.addData("templateList",templateList);

 	        } catch (Exception e) {
 	            e.printStackTrace();
 	            message = new ResultMessage(ResultMessage.Fail, e.getMessage());
 	        }
 	        writeResultMessage(response.getWriter(), message);
 	    }
 		
 		
 		/**
 		 * Description : 表单实例录入时的权限验证
 		 * Author : XYF
 		 * Date : 2018年8月14日上午9:02:49
 		 * Return : String
 		 */
 		@RequestMapping("powerManage")
 		@ResponseBody
 		public String powerManage(HttpServletRequest request, HttpServletResponse response){
 			String dataId = RequestUtil.getString(request, "dataId");
 			String result = "";
 			long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
 			String UserId = Long.toString(userId);
 			String state = formService.selectZxztById(dataId);
// 			String user = formService.userIdPower(UserId, dataId);
 			// 权限暂时放开
 			String user = "yes";
 			if("no".equals(user)){
 				result = "no";
 			}else{
 				result = "yes";
 			}
 				
 			
 			
 			return result;
 	    }
 		
    /**
     * 导入表单模板操作
     **/
    @RequestMapping("tempfileUpload")
    @Action(description = "文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "上传文件:"
            + "<#list sysFiles as item>"
            + "${item.filename}.${item.ext}"
            + "</#list>")
    public void tempfileUpload(MultipartHttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        // 返回上传结果
    																																																																																																																																																																							String returnMsg = "";
        PrintWriter writer = response.getWriter();
        try {
            //所属文件夹 ID
            String parentId = RequestUtil.getString(request, "pid");
            //所属发次ID
            String projectId = RequestUtil.getString(request, "fcid");
            String filetype = RequestUtil.getString(request, "type");
            String extype = RequestUtil.getString(request, "extype");
            long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
            ISysUser appUser = null;
            if (userId > 0) {
                appUser = sysUserService.getById(userId);
            }
            String rpcrefname = RequestUtil.getString(request,
                    IFieldPool.rpcrefname);
            if (StringUtil.isNotEmpty(rpcrefname)) {
                // 采用IOC方式，根据RPC远程过程调用服务调用数据
                CommonService commonService = (CommonService) AppUtil.getContext().getBean(rpcrefname);
                returnMsg = commonService.uploadAttach(request, response, appUser, userId);
            } else {
                //word结构化
                if (CommonTools.Obj2String(filetype).equalsIgnoreCase("word")) {
                    returnMsg = formService.uploadAttachWord(request, response, appUser, userId, parentId, projectId);
                } else {
                    returnMsg = formService.uploadExcelTemp(request, response, appUser, userId, parentId, projectId,extype);
                }
            }
            writer.println(returnMsg);
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("{\"success\":\"false\",\"context\":\"上传文件失败，请联系系统管理员\"}");
        }
    }

    
    // 表格管理
    @RequestMapping("formManage")
    public ModelAndView formManage(HttpServletRequest request,
                                   HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("oa/editor/gridManage.jsp");
        String slid = RequestUtil.getString(request, "slid");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        List<Map<String, Object>> signresult = formService
                .getSignResultById(slid);
        if (signresult.size() > 0) {
            for (int i = 0; i < signresult.size(); i++) {
                try {
                    String contextPath = request.getContextPath();
                    String fileId = signresult.get(i).get("FILEID").toString();
                    SysFile sysFile = sysFileService.getById(Long
                            .valueOf(fileId));
                    String filePath = sysFile.getFilepath();
                    String fileType = sysFile.getExt();
                    String fileName = sysFile.getFilename() + "."
                            + sysFile.getExt();

                    Boolean isNoGroup = filePath.startsWith("group");// 判断是否分布式文件
                    String interview_server = FastDFSFileOperator
                            .getInterviewServer(); // 分布式请求url端口
                    if (isNoGroup) {
                        if (sysFile.getIsEncrypt() != 1L) {
                            filePath = interview_server + "/"
                                    + sysFile.getFilepath();
                        }
                    } else {
                        filePath = attachPath + File.separator + filePath;
                    }
                    String destFilePath = sysFileService.getDecodeFilePath(
                            filePath, fileName, sysFile.getIsEncrypt(),
                            isNoGroup);
                    if (!"".equals(destFilePath)) {
                        filePath = destFilePath;
                        fileName = FileOperator.getFileNameByPath(filePath);
                    }
                    if ("png,bmp,gif,jpg".contains(fileType.toLowerCase())) {

                        String imgSrc = contextPath
                                + "/oa/system/sysFile/getFileById.do?fileId="
                                + fileId;
                        signresult.get(i).put("imgSrc", imgSrc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            signresult = formService.getSignResById(slid);
        }

        List<Map<String, Object>> condires = formService.getCondiResById(slid);
        //如果所有的检查项都是空的
        Boolean ifAllNullCondires=true;
        if (condires!=null){
            //判断是否空的检查项
            for (Map<String,Object> map:condires){
                if (map.get("F_VALUE")!=null){
                    ifAllNullCondires=false;
                }
            }
        }
        //需求变更,只要模板有就展示,不再关心是否真的有值
        if (condires!=null){
            if (condires.size()>0){
                ifAllNullCondires=false;
            }
        }
        List<Map<String, Object>> checkItemandmap = formService.getCheckItemAndMapById(slid);
        String content = tableins.get("F_CONTENT").toString();
        String title=tableins.get("F_NAME").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        content = content.replaceAll(
                "<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\"",
                "<input class=\"dpInputBtn\" type=\"button\"");
        //可能存在未能将按钮的disabled属性正确替换的情况（因为构建表单模板时的一些顺序因素），这里需要重新替换
        content = content.replaceAll("disabled=\"true\""," ");
        content=formService.conversionHTML(content);
        mv.addObject("content", content);
        mv.addObject("signresult", signresult);
        mv.addObject("condires", condires);
        mv.addObject("ifAllNullCondires",ifAllNullCondires);
        mv.addObject("checkitem", checkItemandmap);
        mv.addObject("slid", slid);
        mv.addObject("title",title);
         return mv;
    }

    // 表单录入
    @RequestMapping("formEntry")
    public ModelAndView formEntry(HttpServletRequest request,
                                  HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("oa/editor/formEntry.jsp");
        String slid = RequestUtil.getString(request, "slid");
        //数据包详细信息ID
        String packageId = RequestUtil.getString(request, "packageId");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        param.put("id", tableins.get("F_TABLE_TEMP_ID").toString());
        Map<String, Object> tabletemp = formService.getTableTempById(param);
//		List<Map<String, Object>> condition = formService
//				.getCheckConditionById(tabletemp.get("ID").toString());
        List<Map<String, Object>> condires = formService.getCondiResById(slid);
//		List<Map<String, Object>> sign = formService.getSignById(tabletemp.get(
//				"ID").toString());
        List<Map<String, Object>> signres = formService.getSignResById(slid);
        String content = tableins.get("F_CONTENT").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        content = content.replaceAll("readonly=\"readonly\"", " ").replaceAll(
                "disabled", " ");
        mv.addObject("slid", slid);
        mv.addObject("content", content);
        //mv.addObject("signs", sign);
        mv.addObject("signs", signres);
        mv.addObject("signSize",signres.size());
        //mv.addObject("conditions", condition);
        mv.addObject("condires", condires);
        mv.addObject("condiresSize",condires.size());
        mv.addObject("packageId", packageId);
        return mv;
    }

    // 表单录入保存
    @RequestMapping("saveForm")
    @ResponseBody
    public Map<String, Object> saveForm(HttpServletRequest request,
                                        HttpServletResponse response) {
        String str = RequestUtil.getString(request, "jsoncontent");
        String slid = RequestUtil.getString(request, "slid");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        Map<String, List<Map<String, String>>> list = JSONObject.parseObject(
                str,
                new TypeReference<Map<String, List<Map<String, String>>>>() {
                });

        String html = tableins.get("F_CONTENT").toString();
        int index1 = html.indexOf("<table");
        int index2 = html.lastIndexOf("</table>");
        html = html.substring(index1, index2 + 8);
        Map<String, Object> msg = formService.saveform(list, html, slid);
        return msg;
    }

    // 预览图片前先判断是否存在图片
    @RequestMapping("existPicture")
    @ResponseBody
    public Map<String, Object> existPicture(HttpServletRequest request,
                                            HttpServletResponse response) {
        String tableId = RequestUtil.getString(request, "tableId");
        String ckResultName = CommonTools.null2String(RequestUtil.getString(request, "ckResultName"));
        //根据检查结果名称来确定图片
        if (!"".equals(ckResultName)) {
            ckResultName = "&&" + ckResultName;
        }
        Map<String, Object> msg = formService.ifExistPic(tableId + ckResultName);
        return msg;
    }
    
 // 判断是否存在附件
    @RequestMapping("existEnclosure")
    @ResponseBody
    public String existEnclosure(HttpServletRequest request,HttpServletResponse response) {
        String tableId = request.getParameter("id");

        String slId = request.getParameter("slId");
        String ckResultName = productService.getCondResultNameByInsId(Long.parseLong(slId));
        
        if (!"".equals(ckResultName)) {
            ckResultName = "&&" + ckResultName;
        }
        String result = formService.ifExistEnclosure(tableId + ckResultName);
        return result;
    }

    @RequestMapping("checkWhetherFileExist")
    public void checkWhetherFileExist(HttpServletRequest request,HttpServletResponse response)throws IOException{

        String fileIdArray = CommonTools.null2String(request.getParameter("fileIdArray"));
        List<Map<String,Object>> fileList = new ArrayList<>() ;
        Gson gson = new Gson() ;
        fileList = gson.fromJson(fileIdArray, new TypeToken<List<Map<String,String>>>(){}.getType());

        Map resultMap = formService.checkWhetherFileExist(fileList);
        // 利用Json插件将Array转换成Json格式
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONArray.fromObject(resultMap).toString());
    }


    // 图片预览
    @RequestMapping("picpreview")
    public ModelAndView picpreview(HttpServletRequest request,HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");
        String ckResultName = CommonTools.null2String(RequestUtil.getString(request, "ckResultName"));
        //根据检查结果名称来确定图片
        if (!"".equals(ckResultName)) {
            ckResultName = "&&" + ckResultName;
        }
        String diagramid = RequestUtil.getString(request, "diagramid");// 检查项示意图ID
        String ifdel = RequestUtil.getString(request, "ifdel");
        String slid = "";// 表格实例ID
        List<Map<String, Object>> paths = new ArrayList<Map<String, Object>>();
        if (id != "")
            paths = formService.getPicPathByID(id + ckResultName);
        else if (diagramid != "") {
            paths = formService.getDiaPathByID(diagramid);
            slid = RequestUtil.getString(request, "slid");
        }
        List<Map<String, Object>> images = new ArrayList<Map<String, Object>>();
		ModelAndView modelView = null;
        String contextPath = request.getContextPath();
        try {
            modelView = new ModelAndView("oa/editor/picpreview.jsp");
            for (int i = 0; i < paths.size(); i++) {
                Map<String, Object> image = new HashMap<String, Object>();
                String fileId = paths.get(i).get("FILEID").toString();
                SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
                String filePath = sysFile.getFilepath();
                String fileType = sysFile.getExt();
                String fileName = sysFile.getFilename() + "."
                        + sysFile.getExt();

                Boolean isNoGroup = filePath.startsWith("group");// 判断是否分布式文件
                String interview_server = FastDFSFileOperator
                        .getInterviewServer(); // 分布式请求url端口
                if (isNoGroup) {
                    if (sysFile.getIsEncrypt() != 1L) {
                        filePath = interview_server + "/"
                                + sysFile.getFilepath();
                    }
                } else {
                    filePath = attachPath + File.separator + filePath;
                }
                // by weilei:对加密文件进行解密处理
                String destFilePath = sysFileService.getDecodeFilePath(
                        filePath, fileName, sysFile.getIsEncrypt(), isNoGroup);
                if (!"".equals(destFilePath)) {
                    filePath = destFilePath;
                    fileName = FileOperator.getFileNameByPath(filePath);
                }
                if ("png,bmp,gif,jpg".contains(fileType.toLowerCase())) {

                    String imgSrc = contextPath
                            + "/oa/system/sysFile/getFileById.do?fileId="
                            + fileId;
                    image.put("imgSrc", imgSrc);
                }else if("txt".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/txt.png";
                    image.put("imgSrc", imgSrc);
                }else if("doc,docx".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/doc.png";
                    image.put("imgSrc", imgSrc);
                }else if("pdf".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/pdf.png";
                    image.put("imgSrc", imgSrc);
                }else if("zip,rar".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/ZIP.png";
                    image.put("imgSrc", imgSrc);
                }else if("xls,xlsx".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/xls.png";
                    image.put("imgSrc", imgSrc);
                }else if("xml".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/XML.png";
                    image.put("imgSrc", imgSrc);
                }else if("mp4,wmv,vcr,".contains(fileType.toLowerCase())){
                	String imgSrc = "/dp/dpImg/fileIcon/MP4.png";
                    image.put("imgSrc", imgSrc);
                }
                else{
                	String imgSrc = "/dp/dpImg/fileIcon/file.png";
                    image.put("imgSrc", imgSrc);
                }
                
                // 获取系统跟路径
               // String root=FileUtil.getRootPath();
                image.put("fileId", fileId);
                image.put("fileName", fileName);
                image.put("fileType", fileType.toLowerCase());
                image.put("path", sysFile.getFilepath());
                image.put("dataId", sysFile.getDataId());
                image.put("filePath", filePath);
                image.put("slid", slid);
                images.add(image);
            }
            modelView.addObject("images", images);
            modelView.addObject("ifdel", ifdel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelView;
    }

    @SuppressWarnings("unused")
    @ResponseBody
    @RequestMapping({"picdel"})
    public Map<String, Object> picdel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> message = new HashMap<String, Object>();
        String returnUrl = RequestUtil.getPrePage(request);
        Long[] lAryId = RequestUtil.getLongAryByStr(request, "fileId");
        Long isDialog = RequestUtil.getLong(request, "isDialog", 0);
        String slid = RequestUtil.getString(request, "slid");
        try {
            if (!this.saveType.contains("database")) {
                for (Long id : lAryId) {
                    SysFile sysFile = (SysFile) this.sysFileService.getById(id);
                    String filePath = sysFile.getFilepath();
                    // 判断附件是本地附件还是服务器存储附件
                    if (filePath.startsWith("group")) {
                        FastDFSFileOperator.deleteFile(filePath);
                    } else {
                        if (StringUtil.isEmpty(attachPath)) {
                            filePath = AppUtil.getRealPath(filePath);
                        }

                        FileUtil.deleteFile(attachPath + File.separator
                                + filePath);
                        if (!(slid.equals(""))) {
                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("diaId", id);
                            param.put("slid", slid);
                            formService.delckresDiagram(param);
                        }
                    }
                }
            }

            this.sysFileService.delByIds(lAryId);
            // 删除文件索引
            solrService.deleteFileIndex(lAryId);

            message.put("success", true);
            message.put("msg", "删除附件成功");
        } catch (Exception e) {
            message.put("success", false);
            message.put("msg", "删除附件失败");
        }
        return message;
    }

    /**
     * 下载附件---FZH
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping({"picdownload"})
    public Map<String, Object> picdownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> message = new HashMap<String, Object>();
        String returnUrl = RequestUtil.getPrePage(request);
        Long[] lAryId = RequestUtil.getLongAryByStr(request, "fileId");
        Long isDialog = RequestUtil.getLong(request, "isDialog", 0);
        String slid = RequestUtil.getString(request, "slid");
        try {
            if (!this.saveType.contains("database")) {
//                for (Long id : lAryId) {
//                    SysFile sysFile = (SysFile) this.sysFileService.getById(id);
//                    String filePath = sysFile.getFilepath();
//                    // 判断附件是本地附件还是服务器存储附件
//                    if (filePath.startsWith("group")) {
//                        FastDFSFileOperator.deleteFile(filePath);
//                    } else {
//                        if (StringUtil.isEmpty(attachPath)) {
//                            filePath = AppUtil.getRealPath(filePath);
//                        }
//
//                        FileUtil.deleteFile(attachPath + File.separator
//                                + filePath);
//                        if (!(slid.equals(""))) {
//                            Map<String, Object> param = new HashMap<String, Object>();
//                            param.put("diaId", id);
//                            param.put("slid", slid);
//                            formService.delckresDiagram(param);
//                        }
//                    }
//                }
            }

//            this.sysFileService.delByIds(lAryId);
//            // 删除文件索引
//            solrService.deleteFileIndex(lAryId);

            message.put("success", true);
            message.put("msg", "下载成功");
        } catch (Exception e) {
            message.put("success", false);
            message.put("msg", "下载失败");
        }
        return message;
    }

    // 上传检查项示意图
    @RequestMapping("diagramupload")
    public ModelAndView diagramupload(HttpServletRequest request, HttpServletResponse response) {
        String id = RequestUtil.getString(request, "id");// 检查项ID
        String slid = RequestUtil.getString(request, "slid");// 检查实例ID
        Long diaId = UniqueIdUtil.genId();// 示意图ID
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("slid", slid);
        Map<String, Object> ckresult = formService.getCkResultById(param);
        ModelAndView mv = new ModelAndView("oa/editor/diagramUpload.jsp");
        // 此处把示意图ID插入到检查结果
        mv.addObject("ckresult", ckresult);
        mv.addObject("diaId", diaId);
        return mv;
    }

    // 示意图保存
    @RequestMapping("diagramsave")
    public void diagramsave(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
        // 返回上传结果
        String diaId = RequestUtil.getString(request, "diaId");
        String chresId = RequestUtil.getString(request, "chresId");
        String returnMsg = "";
        PrintWriter writer = response.getWriter();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("diaId", diaId);
        param.put("chresId", chresId);
        try {
            long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
            ISysUser appUser = null;
            if (userId > 0) {
                appUser = sysUserService.getById(userId);
            }
            returnMsg = sysFileService.flieuploadAttach(request, response,
                    appUser, userId, Long.parseLong(diaId));
            formService.updateckresDiagram(param);
            writer.println(returnMsg);
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("{\"success\":\"false\"}");
        }
    }
    /**
     * 模板导出成Excel
     * @param request
     * @param response
     */
    @RequestMapping("exportTempExcel")
    public void exportTempExcel(HttpServletRequest request, HttpServletResponse response) {
        String tempId = RequestUtil.getString(request, "tempId");
        TableTemp tableTemp=ioTableTempDao.getById(tempId);
      //模板content
        String content = tableTemp.getContents();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        formService.exportModel(content,request,response,tableTemp.getName(),tableTemp);
    }
    /**
     * 导出成Excel
     * @param request
     * @param response
     */
    @RequestMapping("exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String html = RequestUtil.getString(request, "html");
        //W_TB_INSTANT   实例id
        String slid = RequestUtil.getString(request, "slid");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        // W_TABLE_TEMP   模板id
        param.put("id", tableins.get("F_TABLE_TEMP_ID").toString());
        Map<String, Object> tabletemp = formService.getTableTempById(param);
        //W_PACKAGEINFO 表单实例ID
        Map queryMap = new HashMap();
        queryMap.put("F_SSMB", slid);
        List<Map<String, Object>> dataPackageInfoList = dataPackageInfoDao.query(queryMap);
        String security = "";
        if(dataPackageInfoList.size()>0 && dataPackageInfoList !=null) {
            security = CommonTools.Obj2String(dataPackageInfoList.get(0).get("F_MJ"));
        }

        //模板content
        String content = tableins.get("F_CONTENT").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        html = content + "\n" + html;
        try {
            String fileName = tabletemp.get("F_NAME").toString();
            formService.exportExcel(html, request, response, slid, fileName,security);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:批量导出表单实例Excel数据
     * @Author  shenguoliang
     * @param request
     * @param response
     * @Date 2019/1/17 17:10
     * @Return void
     * @Line 1287
     */
    @RequestMapping("batchExportExcel")
    public void batchExportExcel(HttpServletRequest request, HttpServletResponse response) {
        String recordsArr = CommonTools.null2String(request.getParameter("recordsArr"));
        List<Map<String,Object>> dataList = new ArrayList<>() ;
        Gson gson = new Gson() ;
        dataList = gson.fromJson(recordsArr, new TypeToken<List<Map<String, String>>>() {}.getType());
        try {
            //导出压缩包
            dataPackageInfoService.batchExportExcel(request,response,dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 模板导出成Excel
    @RequestMapping("exportModel")
    public void exportModel(HttpServletRequest request,
                            HttpServletResponse response) {
        String modelId = RequestUtil.getString(request, "modelId");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", modelId);
        Map<String, Object> tabletemp = formService.getTableTempById(param);
        String content = tabletemp.get("F_CONTENTS").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        try {
            String fileName = tabletemp.get("F_NAME").toString();
            formService.exportModel(content, request, response, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 表单模板-导入历史数据
    @RequestMapping("importExcel")
    public void importExcel(MultipartHttpServletRequest request
            , HttpServletResponse response) throws Exception {
        Long slid = RequestUtil.getLong(request, "slid");
        ResultMessage message = null;
        try {
            MultipartFile file = request.getFile("file");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", slid);
            Map<String, Object> tableins = formService.getTableInstantById(param);
            String html = tableins.get("F_CONTENT").toString();
            int index1 = html.indexOf("<table");
            int index2 = html.lastIndexOf("</table>");
            html = html.substring(index1, index2 + 8);

            String log = formService.importExcel(file, slid, html);
            message = new ResultMessage(ResultMessage.Success, "true");
            message.addData("log", log);
        } catch (Exception e) {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        writeResultMessage(response.getWriter(), message);
    }

    @RequestMapping("selectFolder")
    public ModelAndView selectFolder(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        String fcId = RequestUtil.getString(request, "fcId");
        String fcName = RequestUtil.getString(request, "fcName");
        String nodeid = RequestUtil.getString(request, "nodeid");
        ModelAndView mv = new ModelAndView("oa/editor/moveModel.jsp");
        mv.addObject("fcId", fcId);
        mv.addObject("fcName", fcName);
        mv.addObject("nodeid", nodeid);
        return mv;
    }

    /**
     * 获取文件夹树
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getFolderTree")
    @ResponseBody
    public void getFolderTree(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        List<String> tree = new ArrayList<String>();
        String projectId = RequestUtil.getString(request, "projectId");
        String projectName = RequestUtil.getString(request, "projectName");
        String rootUrl = "\"\"";

        // 添加根节点
        String rootId = "1";
        String rootParentId = "-1";
        String rootNode = "{id:" + rootId + ", parentId:" + rootParentId
                + ", name:\"" + projectName + "\" , type:\"root\", tempUrl:"
                + rootUrl + ", target:\"formFrame\",open:true}";
        tree.add(rootNode);

        // 添加文件夹
        List<Map<String, Object>> folders = formService
                .allFormFolder(projectId);
        String folderUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720049&__pk__=";
        for (int i = 0; i < folders.size(); i++) {
            Map<String, Object> folder = folders.get(i);
            String id = CommonTools.Obj2String(folder.get("ID"));
            String parentId = CommonTools.Obj2String(folder
                    .get("F_TEMP_FILE_ID"));
            if (StringUtil.isEmpty(parentId)) {
                parentId = rootId;
            }
            String name = CommonTools.Obj2String(folder.get("F_NAME"));
            String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
                    + name + "\" , type:\"folder\", tempUrl:"
                    + (folderUrl + id + "\"")
                    + ", target :\"formFrame\",open:true}";
            tree.add(node);
        }
        // 利用Json插件将Array转换成Json格式
        response.getWriter().print(JSONArray.fromObject(tree).toString());
    }

    // 移动模板
    @RequestMapping("moveModel")
    @ResponseBody
    public Map<String, Object> moveModel(HttpServletRequest request, HttpServletResponse response) {
        String nodeid = RequestUtil.getString(request, "nodeid");
        String pId = RequestUtil.getString(request, "pid");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", nodeid);
        param.put("fileId", pId);
        Map<String, Object> message = formService.moveModelById(param);
        return message;
    }

    @SuppressWarnings("unused")
    @RequestMapping("selectMutilModel")
    public ModelAndView selectMutilModel(HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        String fcId = RequestUtil.getString(request, "fcId");// 发次ID
        String fcName = RequestUtil.getString(request, "fcName");
        String nodeid = RequestUtil.getString(request, "nodeid");// 文件夹ID
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", fcId);
        Map<String, Object> project = formService.getProjectById(param);
        ModelAndView mv = new ModelAndView("oa/editor/copyMutilModel.jsp");
        mv.addObject("productId", project.get("F_SSXH"));
        mv.addObject("projectId", fcId);
        // mv.addObject("projectName", fcName);
        mv.addObject("nodeid", nodeid);
        return mv;
    }

    /**
     * 获取多个文件夹树
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("getMutilFolderTree")
    @ResponseBody
    public void getMutilFolderTree(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        List<String> tree = new ArrayList<String>();
        String projectId = RequestUtil.getString(request, "projectId");// 发次ID
        String productId = RequestUtil.getString(request, "productId");// 型号ID
        // 获取所有发次信息
        List<Map<String, Object>> projects = formService
                .getAllProjectByID(productId);
        /*for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).get("ID").toString().equals(projectId)) {
                projects.remove(i);
                i--;
                break;
            }
        }*/
        for (int j = 0; j < projects.size(); j++) {
            // 添加根节点
            String rootUrl = "\"\"";
            String rootId = String.valueOf(j + 1);
            String rootParentId = "-1";
            String rootNode = "{id:" + rootId + ", parentId:" + rootParentId
                    + ", name:\"" + projects.get(j).get("F_FCMC")
                    + "\" , type:\"root\", tempUrl:" + rootUrl
                    + ", target:\"formFrame\",open:true}";
            tree.add(rootNode);

            // 添加表单模板
            String formUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720028&__pk__=";
            List<Map<String, Object>> forms = formService
                    .getFormsByProjectID(projects.get(j).get("ID").toString());
            for (Map<String, Object> form : forms) {
                String id = CommonTools.Obj2String(form.get("ID"));
                String name = CommonTools.Obj2String(form.get("F_NAME"));
                String parentId = CommonTools.Obj2String(form
                        .get("F_TEMP_FILE_ID"));
                if (StringUtil.isEmpty(parentId)
                        || Long.parseLong(parentId) == 1) {
                    parentId = rootId;
                }
                String node = "{id:" + id + ", parentId:" + parentId
                        + ", name:\"" + name + "\" , type:\"form\", tempUrl:"
                        + (formUrl + id + "\"")
                        + ", target :\"formFrame\",open:true}";
                tree.add(node);
            }
            // 添加文件夹
            List<Map<String, Object>> folders = formService
                    .allFormFolder(projects.get(j).get("ID").toString());
            String folderUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720049&__pk__=";
            for (int i = 0; i < folders.size(); i++) {
                Map<String, Object> folder = folders.get(i);
                String id = CommonTools.Obj2String(folder.get("ID"));
                String parentId = CommonTools.Obj2String(folder
                        .get("F_TEMP_FILE_ID"));
                if (StringUtil.isEmpty(parentId)
                        || Long.parseLong(parentId) == 1) {
                    parentId = rootId;
                }
                String name = CommonTools.Obj2String(folder.get("F_NAME"));
                String node = "{id:" + id + ", parentId:" + parentId
                        + ", name:\"" + name + "\" , type:\"folder\", tempUrl:"
                        + (folderUrl + id + "\"")
                        + ", target :\"formFrame\",open:true}";
                tree.add(node);
            }
        }
        // 利用Json插件将Array转换成Json格式
        response.getWriter().print(JSONArray.fromObject(tree).toString());
    }

    // 复制多个模板
    @RequestMapping("copyMutilModel")
    @ResponseBody
    public Map<String, Object> copyMutilModel(HttpServletRequest request,
                                              HttpServletResponse response) {
        String nodeid = RequestUtil.getString(request, "nodeid");// 文件夹id
        String modelIds = RequestUtil.getString(request, "mids");// 选中的多个模板id
        String projectId = RequestUtil.getString(request, "projectId");// 发次id
        String Mids[] = modelIds.split(",");
        Map<String, Object> message = formService.copyModel(Long.parseLong(nodeid), Mids, Long.parseLong(projectId));
        return message;
    }

    private String arrayToString(String[] strs) {
        boolean first = true;
        StringBuffer sb = new StringBuffer("");
        for (String str : strs) {
            if (StringUtil.isNotEmpty(str)) {
                if (first) {
                    sb.append(str);
                    first = false;
                } else {
                    sb.append("/" + str);
                }
            }
        }
        return sb.toString();
    }

    // 只有所级管理员才可以删除检查表模板
    @RequestMapping("deleteFormTemplateOnlyAdministrator")
    @ResponseBody
    public String deleteFormTemplateOnlyAdministrator(HttpServletRequest request,
                                                  HttpServletResponse response) {
        // 获取当前用户
        ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
        String curRoles=curUser.getRoles();
        if ("sjgly".equals(curRoles)){
            //是所级管理员
            return "1";
        }else {
            //不是所级管理员
            return "0";
        }
    }
}
