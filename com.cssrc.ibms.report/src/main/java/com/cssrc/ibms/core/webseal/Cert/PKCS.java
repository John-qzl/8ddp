package com.cssrc.ibms.core.webseal.Cert;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import com.cssrc.ibms.core.util.date.CertUtil;
import com.cssrc.ibms.core.util.date.DateUtil;

import javax.security.auth.x500.X500Principal;

import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @ClassName: PKCS
 * @Description: 数字证书初始化
 * @author zxg
 * @date 2016年12月5日 下午3:02:21
 * 
 */
public class PKCS
{
    private KeyPairGenerator kpg;
    //密钥库密码
    private String storePass;
    //证书密码
    private String keyPass;
    //证书别名
    private String alias="ibms.";
  
    
    // 系统添加BC加密算法 以后系统中调用的算法都是BC的算法
    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public PKCS()
    {
        try
        {
            /* RSA算法产生公钥和私钥 */
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public boolean ExecPfx(String certPath, String user)
        throws Exception
    {
        try
        {
            //获取密钥库
            KeyStore store = KeyStore.getInstance("PKCS12");
            store.load(null, null);
            KeyPair keyPair = kpg.generateKeyPair();
            // 公钥
            PublicKey publicKey = keyPair.getPublic();
            // 私钥
            PrivateKey privateKey = keyPair.getPrivate();
            // 组装证书
            X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
            certGen.setSerialNumber(CertUtil.getNextSerialNumber());
            certGen.setIssuerDN(new X500Principal(CAConfig.CA_ROOT_ISSUER));
            certGen.setNotBefore(DateUtil.getCurrentDate());
            certGen.setNotAfter(DateUtil.addYear(2));
            certGen.setSubjectDN(new X500Principal(CAConfig.CA_DEFAULT_SUBJECT + user));
            certGen.setPublicKey(publicKey);
            certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
            X509Certificate cert = certGen.generate(privateKey);
            //密钥库中添加密钥对
            store.setKeyEntry(alias+user, privateKey, keyPass.toCharArray(), new Certificate[] {cert});
            File cf = new File(certPath);
            if (!cf.exists())
            {
                cf.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(certPath);
            //保存密钥
            store.store(fout, storePass.toCharArray());
            fout.close();
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    
    public KeyPairGenerator getKpg()
    {
        return kpg;
    }
    
    public void setKpg(KeyPairGenerator kpg)
    {
        this.kpg = kpg;
    }

    public String getStorePass()
    {
        return storePass;
    }

    public void setStorePass(String storePass)
    {
        this.storePass = storePass;
    }

    public String getKeyPass()
    {
        return keyPass;
    }

    public void setKeyPass(String keyPass)
    {
        this.keyPass = keyPass;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
}
