package com.cssrc.ibms.api.report.inf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.report.model.IOfficeItem;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;

public interface IOfficeExtService
{

    /** 
    * @Title: getQuerySql 
    * @Description: TODO(扩展自定义查询sql) 
    * @param @param formTable
    * @param @param feilds
    * @param @param mark
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    StringBuffer getQuerySql(IFormTable formTable, List<? extends IFormField> feilds, IOfficeItem mark);

    /** 
    * @Title: extWordWriter 
    * @Description: TODO(循环标签多张表扩展) 
    * @param @param wordWriter
    * @param @param max
    * @param @param mark
    * @param @param tabData
    * @param @return     
    * @return int    返回类型 
    * @throws 
    */
    int extWordWriter(WordDocument wordWriter, int max, IOfficeItem mark, List<Map<String, Object>> tabData);
    
}
