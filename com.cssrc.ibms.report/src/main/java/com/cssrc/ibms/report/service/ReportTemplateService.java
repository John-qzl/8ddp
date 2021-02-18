package com.cssrc.ibms.report.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.report.inf.IReportTemplateService;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.report.dao.ReportParamDao;
import com.cssrc.ibms.report.dao.ReportTemplateDao;
import com.cssrc.ibms.report.model.ReportTemplate;

import freemarker.template.TemplateException;

/**
 * <pre>
 * 对象功能:报表设计模板 Service类 
 * 开发人员:zxg
 * </pre>
 */
@Service("reportTemplateService")
public class ReportTemplateService extends BaseService<ReportTemplate> implements IReportTemplateService
{
    private static Logger logger = Logger.getLogger("IBMS.REPORT");
    
    @Resource
    private ReportTemplateDao reportTemplateDao;
    
    @Resource
    private ReportParamDao reportParamDao;
    
    @Resource
    private FreemarkEngine freemarkEngine;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    @Resource
    IFormTableService formTableService;
    
    @Override
    protected IEntityDao<ReportTemplate, Long> getEntityDao()
    {
        return reportTemplateDao;
    }
    
    public ResultMessage saveReportTemplate(ReportTemplate reportTemplate, String localPath, Date createTime)
    {
        try
        {
            createTime = (createTime == null ? new Date() : createTime);
            if (reportTemplate.getReportid() == null)
            {
                reportTemplate.setReportid(Long.valueOf(UniqueIdUtil.genId()));
                reportTemplate.setReportlocation(localPath);
                reportTemplate.setCreatetime(createTime);
                reportTemplate.setUpdatetime(createTime);
                add(reportTemplate);
                return new ResultMessage(ResultMessage.Success, "新增报表模板成功");
            }
            else
            {
                reportTemplate.setReportlocation(localPath);
                reportTemplate.setCreatetime(createTime);
                reportTemplate.setUpdatetime(new Date());
                update(reportTemplate);
                return new ResultMessage(ResultMessage.Success, "更新报表模板成功");
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 删除报表模板以及模板参数
     * 
     * @param lAryId
     * @return
     */
    public ResultMessage delReportTemplates(Long[] lAryId)
    {
        try
        {
            this.delByIds(lAryId);
            for (Long reportid : lAryId)
            {
                reportParamDao.delByReportId(reportid);
            }
            return new ResultMessage(ResultMessage.Success, "删除报表模板成功");
        }
        catch (Exception e)
        {
            logger.error(e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        
    }
    
    /**
     * 获取报表模板
     * 
     * @param title
     * @return
     */
    public ReportTemplate getByTitle(String title)
    {
        
        return reportTemplateDao.getByTitle(title);
    }
    
    /**
     * 保存报表模板，不需要上传附件
     * 
     * @param reportTemplate
     * @return
     */
    public ResultMessage saveReportTemplate(ReportTemplate reportTemplate)
    {
        try
        {
            reportTemplate.setReportid(Long.valueOf(UniqueIdUtil.genId()));
            reportTemplate.setCreatetime(new Date());
            reportTemplate.setUpdatetime(new Date());
            reportTemplate.setReporttype("FR");
            add(reportTemplate);
            return new ResultMessage(ResultMessage.Success, "新增报表模板成功");
        }
        catch (Exception e)
        {
            if (e instanceof DuplicateKeyException)
            {
                return new ResultMessage(ResultMessage.Fail, "报表标题重复");
            }
            else
            {
                return new ResultMessage(ResultMessage.Fail, e.getClass().getName());
            }
        }
        
    }
    
    public String explainQueryHtml(String displayId, String filterKey)
    {
        
        String templateHtml = FileOperator.readFile(getQueryHead());
        Map<String, Object> map = new HashMap<String, Object>();
        IDataTemplate bpmDataTemplate = dataTemplateService.getById(Long.valueOf(displayId));
        boolean hasCondition = false;
        if (!StringUtils.isEmpty(bpmDataTemplate.getConditionField()))
        {
            JSONArray jsonAry = JSONArray.fromObject(bpmDataTemplate.getConditionField());
            hasCondition = jsonAry.size() > 0 ? true : false;
        }
        map.put("hasCondition", hasCondition);
        map.put("bpmDataTemplate", bpmDataTemplate);
        
        String filter = dataTemplateService.getFilterSql(Long.valueOf(displayId), filterKey);
        map.put("formatData", dataTemplateService.getFormatDataMap(bpmDataTemplate.getTableId(), 1));
        map.put("param", new HashMap<>());
        if (StringUtil.isNotEmpty(filter))
        {
            map.put("filter", " and " + filter);
        }
        else
        {
            map.put("filter", "");
        }
        
        try
        {
            // System.out.println(templateHtml);
            String templateHtml1 = freemarkEngine.parseByStringTemplate(map, templateHtml);
            // System.out.println(templateHtml1);
            String templateHtml2 = freemarkEngine.parseByStringTemplate(map, templateHtml1);
            // System.out.println(templateHtml2);
            return templateHtml2;
        }
        catch (TemplateException | IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String getQueryHead()
    {
        return SysConfConstant.FTL_ROOT + "form" + File.separator + "queryHeade.ftl";
    }
    
}
