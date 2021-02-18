package com.cssrc.ibms.share.rights;
import com.cssrc.ibms.share.model.SysShareRights;
/**
 * 
 * <p>Title:IShareRightsService</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:01:39
 */
public abstract interface IShareRightsService {
	public abstract void addShare(SysShareRights paramSysShareRights);

	public abstract String getShareType();

	public abstract String getShareDesc();

	public abstract DataTemplateVO getDataObject(String paramString);

	public abstract void removeShareRights(SysShareRights paramSysShareRights);
}
