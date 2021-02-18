package com.cssrc.ibms.api.form.model;

import java.util.List;
import com.cssrc.ibms.api.system.model.BaseSysFile;

public class BaseFormDefXml implements IFormDefXml 
{
    private List<? extends BaseSysFile> sysFileList;  

    public List<? extends BaseSysFile> getSysFileList()
    {
        return sysFileList;
    }
    
}
