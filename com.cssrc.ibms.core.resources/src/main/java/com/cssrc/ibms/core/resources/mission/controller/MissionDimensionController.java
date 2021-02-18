package com.cssrc.ibms.core.resources.mission.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.core.resources.mission.service.RangeMissionService;
import com.cssrc.ibms.core.resources.mission.service.TestPlanService;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zmz
 * @description 产品维度结构树控制器
 */
@Controller
@RequestMapping("/mission/dimension/tree/")
public class MissionDimensionController extends BaseController {

    @Resource
    private RangeMissionService rangeMissionService;
    @Resource
    private TestPlanService testPlanService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private ModuleManageService moduleManageService;
    /**
     * 获取任务树信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping({"getTreeData"})
    @Action(description = "获取任务维度结构树")
    public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 型号id、型号名称
        String moduleId = request.getParameter("moduleId");

        String moduleName = moduleService.getByModelId(moduleId).get("F_XHDH").toString();


        //型号基本信息
        //10000029830022	型号管理列表
        String moduleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000029830022\"";
        //470184	靶场试验任务列表
        String moduleHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000028470184\"";
        //任务基本信息
        //470184	靶场试验任务列表
        String missionUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000028470184\"";    //任务树右键编辑\菜单按钮新增
        String missionBuilderUrl = "\"/dataPackage/tree/mission/builder.do?\"";        //任务树左击tab页
///		String missionHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021200168\"";		//200168	产品批次列表
        //470184	靶场试验任务列表
        String missionHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000028470184\"";
        //任务策划
        String acceptancePlanBuilderUrl = "\"/dataPackage/tree/missionAcceptancePlan/builder.do?\"";
        //任务 - 数据包url
        String dataPackage = "\"/dataPackage/tree/rangeDataPackageShow/main.do?\"";
        String productUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200549\"";        //200549	产品列表
        //产品策划-url
        String testMissionUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000028760024\"";        //		靶场试验策划报告表
        String tempmoduleHandleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000028760024\"";    //		靶场试验策划报告表

        List<String> missionDimensionTree = new ArrayList<String>();

        String missionNode = "{id:" + 0 + ","
                + "parentId:" + moduleId + ","
                + "name:\"" + "全部任务" + "\","
                + "tempUrl:" + missionUrl + ","
//				+"handleUrl:"+missionHandleUrl+","
                + "handleUrl:" + missionUrl + ","
                + "missionBuilderUrl:" + missionBuilderUrl + ","
                + "moduleId:" + moduleId + ","
                + "target : \"dataPackageFrame\","
                + "type: \"mission\" ,"
                + "open:true}";
        missionDimensionTree.add(missionNode);
        //获取靶场试验策划节点
//				List<Map<String,Object>> testPlans=testPlanDao.getApprovedPlansByModuleName(moduleName);
        List<Map<String, Object>> testPlans = testPlanService.getApprovedPlansByModuleId(moduleId);
        for (Map<String, Object> planMap : testPlans) {
            // 任务策划报告表编号
            String planName = planMap.get("F_CHBGBBH") + "";
            String planNode = "{id:" + planMap.get("ID") + ", "
                    + "tempUrl:" + tempmoduleHandleUrl + ", "
                    + "handleUrl:" + testMissionUrl + ", "
                    + "parentId:0, "
                    + "name:\"" + planName + "\" ,"
                    + " type: \"testPlan\" , "
                    + "acceptancePlanBuilderUrl:" + acceptancePlanBuilderUrl + ", "
                    + "moduleId:" + moduleId + ", "
//							+ "moduleName:\"" + moduleName 
//							+ "missionId:" + missionId+ ", "
//							+ "missionName:\"" + missionName+ "\","
//							+ "batchId:" + batchId + ", "
//							+ "batchKey:\"" + batchKey + "\", "
                    + "target : \"dataPackageFrame\","
                    + "open:true}";
            missionDimensionTree.add(planNode);
        }
        response.getWriter().print(JSONArray.fromObject(missionDimensionTree).toString());
    }

    /**
     * 获取武器所检的策划任务树
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping({"getTreeDataForWeaponCheck"})
    @Action(description = "获取任务维度结构树")
    public void getTreeDataForWeaponCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 型号id、型号名称
        String moduleId = request.getParameter("moduleId");

        String moduleName = moduleService.getByModelId(moduleId).get("F_XHDH").toString();


        //型号基本信息
        //10000029830022	型号管理列表
        String moduleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000029830022\"";
        //10000031900580	武器所检列表
        String moduleHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000031900580\"";
        //任务基本信息
        //10000031900580	武器所检列表
        String missionUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000031900580\"";    //任务树右键编辑\菜单按钮新增
        String missionBuilderUrl = "\"/dataPackage/tree/weaponCheckMission/builder.do?\"";        //任务树左击tab页
///		String missionHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021200168\"";		//200168	产品批次列表
        //470184	靶场试验任务列表
        String missionHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000031900580\"";
        //任务策划
        String acceptancePlanBuilderUrl = "\"/dataPackage/tree/weaponCheckAcceptancePlan/builder.do?\"";
        //任务 - 数据包url
/*        String dataPackage = "\"/dataPackage/tree/rangeDataPackageShow/main.do?\"";
        String productUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200549\""; */       //200549	产品列表
        //产品策划-url
        String testMissionUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000031900573\"";        //		靶场试验策划报告表
        String tempmoduleHandleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000031900573\"";    //		靶场试验策划报告表

        List<String> missionDimensionTree = new ArrayList<String>();

        String missionNode = "{id:" + 0 + ","
                + "parentId:" + moduleId + ","
                + "name:\"" + "全部任务" + "\","
                + "tempUrl:" + missionUrl + ","
//				+"handleUrl:"+missionHandleUrl+","
                + "handleUrl:" + missionUrl + ","
                + "missionBuilderUrl:" + missionBuilderUrl + ","
                + "moduleId:" + moduleId + ","
                + "target : \"dataPackageFrame\","
                + "type: \"mission\" ,"
                + "open:true}";
        missionDimensionTree.add(missionNode);
        //获取靶场试验策划节点
//				List<Map<String,Object>> testPlans=testPlanDao.getApprovedPlansByModuleName(moduleName);
        List<Map<String, Object>> testPlans = testPlanService.getApprovedWeaponCheckPlansByModuleId(moduleId);
        for (Map<String, Object> planMap : testPlans) {
            // 任务策划报告表编号
            String planName = planMap.get("F_CHBGBBH") + "";
            String planNode = "{id:" + planMap.get("ID") + ", "
                    + "tempUrl:" + tempmoduleHandleUrl + ", "
                    + "handleUrl:" + testMissionUrl + ", "
                    + "parentId:0, "
                    + "name:\"" + planName + "\" ,"
                    + " type: \"testPlan\" , "
                    + "acceptancePlanBuilderUrl:" + acceptancePlanBuilderUrl + ", "
                    + "moduleId:" + moduleId + ", "
//							+ "moduleName:\"" + moduleName
//							+ "missionId:" + missionId+ ", "
//							+ "missionName:\"" + missionName+ "\","
//							+ "batchId:" + batchId + ", "
//							+ "batchKey:\"" + batchKey + "\", "
                    + "target : \"dataPackageFrame\","
                    + "open:true}";
            missionDimensionTree.add(planNode);
        }
        response.getWriter().print(JSONArray.fromObject(missionDimensionTree).toString());
    }
    /**
     * 这个是点击"全部型号"之后,靶场的型号表单就被替换成了产品验收的型号表单
     * 虽然两个型号是一样的,但是在型号页面有个上传数据包的按钮,产品验收和靶场试验对应的是不同的按钮
     * 所以要重写这个方法
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping({ "getModuleTreeData" })
    @ResponseBody
    @Action(description = "获取型号管理结构树")
    public void getModuleTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> productTree = new ArrayList<String>();
        String productUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000029830022\"";
        String projectUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000029830022\"";

        // 构建静态根节点
        String rootName = "全部型号";
        String rootNode = "{typeId:0" + ",dbomSql : \"F_TYPE='全部'\", parentId:-1"
                + ", typeName:\"" + rootName + "\" , tempUrl:" + productUrl + ", target : \"listFrame\",open:true}";
        productTree.add(rootNode);
        //权限
        ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
        String userId=String.valueOf(curUser.getUserId());
        List<Map<String, Object>> moduleList = moduleService.getAllModules();
        for (Map<String, Object> moduleMap : moduleList) {
            /*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
            List<String> userIdList=moduleManageService.getByModuleId(moduleMap.get("ID").toString());
            if(curUser.getRoles().indexOf("sjgly")<0) {
                if(!userIdList.contains(userId)) {
                    if(!userIdList.contains(userId)) {
                        continue;
                    }
                }
            }
            String typeId = CommonTools.Obj2String(moduleMap.get("ID"));
            String parentId = "0";
            String typeName = CommonTools.Obj2String(moduleMap.get("F_XHMC"));
            String typeKey = CommonTools.Obj2String(moduleMap.get("F_XHDH"));
//			String typeOfPart = CommonTools.Obj2String(moduleMap.get("F_SSMK"));
//			String node = "{typeId:" + typeId +",typeOfPart:"+"\""+typeOfPart+"\""+ ",dbomSql:'F_SSXH=" + typeId + "',parentId:" + parentId
            String node = "{typeId:" + typeId + ",dbomSql:'F_SSXH=" + typeId + "',parentId:" + parentId
                    + ", typeName:\"" + typeKey + "\" , tempUrl:" + projectUrl
                    + ", target : \"listFrame\",open:true}";
            productTree.add(node);
        }
        response.getWriter().print(JSONArray.fromObject(productTree).toString());
    }

}



