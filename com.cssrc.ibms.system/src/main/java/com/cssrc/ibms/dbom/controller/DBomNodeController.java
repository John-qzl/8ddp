package com.cssrc.ibms.dbom.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dbom.model.DBomNode;
import com.cssrc.ibms.dbom.service.DBomService;
import com.cssrc.ibms.dbom.service.DbomMetaDataService;

/**
 * @ClassName: DBomNodeController
 * @Description: dbom tree 元数据 管理 controller
 * @author zxg
 * @date 2017年3月15日 上午11:34:33
 * 
 */
@Controller
@RequestMapping("/oa/system/dbomNode/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DBomNodeController extends BaseController
{
    @Resource
    private DBomService dBomService;
    
    @Resource
    DbomMetaDataService dbomMetaDataService;
    
    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("tree")
    @ResponseBody
    @Action(description = "获取DBom树节点数据")
    public List<Map<String, Object>> tree(HttpServletRequest request, HttpServletResponse response)
    {
        String pCode = RequestUtil.getString(request, "pCode");
        return dbomMetaDataService.getMetaDbomNodeTree(pCode);
    }
    
    /**
     * 获取dbom 节点 详细数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("get")
    @ResponseBody
    @Action(description = "获取DBom数据明细")
    public void get(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Long id = RequestUtil.getLong(request, "id");
            JSONObject object = dbomMetaDataService.getDbomNodeDetail(id);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(object.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 检查dbom code 是否已经存在
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("check")
    @ResponseBody
    @Action(description = "检查DBom节点代号是否已存在")
    public boolean check(HttpServletRequest request, HttpServletResponse response)
    {
        Long id = RequestUtil.getLong(request, "id");
        String code = RequestUtil.getString(request, "code");
        return dbomMetaDataService.check(id, code);
    }
    
    /**
     * 保存dbom node 信息
     * 
     * @param request
     * @param response
     * @param dbomNode
     */
    @RequestMapping("save")
    @ResponseBody
    @Action(description = "保存DBom节点信息")
    public void save(HttpServletRequest request, HttpServletResponse response, DBomNode dbomNode)
    {
        try
        {
            String dbomId = request.getParameter("dbomId");
            String dataSource = request.getParameter("hiddenDataSource");
            dbomNode.setDbomCode(dBomService.getById(Long.valueOf(dbomId)).getCode());
            dbomNode.setDataSource(dataSource);
            String nodeType = CommonTools.Obj2String(dbomNode.getNodeType());
            dbomNode.setNodeType("".equals(nodeType) ? "0" : nodeType);
            dbomMetaDataService.saveDbomNode(dbomNode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除dbom节点
     * 
     * @param request
     * @param response
     */
    @RequestMapping("delete")
    @ResponseBody
    @Action(description = "删除DBom节点及其子节点")
    public void delete(HttpServletRequest request, HttpServletResponse response)
    {
        
        try
        {
            Long id = RequestUtil.getLong(request, "ids");
            dbomMetaDataService.deleteById(id);
            ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "删除成功");
            writeResultMessage(response.getWriter(), resultMessage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取所有表，或者视图
     * 
     * @param request
     * @param response
     */
    @RequestMapping("getDataSource")
    @ResponseBody
    @Action(description = "获取数据源[包括数据类和数据视图]")
    public void getDataSource(HttpServletRequest request, HttpServletResponse response)
    {
        
        try
        {
            QueryFilter filter = new QueryFilter(request);
            String modelType = RequestUtil.getString(request, "modelType");
            if (!"数据视图".equals(modelType))
            {
                filter.addFilterForIB("isPublished", "1");
                filter.addFilterForIB("isMain", "1");
                filter.addFilterForIB("tableName", "%" + request.getParameter("tableName") + "%");
                filter.addFilterForIB("tableDesc", "%" + request.getParameter("tableDesc") + "%");
                filter.addFilterForIB("orderField", "UPPER(tableName)");
                filter.addFilterForIB("orderSeq", "ASC");
            }
            else
            {
                filter.addFilterForIB("alias", "%" + request.getParameter("tableName") + "%");
                filter.addFilterForIB("name", "%" + request.getParameter("tableDesc") + "%");
                filter.addFilterForIB("orderField", "UPPER(alias)");
                filter.addFilterForIB("orderSeq", "ASC");
            }
            String mainTableName = RequestUtil.getString(request, "mainTableName");
            String relationType = RequestUtil.getString(request, "relationType");
            Map<String, Object> dataMap = dbomMetaDataService.getTables(filter, mainTableName, relationType, modelType);
            response.getWriter().write(JSONObject.fromObject(dataMap).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 根据数据 源 name 获取 list
     * @param request
     * @param response
     */
    @RequestMapping("getDataSourceByName")
    @ResponseBody
    @Action(description="DBom节点关联关系-获取目标数据源下拉框选项数据")
    public void getDataSourceByName(HttpServletRequest request, HttpServletResponse response){
        try {
            String dataSource = RequestUtil.getString(request, "dataSource");
            List<JSONObject> result = dbomMetaDataService.getDataSourceByName(dataSource);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSONArray.toJSONString(result).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 获取数据源字段
     * 
     * @param request
     * @param response
     */
    @RequestMapping("getShowFiled")
    @ResponseBody
    public void getShowFiled(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            QueryFilter filter = new QueryFilter(request);
            Map<String, Object> dataMap = dbomMetaDataService.getShowFiled(request, filter);
            response.getWriter().write(JSONObject.fromObject(dataMap).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("getTargetDataRelation")
    @ResponseBody
    @Action(description = "DBom节点关联关系-初始化数据")
    public void getNodeRelation(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String id = RequestUtil.getString(request, "id");
            Map<String, Object> dataMap = dbomMetaDataService.getTargetDataRelation(id);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSONObject.fromObject(dataMap).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取点击URL列表，返回业业务表单模板LIST
     * 
     * @param request
     * @param response
     */
    @RequestMapping("getFormDefList")
    @ResponseBody
    public void getFormDefList(HttpServletRequest request, HttpServletResponse response)
    {
        
        try
        {
            QueryFilter filter = new QueryFilter(request);
            String dataSource = RequestUtil.getString(request, "dataSource");
            if (StringUtil.isEmpty(dataSource))
            {
                filter.addFilterForIB("isPublished", "1");
                filter.addFilterForIB("subject", "%" + request.getParameter("subject") + "%");
                filter.addFilterForIB("tableName", "%" + request.getParameter("tableName") + "%");
                filter.addFilterForIB("orderSeq", "ASC");
                
            }
            else if (dataSource.startsWith("W_"))
            {
                filter.addFilterForIB("isPublished", "1");
                filter.addFilterForIB("subject", "%" + request.getParameter("subject") + "%");
                filter.addFilterForIB("tableName", "%" + request.getParameter("tableName") + "%");
                filter.addFilterForIB("orderField", "UPPER(tableName)");
                filter.addFilterForIB("orderSeq", "ASC");
            }
            else
            {
                filter.addFilterForIB("name", "%" + request.getParameter("subject") + "%");
                filter.addFilterForIB("alias", "%" + request.getParameter("tableName") + "%");
                filter.addFilterForIB("orderField", "UPPER(alias)");
                filter.addFilterForIB("orderSeq", "ASC");
            }
            Map<String, Object> dataMap = dbomMetaDataService.getFormDefList(filter, dataSource);
            response.getWriter().write(JSONObject.fromObject(dataMap).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
