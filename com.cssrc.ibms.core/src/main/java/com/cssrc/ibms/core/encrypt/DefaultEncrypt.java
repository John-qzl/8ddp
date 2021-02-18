package com.cssrc.ibms.core.encrypt;

import com.cssrc.ibms.core.encrypt.Coder;

public class DefaultEncrypt implements IFiledEncrypt
{
    @Override
    public Object encrypt(Object data)
    {
        try
        {
            return Coder.encryptBASE64(data.toString().getBytes());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return data;
        }
    }

    @Override
    public Object decrypt(Object data)
    {
        try
        {
            return new String(Coder.decryptBASE64(data.toString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return data;
        }
    }
}
