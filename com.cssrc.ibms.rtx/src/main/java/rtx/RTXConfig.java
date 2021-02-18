package rtx;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.XMLProperties;


public class RTXConfig
{
  private XMLProperties jdField_do;
  private final String jdField_for = "app-rtx.xml";
  private String a;
  Logger  logger = Logger.getLogger(RTXConfig.class.getName());
  Document doc = null;
  Element root = null;
  public static RTXConfig cfg = null;
  private static Object jdField_if = new Object();

  public void init()
  { 
    URL localURL = getClass().getClassLoader().getResource("/app-rtx.xml");
    this.a = localURL.getFile();
    this.a = URLDecoder.decode(this.a);
    this.jdField_do = new XMLProperties(this.a);
    SAXBuilder localSAXBuilder = new SAXBuilder();
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(this.a);
      this.doc = localSAXBuilder.build(localFileInputStream);
      this.root = this.doc.getRootElement();
      localFileInputStream.close();
    }
    catch (JDOMException localJDOMException)
    {
    	logger.error("init:" + localJDOMException.getMessage());
    }
    catch (IOException localIOException)
    {
    	logger.error("init:" + localIOException.getMessage()); 
    }
  }

  public Element getRoot()
  {
    return this.root;
  }

  public static RTXConfig getInstance()
  {
    if (cfg == null)
      synchronized (jdField_if)
      {
        cfg = new RTXConfig();
        cfg.init();
      }
    return cfg;
  }

  public static void reload()
  {
    cfg = null;
  }

  public String getProperty(String paramString)
  {
    return CommonTools.Obj2String(this.jdField_do.getProperty(paramString));
  }

  public int getIntProperty(String paramString)
  {
    String str = getProperty(paramString);
    if (StringUtil.isInteger(str))
      return Integer.parseInt(str);
    return -65536;
  }

  public boolean getBooleanProperty(String paramString)
  {
    String str = getProperty(paramString);
    return str.equals("true");
  }

  public void setProperty(String paramString1, String paramString2)
  {
    this.jdField_do.setProperty(paramString1, paramString2);
  }

  public String getProperty(String paramString1, String paramString2, String paramString3)
  {
    return CommonTools.Obj2String(this.jdField_do.getProperty(paramString1, paramString2, paramString3));
  }

  public String getProperty(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return CommonTools.Obj2String(this.jdField_do.getProperty(paramString1, paramString2, paramString3, paramString4));
  }

  public void setProperty(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.jdField_do.setProperty(paramString1, paramString2, paramString3, paramString4);
  }

  public void setProperty(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    this.jdField_do.setProperty(paramString1, paramString2, paramString3, paramString4, paramString5);
  }
}




