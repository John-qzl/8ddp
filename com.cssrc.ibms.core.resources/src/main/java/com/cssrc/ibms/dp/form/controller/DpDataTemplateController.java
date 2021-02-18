package com.cssrc.ibms.dp.form.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.ISolrService;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
@Controller
@RequestMapping("/dp/form/dpDataTemplate/")
@Action(ownermodel = SysAuditModelType.FORM_MANAGEMENT)
public class DpDataTemplateController  extends BaseController{
	@Resource
    private DataTemplateService dataTemplateService;
	@Resource
	ISolrService solrService;
	 /**
     * 删除数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@ResponseBody
    @RequestMapping("deleteData")
    @Action(description = "删除业务数据",execOrder = ActionExecOrder.BEFORE, 
    detail = "删除业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,__pk__)}", 
    exectype = SysAuditExecType.DELETE_TYPE,
    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public ResultMessage deleteData(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ResultMessage message = null;
        Long id = RequestUtil.getLong(request, "__displayId__");
        String pk = RequestUtil.getString(request, "__pk__");
       
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
        }
        return message;
    }
}
