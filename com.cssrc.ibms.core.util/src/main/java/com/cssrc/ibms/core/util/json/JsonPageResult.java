package com.cssrc.ibms.core.util.json;

import java.util.List;

public class JsonPageResult<T>
{
	private Integer total = Integer.valueOf(0);
	private List<T> data;
	private Boolean success;
	private String message;

	public JsonPageResult()
	{
	}

	public JsonPageResult(List<T> dataList, Integer totalCount)
	{
		this.data = dataList;
		this.total = totalCount;
	}

	public JsonPageResult(Boolean success, List<T> dataList, Integer totalCount, String message) {
		this.success = success;
		this.data = dataList;
		this.total = totalCount;
		this.message = message;
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getData() {
		return this.data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}