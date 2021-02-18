package com.cssrc.ibms.core.form.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormDefTree;

@Repository
public class FormDefTreeDao extends BaseDao<FormDefTree> {
	public Class<?> getEntityClass() {
		return FormDefTree.class;
	}

	public FormDefTree getByFormKey(Long formKey) {
		FormDefTree defTree = (FormDefTree) getUnique("getByFormKey", formKey);
		return defTree;
	}

	public void delByFormDefKey(Long formKey) {
		delBySqlKey("delByFormDefKey", formKey);
	}
}
