package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import java.util.Map;
import com.cssrc.ibms.api.activity.model.IDefAct;
import com.cssrc.ibms.api.activity.model.IDefAuthorize;
import com.cssrc.ibms.api.activity.model.IDefUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IDefAuthorizeService {

	public abstract List<?extends IDefAuthorize> getAuthorizeListByFilter(
			QueryFilter queryFilter);

	public abstract IDefAuthorize getAuthorizeById(Long id);

	public abstract Long deleteAuthorizeByIds(Long[] lAryId);

	public abstract List<?extends IDefUser> toDefUserList(String myOwnerNameJson,
			Long authorizeId);

	public abstract List<?extends IDefAct> toDefActList(String myDefNameJson,
			Long authorizeId);

	public abstract Map<String, Object> getActRightByUserMap(Long userId,
			String authorizeType, boolean isRight, boolean isMyDef);

}