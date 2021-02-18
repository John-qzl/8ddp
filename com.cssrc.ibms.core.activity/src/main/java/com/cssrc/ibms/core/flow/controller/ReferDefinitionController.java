 package com.cssrc.ibms.core.flow.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ReferDefinition;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.ReferDefinitionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

 import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
 
 @Controller
 @RequestMapping({"/oa/flow/referDefinition/"})
 public class ReferDefinitionController extends BaseController
 {
 
   @Resource
   private ReferDefinitionService referDefinitionService;
 
   @Resource
   private ProcessRunService processRunService;

   @Resource
   private DefinitionService definitionService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新流程定义引用")
   public void save(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
        String resultMsg = null;
        ReferDefinition bpmReferDefinition = getFormObject(request);
     try {
          if ((bpmReferDefinition.getId() == null) || (bpmReferDefinition.getId().longValue() == 0L)) {
            bpmReferDefinition.setId(Long.valueOf(UniqueIdUtil.genId()));
            this.referDefinitionService.add(bpmReferDefinition);
            resultMsg = "添加流程定义引用成功";
       } else {
            this.referDefinitionService.update(bpmReferDefinition);
            resultMsg = "更新流程定义引用成功";
       }
          writeResultMessage(response.getWriter(), resultMsg, 1);
     } catch (Exception e) {
          writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
     }
   }
 
   protected ReferDefinition getFormObject(HttpServletRequest request)
     throws Exception
   {
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd" }));
 
       String json = RequestUtil.getString(request, "json");
       JSONObject obj = JSONObject.fromObject(json);
 
       ReferDefinition bpmReferDefinition = (ReferDefinition)JSONObject.toBean(obj, ReferDefinition.class);
 
       return bpmReferDefinition;
   }
 
   @RequestMapping({"list"})
   @Action(description="查看流程定义引用分页列表")
   public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
       List list = this.referDefinitionService.getAll(new QueryFilter(request, "bpmReferDefinitionItem"));
       ModelAndView mv = getAutoView().addObject("bpmReferDefinitionList", list);
 
       return mv;
   }
 
   @RequestMapping({"del"})
   @Action(description="删除流程定义引用")
   public void del(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
       String preUrl = RequestUtil.getPrePage(request);
       ResultMessage message = null;
     try {
         Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
         this.referDefinitionService.delByIds(lAryId);
         message = new ResultMessage(1, "删除流程定义引用成功!");
     } catch (Exception ex) {
         message = new ResultMessage(0, "删除失败" + ex.getMessage());
     }
       addMessage(message, request);
       response.sendRedirect(preUrl);
   }
 
   @RequestMapping({"edit"})
   @Action(description="编辑流程定义引用")
   public ModelAndView edit(HttpServletRequest request)
     throws Exception
   {
       Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
       String returnUrl = RequestUtil.getPrePage(request);
       ReferDefinition bpmReferDefinition = this.referDefinitionService.getById(id);
 
       return getAutoView().addObject("bpmReferDefinition", bpmReferDefinition).addObject("returnUrl", returnUrl);
   }
 
   @RequestMapping({"get"})
   @Action(description="查看流程定义引用明细")
   public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
       long id = RequestUtil.getLong(request, "id");
       ReferDefinition bpmReferDefinition = this.referDefinitionService.getById(Long.valueOf(id));
       return getAutoView().addObject("bpmReferDefinition", bpmReferDefinition); } 
     @RequestMapping({"getByDefId"})
   @ResponseBody
   @Action(description="")
   public Map<String, Object> getByDefId(HttpServletRequest request, HttpServletResponse response) throws Exception { Map map = new HashMap();
 
       Long defId = Long.valueOf(RequestUtil.getLong(request, "defId", 0L));
       if (defId.longValue() == 0L) {
         return map;
     }
 
       List refers = this.referDefinitionService.getByDefId(defId);
       map.put("refers", refers);
 
       return map; }
 
   @RequestMapping({"actInstDialog"})
   public ModelAndView actInstDialog(HttpServletRequest request, HttpServletResponse response) throws Exception
   {
       Long defId = Long.valueOf(RequestUtil.getLong(request, "defId", 0L));
       List refers = null;
       boolean hasRefer = false;
       if (defId.longValue() == 0L)
         refers = new ArrayList();
     else {
         refers = this.referDefinitionService.getByDefId(defId);
     }
       if ((refers != null) && (refers.size() > 0)) {
         hasRefer = true;
     }
       return getAutoView().addObject("refers", refers)
         .addObject("isSingle", Integer.valueOf(RequestUtil.getInt(request, "isSingle")))
         .addObject("hasRefer", Boolean.valueOf(hasRefer))
         .addObject("defId", defId);
   }
 
   @RequestMapping({"actInstSelector"})
   public ModelAndView actInstSelector(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
       boolean hasRefer = RequestUtil.getBoolean(request, "hasRefer", false);
       String referDefKey = RequestUtil.getString(request, "referDefKey");
       Long defId = Long.valueOf(RequestUtil.getLong(request, "defId"));
 
       List processRunList = null;
       QueryFilter filter = new QueryFilter(request, "processRunItem");
       filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());
       if (hasRefer) {
         if (StringUtil.isEmpty(referDefKey)) {
           List referDefKeyList = this.referDefinitionService.getDefKeysByDefId(defId);
           filter.addFilterForIB("referDefKeyList", referDefKeyList);
       } else {
           filter.addFilterForIB("referDefKey", referDefKey);
       }
         processRunList = this.processRunService.getMyFlowsAndCptoList(filter);
     }
     else {
         processRunList = this.processRunService.getMyCompletedAndCptoList(filter);
     }
       return getAutoView().addObject("processRunList", processRunList)
         .addObject("isSingle", Integer.valueOf(RequestUtil.getInt(request, "isSingle")))
         .addObject("hasRefer", Boolean.valueOf(hasRefer)).addObject("defId", defId);
   }
 }
