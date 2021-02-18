package com.cssrc.ibms.dp.template.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.util.FormUtils;
import com.cssrc.ibms.dp.template.constant.TemplateConstant;
import com.cssrc.ibms.dp.template.dao.TemplateCheckConditionDao;
import com.cssrc.ibms.dp.template.dao.TemplateCheckItemDao;
import com.cssrc.ibms.dp.template.dao.TemplateDao;
import com.cssrc.ibms.dp.template.dao.TemplateInstanceDao;
import com.cssrc.ibms.dp.template.dao.TemplateSignDao;
import com.cssrc.ibms.dp.template.model.Template;
import com.cssrc.ibms.dp.template.model.TemplateCheckCondition;
import com.cssrc.ibms.dp.template.model.TemplateCheckItem;
import com.cssrc.ibms.dp.template.model.TemplateSign;

/**
 * @description 模板管理业务解析类
 * @author xie chen
 * @date 2019年12月3日 下午3:58:33
 * @version V1.0
 */
@Service
public class TemplateService {
    
	@Resource
	private TemplateDao dao;
	@Resource
	private TemplateCheckConditionDao templateCheckConditionDao;
	@Resource
	private TemplateSignDao templateSignDao;
	@Resource
	private TemplateCheckItemDao templateCheckItemDao;
	@Resource
	private TemplateInstanceDao templateInstanceDao;
	
	/**
	 * @Desc 获取型号下所有通用模板
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getTemplatesByModuleId(String moduleId) {
        return dao.getTemplatesByModuleId(moduleId);
    }
	
	public Template getById(Long templateId) {
		return dao.getById(templateId);
	}
	
    /**
     * @Desc 模板编号唯一性校验
     * @param templateCode
     * @param templateId
     * @return
     */
    public Map<String, Object> templateCodeCheck(String templateCode, String templateId) {
        Map<String, Object> message = new TreeMap<>();
        // 编辑
        if (StringUtil.isNotEmpty(templateId)) {
            try {
                if (!dao.isEditExistTemplateCode(templateCode, templateId)) {
                    message.put("success", "true");
                    message.put("msg", "检查表编号未被使用");
                    return message;
                } else {
                	message.put("success", "true");
                    message.put("msg", "检查表编号未被使用");
                    return message;
                }
            } catch (Exception e) {
                message.put("success", "false");
                message.put("msg", "数据库查询失败！");
                return message;
            }

        }
        // 新增
        if (dao.isAddExistTemplateCode(templateCode)) {
            message.put("success", "false");
            message.put("msg", "检查表编号已存在");
            return message;
        } else {
            message.put("success", "true");
            message.put("msg", "检查表编号未被使用");
            return message;
        }
    }
    
    public Map<String, Object> addTemplateBaseInfor(String templateCode, String templateName, String[] Sign, String[] Status, String attention, String moduleId, String folderId, String type, String templateStatus) {
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> checkRes = templateCodeCheck(templateCode, "");
        if (checkRes.get("success").equals("false")) {
            return checkRes;
        }
        String msg = "";
        Long ID = UniqueIdUtil.genId();//模板ID
        try {
        	Template template = new Template();
            template.setID(ID);
            template.setTemplateName(templateName);
            template.setTemplateCode(templateCode);
            template.setRemark(attention);
            //template.setProjectId(Long.parseLong(pid));
            template.setModuleId(moduleId);
            // 文件夹id
            template.setFolderId(Long.parseLong(folderId));
            template.setType(type);
            template.setStatus(templateStatus);
            dao.add(template);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "模板基础信息保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }

        message = saveCheckCondition(Status, ID);
        if (message.get("success").equals("false")) {
            message.put("ID", ID);
            return message;
        } else {
            message = saveDefineSign(Sign, ID);
            if (message.get("success").equals("false")) {
                message.put("ID", ID);
                return message;
            }
        }
        message.put("success", "true");
        message.put("msg", "模板基础信息保存成功");
        message.put("ID", ID);
        return message;
    }
    
    private Map<String, Object> saveCheckCondition(String[] status, Long templateId) {
        Map<String, Object> message = new HashMap<>();
        try {
            
            int k = 1;
            for (String check : status) {
                if (check == null || check.equals("")) {
                    continue;
                }
                TemplateCheckCondition checkCondition = new TemplateCheckCondition();
                checkCondition.setId(UniqueIdUtil.genId());
                checkCondition.setName(check);
                checkCondition.setOrder(CommonTools.Obj2String(k));
                checkCondition.setTemplateId(CommonTools.Obj2String(templateId));
                templateCheckConditionDao.add(checkCondition);
                k++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "检查条件保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        message.put("success", "true");
        return message;
    }

    private Map<String, Object> saveDefineSign(String[] sign, Long tableTempId) {
        Map<String, Object> message = new HashMap<>();
        try {
            int k = 1;
            for (String define : sign) {
                if (define == null || define.equals("")) {
                    continue;
                }
                TemplateSign templateSign = new TemplateSign();
                templateSign.setId(UniqueIdUtil.genId());
                templateSign.setName(define);
                templateSign.setOrder(CommonTools.Obj2String(k));
                templateSign.setTemplateId(CommonTools.Obj2String(tableTempId));
                templateSignDao.add(templateSign);
                k++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "签署信息保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        message.put("success", "true");
        return message;
    }
    
    public Map<String, Object> updateTemplateBaseInfor(String templateId, String templateCode, String templateName, String[] sign, String[] status, String attention, String type) {
        Map<String, Object> message = new HashMap<>();
        try {
            Template template = new Template();
            template.setID(Long.valueOf(templateId));
            template.setTemplateCode(templateCode);
            template.setTemplateName(templateName);
            template.setRemark(attention);
            template.setType(type);
            dao.update(template);
            // 删除模板下的所有检查条件
            templateCheckConditionDao.delByTemplateId(templateId);
            // 删除模板下的所有签署
            templateSignDao.delByTemplateId(templateId);
            // （新增，重新创建）保存模板检查条件
            message = saveCheckCondition(status, Long.valueOf(templateId));
            if (message.get("success").equals("false")) {
                message.put("ID", templateId);
                return message;
            } else {
            	// 保存模板签署属性 
                message = saveDefineSign(sign, Long.valueOf(templateId));
                if (message.get("success").equals("false")) {
                    message.put("ID", templateId);
                    return message;
                }
            }
            message.put("success", "true");
        } catch (Exception e) {
            message.put("success", "false");
            message.put("msg", "表单模板数据修改失败！" + e.getMessage());
        }
        message.put("ID", templateId);
        return message;
    }
    
    public Map<String, Object> delCommonTemplate(String templateId) {
        Map<String, Object> message = new TreeMap<>();
        if (templateInstanceDao.isExistInstance(templateId)) {
            message.put("success", "false");
            message.put("msg", "存在表单实例，请先删除表单实例以及工作队信息");
            return message;
        } else {
            try {
            	// 1.删除模板
                dao.delById(Long.parseLong(templateId));
                // 2.删除检查条件
                templateCheckConditionDao.delByTemplateId(templateId);
                // 3.删除检查项
                templateCheckItemDao.delByTemplateId(templateId);
                // 4.删除签署
                templateSignDao.delByTemplateId(templateId);
                message.put("success", "true");
                message.put("msg", "模板删除成功");
                return message;
            } catch (Exception e) {
                message.put("success", "false");
                message.put("msg", "模板删除失败");
                return message;
            }
        }
    }
    
    public Map<String, Object> commonSave(String html, String templateId, String type) {
        String msg = "保存成功！";
        Map<String, Object> message = new TreeMap<String, Object>();
        //dom4j中不能出现& 否则无法转换
        Map<String, Object> TableRes;
        String title = "";
        List<Map<String, Object>> formcontent = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> colorformcontent = new ArrayList<Map<String, Object>>();
        Document tableDoc = null;
        try {
            html = html.replaceAll("&", "#");
            html = "<html>" + html + "</html>";
            tableDoc = DocumentHelper.parseText(html);
        } catch (DocumentException e) {
            e.printStackTrace();
            msg = "无法从html转到Dom对象：请检查html文件的闭合情况";
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        try {

            TableRes = FormUtils.getTableRes(html, type);
            title = TableRes.get("titles").toString();
            formcontent = (List<Map<String, Object>>) TableRes.get("formcontent");
            colorformcontent = (List<Map<String, Object>>) TableRes.get("colorformcontent");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "检查项保存失败");
            return message;
        }

        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("templateId", templateId);
            param.put("templateHtml", title);
            param.put("status", TemplateConstant.TEMP_COMPLETE);
            // 更新模板html及模板状态（已完成）
            dao.updateTemplateHtmlAndStatus(param);
            // 删除模板检查项
            templateCheckItemDao.delByTemplateId(templateId);
            
            TemplateCheckItem templateCheckItem = new TemplateCheckItem();
            for (int i = 0; i < formcontent.size(); i++) {
                Map<String, Object> content = formcontent.get(i);
                Map<String, Object> colorcontent = colorformcontent.get(i);
                String colordescribe = (String) colorcontent.get("colordescribe");
                String colordescribenew = "";
                String[] shortname = colordescribe.split(",");
                if (shortname.length == 1) {
                    if (shortname[0].indexOf("*/") > -1)
                        colordescribenew = shortname[0].replaceAll("[*/]", "");
                    else if (shortname[0].indexOf("/*") > -1)
                        colordescribenew = shortname[0].replaceAll("[/*]", "");
                    else if (shortname[0].indexOf("*") > -1)
                        colordescribenew = shortname[0].replaceAll("[*]", "");
                }
                if (shortname.length == 2) {
                    if (shortname[0].contains("*")) {
                        if (shortname[0].indexOf("*/") > -1)
                            colordescribenew = shortname[0].replaceAll("[*/]", "") + shortname[1];
                        else if (shortname[0].indexOf("/*") > -1)
                            colordescribenew = shortname[0].replaceAll("[/*]", "") + shortname[1];
                        else if (shortname[0].indexOf("*") > -1)
                            colordescribenew = shortname[0].replaceAll("[*]", "") + shortname[1];
                    } else {
                        if (shortname[1].indexOf("*/") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[*/]", "");
                        else if (shortname[1].indexOf("/*") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[/*]", "");
                        else if (shortname[1].indexOf("*") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[*]", "");
                    }
                }
                
                templateCheckItem.setId((Long) content.get("id"));
                templateCheckItem.setType(CommonTools.Obj2String(content.get("type")));
                templateCheckItem.setShortName(colordescribenew);
                templateCheckItem.setDescription(CommonTools.Obj2String(content.get("describe")));
                templateCheckItem.setTemplateId(templateId);
                templateCheckItem.setIILdd("否");
                templateCheckItem.setILdd("否");
                templateCheckItem.setZhycdz("否");
                templateCheckItem.setNjljyq("否");
                templateCheckItem.setYcn("否");
                if (content.get("photo").equals("true"))
                	templateCheckItem.setIfMedia("是");
                else
                	templateCheckItem.setIfMedia("否");
                // 重建模板检查项
                templateCheckItemDao.add(templateCheckItem);
            }
            String createTime = TimeUtil.getCurrentTime();
            createTime = "to_date('"+createTime+"','yyyy-mm-dd,hh24:mi:ss')";
            // 更新模板创建信息
            dao.updateTemplateCreateInfor(templateId, UserContextUtil.getCurrentUserId().toString(), createTime);
            message.put("success", "true");
            message.put("msg", msg);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "检查项保存失败");
            return message;
        }
    }
    
    public Map<String, Object> getTemplateMapById(String templateId) {
		return dao.getTemplateMapById(templateId);
	}
    
    public Map<String, Object> commonMultiCopy(String categoryId, String templateIds[]) {
        Map<String, Object> message = new HashMap<>();
        for (String templateId : templateIds) {
        	try {
        		// 1.复制模板
                Template commonTemplate = dao.getById(Long.parseLong(templateId));
                commonTemplate.setFolderId(1L);
                Long mpk = UniqueIdUtil.genId();
                String templateName = commonTemplate.getTemplateName();
                String newTemplateName = templateName + "-副本";
                int n = 0;
                List<Map<String, Object>> templates = dao.getTemplatesByCategoryIdAndName(categoryId, templateName);
                while (templates.size() > 0) {
                    n++;
                    String N = String.valueOf(n);
                    templateName = newTemplateName + N;
                    templates = dao.getTemplatesByCategoryIdAndName(categoryId, templateName);
                }
                commonTemplate.setTemplateName(templateName);
                commonTemplate.setID(mpk);
                commonTemplate.setProjectId(Long.parseLong(categoryId));
                commonTemplate.setModuleId("");
                dao.add(commonTemplate);
                
                // 2.复制检查条件
                List<TemplateCheckCondition> checkConditions = templateCheckConditionDao.getByTemplateId(templateId);
                for (TemplateCheckCondition checkCondition : checkConditions) {
					checkCondition.setId(UniqueIdUtil.genId());
					checkCondition.setTemplateId(CommonTools.Obj2String(mpk));
					templateCheckConditionDao.add(checkCondition);
				}
                
                // 3.复制签署项
                List<TemplateSign> signs = templateSignDao.getByTemplateId(templateId);
                for (TemplateSign sign : signs) {
					sign.setId(UniqueIdUtil.genId());
					sign.setTemplateId(CommonTools.Obj2String(mpk));
					templateSignDao.add(sign);
				}

                // 4.复制检查项
                List<TemplateCheckItem> checkItems = templateCheckItemDao.getByTemplateId(templateId);
                for (TemplateCheckItem checkItem : checkItems) {
					checkItem.setId(UniqueIdUtil.genId());
					checkItem.setTemplateId(CommonTools.Obj2String(mpk));
					templateCheckItemDao.add(checkItem);
				}
                

                // 5.复制后的模板信息-替换content检查项id
                Map<String, Object> newTemplate = dao.getTemplateMapById(mpk.toString());
                String content = CommonTools.Obj2String(newTemplate.get("F_CONTENTS"));

                List<Map<String, Object>> newCheckItems = templateCheckItemDao.getCheckItemListMapByTemplateId(mpk.toString());
                List<Map<String, Object>> oldCheckItems = templateCheckItemDao.getCheckItemListMapByTemplateId(templateId);
                for (int i = 0; i < newCheckItems.size(); i++) {
                    for (int k = 0; k < oldCheckItems.size(); k++) {
                        if (oldCheckItems.get(k).get("F_TYPE").toString()
                                .equals(newCheckItems.get(i).get("F_TYPE").toString())
                                && oldCheckItems.get(k).get("F_DESCRIPTION").toString()
                                .equals(newCheckItems.get(i).get("F_DESCRIPTION").toString())) {
                            content = content.replaceAll(oldCheckItems.get(k).get("ID").toString()
                                    , newCheckItems.get(i).get("ID").toString());
                            break;
                        }
                    }
                }
                
                dao.updateTemplateHtmlById(mpk, content);
            } catch (Exception e) {
                e.printStackTrace();
                message.put("success", "false");
                message.put("msg", "模板复制失败,被复制的模板中存在检查内容为空的情况");
                return message;
            }
		}
        
        message.put("success", "true");
        message.put("msg", "模板复制成功");
        return message;
    }
    
    /**
     * @Desc 获取产品类别下的模板集合
     * @param productCategoryId
     * @return
     */
    public List<Map<String, Object>> getTemplatesByCategoryId(String productCategoryId) {
    	return dao.getTemplatesByCategoryId(productCategoryId);
    }
    
}
