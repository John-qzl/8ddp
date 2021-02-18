package com.cssrc.ibms.core.encrypt;

import java.io.ByteArrayOutputStream;

public class KGBase64
{
    private String base64Table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    public String getBase64Table()
    {
        return this.base64Table;
    }
    
    public void setBase64Table(String base64Table)
    {
        if (base64Table.length() != 65)
            throw new IllegalArgumentException("base64必须65位");
        this.base64Table = base64Table;
    }
    
    public String encode(byte[] bytes)
    {
        int length = bytes.length;
        StringBuilder sb = new StringBuilder();
        byte[] bt = new byte[4];
        
        int flag = -1;
        
        for (int i = 0; i < length; i += 3)
        {
            for (int j = i; (j < i + 3) && (j < length); j++)
            {
                byte b = bytes[j];
                switch (j % 3)
                {
                    case 0:
                        bt[0] = (byte)((b & 0xFC) >> 2);
                        bt[1] = (byte)((b & 0x3) << 4);
                        break;
                    case 1:
                        bt[1] = (byte)(bt[1] + ((b & 0xF0) >> 4));
                        bt[2] = (byte)((b & 0xF) << 2);
                        flag = 2;
                        break;
                    case 2:
                        bt[2] = (byte)(bt[2] + ((b & 0xC0) >> 6));
                        bt[3] = (byte)(b & 0x3F);
                        flag = 3;
                }
                
            }
            
            for (int k = 0; k < 4; k++)
            {
                if (k > flag)
                    bt[k] = 64;
                sb.append(this.base64Table.charAt(bt[k]));
            }
            flag = -1;
        }
        
        return sb.toString();
    }
    
    public byte[] decode(String data)
    {
        byte[] bytes = data.getBytes();
        int length = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bt = new byte[3];
        
        int flag = -1;
        
        for (int i = 0; i < length; i += 4)
        {
            for (int j = i; (j < i + 4) && (j < length); j++)
            {
                byte b = (byte)this.base64Table.indexOf(bytes[j]);
                if (64 != b)
                {
                    switch (j % 4)
                    {
                        case 0:
                            bt[0] = (byte)(b << 2);
                            break;
                        case 1:
                            bt[0] = (byte)(bt[0] + ((b & 0xF0) >> 4));
                            bt[1] = (byte)((b & 0xF) << 4);
                            flag = 0;
                            break;
                        case 2:
                            bt[1] = (byte)(bt[1] + ((b & 0xFC) >> 2));
                            bt[2] = (byte)((b & 0x3) << 6);
                            flag = 1;
                            break;
                        case 3:
                            bt[2] = (byte)(bt[2] + b);
                            flag = 2;
                    }
                    
                }
                
            }
            
            for (int k = 0; k <= flag; k++)
            {
                out.write(bt[k]);
            }
            flag = -1;
        }
        return out.toByteArray();
    }
}
