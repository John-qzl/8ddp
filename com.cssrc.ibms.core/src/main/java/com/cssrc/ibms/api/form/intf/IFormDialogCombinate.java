package com.cssrc.ibms.api.form.intf;

import com.cssrc.ibms.api.form.model.IFormDialog;

public abstract interface IFormDialogCombinate {



	public abstract void setId(Long id);


	public abstract Long getId();


	public abstract String getName();


	public abstract void setName(String name);


	public abstract String getAlias();


	public abstract void setAlias(String alias);


	public abstract int getWidth();


	public abstract void setWidth(int width);


	public abstract int getHeight();


	public abstract void setHeight(int height);

	public abstract void setTreeDialogId(Long treeDialogId); 

	public abstract Long getTreeDialogId();


	public abstract void setTreeDialogName(String treeDialogName);


	public abstract String getTreeDialogName();


	public abstract void setListDialogId(Long listDialogId) ;

	public abstract Long getListDialogId();


	public abstract void setListDialogName(String listDialogName); 

	public abstract String getListDialogName();


	public abstract void setField(String field); 

	public abstract String getField();

	public abstract IFormDialog getTreeDialog();


	public abstract IFormDialog getListDialog();



}
