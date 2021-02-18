package com.cssrc.ibms.core.webseal.Cert;

/**
 * @ClassName: CAConfig
 * @Description: 数字证书配置文件
 * @author zxg
 * @date 2016年12月5日 下午3:01:45
 * 
 */
public interface CAConfig
{
    
    /**
     * CN 国家 中国
     */
    String CA_C = "CN";
    
    /**
     * ST 州或省份名称 BJ
     */
    String CA_ST = "BJ";
    
    /**
     * L 城市或区域名称
     */
    String CA_L = "BJ";
    
    /**
     * 组织名称
     */
    String CA_O = "CAA";
    
    /**
     * 组织单位名称
     */
    String CA_OU = "CAA";
    
    /**
     * CA_ROOT_ISSUER
     */
    String CA_ROOT_ISSUER = "C=CN,ST=BJ,L=BJ,O=CAA,OU=SC,CN=CAA";
    
    /**
     * CA_DEFAULT_SUBJECT
     */
    String CA_DEFAULT_SUBJECT = "C=CN,ST=BJ,L=BJ,O=CAA,OU=SC,CN=";
    
}
