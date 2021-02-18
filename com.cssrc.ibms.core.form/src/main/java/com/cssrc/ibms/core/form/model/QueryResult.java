package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.cssrc.ibms.api.form.model.IQueryResult;
/**
 * 通用查询返回结果
 * @author zhulongchao
 *
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "queryField")
@XmlAccessorType(XmlAccessType.NONE)
public class QueryResult implements IQueryResult{
	
	private List list=new ArrayList();
	/**
	 * 是否分页。
	 */
	private int isPage=0;
	/**
	 * 每页记录条数
	 */
	private int pageSize=10;
	/**
	 * 页码
	 */
	private int page=1;
	/**
	 * 错误信息
	 */
	private String errors = "";
	/**
	 * 总条数
	 */
	private int totalCount = 0;
	/**
	 * 总页数
	 */
	private int totalPage = 0;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getIsPage() {
		return isPage;
	}

	public void setIsPage(int isPage) {
		this.isPage = isPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
