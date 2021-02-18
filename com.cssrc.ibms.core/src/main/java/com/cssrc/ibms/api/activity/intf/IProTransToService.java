package com.cssrc.ibms.api.activity.intf;

import java.util.List;

import com.cssrc.ibms.api.activity.model.IProTransTo;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IProTransToService {

	List<? extends IProTransTo> mattersList(QueryFilter filter);

}