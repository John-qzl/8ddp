package com.cssrc.ibms.dp.sync.util;

public class SyncBaseFilter {
	/** 
	* @Fields filterName : 过滤的名称
	*/
	private String filterName;
	/** 
	* @Fields operation : 操作符
	*/
	private String operation;
	/** 
	* @Fields filterValue : 过滤的值
	*/
	private String filterValue;
	/** 
	* @Fields connection : 过滤条件之间的连接方式，默认采用And
	*/
	private String connection;
	/** 
	* @Fields parent : 该过滤对象的父对象
	*/
	private SyncBaseFilter parent;
	/** 
	* @Fields child : 该过滤对象的子对象
	*/
	private SyncBaseFilter child;
	/** 
	* @Fields visited : 该过滤对象在转成Sql语句时，是否已经被遍历过
	*/
	private boolean visited = false; 
	
	private String expressType = "ID";

	public SyncBaseFilter(String filterName, String operation,
			String filterValue) { 
		this(filterName,operation,filterValue,null); 
	}
	
	public SyncBaseFilter(String filterName, String operation,
			String filterValue, String connection) { 
		this.filterName = filterName;
		this.operation = operation;
		this.filterValue = filterValue;
		this.connection = connection;
	}
	
	public SyncBaseFilter(String filterName, String operation,
			String filterValue, String connection, String expressType) { 
		this.filterName = filterName;
		this.operation = operation;
		this.filterValue = filterValue;
		this.connection = connection;
		this.expressType = expressType;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}


	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public SyncBaseFilter getParent() {
		return parent;
	}

	public void setParent(SyncBaseFilter parent) {
		this.parent = parent;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public SyncBaseFilter getChild() {
		return child;
	}

	public void setChild(SyncBaseFilter child) {
		this.child = child;
	}
	
}
