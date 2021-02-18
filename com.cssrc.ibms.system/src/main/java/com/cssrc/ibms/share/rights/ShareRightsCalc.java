package com.cssrc.ibms.share.rights;

import com.cssrc.ibms.share.model.SysShareRights;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
/**
 * 
 * <p>Title:ShareRightsCalc</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:02:07
 */
public class ShareRightsCalc {

	@Resource
	ShareRightsContainer container;

	public void setContainer(ShareRightsContainer container) {
		this.container = container;
	}

	public void execute(SysShareRights sysShareRights) {
		IShareRightsService service = this.container
				.getShareRightsService(getType(sysShareRights));
		service.addShare(sysShareRights);
	}

	public DataTemplateVO getDataTemplateVO(String type, String id) {
		IShareRightsService service = this.container
				.getShareRightsService(type);
		return service.getDataObject(id);
	}

	public void removeShareRights(SysShareRights sysShareRights) {
		IShareRightsService service = this.container
				.getShareRightsService(getType(sysShareRights));
		service.removeShareRights(sysShareRights);
	}

	private String getType(SysShareRights sysShareRights) {
		JSONObject shareItem = JSONObject.fromObject(sysShareRights
				.getShareItem());
		return shareItem.getString("type");
	}
}
