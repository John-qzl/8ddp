package com.cssrc.ibms.api.report.inf;

import com.cssrc.ibms.api.report.model.IReportTemplate;
import com.cssrc.ibms.core.util.result.ResultMessage;

public interface IReportTemplateService
{
    
    /**
     * 删除报表模板以及模板参数
     * 
     * @param lAryId
     * @return
     */
    public abstract ResultMessage delReportTemplates(Long[] lAryId);
    
    /**
     * 获取报表模板
     * 
     * @param title
     * @return
     */
    public abstract IReportTemplate getByTitle(String title);
    
    /**
     * 获取模板ID
     * 
     * @param id
     * @return
     */
    public abstract IReportTemplate getById(Long id);
    
    /**
     * 根据 displayId 解析出报表导出 功能的查询头html 内容
     * @param displayId datatemplate Id
     * @param filterKey 表单配置的过滤条件 key
     * @return
     */
    public String explainQueryHtml(String displayId,String filterKey);
    
    /**
     * 获取报表导出 功能所需的查询模板路径
     * @return
     */
    public abstract String getQueryHead();
    
}