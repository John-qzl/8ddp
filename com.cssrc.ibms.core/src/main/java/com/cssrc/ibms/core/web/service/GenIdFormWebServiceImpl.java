package com.cssrc.ibms.core.web.service;

import com.cssrc.ibms.api.core.intf.IGenIdFormWebService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;


public class GenIdFormWebServiceImpl implements IGenIdFormWebService
{

    @Override
    public Long genId(String user, String pass)
    {
        return UniqueIdUtil.genId();
    }

}
