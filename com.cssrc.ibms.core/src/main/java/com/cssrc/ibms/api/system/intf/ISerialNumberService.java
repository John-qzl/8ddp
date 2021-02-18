package com.cssrc.ibms.api.system.intf;

import java.io.InputStream;
import java.util.List;

import com.cssrc.ibms.api.system.model.ISerialNumber;

public interface ISerialNumberService{

	public abstract boolean isAliasExisted(String alias);

	public abstract String getCurIdByAlias(String alias);

	public abstract String nextId(String alias);

	public abstract String preview(String alias);

	public abstract List<?extends ISerialNumber> getList();

	public abstract ISerialNumber getByAlias(String alias);

	public abstract String exportXml(Long[] tableIds) throws Exception;

	public abstract void importXml(InputStream inputStream) throws Exception;

	public abstract List<?extends ISerialNumber> getAll();

	public abstract void add(ISerialNumber serial);

    public abstract Class<?extends ISerialNumber> getSerialNumberClass();

}