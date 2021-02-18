package com.cssrc.ibms.share.rights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * <p>Title:ShareRightsContainer</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:02:44
 */
public class ShareRightsContainer {
	private Map<String, IShareRightsService> shareRightsMap = new HashMap();

	private List<IShareRightsService> shareRightsList = new ArrayList();

	public void setShareRightsList(
			List<IShareRightsService> IShareRightsServices) {
		for (IShareRightsService shareRights : IShareRightsServices) {
			this.shareRightsMap.put(shareRights.getShareType(), shareRights);
		}
		this.shareRightsList = IShareRightsServices;
	}

	public List<IShareRightsService> getShareRightsList() {
		return this.shareRightsList;
	}

	public IShareRightsService getShareRightsService(String type) {
		return (IShareRightsService) this.shareRightsMap.get(type);
	}
}
