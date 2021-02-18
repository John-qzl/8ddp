package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;

public interface IFormDialog {
	
	public static final String Page = "p";
	public static final String PageSize = "z";

	/**
	 * 取得tree字段。
	 * 
	 * @return
	 */
	public Map<String, String> getTreeField();
	/**
	 * 返回显示字段列表 [{"field":"","comment":""}]
	 * 
	 * @return
	 */
	public List<?extends IDialogField> getDisplayList();

	/**
	 * 解析条件字段。 [{"field":"字段名","comment":"注释","condition":"条件","dbType":
	 * "varchar,number,date"},....]
	 * 
	 * @return
	 */
	public List<?extends IDialogField> getConditionList();

	/**
	 * 返回返回字段列表。
	 * 
	 * @return
	 */
	public List<?extends IDialogField> getReturnList();

	/**
	 * 返回排序字段列表。
	 * 
	 * @return
	 */
	public List<?extends IDialogField> getSortList();

	public void setId(Long id);

	/**
	 * 返回 主键ID
	 * 
	 * @return
	 */
	public Long getId();

	public void setName(String name);
	/**
	 * 返回 对话框名称
	 * 
	 * @return
	 */
	public String getName();

	public void setAlias(String alias);

	/**
	 * 返回 对话框别名
	 * 
	 * @return
	 */
	public String getAlias();

	public void setStyle(Integer style);

	/**
	 * 返回 显示样式 0,列表 1,树形
	 * 
	 * @return
	 */
	public Integer getStyle();

	public void setWidth(Integer width);

	/**
	 * 返回 对话框宽度
	 * 
	 * @return
	 */
	public Integer getWidth();

	public void setHeight(Integer height);

	/**
	 * 返回 高度
	 * 
	 * @return
	 */
	public Integer getHeight();

	public void setIssingle(Integer issingle);

	/**
	 * 返回 是否单选 0,多选 1.单选
	 * 
	 * @return
	 */
	public Integer getIssingle();

	public void setNeedpage(Integer needpage);

	/**
	 * 返回 是否分页
	 * 
	 * @return
	 */
	public Integer getNeedpage();

	public void setIstable(Integer istable);

	/**
	 * 返回 是否为表 0,视图 1,数据库表
	 * 
	 * @return
	 */
	public Integer getIstable();

	public void setObjname(String objname);

	/**
	 * 返回 对象名称
	 * 
	 * @return
	 */
	public String getObjname();

	public void setDisplayfield(String displayfield);

	/**
	 * 返回 需要显示的字段
	 * 
	 * @return
	 */
	public String getDisplayfield();

	public void setConditionfield(String conditionfield);
	/**
	 * 返回 条件字段
	 * 
	 * @return
	 */
	public String getConditionfield();
	public String getReturnFields();

	// 排序字段
	public String getSortfield();

	public void setSortfield(String sortfield);

	public void setResultfield(String resultfield);

	/**
	 * 返回 显示结果字段
	 * 
	 * @return
	 */
	public String getResultfield();

	public void setDsname(String dsname);

	/**
	 * 返回 数据源名称
	 * 
	 * @return
	 */
	public String getDsname();

	public void setDsalias(String dsalias);

	/**
	 * 返回 数据源别名
	 * 
	 * @return
	 */
	public String getDsalias() ;

	/**
	 * 返回 分页大小
	 * 
	 * @return
	 */
	public Integer getPagesize();
	/**
	 * 
	 * @return
	 */
	public void setPagesize(Integer pagesize);

	public List<Map<String, Object>> getList();

	public void setList(List<Map<String, Object>> list);

	public PagingBean getPagingBean();

	public void setPagingBean(PagingBean pagingBean);
	

}
