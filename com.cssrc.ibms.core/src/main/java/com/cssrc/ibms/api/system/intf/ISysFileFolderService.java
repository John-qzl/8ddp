package com.cssrc.ibms.api.system.intf;

import java.util.List;

import com.cssrc.ibms.api.system.model.ISysFileFolder;

public interface ISysFileFolderService {

	List<? extends ISysFileFolder> getFolderByUserId(Long userId);

	List<? extends ISysFileFolder> saveFolder(Long userId);

}
