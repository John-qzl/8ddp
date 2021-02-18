package com.cssrc.ibms.core.web.singlelogin;


import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;

public class IbmsSinglePasswordEncoder implements PasswordEncoder{
    private HttpServletRequest request;
    private IbmsSingleLogin singleLogin;
    @Override
    public String encodePassword(String params, Object salt)
            throws DataAccessException {
        String token = PasswordUtil.generatePassword(params);
        return token;
    }

    @Override
    public boolean isPasswordValid(String token, String params, Object salt)
            throws DataAccessException {
        
        return valid(request);
    }

    public IbmsSingleLogin getSingleLogin()
    {
        return singleLogin;
    }

    public void setSingleLogin(IbmsSingleLogin singleLogin)
    {
        this.singleLogin = singleLogin;
    }

    public String getSignParam(String url)
    {
        return singleLogin.getSignParam(url);
    }

    public boolean valid(HttpServletRequest request)
    {
        String query=request.getQueryString();
        return singleLogin.validUrl(query);
    }

    public void authenticate(HttpServletRequest request)
    {
        this.request=request;
        
    }

   /* public static void decryptParam(ServletRequest request)
    {
        IbmsSinglePasswordEncoder singlePasswordEncoder=AppUtil.getBean(IbmsSinglePasswordEncoder.class);
        IbmsSingleLogin singleLogin=singlePasswordEncoder.getSingleLogin();
        if(singleLogin.isEncrypt()) {
            singleLogin.decryptParam(request);
        }
    }*/

    
}
