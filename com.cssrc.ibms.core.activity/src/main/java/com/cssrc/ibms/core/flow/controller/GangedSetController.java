package com.cssrc.ibms.core.flow.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.GangedSetService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;

/**
 * 
 * 对象功能:联动设置表 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/gangedSet/")
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class GangedSetController extends BaseController
{
    @Resource
    private GangedSetService gangedSetService;
    
    @Resource
    private DefinitionService definitionService;
    
    @Resource
    private NodeSetService nodeSetService;
    
    /**
     * 保存联动设置
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("save")
    @ResponseBody
    @Action(description = "保存联动设置", execOrder = ActionExecOrder.AFTER, detail = "保存流程定义${SysAuditLinkService.getDefinitionLink(Long.valueOf(defid))}联动设置")
    public String save(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String json = RequestUtil.getString(request, "json");
        Long defId = RequestUtil.getLong(request, "defid");
        try
        {
            gangedSetService.batchSave(defId, json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "{result:0,message:\"" + getText("controller.save.fail") + "\"}";
        }
        return "{result:1,message:\"" + getText("controller.save.success") + "\"}";
    }
    
    /**
     * 取得 GangedSet 实体 
     * @param request
     * @return
     * @throws Exception
     */
    protected GangedSet getFormObject(HttpServletRequest request)
        throws Exception
    {
        
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] {"yyyy-MM-dd"})));
        
        String json = RequestUtil.getString(request, "json");
        JSONObject obj = JSONObject.fromObject(json);
        
        GangedSet bpmGangedSet = (GangedSet)JSONObject.toBean(obj, GangedSet.class);
        
        return bpmGangedSet;
    }
    
    /**
     * 取得联动设置表分页列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @Action(description = "查看联动设置表分页列表")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long defId = RequestUtil.getLong(request, "defId");
        Definition bpmDefinition = definitionService.getById(defId);
        List<NodeSet> nodeList = nodeSetService.getByDefId(defId);
        NodeSet globalNodeSet = nodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm);
        nodeList.add(0, globalNodeSet);
        
        JSONArray jArray = (JSONArray)JSONArray.fromObject(nodeList);
        
        List<GangedSet> list = gangedSetService.getByDefId(defId);
        String s = getJsonFromList(list);
        ModelAndView mv = this.getAutoView()
            .addObject("bpmGangedSetList", s)
            .addObject("defid", defId)
            .addObject("nodes", jArray.toString())
            .addObject("bpmDefinition", bpmDefinition);
        
        return mv;
    }
    
    /**
     * 将GangedSet列表转为json格式的字符串
     * @param list
     * @return
     */
    protected String getJsonFromList(List<GangedSet> list)
    {
        JSONArray jarray = new JSONArray();
        for (GangedSet bpmGangedSet : list)
        {
            JSONObject setObject = new JSONObject().accumulate("id", bpmGangedSet.getId())
                .accumulate("nodename", bpmGangedSet.getNodename())
                .accumulate("changefield", (JSONArray)JSONArray.fromObject(bpmGangedSet.getChangefield()))
                .accumulate("choisefield", (JSONArray)JSONArray.fromObject(bpmGangedSet.getChoisefield()));
            jarray.add(setObject);
        }
        return jarray.toString();
    }
    
    /**
     * 获取字段
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getFields")
    @ResponseBody
    public String getFields(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long formKey = RequestUtil.getLong(request, "formKey");
        Integer needFilter = RequestUtil.getInt(request, "filter");
        
        String fields = gangedSetService.getFieldsByFormkey(formKey, needFilter == 1);
        return fields;
    }
    
    /**
     * 删除联动设置表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("del")
    @Action(description = "删除联动设置表")
    public void del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        try
        {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
            gangedSetService.delByIds(lAryId);
            message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
        }
        catch (Exception ex)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ex.getMessage());
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    /**
     * 	编辑联动设置表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("edit")
    @Action(description = "编辑联动设置表")
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "id");
        String returnUrl = RequestUtil.getPrePage(request);
        GangedSet bpmGangedSet = gangedSetService.getById(id);
        
        return getAutoView().addObject("bpmGangedSet", bpmGangedSet).addObject("returnUrl", returnUrl);
    }
    
    /**
     * 取得联动设置表明细
     * @param request   
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("get")
    @Action(description = "查看联动设置表明细")
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        long id = RequestUtil.getLong(request, "id");
        GangedSet bpmGangedSet = gangedSetService.getById(id);
        return getAutoView().addObject("bpmGangedSet", bpmGangedSet);
    }
}
