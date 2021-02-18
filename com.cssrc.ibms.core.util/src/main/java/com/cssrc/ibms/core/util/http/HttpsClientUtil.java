/*
 * 文 件 名:  HttpsClientBusiness.java
 * 版    权:  xiaoyuer Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zhuguanglong
 * 修改时间:  2014-10-23
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.cssrc.ibms.core.util.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Service;

/**
 * HTTPS客户端发送报文通道
 * 
 * @author  zxg
 * @version  [版本号, 2016-11-30]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HttpsClientUtil
{
    
    /** {@inheritDoc} */
    
    public static Object transmit(ServerBean serverBean)
        throws Exception
    {
        String result = "";
        HttpsURLConnection conn = null;
        OutputStream os = null;
        BufferedReader br = null;
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] {new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
                {
                }
                
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
                {
                }
                
                public X509Certificate[] getAcceptedIssuers()
                {
                    return new X509Certificate[] {};
                }
            }}, new java.security.SecureRandom());
            
            URL url;
            url = new URL(serverBean.getUrl());
            
            conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
            conn.setRequestMethod(serverBean.getRequestMethod());
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            
            os = conn.getOutputStream();
            os.write(serverBean.getContent().getBytes(serverBean.getCharsetName()));
            os.close();
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), serverBean.getCharsetName()));
            String line;
            while ((line = br.readLine()) != null)
            {
                result += line;
            }
            br.close();
            conn.disconnect();
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
            if (conn != null)
            {
                conn.disconnect();
            }
            if (os != null)
            {
                os.close();
            }
        }
        return result;
    }
    
    public static Object transmitToGet(ServerBean serverBean)
        throws Exception
    {
        String result = "";
        HttpsURLConnection conn = null;
        OutputStream os = null;
        BufferedReader br = null;
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] {new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
                {
                }
                
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
                {
                }
                
                public X509Certificate[] getAcceptedIssuers()
                {
                    return new X509Certificate[] {};
                }
            }}, new java.security.SecureRandom());
            
            URL url;
            url = new URL(serverBean.getUrl());
            
            conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
            conn.setRequestMethod(serverBean.getRequestMethod());
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            
//            os = conn.getOutputStream();
//            os.write(serverBean.getContent().getBytes(serverBean.getCharsetName()));
//            os.close();
            
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), serverBean.getCharsetName()));
            String line;
            while ((line = br.readLine()) != null)
            {
                result += line;
            }
            br.close();
            conn.disconnect();
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
            if (conn != null)
            {
                conn.disconnect();
            }
            if (os != null)
            {
                os.close();
            }
        }
        return result;
    }
}
