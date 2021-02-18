package com.cssrc.ibms.api.activity.intf;

import java.util.List;

import com.cssrc.ibms.api.activity.model.ITaskExe;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface ITaskExeService {

	List<?extends ITaskExe> accordingMattersList(Long ownerId, PagingBean pb);

	List<? extends ITaskExe> accordingMattersList(QueryFilter filter);


}