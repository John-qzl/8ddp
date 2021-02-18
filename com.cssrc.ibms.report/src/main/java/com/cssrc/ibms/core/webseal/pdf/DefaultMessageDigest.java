package com.cssrc.ibms.core.webseal.pdf;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultMessageDigest
{
    
    private MessageDigest messageDigest;
    
    public DefaultMessageDigest()
    {
        try
        {
            this.messageDigest = MessageDigest.getInstance("SHA1");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public byte[] digest()
    {
        return this.messageDigest.digest();
    }
    
    public void update(byte[] data)
    {
        this.messageDigest.update(data);
    }
    
}
