package com.cssrc.ibms.index.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
	public IndexColumns createIndexColumns() {
		return new IndexColumns();
	}

	public IndexColumn createIndexColumn() {
		return new IndexColumn();
	}
}
