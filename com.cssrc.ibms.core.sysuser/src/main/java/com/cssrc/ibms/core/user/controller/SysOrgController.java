package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.login.model.OnlineUser;
import com.cssrc.ibms.core.user.listener.UserSessionListener;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgType;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.service.*;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.dbom.ITreeNode;
import com.cssrc.ibms.core.util.dbom.JsonTree;
import com.cssrc.ibms.core.util.dbom.TreeNode;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping({"/oa/system/sysOrg/"})
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysOrgController extends BaseController {
    @Resource
    Properties configproperties;
    /**
     * 组织:是逻辑删除,不会再出现(参数有变化isdelete=1)
     * 用户:是物理删除,不会再出现
     * 角色:是物理删除,不会再出现
     * 岗位:是逻辑删除,不会再出现(参数有变化isdelete=1)
     */
    @Resource
    private SysOrgService sysOrgService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private IDemensionService demensionService;
    @Resource
    private UserPositionService userPositionService;

    @Resource
    private SysOrgTypeService sysOrgTypeService;

    @Resource
    private SysOrgParamService sysOrgParamService;

    @Resource
    private OrgServiceImpl orgServiceImpl;

    /**
     * 获取维度列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"getDem"})
    @ResponseBody
    public List<IDemension> getDem(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        return this.demensionService.getAll();
    }

    @RequestMapping({"selector"})
    @Action(description = "组织对话框的展示", execOrder = ActionExecOrder.AFTER, detail = "获取组织对话框的展示", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView selector(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        QueryFilter filter = new QueryFilter(request, "sysOrgItem");
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId"));
        String orgName = RequestUtil.getString(request, "orgName");
        String type = RequestUtil.getString(request, "type");
        String typeVal = RequestUtil.getString(request, "typeVal");
        if (StringUtil.isNotEmpty(orgName)) {
            filter.addFilterForIB("orgName", "%" + orgName + "%");
        }
        if (demId.longValue() != 0L) {
            filter.addFilterForIB("demId", demId);
        }
        if (StringUtil.isNotEmpty(type)) {
            SysOrg sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
            filter.addFilterForIB("path", "%." + sysOrg.getOrgId() + ".%");
        }
       
        SysOrg org = (SysOrg) this.sysOrgService.getById(orgId);
        if (org != null) {
            filter.addFilterForIB("path", "%." + org.getOrgId() + ".%");
        }

        List<SysOrg> sysOrgList = this.sysOrgService.getAll(filter);
        
        for (SysOrg sysOrg : sysOrgList) { //过滤根目录 不嫩选择
			if(sysOrg.getOrgId()==1001L) {
				sysOrgList.remove(sysOrg);
				break;
			}
		}
        String isSingle = RequestUtil.getString(request, "isSingle", "false");
        ModelAndView mv = getAutoView().addObject("sysOrgList", sysOrgList)
                .addObject("type", type).addObject("typeVal", typeVal)
                .addObject("isSingle", isSingle);
        return mv;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping({"dialog"})
    @Action(description = "组织对话框", execOrder = ActionExecOrder.AFTER, detail = "获取组织对话框", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView dialog(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        List<IDemension> demensionList = this.demensionService.getAll();
        ModelAndView mv = getAutoView().addObject("demensionList", demensionList);
        return mv;
    }

    /**
     * 组织树
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping({"list"})
    @Action(description = "组织信息列表", execOrder = ActionExecOrder.AFTER, detail = "组织信息列表", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView list(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        List<IDemension> demensionList = this.demensionService.getAll();
        ISysUser currentUser = UserContextUtil.getCurrentUser();
        boolean isRight = false;
        if (currentUser.getUserId().equals(ISysUser.RIGHT_USER))
            isRight = true;
        boolean isImplemnet = false;
        if (currentUser.getUserId().equals(ISysUser.IMPLEMENT_USER))
            isImplemnet = true;
        ModelAndView mv = getAutoView().addObject("demensionList", demensionList)
                .addObject("isRight", isRight)
                .addObject("isImplemnet", isImplemnet);

        return mv;
    }

    @RequestMapping({"search"})
    public ModelAndView search(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        QueryFilter filter = new QueryFilter(request, "sysOrgItem");
        filter.getPagingBean().setPageSize(10);
        List<SysOrg> sysOrgList = this.sysOrgService.getAll(filter);
        ModelAndView mv = getAutoView().addObject("sysOrgList", sysOrgList);

        return mv;
    }

    @RequestMapping({"view"})
    @Action(description = "组织信息展示", execOrder = ActionExecOrder.AFTER, detail = "组织信息展示", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView view(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
        SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
        String path = "";
        String paramPath = "";
        if (sysOrg != null) {
            path = sysOrg.getPath();
            paramPath = path.replace(".", ",");
        }
        ModelAndView mv = getAutoView().addObject("orgId", orgId).addObject(
                "path", path).addObject("paramPath", paramPath);
        return mv;
    }

    /**
     * 组织详情
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"listById"})
    @Action(description = "组织信息查询", execOrder = ActionExecOrder.AFTER, detail = "组织信息查询", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView listById(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ModelAndView mv = getAutoView();
        mv.addObject("action", "global");
        return getListByOrgId(request, mv);
    }

    /**
     * 分级组织详情
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"listGradeById"})
    public ModelAndView listGradeById(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("action", "grade");
        mv.setViewName("/oa/system/sysOrgListById.jsp");
        return getListByOrgId(request, mv);
    }

    private ModelAndView getListByOrgId(HttpServletRequest request,
                                        ModelAndView mv) throws Exception {
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
        SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);

        if (sysOrg == null) {
            return mv.addObject("sysOrg", sysOrg);
        }
        String path = sysOrg.getPath();
        QueryFilter filter = new QueryFilter(request, "sysOrgItem");
        filter.getFilters().put("path",
                StringUtil.isNotEmpty(path) ? path + "%" : "");
        List list = this.sysOrgService.getOrgByOrgId(filter);
        return mv.addObject("sysOrgList", list).addObject("orgId", orgId)
                .addObject("sysOrg", Integer.valueOf(1));
    }

    /**
     * 组织架构编辑
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"edit"})
    @Action(description = "编辑组织架构", execOrder = ActionExecOrder.AFTER, detail = "编辑组织架构", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView edit(HttpServletRequest request) throws Exception {
        ModelAndView mv = getAutoView();
        mv.addObject("scope", "global");
        return getEditMv(request, mv);
    }

    /**
     * 还原被逻辑删除的组织架构
     *
     * @param request
     * @return
     * @throws Exception
     * @author liubo
     */
    @RequestMapping({"restore"})
    @Action(description = "还原被逻辑删除的组织架构", execOrder = ActionExecOrder.AFTER, detail = "还原被逻辑删除的组织架构:<#list orgId?split(\",\") as item><#assign entity=sysOrgService.getById(Long.valueOf(item))/>${entity.orgName}【${entity.code}】</#list>", exectype = SysAuditExecType.UPDATE_TYPE)
    public void restore(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultMessage message = null;
        //设置操作结果，默认为操作失败
        Short result = 0;
        try {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "orgId");
            for (int i = 0; i < lAryId.length; i++) {
                this.sysOrgService.restoreLogicByPath(lAryId[i]);
            }
            result = 1;
            message = new ResultMessage(1, "组织架构还原成功");
        } catch (Exception e) {
            message = new ResultMessage(0, "组织架构还原失败" + e.getMessage());
        }
        writeResultMessage(response.getWriter(), message);
        try {
            LogThreadLocalHolder.setResult(result);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 分级组织编辑方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"editGrade"})
    @Action(description = "编辑组织架构", execOrder = ActionExecOrder.AFTER, detail = "编辑组织架构", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView editGrade(HttpServletRequest request) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oa/system/sysOrgEdit.jsp");
        mv.addObject("scope", "grade");
        return getEditMv(request, mv);
    }

    /**
     * 组织编辑MV
     *
     * @param request
     * @param mv
     * @return
     */
    private ModelAndView getEditMv(HttpServletRequest request, ModelAndView mv) {
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId", 0L));
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
        String action = RequestUtil.getString(request, "action");
        SysOrg sysOrg = null;
        Long parentOrgId = Long.valueOf(0L);
        String parentCode = "";

        IDemension demension = (IDemension) this.demensionService.getById(demId);
        List sysOrgTypelist = this.sysOrgTypeService.getByDemId(demId
                .longValue());
        List returnSysOrgTypelist = new LinkedList();
        SysOrgType subSysOrgType = null;
        if ("add".equals(action)) {
            sysOrg = new SysOrg();
            SysOrg supSysOrg = (SysOrg) this.sysOrgService.getById(orgId);

            if (supSysOrg == null) {
                sysOrg.setOrgSupId(demId);
                returnSysOrgTypelist = sysOrgTypelist;
            } else {
                supSysOrg = (SysOrg) this.sysOrgService.getById(orgId);
                parentCode = supSysOrg.getCode();
                sysOrg.setOrgSupId(supSysOrg.getOrgId());
                sysOrg.setOrgSupName(supSysOrg.getOrgName());
                Long supSysOrgId = supSysOrg.getOrgType();
                if (supSysOrgId != null)
                    subSysOrgType = (SysOrgType) this.sysOrgTypeService
                            .getById(supSysOrg.getOrgType());
            }
        } else {
            sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
            SysOrg charge = this.userPositionService.getChargeNameByOrgId(orgId);
            sysOrg.setOwnUser(charge.getOwnUser());
            sysOrg.setOwnUserName(charge.getOwnUserName());
            parentOrgId = sysOrg.getOrgSupId();
            //找到分管领导以及分管副领导
            List<? extends ISysUser> leaders = this.sysUserService.getByIdSet(sysOrg.getLeader());
            List<? extends ISysUser> viceLeaders = this.sysUserService.getByIdSet(sysOrg.getViceLeader());
            String leaderNames = "";
            String viceLeaderNames = "";
            for (ISysUser user : leaders) {
                leaderNames += user.getFullname() + ",";
            }
            for (ISysUser user : viceLeaders) {
                viceLeaderNames += user.getFullname() + ",";
            }
            if (StringUtil.isNotEmpty(leaderNames)) {
                leaderNames = leaderNames.substring(0, leaderNames.length() - 1);
            }
            if (StringUtil.isNotEmpty(viceLeaderNames)) {
                viceLeaderNames = viceLeaderNames.substring(0, viceLeaderNames.length() - 1);
            }
            mv.addObject("viceLeaderNames", viceLeaderNames);
            mv.addObject("leaderNames", leaderNames);

            if (sysOrg.getOrgType() != null) {
                subSysOrgType = (SysOrgType) this.sysOrgTypeService
                        .getById(sysOrg.getOrgType());
            }
            if (subSysOrgType == null) {
                SysOrg parentOrg = this.sysOrgService.getParentWithType(sysOrg);
                if (parentOrg != null)
                    subSysOrgType = (SysOrgType) this.sysOrgTypeService
                            .getById(parentOrg.getOrgType());
                else
                    returnSysOrgTypelist = sysOrgTypelist;
            }
        }
        if ((subSysOrgType != null) && (!parentOrgId.equals(Long.valueOf(1L)))) {
            for (int i = 0; i < sysOrgTypelist.size(); i++)
                if (subSysOrgType.getLevels().longValue() <= ((SysOrgType) sysOrgTypelist
                        .get(i)).getLevels().longValue())
                    returnSysOrgTypelist
                            .add((SysOrgType) sysOrgTypelist.get(i));
        } else if (parentOrgId.equals(Long.valueOf(1L))) {
            returnSysOrgTypelist = sysOrgTypelist;
        }

        return mv.addObject("sysOrg", sysOrg).addObject("demension", demension)
                .addObject("action", action).addObject("sysOrgTypelist", returnSysOrgTypelist)
                .addObject("parentCode", parentCode).addObject("orgId", orgId);
    }

    @RequestMapping({"orgdel"})
    @Action(description = "物理删除组织", execOrder = ActionExecOrder.BEFORE, detail = "物理删除组织:<#list orgId?split(\",\") as item><#assign entity=sysOrgService.getById(Long.valueOf(item))/>${entity.orgName}【${entity.code}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
    public void orgdel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultMessage message = null;
        try {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "orgId");
            for (int i = 0; i < lAryId.length; i++) {
                this.sysOrgService.delById(lAryId[i]);
            }
            message = new ResultMessage(1, "物理删除组织及其所有子组织成功");
        } catch (Exception e) {
            message = new ResultMessage(0, "物理删除组织及其所有子组织失败" + e.getMessage());
        }
        writeResultMessage(response.getWriter(), message);
    }

    @RequestMapping({"orgLogicdel"})
    @Action(description = "逻辑删除组织", execOrder = ActionExecOrder.BEFORE, detail = "逻辑删除组织:<#list orgId?split(\",\") as item><#assign entity=sysOrgService.getById(Long.valueOf(item))/>${entity.orgName}【${entity.code}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
    public void orgLogicdel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultMessage message = null;
        try {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "orgId");
            for (int i = 0; i < lAryId.length; i++) {
                this.sysOrgService.delLogicById(lAryId[i]);
            }
            message = new ResultMessage(1, "逻辑删除组织及其所有子组织成功");
        } catch (Exception e) {
            message = new ResultMessage(0, "逻辑删除组织及其所有子组织失败" + e.getMessage());
        }
        writeResultMessage(response.getWriter(), message);
    }

    @RequestMapping({"get"})
    public ModelAndView get(HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        ModelAndView mv = getAutoView();
        mv.addObject("action", "global")
                .addObject("isOtherLink", Integer.valueOf(0));
        return getByOrgId(request, mv);
    }

    @RequestMapping({"getEmpty"})
    public ModelAndView getEmpty(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        long isDelete = RequestUtil.getLong(request, "isDelete");
        boolean isShow = false;
        if (isDelete == 1) {
            isShow = true;
        }
        return getAutoView().addObject("isShow", isShow);
    }

    @Action(description = "获取组织信息", execOrder = ActionExecOrder.AFTER, detail = "获取组织信息", exectype = SysAuditExecType.SELECT_TYPE)
    private ModelAndView getByOrgId(HttpServletRequest request, ModelAndView mv)
            throws Exception {
        long orgId = RequestUtil.getLong(request, "orgId");
        List sysOrgTypelist = null;
        String ownerName = "";
        SysOrg sysOrg = (SysOrg) this.sysOrgService
                .getById(Long.valueOf(orgId));
        if (sysOrg != null) {
            SysOrg charge = this.userPositionService.getChargeNameByOrgId(Long
                    .valueOf(orgId));
            Long demId = sysOrg.getDemId();
            sysOrgTypelist = this.sysOrgTypeService.getByDemId(demId
                    .longValue());
            if (sysOrg.getDemId().longValue() != 0L) {
                sysOrg.setDemName(((IDemension) this.demensionService
                        .getById(demId)).getDemName());
                ownerName = charge.getOwnUserName();
            }

        }

        return mv.addObject("sysOrg", sysOrg).addObject("userNameCharge",
                ownerName).addObject("orgId", Long.valueOf(orgId)).addObject(
                "sysOrgTypelist", sysOrgTypelist);
    }

    @RequestMapping({"getGrade"})
    public ModelAndView getGrade(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.addObject("action", "grade")
                .addObject("isOtherLink", Integer.valueOf(0));
        mv.setViewName("/oa/system/sysOrgGet.jsp");
        return getByOrgId(request, mv);
    }

    @RequestMapping({"getByLink"})
    public ModelAndView getByLink(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oa/system/sysOrgGet.jsp");
        mv.addObject("action", "grade").addObject("isOtherLink",
                Integer.valueOf(1));
        return getByOrgId(request, mv);
    }

    @RequestMapping({"type"})
    @Action(description = "获取组织类型", execOrder = ActionExecOrder.AFTER, detail = "获取组织类型", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView type(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String flag = RequestUtil.getString(request, "flag");
        long orgId = RequestUtil.getLong(request, "orgId");
        String userNameStr = "";
        String userNameCharge = "";

        List<UserPosition> userlist = this.userPositionService.getByOrgId(Long
                .valueOf(orgId));
        for (UserPosition userPosition : userlist) {
            if (userNameStr.isEmpty())
                userNameStr = userPosition.getFullname();
            else {
                userNameStr = userNameStr + "," + userPosition.getFullname();
            }
            String isCharge = "";
            if (BeanUtils.isNotEmpty(userPosition.getIsCharge())) {
                isCharge = userPosition.getIsCharge().toString();
            }

            if (UserPosition.CHARRGE_YES.equals(isCharge)) {
                if (userNameCharge.isEmpty())
                    userNameCharge = userPosition.getFullname().toString();
                else {
                    userNameCharge = userNameCharge + ","
                            + userPosition.getFullname();
                }
            }
        }
        SysOrg po = (SysOrg) this.sysOrgService.getById(Long.valueOf(orgId));
        return getAutoView().addObject("sysOrg", po).addObject("userNameStr",
                userNameStr).addObject("userNameCharge", userNameCharge)
                .addObject("orgId", Long.valueOf(orgId))
                .addObject("flag", flag);
    }

    private SysOrg getRootSysOrg(Long demId, String orgName) throws Exception {
        SysOrg org = new SysOrg();
        org.setOrgId(demId);
        org.setOrgSupId(Long.valueOf(0L));
        org.setPath(demId.toString());
        org.setDemId(demId);
        org.setIsRoot(Short.valueOf((short) 1));
        org.setOrgName(orgName);
        org.setOrgPathname(orgName);
        return org;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping({"getTreeData"})
    @ResponseBody
    @Action(description = "获取组织树结构", execOrder = ActionExecOrder.AFTER, detail = "获取组织树结构", exectype = SysAuditExecType.SELECT_TYPE)
    public List<SysOrg> getTreeData(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId", 0L));

        String type = RequestUtil.getString(request, "type");
        String typeVal = RequestUtil.getString(request, "typeVal");
        String relvalue = RequestUtil.getString(request, "relvalue");

        List<IDemension> demens = null;
        long orgId = RequestUtil.getLong(request, "orgId", 0L);
        List<SysOrg> orgList = new ArrayList<SysOrg>();
        if (orgId == 0L) {
            if (demId.longValue() != 0L) {
                demens = new ArrayList<IDemension>();
                if (StringUtil.isNotEmpty(relvalue)) {
                    if(relvalue.equals("current")) {
                    	orgId=UserContextUtil.getCurrentOrgId();
                    }
                    SysOrg sysOrg = this.sysOrgService.getById(orgId);
                    orgList.add(sysOrg);
                } else if ((demId.longValue() == 1L) && (StringUtil.isNotEmpty(type))) {
                    SysOrg sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
                    orgList = this.sysOrgService.getOrgByOrgSupId(sysOrg.getOrgId());
                    sysOrg.setOrgPathname(sysOrg.getOrgName());
                    orgList.add(sysOrg);
                } else {
                    demens.add((IDemension) this.demensionService.getById(demId));
                    orgList = this.sysOrgService.getOrgByOrgSupIdAndLevel(demId);
                }
            } else {
                demens = this.demensionService.getAll();
                if (demens.size() > 0) {
                    for (int i = 0; i < demens.size(); i++) {
                        orgList.addAll(this.sysOrgService.getOrgByOrgSupIdAndLevel(((IDemension) demens.get(i)).getDemId()));
                    }
                }
            }

            for (IDemension demension : demens)
                orgList.add(getRootSysOrg(demension.getDemId(), demension.getDemName()));
        } else {
            orgList = this.sysOrgService.getOrgByOrgSupId(Long.valueOf(orgId));
        }
        for (SysOrg sysOrg : orgList) {
            SysOrg charge = this.userPositionService.getChargeNameByOrgId(sysOrg.getOrgId());
            sysOrg.setOwnUser(charge.getOwnUser());
            sysOrg.setOwnUserName(charge.getOwnUserName());
            if (sysOrg.getOrgType() == null)
                continue;
            SysOrgType tempSysOrgType = (SysOrgType) this.sysOrgTypeService.getById(sysOrg.getOrgType());
            if (tempSysOrgType == null)
                continue;
            if (tempSysOrgType.getIcon() == null)
                continue;
            sysOrg.setIconPath(tempSysOrgType.getIcon());
        }
        return orgList;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping({"getTreeDataManage"})
    @ResponseBody
    @Action(description = "获取组织管理界面的组织树结点", execOrder = ActionExecOrder.AFTER, detail = "获取组织管理界面的组织树结点", exectype = SysAuditExecType.SELECT_TYPE)
    public List<SysOrg> getTreeDataManage(HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId", 0L));

        String type = RequestUtil.getString(request, "type");
        String typeVal = RequestUtil.getString(request, "typeVal");

        List<IDemension> demens = null;
        long orgId = RequestUtil.getLong(request, "orgId", 0L);
        List<SysOrg> orgList = new ArrayList<SysOrg>();
        if (orgId == 0L) {
            if (demId.longValue() != 0L) {
                demens = new ArrayList<IDemension>();
//                if ((demId.longValue() == 1L) && (StringUtil.isNotEmpty(type))) {
//                    SysOrg sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
//                    orgList = this.sysOrgService.getOrgManageByOrgSupId(sysOrg.getOrgId());
//                    sysOrg.setOrgPathname(sysOrg.getOrgName());
//                    orgList.add(sysOrg);
//                } else {
//                    demens.add((IDemension) this.demensionService.getById(demId));
//                    orgList = this.sysOrgService.getOrgByOrgSupIdAndLevel(demId);
//                }
                // 添加组织根节点
                SysOrg rootOrg = this.sysOrgService.getByCode("root");
                if (rootOrg == null) {
                    rootOrg = new SysOrg();
                    rootOrg.setOrgId(1L);
                    rootOrg.setDemId(1L);
                    rootOrg.setOrgName("xx公司");
                    rootOrg.setCode("root");
                    rootOrg.setIsParent("1");
                    rootOrg.setPath(".1.");
                    rootOrg.setOrgSupId(0L);
                    this.sysOrgService.addOrg(rootOrg);
                }
                rootOrg.setIsRoot((short) 1);
//                demens.add((IDemension) this.demensionService.getById(demId));
                orgList = this.sysOrgService.getOrgByOrgSupIdAndLevel(demId);
                orgList.add(rootOrg);
            } else {
                demens = this.demensionService.getAll();
                if (demens.size() > 0) {
                    for (int i = 0; i < demens.size(); i++) {
                        orgList.addAll(this.sysOrgService.getOrgByOrgSupIdAndLevel(((IDemension) demens.get(i)).getDemId()));
                    }
                }
            }

//            for (IDemension demension : demens)
//                orgList.add(getRootSysOrg(demension.getDemId(), demension.getDemName()));
        } else {
            orgList = this.sysOrgService.getOrgManageByOrgSupId(Long.valueOf(orgId));
        }
        for (SysOrg sysOrg : orgList) {
            SysOrg charge = this.userPositionService.getChargeNameByOrgId(sysOrg.getOrgId());
            sysOrg.setOwnUser(charge.getOwnUser());
            sysOrg.setOwnUserName(charge.getOwnUserName());
            if (sysOrg.getOrgType() == null)
                continue;
            SysOrgType tempSysOrgType = (SysOrgType) this.sysOrgTypeService.getById(sysOrg.getOrgType());
            if (tempSysOrgType == null)
                continue;
            if (tempSysOrgType.getIcon() == null)
                continue;
            sysOrg.setIconPath(tempSysOrgType.getIcon());
        }
        //将已逻辑删除的组织用*标识
        List<SysOrg> list = orgList;
        for (SysOrg sysOrg : list) {
            String orgName = sysOrg.getOrgName();
            if (sysOrg.getIsDelete() != null && sysOrg.getIsDelete() == 1) {
                sysOrg.setOrgName(orgName + "*");
            }
        }
        return list;
    }

    @RequestMapping({"getPosTreeData"})
    @ResponseBody
    @Action(description = "获取组织树结构", execOrder = ActionExecOrder.AFTER, detail = "获取组织树结构", exectype = SysAuditExecType.SELECT_TYPE)
    public List<SysOrg> getPosTreeData(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId", 0L));

        List<IDemension> demens = null;
        long orgId = RequestUtil.getLong(request, "orgId", 0L);

        List<SysOrg> orgList = new ArrayList();
        if (orgId == 0L) {
            if (demId.longValue() != 0L) {
                demens = new ArrayList();
                demens.add((IDemension) this.demensionService.getById(demId));
                orgList = this.sysOrgService.getOrgByOrgSupIdAndLevel(demId);
            } else {
                demens = this.demensionService.getAll();
                if (demens.size() > 0) {
                    for (int i = 0; i < demens.size(); i++) {
                        orgList.addAll(this.sysOrgService
                                .getOrgByOrgSupIdAndLevel(((IDemension) demens
                                        .get(i)).getDemId()));
                    }
                }
            }

            for (IDemension demension : demens)
                orgList.add(getRootSysOrg(demension.getDemId(), demension
                        .getDemName()));
        } else {
            orgList = this.sysOrgService.getOrgByOrgSupId(Long.valueOf(orgId));
        }

        for (SysOrg sysOrg : orgList) {
            SysOrg charge = this.userPositionService.getChargeNameByOrgId(sysOrg
                    .getOrgId());
            sysOrg.setOwnUser(charge.getOwnUser());
            sysOrg.setOwnUserName(charge.getOwnUserName());
            if (sysOrg.getOrgType() == null)
                continue;
            SysOrgType tempSysOrgType = (SysOrgType) this.sysOrgTypeService
                    .getById(sysOrg.getOrgType());
            if (tempSysOrgType == null)
                continue;
            if (tempSysOrgType.getIcon() == null)
                continue;
            sysOrg.setIconPath(tempSysOrgType.getIcon());
        }
        return orgList;
    }

    @RequestMapping({"getTreeOnlineData"})
    @ResponseBody
    @Action(description = "获取在线组织树结构", execOrder = ActionExecOrder.AFTER, detail = "获取在线组织树结构", exectype = SysAuditExecType.SELECT_TYPE)
    public List<SysOrg> getTreeOnlineData(HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        String type = RequestUtil.getString(request, "type");
        String typeVal = RequestUtil.getString(request, "typeVal");
        Map<Long, OnlineUser> onlineUsers = UserSessionListener
                .getOnLineUsers();
        List<OnlineUser> onlineList = new ArrayList();
        for (Long userId : onlineUsers.keySet()) {
            OnlineUser onlineUser = (OnlineUser) onlineUsers.get(userId);
            SysOrg sysOrg = this.sysOrgService.getPrimaryOrgByUserId(onlineUser
                    .getUserId());
            if (sysOrg != null) {
                onlineUser.setOrgId(sysOrg.getOrgId());
                onlineUser.setOrgName(sysOrg.getOrgName());
            }
            onlineList.add(onlineUser);
        }
        String depTreeRootId = this.configproperties
                .getProperty("depTreeRootId");
        Long demId = Long.valueOf(RequestUtil.getLong(request, "demId", 0L));
        IDemension demension = (IDemension) this.demensionService.getById(demId);
        Map<Long, SysOrg> orgMap = this.sysOrgService.getOrgMapByDemId(demId);
        orgMap.put(Long.valueOf(0L), getRootSysOrg(Long.valueOf(-1L), "未分配组织"));
        SysOrg sysOrg;
        for (OnlineUser onlineUser : onlineList) {
            Long onlineUserId = onlineUser.getOrgId();
            sysOrg = (SysOrg) orgMap.get(onlineUserId);
            if (sysOrg != null) {
                int onlineNum = sysOrg.getOnlineNum().intValue();
                ((SysOrg) orgMap.get(onlineUserId)).setOnlineNum(Integer
                        .valueOf(onlineNum + 1));

                SysOrg pso = (SysOrg) orgMap.get(sysOrg.getOrgSupId());
                while (pso != null) {
                    pso.setOnlineNum(Integer.valueOf(pso.getOnlineNum()
                            .intValue() + 1));
                    pso = (SysOrg) orgMap.get(pso.getOrgSupId());
                }
            } else {
                ((SysOrg) orgMap.get(Long.valueOf(0L))).setOnlineNum(Integer
                        .valueOf(((SysOrg) orgMap.get(Long.valueOf(0L)))
                                .getOnlineNum().intValue() + 1));
            }
        }
        List orgList = new ArrayList();
        List<SysOrg> orgListTemp = new ArrayList();
        for (SysOrg sysOrg1 : orgMap.values()) {
            String newName = sysOrg1.getOrgName() + "("
                    + sysOrg1.getOnlineNum() + ")";
            sysOrg1.setOrgName(newName);
            ((List) orgListTemp).add(sysOrg1);
        }

        if (demId.longValue() == 1L) {
            sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
            String path = sysOrg.getPath();
            List<SysOrg> orgList1 = this.sysOrgService.getByOrgPath(path + "%");
            for (SysOrg sysOrg2 : orgListTemp) {
                for (SysOrg sysOrg3 : orgList1) {
                    if (sysOrg2.getOrgId().longValue() == sysOrg3.getOrgId()
                            .longValue()) {
                        orgList.add(sysOrg2);
                    }
                }
                if ((sysOrg2.getDemId().longValue() == -1L)
                        && (sysOrg.getOrgId().longValue() == 1L))
                    orgList.add(sysOrg2);
            }
        } else {
            orgList = (List) orgListTemp;
        }

        if (!StringUtils.isEmpty(depTreeRootId)) {
            SysOrg org = getRootSysOrg(demId, "全部");
            if (demension != null)
                org.setOrgName(demension.getDemName());
            orgList.add(org);
        }
        return (List<SysOrg>) orgList;
    }

    @RequestMapping({"move"})
    @Action(description = "转移分类", execOrder = ActionExecOrder.AFTER, detail = "转移分类", exectype = SysAuditExecType.UPDATE_TYPE)
    public void move(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultMessage resultMessage = null;
        PrintWriter out = response.getWriter();
        long targetId = RequestUtil.getLong(request, "targetId", 0L);
        long dragId = RequestUtil.getLong(request, "dragId", 0L);
        String moveType = RequestUtil.getString(request, "moveType");
        //设置操作结果，默认为操作失败
        Short result = 0;
        try {
            this.sysOrgService.move(Long.valueOf(targetId), Long
                    .valueOf(dragId), moveType);

            result = 1;
            resultMessage = new ResultMessage(1, "转移分类完成");
        } catch (Exception ex) {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str)) {
                resultMessage = new ResultMessage(0, "转移分类失败:" + str);
                response.getWriter().print(resultMessage);
            } else {
                String message = ExceptionUtil.getExceptionMessage(ex);
                resultMessage = new ResultMessage(0, message);
                response.getWriter().print(resultMessage);
            }
        }
        out.print(resultMessage);
        try {
            LogThreadLocalHolder.setResult(result);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @RequestMapping({"paramList"})
    @Action(description = "取得组织参数表", execOrder = ActionExecOrder.AFTER, detail = "取得组织参数表", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView paramList(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        ModelAndView mv = getAutoView();
        mv.addObject("scope", "global");
        return getParamMv(request, mv);
    }

    @RequestMapping({"paramGradeList"})
    @Action(description = "取得组织参数表", execOrder = ActionExecOrder.AFTER, detail = "取得组织参数表", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView paramGradeList(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oa/system/sysOrgParamList.jsp");
        mv.addObject("scope", "grade");
        return getParamMv(request, mv);
    }

    @RequestMapping({"sortList"})
    @Action(description = "组织树排序列表", execOrder = ActionExecOrder.AFTER, detail = "组织树排序列表", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView sortList(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId", -1L));
        List<SysOrg> list = new ArrayList<SysOrg>();
        List<SysOrg> SysOrgList = new ArrayList<SysOrg>();
        SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
        int SysOrgLength;
        if (sysOrg != null) {
            list = this.sysOrgService.getByOrgPath(sysOrg.getPath());
            if (list.size() > 0) {
                for (SysOrg SysOrg : list) {
                    SysOrgLength = SysOrg.getPath().split("\\.").length;
                    int sysOrgLength = sysOrg.getPath().split("\\.").length + 1;
                    if (SysOrgLength == sysOrgLength)
                        SysOrgList.add(SysOrg);
                }
            }
        } else {
            Long demId = Long
                    .valueOf(RequestUtil.getLong(request, "demId", 0L));
            list = this.sysOrgService.getOrgsByDemIdOrAll(demId);
            if (list.size() > 0) {
                for (SysOrg SysOrg : list) {
                    SysOrgLength = SysOrg.getPath().split("\\.").length;
                    if (SysOrgLength == 2) {
                        SysOrgList.add(SysOrg);
                    }
                }
            }
        }
        return getAutoView().addObject("SysOrgList", SysOrgList);
    }

    @RequestMapping({"sort"})
    @Action(description = "组织树排序", execOrder = ActionExecOrder.AFTER, detail = "组织树排序", exectype = SysAuditExecType.UPDATE_TYPE)
    public void sort(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ResultMessage resultObj = null;
        PrintWriter out = response.getWriter();
        Long[] lAryId = RequestUtil.getLongAryByStr(request, "orgIds");
        if (BeanUtils.isNotEmpty(lAryId)) {
            for (int i = 0; i < lAryId.length; i++) {
                Long orgId = lAryId[i];
                long sn = i + 1;
                this.sysOrgService.updSn(orgId, sn);
            }
        }
        resultObj = new ResultMessage(1, "排序分类完成");
        out.print(resultObj);
    }

    private ModelAndView getParamMv(HttpServletRequest request, ModelAndView mv) {
        Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
        if (orgId.longValue() == 0L) {
            mv.addObject("sysOrg", null);
        } else {
            SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
            List list = this.sysOrgParamService.getListByOrgId(orgId);
            mv.addObject("userParamList", list).addObject("orgId", orgId)
                    .addObject("sysOrg", sysOrg);
        }
        return mv;
    }

    @RequestMapping({"updCompany"})
    @Action(description = "更新组织分公司", execOrder = ActionExecOrder.AFTER, detail = "更新组织分公司", exectype = SysAuditExecType.UPDATE_TYPE)
    public void updCompany(HttpServletResponse response) throws IOException {
        ResultMessage resultObj = new ResultMessage(1, "更新组织分公司成功");
        PrintWriter out = response.getWriter();
        try {
            this.sysOrgService.updCompany();
        } catch (Exception ex) {
            resultObj = new ResultMessage(0, "更新组织分公司失败");
        }
        out.print(resultObj);
    }

    /**
     * 暂时供ext使用
     * 初始化组织以及组织人员树
     **/
    @RequestMapping("orgUserTree")
    @ResponseBody
    @Action(description = "组织查看", execOrder = ActionExecOrder.AFTER, detail = "【${loginName}】查看组织", exectype = SysAuditExecType.SELECT_TYPE)
    public List<TreeNode> orgUserTree(HttpServletRequest request) throws Exception {
        Long demId = RequestUtil.getLong(request, "demId", 1L);
        List list = sysOrgService.getOrgsByDemIdOrAll(demId);
        List ls = new ArrayList();
        for (Object obj : list) {
            SysOrg org = (SysOrg) obj;
            SysOrg sysOrg = sysOrgService.getById(org.getOrgId());
            List ls2 = new ArrayList();
            ls2.add(sysOrg);
            if (null != ls2 && ls2.size() > 0) {
                ls.addAll(ls2);
            }
        }
        list.addAll(ls);
        TreeNode node = JsonTree.beansToTree((List<ITreeNode>) list, true);
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(node);
        return nodes;
    }

    /**
     * 通过前台传过来的组织输入框的值对组织进行模糊查询
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping({"getFuzzySysOrg"})
    public void getFuzzySysOrg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();

        String orgJson = "";

        String orgFuzzyName = RequestUtil.getString(request, "fuzzyName");
        String fieldName = RequestUtil.getString(request, "fieldName");

        String type = RequestUtil.getString(request, "type");
        String typeVal = RequestUtil.getString(request, "typeVal");
        String relvalue = RequestUtil.getString(request, "relvalue");

        if (fieldName == "") {
            //若没有指定字段名称，默认为orgName
            fieldName = "orgName";
        }

        try {
            JSONArray userArr = this.sysOrgService.getFuzzySysOrgList(orgFuzzyName, fieldName, relvalue, type, typeVal);
            orgJson = userArr.toString();

            ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, orgJson);
            resultMessage.addData("type", "org");
            out.print(resultMessage);
        } catch (Exception e) {
            // TODO: handle exception
            ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, e.getMessage());
            out.print(resultMessage);
        }

    }
}