package com.cssrc.ibms.core.flow.controller.pendingtask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.PendingTaskTemplateService;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/oa/flow/pendingSetting/")
public class PendingSettingController extends BaseController
{    
    private static Logger logger = Logger.getLogger(PendingSettingController.class);
    private static String category = "pendingSetting";

    @Resource
    DefinitionService definitionService;
    @Resource
    NodeSetService nodeSetService;
    @Resource
    IFormDefService formDefService;
    @Resource
    IFormTemplateService formTemplateService;
    @Resource
    PendingTaskTemplateService pendingTaskTemplateService;
    
    
    
    @RequestMapping("view")
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        ModelAndView mv=this.getView(category, "view");
        Long defId=RequestUtil.getLong(request, "defId");
        Definition definition=definitionService.getById(defId);
        NodeSet globalForm=nodeSetService.getBySetType(defId,NodeSet.SetType_GloabalForm);
        Long tableId=formDefService.getById(globalForm.getFormKey()).getTableId();
        String pendingSetting=definition.getPendingSetting();
  		
        String displayField="";
        String conditionField="";
        String templateAlias="";
        if(StringUtil.isNotEmpty(pendingSetting)){
            JSONObject json=JSON.parseObject(pendingSetting);
            displayField=json.get("displayField").toString();
            conditionField=json.get("conditionField").toString();
            templateAlias=json.get("templateAlias").toString();
            displayField=pendingTaskTemplateService.getDisplayField(tableId, displayField);
        }else{
            displayField=pendingTaskTemplateService.getDisplayField(tableId, null);
        }
        mv.addObject("displayField", displayField);
        mv.addObject("tableFields", pendingTaskTemplateService.getTableFields(tableId));
        mv.addObject("conditionField", conditionField);
        mv.addObject("templateAlias", templateAlias);
        mv.addObject("defId", defId);
        mv.addObject("tableId", tableId);
        mv.addObject("templates", formTemplateService.getTemplateType(IFormTemplate.PENDINGSETTING));
        
        return mv;
    }
    
    
    @RequestMapping("save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            Long defId=RequestUtil.getLong(request, "defId");
            Long tableId=RequestUtil.getLong(request, "tableId");
            String displayField=RequestUtil.getString(request, "displayField");
            String conditionField=RequestUtil.getString(request, "conditionField");
            String templateAlias=RequestUtil.getString(request, "templateAlias");
            JSONObject pendingSetting=new JSONObject();
            pendingSetting.put("displayField", displayField);
            pendingSetting.put("conditionField", conditionField);
            pendingSetting.put("templateAlias", templateAlias);
            pendingSetting.put("tableId", tableId);
            pendingSetting.put("defId", defId);
            definitionService.updatePendingSetting(defId,pendingSetting.toJSONString());
            this.writeResultMessage(response.getWriter(), "保存成功", ResultMessage.Success);
        }catch(Exception e){
            this.writeResultMessage(response.getWriter(), e.getMessage(), ResultMessage.Fail);
        }
        return null;
    }

    
 
    
    
 
}
